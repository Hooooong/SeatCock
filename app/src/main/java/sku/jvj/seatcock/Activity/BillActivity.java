package sku.jvj.seatcock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import sku.jvj.seatcock.Interface.Bill.BillPresenter;
import sku.jvj.seatcock.Interface.Bill.BillView;
import sku.jvj.seatcock.Presenter.BillPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

/**
 * Created by Android Hong on 2016-10-10.
 */

public class BillActivity extends Activity implements BillView,View.OnClickListener {


    public static Activity mActivity;
    public static Context mContext;

    private BillPresenter billPresenter;
    private LinearLayout zoneLayout;

    private TextView storeNameTextView, storeAddressTextView,seatNumberTextVIew, userNameTextView, userPhoneNumberTextView, reservationDateTextView,
            reservationTimeTextView, reservationPersonTextView, reservationOkTextView, reservationCancelTextView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bill);
        mActivity = this;
        mContext = this;

        initLayout();
        billPresenter = new BillPresenterImpl(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void initLayout(){
        storeNameTextView = (TextView) findViewById(R.id.storeNameTextView);
        storeAddressTextView = (TextView) findViewById(R.id.storeAddressTextView);
        zoneLayout = (LinearLayout)findViewById(R.id.zoneLayout);
        seatNumberTextVIew = (TextView) findViewById(R.id.seatNumberTextVIew);
        userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        userPhoneNumberTextView = (TextView) findViewById(R.id.userPhoneNumberTextView);
        reservationDateTextView = (TextView) findViewById(R.id.reservationDateTextView);
        reservationTimeTextView = (TextView) findViewById(R.id.reservationTimeTextView);
        reservationPersonTextView = (TextView) findViewById(R.id.reservationPersonTextView);
        reservationOkTextView = (TextView) findViewById(R.id.reservationOkTextView);
        reservationCancelTextView = (TextView) findViewById(R.id.reservationCancelTextView);

        reservationOkTextView.setOnClickListener(this);
        reservationCancelTextView.setOnClickListener(this);
    }

    /**
     * 외각 터치
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        return false;
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
    public void displayBill(String storeName, String storeAddress, String checkSeatNum, String userName, String userPhoneNumber, String reservationDate, String reservationTime, String reservationPerson) {

        storeNameTextView.setText(storeName);
        storeAddressTextView.setText(storeAddress);

        if(checkSeatNum != null){
            zoneLayout.setVisibility(View.VISIBLE);
            seatNumberTextVIew.setText(checkSeatNum);
        }else{
            zoneLayout.setVisibility(View.GONE);
        }
        userNameTextView.setText(userName);
        userPhoneNumberTextView.setText(userPhoneNumber);
        reservationDateTextView.setText(reservationDate);
        reservationTimeTextView.setText(reservationTime);
        reservationPersonTextView.setText(reservationPerson + " 명");

    }

    @Override
    public void onClick(View v) {
        int getId = v.getId();
        switch (getId){
            case R.id.reservationOkTextView:
                billPresenter.startReservation();
                break;
            case R.id.reservationCancelTextView:
                finish();
                break;
        }
    }
}
