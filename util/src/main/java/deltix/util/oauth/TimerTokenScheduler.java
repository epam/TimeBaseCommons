package deltix.util.oauth;

import java.util.Timer;
import java.util.TimerTask;

class TimerTokenScheduler implements RefreshTokenScheduler {

    private final Timer timer;

    TimerTokenScheduler(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void schedule(long timestampMs, Runnable task) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        }, timestampMs);
    }

    @Override
    public void close() {
    }
}
