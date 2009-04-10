package deltix.util.concurrent;

/*  ##UTILS## */

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 */
public class Test_ThrottlingExecutor {
    private final Random                    random = new Random (2009);
    private BlockingQueue <Runnable>        queue;
    private long                            usedTime;
    
    class TestTask implements Runnable {
        public void         run () {
            long                t1 = System.currentTimeMillis ();
            int                 t = random.nextInt (30) + 15;

            if (t < 20)
                t = 0;
            else
                try {
                    Thread.sleep (t);
                } catch (InterruptedException x) {
                    throw new RuntimeException (x);
                }

            long                dt = System.currentTimeMillis () - t1;

            usedTime += dt;

            if (!Boolean.getBoolean ("quiet"))
                System.out.printf ("%,16d %16d %16d\n", t1, t, dt);
            
            queue.offer (this);
        }
    }

    @Test
    public void             go () throws InterruptedException {
        double                  desiredRatio = 0.07;
        ThrottlingExecutor      exe = new ThrottlingExecutor ("Test", desiredRatio);

        exe.start ();

        queue = exe.getQueue ();

        Thread.sleep (100);

        long            startTime = System.currentTimeMillis ();

        queue.offer (new TestTask ());

        Thread.sleep (10000);

        exe.interrupt ();
        exe.join ();

        double          totalTime = System.currentTimeMillis () - startTime;
        double          actualRatio = usedTime / totalTime;

        if (!Boolean.getBoolean ("quiet"))
            System.out.println (actualRatio);

        assertTrue (
            "Desired ratio: " + desiredRatio +
                " is too different from actual: " + actualRatio,
            Math.abs (actualRatio - desiredRatio) < 0.02
        );
    }
}
