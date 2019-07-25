package com.prabath.mywallet.fregments;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.R;

public class TitleFragment extends Fragment {

    private static final String PARA_KEY_ICON = "ICON";
    private static final String PARA_KEY_TITLE = "TITLE";
    private static final String PARA_KEY_BACK = "BACK";

    private int icon;
    private String title;
    private Class back;

    public TitleFragment() {
    }

    public static TitleFragment getInstance(int icon, String title, Class back) {
        TitleFragment fragment = new TitleFragment();
        Bundle args = new Bundle();
        args.putInt(PARA_KEY_ICON, icon);
        args.putString(PARA_KEY_TITLE, title);
        args.putSerializable(PARA_KEY_BACK, back);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            icon = getArguments().getInt(PARA_KEY_ICON);
            title = getArguments().getString(PARA_KEY_TITLE);
            back = (Class) getArguments().getSerializable(PARA_KEY_BACK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_page_title, container, false);
    }

    private TextView vTitle;
    private ImageView vBack;
    private ImageView vIcon;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final FragmentActivity a = getActivity();
        vTitle = a.findViewById(R.id.titleText);
        vBack = a.findViewById(R.id.btnback);
        vIcon = a.findViewById(R.id.titleIcon);
        vTitle.setText(this.title);
        vIcon.setImageResource(this.icon);
        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.ZoomOutRight).duration(200).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        a.startActivity(new Intent(a, TitleFragment.this.back));
                    }
                }).playOn(vBack);

            }
        });
        vIcon.setAlpha(0f);
        vTitle.setAlpha(0f);
        vBack.setAlpha(0f);
    }

    @Override
    public void onStart() {
        super.onStart();
        YoYo.with(Techniques.ZoomIn).duration(500).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                YoYo.with(Techniques.ZoomIn).duration(200).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        YoYo.with(Techniques.FadeInRight).duration(200).playOn(vBack);
                    }
                }).playOn(vTitle);
            }
        }).playOn(vIcon);


    }


}
