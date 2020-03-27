package com.example.billardtable;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class BillardTable extends View {
    private Ball ball;
    private static class Ball {
        private Paint paint;
        private RectF rect;
        private PointF center;
        private PointF speed;
        private int radius;

        public Ball(int color, int radius) {
            this.paint = new Paint();
            this.paint.setColor(color);

            this.radius = radius;
            this.rect = new RectF();
            this.center = new PointF();
            this.speed = new PointF();
        }

        public void updateRect() {
            rect.left = center.x - radius;
            rect.right = center.x + radius;
            rect.top = center.y - radius;
            rect.bottom = center.y + radius;
        }

        public int getRadius() {
            return radius;
        }

        public Paint getPaint() {
            return paint;
        }

        public RectF getRect() {
            return rect;
        }

        public PointF getCenter() {
            return center;
        }

        public PointF getSpeed() {
            return speed;
        }
    }

    private Table table;
    private static class Table {
        private Paint paint;
        private RectF rect;

        public Table(int color, int thickness) {
            this.paint = new Paint();
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(thickness);
            this.paint.setColor(color);

            this.rect = new RectF();
        }

        public Paint getPaint() {
            return paint;
        }

        public RectF getRect() {
            return rect;
        }
    }

    public BillardTable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        table = new Table(Color.BLACK, 10);
        ball = new Ball(Color.MAGENTA, 50);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        table.getRect().left = w / 20f;
        table.getRect().top = h / 20f;
        table.getRect().right = w - table.getRect().left;
        table.getRect().bottom = h - table.getRect().top;

        ball.getCenter().x = w / 2f;
        ball.getCenter().y = h / 2f;

        ball.updateRect();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ball.getSpeed().x = (ball.getCenter().x - event.getX()) / 10;
        ball.getSpeed().y = (ball.getCenter().y - event.getY()) / 10;

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ball.updateRect();

        canvas.drawRect(table.getRect(), table.getPaint());
        canvas.drawOval(ball.getRect(), ball.getPaint());

        ball.getCenter().x += ball.getSpeed().x;
        ball.getCenter().y += ball.getSpeed().y;

        if (table.getRect().left > ball.getRect().left) {
            ball.getSpeed().x = -ball.getSpeed().x;
            ball.getCenter().x = table.getRect().left + ball.getRadius();
        } else if (table.getRect().right < ball.getRect().right) {
            ball.getSpeed().x = -ball.getSpeed().x;
            ball.getCenter().x = table.getRect().right - ball.getRadius();
        }

        if (table.getRect().top > ball.getRect().top) {
            ball.getSpeed().y = -ball.getSpeed().y;
            ball.getCenter().y = table.getRect().top + ball.getRadius();
        } else if (table.getRect().bottom < ball.getRect().bottom) {
            ball.getSpeed().y = -ball.getSpeed().y;
            ball.getCenter().y = table.getRect().bottom - ball.getRadius();
        }

        invalidate();
    }
}
