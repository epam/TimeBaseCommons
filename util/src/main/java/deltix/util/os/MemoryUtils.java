package deltix.util.os;

import deltix.util.io.IOUtil;
import deltix.util.lang.Util;

import javax.management.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: TurskiyS
 * Date: 4/29/13
 */
public class MemoryUtils {

    public static String getTotalPhysicalMemory() {

        try {
            if (Util.IS_WINDOWS_OS)
                return getTotalPhysicalMemoryWindows();
            else
                return getTotalPhysicalMemoryUnix();

        } catch (IOException | InterruptedException e) {
            Util.LOGGER.log(Level.WARNING, "Error getting total memory", e);
        }

        return null;
    }

    public static String        getTotalPhysicalMemoryWindows() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        try {
            Object attribute = mBeanServer.getAttribute(new ObjectName("java.lang", "type", "OperatingSystem"), "TotalPhysicalMemorySize");
            return attribute != null ? attribute.toString() : null;
        } catch (JMException e) {
            Util.LOGGER.log(Level.WARNING, "Error getting total memory: ", e);
        }

        return null;
    }

//    public static String getTotalPhysicalMemoryWindows(ProcessBuilder pb) throws IOException, InterruptedException {
//        //wmic ComputerSystem get TotalPhysicalMemory
//
//        pb.command().add("wmic");
//        pb.command().add("ComputerSystem");
//        pb.command().add("get");
//        pb.command().add("TotalPhysicalMemory");
//
//        String output = exec(pb, false);
//        if (output != null) {
//            Pattern pattern = Pattern.compile("\\d+");
//            Matcher m = pattern.matcher(output);
//            if (m.find()) {
//                return m.group();
//            }
//        }
//
//        return null;
//    }

    public static String getTotalPhysicalMemoryUnix() throws IOException, InterruptedException {
        //cat /proc/meminfo

        ProcessBuilder pb = new ProcessBuilder();
        pb.redirectErrorStream(true);

        pb.command().add("cat");
        pb.command().add("/proc/meminfo");

        String output = exec(pb, true);
        if (output != null) {
            Pattern pattern = Pattern.compile("(MemTotal.*?)(\\d+)");
            Matcher m = pattern.matcher(output);
            if (m.find()) {
                return m.group(2);
            }
        }

        return null;
    }

    private static String exec(ProcessBuilder pb, boolean needWait)
            throws IOException, InterruptedException {
        String executedCommand = "";
        try {
            final Process proc = pb.start();
            int exitVal = 0;
            if (needWait) {
                exitVal = proc.waitFor();
            }
            String output = IOUtil.readFromStream(proc.getInputStream());

            if (exitVal != 0) {

                for (String commandPart : pb.command()) {
                    executedCommand += commandPart + " ";
                }
                Util.LOGGER.log(Level.SEVERE, String.format("Error while command '%s' was executed:\n%s", executedCommand, output));
            }

            closeProcess(proc);

            return (output);
        } catch (Exception e) {
            for (String commandPart : pb.command()) {
                executedCommand += commandPart + " ";
            }
            Util.LOGGER.log(Level.SEVERE, String.format("Error while command '%s' was executed", executedCommand));
        }
        return null;
    }

    private static void closeProcess(Process process) {

        try {
            Util.close(process.getInputStream());
            Util.close(process.getOutputStream());
            Util.close(process.getErrorStream());
        } catch (Throwable e) {
            Util.LOGGER.log(Level.SEVERE, String.format("Close process excpetion: %s", e.getMessage()));
        }
    }

    public static void main(String[] args) throws Throwable {
        String memory = getTotalPhysicalMemoryWindows();
        System.out.println("Total memory: "+ memory +" B");
    }




}
