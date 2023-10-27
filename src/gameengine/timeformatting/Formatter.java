package gameengine.timeformatting;

public interface Formatter {
    String format(long nanoseconds);

    static String formatOneTimeStep(long nanoseconds, TimeConversionFactor timeLevel, boolean addComma) {
        String time;
        if (addComma) {
//            if (timeLevel.convert(nanoseconds) - timeLevel.convert(timeLevel.remainder(nanoseconds)) == 0) {
//                return "";
//            }
            time = (timeLevel.convert(nanoseconds) - timeLevel.convert(timeLevel.remainder(nanoseconds)))
                    + " " + timeLevel.getFormattedName();
        } else {
//            if (timeLevel.convert(nanoseconds) == 0) {
//                return "";
//            }
            time = timeLevel.convert(nanoseconds) + " " + timeLevel.getFormattedName();
        }
        if (timeLevel.convert(nanoseconds) != 1) {
            time += "s";
        }
        if (addComma) {
            time += ", ";
        }
        return time;
    }

    static String formatOneTimeStep(double nanoseconds, TimeConversionFactor timeLevel, boolean addComma) {
        String time;
        if (addComma) {
//            if (timeLevel.convert(nanoseconds) - timeLevel.convert(timeLevel.remainder(nanoseconds)) == 0) {
//                return "";
//            }
            time = (timeLevel.convert(nanoseconds) - timeLevel.convert(timeLevel.remainder(nanoseconds)))
                    + " " + timeLevel.getFormattedName();
        } else {
//            if (timeLevel.convert(nanoseconds) == 0) {
//                return "";
//            }
            time = timeLevel.convert(nanoseconds) + " " + timeLevel.getFormattedName();
        }
        if (timeLevel.convert(nanoseconds) != 1) {
            time += "s";
        }
        if (addComma) {
            time += ", ";
        }
        return time;
    }

    static String formatOneTimeStep(long nanoseconds, TimeConversionFactor timeLevel) {
//        if (timeLevel.convert(nanoseconds) - timeLevel.convert(timeLevel.remainder(nanoseconds)) == 0) {
//            return "";
//        }
        String time = (timeLevel.convert(nanoseconds) - timeLevel.convert(timeLevel.remainder(nanoseconds)))
                + " " + timeLevel.getFormattedName();
        if (timeLevel.convert(nanoseconds) != 1) {
            time += "s";
        }
        time += ", ";
        return time;
    }

    static String formatOneTimeStep(double nanoseconds, TimeConversionFactor timeLevel) {
//        if (timeLevel.convert(nanoseconds) - timeLevel.convert(timeLevel.remainder(nanoseconds)) == 0) {
//            return "";
//        }
        String time = (timeLevel.convert(nanoseconds) - timeLevel.convert(timeLevel.remainder(nanoseconds)))
                + " " + timeLevel.getFormattedName();
        if (timeLevel.convert(nanoseconds) != 1) {
            time += "s";
        }
        time += ", ";
        return time;
    }
}
