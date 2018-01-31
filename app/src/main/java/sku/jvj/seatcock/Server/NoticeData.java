package sku.jvj.seatcock.Server;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import sku.jvj.seatcock.Activity.NoticeActivity;
import sku.jvj.seatcock.Interactor.NoticeInteractorImpl;
import sku.jvj.seatcock.Model.Notice;
import sku.jvj.seatcock.Util.NoticeDBHelper;

import static sku.jvj.seatcock.Interface.Notice.NoticeInteractor.NOTICE_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.Notice.NoticeInteractor.NOTICE_NETWORK_SUCCESS;

/**
 * Created by Android Hong on 2016-10-12.
 */

public class NoticeData {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private static JSONArray jsonArray = null;

    private static final String URL = "http://14.63.213.157/dongimg/noti.php";
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NOTI_NUM = "noti_num";
    private static final String TAG_NOTI_TITLE = "noti_title";
    private static final String TAG_NOTI_DATE = "noti_date";
    private static final String TAG_NOTI_CONT = "noti_cont";

    private NoticeInteractorImpl.NoticeHandler handler;

    NoticeDBHelper noticeDbHelper = new NoticeDBHelper(NoticeActivity.mContext, "tbl_noti.db", null, 1);
    private String myJSONString;

    public NoticeData(NoticeInteractorImpl.NoticeHandler handler) {
        this.handler = handler;
    }

    /**
     * 공지사항 불러오기
     *
     */
    public void noticeGetData() {
        class GetData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String json;

                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                }catch (IllegalArgumentException e ){
                    return new String("failOfTimeOut");
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSONString = result;
                if(result.trim().equals("failOfTimeOut")){
                    handler.sendEmptyMessage(NOTICE_NETWORK_FAILER);
                }else if(result.trim().equals("failure")){
                    handler.sendEmptyMessage(NOTICE_NETWORK_FAILER);
                } else{
                    handler.sendEmptyMessage(NOTICE_NETWORK_SUCCESS);
                }
            }
        }
        GetData getData = new GetData();
        getData.execute(URL);
    }

    public ArrayList<Notice> setData(){
        ArrayList<Notice> noticeList =  new ArrayList<>();
        ArrayList<Notice> noNoticeList = noticeDbHelper.getNoticeList();
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                Notice notice;

                int noticeId = Integer.parseInt(c.getString(TAG_NOTI_NUM));
                String noticeTitle = c.getString(TAG_NOTI_TITLE);
                String noticeDate = c.getString(TAG_NOTI_DATE);
                String noticeContets = c.getString(TAG_NOTI_CONT);

                // 상태 ( 0 이 읽은것, 1 이 않읽은것 )
                int status = 0;

                if (noNoticeList.size() != 0) {
                    for (int j = 0; j < noNoticeList.size(); j++) {
                        if (noticeId == noNoticeList.get(j).getNoticeId()) {
                            status = 1;
                        }
                    }
                }

                notice = new Notice(noticeId, noticeTitle, noticeDate, noticeContets, status);
                noticeList.add(notice);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return noticeList;
    }

    public void setAllStatusUpdate(){
        noticeDbHelper.setAllStatusUpdate();
    }

}
