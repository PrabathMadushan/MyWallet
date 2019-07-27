package com.prabath.mywallet.Listeners;

import database.local.models.Record;

public interface RecordSelectListener {
    void onSelect(int position, Record record);
}
