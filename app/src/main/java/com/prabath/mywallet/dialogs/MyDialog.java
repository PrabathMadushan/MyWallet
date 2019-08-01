package com.prabath.mywallet.dialogs;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.R;

public class MyDialog extends Dialog {

    private DialogInterface.OnClickListener listener = null;
    public static final int TYPE_INFORMATION = 1;
    public static final int TYPE_WARNING = 2;
    public static final int TYPE_QUESTION = 3;
    public static final int TYPE_ERROR = 4;

    public static final int RESULT_YES = 5;
    public static final int RESULT_NO = 6;

    private String title;
    private String message;
    private int type;

    public MyDialog(Context context, String title, String message, int type) {
        super(context);
        this.title = title;
        this.message = message;
        this.type = type;
    }

    public MyDialog(Context context, String title, String message, int type, DialogInterface.OnClickListener listener) {
        super(context);
        this.title = title;
        this.message = message;
        this.type = type;
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_custom);
        TextView txtTitle = findViewById(R.id.title);
        TextView txtMessage = findViewById(R.id.message);
        txtTitle.setText(title);
        txtMessage.setText(message);
        Button no = findViewById(R.id.btnNo);
        no.setVisibility(View.INVISIBLE);
        switch (type) {
            case TYPE_INFORMATION:
                decorateIcon(R.drawable.ic_nui_info, R.drawable.image_alert_infor);
                break;
            case TYPE_QUESTION:
                decorateIcon(R.drawable.ic_nui_question, R.drawable.image_alert_quection);
                no.setVisibility(View.VISIBLE);
                break;
            case TYPE_WARNING:
                decorateIcon(R.drawable.ic_nui_warning, R.drawable.image_alert_warning);
                break;
            case TYPE_ERROR:
                decorateIcon(R.drawable.ic_nui_error, R.drawable.image_alert_error);
                break;
        }
        doAction();


    }

    private void decorateIcon(int icon, int background) {
        ImageView iconview = findViewById(R.id.icon);
        iconview.setImageResource(icon);
    }

    private void doAction() {
        Button no = findViewById(R.id.btnNo);
        Button yes = findViewById(R.id.btnYes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Bounce).duration(200).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        if (type == TYPE_QUESTION) {
                            listener.onClick(MyDialog.this, RESULT_YES);
                        }
                        dismiss();
                    }
                }).playOn(v);

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Bounce).duration(200).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        listener.onClick(MyDialog.this, RESULT_NO);
                        dismiss();
                    }
                }).playOn(v);
            }
        });
    }


}
