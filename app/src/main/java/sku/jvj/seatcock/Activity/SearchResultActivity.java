package sku.jvj.seatcock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sku.jvj.seatcock.Adapter.StoreResultAdapter;
import sku.jvj.seatcock.Interface.SearchResult.SearchResultPresenter;
import sku.jvj.seatcock.Interface.SearchResult.SearchResultView;
import sku.jvj.seatcock.Model.Store;
import sku.jvj.seatcock.Presenter.SearchResultPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;


/**
 * Created by Android Hong on 2016-06-12.
 */
public class SearchResultActivity extends AppCompatActivity implements SearchResultView, View.OnClickListener {

    public static Activity mActivity;
    public static Context mContext;
    private TextView storeCount;
    private RelativeLayout noSearchLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SearchResultPresenter searchResultPresenter;
    private StoreResultAdapter mAdapter;
    private TextView searchSortTextView;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mActivity = this;
        mContext = this;

        searchResultPresenter = new SearchResultPresenterImpl(this);

        initLayout();
    }

    private void initLayout() {
        toolbar = (Toolbar) findViewById(R.id.searchResultToolbar);
        toolbar.setTitle(searchResultPresenter.getSearchWord());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storeCount = (TextView)findViewById(R.id.searchResultTextView);
        noSearchLayout = (RelativeLayout)findViewById(R.id.noSearchLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.storeResultRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StoreResultAdapter(searchResultPresenter);
        mRecyclerView.setAdapter(mAdapter);
        searchSortTextView = (TextView) findViewById(R.id.searchSortTextView);

        searchSortTextView.setOnClickListener(this);
        toolbar.setOnClickListener(this);
    }


    @Override
    public void showProgress() {
        progressDialog = Util.createProgressDialog(this);
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showSortDialog() {
        CharSequence info[] = new CharSequence[]{"거리순", "인기순", "리뷰순", "잔여좌석순"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#ffb300'>정렬</font>"));

        builder.setItems(info, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        searchResultPresenter.getSortStoreData(0);
                        break;
                    case 1:
                        searchResultPresenter.getSortStoreData(1);
                        break;
                    case 2:
                        searchResultPresenter.getSortStoreData(2);
                        break;
                    case 3:
                        searchResultPresenter.getSortStoreData(3);
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void navigateToSearch() {
        Intent searchWord = new Intent(this, SearchActivity.class);
        searchWord.putExtra("latitude",searchResultPresenter.getLatitude());
        searchWord.putExtra("longitude",searchResultPresenter.getLongitude());
        overridePendingTransition(0, 0);
        startActivity(searchWord);
    }

    @Override
    public void navigateToSeat(Store store) {
        Intent storeIntent = new Intent(this, SeatActivity.class);
        storeIntent.putExtra("store", store);
        startActivity(storeIntent);
    }

    @Override
    public void navigateToStore(Store store) {
        Intent storeIntent = new Intent(this, StoreActivity.class);
        storeIntent.putExtra("store", store);
        startActivity(storeIntent);
    }

    @Override
    public void displayStore(ArrayList<Store> storeArrayList, String searchWord, String sort) {
        storeCount.setText("'"+ searchWord +"'결과 " + storeArrayList.size() + "개 검색됨");
        searchSortTextView.setText(sort);
        if(storeArrayList.size() == 0){
            noSearchLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else{
            noSearchLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            mAdapter.addData(storeArrayList);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 기기 버튼(뒤로가기) 눌렀을 시
     */
    @Override
    public void onBackPressed() {
        this.finish();
    }

    /**
     * 뒤로가기 버튼 눌렀을 시
     */
    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }


    @Override
    public void onClick(View v) {
        int getId = v.getId();
        switch (getId){
            case R.id.searchResultToolbar:
                this.navigateToSearch();
                break;
            case R.id.searchSortTextView:
                this.showSortDialog();
                break;
        }
    }
}
