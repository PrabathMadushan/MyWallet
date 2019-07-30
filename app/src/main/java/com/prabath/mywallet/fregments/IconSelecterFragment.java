package com.prabath.mywallet.fregments;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.Listeners.IconCollection;
import com.prabath.mywallet.R;


public class IconSelecterFragment extends Fragment {

    private static final String PARA_KEY_COLLECTION = "icons";
    private static final String PARA_KEY_CURRENT = "current";
    private IconCollection icons;
    private Activity a;
    private ImageView iconPreview;
    private int current;

    public IconSelecterFragment() {

    }


    public static IconSelecterFragment getInstance(IconCollection icons, int current) {
        IconSelecterFragment fragment = new IconSelecterFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARA_KEY_COLLECTION, icons);
        args.putInt(PARA_KEY_CURRENT,current);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            icons = (IconCollection) getArguments().getSerializable(PARA_KEY_COLLECTION);
            current =getArguments().getInt(PARA_KEY_CURRENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_icon_selecter, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        a = getActivity();
        iconPreview = a.findViewById(R.id.iconPreview);
        iconPreview.setImageResource(current);
        iconPreview.setTag(icons.getId(current));
        addIconsToTableLayout();
    }

    private void addIconsToTableLayout() {

        TableLayout tableLayout = a.findViewById(R.id.tableIcons);
        int c = (icons.getIconCount() - (icons.getIconCount() % 4)) / 4;
        int k = 0;
        for (int i = 0; i < c; i++) {
            TableRow row = new TableRow(getContext());
            row.setMinimumHeight(150);
            row.setPadding(0, 15, 0, 0);
            tableLayout.addView(row);
            ViewGroup.LayoutParams params = row.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            row.setLayoutParams(params);
            for (int j = 0; j < 4; j++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setTag(k);
                imageView.setImageResource(icons.getIcon(k));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseIcon(v);
                    }
                });
                row.addView(imageView);
                ViewGroup.LayoutParams params1 = imageView.getLayoutParams();
                params1.width = 30 * 3;
                params1.height = 30 * 3;
                imageView.setLayoutParams(params1);
                k++;
            }

        }


    }

    private void chooseIcon(View view) {
        final ImageView iconView = (ImageView) view;
        YoYo.with(Techniques.ZoomIn).duration(500).playOn(iconView);
        YoYo.AnimationComposer an = YoYo.with(Techniques.FadeOut).duration(200);
        an.onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                iconPreview.setTag(iconView.getTag());
                int tag = (int) iconView.getTag();
                iconPreview.setImageResource(icons.getIcon(tag));
                YoYo.with(Techniques.FadeIn).duration(500).playOn(iconPreview);
            }
        });
        an.playOn(iconPreview);
    }

    public int getSelectedIconId() {
        return Integer.parseInt(iconPreview.getTag().toString());
    }

}
