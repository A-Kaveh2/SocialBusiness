package ir.rasen.myapplication.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import ir.rasen.myapplication.R;

public class EditTextFont extends EditText {
  public EditTextFont(Context context,AttributeSet attrs,int defStyle){
    super(context,attrs,defStyle);
    init();
  }
  public EditTextFont(Context context,AttributeSet attrs){
    super(context,attrs);
    init();
  }
  public EditTextFont(Context context){
    super(context);
    init();
  }

  private void init(){
    if(!isInEditMode()){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/font.ttf");
        setTypeface(tf);
    }
  }

    public void setErrorC(String error) {
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(getResources().getColor(android.R.color.white));
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(error);
        ssbuilder.setSpan(fgcspan, 0, error.length(), 0);
        setError(ssbuilder);
    }
}