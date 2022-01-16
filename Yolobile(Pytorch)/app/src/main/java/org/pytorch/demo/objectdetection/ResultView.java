// Copyright (c) 2020 Facebook, Inc. and its affiliates.
// All rights reserved.
//
// This source code is licensed under the BSD-style license found in the
// LICENSE file in the root directory of this source tree.

package org.pytorch.demo.objectdetection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.os.VibrationEffect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;
import android.os.Vibrator;

import java.util.Arrays;
import java.util.ArrayList;


public class ResultView extends View {

    private final static int TEXT_X = 40;
    private final static int TEXT_Y = 35;
    private final static int TEXT_WIDTH = 260;
    private final static int TEXT_HEIGHT = 50;

    private Paint mPaintRectangle;
    private Paint mPaintText;
    private ArrayList<Result> mResults;

    private Vibrator vib;
    private MediaPlayer mp;
    private Context context;

    public ResultView(Context context) {
        super(context);
        this.context = context;
    }

    public ResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintRectangle = new Paint();
        mPaintRectangle.setColor(Color.YELLOW);
        mPaintText = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mp = MediaPlayer.create(getContext(), R.raw.warning);
        if (mResults == null) return;
        String[] TriggerObjects = {"knife", "scissors", "backpack", "handbag", "suitcase", "fork"};
        for (Result result : mResults) {
            String objectName = PrePostProcessor.mClasses[result.classIndex];
            if (Arrays.toString(TriggerObjects).contains(objectName)) {
                mPaintRectangle.setStrokeWidth(5);
                mPaintRectangle.setStyle(Paint.Style.STROKE);
                canvas.drawRect(result.rect, mPaintRectangle);

                Path mPath = new Path();
                RectF mRectF = new RectF(result.rect.left, result.rect.top, result.rect.left + TEXT_WIDTH, result.rect.top + TEXT_HEIGHT);
                mPath.addRect(mRectF, Path.Direction.CW);
                mPaintText.setColor(Color.MAGENTA);
                canvas.drawPath(mPath, mPaintText);

                mPaintText.setColor(Color.WHITE);
                mPaintText.setStrokeWidth(0);
                mPaintText.setStyle(Paint.Style.FILL);
                mPaintText.setTextSize(32);

                canvas.drawText(String.format("%s %.2f", PrePostProcessor.mClasses[result.classIndex], result.score), result.rect.left + TEXT_X, result.rect.top + TEXT_Y, mPaintText);



                Paint warning = new Paint();
                FontMetrics fm = new FontMetrics();
                warning.setColor(Color.RED);
                warning.setTextSize(50.0f);
                warning.getFontMetrics(fm);
                int margin = 5;

                String wText = "\"Warning! Triggered object detected.";
                canvas.drawRect(100 - margin, 100 + fm.top - margin,
                        100 + warning.measureText(wText) + margin, 100 + fm.bottom
                                + margin, warning);

                warning.setColor(Color.BLACK);
                canvas.drawText(wText, 100, 100, warning);
//                warning.set(0, 0, canvas.getWidth(), canvas.getHeight());
//                canvas.drawRect(warning, war);
                if(!mp.isPlaying())
                mp.start();
//                mp.stop();

            }
            else {
                if(mp.isPlaying())
                mp.stop();
            }
//            canvas.drawText(String.format("%s %.2f", PrePostProcessor.mClasses[result.classIndex], result.score), result.rect.left + TEXT_X, result.rect.top + TEXT_Y, mPaintText);

        }
    }

    public void setResults(ArrayList<Result> results) {
        mResults = results;
    }
}
