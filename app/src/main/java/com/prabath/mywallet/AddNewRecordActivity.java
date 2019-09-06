package com.prabath.mywallet;

import android.animation.Animator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.Others.CategoryIcons;
import com.prabath.mywallet.fregments.AddLocationFragment;
import com.prabath.mywallet.fregments.AddPhotoFragment;
import com.prabath.mywallet.fregments.AddRouteFragment;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import database.local.LocalDatabaseController;
import database.local.LocalDatabaseHelper;
import database.local.models.Category;
import database.local.models.CategoryType;
import database.local.models.GLocation;
import database.local.models.Record;

public class AddNewRecordActivity extends AppCompatActivity {

    private AddLocationFragment locationFragment;
    private AddPhotoFragment photoFragment;
    private AddRouteFragment routeFragment;

    private Record record;

    public AddNewRecordActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        initNumberPadComponents();
        init();
        enterTransition();
    }


    private void enterTransition() {
        Record record = (Record) getIntent().getSerializableExtra(AccountActivity.EXTRA_RECORD);
        Category c = record.getCategory();
        ImageView cicon = findViewById(R.id.categoryicon);
        TextView cname = findViewById(R.id.categoryname);
        cname.setText(c.getName());
        cicon.setImageResource(CategoryIcons.getInstance().getIcon(Integer.parseInt(c.getIcon())));
        if (c.getType() == CategoryType.INCOME) {
            cicon.setBackgroundResource(R.drawable.style_button_circle_blue);
        } else {
            cicon.setBackgroundResource(R.drawable.style_button_circle_rose);
        }
        cicon.setTransitionName(c.getName());

    }


    private void init() {
        record = (Record) getIntent().getSerializableExtra(AccountActivity.EXTRA_RECORD);
        ImageView btnRoute = findViewById(R.id.btnRoute);
        ImageView btnLocation = findViewById(R.id.btnLocation);
        FragmentManager manager = getSupportFragmentManager();
        if (record.getLocation().getType() == GLocation.Type.LOCATION) {//
            locationFragment = AddLocationFragment.newInstance(record);//
            btnLocation.setVisibility(View.VISIBLE);
            btnRoute.setVisibility(View.GONE);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.rlpLocationFragment, locationFragment);
            transaction.commit();
        } else {//
            routeFragment = AddRouteFragment.newInstance(record);//
            btnLocation.setVisibility(View.GONE);
            btnRoute.setVisibility(View.VISIBLE);
            FragmentTransaction transaction3 = manager.beginTransaction();
            transaction3.replace(R.id.rlpRoutFragment, routeFragment);
            transaction3.commit();
        }
        photoFragment = AddPhotoFragment.newInstance(record);//
        FragmentTransaction transaction2 = manager.beginTransaction();
        transaction2.replace(R.id.rlpPhotoFragment, photoFragment);
        transaction2.commit();
        setDataToForm();
        setInitFloatCount();
        ImageView btnAddRecord = findViewById(R.id.btnSave);
        btnAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecord();
            }
        });
        TextView btnDate = findViewById(R.id.btnDate);
        TextView btnTime = findViewById(R.id.btnTime);
        java.util.Date date = new java.util.Date();
        btnDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        btnTime.setText(new SimpleDateFormat("hh:mm:ss").format(date));
    }


    public void showPhotoFragment(View view) {
        final ConstraintLayout otherFragment;
        if (record.getLocation().getType() == GLocation.Type.LOCATION) {
            otherFragment = findViewById(R.id.rlpLocationFragment);
        } else {
            otherFragment = findViewById(R.id.rlpRoutFragment);
        }

        final ConstraintLayout photoFragment = findViewById(R.id.rlpPhotoFragment);
        YoYo.with(Techniques.RubberBand).duration(200).playOn(view);
        YoYo.with(Techniques.FadeOutRight).duration(200).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                otherFragment.setVisibility(View.GONE);
                photoFragment.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.ZoomIn).duration(200).playOn(photoFragment);
            }
        }).playOn(otherFragment);


    }

    public void showLocationFragment(View view) {
        final ConstraintLayout locationFragment = findViewById(R.id.rlpLocationFragment);
        final ConstraintLayout photoFragment = findViewById(R.id.rlpPhotoFragment);
        YoYo.with(Techniques.RubberBand).duration(200).playOn(view);
        YoYo.with(Techniques.FadeOutRight).duration(200).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                locationFragment.setVisibility(View.VISIBLE);
                photoFragment.setVisibility(View.GONE);
                YoYo.with(Techniques.ZoomIn).duration(200).playOn(locationFragment);
            }
        }).playOn(photoFragment);
    }

    public void showRouteFragment(View view) {
        final ConstraintLayout routeFragment = findViewById(R.id.rlpRoutFragment);
        final ConstraintLayout photoFragment = findViewById(R.id.rlpPhotoFragment);
        YoYo.with(Techniques.RubberBand).duration(200).playOn(view);
        YoYo.with(Techniques.FadeOutRight).duration(200).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                routeFragment.setVisibility(View.VISIBLE);
                photoFragment.setVisibility(View.GONE);
                YoYo.with(Techniques.ZoomIn).duration(200).playOn(routeFragment);
            }
        }).playOn(photoFragment);
    }


    public void toSelectCategory(View v) {
        selectCategory();
    }

    private void selectCategory() {
        setDataToRecord();
        Intent intent = new Intent();
        intent.putExtra(AccountActivity.EXTRA_RECORD, record);
        setResult(Activity.RESULT_OK, intent);
        ActivityCompat.finishAfterTransition(this);
    }


    private void setDataToForm() {
        EditText txtDescription = findViewById(R.id.txtDescription);
        txtDescription.setText(record.getDescription());
    }

    private void setDataToRecord() {
        TextView txtValue = findViewById(R.id.txtValue);
        EditText txtDescription = findViewById(R.id.txtDescription);
        record.setValue(Float.parseFloat(txtValue.getText().toString()));
        record.setDescription(txtDescription.getText().toString());
        if (record.getLocation().getType() == GLocation.Type.LOCATION) {
            record.setLocation(locationFragment.getSelectedLocation());
        } else {
            record.setLocation(routeFragment.getSelectedRoute());
        }

    }


    @Override
    public void onBackPressed() {
        selectCategory();
    }


    public void setDate(View v) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                TextView txtDate = findViewById(R.id.btnDate);
                record.setDate(new Date(year - 1900, month - 1, dayOfMonth));
                txtDate.setText(record.getDate().toString());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setTime(View view) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TextView txtDate = findViewById(R.id.btnTime);
                java.util.Date date = new java.util.Date();
                date.setHours(hourOfDay);
                date.setMinutes(minute);
                record.setTime(new Time(date.getTime()));
                txtDate.setText(record.getTime().toString());
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this)).show();
    }

    public void addRecord() {
        /*
    private String id;
    private float value;
    private Category category;
    private Account account;
    private GLocation location;
    private String image;
    private String description;
    private Date date;
    private Time time;
        *
        * */
        if (record.getDate() == null) {
            record.setDate(new Date(new java.util.Date().getTime()));
        }
        if (record.getTime() == null) {
            record.setTime(new Time(new java.util.Date().getTime()));
        }
        record.setId(LocalDatabaseController.genareteRandomKey());
        if(record.getLocation().getType()== GLocation.Type.LOCATION){
            record.setLocation(locationFragment.getSelectedLocation());
        }else{
            record.setLocation(routeFragment.getSelectedRoute());
        }
        record.setImage(photoFragment.seveImage());
        record.setValue(Float.parseFloat(getValue()));
        EditText txtDes = findViewById(R.id.txtDescription);
        record.setDescription(txtDes.getText().toString());

        LocalDatabaseController.getInstance(LocalDatabaseHelper.getInstance(this)).new TableRecord().add(record);
        Toast.makeText(this, "record added", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,AccountActivity.class);
        intent.putExtra(AccountsActivity.EXTRA_ACCOUNT,record.getAccount());
        startActivity(intent);
    }


    //TODO: Number pad implementation

    private void setInitFloatCount() {

        float value = record.getValue();
        if ((value - ((int) value)) == 0) {
            setValue(((int) value) + "");
        } else {
            setValue(value + "");
            String vasstring = (value == 0f) ? "0" : value + "";
            String[] split = vasstring.split("\\.");
            Toast.makeText(this, vasstring + "" + Arrays.toString(split), Toast.LENGTH_SHORT).show();
            if (split.length >= 2) {
                String f = split[1];
                floatCount = f.length();
                isFloatEnabled = true;
            }
        }


    }

    private void initNumberPadComponents() {
        txtValue = findViewById(R.id.txtValue);

    }

    private TextView txtValue;
    private int floatCount = 0;
    private boolean isFloatEnabled = false;

    public void numberPress(View v) {
        if (getValue().length() > 12) {
            txtValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
        } else {
            txtValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        }
        if (!(v instanceof ImageButton)) {
            Button b = (Button) v;
            String number = b.getText().toString();
            if (!number.equals("C") && !number.equals(".")) {
                if (floatCount < 2) {
                    if (0f == Float.parseFloat(getValue())) {
                        setValue(number);
                    } else {
                        setValue(getValue() + number);
                    }
                    if (isFloatEnabled) floatCount++;
                }
            } else {
                if (number.equals("C")) {
                    setValue("0");
                    txtValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    floatCount = 0;
                    isFloatEnabled = false;
                } else {
                    if (!isFloatEnabled) {
                        setValue(getValue() + ".");
                        isFloatEnabled = true;
                    }
                }
            }
        } else {
            if (getValue().length() == 1) {
                setValue("0");
            } else {
                if (getValue().charAt(getValue().length() - 1) == '.') isFloatEnabled = false;
                String s = getValue().substring(0, getValue().length() - 1);
                setValue(s);
                if (isFloatEnabled) floatCount--;
            }
        }
    }

    private void setValue(String value) {
        txtValue.setText(value);
    }

    private String getValue() {
        return txtValue.getText().toString();
    }
    //TODO: End Number pad implementation


}
