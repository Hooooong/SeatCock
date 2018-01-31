package sku.jvj.seatcock.Interface.Reservation;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.SeatTime;
import sku.jvj.seatcock.Model.Store;

/**
 * Created by Android Hong on 2016-10-30.
 */

public interface ReservationPresenter {

    void getReservationInfo();
    void getPersonNum();
    void getPlusPersonNum();
    void getMinusPersonNum();

    void getReservationSeatTime(String day);
    String getStoreMaxTime();
    void setCheckDate(boolean checkDate);
    boolean getCheckDate();
    void setCheckUseTime(boolean checkTime);
    boolean getCheckUseTime();
    void setCheckUseTime(int checkNum, CharSequence checkText);
    int getCheckUseTimeNum();
    String getCheckUseTimeText();
    void setCheckTimeArray(int num, boolean check);

    Store getStore();
    ArrayList<SeatTime> getStoreTimeArrayList();
    String getSeatNum();

    void onSuccess();
    void failOfTimeOut();

}
