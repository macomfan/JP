package com.jpword.ma.baseui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by u0151316 on 12/28/2017.
 */

public class ViewLogViewer extends View {

    public enum Type {
        HARMLESS,
        WARNING,
        SUCCESS,
        FAILURE,
    }

    class Log {
        public Type mType;
        public String mWhat;

        public Log(Type type, String what) {
            mType = type;
            mWhat = what;
        }
    }

    private List<Log> logs_ = new LinkedList<>();

    public ViewLogViewer(Context context) {
        super(context);
    }

    public ViewLogViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewLogViewer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewLogViewer(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void addLog(Type type, String log) {
        logs_.add(new Log(type, log));
        this.setMinimumHeight(this.getMinimumHeight() + 10);
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float baselineX = 30.0f;
        float baselineY = 0.0f;

        for (Log line : logs_) {
            baselineY = drawLine(canvas, line, baselineX, baselineY);
            baselineY += 10.0f;
        }

        if (this.getMinimumHeight() != (int)baselineY) {
            this.setMinimumHeight((int)baselineY);
        }
    }

    protected float drawLine(Canvas canvas, Log line, float x, float y) {
        final float tvWidth = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
        Paint paint = new Paint();
        switch (line.mType) {
            case HARMLESS:
                paint.setColor(Color.WHITE);
                break;
            case FAILURE:
                paint.setColor(Color.RED);
                break;
            case SUCCESS:
                paint.setColor(Color.GREEN);
                break;
            case WARNING:
                paint.setColor(Color.YELLOW);
                break;
        }

        paint.setTextSize(35);
        paint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float baselineX = x;
        float baselineY = y - fm.top;
        float lineWidth = baselineX;
        if (paint.measureText(line.mWhat) < tvWidth) {
            canvas.drawText(line.mWhat, baselineX, baselineY, paint);
            return baselineY + fm.bottom;
        }

        // mutlilines
        float currentBaselineX = baselineX;
        float currentBaselineY = baselineY;
        int currentLineStartIndex = 0;
        int i = 0;
        for (; i < line.mWhat.length(); i++) {
            lineWidth += paint.measureText(String.valueOf(line.mWhat.charAt(i)));
            if (lineWidth >= tvWidth) {
                lineWidth = 0.0f;
                canvas.drawText(line.mWhat, 0, i, currentBaselineX, currentBaselineY, paint);
                currentLineStartIndex = i;
                currentBaselineY += fm.bottom - fm.top;
                currentBaselineY += 1.0f;
                // go to new line
            }
        }
        if (currentLineStartIndex < i)
        {
            canvas.drawText(line.mWhat, currentLineStartIndex, line.mWhat.length(), currentBaselineX, currentBaselineY, paint);
        }
        return currentBaselineY + fm.bottom;
    }
}
