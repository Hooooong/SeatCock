package sku.jvj.seatcock.Interface.SignUp;

/**
 * Created by Android Hong on 2016-10-26.
 */

public interface SignUpPresenter {

    void setSignUp(String name,String jubun,String phoneNumber);

    void onSuccess();
    void failOfTimeOut();

}
