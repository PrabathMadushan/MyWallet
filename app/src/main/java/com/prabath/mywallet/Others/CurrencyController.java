package com.prabath.mywallet.Others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.prabath.mywallet.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CurrencyController {

    private final Context context;
    private Currency base;
    private Currency quote;

    public CurrencyController(Context context) {
        this.context = context;
        this.base = new Currency("LKR", "Sri Lankan Rupee", Uri.parse("https://www.countryflags.io/lk/shiny/64.png"));
        this.quote = new Currency("USD", "United States Dollar", Uri.parse("https://www.countryflags.io/us/shiny/64.png"));
        convert((b, q) -> {
        }, error -> {
        });
    }


    public void convert(ConvertCompleteListener listener, Response.ErrorListener errorListener) {
        getValue(base, baseValue -> getValue(quote, quoteValue -> {
            float k = quoteValue / baseValue;
            quote.setValue(base.getValue() * k);
            listener.complete(base, quote);
        }, errorListener), errorListener);
    }


    public void swap() {
        Currency xBase = base;
        base = quote;
        quote = xBase;
        convert((b, q) -> {
        }, error -> {
        });
    }

    private void getValue(Currency currency, LoadCompleteListener<Float> CompletListener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.fixer_url) + "/latest?access_key=" + context.getString(R.string.fixer_api_key) + "&symbols=" + currency.getCode();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            JsonElement element = jsonObject.get("rates");
            JsonObject jsonObject1 = element.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entries = jsonObject1.entrySet();
            float value = -1;
            for (Map.Entry<String, JsonElement> entry : entries) {
                value = entry.getValue().getAsFloat();
            }
            CompletListener.complete(value);
        }, errorListener);
        requestQueue.add(stringRequest);
    }

    private double getK() {
        return quote.getValue() / base.getValue();
    }

    int xx;

    private void getHistory(LoadCompleteListener<ArrayList<HistoryItem>> completeListener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Calendar calendar = Calendar.getInstance();
        final ArrayList<HistoryItem> historyItems = new ArrayList<>();
        for (xx = 0; xx < 20; xx++) {
            calendar.add(Calendar.DATE, -15 * xx);
            Date date = calendar.getTime();
            @SuppressLint("SimpleDateFormat")
            String url = context.getString(R.string.fixer_url) + "/" + new SimpleDateFormat("yyyy-MM-dd").format(date) + "?access_key=" + context.getString(R.string.fixer_api_key) + "&symbols=" + base.getCode() + "," + quote.getCode();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                JsonElement element = jsonObject.get("rates");
                JsonObject jsonObject1 = element.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entries = jsonObject1.entrySet();

                float bValue = -1;
                float qValue = -1;
                for (Map.Entry<String, JsonElement> entry : entries) {
                    if (entry.getKey().equalsIgnoreCase(base.getCode())) {
                        bValue = entry.getValue().getAsFloat();
                    } else if (entry.getKey().equalsIgnoreCase(quote.getCode())) {
                        qValue = entry.getValue().getAsFloat();
                    }
                }
                float rValue = qValue / bValue;
                HistoryItem historyItem = new HistoryItem();
                historyItem.setX(xx);
                historyItem.setY(rValue);
                historyItem.setDate(date);
                historyItems.add(historyItem);
                if (xx < 20) {
                    completeListener.complete(historyItems);
                }
            }, errorListener);
            requestQueue.add(stringRequest);
        }

    }

    public static class Currency {
        private String code;
        private String name;
        private Uri flag;
        private float value = 1f;

        public Currency(String code, String name, Uri flag) {
            this.code = code;
            this.name = name;
            this.flag = flag;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Uri getFlag() {
            return flag;
        }

        public void setFlag(Uri flag) {
            this.flag = flag;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        @NonNull
        @Override
        public String toString() {
            return code + ": " + name;
        }
    }

    public void getAllCurrencies(LoadCompleteListener<List<Currency>> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.fixer_url) + "/symbols?access_key=" + context.getString(R.string.fixer_api_key);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            JsonElement element = jsonObject.get("symbols");
            JsonObject jsonObject1 = element.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entries = jsonObject1.entrySet();
            List<Currency> clist = new ArrayList<>();
            for (Map.Entry<String, JsonElement> entry : entries) {
                String code = entry.getKey();
                String name = entry.getValue().getAsString().replace('\"', ' ');
                Uri uri = Uri.parse("https://www.countryflags.io/" + code.substring(0, 2) + "/shiny/64.png");//flat
                Currency currency = new Currency(code, name, uri);
                clist.add(currency);
            }
            listener.complete(clist);
        }, errorListener);
        requestQueue.add(stringRequest);
    }

    public interface LoadCompleteListener<E> {
        void complete(E e);
    }

    public interface ConvertCompleteListener {
        void complete(Currency base, Currency quote);
    }

    public static class HistoryItem {
        private float x;
        private float y;
        private Date date;

        public HistoryItem() {
        }

        public HistoryItem(float x, float y, Date date) {
            this.x = x;
            this.y = y;
            this.date = date;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }


    public Currency getBase() {
        return base;
    }

    public void setBase(Currency base) {
        this.base = base;
    }

    public Currency getQuote() {
        return quote;
    }

    public void setQuote(Currency quote) {
        this.quote = quote;
    }

    public Context getContext() {
        return context;
    }
}

