package com.nimus.android.Customs;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class NiMusButton extends AppCompatButton {
    private Boolean isLoading = true;

    public NiMusButton(Context context) {
        super(context);
    }

    public NiMusButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NiMusButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setButtonWhileLoading(CharSequence text, int colorText, int backgroundColor){
        setBackgroundColor(backgroundColor);
        setTextColor(colorText);
        setText(text);
        isLoading = true;
    }

    public void setButtonAfterLoading(CharSequence text, int colorText, int backgroundColor){
        setBackgroundColor(backgroundColor);
        setTextColor(colorText);
        setText(text);
        isLoading = false;
    }

    public boolean isLoading(){
        if(isLoading){
            return true;
        }

        else{
            return false;
        }
    }

}
