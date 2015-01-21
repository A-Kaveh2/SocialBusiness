package ir.rasen.myapplication.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

public class ScrollViewNonFocusing extends ScrollView {

    public ScrollViewNonFocusing(Context context) {
        super(context);
    }

    public ScrollViewNonFocusing(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewNonFocusing(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return true;
    }

}