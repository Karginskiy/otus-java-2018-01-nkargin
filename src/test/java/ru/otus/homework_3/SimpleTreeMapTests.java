package ru.otus.homework_3;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by nkargin on 18.02.2018.
 * hei@spark-mail.ru
 */
public class SimpleTreeMapTests {

    private static final SimpleTreeMap<Integer, String> treeMap = new SimpleTreeMap<>();

    @Before
    public void before() {
        treeMap.put(5, "5");
        treeMap.put(12, "12");
        treeMap.put(2, "2");
        treeMap.put(3, "3");
        treeMap.put(6, "6");
    }

    @Test
    public void testPut() {
        assertThat(treeMap.size(), equalTo(5));
    }

    @Test
    public void testGet() {
        assertThat(treeMap.get(5), equalTo("5"));
        assertThat(treeMap.get(12), equalTo("12"));
        assertThat(treeMap.get(6), equalTo("6"));
        assertThat(treeMap.get(2), equalTo("2"));
        assertThat(treeMap.get(3), equalTo("3"));
    }

    @Test
    public void testRemove() {
        assertThat(treeMap.remove(5), equalTo("5"));
        assertThat(treeMap.size(), equalTo(4));

        assertThat(treeMap.remove(3), equalTo("3"));
        assertThat(treeMap.size(), equalTo(3));
    }

}
