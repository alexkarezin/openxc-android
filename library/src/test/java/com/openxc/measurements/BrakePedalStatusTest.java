package com.openxc.measurements;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class BrakePedalStatusTest {
    BrakePedalStatus measurement;

    @Before
    public void setUp() {
        measurement = new BrakePedalStatus(new Boolean(false));
    }

    @Test
    public void testGet() {
        assertThat(measurement.getValue().booleanValue(), equalTo(false));
    }

    @Test
    public void testHasNoRange() {
        assertFalse(measurement.hasRange());
    }

    @Test
    public void testGenericName() {
        assertEquals(BrakePedalStatus.ID, measurement.getGenericName());
    }
}
