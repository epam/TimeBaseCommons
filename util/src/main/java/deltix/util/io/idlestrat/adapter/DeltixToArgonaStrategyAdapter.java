package deltix.util.io.idlestrat.adapter;

import deltix.util.io.idlestrat.BusySpinIdleStrategy;
import deltix.util.io.idlestrat.IdleStrategy;
import deltix.util.io.idlestrat.YieldingIdleStrategy;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Adapts {@link IdleStrategy} to {@link org.agrona.concurrent.IdleStrategy} interface.
 *
 * @author Alexei Osipov
 */
@ParametersAreNonnullByDefault
public class DeltixToArgonaStrategyAdapter implements org.agrona.concurrent.IdleStrategy {
    private final IdleStrategy wrapped;

    static org.agrona.concurrent.IdleStrategy adapt(IdleStrategy idleStrategy) {
        // Try to find direct match and use "native" implementation if possible.
        Class<? extends IdleStrategy> sClass = idleStrategy.getClass();
        if (sClass.equals(BusySpinIdleStrategy.class)) {
            return new org.agrona.concurrent.BusySpinIdleStrategy();
        } else if (sClass.equals(YieldingIdleStrategy.class)) {
            return new org.agrona.concurrent.YieldingIdleStrategy();
        } else if (sClass.equals(ArgonaToDeltixStrategyAdapter.class)) {
            return ((ArgonaToDeltixStrategyAdapter) idleStrategy).getWrapped();
        } else {
            // Fallback to proxy-based adapter
            return new DeltixToArgonaStrategyAdapter(idleStrategy);
        }
    }

    IdleStrategy getWrapped() {
        return wrapped;
    }

    private DeltixToArgonaStrategyAdapter(IdleStrategy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void idle(int workCount) {
        wrapped.idle(workCount);
    }

    @Override
    public void idle() {
        wrapped.idle();
    }

    @Override
    public void reset() {
        wrapped.reset();
    }
}
