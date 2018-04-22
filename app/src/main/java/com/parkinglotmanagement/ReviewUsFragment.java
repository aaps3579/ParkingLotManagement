package com.parkinglotmanagement;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewUsFragment extends Fragment implements View.OnClickListener{


    RatingBar ratingBar;
    EditText reviewEt;
    Button submitReview;
    public ReviewUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_review_us, container, false);
        ratingBar=(RatingBar)myview.findViewById(R.id.ratingBar);
        reviewEt=(EditText)myview.findViewById(R.id.reviewEt);
        submitReview=(Button)myview.findViewById(R.id.submitReview);
        submitReview.setOnClickListener(this);
        return myview;
    }

    @Override
    public void onClick(final View v) {
        if(v.getId()==R.id.submitReview)
        {
            SharedPreferences sharedPreferences=getActivity().getSharedPreferences("MyRef", Context.MODE_PRIVATE);
            String name=sharedPreferences.getString("Name","NA");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference reviews = databaseReference.child("Reviews");
            reviews.child(name).setValue(new RatingProperties(ratingBar.getRating(), reviewEt.getText().toString()), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError==null)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Thanks!");
                        builder.setCancelable(false);
                        builder.setMessage("Your Review Is Valuable To Us");
                        builder.setNeutralButton("Close!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ratingBar.setRating(0);
                                reviewEt.setText("");
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                    else
                    {
                        Log.d("Error",databaseError.getMessage());
                        Snackbar.make(v,"Try Later",Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
