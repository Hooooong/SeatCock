package sku.jvj.seatcock.Interface.Notice;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.Notice;

/**
 * Created by Android Hong on 2016-10-26.
 */

public interface NoticeView {

    void showProgress();
    void hideProgress();

    void displayNotice(ArrayList<Notice> noticeArrayList);

}
