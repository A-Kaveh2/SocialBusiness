package ir.rasen.myapplication.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerPaging extends ViewPager {

    boolean paging=true;

    public ViewPagerPaging(Context context) {
        super(context);
    }

    public ViewPagerPaging(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!paging) {
            return false; // do not consume
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!paging) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }
}