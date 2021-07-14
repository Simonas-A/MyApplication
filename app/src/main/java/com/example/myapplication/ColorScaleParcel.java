package com.example.myapplication;

import android.os.Parcel;

public class ColorScaleParcel {
    ColorScale[] colorScales;

    public void writeToParcel(Parcel out) {
        out.writeTypedArray(colorScales, 0);
    }

    private void readFromParcel(Parcel in) {
        colorScales = in.createTypedArray(ColorScale.CREATOR);
    }
}
