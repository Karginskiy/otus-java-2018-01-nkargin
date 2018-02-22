package ru.otus.homework_3;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by nkargin on 18.02.2018.
 * hei@spark-mail.ru
 */
public class SimpleTreeMapTest {

    private static final SimpleTreeMap<Integer, String> treeMap = new SimpleTreeMap<>();

    @Before
    public void before() {
        treeMap.put(5, "first");
        treeMap.put(12, "second");
        treeMap.put(2, "third");
        treeMap.put(3, "fourth");
        treeMap.put(6, "fifth");
    }

    @Test
    public void testPut() {
        assertThat(treeMap.size(), equalTo(5));
    }

    @Test
    public void testGet() {
        assertThat(treeMap.get(5), equalTo("first"));
        assertThat(treeMap.get(12), equalTo("second"));
        assertThat(treeMap.get(6), equalTo("fifth"));
        assertThat(treeMap.get(2), equalTo("third"));
        assertThat(treeMap.get(3), equalTo("fourth"));
    }

    @Test
    public void testRemove() {
        assertThat(treeMap.remove(5), equalTo("first"));
        assertThat(treeMap.size(), equalTo(4));

        assertThat(treeMap.remove(3), equalTo("fourth"));
        assertThat(treeMap.size(), equalTo(3));
    }

}
