package sku.jvj.seatcock.Interface.Store.Review;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Android Hong on 2016-10-24.
 */

public interface ReviewWriteView {

    int REQ_CODE_IMAGE = 0;
    int REQ_CHANGE_IMAGE1 = 1;
    int REQ_CHANGE_IMAGE2 = 2;
    int REQ_CHANGE_IMAGE3 = 3;


    void showProgress();
    void hideProgress();
    void hideProgressAndFinish();
    void showToast(String message);

    void showPictureDialog(int pictureNum);
    void showBackDialog();

    void navigateToGallery(int number);

    void setPicture(Uri uri);
    void laterModifyPicture(Uri uri, int pictureNum);


    void laterDeletePictrue(ArrayList arrayList);
}
