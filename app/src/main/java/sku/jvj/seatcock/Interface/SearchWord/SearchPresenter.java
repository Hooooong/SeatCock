package sku.jvj.seatcock.Interface.SearchWord;

/**
 * Created by Android Hong on 2016-10-28.
 */

public interface SearchPresenter {

    void getSearchWordData();
    void getSearchRankData();
    void deleteSearchWord(String searchWord);

    void insertSearchWord(String searchWord);

    void navigateToSearchResult(String searchWord);

    void onSuccess();
    void failOfTimeOut();

    void showDialog(String searchWord);

    double getLatitude();
    double getLongitude();


}
