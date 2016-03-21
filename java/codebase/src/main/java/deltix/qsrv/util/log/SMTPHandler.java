package deltix.qsrv.util.log;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMultipart;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.Properties;
import java.util.Date;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import deltix.util.lang.Filter;
import deltix.util.concurrent.DirectExecutor;
import deltix.util.lang.Util;
import deltix.util.log.TerseFormatter;
import deltix.util.text.SimpleMessageFormat;

public class SMTPHandler extends Handler {

    private static final int MAX_SUBJECT_LEN = Util.getIntSystemProperty("QuantServer.alerts.maxSubjectLength", 64, 10, 1000);
    private static final int DEFAULT_SMTP_TIMEOUT = 60000;

    private static final Level DEFAULT_LEVEL = Level.INFO;
    private static final Formatter DEFAULT_FORMATTER = new TerseFormatter();
    private static final int DEFAULT_BUFFER_SIZE = 128;

    private static final Level DEFAULT_PUSH_LEVEL = Level.SEVERE;
    private static final int DEFAULT_GRACEFUL_PERIOD = 30 * 60000;
    private static final int DEFAULT_MAX_EMAIL_COUNT = 5;

    private static final int DEFAULT_ERROR_CODE = 1;

    private static final String PREFIX = SMTPHandler.class.getName();

    private Executor executor;

    private String to;
    private String from;
    private String subject;
    private String smtpHost;
    private int smtpPort;
    private boolean smtpSecure;
    private String smtpUsername;
    private String smtpPassword;

    private Level pushLevel;
    private long gracefulPeriod;
    private int maxEmailCount;

    private int bufferSize;
    private LogRecord buffer[];
    int start, headIndex;

    private Filter<LogRecord> trigger;

    private int smtpTimeout;
    private boolean debug;

    public SMTPHandler() {
        this(DEFAULT_PUSH_LEVEL, DEFAULT_GRACEFUL_PERIOD, DEFAULT_MAX_EMAIL_COUNT);
    }

    public SMTPHandler(Level defaultPushLevel, long defaultGracefulPeriod, int defaultMaxEmailCount) {
        LogManager manager = LogManager.getLogManager();

        // set handler level
        setLevel(parseLevel(manager.getProperty(PREFIX + ".level"), DEFAULT_LEVEL));

        // set handler formatter
        String formatterClassName = manager.getProperty(PREFIX + ".formatter");
        setFormatter((Formatter) instantiateByClassName(formatterClassName, DEFAULT_FORMATTER));

        setTo(getProperty(manager, "to", null));
        setFrom(getProperty(manager, "from", null));
        setSmtpHost(getProperty(manager, "smtpHost", null));
        setSmtpPort(parseInt(getProperty(manager, "smtpPort", null), 0));
        setSmtpSecure(Boolean.valueOf(getProperty(manager, "smtpSecure", "false")));
        setSmtpUsername(getProperty(manager, "smtpUsername", null));
        setSmtpPassword(getProperty(manager, "smtpPassword", null));
        setSubject(getProperty(manager, "subject", null));

        setSmtpTimeout(parseInt(getProperty(manager, "smtpTimeout", null), DEFAULT_SMTP_TIMEOUT));
        setDebug(Boolean.valueOf(getProperty(manager, "debug", "false")));

        boolean syncSend = Boolean.valueOf(getProperty(manager, "sync", "false"));
        executor = syncSend ? DirectExecutor.INSTANCE : Executors.newSingleThreadExecutor();

        pushLevel = parseLevel(getProperty(manager, "pushLevel", null), defaultPushLevel);
        gracefulPeriod = parseLong(getProperty(manager, "gracefulPeriod", null), defaultGracefulPeriod);
        maxEmailCount = parseInt(getProperty(manager, "maxEmailCount", null), defaultMaxEmailCount);

        setTriggerClass(getProperty(manager, "triggerClass", null));

        // set buffer size and create buffer
        setBufferSize(parseInt(getProperty(manager, "bufferSize", null), DEFAULT_BUFFER_SIZE));
    }

    ///////////////////////////// Handler IMPL /////////////////////////

    /**
     * Perform SMTPHandler specific appending actions, mainly adding
     * the record to a cyclic buffer and checking if the record triggers an e-mail to be sent.
     */
    public synchronized void publish(LogRecord record) {
        if (record == null || !isLoggable(record))
            return;

        headIndex = headIndex % bufferSize;
        buffer[headIndex] = record;
        // increment next index
        headIndex++;

        if (trigger.accept(record)) {
            // send email
            doSendBuffer(Arrays.copyOf(buffer, headIndex));
            // cleanup buffer
            headIndex = 0;
        }
    }

    public void close() {
        if (executor instanceof ExecutorService)
            ((ExecutorService) executor).shutdownNow();
    }


    public void flush() {
        // do nothing
    }

    //////////////////////////// HANDLER PROPERTIES ////////////////////////

    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * The <b>bufferSize</b> option takes a positive integer
     *  representing the maximum number of logging records to collect in a cyclic buffer.
     * When the <code>bufferSize</code> is reached, oldest records are deleted as new records are added to the buffer.
     * By default the size of the cyclic buffer is 512 records.
     */
    public synchronized void setBufferSize(int bufferSize) {
        if (this.bufferSize == bufferSize)
            return;

        this.bufferSize = bufferSize;
        buffer = buffer == null ? new LogRecord[bufferSize] :  Arrays.copyOf(buffer, bufferSize);
        headIndex = Math.min(headIndex, bufferSize - 1);
    }

    /**
     * Returns value of the <b>TriggerClass</b> option.
     */
    public String getTriggerClass() {
        return (trigger == null) ? null : trigger.getClass().getName();
    }

    /**
     * The <b>triggerClass</b> option takes a string value
     * representing the name of the class implementing the {@link Filter<LogRecord>} interface.
     * A corresponding object will be instantiated and assigned
     * as the triggering record evaluator for the SMTPHandler.
     */
    @SuppressWarnings("unchecked")
    public void setTriggerClass(String value) {
        trigger = (Filter<LogRecord>) instantiateByClassName(value, new DefaultTrigger(pushLevel, gracefulPeriod, maxEmailCount));
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Level getPushLevel() {
        return pushLevel;
    }

    public long getGracefulPeriod() {
        return gracefulPeriod;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public boolean isSmtpSecure() {
        return smtpSecure;
    }

    public void setSmtpSecure(boolean smtpSecure) {
        this.smtpSecure = smtpSecure;
    }

    public int getSmtpTimeout() {
        return smtpTimeout;
    }

    public void setSmtpTimeout(int smtpTimeout) {
        this.smtpTimeout = smtpTimeout;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String value) {
        this.smtpPassword = value;
    }

    public String getSmtpUsername() {
        return smtpUsername;
    }

    public void setSmtpUsername(String value) {
        this.smtpUsername = value;
    }

    /////////////////////////////// EMAIL SENDING ////////////////////////

    protected void assertConfigValid() {
        if (trigger == null)
            throw new IllegalStateException("No Trigger is set for handler [" + PREFIX + "].");
        if (to == null || to.isEmpty())
            throw new IllegalStateException("No 'To' email address set for handler [" + PREFIX + "].");
    }

    protected InternetAddress getAddress(String addressStr) {
        try {
            return new InternetAddress(addressStr);
        } catch (AddressException e) {
            reportError("Could not parse address [" + addressStr + "].", e, DEFAULT_ERROR_CODE);
            return null;
        }
    }

    protected InternetAddress[] parseAddress(String addressStr) {
        try {
            return InternetAddress.parse(addressStr, true);
        } catch (AddressException e) {
            reportError("Could not parse address [" + addressStr + "].", e, DEFAULT_ERROR_CODE);
            return new InternetAddress[0];
        }
    }

    protected void doSendBuffer(final LogRecord[] recordBuffer) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                sendBuffer(recordBuffer);
            }
        });
    }

    /**
     * Send the contents of the cyclic buffer as an e-mail message.
     */
    protected void sendBuffer(LogRecord[] recordBuffer) {
        // check configuration
        assertConfigValid();

        Properties props = new Properties(System.getProperties());

        if (smtpSecure)
            props.put("mail.smtp.starttls.enable", "true");

        if (smtpTimeout > 0)
            props.put("mail.smtp.timeout", smtpTimeout);

        if (smtpHost != null)
            props.put("mail.smtp.host", smtpHost);
        if (smtpPort > 0)
            props.put("mail.smtp.port", smtpPort);

        Authenticator auth = null;
        if (smtpUsername != null) {
            auth = new UsernamePasswordAuthenticator(smtpUsername, smtpPassword);
            props.put("mail.smtp.user", smtpUsername);
            props.put("mail.smtp.auth", "true");
        }

        Session session = Session.getInstance(props, auth);
        session.setDebug(debug);

        MimeMessage message = new MimeMessage(session);

        try {
            if (from != null)
                message.setFrom(getAddress(from));
            else
                message.setFrom();

            message.setRecipients(Message.RecipientType.TO, parseAddress(to));

            message.setSubject(getSubject(subject, recordBuffer[recordBuffer.length - 1], MAX_SUBJECT_LEN));

            MimeBodyPart part = new MimeBodyPart();

            StringBuffer sbuf = new StringBuffer();

            Formatter formatter = getFormatter();
            String head = formatter.getHead(this);
            if (head != null)
                sbuf.append(head);

            for (LogRecord record : recordBuffer) {
                sbuf.append(formatter.format(record));
            }

            String tail = formatter.getTail(this);
            if (tail != null)
                sbuf.append(tail);

            part.setContent(sbuf.toString(), getEmailContentType());

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(part);
            message.setContent(mp);

            message.setSentDate(new Date());

            // do send message
            Transport.send(message);
        } catch (Exception ex) {
            reportError("Email send failure: " + ex.getMessage(), ex, DEFAULT_ERROR_CODE);
        }
    }

    private static String getSubject(String prefix, LogRecord record, int maxLength) {
        String message = record.getMessage ();
        Object [] params = record.getParameters();
        if (params != null) {
            StringBuilder result = new StringBuilder (256);
            SimpleMessageFormat.format(result, message, params);
            result.setLength(maxLength);
            message = result.toString();
        }

        // use only first line in message
        int index = message.indexOf('\n');
        if (index >= 0)
            message = message.substring(0, index);
        // add prefix if any
        message = prefix != null ? prefix + ": " + message : message;
        // cut off message if needed
        return message.substring(0, Math.min(maxLength, message.length()));
    }

    protected String getEmailContentType() {
        return "text/plain";
    }

    protected static Object instantiateByClassName(String className, Object defaultObj) {
        if (className == null)
            return defaultObj;

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class clazz = loader.loadClass(className);
            return clazz.newInstance();
        } catch (Exception ex) {
            return defaultObj;
        }
    }

    protected static String getProperty(LogManager manager, String key, String defaultValue) {
        String value = manager.getProperty(PREFIX + "." + key);
        if (value == null)
            return defaultValue;
        value = value.trim();
        if (value.isEmpty())
            return defaultValue;
        return value;
    }

    protected static Level parseLevel(String level, Level defaultLevel) {
        if (level == null)
            return defaultLevel;
        try {
            return Level.parse(level);
        } catch (IllegalArgumentException e) {
            return defaultLevel;
        }
    }

    protected static long parseLong(String value, long defaultValue) {
        if (value == null)
            return defaultValue;
        try {
            return Long.parseLong(value);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    protected static int parseInt(String value, int defaultValue) {
        if (value == null || value.isEmpty())
            return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    ///////////////////////// HELPER CLASSES /////////////////////

    public static final class UsernamePasswordAuthenticator extends Authenticator {
        private PasswordAuthentication auth = null;

        public UsernamePasswordAuthenticator(String user, String password) {
            auth = new PasswordAuthentication(user, password);
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return auth;
        }
    }

    public static class DefaultTrigger implements Filter<LogRecord> {
        private final Level pushLevel;
        private final long gracefulPeriodMillis;
        private final int maxEmailCount;

        private long gracefulPeriodStartTime;
        private int counter;

        public DefaultTrigger(Level pushLevel, long gracefulPeriodMillis, int maxEmailCount) {
            this.pushLevel = pushLevel;
            this.gracefulPeriodMillis = gracefulPeriodMillis;
            this.maxEmailCount = maxEmailCount;
        }

        public boolean accept(final LogRecord record) {
            if (record.getLevel() == null || record.getLevel().intValue() < pushLevel.intValue())
                return false;

            if (gracefulPeriodMillis <= 0) // skip additional checks
                return true;

            long current = System.currentTimeMillis();
            if (current - gracefulPeriodStartTime <= gracefulPeriodMillis) { // we are within graceful period
                if (maxEmailCount <= 0 || ++counter >= maxEmailCount)
                    return false;
            } else { // graceful period expired
                gracefulPeriodStartTime = current;
                counter = 0; // reset email counter
            }

            return true;
        }
    }
}
