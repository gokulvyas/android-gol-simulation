package com.example.gol;

import android.test.suitebuilder.annotation.SmallTest;

import com.example.gol.Point;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;


/**
 * Unit test
 */
@SmallTest
public class PointTest  {
    @Test
    public void testGetX() {
        Point p = new Point(1,2);
        assertEquals(p.getX(),1);
    }
}
