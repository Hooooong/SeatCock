package sku.jvj.seatcock.Interface.Waiting;

/**
 * Created by Android Hong on 2016-10-31.
 */

public interface WaitingPresenter {


    void getWaitingData();

    void getPlusPersonNum();
    void getMinusPersonNum();

    void issuanceWaitingTicket();


    void onSuccess(String type);
    void failOfTimeOut(String type);
}
