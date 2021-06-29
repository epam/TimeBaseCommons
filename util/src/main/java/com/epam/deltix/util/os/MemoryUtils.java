/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.os;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.util.io.IOUtil;
import com.epam.deltix.util.lang.Util;

import javax.management.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: TurskiyS
 * Date: 4/29/13
 */
public class MemoryUtils {
    private static final Log LOG = LogFactory.getLog(MemoryUtils.class);

    public static String getTotalPhysicalMemory() {

        try {
            if (Util.IS_WINDOWS_OS)
                return getTotalPhysicalMemoryWindows();
            else
                return getTotalPhysicalMemoryUnix();

        } catch (IOException | InterruptedException e) {
            LOG.error("Error getting total memory %s").with(e);
        }

        return null;
    }

    public static String        getTotalPhysicalMemoryWindows() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        try {
            Object attribute = mBeanServer.getAttribute(new ObjectName("java.lang", "type", "OperatingSystem"), "TotalPhysicalMemorySize");
            return attribute != null ? attribute.toString() : null;
        } catch (JMException e) {
            LOG.error("Error getting total memory %s").with(e);
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
                LOG.error("Error while command '%s' was executed:\n%s").with(executedCommand).with(output);
            }

            closeProcess(proc);

            return (output);
        } catch (Exception e) {
            for (String commandPart : pb.command()) {
                executedCommand += commandPart + " ";
            }
            LOG.error("Error while command '%s' was executed").with(executedCommand);
        }
        return null;
    }

    private static void closeProcess(Process process) {

        try {
            Util.close(process.getInputStream());
            Util.close(process.getOutputStream());
            Util.close(process.getErrorStream());
        } catch (Throwable e) {
            LOG.error("Close process exception: %s").with(e.getMessage());
       }
    }

    public static void main(String[] args) throws Throwable {
        String memory = getTotalPhysicalMemoryWindows();
        System.out.println("Total memory: "+ memory +" B");
    }




}