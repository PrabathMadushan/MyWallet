package com.prabath.mywallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Testing
        drawChart();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_accounts) {
            Intent intentAccount = new Intent(this, AccountsActivity.class);
            startActivity(intentAccount);
        } else if (id == R.id.nav_categories) {
            Intent intentCategories = new Intent(this, CategoriesActivity.class);
            startActivity(intentCategories);

        } else if (id == R.id.nav_budget) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void finish() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    //Testing

    public void drawChart() {
        BarChart chart = findViewById(R.id.pieChart);
        ArrayList<BarEntry> list = new ArrayList<>();
        list.add(new BarEntry(0, 20));
        list.add(new BarEntry(10, 90));
        list.add(new BarEntry(20, 15));
        list.add(new BarEntry(30, 85));
        list.add(new BarEntry(40, 18));
        list.add(new BarEntry(50, 40));
        list.add(new BarEntry(60, 50));

        BarDataSet dataSet = new BarDataSet(list, "Projects");
        BarData data = new BarData(dataSet);
        chart.setData(data);

        //Custermize chart
        chart.setDescription(null);
        data.setBarWidth(8);
        chart.setFitBars(true);
        chart.setClickable(false);

        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        String names[] = {"Transport", "Books", "Tax", "Shopping", "Telephone", "Vehicles", "Students"};
        int cs[] = {
                R.color.primaryBlueDark
                , R.color.primaryRoseDark
                , R.color.primaryBlueLight
                , R.color.primaryRoseLight
                , R.color.colorPrimary
                , R.color.colorAccent
                , R.color.darkGray};
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            colors.add(getResources().getColor(cs[i]));
            legendEntries.add(new LegendEntry(names[i], Legend.LegendForm.SQUARE, Float.NaN, Float.NaN, null,
                    getResources().getColor(cs[i])));
        }
        dataSet.setColors(colors);
        chart.getLegend().setCustom(legendEntries);
        chart.getLegend().setWordWrapEnabled(true);
        chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        chart.animateY(2000, Easing.EaseInCubic);

        chart.invalidate();
    }

    public void openRegisterActivity(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void openTestActivity(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }


}
