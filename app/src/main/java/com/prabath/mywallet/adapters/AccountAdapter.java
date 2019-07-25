package com.prabath.mywallet.adapters;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.prabath.mywallet.AddNewAccountActivity;
import com.prabath.mywallet.Listeners.AccountISelectListener;
import com.prabath.mywallet.Others.AccountIcons;
import com.prabath.mywallet.R;
import com.prabath.mywallet.dialogs.MyDialog;

import java.util.List;

import database.local.LocalDatabaseController;
import database.local.models.Account;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {

    private List<Account> accounts;
    private Context context;
    private LocalDatabaseController.TableAccount tableAccount;
    private AccountISelectListener listener;


    public AccountAdapter(List<Account> accounts, Context context, LocalDatabaseController.TableAccount tableAccount,AccountISelectListener listener) {
        this.accounts = accounts;
        this.context = context;
        this.tableAccount = tableAccount;
        this.listener=listener;
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
        myViewHolder.icon.setImageResource(AccountIcons.getInstance().getIcon(Integer.parseInt(account.getIcon())));
        myViewHolder.name.setText(account.getName());
        if (account.isDefault()) {
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
        myViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.ZoomIn).duration(200).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        Intent intent = new Intent(context, AddNewAccountActivity.class);
                        intent.putExtra(AddNewAccountActivity.TYPE, AddNewAccountActivity.TYPE_EDIT)
                                .putExtra(AddNewAccountActivity.ACCOUNT_ID, account.getId());
                        context.startActivity(intent);
                    }
                }).playOn(v);
            }
        });
        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.ZoomIn).duration(200).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        new MyDialog(
                                myViewHolder.root.getContext(),
                                "Delete",
                                "Do you want to delete this category item?",
                                MyDialog.TYPE_QUESTION,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case MyDialog.RESULT_YES:
                                                tableAccount.remove(account);
                                                accounts.remove(i);
                                                notifyItemRemoved(i);
                                                notifyItemRangeChanged(i, accounts.size());
                                                break;
                                        }
                                    }
                                }
                        ).show();
                    }
                }).playOn(v);
            }
        });

        myViewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.RubberBand).duration(500).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        listener.onSelect(account);
                    }
                }).playOn(myViewHolder.root);
            }
        });



    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public ImageButton edit;
        public ImageButton delete;
        public TextView name;
        public TextView income;
        public TextView expense;
        public TextView balunce;
        public CardView root;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            edit = itemView.findViewById(R.id.edit);
            income = itemView.findViewById(R.id.income);
            expense = itemView.findViewById(R.id.expense);
            balunce = itemView.findViewById(R.id.balunce);
            root = itemView.findViewById(R.id.card_view);
            delete = itemView.findViewById(R.id.delete);



        }
    }
}
