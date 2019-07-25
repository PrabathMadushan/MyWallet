package com.prabath.mywallet.fregments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.prabath.mywallet.Others.CategoryIcons;
import com.prabath.mywallet.R;

import database.local.models.Category;
import database.local.models.CategoryType;


public class CategoryItemFragment extends Fragment {
    private static final String ARG_CATEGORY = "param1";


    private Category category;


    public CategoryItemFragment() {
        // Required empty public constructor
    }


    public static CategoryItemFragment newInstance(Category category) {
        CategoryItemFragment fragment = new CategoryItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = (Category) getArguments().getSerializable(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_item, container, false);
    }

    private Activity a;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        a = getActivity();
        TextView txtName = a.findViewById(R.id.catname);
        ImageView imgIcon = a.findViewById(R.id.caticon);
        txtName.setText(category.getName());
        imgIcon.setImageResource(CategoryIcons.getInstance().getIcon(Integer.parseInt(category.getIcon())));
        if (category.getType()==CategoryType.INCOME){
            imgIcon.setBackgroundResource(R.drawable.style_button_circle_blue);
        }else{
            imgIcon.setBackgroundResource(R.drawable.style_button_circle_rose);
        }
    }
}
