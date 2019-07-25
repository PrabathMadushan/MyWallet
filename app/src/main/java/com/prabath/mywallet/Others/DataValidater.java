package com.prabath.mywallet.Others;

import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.R;

public class DataValidater {

    public static boolean validateText(TextView textView){
        if(textView.getText().toString().trim().isEmpty()){
            textView.setBackgroundResource(R.drawable.style_textbox_error);

            applyAnimation(textView);

            return false;
        }else{
            textView.setBackgroundResource(R.drawable.style_textbox_normal);
            return true;
        }
    }

    private static void applyAnimation(View view){
        YoYo.with(Techniques.Shake).duration(200).playOn(view);
    }
}
