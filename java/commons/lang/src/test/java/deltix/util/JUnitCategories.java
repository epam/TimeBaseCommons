package deltix.util;

/**
 * Note: If you choose TickDB category all child categories will be executed
 * When you want to group your categories you should specify children names like ${parent_name}${some_extension} (e.g. TickDB)
 */
public final class JUnitCategories {
    public interface Utils {}

    public interface UHFFramework {}

    public interface TickDB {}

    public interface TickDBFast extends TickDB {}

    public interface TickDBQQL extends TickDB {}

    public interface TickDBSlow extends TickDB {}

    public interface TickDBStress extends TickDB {}

    public interface RAMDisk {}

    public interface UHFUtils {}

    //Internal
    public interface NonParallel {}
}


