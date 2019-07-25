package com.prabath.mywallet;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.adapters.CategoryAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database.local.LocalDatabaseController;
import database.local.LocalDatabaseHelper;
import database.local.models.Category;

public class CategoriesActivity extends AppCompatActivity {

    private LocalDatabaseController.TableCategory tableCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        setupTileLayout();
        setupRecycleView();
    }

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Category> categories;
    private CategoryAdapter categoryAdapter;

    private void setupRecycleView() {
        categories = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        tableCategory=LocalDatabaseController.getInstance(LocalDatabaseHelper.getInstance(getApplicationContext())).new TableCategory();
        categoryAdapter = new CategoryAdapter(categories,getApplicationContext(),tableCategory);
        recyclerView.setAdapter(categoryAdapter);
        showCategories();
    }

    private void showCategories() {
        List<Category> cs = LocalDatabaseController.getInstance(LocalDatabaseHelper.getInstance(getApplicationContext())).new TableCategory().get(null);
        Collections.reverse(cs);
        for (Category c : cs) {
            categories.add(c);
        }
        categoryAdapter.notifyDataSetChanged();
    }


    private void setupTileLayout() {
        TextView titleText = findViewById(R.id.titleText);
        titleText.setText("Categories");
        ImageView icon = findViewById(R.id.titleIcon);
        icon.setImageResource(R.drawable.ic_nui_categories);
    }

    private void applyEntranceAnimation() {
        final TextView title = findViewById(R.id.titleText);
        final ImageView icon = findViewById(R.id.titleIcon);
        title.setAlpha(0);
        YoYo.with(Techniques.Shake).duration(400).delay(200).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                YoYo.with(Techniques.FadeInRight).duration(200).playOn(title);
            }
        }).playOn(icon);
    }

    @Override
    protected void onStart() {
        super.onStart();
        applyEntranceAnimation();
    }


    public void gotoAddCategory(View view) {
        YoYo.with(Techniques.RubberBand).duration(500).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                Intent intent = new Intent(CategoriesActivity.this, AddNewCategoryActivity.class);
                intent.putExtra(AddNewCategoryActivity.TYPE,AddNewCategoryActivity.TYPE_NEW);
                startActivity(intent);
            }
        }).playOn(view);
    }

    public void gotoHome(View view) {
        YoYo.with(Techniques.RubberBand).duration(500).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                startActivity(new Intent(CategoriesActivity.this,MainActivity.class));
            }
        }).playOn(view);
    }
}
