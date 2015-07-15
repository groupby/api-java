package com.groupbyinc.api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by ferron on 4/22/15.
 */
public class AbstractQueryTest {

    BaseQuery query;

    @Before
    public void setUp() {
        query = new BaseQuery();
    }

    @Test
    public void splitTestRange() {
        String [] split = query.splitRefinements("test=bob~price:10..20");
        assertArrayEquals(new String[]{"test=bob", "price:10..20"}, split);
    }

    @Test
    public void splitTestNoCategory () {
        String [] split = query.splitRefinements("~gender=Women~simpleColorDesc=Pink~product=Clothing");
        assertArrayEquals(new String[]{"gender=Women", "simpleColorDesc=Pink", "product=Clothing"}, split);
    }

    @Test
    public void splitTestCategory() {
        String[] split = query.splitRefinements("~category_leaf_expanded=Category Root~Athletics~Men's~Sneakers");

        assertArrayEquals(
                new String[]{"category_leaf_expanded=Category Root~Athletics~Men's~Sneakers"}, split);
    }

    @Test
    public void splitTestMultipleCategory() {
        String[] split = query.splitRefinements("~category_leaf_expanded=Category Root~Athletics~Men's~Sneakers~category_leaf_id=580003");

        assertArrayEquals(
                new String[]{"category_leaf_expanded=Category Root~Athletics~Men's~Sneakers", "category_leaf_id=580003"}, split);
    }

    @Test
    public void splitTestRangeAndMultipleCategory () {
        String[] split = query.splitRefinements(
                "test=bob~price:10..20~category_leaf_expanded=Category Root~Athletics~Men's" +
                "~Sneakers~category_leaf_id=580003~color=BLUE~color=YELLOW~color=GREY");
        assertArrayEquals(new String[]{"test=bob", "price:10..20",
                                       "category_leaf_expanded=Category Root~Athletics~Men's~Sneakers", "category_leaf_id=580003",
                                       "color=BLUE", "color=YELLOW", "color=GREY"}, split);
    }

    @Test
    public void splitTestCategoryLong() {
        String reallyLongString =
                "~category_leaf_expanded=Category Root~Athletics~Men's~Sneakers~category_leaf_id=580003~" +
                "color=BLUE~color=YELLOW~color=GREY~feature=Lace Up~feature=Light Weight~brand=Nike";

        String [] split = query.splitRefinements(reallyLongString);

        assertArrayEquals(
                new String[]{"category_leaf_expanded=Category Root~Athletics~Men's~Sneakers", "category_leaf_id=580003",
                        "color=BLUE", "color=YELLOW", "color=GREY", "feature=Lace Up", "feature=Light Weight",
                        "brand=Nike"
                },
                split);
    }

    @Test
    public void testNull () {
        String[] split = query.splitRefinements(null);
        assertArrayEquals(new String[]{}, split);
    }

    @Test
    public void testEmpty () {
        String[] split = query.splitRefinements("");
        assertArrayEquals(new String[]{}, split);
    }

    @Test
    public void testUtf8 () {
        String [] split = query.splitRefinements("tëst=bäb~price:10..20");
        assertArrayEquals(new String[]{"tëst=bäb", "price:10..20"}, split);
    }
}
