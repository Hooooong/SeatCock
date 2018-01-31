package sku.jvj.seatcock.Interface.Bill;

/**
 * Created by Android Hong on 2016-10-31.
 */

public interface BillPresenter {

    void getBillData();
    void startReservation();

    void onSuccess(String type);
    void failOfTimeOut();

}
