package com.shadow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

public class ShadowView extends ViewGroup {
    int shadowOffsetX = 0;
    int shadowOffsetY = (int)(-4 * Resources.getSystem().getDisplayMetrics().density);
    int shadowRadius = 0;
    int borderRadius = 0;
    int shadowColor;
    int shadowColorToDraw;
    int borderShadowColor;
    int shadowOpacity;
    double borderWidth;

    Paint viewPaint = new Paint();
    Paint borderPaint = new Paint();



    public ShadowView(Context context) {
        super(context);
        init();
    }

    public ShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShadowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        invalidate();
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(Color.TRANSPARENT);
        viewPaint.setColor(color);
        createShadowColor();
        invalidate();
    }

    private void init() {
        viewPaint.setAntiAlias(true);
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Color.BLACK);
        shadowColor = Color.BLACK;
        shadowColorToDraw = Color.BLACK;
        createShadowColor();
        invalidate();
    }

    public void setBorderRadius(double borderRadius) {
        this.borderRadius=(int) (borderRadius * Resources.getSystem().getDisplayMetrics().density);
        invalidate();
    }

    public void setShadowOffsetX(double shadowOffsetX) {
        this.shadowOffsetX = (int)((shadowOffsetX * Resources.getSystem().getDisplayMetrics().density));
        invalidate();
    }

    public void setShadowOffsetY(double shadowOffsetY) {
        this.shadowOffsetY = (int)((shadowOffsetY * Resources.getSystem().getDisplayMetrics().density));
        invalidate();
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        createShadowColor();
        invalidate();
    }

    public void setShadowOpacity(double shadowOpacity) {
        shadowOpacity = Math.min(Math.max(0, shadowOpacity), 1);
        this.shadowOpacity = (int)(shadowOpacity * 255);
        this.createShadowColor();
        invalidate();
    }

    public void setShadowRadius(double shadowRadius) {
        shadowRadius = Math.max(0.2, shadowRadius);
        this.shadowRadius = (int)((shadowRadius * Resources.getSystem().getDisplayMetrics().density) * 1.5);
        invalidate();
    }

    public void setBorderColor(int borderColor) {
        borderPaint.setColor(borderColor);
        createShadowColor();
        invalidate();
    }

    public void setBorderWidth(double borderWidth) {
        this.borderWidth = (borderWidth * Resources.getSystem().getDisplayMetrics().density * 1.1);
        invalidate();
    }

    private void createShadowColor() {
        int red = Color.red(shadowColor);
        int green = Color.green(shadowColor);
        int blue = Color.blue(shadowColor);
        int shadowColorAlpha = Color.alpha(shadowColor);
        int backgroundColorAlpha = Color.alpha(viewPaint.getColor());
        int borderColorAlpha = Color.alpha(borderPaint.getColor());
        int shadowAlpha = (int)((double)shadowOpacity * ((double)shadowColorAlpha/255.0) * ((double)backgroundColorAlpha/255.0));
        int borderShadowAlpha = (int)((double)shadowOpacity * ((double)shadowColorAlpha/255.0) * ((double)borderColorAlpha/255.0) / ((double)backgroundColorAlpha / 255.0) * 0.5);
        borderColorAlpha = (int)Math.min((double)borderColorAlpha * ((double)shadowColorAlpha/255.0)* ((double)shadowOpacity/255.0), Math.min(shadowOpacity, borderShadowAlpha));
        shadowColorToDraw = Color.argb(shadowAlpha, red, green, blue);
        borderShadowColor = Color.argb((int)((double)borderColorAlpha*0.8), red, green, blue);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        viewPaint.setShadowLayer(this.shadowRadius, shadowOffsetX, shadowOffsetY, shadowColorToDraw);
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), borderRadius, borderRadius, viewPaint);
        if (borderWidth > 0) {
            borderPaint.setStrokeWidth((float)borderWidth);
            if (Color.alpha(viewPaint.getColor()) < 255) {
                borderPaint.setShadowLayer(this.shadowRadius, shadowOffsetX, shadowOffsetY, borderShadowColor);
            }
            else {
                borderPaint.clearShadowLayer();
            }
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), borderRadius, borderRadius, borderPaint);
        }
    }
}
