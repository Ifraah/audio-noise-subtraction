package com.example.ifraah.audionoisesubtraction;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by ifraah on 12/1/15.
 */
public  class SpectrogramView extends View {
    private Paint paint = new Paint();
    private double[][] data;

    public SpectrogramView(Context context, double[][] data) {
        super(context);
        this.data = data;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (data != null) {
            paint.setStrokeWidth(1);
            canvas.drawColor(Color.WHITE);
            int width = data.length;
            int height = data[0].length;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int value;
                    value = 255 - (int) (data[i][j] * 255);
                    paint.setColor(value << 16 | value << 8 | value | 255 << 24);
                    canvas.drawPoint(i, height - 1 - j, paint);
                }
            }

        } else {
            System.err.println("Data Corrupt");
        }

        //draw circle

    }
}
