package deltix.util.lang;

import java.io.*;
import java.lang.reflect.*;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.LogManager;

public class IKVMUtil {
    public static final Logger LOGGER = Logger.getLogger("deltix.util.lang.ikvm");
    private static final boolean newAssemblyResolutionMethod = ! Boolean.getBoolean("deltix.ikvm.use.old.assembly.resolution");
    public static final boolean     IS_IKVM = isIKVM ();

    public static Class <?>         Int32;
    public static Class <?>         Assembly;
    public static Class <?>         AssemblyClassLoader;
    public static Class <?>         Type;
    public static Class <?>         DateConv;
    public static Class <?>         DateTime;
    public static Class <?>         Util;
    public static Class <?>         AssemblyName;

    private static Field            Int32_m_value;
    private static Method           Assembly_Load_bb;
    private static Method           Assembly_Load_s;
    private static Method           Assembly_get_Location;
    private static Method           Assembly_GetExecutingAssembly;
    private static Method           Assembly_GetEntryAssembly;
    private static Constructor      AssemblyClassLoader_new;
    private static Method           Assembly_LoadFile;
    private static Method           Assembly_LoadFrom;
    private static Method           Assembly_GetExportedTypes;
    private static Method           Assembly_GetName;
    private static Method           Type_get_FullName;
    private static Method           Type_get_Assembly;
    private static Method           Type_IsAssignableFrom;
    private static Method           DateConverter_ToLong;
    private static Method           DateConverter_FromLong;
    private static Method           DateTime_Now;
    private static Method           Util_getInstanceTypeFromClass;
    private static Method           Util_getFriendlyClassFromType;
    private static Method           AssemblyName_get_FullName;
    private static Method           AssemblyName_get_Name;
    private static Method           GC_GetTotalMemory;
    private static Method           AssemblyUtil_PreJITAssemblies;

    private static String           dllLocation = null;

    private static final String[] IKVM_LIB_NAMES = {
        "IKVM.Runtime",
        "IKVM.OpenJDK.Core",
        "IKVM.OpenJDK.Misc",
        "IKVM.OpenJDK.Util",
        "IKVM.OpenJDK.Jdbc",
        "IKVM.OpenJDK.Text",
        "IKVM.OpenJDK.Beans",
        "IKVM.OpenJDK.XML.API",
        "IKVM.OpenJDK.XML.Bind",
        "IKVM.OpenJDK.XML.Crypto",
        "IKVM.OpenJDK.XML.Parse",
        "IKVM.OpenJDK.XML.Transform",
        "IKVM.OpenJDK.XML.WebServices",
        "IKVM.OpenJDK.XML.XPath",
        "IKVM.OpenJDK.Remoting",
        "IKVM.OpenJDK.Charsets",
        "IKVM.OpenJDK.Security",
        "IKVM.OpenJDK.Management",
        "IKVM.OpenJDK.SwingAWT"
    };

    private static boolean          isIKVM () {
        return System.getProperty("java.vendor.url").contains("ikvm");
    }

    static {
        if (IS_IKVM) {
            try {
                Int32 = Class.forName ("cli.System.Int32");
                Int32_m_value = Int32.getDeclaredField ("m_value");
                Int32_m_value.setAccessible (true);

                AssemblyName = Class.forName ("cli.System.Reflection.AssemblyName");
                AssemblyName_get_FullName = AssemblyName.getMethod ("get_FullName");
                AssemblyName_get_Name = AssemblyName.getMethod ("get_Name");

                Assembly = Class.forName ("cli.System.Reflection.Assembly");
                Assembly_Load_bb = Assembly.getMethod ("Load", byte [].class, byte [].class);
                Assembly_Load_s = Assembly.getMethod ("Load", String.class);
                Assembly_LoadFile = Assembly.getMethod ("LoadFile", String.class);
                Assembly_LoadFrom = Assembly.getMethod ("LoadFrom", String.class);
                Assembly_GetExportedTypes = Assembly.getMethod ("GetExportedTypes");
                Assembly_get_Location = Assembly.getMethod ("get_Location");
                Assembly_GetExecutingAssembly = Assembly.getMethod ("GetExecutingAssembly");
                Assembly_GetEntryAssembly = Assembly.getMethod ("GetEntryAssembly");
                Assembly_GetName = Assembly.getMethod ("GetName");

                Class<?> GC = Class.forName("cli.System.GC");
                GC_GetTotalMemory = GC.getMethod("GetTotalMemory", boolean.class);

                Type = Class.forName ("cli.System.Type");
                Type_get_FullName = Type.getMethod ("get_FullName");
                Type_get_Assembly = Type.getMethod ("get_Assembly");
                Type_IsAssignableFrom = Type.getMethod ("IsAssignableFrom", Type);

                AssemblyClassLoader = Class.forName ("ikvm.runtime.AssemblyClassLoader");
                AssemblyClassLoader_new = AssemblyClassLoader.getConstructor (Assembly);

                DateTime = Class.forName ("cli.System.DateTime");
                DateTime_Now = DateTime.getMethod ("get_Now");

                Util = Class.forName ("ikvm.runtime.Util");
                Util_getInstanceTypeFromClass = Util.getMethod ("getInstanceTypeFromClass", Class.class);
                Util_getFriendlyClassFromType = Util.getMethod("getFriendlyClassFromType", Type);
            } catch (Exception x) {
                throw new RuntimeException (x);
            }
        }
    }

    public static String getPlatformInfo() {
        return (IKVMUtil.IS_IKVM ? "NET" : "JAVA") + (deltix.util.lang.Util.IS32BIT ? "32" : (deltix.util.lang.Util.IS64BIT ? "64" : ""));
    }

    public static String[] getIKVMLibs() {
        return IKVM_LIB_NAMES.clone();
    }

    /**
     *  Call this method in order to supply alternative DLL load location to
     *  the default DELTIX_HOME\build\dotnet
     */
    public static synchronized void     setDLLLocation (String location) {
        dllLocation = location;
    }

    private static synchronized void initDXUtil () {
        if (DateConv != null)
            return;

        try {
            ClassLoader cl = IKVMUtil.createAssemblyClassLoader(IKVMUtil.loadAssembly("dxutil"));

            DateConv = cl.loadClass ("cli.deltix.util.time.DateConverter");
            DateConverter_ToLong = DateConv.getMethod ("ToLong", DateTime);
            DateConverter_FromLong = DateConv.getMethod ("FromLong", long.class);

            Class<?> assemblyUtil = cl.loadClass("cli.deltix.util.assembly.Util");
            AssemblyUtil_PreJITAssemblies = assemblyUtil.getMethod("PreJITAssemblies", String.class);
        } catch (Exception x) {
            throw new RuntimeException ("Failed to initialize dxutil.dll", x);
        }
    }

    /**
     * Pre-JIT specified assemblies types according to pattern.
     * @param prejitPattern patten to specify desired assemblies and types: ASSEMBLY_FILE_NAME[:ACCEPTED_TYPES_REGEXP]
     * Example: qsc.dll;uhfs.dll:deltix.qsrv\.hf\.pub\.trade\..+
     *          (all types from qsc.dll and all types in deltix.qsrv.hf.pub.trade namespace from uhfs.dll)
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void preJITAssemblies(String prejitPattern) throws IllegalAccessException, InvocationTargetException {
        initDXUtil ();
        AssemblyUtil_PreJITAssemblies.invoke(null, prejitPattern);
    }


    //


    /**
     *  Allows IKVM to perform necessary magic when this method is called.
     *  @param obj  Native dot Net object.
     *  @return The object's class loader.
     */
    public static Class         getClass (Object obj) {
        return (obj.getClass ());
    }

    public static Object        getNow ()
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return (DateTime_Now.invoke (null));
    }

    public static long          dateTimeToLong (Object dateTime)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        initDXUtil ();
        return ((Long) DateConverter_ToLong.invoke (null, dateTime));
    }

    public static Object        longToDateTime (long t)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        initDXUtil ();
        return (DateConverter_FromLong.invoke (null, t));
    }

    public static Object        getTypeFromClass (Class <?> cls)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return (Util_getInstanceTypeFromClass.invoke (null, cls));
    }

    public static Class <?>     getClassFromType (Object type)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return ((Class <?>) Util_getFriendlyClassFromType.invoke (null, type));
    }

    public static String        getTypeName (Object type)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return ((String) Type_get_FullName.invoke (type));
    }

    public static Object        getTypeAssembly (Object type)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return (Type_get_Assembly.invoke (type));
    }

    public static boolean       isAssignableFrom (Object type, Object fromType)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return ((Boolean) Type_IsAssignableFrom.invoke (type, fromType));
    }

    public static boolean       isIKVMDynamicAssemblyName (String name) {
        return (name.startsWith ("ikvm_dynamic_assembly"));
    }

    public static String        getAssemblyName (Object assembly)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return ((String) AssemblyName_get_Name.invoke (Assembly_GetName.invoke (assembly)));
    }

    public static String        getAssemblyNameFromClass (Class <?> cls)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return (getAssemblyName (getTypeAssembly (getTypeFromClass (cls))));
    }

    public static String        getAssemblyFullName (Object assembly)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return ((String) AssemblyName_get_FullName.invoke (Assembly_GetName.invoke (assembly)));
    }

    public static Object []     getExportedTypes (Object assembly)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return ((Object []) Assembly_GetExportedTypes.invoke (assembly));
    }

    public static String        getAssemblyLocation (Object assembly)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return ((String) Assembly_get_Location.invoke (assembly));
    }

    public static File          getAssemblyLocationFile (Object assembly)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return (new File (getAssemblyLocation (assembly)));
    }

    public static Object        getExecutingAssembly ()
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return (Assembly_GetExecutingAssembly.invoke (null));
    }

    public static Object        getEntryAssembly ()
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return (Assembly_GetEntryAssembly.invoke (null));
    }

    public static Object        loadAssembly (byte [] obj, byte [] pdb)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return (Assembly_Load_bb.invoke (null, obj, pdb));
    }

    public static Object        loadAssembly (String name)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return (Assembly_Load_s.invoke (null, name));
    }

    public static Object        loadAssemblyFile (File path)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return (loadAssemblyFile (path.getPath ()));
    }

    public static Object        loadAssemblyFile (String path)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        try {
            return (Assembly_LoadFile.invoke (null, path));
        } catch (Throwable e) {
            throw new deltix.util.io.UncheckedIOException("Failed to load assembly \"" + path + "\": " + e.getMessage(), e);
        }
    }

    public static Object        loadAssemblyFrom (File path)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        return (loadAssemblyFrom (path.getPath ()));
    }

    public static Object        loadAssemblyFrom (String path)
        throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        try {
            return (Assembly_LoadFrom.invoke (null, path));
        } catch (Throwable e) {
            throw new deltix.util.io.UncheckedIOException("Failed to load assembly \"" + path + "\": " + e.getMessage(), e);
        }
    }

    public static ClassLoader   createAssemblyClassLoader (Object assembly)
        throws InstantiationException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException
    {
        return ((ClassLoader) AssemblyClassLoader_new.newInstance (assembly));
    }

    public static Object        mkInt32 (int value)
        throws InstantiationException, IllegalAccessException
    {
        Object      ret = Int32.newInstance ();
        Int32_m_value.set (ret, value);
        return (ret);
    }

    public static Object        mkEnumConstant (Class <?> cls, int value) {
        try {
            return (cls.getDeclaredMethod ("wrap", int.class).invoke (null, value));
        } catch (NoSuchMethodException x) {
            throw new RuntimeException (cls + " is not an IKVM enum", x);
        } catch (Exception x) {
            throw new RuntimeException ("Error wrapping " + value, x);
        }
    }
    
    public static void          assertIkvmRuntime() {
        if (!IS_IKVM)
            throw new IllegalStateException ("You must be running under IKVM (not JVM) to load .NET assemblies");
    }

    /**
     * Gets <b>Debugger.IsAttached</b> property via reflection.
     * <p>
     * Always returns <code>false</code> if code is not run under IKVM. 
     * </p>
     * @return <code>true</code> if C# debugger controls the process. 
     */
    public static boolean isDebugged() {
        if (!IS_IKVM)
            return false;

        try {
            final Class<?> clazz = Class.forName("cli.System.Diagnostics.Debugger");
            final Method method = clazz.getDeclaredMethod("get_IsAttached");
            return (Boolean) method.invoke(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static long getGCTotalMemory(boolean forceFullCollection) {
        try {
            return (Long) GC_GetTotalMemory.invoke(null, forceFullCollection);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addLogHandler (Handler handler) {
         Logger root = LogManager.getLogManager().getLogger("");
         root.addHandler(handler);
    }
}
