package sku.jvj.seatcock.Interface.Store;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.Store;

/**
 * Created by Android Hong on 2016-10-22.
 */
public interface StoreInterator {
    int PIC_NETWORK_SUCCESS = 1;
    int PIC_NETWORK_FAILER = 2;

    String getStoreName();
    ArrayList<String> getStorePicture();


    void getStorePictureData();

    Store getStore();
    String getStoreId();

    double getStoreX();
    double getStoreY();



}
