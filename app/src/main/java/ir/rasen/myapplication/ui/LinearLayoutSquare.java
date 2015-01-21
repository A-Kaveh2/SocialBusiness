package ir.rasen.myapplication.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LinearLayoutSquare extends LinearLayout {//implements View.OnTouchListener {

	//boolean exed=true;
	//float startY=0, befY=0;

public LinearLayoutSquare(final Context context) {
    super(context);
    //init(context, null);
}

public LinearLayoutSquare(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    //init(context, attrs);
}

@Override
protected void onMeasure(int width, int height) {
    super.onMeasure(width, height);
    setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); 
    //if(startY==0) measureNow();
}

/*public void clicked() {
	if(exed)
		exed=false;
	else
		exed=true;
	measureNow();
}

void measureNow() {
	MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
    if(exed) {
    	params.topMargin=-getMeasuredWidth()/4;
    	setLayoutParams(params);
    	setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()*3/4);
    } else {
    	params.topMargin=0;
    	setLayoutParams(params);
    	setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());    	
    }
}*/
/*
private void init(Context context, AttributeSet attrs) {
    // set scroll mode
    setOverScrollMode(OVER_SCROLL_NEVER);
    setOnTouchListener(this);
}

@Override
public boolean onTouch(View arg0, MotionEvent event) {
    int action = event.getAction();

    switch (action) {
    
    case MotionEvent.ACTION_DOWN:
    	startY = event.getY();
    	break;
    case MotionEvent.ACTION_MOVE:
        doActionMove(event);
        break;

    case MotionEvent.ACTION_UP:
        //exed=false;
       //measureNow();
    	startY=0;
        break;

    default:
        break;
    }
	return false;
}

private void doActionMove(MotionEvent event) {
	if(abs(befY-event.getY())<5) return;
	befY=event.getY();
	int top = -getMeasuredWidth()/4 + (int) (event.getY() - startY);
	if(top>0) top=0;
	int measuredHeight = getMeasuredWidth()+top;
	MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
	params.topMargin=top;
	setLayoutParams(params);
	setMeasuredDimension(getMeasuredWidth(), measuredHeight);
}

private float abs(float f) {
	return (f>0 ? f : -f);
}*/

}