package sku.jvj.seatcock.Interactor;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import sku.jvj.seatcock.Activity.ReservationActivity;
import sku.jvj.seatcock.Interface.Reservation.ReservationInteractor;
import sku.jvj.seatcock.Interface.Reservation.ReservationPresenter;
import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.SeatTime;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Model.StoreSeat;
import sku.jvj.seatcock.Server.ReservationData;
import sku.jvj.seatcock.Util.Util;

/**
 * Created by Android Hong on 2016-10-30.
 */

public class ReservationInteractorImpl implements ReservationInteractor {

    private String seatNum = "";
    private String checkNum = "";

    private int checkUseTimeNum;
    private String checkUseTimeText;

    private boolean isZone, isCheckTime, isCheckDate;
    private int personNum;

    private ArrayList<StoreSeat> checkSeat;
    private ArrayList<SeatTime> storeTimeArrayList;
    private ArrayList<String> reservationTimeArrayList;

    private ReservationPresenter reservationPresenter;
    private ReservationHandler reservationHandler;
    private Store store;
    private ReservationData reservationData;
    private KakaoUser kakaoUser = KakaoUser.getInstance();

    public ReservationInteractorImpl(ReservationPresenter reservationPresenter) {
        this.reservationPresenter = reservationPresenter;
        store = (Store) ReservationActivity.mActivity.getIntent().getSerializableExtra("store");
        checkSeat = (ArrayList<StoreSeat>) ReservationActivity.mActivity.getIntent().getSerializableExtra("checkSeat");
        reservationHandler = new ReservationHandler();
        reservationData = new ReservationData(reservationHandler);

        if (checkSeat != null && checkSeat.size() > 0) {
            isZone = true;
            seatNum = "좌석 번호 : ";
            for (int i = 0; i < checkSeat.size(); i++) {
                int num = checkSeat.get(i).getNum();
                checkNum += Integer.toString(num);
                checkNum += (i != checkSeat.size() - 1) ? "," : "";
                personNum = (checkSeat.size() - 1) * 4 + 1;
            }
            seatNum = "좌석 번호 : " + checkNum;
        } else {
            isZone = false;
            seatNum = "일반예약";
            personNum = 1;
        }
    }

    @Override
    public String getStoreName() {
        return store.getStoreName();
    }

    @Override
    public KakaoUser getKakaoUser() {
        return kakaoUser;
    }

    @Override
    public String getSeatNum() {
        return seatNum;
    }
    public String getCheckNum(){
        return checkNum;
    }

    @Override
    public boolean isZoneReservation() {
        return isZone;
    }

    @Override
    public void getReservationSeatTime(String day) {
        reservationData.getData(store.getStoreId(), day, checkNum);
    }

    @Override
    public ArrayList<SeatTime> setStoreTimeArrayList() {
        return storeTimeArrayList;
    }

    @Override
    public int getCheckSize() {
        return checkSeat.size();
    }

    @Override
    public int getPersonNum() {
        return personNum;
    }

    @Override
    public boolean isCheckUseTime() {
        return isCheckTime;
    }

    @Override
    public boolean isCheckDate() {
        return isCheckDate;
    }

    @Override
    public int setPersonNum(String type) {
        if (type.equals("plus")) {
            personNum = personNum + 1;
        } else {
            personNum = personNum - 1;
        }
        return personNum;
    }

    @Override
    public void setCheckUseTime(boolean checkTime) {
        this.isCheckTime = checkTime;
    }

    @Override
    public void setCheckDate(boolean checkDate) {
        this.isCheckDate = checkDate;
    }

    @Override
    public void setCheckTime(int checkUseTimeNum, CharSequence checkUseTimeText) {

        this.checkUseTimeNum = checkUseTimeNum;

        if (checkUseTimeText.toString().contains("시간") && !checkUseTimeText.toString().contains("분")) {
            // 시간만
            int checkHour = checkUseTimeText.toString().indexOf("시");
            this.checkUseTimeText = "0" + checkUseTimeText.toString().substring(0, checkHour) + ":00";
        } else if (checkUseTimeText.toString().contains("시간") && checkUseTimeText.toString().contains("분")) {
            //시간 과 분
            int checkHour = checkUseTimeText.toString().indexOf("시");
            this.checkUseTimeText = "0" + checkUseTimeText.toString().substring(0, checkHour) + ":30";
        } else {
            this.checkUseTimeText = "00:30";
        }
    }

    @Override
    public int getCheckUseTimeNum() {
        return checkUseTimeNum;
    }

    @Override
    public String getCheckUseTimeText() {
        return checkUseTimeText;
    }

    @Override
    public void setCheckTimeArray(int num,boolean check) {
        storeTimeArrayList.get(num).setCheckTime(check);
    }

    @Override
    public String getStoreMaxTime() {
        return store.getStoreMaxTime();
    }

    @Override
    public Store getStore() {
        return store;
    }

    /**
     * 핸들러 클래스
     */
    public class ReservationHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RES_NETWORK_SUCCESS:
                    //점포 타임 사이즈 구하기
                    int timeSize = Util.timeSize(store.getStoreStartTime(), store.getStoreFinishTime());
                    storeTimeArrayList = Util.storeTime(store.getStoreStartTime(), timeSize);
                    if (isZone) {
                        reservationTimeArrayList = reservationData.setData();
                        //좌석 예약
                        for (int i = 0; i < timeSize; i++) {
                            check:
                            for (int j = 0; j < reservationTimeArrayList.size(); j++) {
                                if (reservationTimeArrayList.get(j).equals(storeTimeArrayList.get(i).getTime())) {
                                    storeTimeArrayList.get(i).setUseTime(true);
                                    break check;
                                } else {
                                    storeTimeArrayList.get(i).setUseTime(false);
                                }
                            }
                        }
                    }
                    reservationPresenter.onSuccess();
                    break;
                case RES_NETWORK_FAILER:
                    reservationPresenter.failOfTimeOut();
                    break;
            }
        }
    }

}
