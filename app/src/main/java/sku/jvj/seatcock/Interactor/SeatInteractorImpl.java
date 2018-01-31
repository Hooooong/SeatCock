package sku.jvj.seatcock.Interactor;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import sku.jvj.seatcock.Activity.SeatActivity;
import sku.jvj.seatcock.Interface.Seat.SeatInteractor;
import sku.jvj.seatcock.Interface.Seat.SeatPresenter;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Model.StoreSeat;
import sku.jvj.seatcock.Server.SeatData;

/**
 * Created by Android Hong on 2016-10-29.
 */
public class SeatInteractorImpl implements SeatInteractor {

    private SeatPresenter seatPresenter;
    private Store store;
    private SeatHandler seatHandler;
    private SeatData seatData;
    private ArrayList<StoreSeat> storeSeatArrayList;

    public SeatInteractorImpl(SeatPresenter seatPresenter) {
        this.seatPresenter =seatPresenter;
        store = (Store)SeatActivity.mActivity.getIntent().getSerializableExtra("store");
        seatHandler = new SeatHandler();
        seatData = new SeatData(seatHandler);
    }

    @Override
    public void getSeatData() {
        seatData.getData(store.getStoreId());
    }

    @Override
    public ArrayList<StoreSeat> getSeat() {
        return storeSeatArrayList;
    }

    @Override
    public Store getStore() {
        return store;
    }

    @Override
    public ArrayList<StoreSeat> setSeatData() {
        this.storeSeatArrayList = seatData.setData();
        return storeSeatArrayList;
    }

    @Override
    public boolean isCheckSeat() {
        boolean check = false;
        for (int i = 0; i < storeSeatArrayList.size(); i++) {
            if (storeSeatArrayList.get(i).isCheckStatus()) {
                check = true;
            }
        }
        return check;
    }

    @Override
    public boolean isReservation() {
        boolean check;
        if(store.isNormalReservation()){
            check = true;
        }else{
            check = false;
        }
        return check;
    }

    @Override
    public String getStoreName() {
        return store.getStoreName();
    }

    @Override
    public ArrayList<StoreSeat> getCheckSeat() {
        ArrayList<StoreSeat> checkSeatList = new ArrayList<>();

        for(int i=0; i<storeSeatArrayList.size(); i++){
            if(storeSeatArrayList.get(i).isCheckStatus()){
                checkSeatList.add(storeSeatArrayList.get(i));
            }
        }
        return checkSeatList;
    }

    @Override
    public boolean getWaitingCheck() {
        return seatData.setWatingCheck();
    }

    /**
     * 핸들러 클래스
     */
    public class SeatHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEAT_NETWORK_SUCCESS:
                    seatPresenter.onSuccess();
                    break;
                case SEAT_NETWORK_FAILER:
                    seatPresenter.failOfTimeOut();
                    break;
            }
        }
    }


}
