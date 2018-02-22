package homework_2.calculator;

/**
 *
 * Main object size calculator interface.
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
    String getMessage();

    default String getUnits() {
        return ", bytes";
    }

}
