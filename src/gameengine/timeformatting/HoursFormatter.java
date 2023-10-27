package gameengine.timeformatting;


import static gameengine.timeformatting.TimeConversionFactor.*;

public class HoursFormatter implements Formatter {
    @Override
    public String format(long nanoseconds) {
        String time = "";
        time += Formatter.formatOneTimeStep(nanoseconds, HOUR);
        double remaining = HOUR.remainder(nanoseconds);

        time += Formatter.formatOneTimeStep(remaining, MINUTE);
        remaining = MINUTE.remainder(remaining);

        time += Formatter.formatOneTimeStep(remaining, SECOND, false);

        return time;
    }
}
