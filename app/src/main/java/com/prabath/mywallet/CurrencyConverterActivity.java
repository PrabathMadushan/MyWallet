package com.prabath.mywallet;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.Others.Commons;
import com.prabath.mywallet.Others.CurrencyController;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CurrencyConverterActivity extends AppCompatActivity {


    private CurrencyController currencyController;
    private List<CurrencyController.Currency> currencies;
    private CharSequence[] currencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);
        currencyController = new CurrencyController(this);
        currencyController.getAllCurrencies(list -> {
            currencies = list;
            currencyList = new CharSequence[list.size()];
            for (int i = 0; i < list.size(); i++) {
                currencyList[i] = list.get(i).getCode() + ":" + list.get(i).getName();
            }
        }, error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show());

        txtCurrencyCodeR = findViewById(R.id.txtCurrencyCodeR);
        txtFullCurrencyNameR = findViewById(R.id.txtFullCountryNameR);
        imgFlagR = findViewById(R.id.imgFlagR);
        txtValueR = findViewById(R.id.txtValueR);

        txtCurrencyCodeL = findViewById(R.id.txtCurrencyCodeL);
        txtFullCurrencyNameL = findViewById(R.id.txtFullCountryNameL);
        imgFlagL = findViewById(R.id.imgFlagL);
        txtValueL = findViewById(R.id.txtValueL);
        setData();
    }

    private void setData() {
        txtCurrencyCodeR.setText(currencyController.getBase().getCode());
        txtFullCurrencyNameR.setText(currencyController.getBase().getName());
        Picasso.get().load(currencyController.getBase().getFlag()).into(imgFlagR);
        txtValueR.setText(Commons.formatCurrency(currencyController.getBase().getValue()));

        txtCurrencyCodeL.setText(currencyController.getQuote().getCode());
        txtFullCurrencyNameL.setText(currencyController.getQuote().getName());
        Picasso.get().load(currencyController.getQuote().getFlag()).into(imgFlagL);
        txtValueL.setText(Commons.formatCurrency(currencyController.getQuote().getValue()));
        drawChart();
    }


    private TextView txtFullCurrencyNameR;
    private TextView txtCurrencyCodeR;
    private ImageView imgFlagR;
    private EditText txtValueR;

    private TextView txtFullCurrencyNameL;
    private TextView txtCurrencyCodeL;
    private ImageView imgFlagL;
    private TextView txtValueL;

    public void changeBaseCurrency(View view) {
        YoYo.with(Techniques.Flash).onEnd(a -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Select Base Currency")
                    .setSingleChoiceItems(currencyList, -1, (dialog, which) -> {
                        CurrencyController.Currency currency = currencies.get(which);
                        txtFullCurrencyNameR.setText(currency.getName());
                        txtCurrencyCodeR.setText(currency.getCode());
                        Picasso.get().load(currency.getFlag()).into(imgFlagR);
                        currencyController.setBase(currency);
                        setData();
                        dialog.dismiss();
                    }).create();
            alertDialog.show();
        }).playOn(view);
    }

    public void changeQuoteCurrency(View view) {
        YoYo.with(Techniques.Flash).onEnd(a -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Select Base Currency")
                    .setSingleChoiceItems(currencyList, -1, (dialog, which) -> {
                        CurrencyController.Currency currency = currencies.get(which);
                        txtFullCurrencyNameL.setText(currency.getName());
                        txtCurrencyCodeL.setText(currency.getCode());
                        Picasso.get().load(currency.getFlag()).into(imgFlagL);
                        currencyController.setQuote(currency);
                        setData();
                        dialog.dismiss();
                    }).create();
            alertDialog.show();
        }).playOn(view);
    }

    public void convert(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 360f * 5).setDuration(2000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
        float baseValue = Commons.formatedCurrencyToFloat(txtValueR.getText().toString());
        float quoteValue = Commons.formatedCurrencyToFloat(txtValueL.getText().toString());
        currencyController.getBase().setValue(baseValue);
        currencyController.getQuote().setValue(quoteValue);
        currencyController.convert((b, q) -> setData(), e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void swap(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 180f).setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
        YoYo.with(Techniques.FadeOutRight).duration(200).playOn(txtCurrencyCodeR);
        YoYo.with(Techniques.FadeOutRight).duration(200).playOn(txtValueR);
        YoYo.with(Techniques.FadeOutRight).duration(200).playOn(txtFullCurrencyNameR);
        YoYo.with(Techniques.FadeOutLeft).duration(200).playOn(txtCurrencyCodeL);
        YoYo.with(Techniques.FadeOutLeft).duration(200).playOn(txtValueL);
        YoYo.with(Techniques.FadeOutLeft).duration(200).playOn(txtFullCurrencyNameL);
        YoYo.with(Techniques.FadeOutLeft).duration(200).playOn(imgFlagL);
        YoYo.with(Techniques.FadeOutRight).duration(200).onEnd(a -> {
            currencyController.swap();
            setData();
            YoYo.with(Techniques.FadeInRight).duration(200).playOn(txtCurrencyCodeL);
            YoYo.with(Techniques.FadeInRight).duration(200).playOn(txtValueL);
            YoYo.with(Techniques.FadeInRight).duration(200).playOn(txtFullCurrencyNameL);
            YoYo.with(Techniques.FadeInRight).duration(200).playOn(imgFlagL);

            YoYo.with(Techniques.FadeInLeft).duration(200).playOn(txtCurrencyCodeR);
            YoYo.with(Techniques.FadeInLeft).duration(200).playOn(txtValueR);
            YoYo.with(Techniques.FadeInLeft).duration(200).playOn(txtFullCurrencyNameR);
            YoYo.with(Techniques.FadeInLeft).duration(200).playOn(imgFlagR);
        }).playOn(imgFlagR);

    }

    public void drawChart() {

//        LineChart chart = findViewById(R.id.lineChart);
//        ArrayList<Entry> list = new ArrayList<>();
//
//        LineDataSet dataSet = new LineDataSet(list, "this year");
//        dataSet.setDrawCircles(false);
//        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        dataSet.setFillAlpha(100);
//        dataSet.setDrawValues(false);
//        dataSet.setDrawFilled(true);
//        dataSet.setFillColor(R.color.primaryBlueDark);
//        LineData data = new LineData(dataSet);
//        chart.setData(data);
//        chart.setDescription(null);
//        chart.setClickable(false);
//        chart.getLegend().setWordWrapEnabled(true);
//        chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        chart.animateY(2000, Easing.EaseInCubic);
//        chart.setAutoScaleMinMaxEnabled(true);
//        chart.getAxisRight().setAxisMinimum(0);
//        chart.getAxisLeft().setAxisMinimum(0);
//        chart.getAxisRight().setAxisMaximum(400);
//        chart.getAxisLeft().setAxisMaximum(400);
//        chart.invalidate();
    }


}
