package ru.otus.homework_2.calculator;

import ru.otus.homework_2.model.Types;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.stream.Stream;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;
import static ru.otus.homework_2.utils.MeasurementUtils.isArray;
import static ru.otus.homework_2.utils.MeasurementUtils.isPrimitive;

/**
 * Created by nkargin on 05.02.2018.
 * hei@spark-mail.ru
 */
public class ReflectionSizeCalculator implements ObjectSizeCalculator {

    private static final long HEADER_SIZE = 8L;
    private static final long ARRAY_HEADER_SIZE = 12L;
    private static final String SIZE_MESSAGE = "Reflection size of %s equal to %d";

    @Override
    public long calculate(Object o) {
        Long rawSize = recursiveCalculateFieldSize(o).reduce(HEADER_SIZE, Long::sum);

        return rawSize % 8 != 0 ? setAlignment(rawSize) : rawSize;
    }

    @Override
    public String getMessage() {
        return SIZE_MESSAGE;
    }

    private Stream<Long> recursiveCalculateFieldSize(Object underlying) {
        if (underlying == null) {
            return Stream.of(0L);
        }

        Class<?> aClass = underlying.getClass();
        if (isPrimitive(aClass)) {
            return of(aClass).map(Types::getSizeForClass);
        }

        if (isArray(aClass)) {
            int length = Array.getLength(underlying);
            Stream<Long> stream = of(ARRAY_HEADER_SIZE);
            for (int i = 0; i < length; i++) {
                final Object o = Array.get(underlying, i);
                if (o != null) {
                    stream = concat(stream, recursiveCalculateFieldSize(o));
                }
            }

            return stream;
        }

        Stream<Long> referenceSizeStream = Stream.of();

        for (Field field : aClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (isStatic(field.getModifiers())) continue;
            Class<?> type = field.getType();
            if (isPrimitive(type)) {
                referenceSizeStream = concat(referenceSizeStream, Stream.of(type).map(Types::getSizeForClass));
                continue;
            }
            try {
                Object o = field.get(underlying);
                if (o != null) {
                    referenceSizeStream = concat(referenceSizeStream, recursiveCalculateFieldSize(o));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return referenceSizeStream;
    }


    private long setAlignment(Long rawSize) {
        long mod = rawSize % 8;
        return mod != 0 ? rawSize + (8 - mod) : rawSize;
    }


}
