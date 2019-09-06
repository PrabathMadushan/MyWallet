package com.prabath.mywallet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.Listeners.SelectListener;
import com.prabath.mywallet.Others.AccountIcons;
import com.prabath.mywallet.R;

import java.util.List;

import database.firebase.models.Account;


public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {

    private List<Account> accounts;
    private SelectListener<Account> editListener;
    private SelectListener<Account> deleteListener;
    private SelectListener<Account> selectListener;


    public AccountAdapter(List<Account> accounts, SelectListener<Account> editListener, SelectListener<Account> deleteListener, SelectListener<Account> selectListener) {
        this.accounts = accounts;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
        this.selectListener = selectListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_account, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    //int lastPosition = -1;

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Account account = accounts.get(i);
        myViewHolder.icon.setImageResource(AccountIcons.getInstance().getIcon(account.getIcon()));
        myViewHolder.name.setText(account.getName());
        if (account.isDefaultx()) {
            myViewHolder.edit.setEnabled(false);
            myViewHolder.delete.setEnabled(false);
            myViewHolder.edit.setAlpha(0.2f);
            myViewHolder.delete.setAlpha(0.2f);
            myViewHolder.edit.setBackgroundResource(R.drawable.style_button_desabled);
            myViewHolder.delete.setBackgroundResource(R.drawable.style_button_desabled);
        } else {
            myViewHolder.edit.setEnabled(true);
            myViewHolder.delete.setEnabled(true);
            myViewHolder.edit.setAlpha(1f);
            myViewHolder.delete.setAlpha(1f);
            myViewHolder.edit.setBackgroundResource(R.drawable.style_button_blue);
            myViewHolder.delete.setBackgroundResource(R.drawable.style_button_rose);
        }
        myViewHolder.edit.setOnClickListener(v -> YoYo.with(Techniques.RubberBand).duration(500).onEnd(a -> editListener.select(i, account)).playOn(v));
        myViewHolder.delete.setOnClickListener(v -> YoYo.with(Techniques.RubberBand).duration(500).onEnd(a -> deleteListener.select(i, account)).playOn(v));
        myViewHolder.root.setOnClickListener(v -> YoYo.with(Techniques.RubberBand).duration(500).onEnd(a -> selectListener.select(i, account)).playOn(v));
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        ImageButton edit;
        ImageButton delete;
        TextView name;
        TextView income;
        TextView expense;
        TextView balance;
        CardView root;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            edit = itemView.findViewById(R.id.edit);
            income = itemView.findViewById(R.id.income);
            expense = itemView.findViewById(R.id.expense);
            balance = itemView.findViewById(R.id.balunce);
            root = itemView.findViewById(R.id.card_view);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
