package com.dsh.whack;

import android.app.Activity;
import android.graphics.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

public class WhackAMole extends Activity {
    private GameView gameView;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    public class GameView extends View {
        private final Paint paint = new Paint();
        private final Paint molePaint = new Paint();
        private int w, h;
        private int score = 0;
        private int moleX = -100, moleY = -100;
        private boolean moleVisible = false;
        private boolean gameStarted = false;

        public GameView(android.content.Context c) {
            super(c);
            paint.setColor(Color.WHITE);
            paint.setTextSize(28);
            paint.setTextAlign(Paint.Align.CENTER);
            molePaint.setColor(0xFF8B4513);
        }

        @Override
        protected void onSizeChanged(int nw, int nh, int oldw, int oldh) {
            w = nw; h = nh;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(0xFF2E7D32);
            canvas.drawText("Score: " + score, w / 2f, 40, paint);
            if (!gameStarted) {
                canvas.drawText("Tap anywhere to start!", w / 2f, h / 2f, paint);
            }
            if (moleVisible) {
                float r = 35;
                canvas.drawCircle(moleX, moleY, r, molePaint);
                Paint eyeP = new Paint();
                eyeP.setColor(Color.BLACK);
                canvas.drawCircle(moleX - 10, moleY - 5, 4, eyeP);
                canvas.drawCircle(moleX + 10, moleY - 5, 4, eyeP);
            }
        }

        private void showMole() {
            int margin = 60;
            moleX = margin + (int)(Math.random() * Math.max(1, w - margin * 2));
            moleY = margin + 80 + (int)(Math.random() * Math.max(1, h - margin * 2 - 80));
            moleVisible = true;
            invalidate();
            handler.postDelayed(new Runnable() {
                @Override public void run() { hideMole(); }
            }, 800);
        }

        private void hideMole() {
            moleVisible = false;
            invalidate();
            if (gameStarted) {
                handler.postDelayed(new Runnable() {
                    @Override public void run() { showMole(); }
                }, 300 + (int)(Math.random() * 700));
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                if (!gameStarted) {
                    gameStarted = true;
                    showMole();
                    return true;
                }
                if (moleVisible) {
                    float dx = e.getX() - moleX;
                    float dy = e.getY() - moleY;
                    if (dx * dx + dy * dy < 35 * 35) {
                        score++;
                        moleVisible = false;
                        invalidate();
                        handler.postDelayed(new Runnable() {
                            @Override public void run() { showMole(); }
                        }, 400 + (int)(Math.random() * 600));
                        return true;
                    }
                }
            }
            return super.onTouchEvent(e);
        }
    }
}
