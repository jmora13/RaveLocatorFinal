//package com.example.ravelocator;
//
//import android.content.res.Resources;
//import android.graphics.Canvas;
//import android.graphics.ColorFilter;
//import android.graphics.Rect;
//import android.graphics.drawable.AnimatedImageDrawable;
//import android.graphics.drawable.AnimatedVectorDrawable;
//import android.graphics.drawable.Drawable;
//import android.util.AttributeSet;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.content.res.ResourcesCompat;
//
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//
//import java.io.IOException;
//
//public class SplashDrawable extends Drawable {
//
//    AnimatedImageDrawable animatedImageDrawable;
//    @Override
//    public void draw(@NonNull Canvas canvas) {
//        int height = getBounds().height();
//        int width = getBounds().width();
//        // centering the layout
//        int centerX = canvas.getWidth() / 2;
//        int centerY = canvas.getHeight() /2;
//        // compute the bounds of the drawable
//        int left = centerX - (width / 2);
//        int top = centerY - (height / 2);
//        int right = centerX + (width / 2);
//        int bottom = centerY + (height / 2);
//        Rect rect = new Rect(left, top, right, bottom);
//        // set the bounds to the animatedVector
//        animatedImageDrawable.setBounds(rect);
//        animatedImageDrawable.draw(canvas);
//    }
//
//    @Override
//    public void setAlpha(int alpha) {
//        animatedImageDrawable.setAlpha(alpha);
//    }
//
//    @Override
//    public void setColorFilter(@Nullable ColorFilter colorFilter) {
//        animatedImageDrawable.setColorFilter(colorFilter);
//    }
//
//    @Override
//    public int getOpacity() {
//        return animatedImageDrawable.getOpacity();
//    }
//    @Override
//    public void inflate(
//            Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme
//    ) {
//        try {
//            super.inflate(r,parser,attrs,theme);
//        } catch (IOException e) {
//            initalizeAnimation(r, theme);
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        }
//        initalizeAnimation(r,theme);
//    }
//
//    private void initalizeAnimation(Resources res, Resources.Theme theme){
//       // animatedImageDrawable = (AnimatedImageDrawable) ResourcesCompat.getDrawable(res, R.drawable.avd_anim, theme);
//        animatedImageDrawable.start();
//    }
//}
