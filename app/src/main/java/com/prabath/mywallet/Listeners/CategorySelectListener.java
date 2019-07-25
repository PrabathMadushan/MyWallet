package com.prabath.mywallet.Listeners;

import android.widget.ImageView;
import android.widget.TextView;

import database.local.models.Category;

public interface CategorySelectListener {
    void onSelect(int position, Category category, ImageView icon, TextView name);
}
