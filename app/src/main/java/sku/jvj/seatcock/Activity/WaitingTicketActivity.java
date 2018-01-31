package sku.jvj.seatcock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sku.jvj.seatcock.Interface.Waiting.WaitingPresenter;
import sku.jvj.seatcock.Interface.Waiting.WaitingView;
import sku.jvj.seatcock.Presenter.WaitingPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

import static sku.jvj.seatcock.Interface.Seat.SeatView.WATINGTICKET_CANCELED;
import static sku.jvj.seatcock.Interface.Seat.SeatView.WATINGTICKET_OK;

/**
 * Created by Android Hong on 2016-10-31.
 */

public class WaitingTicketActivity extends Activity implements WaitingView,View.OnClickListener {

    public static Activity mActivity;
    public static Context mContext;

    private TextView waitingNumberTextView, waitingPersonTextView, waitingTimeTextView, userNameTextView, userPhoneNumberTextView, personNumberTextView, todayTextView;
    private Button waitingBtn;
    private ImageView minus, plus;
    private ProgressDialog progressDialog;
    private WaitingPresenter waitingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_waitingticket);
        mActivity = this;
        mContext = this;

        initLayout();
        waitingPresenter = new WaitingPresenterImpl(this);

    }

    private void initLayout() {
        waitingNumberTextView = (TextView) findViewById(R.id.waitingNumberTextView);
        waitingPersonTextView = (TextView) findViewById(R.id.waitingPersonTextView);
        waitingTimeTextView = (TextView) findViewById(R.id.waitingTimeTextView);
        userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        userPhoneNumberTextView = (TextView) findViewById(R.id.userPhoneNumberTextView);
        personNumberTextView = (TextView) findViewById(R.id.personNumberTextView);
        todayTextView = (TextView) findViewById(R.id.todayTextView);
        waitingBtn = (Button)findViewById(R.id.waitingBtn);
        minus = (ImageView)findViewById(R.id.minus);
        plus = (ImageView)findViewById(R.id.plus);

        minus.setOnClickListener(this);
        plus.setOnClickListener(this);
        waitingBtn.setOnClickListener(this);
    }

    /**
     * 외각 터치
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        return false;
    }

    /**
     * 기기 버튼(뒤로가기) 눌렀을 시
     */
    @Override
    public void onBackPressed() {
        setResult(WATINGTICKET_CANCELED);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setResult(WATINGTICKET_CANCELED);
        finish();
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
    }

    @Override
    public void showToastEnd(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
        //다른 Toast 메세지랑 다르게
        setResult(WATINGTICKET_OK);
        finish();
    }

    @Override
    public void displayPersonNum(int num) {
        personNumberTextView.setText(Integer.toString(num));
    }

    @Override
    public void displayWaitingTicket(String waitingNumber, String waitingPerson, String waitingTime, String userName, String userPhoneNumber, int person, String today) {
        waitingNumberTextView.setText(waitingNumber);
        waitingPersonTextView.setText(waitingPerson);
        waitingTimeTextView.setText(waitingTime);
        userNameTextView.setText(userName);
        userPhoneNumberTextView.setText(userPhoneNumber);
        personNumberTextView.setText(Integer.toString(person));
        todayTextView.setText(today);
    }

    @Override
    public void onClick(View v) {
        int getId = v.getId();
        switch (getId){
            case R.id.plus:
                waitingPresenter.getPlusPersonNum();
                break;
            case R.id.minus:
                waitingPresenter.getMinusPersonNum();
                break;
            case R.id.waitingBtn:
                waitingPresenter.issuanceWaitingTicket();
                break;
        }

    }
}
