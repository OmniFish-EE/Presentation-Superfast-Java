package ee.omnifish.superfast.piranha;

public class Logging {
    public static System.Logger getLoggerForEnclosingClass(Object obj) {
        return System.getLogger(obj.getClass().getEnclosingClass().getName());
    }

    public static System.Logger getLoggerForObject(Object obj) {
        return System.getLogger(obj.getClass().getName());
    }
}
