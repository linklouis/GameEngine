package gameengine.timeformatting;


import static gameengine.timeformatting.TimeConversionFactor.NANOSECOND;

public class NanosecondFormatter implements Formatter {
    @Override
    public String format(long nanoseconds) {
        return Formatter.formatOneTimeStep(nanoseconds, NANOSECOND, false);
    }
}
