package ru.otus.homework_2.calculation;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;
import static ru.otus.homework_2.calculation.MeasurementUtils.isPrimitive;

/**
 * Created by nkargin on 05.02.2018.
 * hei@spark-mail.ru
 */
public class ReflectionSizeCalculator implements ObjectSizeCalculator {

    private static final long HEADER_SIZE = 8L;

    @Override
    public long calculate(Object o) {
        return recursiveCalculateFieldSize(o.getClass()).reduce(HEADER_SIZE, Long::sum);
    }

    private Stream<Long> recursiveCalculateFieldSize(Class<?> aClass) {
        if (isPrimitive(aClass)) {
            return of(aClass).map(Types::getSizeForClass);
        }

        return concat(of(aClass).map(Types::getSizeForClass), of(aClass)
                .map(aClass1 -> Arrays.stream(aClass1.getDeclaredFields()))
                .flatMap(field -> field)
                .filter(field -> !isStatic(field.getModifiers()))
                .flatMap(field -> recursiveCalculateFieldSize(field.getType())));
    }
}
