package sku.jvj.seatcock.Interface.Store.Contents;

/**
 * Created by Android Hong on 2016-10-24.
 */

public interface ContentsInteractor {
    int CON_NETWORK_SUCCESS = 1;
    int CON_NETWORK_FAILER = 2;

    void getContentsData(String storeId);

}
