package sku.jvj.seatcock.Interface.SearchResult;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.Store;

/**
 * Created by Android Hong on 2016-10-28.
 */

public interface SearchResultView {
    void showProgress();
    void hideProgress();

    void showSortDialog();

    void navigateToSearch();
    void navigateToSeat(Store store);
    void navigateToStore(Store store);

    void displayStore(ArrayList<Store> storeArrayList,String searchWord,String sort);
}
