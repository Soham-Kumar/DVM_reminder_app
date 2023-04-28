package com.example.reminder_app;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
public class MainActivity extends AppCompatActivity {

    private mySQLiteDBHandler dbHandler;
    private SQLiteDatabase sqLiteDatabase;
    private EditText reminder_name;
    private CalendarView calenderView;
    private String selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reminder_name = findViewById(R.id.reminder_name);
        calenderView = findViewById(R.id.calendarView);

        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
            selectedDate = Integer.toString(year) + Integer.toString(month) + Integer.toString(dayOfMonth);
            ReadDatabase(view);
            }
        });

        try{
            dbHandler = new mySQLiteDBHandler(this, "CalenderDatabase", null, 1);
            sqLiteDatabase = dbHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE EventCalender(Date TEXT, Event TEXT)");


        } catch(Exception e){
            e.printStackTrace();
        }

    }
    public void InsertIntoDatabase (View view){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", selectedDate);
        contentValues.put("Event", reminder_name.getText().toString());
        sqLiteDatabase.insert("EventCalender", null, contentValues);

    }

    public void ReadDatabase(View view){
        String query = "Select Event from EventCalender where Date = " + selectedDate;
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToFirst();
            reminder_name.setText(cursor.getString(0));
        }catch(Exception e){
            e.printStackTrace();
            reminder_name.setText("");
        }
    }
}