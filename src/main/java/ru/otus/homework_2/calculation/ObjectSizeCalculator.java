package ru.otus.homework_2.calculation;

/**
 *
 * Main object size calculation interface.
 *
 * Created by nkargin on 05.02.2018.
 * hei@spark-mail.ru
 */
public interface ObjectSizeCalculator {

    /**
     * Calculates size of any object-instance
     * @return - object size, bytes
     */

    long calculate(Object o);

}
