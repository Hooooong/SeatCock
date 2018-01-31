package sku.jvj.seatcock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sku.jvj.seatcock.Adapter.MyReviewAdapter;
import sku.jvj.seatcock.Interface.MyInfo.MyInfoPresenter;
import sku.jvj.seatcock.Interface.MyInfo.MyInfoView;
import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.StoreReview;
import sku.jvj.seatcock.Presenter.MyInfoPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;


/**
 * Created by Android Hong on 2016-09-02.
 */
public class MyInfoActivity extends AppCompatActivity implements MyInfoView , View.OnClickListener, AppBarLayout.OnOffsetChangedListener{
    boolean isShow = false;

    public static Activity mActivity;
    public static Context mContext;

    public Toolbar toolbar;
    public AppBarLayout appBarLayout;
    public ImageView userImageView;
    public TextView userNickNameTextView;
    public TextView userProfileRefresh;
    public TextView userNameTextView;
    public TextView userBirthdayTextView;
    public TextView userSexTextView;
    public TextView userPhoneNumberTextView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private MyInfoPresenter myInfoPresenter;
    private ProgressDialog progressDialog;
    private TextView noReivewTextView;
    private TextView myReviewTextView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyReviewAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        myInfoPresenter = new MyInfoPresenterImpl(this);
        initLayout();
        myInfoPresenter.getMyReviewData();
        myInfoPresenter.getMyInfo();
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout() {
        mContext = this;
        mActivity = this;

        toolbar = (Toolbar) findViewById(R.id.myInfoToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.myInfoBarLayout);
        userProfileRefresh = (TextView)findViewById(R.id.userProfileRefresh);
        userImageView = (ImageView)findViewById(R.id.userImageView);
        userNickNameTextView = (TextView)findViewById(R.id.userNickNameTextView);
        userNameTextView = (TextView)findViewById(R.id.userNameTextView);
        userBirthdayTextView = (TextView)findViewById(R.id.userBirthdayTextView);
        userSexTextView = (TextView)findViewById(R.id.userSexTextView);
        userPhoneNumberTextView = (TextView)findViewById(R.id.userPhoneNumberTextView);
        noReivewTextView = (TextView) findViewById(R.id.noReivewTextView);
        myReviewTextView = (TextView) findViewById(R.id.myReviewTextView);
        mRecyclerView = (RecyclerView)findViewById(R.id.myReviewRecyclerView);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyReviewAdapter(myInfoPresenter);
        mRecyclerView.setAdapter(mAdapter);

        userProfileRefresh.setOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void showProgress() {
        progressDialog = Util.createProgressDialog(this);
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
        myInfoPresenter.getMyReviewData();
    }

    @Override
    public void displayMyInfo(final KakaoUser kakaoUser) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (kakaoUser.getProfile_image().equals("noimage")) {
                    userImageView.setImageResource(R.drawable.user_icon);
                } else {
                    Glide.with(getApplicationContext())
                            .load(kakaoUser.getProfile_image())
                            .into(userImageView);
                }
                userNickNameTextView.setText(kakaoUser.getNickname());
                userNameTextView.setText(kakaoUser.getName());
                userBirthdayTextView.setText(changeJubun(kakaoUser.getJubun()));
                userSexTextView.setText(sex(kakaoUser.getJubun()));
                userPhoneNumberTextView.setText(kakaoUser.getPhoneNumber());
            }
        });
    }

    @Override
    public void dislpayMyReview(ArrayList<StoreReview> storeReviewArrayList) {
        myReviewTextView.setText(String.valueOf(storeReviewArrayList.size()));
        if (storeReviewArrayList.size() == 0) {
            noReivewTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            noReivewTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.addData(storeReviewArrayList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void navigateToPicture(ArrayList<String> pictureArrayList, int index) {
        Intent pic = new Intent(this, PictureActivity.class);
        pic.putExtra("picture", pictureArrayList);
        pic.putExtra("index", index);
        startActivity(pic);
    }

    @Override
    public void navigateToDetailReview(StoreReview storeReview) {

    }

    @Override
    public void showChoiceDialog(final int reviewNum) {
        final CharSequence[] items = {"수정", "삭제"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#ffb300'>리뷰</font>"));

        alertDialogBuilder.setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (id) {
                            case 0:
                                myInfoPresenter.getReviewDetailData(reviewNum);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("리뷰 삭제");
        builder.setMessage("리뷰를 삭제하시겠습니까?");

        String positiveText = "확인";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myInfoPresenter.deleteMyReview(reviewNum);
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
    public void onClick(View v) {
        int getId = v.getId();
        switch (getId){
            case R.id.userProfileRefresh:
                myInfoPresenter.refreshMyProfile();
            break;
        }

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int scrollRange = -1;
        if (scrollRange == -1) {
            scrollRange = appBarLayout.getTotalScrollRange();
        }
        if (scrollRange + verticalOffset <= 210) {
            collapsingToolbarLayout.setTitle("나의 정보");
            isShow = true;
        } else if (isShow) {
            collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
            isShow = false;
        }
    }


    /**
     * 뒤로가기 버튼 눌렀을 시
     */
    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    /**
     * 기기 버튼(뒤로가기) 눌렀을 시
     */
    @Override
    public void onBackPressed() {
        this.finish();
    }

    /**
     * 주번 변경 XX.XX.XX
     * @param jubun
     * @return
     */
    private String changeJubun(int jubun){

        String result = Integer.toString(jubun).substring(0,6);

        String year = result.substring(0,2);
        String month = result.substring(2,4);
        String day = result.substring(4,6);

        return year+"."+month+"."+day;
    }

    /**
     * 성별 변경 (남성, 여성)
     * @param jubun
     * @return
     */
    private String sex(int jubun){
        String sex;
        if(Integer.toString(jubun).charAt(6)== '1'){
            sex = "남성";
        }else{
            sex = "여성";
        }
        return sex;
    }

}
