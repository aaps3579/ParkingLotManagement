package com.parkinglotmanagement;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SlotCheckFragment extends Fragment implements View.OnClickListener {

    EditText etSlotNo,etLicensePlateNo;
    Button btCheckSlot;
    DatabaseReference dbref= FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbrefChild;
    public SlotCheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.fragment_slot_check, container, false);
        etSlotNo=(EditText)myview.findViewById(R.id.etSlotNo);
        etLicensePlateNo=(EditText)myview.findViewById(R.id.etPlateNo);
        btCheckSlot=(Button)myview.findViewById(R.id.btCheckSlotAvail);
        btCheckSlot.setOnClickListener(this);
        return myview;
    }

    @Override
    public void onClick(final View v) {
        if(v.getId()==R.id.btCheckSlotAvail)
        {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            final String slotNo=etSlotNo.getText().toString().trim(),plateNo=etLicensePlateNo.getText().toString().trim();
            if (slotNo.isEmpty()||plateNo.isEmpty())
            {
                Snackbar.make(v,"Please Fill Details",Snackbar.LENGTH_SHORT).show();
            }
            else if(Integer.parseInt(slotNo)<1 ||Integer.parseInt(slotNo)>20)
            {
                Snackbar.make(v,"Inavlid Slot No",Snackbar.LENGTH_SHORT).show();
            }
            else
            {
                dbrefChild=dbref.child("Booked");
                final ProgressDialog progressDialog=new ProgressDialog(getContext());
                progressDialog.setTitle("Loading");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Please Wait !!");
                progressDialog.show();
                dbrefChild.child(slotNo).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {
                            progressDialog.dismiss();
                            Snackbar.make(v,"Slot Already Filled",Snackbar.LENGTH_SHORT).show();
                            Log.d("TAG","Free");
                        }
                        else
                        {
                            progressDialog.dismiss();
                            //open timing activity
                            Intent intent=new Intent(getContext(),ConfirmSlotActivity.class);
                            intent.putExtra("Slot",slotNo);
                            intent.putExtra("Plate",plateNo);
                            startActivity(intent);
                            Log.d("TAG","Free");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}
