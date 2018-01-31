package sku.jvj.seatcock.Interface.MyReservation;

import sku.jvj.seatcock.Model.KakaoUser;

/**
 * Created by Android Hong on 2016-10-27.
 */

public interface MyReservationPresenter {

    void showChangeDialog(String date, String time, String storeId, String ZNchk);

    void getReservationData(String reservationType);
    void changeReservation(String date, String time, String storeId, String ZNchk);

    void onSuccess(String reservationStatus);
    void failOfTimeOut(String reservationStatus);

    KakaoUser getKakaoUser();
}
