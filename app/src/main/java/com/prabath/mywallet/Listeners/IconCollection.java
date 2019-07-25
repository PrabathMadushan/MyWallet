package com.prabath.mywallet.Listeners;

import java.io.Serializable;

public interface IconCollection extends Serializable {
    int getIcon(int id);
    int getId(int id);
    int getIconCount();
}
