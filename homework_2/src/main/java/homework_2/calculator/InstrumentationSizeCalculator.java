package homework_2.calculator;

import java.lang.instrument.Instrumentation;

import static java.util.Objects.requireNonNull;

/**
 * Created by nkargin on 09.02.2018.
 * hei@spark-mail.ru
 */
public class InstrumentationSizeCalculator implements ObjectSizeCalculator {

    private static Instrumentation instrumentation;
    private static final String SIZE_MESSAGE = "Instrumentation size of %s equal to %d";

    public static void premain(String args, Instrumentation instrumentation) {
        InstrumentationSizeCalculator.instrumentation = instrumentation;
    }

    @Override
    public long calculate(Object o) {
        requireNonNull(InstrumentationSizeCalculator.instrumentation, "No instrumentation agent available");
        return instrumentation.getObjectSize(o);
    }

    @Override
    public String getMessage() {
        return SIZE_MESSAGE + getUnits();
    }
}
