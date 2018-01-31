package sku.jvj.seatcock.Interface.Bill;

/**
 * Created by Android Hong on 2016-10-31.
 */

public interface BillView {
    void showProgress();
    void hideProgress();

    void displayBill(String storeName, String storeAddress, String checkSeatNum, String userName, String userPhoneNumber, String reservationDate, String reservationTime, String reservationPerson);
}
