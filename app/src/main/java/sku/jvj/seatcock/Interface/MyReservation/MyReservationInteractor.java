package sku.jvj.seatcock.Interface.MyReservation;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.StoreReservation;

/**
 * Created by Android Hong on 2016-10-27.
 */

public interface MyReservationInteractor {
    int RESERVATION_NETWORK_SUCCESS = 1;
    int RESERVATION_NETWORK_FAILER = 2;

    int CHANGE_NETWORK_SUCCESS = 3;
    int CHANGE_NETWORK_FAILER = 4;

    KakaoUser getKakaoUser();

    void getReservationData(String reservationType);
    ArrayList<StoreReservation> setReservationData();

    void changeReservationData(String date, String time, String storeId, String ZNchk);
}
