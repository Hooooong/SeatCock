package sku.jvj.seatcock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sku.jvj.seatcock.Adapter.SearchRankAdapter;
import sku.jvj.seatcock.Adapter.SearchWordAdapter;
import sku.jvj.seatcock.Interface.SearchWord.SearchPresenter;
import sku.jvj.seatcock.Model.SearchWord;
import sku.jvj.seatcock.Presenter.SearchPresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;


/**
 * Created by Android Hong on 2016-06-11.
 */
public class SearchActivity extends AppCompatActivity implements sku.jvj.seatcock.Interface.SearchWord.SearchView,View.OnClickListener, SearchView.OnCloseListener,SearchView.OnQueryTextListener{

    public static Context mContext;
    public static Activity mActivity;

    private Toolbar toolbar;
    private ListView listView;
    private SearchView searchView;
    private MenuItem searchItem;
    private TextView recentlySearch, popularitySearch;
    private RelativeLayout noSearchLayout;
    private SearchPresenter searchPresenter;
    private ProgressDialog progressDialog;
    private SearchWordAdapter searchWordAdapter;
    private SearchRankAdapter searchRankAdapter;

    private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = this;
        mActivity = this;
        searchPresenter = new SearchPresenterImpl(this);
        initLayout();

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == event.ACTION_DOWN){
                    String text = recentlySearch.getText().toString();
                    Toast.makeText(v.getContext(), text ,Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        recentlySearch.setBackgroundColor(Color.parseColor("#FFB300"));
        recentlySearch.setTextColor(Color.parseColor("#FFFFFF"));

        popularitySearch.setBackgroundColor(Color.parseColor("#FAFAFA"));
        popularitySearch.setTextColor(Color.parseColor("#7A7A7A"));

        searchPresenter.getSearchWordData();
    }

    /**
     * Search Layout 초기화
     */
    private void initLayout() {

        toolbar = (Toolbar) findViewById(R.id.searchToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listView = (ListView) findViewById(R.id.searchWordListView);
        searchWordAdapter = new SearchWordAdapter(searchPresenter);
        searchRankAdapter = new SearchRankAdapter(searchPresenter);

        noSearchLayout = (RelativeLayout) findViewById(R.id.noSearchLayout);
        recentlySearch = (TextView) findViewById(R.id.recentlySearch);
        popularitySearch = (TextView) findViewById(R.id.popularitySearch);

        recentlySearch.setOnClickListener(this);
        popularitySearch.setOnClickListener(this);






        listView.setEmptyView(noSearchLayout);
    }

    /**
     * SearchView 메뉴 초기화
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        //항상 열어두기
        searchView.setIconified(false);
        searchView.setMaxWidth(1999999999);
        searchView.setQueryHint("이름 또는 주소를 입력하세요.");

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        return true;
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
    public void showProgress() {
        progressDialog = Util.createProgressDialog(this);
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displaySearchWord(ArrayList<SearchWord> searchWordArrayList) {
        searchWordAdapter.addData(searchWordArrayList);
        listView.setAdapter(searchWordAdapter);
        searchWordAdapter.notifyDataSetChanged();
    }

    @Override
    public void displaySearchRank(ArrayList<SearchWord> searchRankArrayList) {
        searchRankAdapter.addData(searchRankArrayList);
        listView.setAdapter(searchRankAdapter);
        searchWordAdapter.notifyDataSetChanged();

    }

    @Override
    public void navigateToSearchResult(String result) {
        Intent search = new Intent(this, SearchResultActivity.class);
        search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        search.putExtra("latitude",searchPresenter.getLatitude());
        search.putExtra("longitude",searchPresenter.getLongitude());
        search.putExtra("searchWord",result);
        startActivity(search);
    }

    @Override
    public void showDialog(final String searchWord) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("최근검색어 삭제");
        builder.setMessage("'" + searchWord + "' 삭제하시겠습니까?");


        String positiveText = "OK";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                searchPresenter.deleteSearchWord(searchWord);
                searchPresenter.getSearchWordData();
            }
        });

        String negativeText = "Cancel";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        int getId = v.getId();
        switch (getId){
            case R.id.recentlySearch:
                recentlySearch.setBackgroundColor(Color.parseColor("#FFB300"));
                recentlySearch.setTextColor(Color.parseColor("#FFFFFF"));

                popularitySearch.setBackgroundColor(Color.parseColor("#FAFAFA"));
                popularitySearch.setTextColor(Color.parseColor("#7A7A7A"));

                searchPresenter.getSearchWordData();

                break;
            case R.id.popularitySearch:
                popularitySearch.setBackgroundColor(Color.parseColor("#FFB300"));
                popularitySearch.setTextColor(Color.parseColor("#FFFFFF"));

                recentlySearch.setBackgroundColor(Color.parseColor("#FAFAFA"));
                recentlySearch.setTextColor(Color.parseColor("#7A7A7A"));

                searchPresenter.getSearchRankData();
                break;
        }

    }

    @Override
    public boolean onClose() {
        finish();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        String searchWord = searchView.getQuery().toString().trim();
        if (searchWord.length() <= 0) {
            Toast.makeText(getApplicationContext(), "검색어를 입력하세요.", Toast.LENGTH_LONG).show();
            searchView.setQuery("", true);
        } else {
            Intent search = new Intent(SearchActivity.this, SearchResultActivity.class);
            search.putExtra("searchWord", searchWord);
            searchPresenter.insertSearchWord(searchWord);
            search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(search);
            searchView.setQuery("", true);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        return false;
    }
}
