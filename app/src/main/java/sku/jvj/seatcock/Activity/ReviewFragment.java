package sku.jvj.seatcock.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import sku.jvj.seatcock.Adapter.StoreReviewAdapter;
import sku.jvj.seatcock.Interface.Store.Review.ReviewPresenter;
import sku.jvj.seatcock.Interface.Store.Review.ReviewView;
import sku.jvj.seatcock.Model.StoreReview;
import sku.jvj.seatcock.Presenter.ReviewPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;


/**
 * Created by Android Hong on 2016-08-29.
 */
public class ReviewFragment extends Fragment implements ReviewView{

    private String storeId;
    private View view;

    private ProgressDialog progressDialog;
    private ReviewPresenter reviewPresenter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private StoreReviewAdapter mAdapter;
    private RelativeLayout noReviewLayout;

    /**
     * 생성자
     * @return
     */
    public static ReviewFragment create(String storeId) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("storeId",storeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store_review, container, false);
        storeId = (String)getArguments().getSerializable("storeId");
        reviewPresenter = new ReviewPresenterImpl(this);
        initLayout();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reviewPresenter.getReviewData(storeId);
    }

    private void initLayout(){
        noReviewLayout = (RelativeLayout)view.findViewById(R.id.noReviewLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.reviewCardView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StoreReviewAdapter(reviewPresenter);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showProgress() {
        progressDialog = Util.createProgressDialog(getActivity());
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showChoiceDialog(final int reviewNum) {
        final CharSequence[] items = {"수정", "삭제"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#ffb300'>리뷰</font>"));

        alertDialogBuilder.setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (id) {
                            case 0:
                                reviewPresenter.getReviewDetailData(reviewNum);
                                break;
                            case 1:
                                showDeleteDialog(reviewNum);
                                break;
                        }
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void showDeleteDialog(final int reviewNum) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("리뷰 삭제");
        builder.setMessage("리뷰를 삭제하시겠습니까?");

        String positiveText = "확인";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reviewPresenter.deleteReview(reviewNum);
                dialog.dismiss();
            }
        });

        String negativeText = "취소";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showToast(String alarm) {
        Toast.makeText(getContext(), alarm, Toast.LENGTH_SHORT).show();
        reviewPresenter.getReviewData(storeId);
    }

    @Override
    public void displayReview(ArrayList<StoreReview> storeReviewArrayList) {
        if(storeReviewArrayList.size() == 0){
            noReviewLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else{
            noReviewLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.addData(storeReviewArrayList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void navigateToPicture(ArrayList<String> pictureArrayList, int index) {
        Intent pic = new Intent(getContext(), PictureActivity.class);
        pic.putExtra("picture", pictureArrayList);
        pic.putExtra("index", index);
        startActivity(pic);
    }

    @Override
    public void navigateToDetailReview(StoreReview storeReview) {
        Intent reviewDetail = new Intent(getContext(), PictureActivity.class);
        reviewDetail.putExtra("storeReview",  storeReview);
        startActivity(reviewDetail);
    }
}
