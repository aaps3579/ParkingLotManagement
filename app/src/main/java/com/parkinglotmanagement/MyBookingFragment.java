package com.parkinglotmanagement;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyBookingFragment extends Fragment implements View.OnClickListener {

    TextView tvNameMB,tvEmailMB;
    RecyclerView recyclerView;
    BookingAdapter mAdapter;
    List<MyBookingBean> list=new ArrayList<>();
    ProgressBar progressBar;
    String Email;
    SharedPreferences preferences;
    Button refreshBt;
    public MyBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_my_booking, container, false);
        recyclerView = (RecyclerView) myview.findViewById(R.id.rv);
        tvNameMB=(TextView)myview.findViewById(R.id.tvNameMB);
        tvEmailMB=(TextView)myview.findViewById(R.id.tvEmailMB);
        progressBar=(ProgressBar)myview.findViewById(R.id.pBar);
        refreshBt=(Button)myview.findViewById(R.id.refreshBt);
        refreshBt.setOnClickListener(this);
        mAdapter = new BookingAdapter(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        preferences=getActivity().getSharedPreferences("MyRef",MODE_PRIVATE);
        Email=preferences.getString("Email","NA");
        tvEmailMB.setText("Email - "+preferences.getString("Email","NA"));
        tvNameMB.setText("Name - "+preferences.getString("Name","NA"));

        prepareData();
        return myview;
    }
    void prepareData()
    {
        Email=preferences.getString("Email","NA");
        tvEmailMB.setText("Email - "+preferences.getString("Email","NA"));
        tvNameMB.setText("Name - "+preferences.getString("Name","NA"));

        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        mAdapter.notifyDataSetChanged();
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("Booked");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren())
                {
                    Log.d("Tag",d.getKey());
                    //tag is giving slot
                    String slot=d.getKey();
                    BookedProperties val = d.getValue(BookedProperties.class);
                    if(val.getEmail().equals(Email)) {
                        MyBookingBean myBookingBean = new MyBookingBean(slot, val.getPlate(), val.getFrom(), val.getTo(), val.getFees());
                        list.add(myBookingBean);
                    }

                }
                mAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(getActivity().getCurrentFocus(),"Try Later",Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.refreshBt)
        {
            prepareData();
        }
    }
}
