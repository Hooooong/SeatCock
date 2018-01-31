package sku.jvj.seatcock.Interactor;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sku.jvj.seatcock.Activity.SearchActivity;
import sku.jvj.seatcock.Activity.SearchResultActivity;
import sku.jvj.seatcock.Interface.SearchResult.SearchResultInteractor;
import sku.jvj.seatcock.Interface.SearchResult.SearchResultPresenter;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Server.StoreResultData;

/**
 * Created by Android Hong on 2016-10-28.
 */

public class SearchResultInteractorImpl implements SearchResultInteractor {

    private SearchResultPresenter searchResultPresenter;

    private SearchResultHandler searchResultHandler;
    private StoreResultData storeResultData;
    private ArrayList<Store> storeArrayList = null;
    private String searchWord;
    private double latitude = 0;
    private double longitude = 0;

    public SearchResultInteractorImpl(SearchResultPresenter searchResultPresenter) {
        this.searchResultPresenter = searchResultPresenter;

        searchWord = SearchResultActivity.mActivity.getIntent().getStringExtra("searchWord");
        latitude = SearchActivity.mActivity.getIntent().getDoubleExtra("latitude",latitude);
        longitude = SearchActivity.mActivity.getIntent().getDoubleExtra("longitude",longitude);

        searchResultHandler = new SearchResultHandler();
        storeResultData = new StoreResultData(searchResultHandler);
    }

    @Override
    public void getStoreData() {
        storeResultData.getData(searchWord, latitude,longitude);
    }

    @Override
    public void setStoreData() {
        storeArrayList = storeResultData.setData();
        searchResultPresenter.setStoreData(storeArrayList,"거리순");
    }

    @Override
    public void getSortData(int num) {
        switch (num) {
            case 0:
                Collections.sort(storeArrayList, new Comparator<Store>() {
                    @Override
                    public int compare(Store lhs, Store rhs) {
                        return (lhs.getStoreDistance() < rhs.getStoreDistance()) ? -1 : (lhs.getStoreDistance() < rhs.getStoreDistance()) ? 1 : 0;
                    }
                });
                searchResultPresenter.setStoreData(storeArrayList,"거리순");
                break;
            case 1:
                Collections.sort(storeArrayList, new Comparator<Store>() {
                    @Override
                    public int compare(Store lhs, Store rhs) {
                        return (lhs.getGpa() > rhs.getGpa()) ? -1 : (lhs.getGpa() > rhs.getGpa()) ? 1 : 0;

                    }
                });
                searchResultPresenter.setStoreData(storeArrayList,"인기순");
                break;
            case 2:
                Collections.sort(storeArrayList, new Comparator<Store>() {
                    @Override
                    public int compare(Store lhs, Store rhs) {
                        return (lhs.getReviewConunt() > rhs.getReviewConunt()) ? -1 : (lhs.getReviewConunt() > rhs.getReviewConunt()) ? 1 : 0;
                    }
                });
                searchResultPresenter.setStoreData(storeArrayList,"리뷰순");
                break;
            case 3:
                Collections.sort(storeArrayList, new Comparator<Store>() {
                    @Override
                    public int compare(Store lhs, Store rhs) {
                        return (lhs.getSeatTotalCountSituation() - lhs.getSeatUseCountSituation() > rhs.getSeatTotalCountSituation() - rhs.getSeatUseCountSituation()) ? -1 : (lhs.getSeatTotalCountSituation() - lhs.getSeatUseCountSituation() > rhs.getSeatTotalCountSituation() - rhs.getSeatUseCountSituation()) ? 1 : 0;
                    }
                });
                searchResultPresenter.setStoreData(storeArrayList,"잔여좌석순");
                break;
        }
    }

    @Override
    public String getSearchWord() {
        return searchWord;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    /**
     * 핸들러 클래스
     */
    public class SearchResultHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SR_NETWORK_SUCCESS:
                    searchResultPresenter.onSuccess();
                    break;
                case SR_NETWORK_FAILER:
                    searchResultPresenter.failOfTimeOut();
                    break;
            }
        }
    }
}
