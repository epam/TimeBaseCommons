package deltix.util.time;

public final class Periodicity {
    private final Interval        interval;
    private final Type            type;

    Periodicity(Type type) {
        this.type = type;
        this.interval = null;
    }

    Periodicity(Type type, Interval interval) {
        this.interval = interval;
        this.type = type;
    }

    public Interval         getInterval() {
        return interval;
    }

    public Type             getType() {
        return type;
    }

    public Periodicity      mkRegular(Interval interval) {
        return new Periodicity(Type.REGULAR, interval);
    }

    public Periodicity      mkIrregular() {
        return new Periodicity(Type.IRREGULAR);
    }

    public Periodicity      mkStatic() {
        return new Periodicity(Type.STATIC);
    }

    public enum Type {
        REGULAR,
        IRREGULAR,
        STATIC,
    }
}
