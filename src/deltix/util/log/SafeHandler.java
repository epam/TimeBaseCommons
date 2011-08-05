package deltix.util.log;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import deltix.util.lang.StringUtils;
import deltix.util.lang.Util;
import deltix.util.text.SimpleMessageFormat;
import deltix.util.time.TimerRunner;

/**
 * Description: deltix.util.log.SafeHandler
 * Date: 8/4/11
 *
 * @author Nickolay Dul
 */
public class SafeHandler extends Handler {
    private static final String HANDLERS_CLASSES_PROP    = "handlers";
    private static final String MAX_RECORDS_PER_SEC_PROP = "maxRecordCountPerSecond";
    private static final String PUSH_INTERVAL_PROP       = "pushIntervalMillis";
    private static final String SIMILAR_BUFFER_SIZE_PROP = "similarRecordBufferSize";

    private Handler target;
    private int maxRecordCountPerSecond;

    private RecordBuffer buffer;
    private TimerRunner timerTask;

    private final AtomicLong recordCounter = new AtomicLong(0);
    private volatile boolean isSafeMode;

    private long lastTimerTimestamp;

    public SafeHandler() {
        LogManager manager = LogManager.getLogManager();
        String prefix = getClass().getName() + ".";

        String handlersValue = StringUtils.trim(manager.getProperty(prefix + HANDLERS_CLASSES_PROP));
        if (handlersValue == null)
            throw new IllegalArgumentException("Target handlers should be specified!");

        String[] handlerClasses = handlersValue.split(",");
        Handler[] handlers = new Handler[handlerClasses.length];
        for (int i = 0; i < handlerClasses.length; i++)
            handlers[i] = (Handler) Util.newInstanceNoX(handlerClasses[i].trim());
        Handler target = handlers.length == 1 ? handlers[0] : new CompoundHandler(handlers);

        initialize(target);
    }

    public SafeHandler(Handler target) {
        initialize(target);
    }

    public SafeHandler(Handler target, int maxRecordCountPerSecond, long pushIntervalMillis, int similarRecordBufferSize) {
        initialize(target, maxRecordCountPerSecond, pushIntervalMillis, similarRecordBufferSize);
    }

    private void initialize(Handler target) {
        LogManager manager = LogManager.getLogManager();
        String prefix = getClass().getName() + ".";

        String formatter = StringUtils.trim(manager.getProperty(prefix + "formatter"));
        if (formatter != null)
            target.setFormatter((Formatter) Util.newInstanceNoX(formatter));

        int maxRecordCountPerSecond = parseInt(manager.getProperty(prefix + MAX_RECORDS_PER_SEC_PROP), 1000);
        long pushIntervalMillis = parseLong(manager.getProperty(prefix + PUSH_INTERVAL_PROP), 3000);
        int similarRecordBufferSize = parseInt(manager.getProperty(prefix + SIMILAR_BUFFER_SIZE_PROP), 20);

        setLevel(parseLevel(manager.getProperty(prefix + "level"), Level.ALL));

        initialize(target, maxRecordCountPerSecond, pushIntervalMillis, similarRecordBufferSize);
    }

    private void initialize(Handler target, int maxRecordCountPerSecond, long pushIntervalMillis, int similarRecordBufferSize) {
        this.target = target;
        this.maxRecordCountPerSecond = maxRecordCountPerSecond;

        buffer = new RecordBuffer(similarRecordBufferSize);
        lastTimerTimestamp = System.currentTimeMillis();

        timerTask = new TimerRunner() {
            @Override
            protected void runInternal() throws Exception {
                onTimer();
            }
        };
        Util.GLOBAL_TIMER.scheduleAtFixedRate(timerTask, pushIntervalMillis, pushIntervalMillis);
    }

    private void onTimer() {
        // retrieve record count and reset counter
        long recordCount = recordCounter.getAndSet(0);

        // calculate record frequency per second
        long now = System.currentTimeMillis();
        double actualIntervalSec = (now - lastTimerTimestamp) / 1000d;
        double frequencyPerSec = recordCount / actualIntervalSec;

        // update safe mode flag
        isSafeMode = frequencyPerSec >= maxRecordCountPerSecond;

        // publish record buffer
        buffer.publish(target);

        // update last timer timestamp
        lastTimerTimestamp = now;
    }

    public Handler getTarget() {
        return target;
    }

    @Override
    public void publish(LogRecord record) {
        // increment record counter
        recordCounter.incrementAndGet();
        // publish log record
        if (isSafeMode)
            buffer.add(record);
        else
            target.publish(record);
    }

    @Override
    public void flush() {
        buffer.publish(target);
        target.flush();
    }

    @Override
    public void close() throws SecurityException {
        buffer.publish(target);
        timerTask.cancel();
        target.close();
    }

    private static int parseInt(String value, int defaultValue) {
        value = value != null ? value.trim() : null;
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    private static long parseLong(String value, long defaultValue) {
        value = value != null ? value.trim() : null;
        return value != null ? Long.parseLong(value) : defaultValue;
    }

    private static Level parseLevel(String value, Level defaultLevel) {
        value = value != null ? value.trim() : null;
        return value != null ? Level.parse(value) : defaultLevel;
    }

    ///////////////////////// HELPER CLASSES ////////////////////

    private static final class RecordBuffer {
        private static final int KEY_LENGTH = 40;

        private final int similarRecordBufferSize;

        private Map<String, SimilarRecordBuffer> buffers;

        public RecordBuffer(int similarRecordBufferSize) {
            this.similarRecordBufferSize = similarRecordBufferSize;
            buffers = new HashMap<String, SimilarRecordBuffer>(1000);
        }

        public synchronized void add(LogRecord record) {
            String key = getRecordKey(record);
            SimilarRecordBuffer buffer = buffers.get(key);
            if (buffer == null) {
                buffer = new SimilarRecordBuffer(similarRecordBufferSize);
                buffers.put(key, buffer);
            }
            buffer.add(record);
        }

        public synchronized void publish(Handler target) {
            if (buffers.isEmpty())
                return;

            target.publish(new LogRecord(Level.WARNING, ">>> Start publishing buffered log records"));
            for (SimilarRecordBuffer buffer : buffers.values())
                target.publish(buffer.toRecord());
            target.publish(new LogRecord(Level.WARNING, "<<< Finish publishing buffered log records"));
            buffers.clear();
        }

        private String getRecordKey(LogRecord record) {
            // if exception details exists - use first stack trace line as key
            Throwable thrown = record.getThrown();
            if (thrown != null)
                return thrown.getStackTrace()[0].toString();
            // if record contains only message - use first KEY_LENGTH chars as key
            String message = record.getMessage().trim();
            return message.substring(0, Math.min(message.length(), KEY_LENGTH));
        }
    }

    private static final class SimilarRecordBuffer {
        private final ArrayDeque<LogRecord> buffer;
        private final int bufferSize;
        private long totalCount;

        private SimilarRecordBuffer(int bufferSize) {
            this.bufferSize = bufferSize;
            this.buffer = new ArrayDeque<LogRecord>(bufferSize);
        }

        public void add(LogRecord record) {
            // remove first element from buffer
            if (buffer.size() >= bufferSize)
                buffer.poll();
            // add current record to the end of buffer
            buffer.offer(record);

            // increment total record counter
            totalCount++;
        }

        public LogRecord toRecord() {
            if (buffer.size() == 1)
                return buffer.getFirst();

            StringBuilder sbuf = new StringBuilder(512);
            sbuf.append("There were published ").append(totalCount).append(" records with similar messages.");
            sbuf.append(Util.NATIVE_LINE_BREAK);
            sbuf.append("Here are the last ").append(bufferSize).append(" messages:");

            Throwable thrown = null;
            for (LogRecord record : buffer) {
                sbuf.append(Util.NATIVE_LINE_BREAK);
                sbuf.append('\t');
                // time
                sbuf.append(String.format("%1$tF %1$tT.%1$tL", record.getMillis())).append(' ');
                // message
                String message = record.getMessage ();
                Object[] params = record.getParameters();
                if (params != null)
                    SimpleMessageFormat.format(sbuf, message, params);
                else
                    sbuf.append(message);

                if (thrown == null)
                    thrown = record.getThrown();
            }
            if (thrown != null) {
                sbuf.append(Util.NATIVE_LINE_BREAK);
                sbuf.append(Util.printStackTrace(thrown));
            }
            return new LogRecord(Level.SEVERE, sbuf.toString());
        }
    }
}
