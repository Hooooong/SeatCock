package sku.jvj.seatcock.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import sku.jvj.seatcock.Model.Advertising;
import sku.jvj.seatcock.R;
import sku.jvj.seatcock.Util.KakaoWebViewClient;

/**
 * Created by Android Hong on 2016-10-18.
 */

public class AdActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView webView;
    private Advertising advertising;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);

        Intent advertisingIntent = getIntent();
        advertising = (Advertising)advertisingIntent.getSerializableExtra("Advertising");

        initLayout();
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout() {
        toolbar = (Toolbar) findViewById(R.id.adToolbar);
        toolbar.setTitle(advertising.getAdTitle());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView)findViewById(R.id.advertisingWebView);
        webView.setWebViewClient(new KakaoWebViewClient(this));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.loadUrl(advertising.getAdContent());
    }

    /**
     * Appbar 메뉴 생성 초기화
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    /**
     * Appbar 메뉴 선택 이벤트
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //메뉴 추가
        //noinspection SimplifiableIfStatement
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
}
