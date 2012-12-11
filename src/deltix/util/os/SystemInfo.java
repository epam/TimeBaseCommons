package deltix.util.os;

/**
 *
 */
public class SystemInfo {

    public static final boolean  IS_WINDOWS      = System.getProperty ("path.separator").equals(";");

    public static String            getUserProfile() {
        return IS_WINDOWS ?
                System.getenv("USERPROFILE") :
                System.getProperty("user.home");
    }

    public static String            getAppData() {
        return IS_WINDOWS ? System.getenv("APPDATA") : getUserProfile();
    }
    
    public static void main(String[] args) {
        System.out.println("User profile: " + getUserProfile());
        System.out.println("AppData: " + getAppData());
    }
}
