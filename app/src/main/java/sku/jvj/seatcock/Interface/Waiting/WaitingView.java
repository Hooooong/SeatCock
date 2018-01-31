package sku.jvj.seatcock.Interface.Waiting;

/**
 * Created by Android Hong on 2016-10-31.
 */

public interface WaitingView {

    void showProgress();
    void hideProgress();

    void showToast(String message);
    void showToastEnd(String message);

    void displayPersonNum(int num);

    void displayWaitingTicket(String waitingNumber,String waitingPerson, String waitingTime, String userName, String userPhoneNumber, int person, String today);
}
