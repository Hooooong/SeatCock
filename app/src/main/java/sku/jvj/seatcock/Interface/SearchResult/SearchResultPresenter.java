package sku.jvj.seatcock.Interface.SearchResult;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.Store;

/**
 * Created by Android Hong on 2016-10-28.
 */

public interface SearchResultPresenter {

    void getStoreData();
    void getSortStoreData(int num);

    void setStoreData(ArrayList<Store> storeArrayList, String sort);
    void navigateToSeat(Store store);
    void navigateToStore(Store store);

    void onSuccess();
    void failOfTimeOut();

    String getSearchWord();

    double getLatitude();
    double getLongitude();
}
