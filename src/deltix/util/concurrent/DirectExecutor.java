package deltix.util.concurrent;

import java.util.concurrent.Executor;

/**
 * Description: deltix.util.concurrent.DirectExecutor
 * Date: Sep 25, 2009
 *
 * @author Nickolay Dul
 */
public class DirectExecutor implements Executor {
    public static final Executor INSTANCE = new DirectExecutor();

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
