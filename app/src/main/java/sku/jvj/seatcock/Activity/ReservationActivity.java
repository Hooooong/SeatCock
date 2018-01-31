package sku.jvj.seatcock.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import sku.jvj.seatcock.Interface.Reservation.ReservationPresenter;
import sku.jvj.seatcock.Interface.Reservation.ReservationView;
import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.SeatTime;
import sku.jvj.seatcock.Presenter.ReservationPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

import static sku.jvj.seatcock.Interface.Seat.SeatView.REQUEST_CHECK_RESERVATION;
import static sku.jvj.seatcock.Interface.Seat.SeatView.RESERVATION_CANCELED;
import static sku.jvj.seatcock.Interface.Seat.SeatView.RESERVATION_OK;


/**
 * Created by Android Hong on 2016-09-11.
 */
public class ReservationActivity extends AppCompatActivity implements ReservationView, View.OnClickListener {
    public static Context mContext;
    public static Activity mActivity;
    private Toolbar toolbar;

    private ReservationPresenter reservationPresenter;
    private ProgressDialog progressDialog;

    private LinearLayout reservationDatePickerLayout, reservationUseTimeLayout;
    private TextView storeNameTextView, checkSeatTextView, userNameTextView, userPhoneNumberTextView;
    private TextView personNumberTextView, plus, minus, reservationDateTextView, reservationUseTimeTextView;
    private Button reservationPayBtn;
    private RelativeLayout timeScrollLayout;
    private LinearLayout timeLayout;
    private TextView reservationTimeStartTextView;
    private TextView reservationTimeFinishTextView;
    private TextView storeTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        mContext = this;
        mActivity = this;

        toolbar = (Toolbar) findViewById(R.id.reservationToolbar);
        toolbar.setTitle("예약하기");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reservationPresenter = new ReservationPresenterImpl(this);

        initLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reset();
    }

    //초기화
    private void reset() {
        reservationPresenter.getReservationInfo();
        reservationPresenter.getPersonNum();

        reservationPresenter.setCheckDate(false);
        reservationPresenter.setCheckUseTime(false);

        reservationDateTextView.setText("날짜 선택");
        reservationUseTimeTextView.setText("시간 선택");

        reservationTimeStartTextView.setText("");
        reservationTimeFinishTextView.setText("");

        timeLayout.setVisibility(View.GONE);
    }

    private void initLayout() {
        storeNameTextView = (TextView) findViewById(R.id.storeNameTextView);
        checkSeatTextView = (TextView) findViewById(R.id.checkSeatTextView);
        userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        userPhoneNumberTextView = (TextView) findViewById(R.id.userPhoneNumberTextView);
        personNumberTextView = (TextView) findViewById(R.id.personNumberTextView);
        reservationDateTextView = (TextView) findViewById(R.id.reservationDateTextView);
        reservationDatePickerLayout = (LinearLayout) findViewById(R.id.reservationDatePickerLayout);
        reservationUseTimeTextView = (TextView) findViewById(R.id.reservationUseTimeTextView);
        reservationUseTimeLayout = (LinearLayout) findViewById(R.id.reservationUseTimeLayout);
        reservationPayBtn = (Button) findViewById(R.id.reservationPayBtn);
        minus = (TextView) findViewById(R.id.minus);
        plus = (TextView) findViewById(R.id.plus);
        timeLayout = (LinearLayout) findViewById(R.id.timeLayout);
        timeScrollLayout = (RelativeLayout) findViewById(R.id.timeScrollLayout);
        reservationTimeStartTextView = (TextView) findViewById(R.id.reservationTimeStartTextView);
        reservationTimeFinishTextView = (TextView) findViewById(R.id.reservationTimeFinishTextView);

        reservationDateTextView.setOnClickListener(this);
        reservationDatePickerLayout.setOnClickListener(this);
        reservationUseTimeTextView.setOnClickListener(this);
        reservationUseTimeLayout.setOnClickListener(this);
        reservationPayBtn.setOnClickListener(this);
        minus.setOnClickListener(this);
        plus.setOnClickListener(this);
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
    public void showDateDialog() {
        Calendar todayCurrentDate = Calendar.getInstance();
        final int todayYear = todayCurrentDate.get(Calendar.YEAR);
        final int todayMonth = todayCurrentDate.get(Calendar.MONTH);
        final int todayDay = todayCurrentDate.get(Calendar.DAY_OF_MONTH);
        todayCurrentDate.add(Calendar.DAY_OF_MONTH, 1);
        long todayTime = todayCurrentDate.getTimeInMillis();

        Calendar afterCurrentDate = Calendar.getInstance();
        afterCurrentDate.add(Calendar.MONTH, 1);
        long afterTime = afterCurrentDate.getTimeInMillis();

        DatePickerDialog mDatePicker = new DatePickerDialog(ReservationActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                String selectYear = Integer.toString(selectedYear);
                String selectMonth = Integer.toString(selectedMonth + 1);
                String selectDay = Integer.toString(selectedDay);
                if (selectMonth.length() == 1) {
                    selectMonth = "0" + selectMonth;
                }
                if (selectDay.length() == 1) {
                    selectDay = "0" + selectDay;
                }
                String day = selectYear + "-" + selectMonth + "-" + selectDay;
                reservationDateTextView.setText(selectYear + "-" + selectMonth + "-" + selectDay);
                // 날짜선택으로 정보 불러오기
                reservationPresenter.getReservationSeatTime(day);
                reservationPresenter.setCheckDate(true);

                reservationTimeStartTextView.setText("");
                reservationTimeFinishTextView.setText("");

            }

        }, todayYear, todayMonth, todayDay + 1);

        mDatePicker.getDatePicker().setMinDate(todayTime);
        mDatePicker.getDatePicker().setMaxDate(afterTime);
        mDatePicker.show();
    }

    @Override
    public void showTImeDialog() {
        final CharSequence[] items = Util.charUseTime(reservationPresenter.getStoreMaxTime());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#ffb300'>이용 시간</font>"));
        alertDialogBuilder.setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        reservationUseTimeTextView.setText(items[id]);
                        reservationPresenter.setCheckUseTime(true);
                        reservationPresenter.setCheckUseTime(id + 1, items[id]);

                        //초기화
                        for (int j = 0; j < reservationPresenter.getStoreTimeArrayList().size(); j++) {
                            if (reservationPresenter.getStoreTimeArrayList().get(j).isCheckTime()) {
                                reservationPresenter.setCheckTimeArray(j, false);
                            }
                        }
                        reservationTimeStartTextView.setText("");
                        reservationTimeFinishTextView.setText("");

                        displaySeatReservationTime(reservationPresenter.getStoreTimeArrayList());

                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void showBackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("예약");
        builder.setMessage("현재 작성중인 예약이 있습니다. \n예약 작성창을 나가시겠습니까?");
        String positiveText = "확인";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESERVATION_CANCELED);
                finish();
            }
        });
        String negativeText = "취소";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void displayPersonNum(int personNum) {
        personNumberTextView.setText(Integer.toString(personNum));
    }

    @Override
    public void displayReservationInfo(String storeNum, KakaoUser kakaoUser, String seatNum) {
        storeNameTextView.setText(storeNum);
        checkSeatTextView.setText(seatNum);
        userNameTextView.setText(kakaoUser.getName());
        userPhoneNumberTextView.setText(kakaoUser.getPhoneNumber());
    }

    @Override
    public void displaySeatReservationTime(ArrayList<SeatTime> storeTimeArrayList) {
        timeLayout.setVisibility(View.VISIBLE);
        timeScrollLayout.removeAllViews();
        int x = 0;
        // 테이블(좌석) 추가 for문
        for (int i = 0; i < storeTimeArrayList.size(); i++) {
            storeTime = new TextView(this);
            storeTime.setId(R.id.storeReservationTime);
            storeTime.setTag(i);
            storeTime.setText(storeTimeArrayList.get(i).getTime());
            RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            // Margin, Padding 정해주기
            textViewParams.setMargins(x, 35, 0, 0);
            storeTime.setPadding(50, 30, 50, 30);

            x += 300;
            storeTime.setLayoutParams(textViewParams);
            if (storeTimeArrayList.get(i).isUseTime()) {
                storeTime.setBackgroundResource(R.drawable.blackcircle_bg);
            } else if (storeTimeArrayList.get(i).isCheckTime()) {
                storeTime.setBackgroundResource(R.drawable.yellowcircle_bg);
            } else {
                storeTime.setBackgroundResource(R.drawable.whitecircle_bg);
            }
            storeTime.setOnClickListener(this);
            storeTime.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            timeScrollLayout.addView(storeTime);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        showBackDialog();
        return true;
    }

    @Override
    public void onBackPressed() {
        showBackDialog();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.plus:
                reservationPresenter.getPlusPersonNum();
                break;
            case R.id.minus:
                reservationPresenter.getMinusPersonNum();
                break;
            case R.id.reservationDateTextView:
            case R.id.reservationDatePickerLayout:
                this.showDateDialog();
                break;
            case R.id.reservationUseTimeTextView:
            case R.id.reservationUseTimeLayout:
                if (reservationPresenter.getCheckDate()) {
                    showTImeDialog();
                } else {
                    this.showToast("날짜 선택을 해주세요");
                }
                break;
            case R.id.storeReservationTime:
                if (reservationPresenter.getCheckUseTime()) {
                    // 이용시간을 선택했을 때
                    if (reservationPresenter.getStoreTimeArrayList().get((int) v.getTag()).isCheckTime()) {
                        // 누른 시간이 이미 선택한 경우
                        v.setBackgroundResource(R.drawable.whitecircle_bg);
                        reservationPresenter.getStoreTimeArrayList().get((int) v.getTag()).setCheckTime(false);
                        reservationTimeStartTextView.setText("");
                        reservationTimeFinishTextView.setText("");

                    } else if (!reservationPresenter.getStoreTimeArrayList().get((int) v.getTag()).isUseTime()) {
                        // 사용중인 시간이 아니고, 시간선택을 안한 경우
                        boolean notUse;
                        String endTime = Util.timeChoice(((TextView) v).getText().toString(), reservationPresenter.getCheckUseTimeText());

                        if (reservationPresenter.getCheckUseTimeNum() != 1) {
                            // 시작시간+1의 사용유무 와 종료시간-1의 사용유무
                            try {
                                if (reservationPresenter.getStoreTimeArrayList().get((int) v.getTag() + 1).isUseTime()) {
                                    // 시작시간 +1이 사용중이라면 무조건 true;
                                    notUse = true;
                                } else {
                                    // 시작시간 +1이 미사용중
                                    if (reservationPresenter.getStoreTimeArrayList().get((int) v.getTag() + reservationPresenter.getCheckUseTimeNum() - 1).isUseTime()) {
                                        //종료시간 - 1이 사용중이라면
                                        notUse = true;
                                    } else {
                                        notUse = false;
                                    }
                                }
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                                notUse = false;
                            }
                        } else {
                            notUse = false;
                        }

                        if (notUse) {  // 끝나는 시간에 맞춰 예약 불가능
                            this.showToast("이용시간을 다시 설정해주세요");
                        } else {  // 끝나는 시간에 맞춰 예약 예약 가능
                            // 이전에 선택한 시간이 있으면 다시 하얀색으로 바뀌어야 함
                            for (int i = 0; i < reservationPresenter.getStoreTimeArrayList().size(); i++) {
                                if (reservationPresenter.getStoreTimeArrayList().get(i).isCheckTime()) {
                                    reservationPresenter.setCheckTimeArray(i, false);
                                }
                            }
                            // 시간 선택
                            reservationPresenter.setCheckTimeArray((int) v.getTag(), true);
                            v.setBackgroundResource(R.drawable.yellowcircle_bg);
                            this.displaySeatReservationTime(reservationPresenter.getStoreTimeArrayList());

                            reservationTimeStartTextView.setText(((TextView) v).getText().toString() + " ~ ");
                            reservationTimeFinishTextView.setText(endTime);
                        }
                    } else {  // 사용중인 시간일 때
                        this.showToast("이미 예약되어 있는 시간입니다.");
                    }

                } else {  //이용시간을 선택하지 않았을 때
                    this.showToast("이용시간을 먼저 선택해 주세요.");
                }
                break;

            case R.id.reservationPayBtn:

                if (reservationPresenter.getCheckDate()) {
                    if (reservationPresenter.getCheckUseTime()) {
                        // 영수증 으로 넘어가기

                        Intent billIntent = new Intent(this, BillActivity.class);
                        billIntent.putExtra("store", reservationPresenter.getStore());
                        billIntent.putExtra("person", personNumberTextView.getText().toString());
                        billIntent.putExtra("checkNum", reservationPresenter.getSeatNum());
                        billIntent.putExtra("day", reservationDateTextView.getText().toString());
                        billIntent.putExtra("timeChoice", reservationTimeStartTextView.getText().toString()+reservationTimeFinishTextView.getText().toString());

                        if(reservationPresenter.getSeatNum() == ""){
                            billIntent.putExtra("ZNchk", "N");
                        }else{
                            billIntent.putExtra("ZNchk", "Z");
                        }

                        startActivityForResult(billIntent, REQUEST_CHECK_RESERVATION);
                    } else {
                        Toast.makeText(ReservationActivity.this, "시간 선택을 해주세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ReservationActivity.this, "날짜 선택을 해주세요", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    /**
     * 예약 화면 결과값
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            // 예약 성공했을 때
            case RESULT_OK:
                this.showToast( "예약이 완료되었습니다\n[예약 정보]에서 확인해주시기 바랍니다");
                setResult(RESERVATION_OK);
                finish();
                break;
            case RESULT_CANCELED:
                reservationPresenter.getReservationInfo();
                reservationPresenter.getPersonNum();
                reservationTimeStartTextView.setText("");
                reservationTimeFinishTextView.setText("");
                //초기화
                for (int j = 0; j < reservationPresenter.getStoreTimeArrayList().size(); j++) {
                    if (reservationPresenter.getStoreTimeArrayList().get(j).isCheckTime()) {
                        reservationPresenter.setCheckTimeArray(j, false);
                    }
                }
                this.displaySeatReservationTime(reservationPresenter.getStoreTimeArrayList());
                break;
        }
    }


}
