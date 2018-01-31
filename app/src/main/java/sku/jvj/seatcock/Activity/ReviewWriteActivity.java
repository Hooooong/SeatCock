package sku.jvj.seatcock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sku.jvj.seatcock.Interface.Store.Review.ReviewWritePresenter;
import sku.jvj.seatcock.Interface.Store.Review.ReviewWriteView;
import sku.jvj.seatcock.Presenter.ReviewWritePresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

/**
 * Created by Android Hong on 2016-08-29.
 */
public class ReviewWriteActivity extends AppCompatActivity implements ReviewWriteView, View.OnClickListener, RatingBar.OnRatingBarChangeListener, View.OnTouchListener{

    public static Activity mActivity;
    private Toolbar toolbar;
    private RatingBar indicator_ratingbar1,indicator_ratingbar2,indicator_ratingbar3;
    private EditText reviewEditText;
    private ScrollView scrollView;
    private ImageView imageView,reviewImageView1, reviewImageView2, reviewImageView3;
    private TextView InsertReviewTextView, indicator_ratingbar1_TextView,indicator_ratingbar2_TextView,indicator_ratingbar3_TextView;

    private ReviewWritePresenter reviewWritePresenter;
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        initLayout();
        reviewWritePresenter = new ReviewWritePresenterImpl(this);
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout() {
        mActivity = this;

        toolbar = (Toolbar) findViewById(R.id.reviewWriteToolbar);
        toolbar.setTitle("리뷰 작성");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        InsertReviewTextView = (TextView) findViewById(R.id.InsertReviewTextView);
        indicator_ratingbar1_TextView = (TextView) findViewById(R.id.indicator_ratingbar1_TextView);
        indicator_ratingbar2_TextView = (TextView) findViewById(R.id.indicator_ratingbar2_TextView);
        indicator_ratingbar3_TextView = (TextView) findViewById(R.id.indicator_ratingbar3_TextView);

        indicator_ratingbar1 = (RatingBar) findViewById(R.id.indicator_ratingbar1);
        indicator_ratingbar2 = (RatingBar) findViewById(R.id.indicator_ratingbar2);
        indicator_ratingbar3 = (RatingBar) findViewById(R.id.indicator_ratingbar3);

        indicator_ratingbar1.setIsIndicator(false);
        indicator_ratingbar2.setIsIndicator(false);
        indicator_ratingbar3.setIsIndicator(false);

        reviewImageView1 = (ImageView) findViewById(R.id.reviewImageView1);
        reviewImageView2 = (ImageView) findViewById(R.id.reviewImageView2);
        reviewImageView3 = (ImageView) findViewById(R.id.reviewImageView3);

        reviewEditText = (EditText) findViewById(R.id.reviewEditText);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        imageView = (ImageView) findViewById(R.id.addPhotoImageView);


        InsertReviewTextView.setOnClickListener(this);
        scrollView.setOnTouchListener(this);
        indicator_ratingbar1.setOnRatingBarChangeListener(this);
        indicator_ratingbar2.setOnRatingBarChangeListener(this);
        indicator_ratingbar3.setOnRatingBarChangeListener(this);
        reviewImageView1.setOnClickListener(this);
        reviewImageView2.setOnClickListener(this);
        reviewImageView3.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = getRealImagePath(uri);
            reviewWritePresenter.addPicture(uri,path);
        } else if (requestCode == REQ_CHANGE_IMAGE1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = getRealImagePath(uri);
            reviewWritePresenter.modifyPicture(uri,path,REQ_CHANGE_IMAGE1-1);
        } else if (requestCode == REQ_CHANGE_IMAGE2 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = getRealImagePath(uri);
            reviewWritePresenter.modifyPicture(uri,path,REQ_CHANGE_IMAGE2-1);
        } else if (requestCode == REQ_CHANGE_IMAGE3 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = getRealImagePath(uri);
            reviewWritePresenter.modifyPicture(uri,path,REQ_CHANGE_IMAGE3-1);
        } else {
            return;
        }
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
    public void hideProgressAndFinish() {
        progressDialog.dismiss();
        finish();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPictureDialog(final int pictureNum) {
        final CharSequence[] items = {"수정", "삭제"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#ffb300'>사진</font>"));
        alertDialogBuilder.setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("id", Integer.toString(id));
                        switch (id) {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, pictureNum);
                                break;
                            case 1:
                                reviewWritePresenter.deletePicture(pictureNum);
                                break;
                        }

                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void showBackDialog() {
        if (reviewEditText.length() > 0 || reviewWritePresenter.getPictureSize() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("리뷰 작성");
            builder.setMessage("현재 작성중인 리뷰가 있습니다. \n리뷰 작성창을 나가시겠습니까?");

            String positiveText = "OK";
            builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            String negativeText = "Cancel";
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // negative button logic
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            finish();
        }
    }

    @Override
    public void navigateToGallery(int number) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, number);
    }

    @Override
    public void setPicture(Uri uri) {
        switch (reviewWritePresenter.getPictureSize()){
            case 1:
                reviewImageView1.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(uri.toString())
                        .into(reviewImageView1);
                break;
            case 2:
                reviewImageView2.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(uri.toString())
                        .into(reviewImageView2);
                break;
            case 3:
                reviewImageView3.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(uri.toString())
                        .into(reviewImageView3);
                break;
        }
    }

    @Override
    public void laterModifyPicture(Uri uri, int pictureNum) {
        switch (pictureNum){
            case 0:
                Glide.with(this)
                        .load(uri.toString())
                        .into(reviewImageView1);
                break;
            case 1:
                Glide.with(this)
                        .load(uri.toString())
                        .into(reviewImageView2);
                break;
            case 2:
                Glide.with(this)
                        .load(uri.toString())
                        .into(reviewImageView3);
                break;
        }
    }

    @Override
    public void laterDeletePictrue(ArrayList arrayList) {

        switch (arrayList.size()) {
            case 0:
                reviewImageView1.setVisibility(View.GONE);
                break;
            case 1:
                Glide.with(this)
                        .load(arrayList.get(0))
                        .into(reviewImageView1);
                reviewImageView2.setVisibility(View.GONE);
                break;
            case 2:
                Glide.with(this)
                        .load(arrayList.get(0))
                        .into(reviewImageView1);
                Glide.with(this)
                        .load(arrayList.get(1))
                        .into(reviewImageView2);
                reviewImageView3.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int getId = v.getId();
        switch (getId){
            case R.id.InsertReviewTextView:
                reviewWritePresenter.insertReviewData(String.valueOf(indicator_ratingbar1.getRating()),String.valueOf(indicator_ratingbar2.getRating()),String.valueOf(indicator_ratingbar3.getRating()), reviewEditText.getText().toString());
                break;
            case R.id.reviewImageView1:
                this.showPictureDialog(1);
                break;
            case R.id.reviewImageView2:
                this.showPictureDialog(2);
                break;
            case R.id.reviewImageView3:
                this.showPictureDialog(3);
                break;
            case R.id.addPhotoImageView:
                reviewWritePresenter.checkPermission();
                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if(ratingBar == indicator_ratingbar1){
            indicator_ratingbar1_TextView.setText(String.valueOf(indicator_ratingbar1.getRating()));
        }else if(ratingBar == indicator_ratingbar2){
            indicator_ratingbar2_TextView.setText(String.valueOf(indicator_ratingbar2.getRating()));
        }else{
            indicator_ratingbar3_TextView.setText(String.valueOf(indicator_ratingbar2.getRating()));
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(reviewEditText, 0);
        return true;
    }

    /**
     * Appbar 메뉴 생성 초기화
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    /**
     * Appbar 메뉴 선택 이벤트
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_back) {
            /*this.showBackDialog();*/
            this.showBackDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 기기 버튼(뒤로가기) 눌렀을 시
     */
    @Override
    public void onBackPressed() {
        this.showBackDialog();
    }

    /**
     * 뒤로가기 버튼 눌렀을 시
     */
    @Override
    public boolean onSupportNavigateUp() {
        this.showBackDialog();
        return true;
    }

    /**
     * Uri Path 경로 알아오기
     * @param uriPath
     * @return
     */
    public  String getRealImagePath(Uri uriPath) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uriPath, proj, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        path = path.substring(1);
        return path;
    }


}
