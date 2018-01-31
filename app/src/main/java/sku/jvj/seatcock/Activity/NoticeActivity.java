package sku.jvj.seatcock.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import sku.jvj.seatcock.Adapter.NoticeAdapter;
import sku.jvj.seatcock.Interface.Notice.NoticePresenter;
import sku.jvj.seatcock.Interface.Notice.NoticeView;
import sku.jvj.seatcock.Model.Notice;
import sku.jvj.seatcock.Presenter.NoticePresenterImpl;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.Util;


/**
 * Created by Android Hong on 2016-10-12.
 */
public class NoticeActivity extends AppCompatActivity implements NoticeView {

    public static Context mContext;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private ExpandableListView noticeListView;
    private NoticeAdapter noticeAdapter;

    private NoticePresenter noticePresenter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        initLayout();
        noticePresenter = new NoticePresenterImpl(this);
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout() {
        mContext = this;
        toolbar = (Toolbar) findViewById(R.id.noticeToolbar);
        toolbar.setTitle("공지사항");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noticeListView = (ExpandableListView)findViewById(R.id.noticeListView);
        noticeAdapter = new NoticeAdapter();
        noticeListView.setAdapter(noticeAdapter);

    }

    /**
     * Appbar 메뉴 생성 초기화
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    /**
     * Appbar 메뉴 선택 이벤트
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_back) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 뒤로가기 버튼 눌렀을 시
     */
    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    /**
     * 기기 버튼(뒤로가기) 눌렀을 시
     */
    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noticePresenter.setAllStatusUpdate();
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
    public void displayNotice(ArrayList<Notice> noticeArrayList) {
        noticeAdapter.addData(noticeArrayList);
        noticeAdapter.notifyDataSetChanged();
    }
}
