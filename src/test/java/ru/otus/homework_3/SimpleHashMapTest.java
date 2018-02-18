package ru.otus.homework_3;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Created by nkargin on 18.02.2018.
 * hei@spark-mail.ru
 */
public class SimpleHashMapTest {

    private static SimpleHashMap<String, String> testMap = new SimpleHashMap<>();

    @Before
    public void before() {
        testMap.clear();

        testMap.put("123", "123");
        testMap.put("555", "555");
        testMap.put("1255", "1255");
        testMap.put("1", "1");
        testMap.put("2", "2");
        testMap.put("3", "3");
        testMap.put("4", "4");
        testMap.put("5", "5");
        testMap.put("6", "6");
        testMap.put("7", "7");
        testMap.put("8", "8");
        testMap.put("9", "9");
        testMap.put("10", "10");
        testMap.put("11", "11");
        testMap.put("111", "111");
        testMap.put("111125", "111125");
        testMap.put("11112125", "11112125");
        testMap.put("11112125165", "11112125165");
        testMap.put("111121251651kfsld", "111121251651kfsld");
        testMap.put("111121251651kfsldfasf", "111121251651kfsldfasf");
        testMap.put("11112125165asf1kfsldfasf", "11112125165asf1kfsldfasf");
        testMap.put("11112125165as,mds1kfsldfasf", "11112125165as,mds1kfsldfasf");
        testMap.put("11112125165as,md12551kfsldfasf", "11112125165as,md12551kfsldfasf");
        testMap.put("11112125165as,md1255cxbxc1kfsldfasf", "11112125165as,md1255cxbxc1kfsldfasf");
        testMap.put("11112125165as,md1255cxbxc1251kfsldfasf", "11112125165as,md1255cxbxc1251kfsldfasf");
        testMap.put("11112125165as,md1255cxbxc125vxc1kfsldfasf", "11112125165as,md1255cxbxc125vxc1kfsldfasf");
        testMap.put("11112125165as,md1255cxbxc125vxc12311kfsldfasf", "11112125165as,md1255cxbxc125vxc12311kfsldfasf");
        testMap.put("11112125165as,md1251kfsldfasf", "11112125165as,md1251kfsldfasf");
        testMap.put("11112125165as,md125vc1kfsldfasf", "11112125165as,md125vc1kfsldfasf");
        testMap.put("11112125165as,md125vcbcxv1kfsldfasf", "11112125165as,md125vcbcxv1kfsldfasf");
        testMap.put("11112125165as,md125vcbcxv124d1kfsldfasf", "11112125165as,md125vcbcxv124d1kfsldfasf");
        testMap.put("11112125165as,md125vcbcxv124dbvc1kfsldfasf", "11112125165as,md125vcbcxv124dbvc1kfsldfasf");
    }

    @Test
    public void testPut() {
        assertThat(testMap.size(), equalTo(32));
    }

    @Test
    public void testRemove() {
        String remove = testMap.remove("555");
        String remove2 = testMap.remove("1");

        assertThat(remove, equalTo("555"));
        assertThat(remove2, equalTo("1"));
        assertThat(testMap.size(), equalTo(30));
    }
    
    @Test
    public void testGet() {
        assertThat(testMap.get("11112125165as,md125vcbcxv124dbvc1kfsldfasf"),
                equalTo("11112125165as,md125vcbcxv124dbvc1kfsldfasf"));
        assertThat(testMap.get("3"), equalTo("3"));
        assertThat(testMap.get("111"), equalTo("111"));
        assertThat(testMap.get("0000"), equalTo(null));
    }

    @Test
    public void testContainsKey() {
        assertTrue(testMap.containsKey("3"));
        assertTrue(testMap.containsKey("111"));
        assertTrue(testMap.containsKey("11112125165as,md125vcbcxv124dbvc1kfsldfasf"));
        assertFalse(testMap.containsKey("0000"));
    }

}
