package sku.jvj.seatcock.Interface.Seat;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Model.StoreSeat;

/**
 * Created by Android Hong on 2016-10-29.
 */

public interface SeatInteractor {
    int SEAT_NETWORK_SUCCESS = 1;
    int SEAT_NETWORK_FAILER = 2;

    void getSeatData();
    ArrayList<StoreSeat> getSeat();
    Store getStore();
    ArrayList<StoreSeat> setSeatData();
    String getStoreName();
    ArrayList<StoreSeat> getCheckSeat();

    boolean isCheckSeat();
    boolean isReservation();


    boolean getWaitingCheck();



}
