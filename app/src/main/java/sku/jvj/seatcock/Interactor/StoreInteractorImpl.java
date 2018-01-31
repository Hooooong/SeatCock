package sku.jvj.seatcock.Interactor;


import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import sku.jvj.seatcock.Activity.StoreActivity;
import sku.jvj.seatcock.Interface.Store.StoreInterator;
import sku.jvj.seatcock.Interface.Store.StorePresenter;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Server.StorePictureData;

/**
 * Created by Android Hong on 2016-10-22.
 */
public class StoreInteractorImpl implements StoreInterator {

    private StorePictureHandler storePictureHandler;
    private StorePresenter storePresenter;
    private StorePictureData storePictureData;
    private Store store;

    public StoreInteractorImpl(StorePresenter storePresenter) {
        this.storePresenter = storePresenter;
        store  = (Store) StoreActivity.mActivity.getIntent().getSerializableExtra("store");
        storePictureHandler = new StorePictureHandler();
    }

    @Override
    public String getStoreName() {
        return store.getStoreName();
    }

    @Override
    public ArrayList<String> getStorePicture() {
        return storePictureData.setData();
    }

    @Override
    public void getStorePictureData() {
        storePictureData = new StorePictureData(storePictureHandler);
        storePictureData.getData(store.getStoreId());
    }

    @Override
    public Store getStore() {
        return store;
    }

    @Override
    public String getStoreId() {
        return store.getStoreId();
    }

    @Override
    public double getStoreX() {
        return Double.parseDouble(store.getX());
    }

    @Override
    public double getStoreY() {
        return Double.parseDouble(store.getY());
    }

    /**
     * 핸들러 클래스
     */
    public class StorePictureHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PIC_NETWORK_SUCCESS:
                    storePresenter.onSuccess(storePictureData.setData());
                    break;
                case PIC_NETWORK_FAILER:
                    storePresenter.failOfTimeOut();
                    break;
            }
        }
    }
}
