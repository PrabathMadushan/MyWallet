package com.prabath.mywallet.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prabath.mywallet.Listeners.CategorySelectListener;
import com.prabath.mywallet.Others.CategoryIcons;
import com.prabath.mywallet.R;

import java.util.List;

import database.firebase.models.Category;
import database.firebase.models.CategoryType;

//import database.local.models.Category;
//import database.local.models.CategoryType;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.MyViewHolder> {

    private List<Category> categories;
    private CategorySelectListener listener;


    public CategoryItemAdapter(List<Category> categories,CategorySelectListener listener) {
        this.categories = categories;
        this.listener=listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_category_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Category category = categories.get(i);

        myViewHolder.name.setText(category.getName());
        myViewHolder.icon.setImageResource(CategoryIcons.getInstance().getIcon(category.getIcon()));
        if (category.getType()== CategoryType.INCOME){
            myViewHolder.icon.setBackgroundResource(R.drawable.style_button_circle_blue);
        }else{
            myViewHolder.icon.setBackgroundResource(R.drawable.style_button_circle_rose);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myViewHolder.icon.setTransitionName(category.getName());
        }
        //ViewCompat.setTransitionName(myViewHolder.icon,category.getName());

        myViewHolder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelect(myViewHolder.getAdapterPosition(),category,myViewHolder.icon,myViewHolder.name);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView icon;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.catname);
            icon = itemView.findViewById(R.id.caticon);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
