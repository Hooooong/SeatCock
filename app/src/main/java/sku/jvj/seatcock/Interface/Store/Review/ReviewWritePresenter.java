package sku.jvj.seatcock.Interface.Store.Review;

import android.net.Uri;

/**
 * Created by Android Hong on 2016-10-24.
 */

public interface ReviewWritePresenter {
    void insertReviewData(String rating1, String rating2, String rating3, String reviewText);

    void checkPermission();

    int getPictureSize();

    void addPicture(Uri uri, String path);
    void modifyPicture(Uri uri,String path, int pictureNum);
    void deletePicture(int pictureNum);

    void onSuccess();
    void failOfTImeOut();


}
