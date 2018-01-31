package sku.jvj.seatcock.Presenter;

import sku.jvj.seatcock.Interactor.ContentsInteractorImpl;
import sku.jvj.seatcock.Interface.Store.Contents.ContentsInteractor;
import sku.jvj.seatcock.Interface.Store.Contents.ContentsPresenter;
import sku.jvj.seatcock.Interface.Store.Contents.ContentsView;
import sku.jvj.seatcock.Model.Store;

/**
 * Created by Android Hong on 2016-10-24.
 */

public class ContentsPresenterImpl implements ContentsPresenter {

    private ContentsView view;
    private ContentsInteractor contentsInteractor;

    public ContentsPresenterImpl(ContentsView view){
        this.view = view;
         contentsInteractor = new ContentsInteractorImpl(this);
    }

    @Override
    public void getContentsData(String storeId) {
        if(view != null){
            view.showProgress();
        }
        contentsInteractor.getContentsData(storeId);
    }

    @Override
    public void onSuccess(Store store) {
        if(view != null){
            view.hideProgress();
            view.displayContents(store);
        }

    }

    @Override
    public void failOfTimeOut() {
        if(view != null){
            view.hideProgress();
        }
    }
}
