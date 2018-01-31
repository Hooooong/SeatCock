package sku.jvj.seatcock.Presenter;

import android.util.Log;

import sku.jvj.seatcock.Activity.BillActivity;
import sku.jvj.seatcock.Interactor.BillInteractorImpl;
import sku.jvj.seatcock.Interface.Bill.BillInteractor;
import sku.jvj.seatcock.Interface.Bill.BillPresenter;
import sku.jvj.seatcock.Interface.Bill.BillView;
import sku.jvj.seatcock.Util.Util;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Android Hong on 2016-10-31.
 */

public class BillPresenterImpl implements BillPresenter {

    private BillView view;
    private BillInteractor billInteractor;


    public BillPresenterImpl(BillView view) {
        this.view = view;
        billInteractor = new BillInteractorImpl(this);
        this.getBillData();
    }

    @Override
    public void getBillData() {
        view.displayBill(billInteractor.getStoreName(),billInteractor.getStoreAddress(),billInteractor.getCheckSeatNum(), billInteractor.userName(), billInteractor.userPhoneNumber(), billInteractor.reservationDate(), billInteractor.reservationTime(), billInteractor.reservationPerson());
    }

    @Override
    public void startReservation() {
        if(view != null){
            view.showProgress();
        }
        billInteractor.startReservation();
    }

    @Override
    public void onSuccess(String type) {
        if(view != null){
            view.hideProgress();
            if(type.equals("ZONE")){
                //좌석예약
                Log.e("좌석예약","좌석예약");
                Util.reservationPush(BillActivity.mContext,billInteractor.getStoreName(),billInteractor.getSxtoreId(),billInteractor.reservationDate(),billInteractor.reservationTime(),billInteractor.reservationPerson()+" 명",billInteractor.getCheckSeatNum());
                BillActivity.mActivity.setResult(RESULT_OK);
                BillActivity.mActivity.finish();
            }else{
                //일반예약
                Log.e("일반예약","일반예약");
                Util.reservationPush(BillActivity.mContext, billInteractor.getStoreName(),billInteractor.getSxtoreId(),billInteractor.reservationDate(),billInteractor.reservationTime(),billInteractor.reservationPerson()+" 명", null);
                BillActivity.mActivity.setResult(RESULT_OK);
                BillActivity.mActivity.finish();
            }
        }
    }

    @Override
    public void failOfTimeOut() {
        if(view != null) {
            view.hideProgress();
            BillActivity.mActivity.finish();
        }

    }
}
