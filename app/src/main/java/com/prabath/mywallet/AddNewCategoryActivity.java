package com.prabath.mywallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.prabath.mywallet.Others.CategoryIcons;
import com.prabath.mywallet.Others.DataValidater;
import com.prabath.mywallet.fregments.IconSelecterFragment;

import java.util.Date;

import database.firebase.auth.AuthController;
import database.firebase.firestore.FirestoreController;
import database.firebase.models.Category;
import database.firebase.models.CategoryType;

public class AddNewCategoryActivity extends AppCompatActivity {

    public static final String TYPE = "type";
    public static final String TYPE_EDIT = "edit";
    public static final String TYPE_NEW = "new";
    public static final String CATEGORY_ID = "ct_id";

    private String type;
    private Category editCategory;
    IconSelecterFragment iconSelecterFragment;


    private RadioButton income;
    private RadioButton expense;
    private Button actionButton;
    private EditText name;

    private FirestoreController.CollectionCategories collectionCategories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);
        //icons = CategoryIcons.newInstance();
        initComponents();
        Intent intent = getIntent();
        type = intent.getStringExtra(TYPE);
        if (type.equals(TYPE_EDIT)) {
            String id = intent.getStringExtra(CATEGORY_ID);
            loadCategory(id);
        } else {
            setupLayoutAddNew();
        }
    }

    public void addFragment(int current) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        iconSelecterFragment = IconSelecterFragment.getInstance(CategoryIcons.getInstance(), current);
        transaction.replace(R.id.fragment_wraper, iconSelecterFragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void loadCategory(String id) {
        collectionCategories.getById(id, list -> {
            editCategory = list.get(0);
            setupLayoutEdit();
        }).addOnFailureListener(e -> editCategory = null);
    }

    private void initComponents() {

        name = findViewById(R.id.txtName);
        expense = findViewById(R.id.rdbExpence);
        income = findViewById(R.id.rdbIncome);

        actionButton = findViewById(R.id.btnCancel);
        collectionCategories = FirestoreController.newInstance().new CollectionCategories();
    }

    private void setupLayoutAddNew() {

        actionButton.setText("ADD");
        addFragment(CategoryIcons.getInstance().getIcon(0));
    }

    private void setupLayoutEdit() {
        actionButton.setText("UPDATE");
        if (editCategory.getType().equals(CategoryType.EXPENSE)) {
            expense.setChecked(true);
        } else {
            income.setChecked(true);
        }
        name.setText(editCategory.getName());
        addFragment(CategoryIcons.getInstance().getIcon(editCategory.getIcon()));
    }


    public void gotoCategoryActivity(View view) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }


    public void onActionButtonClick(View view) {
        if (type.equals(TYPE_EDIT)) {
            updateCategory(view);
        } else {
            saveCategory(view);
        }
    }

    public void updateCategory(final View view) {
        CategoryType type = getCategoryType();
        if (DataValidater.validateText(name) && type != null) {
            editCategory.setName(name.getText().toString());
            editCategory.setIcon(iconSelecterFragment.getSelectedIconId());
            editCategory.setType(type);
            collectionCategories.update(editCategory).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AddNewCategoryActivity.this, "Category Updated", Toast.LENGTH_SHORT).show();
                    gotoCategoryActivity(view);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
    }

    public void saveCategory(final View view) {
        CategoryType type = getCategoryType();
        if (DataValidater.validateText(name) && type != null) {
            Category category = new Category();
            category.setUser(AuthController.newInstance().getUser().getId());
            category.setName(name.getText().toString());
            category.setIcon(iconSelecterFragment.getSelectedIconId());
            Date today = new Date();
            category.setDateTime(today);
            category.setType(type);
            collectionCategories.add(category).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AddNewCategoryActivity.this, "Category Added", Toast.LENGTH_SHORT).show();
                    gotoCategoryActivity(view);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please fill data correctly", Toast.LENGTH_SHORT).show();
        }
    }

    private CategoryType getCategoryType() {
        income = findViewById(R.id.rdbIncome);
        expense = findViewById(R.id.rdbExpence);
        income.isSelected();
        if (income.isChecked()) {
            return CategoryType.INCOME;
        } else if (expense.isChecked()) {
            return CategoryType.EXPENSE;
        } else {
            Toast.makeText(this, "Please select the category type", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


}
