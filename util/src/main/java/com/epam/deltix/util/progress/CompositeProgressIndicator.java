package com.epam.deltix.util.progress;

/**
*
*/
public class CompositeProgressIndicator implements MsgProgressIndicator {

    double totalWork = 0;
    MsgProgressIndicator listener;

    public CompositeProgressIndicator(MsgProgressIndicator listener) {
        this.listener = listener;
    }

    @Override
    public void message(final String text) {
        listener.message(text);
    }

    @Override
    public void setTotalWork(double v) {
        totalWork += v;
        listener.setTotalWork(totalWork);
    }

    @Override
    public void setWorkDone(double v) {
        listener.incrementWorkDone(v);
    }

    @Override
    public void incrementWorkDone(double inc) {
        listener.incrementWorkDone(inc);
    }

    void reset() {
        totalWork = 0;
        listener.setTotalWork(0);
    }
}
