import java.io.IOException;

/**
 * Created by nkargin on 24.02.2018.
 * hei@spark-mail.ru
 */
public class ObjectArraysMemoryLeaking {

    private static final int INITIAL_SIZE = 2200;

    public static void main(String[] args) throws Exception {
        Object[] leaky = new Object[INITIAL_SIZE];
        fillUp(leaky);

        for (int i = 0; i < 100; i++) {
            Thread.sleep(10000);
            Object[] copy = new Object[leaky.length * 5 / 4];

            fillUp(copy);
            leaky = copy;
        }
    }

    private static void fillUp(Object[] arr) throws IOException {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Object();
        }
    }
}
