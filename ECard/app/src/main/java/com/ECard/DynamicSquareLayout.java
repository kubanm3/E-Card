package com.ECard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import static java.lang.Math.round;

public class DynamicSquareLayout  extends RelativeLayout {

    public DynamicSquareLayout(Context context) {
        super(context);
    }


    public DynamicSquareLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DynamicSquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


// here we are returning the width in place of height, so width = height
// you may modify further to create any proportion you like ie. height = 2*width etc

    @Override public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float heightsuper = (float) ((128.0/296.0)*widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, round(heightsuper));
        float size = (float) (MeasureSpec.getSize(widthMeasureSpec)*0.8);
        float height = (float) ((128.0/296.0)*size*0.8);
        int width = (int) size;
        setMeasuredDimension(width, round(height));
    }


}
