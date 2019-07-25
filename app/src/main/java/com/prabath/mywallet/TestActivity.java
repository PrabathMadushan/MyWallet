package com.prabath.mywallet;


import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;


public class TestActivity extends AppCompatActivity {

    private ConstraintLayout layout;
    private ConstraintSet constraintSetOld=new ConstraintSet();
    private ConstraintSet constraintSetNew=new ConstraintSet();
    private boolean altLayout=true;

           // <!--android:windowSoftInputMode="stateVisible|adjustResize"-->

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        layout=findViewById(R.id.root);
        constraintSetOld.clone(layout);
        constraintSetNew.clone(this,R.layout.activity_test_softkeybord);
        EditText editText = findViewById(R.id.editText7);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ChangeBounds changeBounds = new ChangeBounds();
                changeBounds.setInterpolator(new OvershootInterpolator());
                TransitionManager.beginDelayedTransition(layout,changeBounds);
                    constraintSetNew.applyTo(layout);
                    altLayout=false;

            }
        });
    }

    @Override
    public void onBackPressed() {
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setInterpolator(new OvershootInterpolator());
        TransitionManager.beginDelayedTransition(layout,changeBounds);
        constraintSetOld.applyTo(layout);
        altLayout=true;
    }
}
