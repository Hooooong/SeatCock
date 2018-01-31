package sku.jvj.seatcock.Interface.MyReservation;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.StoreReservation;

/**
 * Created by Android Hong on 2016-10-27.
 */

public interface MyReservationView {
    void showProgress();
    void hideProgress();

    void showToast(String message);

    void showChangeDialog(String date, String time, String storeId, String ZNchk);

    void displayMyReview( ArrayList<StoreReservation> storeReservationArrayList);
}
