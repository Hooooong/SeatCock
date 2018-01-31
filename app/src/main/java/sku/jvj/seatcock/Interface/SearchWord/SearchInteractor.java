package sku.jvj.seatcock.Interface.SearchWord;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.SearchWord;

/**
 * Created by Android Hong on 2016-10-28.
 */

public interface SearchInteractor {
    int RANK_NETWORK_SUCCESS = 1;
    int RANK_NETWORK_FAILER = 2;

    ArrayList<SearchWord> setSearchWordData();
    void getSearchRankData();
    ArrayList<SearchWord> setSearchRankData();
    void insertSearchWord(String searchWord);
    void deleteSearchWord(String searchWord);

    double getLatitude();
    double getLongitude();

}
