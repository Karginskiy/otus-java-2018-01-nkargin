package ru.otus.homework_2.utils;

/**
 * Created by nkargin on 05.02.2018.
 * hei@spark-mail.ru
 */
public class MeasurementUtils {

    private MeasurementUtils() {}

    public static boolean isPrimitive(Class<?> aClass) {
        return aClass.isPrimitive();
    }

    public static boolean isReference(Class<?> aClass) {
        return !(isArray(aClass)) && !(isPrimitive(aClass));
    }

    public static boolean isArray(Class<?> aClass) {
        return aClass.isArray();
    }

}
