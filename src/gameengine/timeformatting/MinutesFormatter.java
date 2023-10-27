package gameengine.timeformatting;


import static gameengine.timeformatting.TimeConversionFactor.MINUTE;
import static gameengine.timeformatting.TimeConversionFactor.SECOND;

public class MinutesFormatter implements Formatter {
    @Override
    public String format(long nanoseconds) {
        String time = "";
        time += Formatter.formatOneTimeStep(nanoseconds, MINUTE);
        double remaining = MINUTE.remainder(nanoseconds);

        time += Formatter.formatOneTimeStep(remaining, SECOND, false);

        return time;
    }
}
