package com.prabath.mywallet.Others;

import java.text.DecimalFormat;

public class Commons {

    public static String formatCurrency(float amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(amount);
    }
}
