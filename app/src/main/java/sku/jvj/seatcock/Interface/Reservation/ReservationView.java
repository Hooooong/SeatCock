package sku.jvj.seatcock.Interface.Reservation;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.SeatTime;

/**
 * Created by Android Hong on 2016-10-30.
 */

public interface ReservationView {

    void showProgress();
    void hideProgress();
    void showToast(String message);

    void showDateDialog();
    void showTImeDialog();
    void showBackDialog();

    void displayPersonNum(int personNum);
    void displayReservationInfo(String storeNum, KakaoUser kakaoUser, String seatNum);
    void displaySeatReservationTime(ArrayList<SeatTime> storeTimeArrayList);

}
