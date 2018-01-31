package sku.jvj.seatcock.Presenter;

import com.kakao.auth.Session;

import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.StoreInteractorImpl;
import sku.jvj.seatcock.Interface.Store.StoreInterator;
import sku.jvj.seatcock.Interface.Store.StorePresenter;
import sku.jvj.seatcock.Interface.Store.StoreView;

/**
 * Created by Android Hong on 2016-10-22.
 */
public class StorePresenterImpl implements StorePresenter {

    private StoreInterator storeInterator;
    private StoreView view;

    public StorePresenterImpl(StoreView view){
        this.view = view;

        storeInterator = new StoreInteractorImpl(this);
       this.getStorePictureData();

    }

    @Override
    public void getStorePictureData() {
        if(view != null){
            view.showProgress();
        }
        storeInterator.getStorePictureData();
    }

    @Override
    public String getStoreName() {
        return storeInterator.getStoreName();
    }

    @Override
    public String getStoreId() {
        return storeInterator.getStoreId();
    }

    @Override
    public void navigateToStorePicture(int position) {
        if(view != null){
            view.navigateToPicture(storeInterator.getStorePicture(), position);
        }
    }

    @Override
    public void navigateToStoreMap() {
        view.navigateToMap(storeInterator.getStoreX(),storeInterator.getStoreY());
    }

    @Override
    public void navigateToSeat() {
        view.navigateToSeat(storeInterator.getStore());
}

    @Override
    public void navigateToReviewWrite() {
        if (Session.getCurrentSession().isOpened()) {
            view.navigateToReviewWrite(storeInterator.getStoreId());
        }else{
            view.showToast("로그인을 먼저 해주세요");
        }


    }

    @Override
    public void onSuccess(ArrayList arrayList) {
        if(view != null){
            view.hideProgress();
            view.displayStorePictre(arrayList);
        }

    }

    @Override
    public void failOfTimeOut() {
        if(view != null){
            view.hideProgress();
        }
    }
}
