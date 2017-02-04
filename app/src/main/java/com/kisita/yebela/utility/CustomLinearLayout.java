package com.kisita.yebela.utility;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;

import com.kisita.yebela.R;

/**
 * Created by Hugues on 11-01-17.
 */

public class CustomLinearLayout extends android.widget.LinearLayout {
    private int mColor;
    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {

        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomLinearLayout,
                0, 0);

        try {
            mColor = a.getColor(R.styleable.CustomLinearLayout_label_color,0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setColor(mColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        Point a = new Point(0, 0);
        Point b = new Point(0, getHeight()/4);
        Point c = new Point(getWidth()/8, getHeight()/8);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();
        canvas.drawPath(path, paint);

    }
}
