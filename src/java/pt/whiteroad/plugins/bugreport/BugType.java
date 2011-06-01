package pt.whiteroad.plugins.bugreport;

public enum BugType {

    GENERATED("Gerado pelo sistema"),
    REPORTED("Submetido pelo utilizador");

    private final String str;

    BugType(String str) {
        this.str = str;
    }

    public static BugType getBugType(int index) {
        switch (index) {
            case 1:
                return BugType.GENERATED;
            default:
                return BugType.REPORTED;
        }
    }

    public int getKey() {
        return ordinal();
    }

    public String getValue() {
        return toString();
    }

    public String toString() {
        return str;
    }
    }
