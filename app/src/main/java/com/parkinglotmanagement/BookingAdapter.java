package com.parkinglotmanagement;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Created by HP_PC on 02-04-2018.
 */

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    private List<MyBookingBean> list;
    public BookingAdapter(List<MyBookingBean> list)
    {
        this.list=list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singlerow_bookings, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyBookingBean myBookingBean = list.get(position);
        holder.tvSlotMB.setText("Slot No - "+myBookingBean.getSlot());
        holder.tvFromMB.setText("Starts From - "+myBookingBean.getFrom().substring(0,19));
        holder.tvToMB.setText("Uptill - "+myBookingBean.getTo().substring(0,19));
        holder.tvFeesMB.setText("Payment - $"+myBookingBean.getFees());
        holder.tvPlateMB.setText("License Plate No - "+myBookingBean.getPlate());
        holder.btNavigateMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:49.2035681,-122.91268939999998?q=49.2035681,-122.91268939999998(Douglas College)"));
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvSlotMB,tvPlateMB,tvFromMB,tvToMB,tvFeesMB;
        Button btNavigateMB;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvSlotMB=(TextView)itemView.findViewById(R.id.tvSLotMB);
            tvPlateMB=(TextView)itemView.findViewById(R.id.tvPlateMB);
            tvFromMB=(TextView)itemView.findViewById(R.id.tvFromMB);
            tvToMB=(TextView)itemView.findViewById(R.id.tvToMB);
            tvFeesMB=(TextView)itemView.findViewById(R.id.tvFeesMB);
            btNavigateMB=(Button)itemView.findViewById(R.id.btNavigateMB);
        }
    }
   }
