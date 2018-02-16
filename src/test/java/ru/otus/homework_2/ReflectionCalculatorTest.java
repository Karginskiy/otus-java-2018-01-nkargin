package ru.otus.homework_2;

import org.junit.Test;
import ru.otus.homework_2.calculator.ObjectSizeCalculator;
import ru.otus.homework_2.calculator.ReflectionSizeCalculator;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


/**
 * Created by nkargin on 13.02.2018.
 * hei@spark-mail.ru
 */
public class ReflectionCalculatorTest {

    private static ObjectSizeCalculator calculator = new ReflectionSizeCalculator();

    @Test
    public void testPrimitivesSizes() {
        assertThat(calculator.calculate(1L), equalTo(8L));
        assertThat(calculator.calculate(1), equalTo(4L));
        assertThat(calculator.calculate(1f), equalTo(4L));
        assertThat(calculator.calculate('c'), equalTo(2L));
        assertThat(calculator.calculate((byte) 1), equalTo(1L));
        assertThat(calculator.calculate((short) 1), equalTo(2L));
        assertThat(calculator.calculate(1.0), equalTo(8L));
    }

    @Test
    public void testEmptyStringSize() {
        assertThat(calculator.calculate(""), equalTo(8L + 12L + 4L));
    }

    @Test
    public void testEmptyObjectSize() {
        assertThat(calculator.calculate(new Object()), equalTo(8L));
    }

    @Test
    public void testArrayOfPrimitivesSize() {
        assertThat(calculator.calculate(new long[] { 1L, 1L, 1L }), equalTo(12L + 8L + 8L + 8L + 4L));
    }

    @Test
    public void testListSize() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("123");
        assertThat(calculator.calculate(strings), equalTo(56L));
    }

    @Test
    public void testHashMapSize() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("125", "TEST");
        assertThat(calculator.calculate(stringStringHashMap), equalTo(112L));
    }

}
