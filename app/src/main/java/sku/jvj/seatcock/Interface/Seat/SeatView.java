package sku.jvj.seatcock.Interface.Seat;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Model.StoreSeat;

/**
 * Created by Android Hong on 2016-10-29.
 */

public interface SeatView {
    int REQUEST_CHECK_RESERVATION = 5;
    int REQUEST_CHECK_WAITING = 10;
    int RESERVATION_OK = 20;
    int WATINGTICKET_OK = 30;
    int RESERVATION_CANCELED = 25;
    int WATINGTICKET_CANCELED = 35;

    void showProgress();
    void hideProgress();

    void showToast(String message);

    void displayStore(Store store);
    void displaySeat(ArrayList<StoreSeat> storeSeatArrayList,boolean waitingCheck);



}
