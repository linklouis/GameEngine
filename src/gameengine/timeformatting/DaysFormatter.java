package gameengine.timeformatting;

import static gameengine.timeformatting.TimeConversionFactor.*;

public class DaysFormatter implements Formatter {
    @Override
    public String format(long nanoseconds) {
        String time = "";
        time += Formatter.formatOneTimeStep(nanoseconds, DAY);
        double remaining = DAY.remainder(nanoseconds);
        System.out.println(HOUR.convert(remaining));

        time += Formatter.formatOneTimeStep(remaining, HOUR);
        remaining = HOUR.remainder(remaining);

        time += Formatter.formatOneTimeStep(remaining, MINUTE, false);

        return time;
    }
}
