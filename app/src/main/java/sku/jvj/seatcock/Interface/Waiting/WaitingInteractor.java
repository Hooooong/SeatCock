package sku.jvj.seatcock.Interface.Waiting;

/**
 * Created by Android Hong on 2016-10-31.
 */

public interface WaitingInteractor {
    int WAIT_NETWORK_SUCCESS = 1;
    int WAIT_NETWORK_FAILER = 2;
    int WAIT_NETWORK_ISSUANCE = 3;
    int WAIT_NETWORK_NOISSUANCE = 4;
    int WAIT_NETWORK_EXIST = 5;


    void getWaitingData();
    void setWaitingData();

    void issuanceWaitingTicket();

    int getPersonNum();
    String getWaitingNumber();
    String getWaitingTime();
    String getWaitingPerson();
    String getStoreName();
    String userName();
    String userPhoneNumber();

    int setPersonNum(String type);

    String getToday();



}
