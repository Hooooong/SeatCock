package sku.jvj.seatcock.Presenter;

import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.SeatInteractorImpl;
import sku.jvj.seatcock.Interface.Seat.SeatInteractor;
import sku.jvj.seatcock.Interface.Seat.SeatPresenter;
import sku.jvj.seatcock.Interface.Seat.SeatView;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Model.StoreSeat;

/**
 * Created by Android Hong on 2016-10-29.
 */

public class SeatPresenterImpl implements SeatPresenter {

    private SeatView view;
    private SeatInteractor seatInteractor;

    public SeatPresenterImpl(SeatView view) {
        this.view = view;
        seatInteractor = new SeatInteractorImpl(this);
    }

    @Override
    public void getSeatData() {
        if(view != null){
            view.showProgress();
        }
        seatInteractor.getSeatData();

    }

    @Override
    public void getStoreData() {
        view.displayStore(seatInteractor.getStore());
    }

    @Override
    public String getStoreName() {
        return seatInteractor.getStoreName();
    }

    @Override
    public ArrayList<StoreSeat> getSeat() {
        return seatInteractor.getSeat();
    }

    @Override
    public Store getStore() {
        return seatInteractor.getStore();
    }

    @Override
    public ArrayList<StoreSeat> getCheckSeat() {
        return seatInteractor.getCheckSeat();
    }

    @Override
    public boolean isCheckSeat() {
        return seatInteractor.isCheckSeat();
    }

    @Override
    public boolean isReservation() {
        return seatInteractor.isReservation();
    }


    @Override
    public void onSuccess() {
        if(view != null){
            view.displaySeat(seatInteractor.setSeatData(),seatInteractor.getWaitingCheck());
            view.hideProgress();
        }

    }

    @Override
    public void failOfTimeOut() {
        if(view != null){
            view.hideProgress();
        }
    }
}
