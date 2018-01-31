package sku.jvj.seatcock.Presenter;


import sku.jvj.seatcock.Interactor.SignUpInteractorImpl;
import sku.jvj.seatcock.Interface.SignUp.SignUpInteractor;
import sku.jvj.seatcock.Interface.SignUp.SignUpPresenter;
import sku.jvj.seatcock.Interface.SignUp.SignUpView;

/**
 * Created by Android Hong on 2016-10-26.
 */

public class SignUpPresenterImpl implements SignUpPresenter {

    private SignUpView view;
    private SignUpInteractor signUpInteractor;

    public SignUpPresenterImpl(SignUpView view) {
        this.view = view;
         signUpInteractor = new SignUpInteractorImpl(this);
    }

    @Override
    public void setSignUp(String name,String jubun,String phoneNumber) {
        if(view != null){
            view.showProgress();
        }
        signUpInteractor.signUpUser(name,jubun,phoneNumber);
    }


    @Override
    public void onSuccess() {
        if(view != null){
            view.hideProgress();
            view.showToast("회원가입이 완료되었습니다.");
            view.navigateToMain();
        }
    }

    @Override
    public void failOfTimeOut() {
        if(view != null){
            view.hideProgress();
            view.showToast("회원가입이 실패하였습니다.");
        }
    }
}
