package com.silvia.rema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.widget.TintableImageSourceView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;
import android.widget.Toolbar;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.CalendarDayEvent;
import com.google.gson.Gson;
import com.silvia.rema.Adapter.AdapterEvent;
import com.silvia.rema.Adapter.AdapterLihatBerita;
import com.silvia.rema.Model.ModelBerita;
import com.silvia.rema.Model.ModelEvent;
import com.silvia.rema.databinding.ActivityEventBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.silvia.rema.R.style.Theme_MaterialComponents_Light_Dialog_MinWidth;


public class EventActivity extends AppCompatActivity {
    private ActivityEventBinding binding;

    CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    String tanggal;
    AdapterEvent adapter;

    API api;

    private List<ModelEvent> dataEvent;
    TinyDB tinyDB;
    private int bulan, tahun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        api = new API();
        AndroidNetworking.initialize(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        tinyDB = new TinyDB(this);

        setSupportActionBar(binding.toolbar);


        binding.imgtoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }


        });

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        tahun = calendar.get(Calendar.YEAR);
        bulan = calendar.get(Calendar.MONTH);



        binding.linearListEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(bulan, tahun);

                dialogFragment.show(getSupportFragmentManager(), null);
                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        tahun = year;
                        bulan = monthOfYear;
                        binding.eventbulanini.setVisibility(View.GONE);
                        getEventMonth();
                    }
                });
            }
        });



        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.drawSmallIndicatorForEvents(true);
        compactCalendarView.setUseThreeLetterAbbreviation(true);



        binding.bulan.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                String day          = (String) DateFormat.format("dd",   dateClicked); // 20
                String monthNumber  = (String) DateFormat.format("MM",   dateClicked); // 06
                String mont  = (String) DateFormat.format("MMM",   dateClicked); // 06
                String year         = (String) DateFormat.format("yyyy", dateClicked);
                 tanggal = year+"-"+monthNumber+"-"+day;
                 String tanggalklik = day+""+mont+""+year;
                 binding.tglklik.setText(tanggalklik);
                 binding.eventbulanini.setVisibility(View.GONE);
                getEventTanggal();

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                binding.bulan.setText(dateFormatForMonth.format(firstDayOfNewMonth));

            }

        });

        binding.recEvent.setHasFixedSize(true);
        dataEvent = new ArrayList<>();
        adapter = new AdapterEvent(dataEvent);
        binding.recEvent.setAdapter(adapter);


        getEvent();
        getEventMonthYear();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void addEventsDua(CompactCalendarView compactCalendarView, int day, int month, int year) {
        day = day - 1;
        month = month - 1;


        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = currentCalender.getTime();

            currentCalender.setTime(firstDayOfMonth);
            if (month > -1) {
                currentCalender.set(Calendar.YEAR, year);
                currentCalender.set(Calendar.MONTH, month);
            }
            currentCalender.add(Calendar.DATE, day);
            setToMidnight(currentCalender);
            compactCalendarView.addEvent(new CalendarDayEvent(currentCalender.getTimeInMillis(), Color.argb(255, 255, 255, 255)), false);
    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private void getEvent() {
        AndroidNetworking.get(api.URL_EVENT_ALL)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("sukses")) {
                                JSONArray res = response.getJSONArray("res");

                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject data = res.getJSONObject(i);
                                    String day = data.getString("day");
                                    int days = Integer.parseInt(day);
                                    String month= data.getString("month");
                                    int months = Integer.parseInt(month);
                                    String year = data.getString("year");
                                    int years = Integer.parseInt(year);

                                    addEventsDua(compactCalendarView, days,months,years);
                                }
                                compactCalendarView.invalidate();

                            }else {
                                Toast.makeText(EventActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("tampil menu","response:"+anError);
                    }
                });

    }

    private void getEventTanggal() {
        AndroidNetworking.get(api.URL_EVENT_TANGGAL+tanggal)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("sukses")) {
                                JSONArray res = response.getJSONArray("res");
                                Gson gson = new Gson();
                                dataEvent.clear();
                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject data = res.getJSONObject(i);
                                    ModelEvent Isi = gson.fromJson(data + "", ModelEvent.class);
                                    dataEvent.add(Isi);
                                }

                            }else {
                                dataEvent.clear();

                                Toast.makeText(EventActivity.this, "Tidak Ada Event", Toast.LENGTH_SHORT).show();
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("tampil menu","response:"+anError);
                    }
                });

    }

    private void getEventMonth() {
        AndroidNetworking.post(api.URL_SELECT_EVENT_MONTH)
                .addBodyParameter("bulan",""+(bulan+1))
                .addBodyParameter("tahun",""+tahun)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("sukses")) {
                                JSONArray res = response.getJSONArray("res");
                                Gson gson = new Gson();
                                dataEvent.clear();
                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject data = res.getJSONObject(i);
                                    ModelEvent Isi = gson.fromJson(data + "", ModelEvent.class);
                                    dataEvent.add(Isi);
                                }

                            }else {
                                dataEvent.clear();

                                Toast.makeText(EventActivity.this, "Tidak Ada Event", Toast.LENGTH_SHORT).show();
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("tampil menu","response:"+anError);
                    }
                });

    }

    private void getEventMonthYear() {
        AndroidNetworking.post(api.URL_SELECT_EVENT_MONTHYEAR)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("sukses")) {
                                JSONArray res = response.getJSONArray("res");
                                Gson gson = new Gson();
                                dataEvent.clear();
                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject data = res.getJSONObject(i);
                                    ModelEvent Isi = gson.fromJson(data + "", ModelEvent.class);
                                    dataEvent.add(Isi);
                                }

                            }else {
                                dataEvent.clear();

                                Toast.makeText(EventActivity.this, "Tidak Ada Event", Toast.LENGTH_SHORT).show();
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("tampil menu","response:"+anError);
                    }
                });

    }



}