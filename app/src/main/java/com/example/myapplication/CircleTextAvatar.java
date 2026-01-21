package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import java.util.Random;

public class CircleTextAvatar extends View {
    private Paint bgPaint;
    private Paint textPaint;
    private String showText = "";
    private int bgColor;

    public CircleTextAvatar(Context context) {
        super(context);
        init();
    }

    public CircleTextAvatar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleTextAvatar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 背景画笔（随机浅色）
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgColor = getRandomLightColor();
        bgPaint.setColor(bgColor);

        // 文字画笔（白色）
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    // 设置显示的文字（取姓名首字）
    public void setName(String name) {
        if (!TextUtils.isEmpty(name)) {
            this.showText = name.substring(0, 1);
        } else {
            this.showText = "";
        }
        invalidate(); // 重绘
    }

    // 随机浅色背景
    private int getRandomLightColor() {
        Random random = new Random();
        int r = 120 + random.nextInt(136); // 120-255
        int g = 120 + random.nextInt(136);
        int b = 120 + random.nextInt(136);
        return Color.rgb(r, g, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制圆形背景
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY);
        canvas.drawCircle(centerX, centerY, radius, bgPaint);

        // 绘制首字
        if (!TextUtils.isEmpty(showText)) {
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            float textY = centerY - (fm.ascent + fm.descent) / 2;
            canvas.drawText(showText, centerX, textY, textPaint);
        }
    }
}
