package ru.otus.homework_3;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;

/**
 * Created by nkargin on 16.02.2018.
 * hei@spark-mail.ru
 */
public class SimpleArrayListTest {

    private static final SimpleArrayList<String> strings = new SimpleArrayList<>();

    @Before
    public void initList() {
        strings.clear();

        strings.add("zzzzz");
        strings.add("bbb2");
        strings.add("1236sd");
        strings.add("155f");
        strings.add("test1");
    }

    @Test
    public void testAdd() {
        assertThat(strings.size(), equalTo(5));
    }

    @Test
    public void testRemoveObject() {
        boolean remove = strings.remove("bbb2");

        assertThat(strings.size(), equalTo(4));
        assertThat(remove, equalTo(true));
    }

    @Test
    public void testRemoveByIndex() {
        String remove = strings.remove(1);

        assertThat(remove, equalTo("bbb2"));
        assertThat(strings.size(), equalTo(4));
    }

    @Test
    public void testRemoveElementWhichNotExists() {
        boolean remove = strings.remove("152");
        String remove1 = strings.remove(5);

        assertThat(remove, equalTo(false));
        assertThat(remove1, equalTo(null));
    }

    @Test
    public void testSortCollections() {
        strings.sort(Collections.reverseOrder());

        strings.forEach(System.out::println);
    }

    @Test
    public void testContainsNull() {
        List<String> stringWithNulls = new SimpleArrayList<>();

        stringWithNulls.add("a");
        stringWithNulls.add(null);
        stringWithNulls.add("b");

        assertTrue(stringWithNulls.contains(null));
    }

    @Test
    public void testRemoveFromListWithNull() {
        List<String> list = new SimpleArrayList<>();
        list.add("a");
        list.add(null);
        list.add("b");

        list.remove("b");
        assertThat(list.size(), equalTo(2));
    }

    @Test
    public void testAddAllCollections() {
        Collections.addAll(strings, "125", "656456", "fasfas", "61326126");

        assertThat(strings.size(), equalTo(9));
    }

}
