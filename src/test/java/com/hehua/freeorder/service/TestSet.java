package com.hehua.freeorder.service;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hesheng on 14-10-29.
 */
public class TestSet {
    @Test
    public void testSet() {
        Set<Integer> itemIds = new HashSet<>();
        List<Integer> test1 = new ArrayList();
        test1.add(123);
        test1.add(123);
        test1.add(124);

        List<Integer> test2 = new ArrayList();
        test2.add(124);
        test2.add(125);

        itemIds.addAll(test1);
        itemIds.addAll(test2);

        System.out.println(itemIds);

        System.out.println(test1.subList(0, 1000));
    }
}
