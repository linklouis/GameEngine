package gameengine.timeformatting;

import static gameengine.timeformatting.TimeConversionFactor.MICROSECOND;

public class MicrosecondFormatter implements Formatter {
    @Override
    public String format(long nanoseconds) {
        return Formatter.formatOneTimeStep(nanoseconds, MICROSECOND, false);
    }
}
