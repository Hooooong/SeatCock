package sku.jvj.seatcock.Presenter;

import sku.jvj.seatcock.Activity.WaitingTicketActivity;
import sku.jvj.seatcock.Interactor.WaitingInteractorImpl;
import sku.jvj.seatcock.Interface.Waiting.WaitingInteractor;
import sku.jvj.seatcock.Interface.Waiting.WaitingPresenter;
import sku.jvj.seatcock.Interface.Waiting.WaitingView;
import sku.jvj.seatcock.Util.Util;

/**
 * Created by Android Hong on 2016-10-31.
 */
public class WaitingPresenterImpl implements WaitingPresenter {


    private WaitingView view;
    private WaitingInteractor waitingInteractor;

    public WaitingPresenterImpl(WaitingView view) {
        this.view = view;
        waitingInteractor = new WaitingInteractorImpl(this);
        this.getWaitingData();
    }

    @Override
    public void getWaitingData() {
        if (view != null) {
            view.showProgress();
        }
        waitingInteractor.getWaitingData();
    }

    @Override
    public void getPlusPersonNum() {
        if ((waitingInteractor.getPersonNum() + 1) == 9) {
            view.showToast("대기번호는 최대 8명입니다.");
        } else {
            view.displayPersonNum(waitingInteractor.setPersonNum("plus"));
        }
    }

    @Override
    public void getMinusPersonNum() {
        if ((waitingInteractor.getPersonNum() - 1) == 0) {
            view.showToast("최소인원수 1명 입니다.");
        } else {
            view.displayPersonNum(waitingInteractor.setPersonNum("minus"));
        }
    }

    @Override
    public void issuanceWaitingTicket() {
        if (view != null){
            view.showProgress();
        }
        waitingInteractor.issuanceWaitingTicket();
    }

    @Override
    public void onSuccess(String type) {
        if (view != null) {
            view.hideProgress();
            if (type.equals("waitingData")) {
                waitingInteractor.setWaitingData();
                view.displayWaitingTicket(waitingInteractor.getWaitingNumber(), waitingInteractor.getWaitingPerson(), waitingInteractor.getWaitingTime(), waitingInteractor.userName(), waitingInteractor.userPhoneNumber(), 1, waitingInteractor.getToday());
            }else{
                Util.waitingPush(WaitingTicketActivity.mContext,waitingInteractor.getStoreName(),waitingInteractor.getWaitingNumber(),waitingInteractor.getWaitingTime(), String.valueOf(waitingInteractor.getPersonNum()));
                view.showToastEnd("대기번호 발급이 완료되었습니다.");
            }
        }
    }

    @Override
    public void failOfTimeOut(String type) {
        if (view != null) {
            view.hideProgress();
            if (type.trim().equalsIgnoreCase("waitingexist")) {
                view.showToastEnd("이미 회원님은 발급된 대기번호가 있습니다.");
            }else if(type.trim().equalsIgnoreCase("waitingDataFailer")) {
                view.showToastEnd("데이터 통신이 불안정합니다.");
            }else{
                view.showToast("대기번호 발급이 실패하였습니다.");
            }
        }
    }
}
