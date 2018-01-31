package sku.jvj.seatcock.Interface.SignUp;

/**
 * Created by Android Hong on 2016-10-26.
 */

public interface SignUpInteractor {
    int USER_NETWORK_SUCCESS = 1;
    int USER_NETWORK_FAILER = 2;

    void signUpUser(String name,String jubun,String phoneNumber);

}
