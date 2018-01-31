package sku.jvj.seatcock.Interface.SearchResult;

/**
 * Created by Android Hong on 2016-10-28.
 */

public interface SearchResultInteractor {
    int SR_NETWORK_SUCCESS = 1;
    int SR_NETWORK_FAILER = 2;


    void getStoreData();

    void setStoreData();

    void getSortData(int num);

    String getSearchWord();

    double getLatitude();
    double getLongitude();
}
