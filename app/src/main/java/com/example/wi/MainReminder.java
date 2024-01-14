package com.example.wi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

//import com.example.wi.databinding.ActivityMainBinding;
import com.example.wi.databinding.MainReminderBinding;


import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import java.util.Calendar;

public class MainReminder extends AppCompatActivity {

    private MainReminderBinding binding;
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager reminderManager;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createNotificationChannel();

        binding.selectTimeBtn.setOnClickListener( new View.OnClickListener(){
        @Override
                public void onClick(View v){

            showTimePicker();



        }
        });

        binding.setReminderBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                setReminder();

            }

        });

        binding.cancelReminderBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                cancelReminder();

            }

        });




    }

    private void cancelReminder() {

        Intent intent = new Intent(this,ReminderReceiver.class);

       // pendingIntent = PendingIntent.getBroadcast(this, 0,intent, 0);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(reminderManager == null){

            reminderManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }

        reminderManager.cancel(pendingIntent);
        Toast.makeText(this,"REMINDER IS CANCELLED", Toast.LENGTH_SHORT).show();
    }

    private void setReminder() {

        reminderManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(this,ReminderReceiver.class);

           // pendingIntent = PendingIntent.getBroadcast(this, 0,intent, 0);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            reminderManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,pendingIntent);

        Toast.makeText(this,"REMINDER SET SUCCESSFULLY !", Toast.LENGTH_SHORT).show();


    }

    private void showTimePicker() {

        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Reminder Time")
                .build();


        picker.show(getSupportFragmentManager(),"foxandroid");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (picker.getHour() > 12){
                    binding.selectedTime.setText(
                            String.format("%02d",(picker.getHour()-12))+" : "+String.format("%02d",picker.getMinute())+" PM"
                    );

                }else {
                    binding.selectedTime.setText(picker.getHour()+" : " + picker.getMinute() + " AM");
                }

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
                calendar.set(Calendar.MINUTE,picker.getMinute());
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);


            }


            });
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "foxandroidReminderChannel";
            String description = "Channel For Reminder Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("foxandroid", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


    }
}