package gameengine.timeformatting;


import static gameengine.timeformatting.TimeConversionFactor.MILLISECOND;

public class MillisecondFormatter implements Formatter {
    @Override
    public String format(long nanoseconds) {
        return Formatter.formatOneTimeStep(nanoseconds, MILLISECOND, false);
    }
}
