package com.parkinglotmanagement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class PaymentActivity extends AppCompatActivity {

    String months[]={"01","02","03","04","05","06","07","08","09","10","11","12"};
    String years[]=new String[33];
    Spinner monthSpinner,yearSpinner;
    EditText card1,card2,card3,card4,cvvEt;
    Button PayBt;
    double totalFees;
    String slot;
    Calendar c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent intent=getIntent();
        totalFees=intent.getDoubleExtra("Fees",0);
        slot=intent.getStringExtra("Slot");
        int j=0;
        for(int i=2018;i<=2050;i++)
        {
            years[j]= String.valueOf(i);
            j++;
        }
        monthSpinner=(Spinner)findViewById(R.id.monthSpinner);
        yearSpinner=(Spinner)findViewById(R.id.yearSpinner);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,months);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        monthSpinner.setAdapter(aa);
        ArrayAdapter aa1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,years);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        yearSpinner.setAdapter(aa1);
        card1=(EditText)findViewById(R.id.card1);
        card2=(EditText)findViewById(R.id.card2);
        card3=(EditText)findViewById(R.id.card3);
        card4=(EditText)findViewById(R.id.card4);
        cvvEt=(EditText)findViewById(R.id.cvvEt);
        PayBt=(Button)findViewById(R.id.payBt);
        PayBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(yearSpinner.getSelectedItem().toString().isEmpty()||monthSpinner.getSelectedItem().toString().isEmpty())
                {
                    Snackbar.make(v,"Select Valid Expiry",Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    if(card1.getText().toString().isEmpty()||card2.getText().toString().isEmpty()||card3.getText().toString().isEmpty()||card4.getText().toString().isEmpty())
                    {
                        Snackbar.make(v,"Invalid Card Number",Snackbar.LENGTH_SHORT).show();
                    }else if(cvvEt.getText().toString().isEmpty())
                    {
                        Snackbar.make(v,"Invalid CVV",Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        c=Calendar.getInstance();
                        c.set(Calendar.YEAR,Integer.parseInt(yearSpinner.getSelectedItem().toString()));
                        c.set(Calendar.MONTH,Integer.parseInt(monthSpinner.getSelectedItem().toString())-1);
                        c.set(Calendar.DAY_OF_MONTH,1);
                        Log.d("SPINNER",Integer.parseInt(yearSpinner.getSelectedItem().toString())+"--"+Integer.parseInt(monthSpinner.getSelectedItem().toString()));
                        Calendar today=Calendar.getInstance();
                        Log.d("TIME",c.getTime().toString()+"-----"+today.getTime().toString());
                        if(today.compareTo(c)>0)
                        {
                            Snackbar.make(v,"Check Card Expiry",Snackbar.LENGTH_SHORT).show();
                        }
                        else {
                            final ProgressDialog pd = ProgressDialog.show(PaymentActivity.this, "Please wait", " Confirming Payment...");

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference payments = databaseReference.child("Payments");
                            pd.show();
                            payments.child(slot).setValue(new CardDetails(card1.getText().toString() + "-" +
                                    card2.getText().toString() + "-" + card3.getText().toString() + "-" + card4.getText().toString(),
                                    yearSpinner.getSelectedItem().toString()+"-"+monthSpinner.getSelectedItem().toString()), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        pd.dismiss();
                                        Intent intent = new Intent();
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                    } else {
                                        Snackbar.make(getCurrentFocus(), "Try Again", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                    }

                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
