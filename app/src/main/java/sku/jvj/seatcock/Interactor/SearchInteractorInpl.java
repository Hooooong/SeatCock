package sku.jvj.seatcock.Interactor;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;

import sku.jvj.seatcock.Activity.SearchActivity;
import sku.jvj.seatcock.Interface.SearchWord.SearchInteractor;
import sku.jvj.seatcock.Interface.SearchWord.SearchPresenter;
import sku.jvj.seatcock.Model.SearchWord;
import sku.jvj.seatcock.Server.KeywordData;
import sku.jvj.seatcock.Util.SearchDBHelper;

/**
 * Created by Android Hong on 2016-10-28.
 */

public class SearchInteractorInpl implements SearchInteractor {

    private SearchDBHelper searchDbHelper;
    private SearchPresenter searchPresenter;
    private KeywordData keywordData;
    private SearchHandler searchHandler;

    private double latitude;
    private double longitude;


    public SearchInteractorInpl(SearchPresenter searchPresenter) {
        this.searchPresenter = searchPresenter;

        latitude = SearchActivity.mActivity.getIntent().getDoubleExtra("latitude",latitude);
        longitude = SearchActivity.mActivity.getIntent().getDoubleExtra("longitude",longitude);

        searchDbHelper = new SearchDBHelper(SearchActivity.mContext, "SearchResult.db", null, 1);
        searchHandler = new SearchHandler();
        keywordData = new KeywordData(searchHandler);
    }

    @Override
    public ArrayList<SearchWord> setSearchWordData() {
        return searchDbHelper.getResult();
    }

    @Override
    public void getSearchRankData() {
        keywordData.KeywordGetData();
    }

    @Override
    public ArrayList<SearchWord> setSearchRankData() {
       return  keywordData.setKeyxwordSetData();
    }

    @Override
    public void insertSearchWord(String searchWord) {
        if (searchDbHelper.searchKeyword(searchWord)) {
            searchDbHelper.update(searchWord);
        } else {
            searchDbHelper.insert(searchWord);
        }
        keywordData.insertToKeyword(searchWord);
    }

    @Override
    public void deleteSearchWord(String searchWord) {
        searchDbHelper.delete(searchWord);
    }

    @Override
    public double getLatitude() {
        Log.e("latitude",String.valueOf(latitude));
        return latitude;
    }

    @Override
    public double getLongitude() {
        Log.e("longitude",String.valueOf(longitude));
        return longitude;
    }

    /**
     * 핸들러 클래스
     */
    public class SearchHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RANK_NETWORK_SUCCESS:
                    searchPresenter.onSuccess();
                    break;
                case RANK_NETWORK_FAILER:
                    searchPresenter.failOfTimeOut();
                    break;
            }
        }
    }
}
