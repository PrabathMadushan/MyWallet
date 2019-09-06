package com.prabath.mywallet;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.prabath.mywallet.Listeners.SelectListener;
import com.prabath.mywallet.adapters.CategoryAdapter;
import com.prabath.mywallet.dialogs.MyDialog;

import java.util.ArrayList;
import java.util.List;

import database.firebase.firestore.FirestoreController;
import database.firebase.models.Category;


public class CategoriesActivity extends AppCompatActivity {

    private FirestoreController.CollectionCategories collectionCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
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
        collectionCategories = FirestoreController.newInstance().new CollectionCategories();
        categoryAdapter = new CategoryAdapter(categories, getEditListener(), getDeleteListener());
        recyclerView.setAdapter(categoryAdapter);
        showCategories();
    }

    private SelectListener<Category> getEditListener() {
        return (index, category) -> {
            Intent intent = new Intent(CategoriesActivity.this, AddNewCategoryActivity.class);
            intent.putExtra(AddNewCategoryActivity.TYPE, AddNewCategoryActivity.TYPE_EDIT);
            intent.putExtra(AddNewCategoryActivity.CATEGORY_ID, category.getId());
            startActivity(intent);
        };
    }

    private SelectListener<Category> getDeleteListener() {
        return (index, category) -> {
            final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case MyDialog.RESULT_YES:
                            collectionCategories.remove(category).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    categories.remove(index);
                                    categoryAdapter.notifyItemRemoved(index);
                                    categoryAdapter.notifyItemRangeChanged(index, categories.size());
                                }
                            });
                            break;
                        case MyDialog.RESULT_NO:
                            break;
                    }
                }
            };

            new MyDialog(
                    CategoriesActivity.this,
                    "Delete",
                    "Do you want to delete this category item?",
                    MyDialog.TYPE_QUESTION, listener
            ).show();

        };
    }

    private void showCategories() {
        collectionCategories.getAll(list -> {
            categories.addAll(list);
            categoryAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
        collectionCategories.getAllDefaults(list -> {
            categories.addAll(list);
            categoryAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    public void gotoAddCategory(View view) {
        YoYo.with(Techniques.RubberBand).duration(500).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                Intent intent = new Intent(CategoriesActivity.this, AddNewCategoryActivity.class);
                intent.putExtra(AddNewCategoryActivity.TYPE, AddNewCategoryActivity.TYPE_NEW);
                startActivity(intent);
            }
        }).playOn(view);
    }

    public void gotoHome(View view) {
        YoYo.with(Techniques.RubberBand).duration(500).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                startActivity(new Intent(CategoriesActivity.this, MainActivity.class));
            }
        }).playOn(view);
    }


    /*
    *
    final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case MyDialog.RESULT_YES:
                                tableCategory.remove(categories.get(i));
                                categories.remove(i);
                                notifyItemRemoved(i);
                                notifyItemRangeChanged(i, categories.size());
                                break;
                            case MyDialog.RESULT_NO:
                                break;
                        }
                    }
                };

                 new MyDialog(
                                myViewHolder.root.getContext(),
                                "Delete",
                                "Do you want to delete this category item?",
                                MyDialog.TYPE_QUESTION, listener
                        ).show();


                        Intent intent = new Intent(context, AddNewCategoryActivity.class);
                        intent.putExtra(AddNewCategoryActivity.TYPE, AddNewCategoryActivity.TYPE_EDIT);
                        intent.putExtra(AddNewCategoryActivity.CATEGORY_ID, category.getId());
                        context.startActivity(intent);
    *
    *
    *
    * */
}
