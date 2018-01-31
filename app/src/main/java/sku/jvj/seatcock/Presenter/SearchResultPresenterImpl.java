package sku.jvj.seatcock.Presenter;

import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.SearchResultInteractorImpl;
import sku.jvj.seatcock.Interface.SearchResult.SearchResultInteractor;
import sku.jvj.seatcock.Interface.SearchResult.SearchResultPresenter;
import sku.jvj.seatcock.Interface.SearchResult.SearchResultView;
import sku.jvj.seatcock.Model.Store;

/**
 * Created by Android Hong on 2016-10-28.
 */

public class SearchResultPresenterImpl implements SearchResultPresenter {

    private SearchResultView view;
    private SearchResultInteractor searchResultInteractor;

    public SearchResultPresenterImpl(SearchResultView view) {
        this.view = view;
        searchResultInteractor = new SearchResultInteractorImpl(this);
        this.getStoreData();
    }

    @Override
    public void getStoreData() {
        if(view != null){
            view.showProgress();
        }
        searchResultInteractor.getStoreData();
    }

    @Override
    public void getSortStoreData(int num) {
        searchResultInteractor.getSortData(num);
    }

    @Override
    public void setStoreData(ArrayList<Store> storeArrayList,String sort) {
        view.displayStore(storeArrayList,searchResultInteractor.getSearchWord(),sort);
    }

    @Override
    public void navigateToSeat(Store store) {
        view.navigateToSeat(store);
    }

    @Override
    public void navigateToStore(Store store) {
        view.navigateToStore(store);
    }

    @Override
    public void onSuccess() {
        if(view != null){
            view.hideProgress();
        }
        searchResultInteractor.setStoreData();
    }

    @Override
    public void failOfTimeOut() {
        if(view != null){
            view.hideProgress();
        }
    }

    @Override
    public String getSearchWord() {
        return searchResultInteractor.getSearchWord();
    }

    @Override
    public double getLatitude() {
        return searchResultInteractor.getLatitude();
    }

    @Override
    public double getLongitude() {
        return searchResultInteractor.getLongitude();
    }
}
