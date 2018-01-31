package sku.jvj.seatcock.Interface.SearchWord;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.SearchWord;

/**
 * Created by Android Hong on 2016-10-28.
 */

public interface SearchView {
    void showProgress();
    void hideProgress();
    void showToast(String message);

    void displaySearchWord(ArrayList<SearchWord> searchWordArrayList);
    void displaySearchRank(ArrayList<SearchWord> searchRankArrayList);

    void navigateToSearchResult(String result);

    void showDialog(String searchWord);
}
