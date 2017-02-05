package com.tyzrpvx.blossomexample;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitTestExampleTest {

    private UnitTestExample mUnitTestExample;

    @Before
    public void setUp() throws Exception {
        mUnitTestExample = new UnitTestExample();
    }

    @Test
    public void sum() throws Exception {
        assertEquals(2d, mUnitTestExample.sum(1d, 1d), 0);
    }

}