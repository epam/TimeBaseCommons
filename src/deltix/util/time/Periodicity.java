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

    public static Periodicity      mkRegular(Interval interval) {
        return new Periodicity(Type.REGULAR, interval);
    }

    public static Periodicity      mkIrregular() {
        return new Periodicity(Type.IRREGULAR);
    }

    public static Periodicity      mkStatic() {
        return new Periodicity(Type.STATIC);
    }

    public static Periodicity   parse(String value) {
        if (value.contains(String.valueOf(Type.STATIC)))
            return mkStatic();
        else if (value.contains(String.valueOf(Type.IRREGULAR)))
            return mkIrregular();

        return mkRegular(Interval.valueOf(value));
    }

    @Override
    public String toString() {
        if (interval == null)
            return String.valueOf(type);

        return interval.toString();
    }

    public enum Type {
        REGULAR,
        IRREGULAR,
        STATIC,
    }
}
