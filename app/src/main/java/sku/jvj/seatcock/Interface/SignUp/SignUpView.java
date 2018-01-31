package sku.jvj.seatcock.Interface.SignUp;

/**
 * Created by Android Hong on 2016-10-26.
 */

public interface SignUpView {
    void showProgress();
    void hideProgress();
    void showToast(String message);

    void setSignUp(String name,String jubun,String phoneNumber);
    void navigateToMain();


}
