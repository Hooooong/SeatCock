package sku.jvj.seatcock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.auth.Session;

import java.util.ArrayList;

import sku.jvj.seatcock.Interface.Seat.SeatPresenter;
import sku.jvj.seatcock.Interface.Seat.SeatView;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Model.StoreSeat;
import sku.jvj.seatcock.Presenter.SeatPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

public class SeatActivity extends AppCompatActivity implements SeatView,View.OnTouchListener,View.OnClickListener {


    public static Context mContext;
    public static Activity mActivity;

    //스크롤뷰 전역변수 설정
    private static HorizontalScrollView Scroll_Horizontal;
    private static ScrollView Scroll_Vertical;
    private static int currentX = 0;
    private static int currentY = 0;

    private TextView seatUseCountTextView,seatTotalCountTextView;
    private LinearLayout ticketLayout,normalReservationLayout,zoneReservationLayout,noReservationLayout;
    private ImageView ticketImageView;
    private SeatPresenter seatPresenter;
    private Toolbar toolbar;
    private ProgressDialog progressDiaolg;
    private RelativeLayout seatRelativeLayout;
    private TextView seatView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        mContext = this;
        mActivity = this;

        seatPresenter = new SeatPresenterImpl(this);
        initLayout();

    }

    private void initLayout() {
        /**
         seatUseCountTextView - 사용중인 좌석
         seatTotalCountTextView - 전체 좌석
         ticketLayout - 대기번호 받기 활성화

         normalReservationLayout - 일반 예약하기
         zoneReservationLayout - 좌석 예약하기
         */

        toolbar = (Toolbar) findViewById(R.id.seatToolbar);
        toolbar.setTitle(seatPresenter.getStoreName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Scroll_Vertical = (ScrollView)findViewById(R.id.verticalScroll);
        Scroll_Vertical.setOnTouchListener(this);
        Scroll_Horizontal = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        Scroll_Horizontal.setOnTouchListener(this);

        seatUseCountTextView = (TextView)findViewById(R.id.seatUseCountTextView);
        seatTotalCountTextView = (TextView)findViewById(R.id.seatTotalCountTextView);

        ticketLayout = (LinearLayout)findViewById(R.id.ticketLayout);
        ticketImageView = (ImageView)findViewById(R.id.ticektImageView);
        seatRelativeLayout = (RelativeLayout)findViewById(R.id.seatRelativeLayout);
        normalReservationLayout = (LinearLayout)findViewById(R.id.normalReservationLayout);
        zoneReservationLayout = (LinearLayout)findViewById(R.id.zoneReservationLayout);
        noReservationLayout = (LinearLayout)findViewById(R.id.noReservationLayout);

        ticketLayout.setOnClickListener(this);
        normalReservationLayout.setOnClickListener(this);
        zoneReservationLayout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        seatPresenter.getSeatData();
        seatPresenter.getStoreData();
    }

    @Override
    public void showProgress() {
        progressDiaolg = Util.createProgressDialog(this);
    }

    @Override
    public void hideProgress() {
        progressDiaolg.dismiss();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayStore(Store store) {
        seatTotalCountTextView.setText(String.valueOf("/ "+ store.getSeatTotalCountSituation()));
        if(store.isNormalReservation() && store.isZoneReservation()){
            // 예약 가능
            normalReservationLayout.setVisibility(View.VISIBLE);
            zoneReservationLayout.setVisibility(View.GONE);
            noReservationLayout.setVisibility(View.GONE);
        }else if( store.isZoneReservation() && !store.isNormalReservation()){
            //좌석 예약만 되는 경우
            normalReservationLayout.setVisibility(View.GONE);
            zoneReservationLayout.setVisibility(View.VISIBLE);
            noReservationLayout.setVisibility(View.GONE);
        }else if( store.isNormalReservation() && !store.isZoneReservation()){
            //일반 예약만 되는 경우
            normalReservationLayout.setVisibility(View.VISIBLE);
            zoneReservationLayout.setVisibility(View.GONE);
            noReservationLayout.setVisibility(View.GONE);
        }else if( !store.isNormalReservation() && !store.isZoneReservation()){
            normalReservationLayout.setVisibility(View.GONE);
            zoneReservationLayout.setVisibility(View.GONE);
            noReservationLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displaySeat(ArrayList<StoreSeat> storeSeatArrayList, boolean waitingCheck) {
        // 사용중인 갯수 / 전체 갯수
        int useCount = 0 ;
        seatRelativeLayout.removeAllViews();
        for(int i = 0 ; i<storeSeatArrayList.size(); i++){
            if(storeSeatArrayList.get(i).isStatus()){
                useCount++;
            }
        }
        seatUseCountTextView.setText(String.valueOf(useCount));

        if(waitingCheck) {
            ticketLayout.setBackgroundResource(R.drawable.whitecircle_bg);
            ticketImageView.setImageResource(R.drawable.ic_waitnumber_color);
            ticketLayout.setClickable(true);
        }else{
            ticketLayout.setBackgroundResource(R.drawable.grarycircle_bg);
            ticketImageView.setImageResource(R.drawable.ic_waitnumbutton_gray);
            ticketLayout.setClickable(false);
        }

        /*seatRelativeLayout.removeAllViews();*/
        for (int i = 0; i < storeSeatArrayList.size(); i++) {
            seatView = new TextView(this);
            seatView.setId(R.id.seatView);
            seatView.setTag(i);
            seatView.setText(" 좌석 : " + storeSeatArrayList.get(i).getNum());
            seatView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

            RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(290, 290);
            textViewParams.setMargins(storeSeatArrayList.get(i).getX() + 15, storeSeatArrayList.get(i).getY() + 15, 15, 15);
            seatView.setLayoutParams(textViewParams);
            seatView.setElevation(5.0F);

            if (storeSeatArrayList.get(i).isSeatZoneCheck() && storeSeatArrayList.get(i).isStatus()) {
                //존예약이지만 사용중일때 (status = true)
                seatView.setBackgroundResource(R.drawable.reservation_use_bg);

            } else if (storeSeatArrayList.get(i).isSeatZoneCheck() && !storeSeatArrayList.get(i).isStatus()) {
                // 존예약이지만 미사용중일때(status = false)
                seatView.setBackgroundResource(R.drawable.reservation_bg);

            } else if (!storeSeatArrayList.get(i).isSeatZoneCheck() && storeSeatArrayList.get(i).isStatus()) {
                // 일반예약이지만 사용중일때(status = true)
                seatView.setBackgroundResource(R.drawable.use_bg);

            } else {
                seatView.setBackgroundColor(Color.parseColor("#94B4A793"));
            }
            seatView.setOnClickListener(this);
            seatRelativeLayout.addView(seatView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //메뉴 추가
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            seatPresenter.getSeatData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public static void scrollBy(int x, int y) {
        Scroll_Horizontal.scrollBy(x, 0);
        Scroll_Vertical.scrollBy(0, y);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentX = (int) event.getRawX();
                currentY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int x2 = (int) event.getRawX();
                int y2 = (int) event.getRawY();
                scrollBy(currentX - x2, currentY - y2);
                currentX = x2;
                currentY = y2;
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                currentX = (int) event.getRawX();
                currentY = (int) event.getRawY();
                break;
        }
        currentX = (int) event.getRawX();
        currentY = (int) event.getRawY();
        return true;
    }

    @Override
    public void onClick(View v) {
        int getId = v.getId();
        switch (getId){
            case R.id.normalReservationLayout:
                Log.e("normalReservationLayout","normalReservation");
                if(!Session.getCurrentSession().isOpened() ){
                    this.showToast("예약하시려면 로그인이 필요합니다.");
                }else{
                    //일반예약
                    Intent normalReservationIntent = new Intent(getApplicationContext(), ReservationActivity.class);
                    normalReservationIntent.putExtra("store", seatPresenter.getStore());
                    startActivityForResult(normalReservationIntent,REQUEST_CHECK_RESERVATION);
                }
                break;
            case R.id.zoneReservationLayout:
                Log.e("zoneReservationLayout","zoneReservation");
                if(!Session.getCurrentSession().isOpened() ){
                    this.showToast("예약하시려면 로그인이 필요합니다.");
                }//좌석예약만 되는
                else if(seatPresenter.isCheckSeat()) {
                    //좌석예약
                    Intent zoneReservationIntent = new Intent(getApplicationContext(), ReservationActivity.class);
                    zoneReservationIntent.putExtra("store", seatPresenter.getStore());
                    zoneReservationIntent.putExtra("checkSeat", seatPresenter.getCheckSeat());
                    startActivityForResult(zoneReservationIntent,REQUEST_CHECK_RESERVATION);
                }else{
                    this.showToast("좌석을 선택해 주세요.");
                }
                break;

            case R.id.ticketLayout:
                if(!Session.getCurrentSession().isOpened() ){
                    this.showToast("대기번호를 발급받으시려면 로그인이 필요합니다.");
                }else{
                    Intent waitingIntent = new Intent(getApplicationContext(), WaitingTicketActivity.class);
                    waitingIntent.putExtra("store", seatPresenter.getStore());
                    startActivityForResult(waitingIntent,REQUEST_CHECK_WAITING);
                }
                break;
            case R.id.seatView:
                int number = (int)v.getTag();
                //좌석 눌렀을 경우
                if (seatPresenter.getSeat().get(number).isSeatZoneCheck()) {
                    if (!seatPresenter.getSeat().get(number).isCheckStatus()) {
                        v.setBackgroundResource(R.drawable.reservation_check_bg);
                        seatPresenter.getSeat().get(number).setCheckStatus(true);
                    } else {
                        if (seatPresenter.getSeat().get(number).isSeatZoneCheck() && seatPresenter.getSeat().get(number).isStatus()) {
                            //존예약이지만 사용중일때 (status = true)
                            v.setBackgroundResource(R.drawable.reservation_use_bg);
                        } else if (seatPresenter.getSeat().get(number).isSeatZoneCheck() && !seatPresenter.getSeat().get(number).isStatus()) {
                            // 존예약이지만 미사용중일때(status = false)
                            v.setBackgroundResource(R.drawable.reservation_bg);
                        }
                        seatPresenter.getSeat().get(number).setCheckStatus(false);
                    }

                    if(seatPresenter.isCheckSeat()){
                        normalReservationLayout.setVisibility(View.GONE);
                        zoneReservationLayout.setVisibility(View.VISIBLE);
                        noReservationLayout.setVisibility(View.GONE);
                    }else{
                        if(!seatPresenter.isReservation()){
                            //좌석예약만 가능한 곳
                            normalReservationLayout.setVisibility(View.GONE);
                            zoneReservationLayout.setVisibility(View.VISIBLE);
                            noReservationLayout.setVisibility(View.GONE);
                        }else{
                            //일반 예약만 가능한 곳
                            normalReservationLayout.setVisibility(View.VISIBLE);
                            zoneReservationLayout.setVisibility(View.GONE);
                            noReservationLayout.setVisibility(View.GONE);
                        }
                    }
                }else{
                    this.showToast("예약 불가능한 좌석입니다.");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            // 예약 성공했을 때
            case RESERVATION_OK:
                Intent myReservationIntent = new Intent(getApplicationContext(), MyReservationActivity.class);
                startActivity(myReservationIntent);
                finish();
                break;
            case WATINGTICKET_OK:
                break;
            case RESERVATION_CANCELED:
                this.showToast("예약이 취소되었습니다.");
                break;
            case WATINGTICKET_CANCELED:
                this.showToast("대기번호 발급이 취소되었습니다.");
                break;
            case RESULT_CANCELED:

                break;
        }
    }

}
