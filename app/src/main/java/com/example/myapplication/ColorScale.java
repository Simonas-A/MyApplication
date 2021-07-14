package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class ColorScale implements Parcelable {
    public int hue = 0;
    public int totalDistance = 0;
    public int totalTimeMs = 0;
    public int totalHits = 0;


    public ColorScale(int Hue) {
        hue = Hue;
        totalDistance = 0;
        totalTimeMs = 0;
        totalHits = 0;
    }

    public void AddHit(int distance, int ms)
    {
        totalDistance += distance;
        totalTimeMs += ms;
        totalHits++;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ColorScale> CREATOR = new Creator<ColorScale>() {
        @Override
        public ColorScale createFromParcel(Parcel in) {
            return new ColorScale(in);
        }

        @Override
        public ColorScale[] newArray(int size) {
            return new ColorScale[size];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(hue);
        out.writeInt(totalDistance);
        out.writeInt(totalTimeMs);
        out.writeInt(totalHits);
    }

    protected ColorScale(Parcel in) {
        hue = in.readInt();
        totalDistance = in.readInt();
        totalTimeMs = in.readInt();
        totalHits = in.readInt();
    }
}


