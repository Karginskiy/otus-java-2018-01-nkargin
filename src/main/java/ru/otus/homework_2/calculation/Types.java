package ru.otus.homework_2.calculation;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

/**
 * Created by nkargin on 05.02.2018.
 * hei@spark-mail.ru
 */
public enum Types {

    Int(int.class, 4L),
    Char(char.class, 2L),
    Short(short.class, 2L),
    Byte(byte.class, 1L),
    Long(long.class, 8L),
    Double(double.class, 8L),
    Float(float.class, 4L);

    /**
     * Size of references on x86_64 if not -XX:+UseCompressedOops.
     */
    public static final long LONG_REFERENCE_SIZE = 8L;

    Types(Class aClass, Long size) {
        this.aClass = aClass;
        this.size = size;
    }

    private static Map<Class, Long> classToSize = stream(values()).collect(toMap(Types::getaClass, Types::getSize));
    private Class aClass;
    private Long size;

    public Class getaClass() {
        return aClass;
    }

    public Long getSize() {
        return size;
    }

    public static Long getSizeForClass(Class aClass) {
        return classToSize.getOrDefault(aClass, LONG_REFERENCE_SIZE);
    }
}
