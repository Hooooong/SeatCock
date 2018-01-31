package sku.jvj.seatcock.Interactor;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import sku.jvj.seatcock.Interface.MyReservation.MyReservationInteractor;
import sku.jvj.seatcock.Interface.MyReservation.MyReservationPresenter;
import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.StoreReservation;
import sku.jvj.seatcock.Server.MyReservationData;

/**
 * Created by Android Hong on 2016-10-27.
 */

public class MyReservationInteractorImpl implements MyReservationInteractor {

    private MyReservationHandler myReservationHandler;
    private MyReservationData myReservationData;
    private KakaoUser kakaoUser = KakaoUser.getInstance();
    private MyReservationPresenter myReservationPresenter;
    private String reservationType;

    public MyReservationInteractorImpl(MyReservationPresenter myReservationPresenter) {
        this.myReservationPresenter = myReservationPresenter;
        myReservationHandler = new MyReservationHandler();
        myReservationData = new MyReservationData(myReservationHandler);
    }

    @Override
    public void getReservationData(String reservationType) {
        this.reservationType = reservationType;
        myReservationData.getData(kakaoUser.getId());
    }

    @Override
    public ArrayList<StoreReservation> setReservationData() {
        return myReservationData.setData(reservationType);
    }

    @Override
    public KakaoUser getKakaoUser() {
        return kakaoUser;
    }

    @Override
    public void changeReservationData(String date, String time, String storeId, String ZNchk) {
        myReservationData.changeReservationData(kakaoUser.getId(),date,time,storeId,ZNchk);
    }

    /**
     * 핸들러 클래스
     */
    public class MyReservationHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RESERVATION_NETWORK_SUCCESS:
                    myReservationPresenter.onSuccess("reservationData");
                    break;
                case RESERVATION_NETWORK_FAILER:
                    myReservationPresenter.failOfTimeOut("reservationData");
                    break;
                case CHANGE_NETWORK_SUCCESS:
                    myReservationPresenter.onSuccess("changeData");
                    getReservationData("nowReservation");
                    break;
                case CHANGE_NETWORK_FAILER:
                    myReservationPresenter.failOfTimeOut("changeData");
                    break;
            }
        }
    }
}
