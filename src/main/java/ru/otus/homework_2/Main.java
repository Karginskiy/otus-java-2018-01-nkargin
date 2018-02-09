package ru.otus.homework_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework_2.calculator.InstrumentationSizeCalculator;
import ru.otus.homework_2.calculator.ObjectSizeCalculator;
import ru.otus.homework_2.calculator.ReflectionSizeCalculator;
import ru.otus.homework_2.calculator.SerializationSizeCalculator;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by nkargin on 05.02.2018.
 * hei@spark-mail.ru
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final String HAS_SIZE_MESSAGE = "%s empty instance has size ~= %d";
    private List<ObjectSizeCalculator> calculators = new ArrayList<ObjectSizeCalculator>() {{
        add(new ReflectionSizeCalculator());
        add(new InstrumentationSizeCalculator());
        add(new SerializationSizeCalculator());
    }};

    public static void main(String[] args) {
        Main main = new Main();
        getSizeForArray(main);
        getSizeForPrimitiveWrappers(main);
        getSizeChangingForList(main);
    }

    private static void getSizeForArray(Main main) {
        calculateWithDifferentCalculator(main, new String[] {"123", "125555", "LOL"});
    }

    private static void getSizeForPrimitiveWrappers(Main main) {
        calculateWithDifferentCalculator(main, 1d);
        calculateWithDifferentCalculator(main, 1);
        calculateWithDifferentCalculator(main, 1L);
        calculateWithDifferentCalculator(main, 1f);
        calculateWithDifferentCalculator(main, '1');
        calculateWithDifferentCalculator(main, (byte) 1);
        calculateWithDifferentCalculator(main, (short) 1);
    }

    private static void getSizeChangingForList(Main main) {
        List<String> list = new ArrayList<>();
        calculateWithDifferentCalculator(main, list);
        list.add("125");
        calculateWithDifferentCalculator(main, list);
        list.add("126");
        calculateWithDifferentCalculator(main, list);
        list.add("127");
        calculateWithDifferentCalculator(main, list);
    }

    private static void calculateWithDifferentCalculator(Main main, Object o) {
        main.calculators.forEach(calculator -> log.info(
                format(calculator.getMessage(), o.getClass(), calculator.calculate(o))
        ));

        log.info("\n");
    }


}
