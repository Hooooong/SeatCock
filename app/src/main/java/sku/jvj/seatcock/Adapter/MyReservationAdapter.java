package sku.jvj.seatcock.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sku.jvj.seatcock.Interface.MyReservation.MyReservationPresenter;
import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.StoreReservation;
import sku.jvj.seatcock.R;

/**
 * Created by Android Hong on 2016-09-06.
 */
public class MyReservationAdapter extends RecyclerView.Adapter<MyReservationAdapter.ViewHolder> {

    private  KakaoUser kakaoUser;
    private MyReservationPresenter myReservationPresenter;
    private ArrayList<StoreReservation> myReservationArrayList;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView storeNameTextView;
        public TextView storeAddressTextView;
        public LinearLayout zoneLayout;
        public TextView seatNumberTextVIew;
        public TextView reservationNameTextView;
        public TextView reservationPhoneNumberTextView;
        public TextView reservationDateTextView;
        public TextView reservationTimeTextView;
        public TextView reservationPersonTextView;
        public TextView storePhoneNumberTextView;
        public Button reservationStateBtn;
        public ImageView zhchkImageView;

        public ViewHolder(View v) {
            super(v);
            storeNameTextView = (TextView)v.findViewById(R.id.storeNameTextView);
            storeAddressTextView = (TextView)v.findViewById(R.id.storeAddressTextView);
            zoneLayout = (LinearLayout) v.findViewById(R.id.zoneLayout);
            seatNumberTextVIew = (TextView) v.findViewById(R.id.seatNumberTextVIew);
            storePhoneNumberTextView =(TextView)v.findViewById(R.id.storePhoneNumberTextView);
            reservationNameTextView = (TextView) v.findViewById(R.id.reservationNameTextView);
            reservationPhoneNumberTextView = (TextView) v.findViewById(R.id.reservationPhoneNumberTextView);
            reservationDateTextView = (TextView) v.findViewById(R.id.reservationDateTextView);
            reservationTimeTextView = (TextView) v.findViewById(R.id.reservationTimeTextView);
            reservationPersonTextView = (TextView) v.findViewById(R.id.reservationPersonTextView);
            reservationStateBtn = (Button) v.findViewById(R.id.reservationStateBtn);
            zhchkImageView = (ImageView)v.findViewById(R.id.zhchkImageView);

            //확인 버튼 눌렀을 때
            reservationStateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myReservationPresenter.showChangeDialog(myReservationArrayList.get( getPosition()).getDate(), myReservationArrayList.get( getPosition()).getTime().substring(0,5),myReservationArrayList.get( getPosition()).getStoreId(),myReservationArrayList.get( getPosition()).getZnCheck());
                }
            });
        }
    }

    public MyReservationAdapter(MyReservationPresenter myReservationPresenter, KakaoUser kakaoUser) {
        this.myReservationArrayList = new ArrayList<>();
        this.myReservationPresenter = myReservationPresenter;
        this.kakaoUser = kakaoUser;
    }

    public void addData(ArrayList<StoreReservation> storeReservationArrayList){
        this.myReservationArrayList = storeReservationArrayList;
    }

    @Override
    public MyReservationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_myreservationcarditems, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyReservationAdapter.ViewHolder holder, int position) {
        holder.storeNameTextView.setText(myReservationArrayList.get(position).getStoreName());
        holder.storeAddressTextView.setText(myReservationArrayList.get(position).getStoreAddress());

        if(myReservationArrayList.get(position).getZnCheck().equals("N")) {
            holder.zoneLayout.setVisibility(View.GONE);
            holder.zhchkImageView.setImageResource(R.drawable.img_normal_mark);
        }else{
            holder.zoneLayout.setVisibility(View.VISIBLE);
            holder.seatNumberTextVIew.setText(myReservationArrayList.get(position).getSeatNum());
            holder.zhchkImageView.setImageResource(R.drawable.img_zone_mark);
        }

        holder.storePhoneNumberTextView.setText(myReservationArrayList.get(position).getStorePhoneNumber());
        holder.reservationNameTextView.setText(kakaoUser.getName());
        holder.reservationPhoneNumberTextView.setText(kakaoUser.getPhoneNumber());
        holder.reservationDateTextView.setText(changeDate(myReservationArrayList.get(position).getDate()));
        holder.reservationTimeTextView.setText(myReservationArrayList.get(position).getTime());
        holder.reservationPersonTextView.setText(Integer.toString(myReservationArrayList.get(position).getPerson()) + " 명");

        if(myReservationArrayList.get(position).isComplete()){
            // complete 가 0이라면 True
            // 현재 예약중
            holder.reservationStateBtn.setText("예약 중");
            holder.reservationStateBtn.setBackgroundColor(Color.parseColor("#89C348"));
            holder.reservationStateBtn.setClickable(true);

        }else{
            // complete 가 1이라면 false
            // 지난 예약
            holder.reservationStateBtn.setText("예약 완료");
            holder.reservationStateBtn.setBackgroundColor(Color.parseColor("#ADADAD"));
            holder.reservationStateBtn.setClickable(false);
        }

    }

    @Override
    public int getItemCount() {
        return myReservationArrayList.size();
    }

    /**
     * 2016-06-04 => 2016.06.04
     * @param source
     * @return
     */
    private String changeDate(String source) {
        String date = source.substring(0, 10);
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        return year + "." + month + "." + day;
    }


}
