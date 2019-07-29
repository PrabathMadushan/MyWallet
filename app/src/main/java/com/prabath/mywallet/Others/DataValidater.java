package com.prabath.mywallet.Others;

import android.view.View;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.R;

public class DataValidater {

    public static boolean validateText(EditText textView) {
        if (textView.getText().toString().trim().isEmpty()) {
            textView.setBackgroundResource(R.drawable.style_textbox_error);

            applyAnimation(textView);

            return false;
        } else {
            textView.setBackgroundResource(R.drawable.style_textbox_normal);
            return true;
        }
    }

    public static boolean validatePassword(EditText p1, EditText p2) {
        if (p1.getText().toString().equals(p2.getText().toString())) {
            if (p1.getText().toString().length() < 8) {
                p1.setBackgroundResource(R.drawable.style_textbox_error);
                p2.setBackgroundResource(R.drawable.style_textbox_error);
                applyAnimation(p1);
                applyAnimation(p2);
                return false;
            } else {
                p1.setBackgroundResource(R.drawable.style_textbox_normal);
                p2.setBackgroundResource(R.drawable.style_textbox_normal);
                return true;
            }
        } else {
            p1.setBackgroundResource(R.drawable.style_textbox_error);
            p2.setBackgroundResource(R.drawable.style_textbox_error);
            applyAnimation(p1);
            applyAnimation(p2);
            return false;
        }
    }

    private static void applyAnimation(View view) {
        YoYo.with(Techniques.Shake).duration(200).playOn(view);
    }
}
