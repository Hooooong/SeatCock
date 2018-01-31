package sku.jvj.seatcock.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sku.jvj.seatcock.Adapter.MyReservationAdapter;
import sku.jvj.seatcock.Interface.MyReservation.MyReservationPresenter;
import sku.jvj.seatcock.Interface.MyReservation.MyReservationView;
import sku.jvj.seatcock.Model.StoreReservation;
import sku.jvj.seatcock.Presenter.MyReservationPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

/**
 * Created by Android Hong on 2016-09-06.
 */
public class MyReservationActivity extends AppCompatActivity implements MyReservationView, View.OnClickListener {

    public static Context mContext;
    private MyReservationPresenter myReservationPresenter;
    private TextView nowReservation;
    private TextView beforeReservation;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyReservationAdapter mAdapter;
    private RelativeLayout noReservationLayout;
    private TextView noReservationTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreservation);

        toolbar = (Toolbar) findViewById(R.id.myReservationToolbar);
        toolbar.setTitle("예약 정보");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myReservationPresenter = new MyReservationPresenterImpl(this);
        initLayout();
    }

    /**
     * Search Layout 초기화
     */
    private void initLayout() {
        mContext = this;

        nowReservation = (TextView) findViewById(R.id.nowReservation);
        beforeReservation = (TextView) findViewById(R.id.beforeReservation);
        noReservationTextView = (TextView) findViewById(R.id.noReservationTextView);
        noReservationLayout = (RelativeLayout)findViewById(R.id.noReservationLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.myReservationRecyclerView);

        nowReservation.setBackgroundColor(Color.parseColor("#89C348"));
        nowReservation.setTextColor(Color.parseColor("#FFFFFF"));
        beforeReservation.setBackgroundColor(Color.parseColor("#FAFAFA"));
        beforeReservation.setTextColor(Color.parseColor("#7A7A7A"));

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyReservationAdapter(myReservationPresenter, myReservationPresenter.getKakaoUser());
        mRecyclerView.setAdapter(mAdapter);

        nowReservation.setOnClickListener(this);
        beforeReservation.setOnClickListener(this);

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
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showChangeDialog(final String date, final String time, final String storeId, final String ZNchk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경고");
        builder.setIcon(R.drawable.ic_error_outline_black);
        builder.setMessage("확인을 누르시면 '현재 예약' 정보가 '지난 예약'으로 변경이 됩니다. 점포가 누르지 않은 경우 불이익이 발생할 수 있습니다.불이익에 대해서는 어떠한 손해 배상도 불가합니다.");

        String positiveText = "확인";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myReservationPresenter.changeReservation(date, time, storeId, ZNchk);
            }
        });

        String negativeText = "취소";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
    }

    @Override
    public void displayMyReview(ArrayList<StoreReservation> storeReservationArrayList) {
        if (storeReservationArrayList.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            noReservationLayout.setVisibility(View.VISIBLE);
            noReservationTextView.setText("현재 예약 정보가 없습니다.\n새로운 예약을 하시길 바랍니다.");
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            noReservationLayout.setVisibility(View.GONE);
            mAdapter.addData(storeReservationArrayList);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 기기 버튼(뒤로가기) 눌렀을 시
     */
    @Override
    public void onBackPressed() {
        this.finish();
    }

    /**
     * 뒤로가기 버튼 눌렀을 시
     */
    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }


    @Override
    public void onClick(View v) {
        int getId = v.getId();
        switch (getId) {
            case R.id.beforeReservation:
                beforeReservation.setBackgroundColor(Color.parseColor("#89C348"));
                beforeReservation.setTextColor(Color.parseColor("#FFFFFF"));
                nowReservation.setBackgroundColor(Color.parseColor("#FAFAFA"));
                nowReservation.setTextColor(Color.parseColor("#7A7A7A"));

                myReservationPresenter.getReservationData("beforeReservation");
                break;
            case R.id.nowReservation:
                nowReservation.setBackgroundColor(Color.parseColor("#89C348"));
                nowReservation.setTextColor(Color.parseColor("#FFFFFF"));
                beforeReservation.setBackgroundColor(Color.parseColor("#FAFAFA"));
                beforeReservation.setTextColor(Color.parseColor("#7A7A7A"));

                myReservationPresenter.getReservationData("nowReservation");
                break;
        }
    }
}
