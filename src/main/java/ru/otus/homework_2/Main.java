package ru.otus.homework_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by nkargin on 05.02.2018.
 * hei@spark-mail.ru
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final String HAS_SIZE_MESSAGE = "%s instance has size ~= %d";

    public static void main(String[] args) {
        List<String> classNames = Arrays.asList(args);
        if (classNames.isEmpty()) {
            return;
        }

        DefaultInstanceMeasurement defaultInstanceMeasurement = new DefaultInstanceMeasurement();
        defaultInstanceMeasurement.measure(classNames)
                .entrySet()
                .stream()
                .map(entry -> format(HAS_SIZE_MESSAGE, entry.getKey(), entry.getValue()))
                .forEach(log::info);
    }

}
