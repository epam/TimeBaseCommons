package deltix.util.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DotnetCompilerHelper {


    private static Class <?>        class_CodeDomProvider;
    private static Class <?>        class_CompilerParameters;
    private static Class <?>        class_CompilerResults;
    private static Class <?>        class_StringCollection;
    private static Class <?>        class_Assembly;
    private static Class <?>        class_CompilerErrorCollection;
    private static Class <?>        class_Array;
    private static Class <?>        class_ICollection;
    private static Class <?>        class_AssemblyName;
    private static Class <?>        class_AppDomain;

    
    private static Method           method_CodeDomProvider_CreateProvider;
    private static Method           method_CodeDomProvider_CompileAssemblyFromSource;
    private static Method           method_StringCollection_Add;
    private static Method           method_Assembly_GetEntryAssembly;
    private static Method           method_Assembly_GetReferencedAssemblies;
    private static Method           method_Assembly_Load;
    private static Method           method_Assembly_ToString;
    private static Method           method_ICollection_CopyTo;
    private static Method           method_AppDomain_GetAssemblies;

    private static Method           property_Assembly_Location_GET;
    private static Method           property_CompilerParameters_ReferencedAssemblies_GET;
    private static Method           property_CompilerParameters_GenerateExecutable_SET;
    private static Method           property_CompilerParameters_GenerateInMemory_SET;
    private static Method           property_CompilerParameters_CompilerOptions_SET;
    private static Method           property_CompilerResults_Errors_GET;
    private static Method           property_CompilerResults_CompiledAssembly_GET;
    private static Method           property_CompilerErrorCollection_HasErrors_GET;
    private static Method           property_CompilerErrorCollection_HasWarnings_GET;
    private static Method           property_ICollection_Count_GET;
    private static Method           property_AppDomain_CurrentDomain_GET;

    private static Constructor      ctor_CompilerParameters;

    private final static Pattern PATTERN_CLASS = Pattern.compile(
            "^[^/]*class\\s*([\\S&&[^\\{\\:]]+)[\\s]{0,1}", Pattern.MULTILINE
    );

    static
    {
        if (IKVMUtil.IS_IKVM) {
            try
            {

                class_AssemblyName = Class.forName ("cli.System.Reflection.AssemblyName");

                class_Assembly = Class.forName ("cli.System.Reflection.Assembly");
                method_Assembly_GetEntryAssembly = class_Assembly.getMethod ("GetEntryAssembly");
                method_Assembly_GetReferencedAssemblies = class_Assembly.getMethod ("GetReferencedAssemblies");
                method_Assembly_ToString = class_Assembly.getMethod ("ToString");
                method_Assembly_Load = class_Assembly.getMethod ("Load", class_AssemblyName);
                property_Assembly_Location_GET = class_Assembly.getMethod ("get_Location");

                class_AppDomain = Class.forName ("cli.System.AppDomain");
                method_AppDomain_GetAssemblies = class_AppDomain.getMethod ("GetAssemblies");
                property_AppDomain_CurrentDomain_GET = class_AppDomain.getMethod ("get_CurrentDomain");
                
                class_StringCollection = Class.forName ("cli.System.Collections.Specialized.StringCollection");
                method_StringCollection_Add = class_StringCollection.getMethod("Add", String.class);

                class_CompilerParameters = Class.forName ("cli.System.CodeDom.Compiler.CompilerParameters");
                ctor_CompilerParameters = class_CompilerParameters.getConstructor();
                property_CompilerParameters_ReferencedAssemblies_GET = class_CompilerParameters.getMethod("get_ReferencedAssemblies");
                property_CompilerParameters_GenerateExecutable_SET = class_CompilerParameters.getMethod("set_GenerateExecutable", boolean.class);
                property_CompilerParameters_GenerateInMemory_SET = class_CompilerParameters.getMethod("set_GenerateInMemory", boolean.class);
                property_CompilerParameters_CompilerOptions_SET = class_CompilerParameters.getMethod("set_CompilerOptions", String.class);

                class_CodeDomProvider = Class.forName ("cli.System.CodeDom.Compiler.CodeDomProvider");
                method_CodeDomProvider_CreateProvider = class_CodeDomProvider.getMethod("CreateProvider", String.class);
                method_CodeDomProvider_CompileAssemblyFromSource = class_CodeDomProvider.getMethod("CompileAssemblyFromSource", class_CompilerParameters, String[].class);

                class_CompilerResults = Class.forName ("cli.System.CodeDom.Compiler.CompilerResults");
                property_CompilerResults_Errors_GET = class_CompilerResults.getMethod("get_Errors");
                property_CompilerResults_CompiledAssembly_GET = class_CompilerResults.getMethod("get_CompiledAssembly");

                class_CompilerErrorCollection = Class.forName ("cli.System.CodeDom.Compiler.CompilerErrorCollection");
                property_CompilerErrorCollection_HasErrors_GET = class_CompilerErrorCollection.getMethod("get_HasErrors");
                property_CompilerErrorCollection_HasWarnings_GET = class_CompilerErrorCollection.getMethod("get_HasWarnings");

                class_Array = Class.forName ("cli.System.Array");
                class_ICollection = Class.forName ("cli.System.Collections.ICollection");
                method_ICollection_CopyTo = class_ICollection.getMethod("CopyTo", class_Array, int.class);
                property_ICollection_Count_GET = class_ICollection.getMethod("get_Count");
                
                
            } catch (Exception ex) {
                throw new RuntimeException (ex);
            }
        }
    }

    public static Object compileAssembly(String code, String fileName) throws DotNetCompilerException
    {
        if (!IKVMUtil.IS_IKVM) {
            throw new UnsupportedOperationException("Dotnet compiler can not be run from java code.");
        }

        try {

            String language = fileName.substring(fileName.lastIndexOf('.') + 1);
            if (!language.equals("cs") && !language.equals("vb"))
                throw new DotNetCompilerException("Unsupported dotnet language: " + language);

            Object provider = method_CodeDomProvider_CreateProvider.invoke(null, language);
            Object parameters = ctor_CompilerParameters.newInstance();
            Object refAssemblies = property_CompilerParameters_ReferencedAssemblies_GET.invoke(parameters);
            Object entryAssembly = method_Assembly_GetEntryAssembly.invoke(null);
            Object entryLocation = property_Assembly_Location_GET.invoke(entryAssembly);
            method_StringCollection_Add.invoke(refAssemblies, (String)entryLocation);

            /*
            Object[] entryAssemblyRefs = (Object[])method_Assembly_GetReferencedAssemblies.invoke(entryAssembly);
            for (Object o : entryAssemblyRefs) {
                Object refAsm = method_Assembly_Load.invoke(null, o);
                if (refAsm != null) {
                    Object refAsmLocation = property_Assembly_Location_GET.invoke(refAsm);
                    method_StringCollection_Add.invoke(refAssemblies, (String)refAsmLocation);
                }
            }
            */
            Object appDomain = property_AppDomain_CurrentDomain_GET.invoke(null);
            Object[] domainAssemblyRefs = (Object[])method_AppDomain_GetAssemblies.invoke(appDomain);
            for (Object o : domainAssemblyRefs) {
                try {
                    Object refAsmLocation = property_Assembly_Location_GET.invoke(o);
                    method_StringCollection_Add.invoke(refAssemblies, (String)refAsmLocation);
                } catch (Throwable t) {
                    IKVMUtil.LOGGER.log(Level.FINE, "Skip loading " + (String)method_Assembly_ToString.invoke(o));
                }
            }

            property_CompilerParameters_GenerateExecutable_SET.invoke(parameters, false);
            property_CompilerParameters_GenerateInMemory_SET.invoke(parameters, true);
            property_CompilerParameters_CompilerOptions_SET.invoke(parameters, "/optimize");

            Object compileResults = method_CodeDomProvider_CompileAssemblyFromSource.invoke(provider, parameters, new String[] { code });
            Object errors = property_CompilerResults_Errors_GET.invoke(compileResults);
            boolean hasErrors = (Boolean)property_CompilerErrorCollection_HasErrors_GET.invoke(errors);
            boolean hasWarnings = (Boolean)property_CompilerErrorCollection_HasWarnings_GET.invoke(errors);
            if (hasErrors || hasWarnings) {
                StringBuilder sb = new StringBuilder();

                int errorsCount = (Integer)property_ICollection_Count_GET.invoke(errors);
                Object[] errorsArray = new Object[errorsCount];
                method_ICollection_CopyTo.invoke(errors, errorsArray, 0);

                for (Object o : errorsArray) {
                    sb.append(o.toString());
                    sb.append(Util.NATIVE_LINE_BREAK);
                }

                throw new DotNetCompilerException(sb.toString());
            }

            Object asm = property_CompilerResults_CompiledAssembly_GET.invoke(compileResults);
            return asm;
        } catch (Throwable t) {
            if (t instanceof DotNetCompilerException)
                throw (DotNetCompilerException) t;
            else
                throw new RuntimeException(t);
        }
    }

    public static Class<?> compile(String code, String fileName) throws DotNetCompilerException
    {
        if (!IKVMUtil.IS_IKVM) {
            throw new UnsupportedOperationException("Dotnet compiler can not be run from java code.");
        }

        try {
            Matcher m = PATTERN_CLASS.matcher(code);
            if (!m.find()) {
                throw new DotNetCompilerException("Code doesn't contain a class definition");
            }

            String className = m.group(1);

            ClassLoader cl = IKVMUtil.createAssemblyClassLoader(compileAssembly(code, fileName));
            Class<?> cls = cl.loadClass("cli." + className);

            return cls;
        } catch (Throwable t) {
            if (t instanceof DotNetCompilerException)
                throw (DotNetCompilerException) t;
            else
                throw new RuntimeException(t);
        }
    }

    public static class DotNetCompilerException extends Exception {

        DotNetCompilerException(String msg) {
            super(msg);
        }

    }
}

