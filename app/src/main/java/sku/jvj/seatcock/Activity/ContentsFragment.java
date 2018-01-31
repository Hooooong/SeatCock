package sku.jvj.seatcock.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;

import sku.jvj.seatcock.Interface.Store.Contents.ContentsPresenter;
import sku.jvj.seatcock.Interface.Store.Contents.ContentsView;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Presenter.ContentsPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;

/**
 * Created by Android Hong on 2016-06-08.
 */
public class ContentsFragment extends Fragment implements ContentsView {

    private static String storeId;
    private static View view;

    private TextView gpaTextView,gpaCountTextView,titleTextView,addressTextView,phoneTextView,introTextView,useTimeTextView,homepageTextView,gpaNullTextView;
    private ImageView trademarkImageVIew,normalImageView,zoneImageView;
    private LinearLayout progressLayout;
    private ContentsPresenter contentsPresenter;
    private ProgressDialog progressDialog;
    private RatingBar gpaRatingBar;
    private RoundCornerProgressBar progressBar5,progressBar4,progressBar3,progressBar2,progressBar1;

    /**
     * 생성자
     * @return storeId
     * @return
     */
    public static ContentsFragment create(String storeId) {
        ContentsFragment fragment = new ContentsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("storeId",storeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * onCreateView
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store_contents, container, false);
        storeId = (String)getArguments().getSerializable("storeId");
        initlayout();

        contentsPresenter = new ContentsPresenterImpl(this);
        contentsPresenter.getContentsData(storeId);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initlayout(){
        gpaTextView = (TextView) view.findViewById(R.id.gpaTextView);
        gpaCountTextView = (TextView) view.findViewById(R.id.gpaCountTextView);
        gpaRatingBar = (RatingBar) view.findViewById(R.id.gpaRatingBar);

        titleTextView = (TextView) view.findViewById(R.id.storeNameTextView);
        addressTextView = (TextView) view.findViewById(R.id.storeAddressTextView);
        phoneTextView = (TextView) view.findViewById(R.id.storePhoneNumberTextView);
        introTextView = (TextView) view.findViewById(R.id.storeIntroTextView);
        useTimeTextView = (TextView) view.findViewById(R.id.storeUseTime);
        homepageTextView = (TextView) view.findViewById(R.id.storeHomepageTextView);
        trademarkImageVIew = (ImageView) view.findViewById(R.id.tradeMarkImg);
        normalImageView = (ImageView) view.findViewById(R.id.normalImageView);
        zoneImageView = (ImageView) view.findViewById(R.id.zoneImageView);

        progressLayout = (LinearLayout)view.findViewById(R.id.progressLayout);
        gpaNullTextView =(TextView)view.findViewById(R.id.gpaNullTextView);

        progressBar5 = (RoundCornerProgressBar) view.findViewById(R.id.progressBar5);
        progressBar4 = (RoundCornerProgressBar) view.findViewById(R.id.progressBar4);
        progressBar3 = (RoundCornerProgressBar) view.findViewById(R.id.progressBar3);
        progressBar2 = (RoundCornerProgressBar) view.findViewById(R.id.progressBar2);
        progressBar1 = (RoundCornerProgressBar) view.findViewById(R.id.progressBar1);
    }

    @Override
    public void showProgress() {
        progressDialog = Util.createProgressDialog(getActivity());
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void displayContents(Store store) {
        gpaTextView.setText(String.valueOf(store.getGpa()));
        printProgress(store.getGpa_five(),store.getGpa_four(),store.getGpa_three(),store.getGpa_two(),store.getGpa_one());
        gpaCountTextView.setText(store.getGpa_five() + store.getGpa_four() + store.getGpa_three() + store.getGpa_two() + store.getGpa_one() + "명");
        gpaRatingBar.setRating(store.getGpa());
        titleTextView.setText(store.getStoreName());
        addressTextView.setText(store.getStoreAddress());
        phoneTextView.setText(store.getPhoneNumber());
        introTextView.setText(store.getStoreIntro());
        String useTime = store.getStoreStartTime()+" ~ "+store.getStoreFinishTime();
        useTimeTextView.setText(useTime);
        homepageTextView.setText(store.getHomepage());
        trademarkPrint(store.getStoreTrademark());
        normalImageView.setImageResource(checkNormalImageResource(store.isNormalReservation()));
        zoneImageView.setImageResource(checkZoneImageResource(store.isZoneReservation()));
    }

    /**
     * TreadMarK 그리기
     * @param check
     */
    private void trademarkPrint(String check) {
        if ("http://14.63.213.157/dongimg/trademark/noimage".equals(check)) {
            Glide.with(view.getContext())
                    .load(R.drawable.img_noreult)
                    .into(trademarkImageVIew);

        } else {
            Glide.with(view.getContext())
                    .load(check)
                    .into(trademarkImageVIew);

        }
    }

    /**
     * Normal Image 그리기
     * @param source
     * @return
     */
    private int checkNormalImageResource(boolean source) {
        int result;
        if (source) {
            result = R.drawable.img_normalreservation;
        } else {
            result = R.drawable.img_unnormalreservation;
        }
        return result;
    }

    /**
     * Zone Image 그리기
     * @param source
     * @return
     */
    private int checkZoneImageResource(boolean source) {
        int result;
        if (source) {
            result = R.drawable.img_zonereservation;
        } else {
            result = R.drawable.img_unzonereservation;
        }
        return result;
    }

    /**
     * Progressbar 그리기
     * @param gpa5
     * @param gpa4
     * @param gpa3
     * @param gpa2
     * @param gpa1
     */
    private void printProgress( int gpa5, int gpa4, int gpa3, int gpa2, int gpa1) {

        int gpaTotal =gpa5+gpa4+gpa3+gpa2+gpa1;
        if (gpaTotal == 0) {
            progressLayout.setVisibility(View.GONE);
            gpaNullTextView.setVisibility(View.VISIBLE);
        } else {
            int max = Util.checkMax(gpa5, gpa4, gpa3 ,gpa2, gpa1);
            progressBar5.setMax(100);
            progressBar5.setProgress(gpa5 * 100 / max);
            progressBar4.setMax(100);
            progressBar4.setProgress(gpa4 * 100 / max);
            progressBar3.setMax(100);
            progressBar3.setProgress(gpa3 * 100 / max);
            progressBar2.setMax(100);
            progressBar2.setProgress(gpa2 * 100 / max);
            progressBar1.setMax(100);
            progressBar1.setProgress(gpa1 * 100 / max);
        }
    }

}
