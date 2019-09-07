package com.prabath.mywallet.Listeners;

import database.firebase.models.Record;

public interface RecordSelectListener {
    void onSelect(int position, Record record);
}
