package deltix.util;

public final class JUnitCategories {
    public interface Utils extends All {}

    public interface UHFFramework extends All {}

    public interface TickDB extends All {}

    public interface TickDBFast extends TickDB {}

    public interface TickDBQQL extends TickDB {}

    public interface TickDBSlow extends TickDBStress {}

    public interface TickDBStress {}

    public interface RAMDisk extends All {}

    public interface UHFUtils extends All {}

    public interface All {}
}

