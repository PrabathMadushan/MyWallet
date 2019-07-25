package com.prabath.mywallet.fregments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.prabath.mywallet.R;

import java.util.List;

import database.local.LocalDatabaseController;
import database.local.LocalDatabaseHelper;
import database.local.models.Category;
import database.local.models.CategoryType;


public class CategoryIconsFragment extends Fragment {
    private static final String ARG_TYPE = "param1";

    private CategoryType type;


    public CategoryIconsFragment() {
        // Required empty public constructor
    }


    public static CategoryIconsFragment newInstance(CategoryType type) {
        CategoryIconsFragment fragment = new CategoryIconsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = (CategoryType) getArguments().getSerializable(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_icons, container, false);
    }

    private Activity a;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        a = getActivity();
        TableLayout tableLayout = a.findViewById(R.id.iconList);
        int i = 1;
        TableRow row = new TableRow(getContext());
        ViewGroup.LayoutParams layoutParams = row.getLayoutParams();
        layoutParams.width=ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height=ViewGroup.LayoutParams.WRAP_CONTENT;
        row.setLayoutParams(layoutParams);
        for (Category c : getCategories()) {
            ConstraintLayout layout=new ConstraintLayout(getContext());
            ViewGroup.LayoutParams layoutParams1 = layout.getLayoutParams();
            layoutParams1.width=ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams1.height=ViewGroup.LayoutParams.MATCH_PARENT;
            row.addView(layout);

            CategoryItemFragment itemFragment=CategoryItemFragment.newInstance(c);

            if (i % 4 == 0) {
                tableLayout.addView(row);
                row = new TableRow(getContext());
            }
        }

    }

    private List<Category> getCategories() {
        return LocalDatabaseController.getInstance(LocalDatabaseHelper.getInstance(getContext()))
                .new TableCategory().get(Category.FIELD_TYPE + "='" + type.toString() + "'");
    }
}
