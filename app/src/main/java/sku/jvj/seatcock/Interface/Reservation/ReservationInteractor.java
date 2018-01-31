package sku.jvj.seatcock.Interface.Reservation;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.KakaoUser;
import sku.jvj.seatcock.Model.SeatTime;
import sku.jvj.seatcock.Model.Store;

/**
 * Created by Android Hong on 2016-10-30.
 */

public interface ReservationInteractor {
    int RES_NETWORK_SUCCESS = 1;
    int RES_NETWORK_FAILER = 2;

    String getStoreName();
    KakaoUser getKakaoUser();
    String getSeatNum();
    boolean isZoneReservation();

    void getReservationSeatTime(String day);
    ArrayList<SeatTime> setStoreTimeArrayList();

    int getCheckSize();
    int getPersonNum();
    boolean isCheckUseTime();
    boolean isCheckDate();

    int setPersonNum(String type);
    void setCheckUseTime(boolean checkTime);
    void setCheckDate(boolean checkDate);

    void setCheckTime(int checkUseTimeNum, CharSequence checkUseTimeText);
    int getCheckUseTimeNum();
    String getCheckUseTimeText();
    void setCheckTimeArray(int num,boolean check);
    String getStoreMaxTime();
    String getCheckNum();
    Store getStore();


}
