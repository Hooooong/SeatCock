package sku.jvj.seatcock.Interface.Bill;

/**
 * Created by Android Hong on 2016-10-31.
 */

public interface BillInteractor {
    int RESERVATION_ZONE_SUCCESS = 0;
    int RESERVATION_NORMAL_SUCCESS = 1;
    int RESERVATION_FAILURE = 3;

    String getStoreName();
    String getStoreAddress();
    String getSxtoreId();
    String getCheckSeatNum();

    String userName();
    String userPhoneNumber();
    String reservationDate();

    String reservationTime();
    String reservationPerson();

    void startReservation();


}
