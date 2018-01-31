package sku.jvj.seatcock.Presenter;

import sku.jvj.seatcock.Interactor.MyReservationInteractorImpl;
import sku.jvj.seatcock.Interface.MyReservation.MyReservationInteractor;
import sku.jvj.seatcock.Interface.MyReservation.MyReservationPresenter;
import sku.jvj.seatcock.Interface.MyReservation.MyReservationView;
import sku.jvj.seatcock.Model.KakaoUser;

/**
 * Created by Android Hong on 2016-10-27.
 */

public class MyReservationPresenterImpl implements MyReservationPresenter {

    private MyReservationView view;
    private MyReservationInteractor myReservationInteractor;

    public MyReservationPresenterImpl(MyReservationView view){
        this.view = view;
        myReservationInteractor = new MyReservationInteractorImpl(this);
        this.getReservationData("nowReservation");
    }

    @Override
    public void showChangeDialog(String date, String time, String storeId, String ZNchk) {
        view.showChangeDialog(date,time,storeId,ZNchk);
    }

    @Override
    public void getReservationData(String reservationType) {
        if(view != null){
            view.showProgress();
        }
        myReservationInteractor.getReservationData(reservationType);
    }

    @Override
    public void changeReservation(String date, String time, String storeId, String ZNchk) {
        if(view != null){
            view.showProgress();
        }
        myReservationInteractor.changeReservationData(date,time,storeId,ZNchk);
    }

    @Override
    public KakaoUser getKakaoUser() {
        return myReservationInteractor.getKakaoUser();
    }

    @Override
    public void onSuccess(String reservationStatus) {
        if(view != null){
            view.hideProgress();
            switch (reservationStatus){
                case "reservationData" :
                    view.displayMyReview(myReservationInteractor.setReservationData());
                break;
                case "changeData":
                    view.showToast("예약정보가 변경되었습니다.\n[지난 예약]에서 확인해주세요");
                    break;
            }
        }
    }

    @Override
    public void failOfTimeOut(String reservationStatus) {
        if(view != null){
            view.hideProgress();
            switch (reservationStatus){
                case "reservationData" :
                break;
                case "changeData":
                    view.showToast("예약정보 변경이 실패하였습니다.");
                break;
            }
        }
    }
}
