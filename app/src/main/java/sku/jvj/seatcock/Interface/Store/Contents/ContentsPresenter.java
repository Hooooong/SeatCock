package sku.jvj.seatcock.Interface.Store.Contents;

import sku.jvj.seatcock.Model.Store;

/**
 * Created by Android Hong on 2016-10-24.
 */

public interface ContentsPresenter {

    void getContentsData(String storeId);

    void onSuccess(Store store);
    void failOfTimeOut();
}
