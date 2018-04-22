package com.parkinglotmanagement;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ConfirmSlotActivity extends AppCompatActivity implements View.OnClickListener {

    RadioGroup radioGroup;
    TextView tvSlotCF,tvPlateCF,tvEmailCF,tvFeesCF,tvTimeCF;
    Button btPayCF,btCancelCF,btTimeCF;
    double feesPerMin=0.2,totalFees=0;
    String Email,Slot,Plate,Name;
    DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbRefChild;
    boolean flag=false;
    Calendar c,c1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_slot);
        SharedPreferences preferences=getSharedPreferences("MyRef",MODE_PRIVATE);
        if(preferences.contains("Email"))
        {
            Email=preferences.getString("Email","NA");
            Name=preferences.getString("Name","NA");
        }
        Intent intent=getIntent();
        Slot = intent.getStringExtra("Slot");
        Plate=intent.getStringExtra("Plate");
        radioGroup=(RadioGroup) findViewById(R.id.radioGroup);
        tvSlotCF=(TextView)findViewById(R.id.tvSlotNoCF);
        tvPlateCF=(TextView)findViewById(R.id.tvPlateCF);
        tvEmailCF=(TextView)findViewById(R.id.tvEmailCF);
        tvFeesCF=(TextView)findViewById(R.id.tvFeesCF);
        tvTimeCF=(TextView)findViewById(R.id.tvTimeCF);
        btPayCF=(Button)findViewById(R.id.btPayCF);
        btCancelCF=(Button)findViewById(R.id.btCancelCF);
        btTimeCF=(Button)findViewById(R.id.btTime);
        btTimeCF.setOnClickListener(this);
        btPayCF.setOnClickListener(this);
        btCancelCF.setOnClickListener(this);
        tvEmailCF.setText("Email - "+Email);
        tvPlateCF.setText("License Plate No - "+Plate);
        tvSlotCF.setText("Parking Slot No - "+Slot);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tvFeesCF.setText("");
                switch (checkedId)
                {

                    case R.id.testRb:
                    {
                        flag=true;
                        tvFeesCF.setText("Parking Fees - $"+feesPerMin*2);
                        totalFees=feesPerMin*2;
                        break;
                    }
                    case R.id.h1Rb:
                    {
                        flag=true;
                        tvFeesCF.append("Parking Fees - $"+feesPerMin*60);
                        totalFees=feesPerMin*60;
                        break;
                    }
                    case R.id.h2Rb:
                    {
                        flag=true;
                        tvFeesCF.append("Parking Fees - $"+feesPerMin*120);
                        totalFees=feesPerMin*120;
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId())
        {
            case R.id.btPayCF:
            {
               if (flag==true)
               {
                   Calendar calendar=Calendar.getInstance();
                   c1= (Calendar) c.clone();
                   Log.d("Out Loop",c1.getTime().toString());
                   if(calendar.before(c))
                   {
                       if(totalFees==0.4)
                       {
                            c.add(Calendar.MINUTE,2);
                            Log.d("In Loop",c.getTime().toString());
                       }else if(totalFees==0.2*60)
                       {
                           c.add(Calendar.HOUR_OF_DAY,1);
                       }else
                       {
                           c.add(Calendar.HOUR_OF_DAY,2);
                       }

                       Intent intent=new Intent(ConfirmSlotActivity.this,PaymentActivity.class);
                       intent.putExtra("Slot",Slot);
                       intent.putExtra("Fees",totalFees);
                       startActivityForResult(intent,1);

                   }
                   else
                   {
                       Snackbar.make(v,"Please Check Start Time",Snackbar.LENGTH_SHORT).show();
                   }
               }
                break;
            }
            case R.id.btCancelCF:
            {
                finish();
                break;
            }
            case R.id.btTime:
            {
                c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                c.set(Calendar.MINUTE,minute);
                                tvTimeCF.setText("");
                                tvTimeCF.setText(c.getTime().toString().substring(0,20));
                                flag=true;
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            }
        }
    }
    void generateNotificationAndService(Calendar c,Calendar c1)
    {
        Log.d("TAG",c.getTime().toString()+"----"+c1.getTime().toString());
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("Slot",Slot);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP,c1.getTimeInMillis(),pintent);

        Intent intent1 = new Intent(this, NotificationService.class);
        intent1.putExtra("Slot",Slot);
        long milisec=c1.getTimeInMillis()-c.getTimeInMillis();
        intent1.putExtra("Time",milisec);
        PendingIntent pintent1 = PendingIntent.getService(this, 1, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm1 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm1.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pintent1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {
                final ProgressDialog pd=ProgressDialog.show(ConfirmSlotActivity.this,"Please wait"," Loading...");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                alertDialogBuilder.setTitle("Slot Booked Successfully");
                alertDialogBuilder.setMessage("Track Your Booking's Under My Booking Options");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNeutralButton("GOT IT!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

                final AlertDialog alertDialog = alertDialogBuilder.create();
                BookedProperties bookedProperties=new BookedProperties(Name,Email,Plate,totalFees,c1.getTime().toString().trim(),c.getTime().toString().trim());
                dbRefChild=dbRef.child("Booked");
                dbRefChild.child(Slot+"").setValue(bookedProperties, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError!=null)
                        {
                           pd.dismiss();
                            alertDialog.dismiss();

                            Snackbar.make(getCurrentFocus(),"Try Later",Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                            pd.dismiss();
                            alertDialog.show();
                            SendMail sendMail=new SendMail(ConfirmSlotActivity.this,Email,"Parking Management System","Your Slot No "+Slot+
                                    " for License Plate No. "+Plate+" is valid from "+c1.getTime().toString().substring(0,20)+ " uptill "+c.getTime().toString().substring(0,20));
                            sendMail.execute();
                        }
                    }
                });
                generateNotificationAndService(c1,c);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                alertDialogBuilder.setTitle("Slot Booking Failed");
                alertDialogBuilder.setMessage("Payment Was Not Successfull");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNeutralButton("Try Again!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        }
    }
}
