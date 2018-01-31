package sku.jvj.seatcock.Presenter;

import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.ReservationInteractorImpl;
import sku.jvj.seatcock.Interface.Reservation.ReservationInteractor;
import sku.jvj.seatcock.Interface.Reservation.ReservationPresenter;
import sku.jvj.seatcock.Interface.Reservation.ReservationView;
import sku.jvj.seatcock.Model.SeatTime;
import sku.jvj.seatcock.Model.Store;

/**
 * Created by Android Hong on 2016-10-30.
 */

public class ReservationPresenterImpl implements ReservationPresenter {

    private ReservationView view;
    private ReservationInteractor reservationInteractor;

    public ReservationPresenterImpl(ReservationView view) {
        this.view = view;
        reservationInteractor = new ReservationInteractorImpl(this);
    }

    @Override
    public void getReservationInfo() {
        view.displayReservationInfo(reservationInteractor.getStoreName(), reservationInteractor.getKakaoUser(), reservationInteractor.getSeatNum());
    }

    @Override
    public void getPersonNum() {
        view.displayPersonNum(reservationInteractor.getPersonNum());
    }

    @Override
    public void getReservationSeatTime(String day) {
        if (view != null) {
            view.showProgress();
        }
        reservationInteractor.getReservationSeatTime(day);
    }

    @Override
    public void getPlusPersonNum() {
        if (reservationInteractor.isZoneReservation()) {
            if (reservationInteractor.getPersonNum() >= reservationInteractor.getCheckSize() * 4) {
                view.showToast("최대 인원수를 초과할 수 없습니다.");
            } else {
                view.displayPersonNum(reservationInteractor.setPersonNum("plus"));
            }
        } else {
            view.displayPersonNum(reservationInteractor.setPersonNum("plus"));
        }
    }

    @Override
    public void getMinusPersonNum() {
        if (reservationInteractor.isZoneReservation()) {
            if (reservationInteractor.getPersonNum() <= (reservationInteractor.getCheckSize() - 1) * 4 + 1) {
                view.showToast("최소인원수는 " + ((reservationInteractor.getCheckSize() - 1) * 4 + 1) + " 명 입니다.");
            } else {
                view.displayPersonNum(reservationInteractor.setPersonNum("minus"));
            }
        } else {
            if ((reservationInteractor.getPersonNum() - 1) == 0) {
                view.showToast("최소인원수 1명 입니다.");
            } else {
                view.displayPersonNum(reservationInteractor.setPersonNum("minus"));
            }
        }
    }

    @Override
    public String getStoreMaxTime() {
        return reservationInteractor.getStoreMaxTime();
    }

    @Override
    public void setCheckUseTime(boolean checkTime) {
        reservationInteractor.setCheckUseTime(checkTime);
    }

    @Override
    public void setCheckUseTime(int checkNum, CharSequence checkText) {
        reservationInteractor.setCheckTime(checkNum, checkText);
    }

    @Override
    public int getCheckUseTimeNum() {
        return reservationInteractor.getCheckUseTimeNum();
    }

    @Override
    public String getCheckUseTimeText() {
        return reservationInteractor.getCheckUseTimeText();
    }

    @Override
    public boolean getCheckDate() {
        return reservationInteractor.isCheckDate();
    }

    @Override
    public boolean getCheckUseTime() {
        return reservationInteractor.isCheckUseTime();
    }

    @Override
    public ArrayList<SeatTime> getStoreTimeArrayList() {
        return reservationInteractor.setStoreTimeArrayList();
    }

    @Override
    public String getSeatNum() {
        return reservationInteractor.getCheckNum();
}

    @Override
    public void setCheckDate(boolean checkDate) {
        reservationInteractor.setCheckDate(checkDate);
    }

    @Override
    public void setCheckTimeArray(int num,boolean check) {
        reservationInteractor.setCheckTimeArray(num,check);
    }

    @Override
    public Store getStore() {
        return reservationInteractor.getStore();
    }

    @Override
    public void onSuccess() {
        if (view != null) {
            view.hideProgress();
            view.displaySeatReservationTime(reservationInteractor.setStoreTimeArrayList());
        }
    }

    @Override
    public void failOfTimeOut() {
        if (view != null) {
            view.hideProgress();
        }
    }

}
