package ir.rasen.myapplication.helper;

/**
 * Created by android on 12/16/2014.
 */
public enum Sex {
    NONE, MALE, FEMALE;


    public static String getName(Sex sex) {
        switch (sex) {
            case NONE:
                return "none";
            case MALE:
                return "male";
            case FEMALE:
                return "female";
        }
        return "none";
    }

    public static Sex getSex(String sex) {
        if (sex.equals(getName(NONE)))
            return NONE;
        else if (sex.equals(getName(MALE)))
            return MALE;
        else if (sex.equals(getName(FEMALE)))
            return FEMALE;
        return NONE;

    }
}
