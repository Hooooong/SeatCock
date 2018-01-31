package sku.jvj.seatcock.Adapter;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kakao.auth.Session;

import java.util.ArrayList;

import sku.jvj.seatcock.Interface.Store.Review.ReviewPresenter;
import sku.jvj.seatcock.Model.StoreReview;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

/**
 * Created by Android Hong on 2016-08-07.
 */
public class StoreReviewAdapter extends RecyclerView.Adapter<StoreReviewAdapter.ViewHolder> {

    private ReviewPresenter reviewPresenter;
    private ArrayList<StoreReview> storeReviewArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder {
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
                    reviewPresenter.navigateToPicture(storeReviewArrayList.get(getPosition()).getReviewPicture(),0);
                }
            });

            reviewImageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reviewPresenter.navigateToPicture(storeReviewArrayList.get(getPosition()).getReviewPicture(),1);
                }
            });

            reviewImageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reviewPresenter.navigateToPicture(storeReviewArrayList.get(getPosition()).getReviewPicture(),2);
                }
            });

            moreVertTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!Session.getCurrentSession().isOpened() ){
                        Snackbar.make(v, "로그인을 먼저 해주세요", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else if( storeReviewArrayList.get(getPosition()).getKakaoId().equals(reviewPresenter.getKakaoId())) {
                        reviewPresenter.showDialog(storeReviewArrayList.get(getPosition()).getReviewNum());
                    }else{
                        Snackbar.make(v, "회원님의 리뷰가 아닙니다", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
        }
    }

    public StoreReviewAdapter(ReviewPresenter reviewPresenter) {
        storeReviewArrayList = new ArrayList<>();
        this.reviewPresenter = reviewPresenter;
    }

    @Override
    public StoreReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_reviewcarditems, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(StoreReviewAdapter.ViewHolder holder, int position) {

        holder.userNameTextView.setText(storeReviewArrayList.get(position).getKakaoName());
        holder.reviewContentTextView.setText(storeReviewArrayList.get(position).getContent());
        holder.reviewDateTextView.setText(changeDate(storeReviewArrayList.get(position).getDate()));
        holder.gpaRatingBar.setRating(storeReviewArrayList.get(position).getGpa());

        if (storeReviewArrayList.get(position).getKakaoProfile().equals("noimage")) {
            holder.profileImageView.setImageResource(R.drawable.user_icon);
        } else {
            Glide.with(holder.profileImageView.getContext())
                    .load(storeReviewArrayList.get(position).getKakaoProfile())
                    .into(holder.profileImageView);
        }

        checkPicture(holder, storeReviewArrayList.get(position).getReviewPicture());
        holder.reviewLastTextView.setText(Util.formatTimeString(storeReviewArrayList.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return storeReviewArrayList.size();
    }

    public void addData(ArrayList<StoreReview> listitems) {
        this.storeReviewArrayList = listitems;
    }

    /**
     * 사진 정보 가지고오기
     *
     * @param holder
     * @param source
     */
    private void checkPicture(StoreReviewAdapter.ViewHolder holder, ArrayList<String> source) {
       if(source == null){
           holder.linearLayout.setVisibility(View.GONE);
           holder.reviewImageView1.setVisibility(View.GONE);
           holder.reviewImageView2.setVisibility(View.GONE);
           holder.reviewImageView3.setVisibility(View.GONE);
       }else{
           switch (source.size()){

               case 1:
                   holder.linearLayout.setVisibility(View.VISIBLE);
                   holder.reviewImageView1.setVisibility(View.VISIBLE);
                   Glide.with(holder.reviewImageView1.getContext())
                           .load(source.get(0))
                           .into(holder.reviewImageView1);
                   break;
               case 2:
                   holder.linearLayout.setVisibility(View.VISIBLE);
                   holder.reviewImageView1.setVisibility(View.VISIBLE);
                   holder.reviewImageView2.setVisibility(View.VISIBLE);
                   Glide.with(holder.reviewImageView1.getContext())
                           .load(source.get(0))
                           .into(holder.reviewImageView1);

                   Glide.with(holder.reviewImageView2.getContext())
                           .load(source.get(1))
                           .into(holder.reviewImageView2);
                   break;
               case 3:
                   holder.linearLayout.setVisibility(View.VISIBLE);
                   holder.reviewImageView1.setVisibility(View.VISIBLE);
                   holder.reviewImageView2.setVisibility(View.VISIBLE);
                   holder.reviewImageView3.setVisibility(View.VISIBLE);
                   Glide.with(holder.reviewImageView1.getContext())
                           .load(source.get(0))
                           .into(holder.reviewImageView1);

                   Glide.with(holder.reviewImageView2.getContext())
                           .load(source.get(1))
                           .into(holder.reviewImageView2);

                   Glide.with(holder.reviewImageView3.getContext())
                           .load(source.get(2))
                           .into(holder.reviewImageView3);
                   break;
           }
       }
    }

    /**
     * 2016-06-04 => 2016.06.04
     *
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