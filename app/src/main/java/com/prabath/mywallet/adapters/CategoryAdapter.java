package com.prabath.mywallet.adapters;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.AddNewCategoryActivity;
import com.prabath.mywallet.Others.CategoryIcons;
import com.prabath.mywallet.R;
import com.prabath.mywallet.dialogs.MyDialog;

import java.util.List;

import database.local.LocalDatabaseController;
import database.local.models.Category;
import database.local.models.CategoryType;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category> categories;
    private Context context;
    LocalDatabaseController.TableCategory tableCategory;


    public CategoryAdapter(List<Category> categories, Context context, LocalDatabaseController.TableCategory tableCategory) {
        this.categories = categories;
        this.context=context;
        this.tableCategory=tableCategory;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_category, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    int lastPosition = -1;

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Category category = categories.get(i);
        System.out.println("de:" + category.isDefault());
        if (category.isDefault()) {
            myViewHolder.edit.setEnabled(false);
            myViewHolder.delet.setEnabled(false);
            myViewHolder.edit.setAlpha(0.2f);
            myViewHolder.delet.setAlpha(0.2f);
            myViewHolder.edit.setBackgroundResource(R.drawable.style_button_desabled);
            myViewHolder.delet.setBackgroundResource(R.drawable.style_button_desabled);
        }else{
            myViewHolder.edit.setEnabled(true);
            myViewHolder.delet.setEnabled(true);
            myViewHolder.edit.setAlpha(1f);
            myViewHolder.delet.setAlpha(1f);
            myViewHolder.edit.setBackgroundResource(R.drawable.style_button_blue);
            myViewHolder.delet.setBackgroundResource(R.drawable.style_button_rose);
        }
        myViewHolder.name.setText(category.getName());
        myViewHolder.type.setText(category.getType().toString());
        myViewHolder.icon.setImageResource(CategoryIcons.getInstance().getIcon(Integer.parseInt(category.getIcon())));
        if (category.getType()== CategoryType.INCOME){
            myViewHolder.icon.setColorFilter(ContextCompat.getColor(myViewHolder.root.getContext(),R.color.primaryBlueDark), PorterDuff.Mode.SRC_IN);
        }else{
            myViewHolder.icon.setColorFilter(ContextCompat.getColor(myViewHolder.root.getContext(),R.color.primaryRoseDark), PorterDuff.Mode.SRC_IN);

        }
        if (i > lastPosition) {
            YoYo.with(Techniques.ZoomInUp).duration(200).playOn(myViewHolder.root);
            lastPosition = i;
        }
        myViewHolder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                YoYo.with(Techniques.ZoomIn).duration(200).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        new MyDialog(
                                myViewHolder.root.getContext(),
                                "Delete",
                                "Do you want to delete this category item?",
                                MyDialog.TYPE_QUESTION,listener
                        ).show();


                    }
                }).playOn(v);
            }
        });

        myViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.ZoomIn).duration(200).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        Intent intent=new Intent(context, AddNewCategoryActivity.class);
                        intent.putExtra(AddNewCategoryActivity.TYPE,AddNewCategoryActivity.TYPE_EDIT);
                        intent.putExtra(AddNewCategoryActivity.CATEGORY_ID,category.getId());
                        context.startActivity(intent);
                    }
                }).playOn(v);
            }
        });


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView type;
        public ImageView icon;
        public ConstraintLayout root;
        public ImageButton edit;
        public ImageButton delet;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.categoryItemWraper);
            name = itemView.findViewById(R.id.value);
            icon = itemView.findViewById(R.id.categoryIcon);
            edit = itemView.findViewById(R.id.btnEdit);
            delet = itemView.findViewById(R.id.btnDelete);
            type = itemView.findViewById(R.id.categoryType);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.RubberBand).duration(500).playOn(root);
                }
            });

        }
    }
}
