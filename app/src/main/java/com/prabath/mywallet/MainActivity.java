package com.prabath.mywallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import database.firebase.auth.AuthController;
import database.firebase.firestore.FirestoreController;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser currentUser;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Testing

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            View headerView = navigationView.getHeaderView(0);
            TextView naviEmail = headerView.findViewById(R.id.naviEmail);
            naviEmail.setText(currentUser.getEmail());
        }
        FirestoreController firestoreController = FirestoreController.newInstance();
        collectionAccounts = firestoreController.new CollectionAccounts();
        collectionRecords = firestoreController.new CollectionRecords();
        drawChart();

    }

    FirestoreController.CollectionAccounts collectionAccounts;
    FirestoreController.CollectionRecords collectionRecords;
    public void openNavigationDrawer(View view) {
        YoYo.with(Techniques.RubberBand).duration(200).onEnd(animator -> {
            if (!drawer.isDrawerOpen(GravityCompat.START)) drawer.openDrawer(GravityCompat.START);
            else drawer.closeDrawer(GravityCompat.END);
        }).playOn(view);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_accounts) {
            Intent intentAccount = new Intent(this, AccountsActivity.class);
            startActivity(intentAccount);
        } else if (id == R.id.nav_categories) {
            Intent intentCategories = new Intent(this, CategoriesActivity.class);
            startActivity(intentCategories);
        }
//        else if (id == R.id.nav_budget) {
//            Intent intentAccount = new Intent(this, BudgetsActivity.class);
//            startActivity(intentAccount);
//        } else if (id == R.id.nav_reports) {
//            Intent intentAccount = new Intent(this, ReportsActivity.class);
//            startActivity(intentAccount);
//        }
        else if (id == R.id.nav_about) {
            Intent intentAccount = new Intent(this, CurrencyConverterActivity.class);
            startActivity(intentAccount);
        }
//        else if (id == R.id.nav_settings) {
//            Intent intentAccount = new Intent(this, SettingsActivity.class);
//            startActivity(intentAccount);
//        }
        else if (id == R.id.nav_logout) {
            if (currentUser != null)
                AuthController.newInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
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
        collectionRecords.getAll(rs -> {
            for (int i = 0; i < rs.size(); i++) {
                list.add(new BarEntry(i * 10, rs.get(i).getValue()));
            }
            rDrawChart();
        });

    }


    ArrayList<BarEntry> list = new ArrayList<>();

    private void rDrawChart() {
        BarChart chart = findViewById(R.id.pieChart);


        BarDataSet dataSet = new BarDataSet(list, "Projects");
        BarData data = new BarData(dataSet);
        chart.setData(data);
        //Custermize chart
        chart.setDescription(null);
        data.setBarWidth(8);
        chart.setFitBars(true);
        chart.animateY(2000, Easing.EaseInCubic);
        chart.invalidate();
    }

//    public void openRegisterActivity(View view) {
//        Intent intent = new Intent(this, RegisterActivity.class);
//        startActivity(intent);
//    }
//
//    public void openTestActivity(View view) {
//        Intent intent = new Intent(this, TestActivity.class);
//        startActivity(intent);
//    }
//
//    //testing
//    //add all default categories
//    public void defaultCategories(View view) {
//        int[] icons_expense = {
//                R.drawable.db_exp_baby,
//                R.drawable.db_exp_beauty,
//                R.drawable.db_exp_bills,
//                R.drawable.db_exp_books,
//                R.drawable.db_exp_clothing,
//                R.drawable.db_exp_entertainment,
//                R.drawable.db_exp_foods,
//                R.drawable.db_exp_fruit,
//                R.drawable.db_exp_gift,
//                R.drawable.db_exp_home,
//                R.drawable.db_exp_insurance,
//                R.drawable.db_exp_medicine,
//                R.drawable.db_exp_pet,
//                R.drawable.db_exp_shopping,
//                R.drawable.db_exp_snaks,
//                R.drawable.db_exp_social,
//                R.drawable.db_exp_sports,
//                R.drawable.db_exp_student,
//                R.drawable.db_exp_tax,
//                R.drawable.db_exp_telephone,
//                R.drawable.db_exp_transportation,
//                R.drawable.db_exp_travel,
//                R.drawable.db_exp_vehicle,
//
//        };
//        String[] names_expense = {
//                "Baby",
//                "Beauty",
//                "Bills",
//                "Books",
//                "Clothing",
//                "Entertainment",
//                "Foods",
//                "Fruits",
//                "Gift",
//                "Home",
//                "Insurance",
//                "Medicine",
//                "Pets",
//                "Shopping",
//                "Snaks",
//                "Social",
//                "Sports",
//                "Students",
//                "Tax",
//                "Telephone",
//                "Transportation",
//                "Travel",
//                "Vehicles",
//        };
//        int[] icons_income = {
//                R.drawable.db_inc_awards,
//                R.drawable.db_inc_coupon,
//                R.drawable.db_inc_dividents,
//                R.drawable.db_inc_grants,
//                R.drawable.db_inc_investments,
//                R.drawable.db_inc_lottery,
//                R.drawable.db_inc_refund,
//                R.drawable.db_inc_salary,
//                R.drawable.db_inc_sale,
//        };
//        String[] names_income = {
//                "Awards",
//                "Coupons",
//                "Dividents",
//                "Grants",
//                "Investments",
//                "Lotteries",
//                "refunds",
//                "Salary",
//                "Sales",
//        };
//
//        CategoryIcons iconsGeter = CategoryIcons.newInstance();
//        Date date = new Date();
//
//        final ProgressBar progressBar = findViewById(R.id.progressBar);
//        float x = icons_income.length;
//        float y = icons_expense.length;
//        float xy = x + y;
//        float u = 100 / xy;
//        int ru = (int) u;
//
//        for (int i = 0; i < icons_income.length; i++) {
//            Category category = new Category();
//            category.setType(CategoryType.INCOME);
//            category.setIcon(iconsGeter.getId(icons_income[i]));
//            category.setName(names_income[i]);
//            category.setDateTime(date);
//            category.setDefaultx(true);
//            category.setUser(null);
//            final int progress = ru * i;
//            FirestoreController.newInstance(null).new CollectionCategories().add(category)
//                    .addOnSuccessListener(a -> progressBar.setProgress(progress))
//                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
//        }
//        for (int i = 0; i < icons_expense.length; i++) {
//            Category category = new Category();
//            category.setType(CategoryType.EXPENSE);
//            category.setIcon(iconsGeter.getId(icons_expense[i]));
//            category.setName(names_expense[i]);
//            category.setDateTime(date);
//            category.setDefaultx(true);
//            category.setDefaultx(true);
//            final int progress = (ru * i) + ru * icons_income.length;
//            FirestoreController.newInstance(null).new CollectionCategories().add(category)
//                    .addOnSuccessListener(a -> progressBar.setProgress(progress))
//                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
//        }
//
//
//    }
//
//    public void defaultAccounts(View view) {
//
//        int[] icons = {
//                R.drawable.db_acc_wallet,
//                R.drawable.db_acc_bank_building
//        };
//        String[] names = {
//                "Wallet",
//                "Bank",
//        };
//        String[] des = {
//                "Your Wallet",
//                "You Bank AccountActivity"
//        };
//
//        AccountIcons iconsGeter = AccountIcons.newInstance();
//        Date date = new Date();
//
//        for (int i = 0; i < icons.length; i++) {
//            Account account = new Account();
//            account.setName(names[i]);
//            account.setDes(des[i]);
//            account.setIcon(iconsGeter.getId(icons[i]));
//            account.setDateTime(date);
//            account.setDefaultx(true);
//            FirestoreController.newInstance(null).new CollectionAccounts().add(account)
//                    .addOnSuccessListener(a -> Toast.makeText(MainActivity.this, "add account", Toast.LENGTH_SHORT).show())
//                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
//
//        }
//    }

}
