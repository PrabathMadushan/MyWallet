package com.prabath.mywallet.adapters;

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
import com.prabath.mywallet.Listeners.RecordSelectListener;
import com.prabath.mywallet.Others.CategoryIcons;
import com.prabath.mywallet.Others.Commons;
import com.prabath.mywallet.R;

import java.util.List;

import database.firebase.models.CategoryType;
import database.firebase.models.Record;


public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder> {

    private List<Record> records;
    private RecordSelectListener listener;


    public RecordAdapter(List<Record> records, RecordSelectListener listener) {
        this.records = records;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecordAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_record, parent, false);
        return new MyViewHolder(view);
    }

    private int lastPosition = -1;

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.MyViewHolder holder, int position) {
        Record record = records.get(position);
        holder.value.setText(Commons.formatCurrency(record.getValue()));
        record.getCategoryX(c -> {
            holder.categoryIcon.setImageResource(CategoryIcons.getInstance().getIcon(c.get(0).getIcon()));

            if (c.get(0).getType() == CategoryType.EXPENSE) {
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.root.getContext(), R.color.primaryRoseDark), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                holder.categoryIcon.setColorFilter(ContextCompat.getColor(holder.root.getContext(), R.color.primaryBlueDark), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            if (position > lastPosition) {
                YoYo.with(Techniques.ZoomInUp).duration(200).playOn(holder.root);
                lastPosition = position;
            }
        });


    }

    @Override
    public int getItemCount() {
        return records.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView categoryIcon;
        public TextView value;
        public ImageButton delete;
        public ImageButton edit;
        public ConstraintLayout root;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            value = itemView.findViewById(R.id.value);
            delete = itemView.findViewById(R.id.btnDelete);
            edit = itemView.findViewById(R.id.btnEdit);
            root = itemView.findViewById(R.id.recordItemWraper);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.RubberBand).duration(500).playOn(root);
                }
            });
        }
    }
}
