package sku.jvj.seatcock.Interactor;

import android.os.Handler;
import android.os.Message;

import sku.jvj.seatcock.Activity.BillActivity;
import sku.jvj.seatcock.Interface.Bill.BillInteractor;
import sku.jvj.seatcock.Interface.Bill.BillPresenter;
import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Server.ReservationData;

/**
 * Created by Android Hong on 2016-10-31.
 */

public class BillInteractorImpl implements BillInteractor {

    private BillPresenter billPresenter;
    private Store store;
    private KakaoUser kakaoUser =KakaoUser.getInstance();
    private String person;
    private String checkNum;
    private String day;
    private String timeChoice;
    private String ZHchk;

    private ReservationHandler reservationHandler;


    public BillInteractorImpl(BillPresenter billPresenter) {
        this.billPresenter = billPresenter;
        reservationHandler = new ReservationHandler();
        store = (Store) BillActivity.mActivity.getIntent().getSerializableExtra("store");
        person =BillActivity.mActivity.getIntent().getStringExtra("person");
        checkNum =BillActivity.mActivity.getIntent().getStringExtra("checkNum");
        day =  BillActivity.mActivity.getIntent().getStringExtra("day");
        timeChoice = BillActivity.mActivity.getIntent().getStringExtra("timeChoice");
        ZHchk =  BillActivity.mActivity.getIntent().getStringExtra("ZNchk");
    }

    @Override
    public String getStoreName() {
        return store.getStoreName();
    }

    @Override
    public String getStoreAddress() {
        return store.getStoreAddress();
    }

    @Override
    public String getSxtoreId() {
        return store.getStoreId();
    }

    @Override
    public String getCheckSeatNum() {
        String SeatNum;
        if("Z".equals(ZHchk)){
            SeatNum = checkNum;
        }else{
            SeatNum = null;
        }
        return SeatNum;
    }

    @Override
    public String userName() {
        return kakaoUser.getName();
    }

    @Override
    public String userPhoneNumber() {
        return kakaoUser.getPhoneNumber();
    }

    @Override
    public String reservationDate() {
        return day;
    }

    @Override
    public String reservationTime() {
        return timeChoice;
    }

    @Override
    public String reservationPerson() {
        return person;
    }

    @Override
    public void startReservation() {
        if("Z".equals(ZHchk)){
            //좌석예약
            ReservationData.insertToZoneReservation(store.getStoreId(), checkNum, day, timeChoice.substring(0,5)+":00", kakaoUser.getId(), person,timeChoice.substring(8,13)+":00",reservationHandler);

        }else{
            //일반예약
            ReservationData.insertToNormalReservation(store.getStoreId(), day, timeChoice.substring(0,5)+":00", kakaoUser.getId(), person,timeChoice.substring(8,13)+":00",reservationHandler);

        }
    }

    /**
     * 핸들러 클래스
     */
    public class ReservationHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RESERVATION_ZONE_SUCCESS:
                    /*좌석 예약 성공했을 경우*/
                    billPresenter.onSuccess("ZONE");
                    break;

                case RESERVATION_NORMAL_SUCCESS:
                     /*일반 예약 성공했을 경우*/
                    billPresenter.onSuccess("NORMAL");
                    break;
                case RESERVATION_FAILURE:
                    billPresenter.failOfTimeOut();
                    break;
                default:
                    billPresenter.failOfTimeOut();
                    break;
            }
        }
    }
}
