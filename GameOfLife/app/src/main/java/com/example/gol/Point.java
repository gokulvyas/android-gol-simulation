package com.example.gol;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author Gokul
 * ArrayList of this Parcelable class objects is saved to support Screen Orientation
 *
 */
public class Point implements Parcelable {

	int x,y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

    public Point(Parcel in){
        int[] data = new int[2];

        in.readIntArray(data);
        this.x = data[0];
        this.y = data[1];
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeIntArray(new int[]{x,y});
		
	}
	
	public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>() {
        public Point createFromParcel(Parcel in) {
            return new Point(in); 
        }

		@Override
		public Point[] newArray(int size) {
			return new Point[size];
		}

    };

}
