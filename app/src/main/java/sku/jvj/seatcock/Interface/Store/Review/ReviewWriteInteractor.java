package sku.jvj.seatcock.Interface.Store.Review;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Android Hong on 2016-10-24.
 */
public interface ReviewWriteInteractor {

    int REVIEW_INSERT_NETWORK_SUCCESS = 1;
    int REVIEW_INSERT_NETWORK_FAILER = 2;
    int REVIEW_INSERT_PICTURE_SUCCESS = 3;
    int REVIEW_INSERT_PICTURE_FAILER = 4;

    void insertToReviewData(String rating1, String rating2, String rating3, String reviewText);
    void insertToReviewRealPictureData();
    void insertToReviewPicture(Uri uri,String path);

    ArrayList deleteToReviewPictture(int pictureNum);

    void modifyToReviewPicture(Uri uri,String path, int pictureNum);

    int getPictureSize();
}
