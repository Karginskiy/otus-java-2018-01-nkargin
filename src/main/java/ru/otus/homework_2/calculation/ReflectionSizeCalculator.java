package ru.otus.homework_2.calculation;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.reflect.Modifier.isStatic;
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
        Long rawSize = Arrays.stream(o.getClass().getDeclaredFields())
                .map(Field::getType)
                .flatMap(this::recursiveCalculateFieldSize)
                .reduce(HEADER_SIZE, Long::sum);
        return rawSize % 8 != 0 ? setAlignment(rawSize) : rawSize;
    }

    private Stream<Long> recursiveCalculateFieldSize(Class<?> aClass) {
        if (isPrimitive(aClass)) {
            return of(aClass).map(Types::getSizeForClass);
        }

        return of(aClass)
                .map(aClass1 -> Arrays.stream(aClass1.getDeclaredFields()))
                .flatMap(field -> field)
                .filter(field -> !isStatic(field.getModifiers()))
                .flatMap(field -> recursiveCalculateFieldSize(field.getType()));
    }

    private long setAlignment(Long rawSize) {
        long mod = rawSize % 8;
        return mod != 0 ? rawSize + (8 - mod) : rawSize;
    }


}
