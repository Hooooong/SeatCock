package sku.jvj.seatcock.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sku.jvj.seatcock.Interface.Main.MainView;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;


/**
 * Created by Android Hong on 2016-08-07.
 */
public class StoreMainAdapter extends RecyclerView.Adapter<StoreMainAdapter.ViewHolder>{

    private ArrayList<Store> storeArrayList;
    private MainView view;

    public class ViewHolder extends RecyclerView.ViewHolder {

            // 점포 Card
            public TextView titleTextView;
            public TextView addressTextView;
            public TextView distanceTextView;
            public TextView reviewCountTextView;
            public TextView gpaTextView;
            public ImageView coverImageView;
            public Button seatSituation;
            public ImageView normalImageView;
        public ImageView zoneImageView;

        public ViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            addressTextView = (TextView)v.findViewById(R.id.addressTextView);
            distanceTextView = (TextView) v.findViewById(R.id.distanceTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            seatSituation = (Button) v.findViewById(R.id.seatSituation);
            reviewCountTextView = (TextView)v.findViewById(R.id.reviewCountTextView);
            gpaTextView = (TextView)v.findViewById(R.id.gpaTextView);

            normalImageView = (ImageView)v.findViewById(R.id.normalImageView);
            zoneImageView = (ImageView)v.findViewById(R.id.zoneImageView);

            /**
             * CardView의 '사진' 클릭했을 때 이벤트
             * */
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.navigateToStore(storeArrayList.get(getPosition()));
                }
            });

            /**
             * CardView의 '좌석 현황 버튼' 클릭했을 때 이벤트
             * */
            seatSituation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.navigateToSeat(storeArrayList.get(getPosition()));
                }
            });
        }
    }

    public StoreMainAdapter(MainView view) {
        storeArrayList = new ArrayList<>();
        this.view = view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_storecarditems, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleTextView.setText(storeArrayList.get(position).getStoreName());
        holder.addressTextView.setText(storeArrayList.get(position).getStoreAddress());
        holder.distanceTextView.setText(Util.distanceCheck(storeArrayList.get(position).getStoreDistance()));

        checkMainImage(holder,position,storeArrayList.get(position).getImageResourceId());

        String seatSituation = Integer.toString(storeArrayList.get(position).getSeatUseCountSituation())+"/"+ Integer.toString(storeArrayList.get(position).getSeatTotalCountSituation());
        holder.seatSituation.setText(seatSituation);
        holder.reviewCountTextView.setText(String.valueOf(storeArrayList.get(position).getReviewConunt()));
        holder.gpaTextView.setText(String.valueOf(storeArrayList.get(position).getGpa()));
        holder.normalImageView.setImageResource(checkNormallmageResource(storeArrayList.get(position).isNormalReservation()));
        holder.zoneImageView.setImageResource(checkZoneImageResource(storeArrayList.get(position).isZoneReservation()));
    }

    @Override
    public int getItemCount() {
        return storeArrayList.size();
    }

    public void addData(ArrayList<Store> listItems){
        this.storeArrayList = listItems;
    }

    public int checkNormallmageResource(boolean source){
        int result;
        if(source){
            result = R.drawable.img_normalreservation;
        }else{
            result = R.drawable.img_unnormalreservation;
        }
        return result;
    }
    public int checkZoneImageResource(boolean source){
        int result;
        if(source){
            result = R.drawable.img_zonereservation;
        }else{
            result = R.drawable.img_unzonereservation;
        }
        return result;
    }
    public void checkMainImage(ViewHolder holder, int position, String source){
        if(source.equals("0") ){
            Glide.with(holder.coverImageView.getContext())
                    .load(R.drawable.img_noimage)
                    .into(holder.coverImageView);
        }else{
            Glide.with(holder.coverImageView.getContext())
                    .load(storeArrayList.get(position).getImageResourceId())
                    .into(holder.coverImageView);
        }
    }

}