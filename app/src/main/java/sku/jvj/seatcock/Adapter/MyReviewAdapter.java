package sku.jvj.seatcock.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sku.jvj.seatcock.Interface.MyInfo.MyInfoPresenter;
import sku.jvj.seatcock.Model.StoreReview;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

/**
 * Created by Android Hong on 2016-09-06.
 */
public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.ViewHolder> {

    private MyInfoPresenter myInfoPresenter;
    private ArrayList<StoreReview> myReviewArrayList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView storeNameTextView;
        public TextView storeAddressTextView;
        public TextView userNameTextView;
        public RatingBar gpaRatingBar;
        public TextView reviewContentTextView;
        public TextView reviewDateTextView;
        public TextView reviewLastTextView;
        public TextView moreVertTextView;
        public ImageView profileImageView;
        public LinearLayout linearLayout;
        public ImageView reviewImageView1;
        public ImageView reviewImageView2;
        public ImageView reviewImageView3;

        public ViewHolder(View v) {
            super(v);
            storeNameTextView = (TextView) v.findViewById(R.id.storeNameTextView);
            storeAddressTextView = (TextView) v.findViewById(R.id.storeAddreSsTextView);
            userNameTextView = (TextView) v.findViewById(R.id.userNameTextView);
            reviewContentTextView = (TextView) v.findViewById(R.id.reviewContentTextView);
            reviewDateTextView = (TextView) v.findViewById(R.id.reviewDateTextView);
            moreVertTextView = (TextView) v.findViewById(R.id.moreVertTextView);
            gpaRatingBar = (RatingBar) v.findViewById(R.id.gpaRatingBar);
            profileImageView = (ImageView) v.findViewById(R.id.profileImageView);
            linearLayout = (LinearLayout) v.findViewById(R.id.pictureLayout);
            reviewLastTextView = (TextView) v.findViewById(R.id.reviewLastTextView);
            reviewImageView1 = (ImageView) v.findViewById(R.id.reviewImageView1);
            reviewImageView2 = (ImageView) v.findViewById(R.id.reviewImageView2);
            reviewImageView3 = (ImageView) v.findViewById(R.id.reviewImageView3);

            reviewImageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myInfoPresenter.navigateToPicture(myReviewArrayList.get(getPosition()).getReviewPicture(),0);
                }
            });

            reviewImageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myInfoPresenter.navigateToPicture(myReviewArrayList.get(getPosition()).getReviewPicture(),1);
                }
            });

            reviewImageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myInfoPresenter.navigateToPicture(myReviewArrayList.get(getPosition()).getReviewPicture(),3);
                }
            });

            moreVertTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myInfoPresenter.showDialog(myReviewArrayList.get(getPosition()).getReviewNum());
                }
            });

        }
    }

    public MyReviewAdapter(MyInfoPresenter myInfoPresenter) {
        this.myReviewArrayList = new ArrayList<>();
        this.myInfoPresenter = myInfoPresenter;
    }

    public void addData(ArrayList<StoreReview> storeReviewArrayList){
        myReviewArrayList = storeReviewArrayList;
    }

    @Override
    public MyReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recycle_myreviewcarditems, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyReviewAdapter.ViewHolder holder, int position) {
        holder.storeNameTextView.setText(myReviewArrayList.get(position).getStoreName());
        holder.storeAddressTextView.setText(myReviewArrayList.get(position).getStoreAddress());
        holder.userNameTextView.setText(myReviewArrayList.get(position).getKakaoName());
        holder.reviewContentTextView.setText(myReviewArrayList.get(position).getContent());
        holder.reviewDateTextView.setText(changeDate(myReviewArrayList.get(position).getDate()));
        holder.gpaRatingBar.setRating(myReviewArrayList.get(position).getGpa());

        if (myReviewArrayList.get(position).getKakaoProfile().equals("noimage")) {
            holder.profileImageView.setImageResource(R.drawable.user_icon);
        } else {
            Glide.with(holder.profileImageView.getContext())
                    .load(myReviewArrayList.get(position).getKakaoProfile())
                    .into(holder.profileImageView);
        }

        checkPicture(holder, myReviewArrayList.get(position).getReviewPicture());
        holder.reviewLastTextView.setText(Util.formatTimeString(myReviewArrayList.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return myReviewArrayList.size();
    }

    /**
     * 사진 정보 가지고오기
     *
     * @param holder
     * @param source
     */
    private void checkPicture(MyReviewAdapter.ViewHolder holder, ArrayList<String> source) {
        if (source != null) {
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.reviewImageView1.setVisibility(View.VISIBLE);
            Glide.with(holder.reviewImageView1.getContext())
                    .load(source.get(0))
                    .into(holder.reviewImageView1);
            if (source.size() == 2) {
                holder.reviewImageView2.setVisibility(View.VISIBLE);
                Glide.with(holder.reviewImageView2.getContext())
                        .load(source.get(1))
                        .into(holder.reviewImageView2);
            } else if (source.size() == 3) {
                holder.reviewImageView2.setVisibility(View.VISIBLE);
                Glide.with(holder.reviewImageView2.getContext())
                        .load(source.get(1))
                        .into(holder.reviewImageView2);
                holder.reviewImageView3.setVisibility(View.VISIBLE);
                Glide.with(holder.reviewImageView3.getContext())
                        .load(source.get(2))
                        .into(holder.reviewImageView3);
            }
        } else {
            holder.linearLayout.setVisibility(View.GONE);
            holder.reviewImageView1.setVisibility(View.GONE);
            holder.reviewImageView2.setVisibility(View.GONE);
            holder.reviewImageView3.setVisibility(View.GONE);
        }
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


