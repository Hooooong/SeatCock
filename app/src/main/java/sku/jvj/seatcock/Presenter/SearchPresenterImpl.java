package sku.jvj.seatcock.Presenter;

import sku.jvj.seatcock.Interactor.SearchInteractorInpl;
import sku.jvj.seatcock.Interface.SearchWord.SearchInteractor;
import sku.jvj.seatcock.Interface.SearchWord.SearchPresenter;
import sku.jvj.seatcock.Interface.SearchWord.SearchView;

/**
 * Created by Android Hong on 2016-10-28.
 */

public class SearchPresenterImpl implements SearchPresenter {

    private SearchView view;
    private SearchInteractor searchInteractor;

    public SearchPresenterImpl(SearchView view) {
        this.view = view;
        searchInteractor = new SearchInteractorInpl(this);
    }

    @Override
    public void getSearchWordData() {
        view.displaySearchWord(searchInteractor.setSearchWordData());
    }

    @Override
    public void getSearchRankData() {
        if(view != null){
            view.showProgress();
        }
        searchInteractor.getSearchRankData();
    }

    @Override
    public void deleteSearchWord(String searchWord) {
        searchInteractor.deleteSearchWord(searchWord);
        view.showToast("'"+searchWord+"' 단어가 삭제되었습니다.");
    }

    @Override
    public void insertSearchWord(String searchWord) {
        searchInteractor.insertSearchWord(searchWord);
    }


    @Override
    public void navigateToSearchResult(String searchWord) {
        view.navigateToSearchResult(searchWord);
    }

    @Override
    public void onSuccess() {
        if(view != null){
            view.hideProgress();
        }
        view.displaySearchRank(searchInteractor.setSearchRankData());
    }

    @Override
    public void failOfTimeOut() {
        if(view != null){
            view.hideProgress();
            view.showToast("값을 불러오지 못했습니다.");
        }
    }

    @Override
    public void showDialog(String searchWord) {
        view.showDialog(searchWord);
    }

    @Override
    public double getLatitude() {
        return searchInteractor.getLatitude();
    }

    @Override
    public double getLongitude() {
        return searchInteractor.getLongitude();
    }
}
