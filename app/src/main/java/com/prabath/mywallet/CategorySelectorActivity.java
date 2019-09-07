package com.prabath.mywallet;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.Listeners.CategorySelectListener;
import com.prabath.mywallet.adapters.CategoryItemAdapter;

import java.util.ArrayList;

import database.firebase.firestore.FirestoreController;
import database.firebase.models.Category;
import database.firebase.models.CategoryType;
import database.firebase.models.GLocation;
import database.firebase.models.Record;

//import database.local.LocalDatabaseController;
//import database.local.LocalDatabaseHelper;
//import database.local.models.Category;
//import database.local.models.CategoryType;
//import database.local.models.GLocation;
//import database.local.models.Record;

public class CategorySelectorActivity extends AppCompatActivity implements CategorySelectListener {

    private Record record;

//    private LocalDatabaseController.TableCategory tableCategory;

    private FirestoreController.CollectionCategories collectionCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selector);
        Intent intent = getIntent();
        record = (Record) intent.getSerializableExtra(AccountActivity.EXTRA_RECORD);
        init();
        setupRecycleView();
    }

    private RecyclerView recyclerView;
    private CategoryItemAdapter adapter;
    private ArrayList<Category> categories;


    private void init() {
        //tableCategory = LocalDatabaseController.getInstance(LocalDatabaseHelper.getInstance(this)).new TableCategory();
        collectionCategories = FirestoreController.newInstance().new CollectionCategories();
        final Button income = findViewById(R.id.btnIncome);
        final Button expense = findViewById(R.id.btnExpense);
        final ConstraintLayout wraper = findViewById(R.id.iconsWraper);

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                income.setTextColor(getResources().getColor(android.R.color.white));
                expense.setTextColor(getResources().getColor(android.R.color.black));

                income.setBackgroundResource(R.drawable.style_flat_button_blue);
                expense.setBackgroundResource(R.drawable.style_flat_button_rose_not_selected);


                YoYo.with(Techniques.FadeOutRight).onEnd(animator -> {
                    showCatIncomes(wraper);

                }).duration(200).playOn(wraper);
            }
        });

        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense.setTextColor(getResources().getColor(android.R.color.white));
                income.setTextColor(getResources().getColor(android.R.color.black));

                expense.setBackgroundResource(R.drawable.style_flat_button_rose);
                income.setBackgroundResource(R.drawable.style_flat_button_blue_not_selected);

                YoYo.with(Techniques.FadeOutLeft).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        showCatExpences(wraper);

                    }
                }).duration(200).playOn(wraper);
            }
        });
    }


    private boolean firstTime = true;

    private void setupRecycleView() {
        if (firstTime) {
            categories = new ArrayList<>();
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            GridLayoutManager layoutManager;
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = new GridLayoutManager(this, 8);
            } else {
                layoutManager = new GridLayoutManager(this, 4);
            }
            recyclerView.setLayoutManager(layoutManager);
            adapter = new CategoryItemAdapter(categories, this);
            recyclerView.setAdapter(adapter);
            showCatExpences(null);
            firstTime = false;
        } else {
            GridLayoutManager layoutManager;
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = new GridLayoutManager(this, 8);
            } else {
                layoutManager = new GridLayoutManager(this, 4);
            }
            recyclerView.setLayoutManager(layoutManager);
        }
    }


    private void showCatExpences(ConstraintLayout wraper) {
        //List<Category> cs = tableCategory.get(Category.FIELD_TYPE + "='" + CategoryType.EXPENSE.toString() + "'");
        categories.clear();
        adapter.notifyDataSetChanged();
        collectionCategories.getByType(CategoryType.EXPENSE, cat -> {
            categories.addAll(cat);
            adapter.notifyDataSetChanged();
            if (wraper != null) YoYo.with(Techniques.FadeInRight).duration(200).playOn(wraper);
        });

    }

    private void showCatIncomes(ConstraintLayout wraper) {
//        List<Category> cs = tableCategory.get(Category.FIELD_TYPE + "='" + CategoryType.INCOME.toString() + "'");
        categories.clear();
        adapter.notifyDataSetChanged();
        collectionCategories.getByType(CategoryType.INCOME, cats -> {
            categories.addAll(cats);
            adapter.notifyDataSetChanged();
            if (wraper != null) YoYo.with(Techniques.FadeInLeft).duration(200).playOn(wraper);
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupRecycleView();
    }

    private final int RESULT_RECORD = 10;

    @Override
    public void onSelect(int position, Category category, ImageView icon, TextView name) {
        Intent intent = new Intent(this, AddNewRecordActivity.class);
        record.setCategory(category.getId());
        if (record.getLocation() == null) {
            if (category.getName().equals("Transportation")) {
                record.setLocation(new GLocation(GLocation.Type.ROUTE));
            } else {
                record.setLocation(new GLocation(GLocation.Type.LOCATION));
            }
        } else {
            if (category.getName().equals("Transportation")) {
                if (record.getLocation().getType() == GLocation.Type.LOCATION)
                    record.setLocation(new GLocation(GLocation.Type.ROUTE));
                record.getLocation().setType(GLocation.Type.ROUTE);
            } else {
                if (record.getLocation().getType() == GLocation.Type.ROUTE)
                    record.setLocation(new GLocation(GLocation.Type.LOCATION));
                record.getLocation().setType(GLocation.Type.LOCATION);
            }
        }
        intent.putExtra(AccountActivity.EXTRA_RECORD, record);
//        ViewCompat.getTransitionName(icon)
        Pair<View, String> p1 = Pair.create((View) icon, ViewCompat.getTransitionName(icon));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, p1
        );
        startActivityForResult(intent, RESULT_RECORD, options.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_RECORD) {
            record = (Record) data.getSerializableExtra(AccountActivity.EXTRA_RECORD);
        }
    }
}
