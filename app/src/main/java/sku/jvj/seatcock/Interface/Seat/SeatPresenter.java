package sku.jvj.seatcock.Interface.Seat;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Model.StoreSeat;

/**
 * Created by Android Hong on 2016-10-29.
 */

public interface SeatPresenter {

    void getSeatData();
    void getStoreData();
    String getStoreName();

    ArrayList<StoreSeat> getSeat();
    Store getStore();
    ArrayList<StoreSeat> getCheckSeat();

    boolean isCheckSeat();
    boolean isReservation();

    void onSuccess();
    void failOfTimeOut();



}
