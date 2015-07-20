package com.example.gol;

import android.os.Parcel;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation Test
 */
public class PointTestTest extends TestCase {

    public static final int TEST_X = 1;
    public static final int TEST_Y = 2;
    public Point point;

    @Before
    public void createPoint() {
        point = new Point(TEST_X,TEST_Y);
    }

    @Test
    public void testCreatePoint_ParcelableWriteRead() {
        // Set up the Parcelable object to send and receive.
        point = new Point(TEST_X,TEST_Y);

        // Write the data.
        Parcel parcel = Parcel.obtain();
        point.writeToParcel(parcel, point.describeContents());

        // After you're done with writing, you need to reset the parcel for reading.
        parcel.setDataPosition(0);

        // Read the data.
        Point createdFromParcel = Point.CREATOR.createFromParcel(parcel);

        // Verify that the received data is correct.
        assertEquals(createdFromParcel.getX(), TEST_X);
        assertEquals(createdFromParcel.getY(), TEST_Y);
    }
}