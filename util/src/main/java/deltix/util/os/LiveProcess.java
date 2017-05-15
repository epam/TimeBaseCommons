package deltix.util.os;

import deltix.util.lang.StringUtils;
import deltix.util.lang.Util;
import java.io.IOException;

public class LiveProcess {
    
    public static LiveProcess get(int pid) throws Exception {
        for (LiveProcess p : list()) {
            if (p.pid == pid) {
                return p;
            }
        }
        return null;
    }
    
    public static LiveProcess get(String cmd) throws Exception {
        for (LiveProcess p : list()) {
            if (StringUtils.matchSignatures(p.cmd, cmd)) {
                return p;
            }
        }
        return null;        
    }
    
    public static LiveProcess[] list() throws Exception {
        
        if (Util.IS_WINDOWS_OS) {
            throw new UnsupportedOperationException("Not implemented for Windows OS yet.");
        }
        
        return LinuxOS.getProcessList();                                   
    }
    
    private final int           pid; 
    private final String        name;
    private final String        cmd;

    public LiveProcess(int pid, String name) {
        this(pid, name, null);
    }
        
    public LiveProcess(int pid, String name, String cmd) {
        this.pid = pid;
        this.name = name;
        this.cmd = cmd;
    }   

    public String getCmd() {
        return cmd;
    }

    public String getName() {
        return name;
    }

    public int getPid() {
        return pid;
    }
     
    public void kill() {
        kill(false);
    }
    
    public void kill(boolean immediately) {
        if (Util.IS_WINDOWS_OS) {
            throw new UnsupportedOperationException("Not implemented yet for Windows OS.");
        }
        
        final StringBuilder out = new StringBuilder("Killing ").append(toString()).append(':').append(Util.NATIVE_LINE_BREAK);
        
        try {
            
            if (immediately) {
                LinuxOS.command(out, "kill", "-9", Integer.toString(pid));
            } else {
                LinuxOS.command(out, "kill", Integer.toString(pid));
            }

        } catch (IOException e) {
            Executor.LOG.fine(out.toString());
        }
    }

    @Override
    public String toString() {
        return "[pid: " + pid + ", name: " + name + ", cmd: " + cmd + ']';
    }
}
