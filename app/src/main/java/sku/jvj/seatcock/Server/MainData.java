package sku.jvj.seatcock.Server;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.MainInteractorImpl;
import sku.jvj.seatcock.Model.Store;

import static sku.jvj.seatcock.Interface.Main.MainInteractor.STORE_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.Main.MainInteractor.STORE_NETWORK_SUCCESS;


/**
 * Created by Android Hong on 2016-08-07.
 * Main화면 데이터 불러오는 Class
 */

public class MainData {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 10000;

    private String myJSONString;

    //php변수를 입력
    private static final String TAG_RESULTS = "result";
    private static final String TAG_STORE_ID = "store_id";
    private static final String TAG_STORE_NAME = "store_name";
    private static final String TAG_AVG_GPA = "avg_gpa";
    private static final String TAG_REVIEW_COUNT = "review_count";
    private static final String TAG_USE_SEAT = "use_seat";
    private static final String TAG_TOTAL_SEAT = "total_seat";
    private static final String TAG_DISTANCE = "distance";
    private static final String TAG_START = "start";
    private static final String TAG_FINISH = "finish";
    private static final String TAG_MAXTIME = "maxtime";
    private static final String TAG_NOR_CHK = "nor_chk";
    private static final String TAG_ZONE_CHK = "zone_chk";
    private static final String TAG_PIC_DATA = "pic_data";
    private static final String TAG_ADDR = "addr";
    private static final String TAG_X = "x";
    private static final String TAG_Y = "y";

    // 주소
    private static final String URL = "http://14.63.213.157/dongimg/mainpage_v6.0.php";

    private static ArrayList<Store> storeArrayList;
    private static JSONArray jsonArray = null;

    private MainInteractorImpl.MainHandler handler;

    /**
     * 생성자
     */
    public MainData(MainInteractorImpl.MainHandler handler) {
        this.handler = handler;
    }

    /**
     * http://14.63.213.157/dongimg/mainpage_v4.0.php 에서
     * 데이터 값을 불러와 JSON 파싱하기
     */
    public void getData(double latitude, double longitude) {
        class GetData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String latitude = (String) params[0];
                    String longitude = (String) params[1];

                    String data = URLEncoder.encode("usr_lati", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8");
                    data += "&" + URLEncoder.encode("usr_longi", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8");

                    URL url = new URL(URL);

                    URLConnection conn = url.openConnection();
                    //IllegalArgumentException
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setReadTimeout(READ_TIMEOUT);

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }catch (IllegalArgumentException e ){
                    return new String("failOfTimeOut");
                } catch (IOException e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
            @Override
            protected void onPostExecute(String result) {
                myJSONString = result;
                if(result.trim().equals("failOfTimeOut")){
                    handler.sendEmptyMessage(STORE_NETWORK_FAILER);
                }else if(result.trim().equals("failure")) {
                    handler.sendEmptyMessage(STORE_NETWORK_FAILER);
                }else{
                    handler.sendEmptyMessage(STORE_NETWORK_SUCCESS);
                }

            }
        }

        GetData getData = new GetData();
        getData.execute(Double.toString(latitude), Double.toString(longitude));
    }

    /**
     * getData()에서 파싱한 데이터를
     * storeArrayList 에 데이터를 저장하기
     * 초기 1회 실행
     */
    public ArrayList<Store> setData() {
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            storeArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                Store store = new Store();
                store.setStoreId(c.getString(TAG_STORE_ID));
                store.setStoreName(c.getString(TAG_STORE_NAME));
                store.setReviewConunt(Integer.parseInt(c.getString(TAG_REVIEW_COUNT)));
                store.setGpa(Float.parseFloat(c.getString(TAG_AVG_GPA)));
                store.setSeatTotalCountSituation(Integer.parseInt(c.getString(TAG_TOTAL_SEAT)));
                store.setSeatUseCountSituation(Integer.parseInt(c.getString(TAG_USE_SEAT)));
                store.setStoreDistance(Integer.parseInt(c.getString(TAG_DISTANCE)));
                store.setNormalReservation(c.getString(TAG_NOR_CHK));
                store.setZoneReservation(c.getString(TAG_ZONE_CHK));
                store.setImageResourceId(c.getString(TAG_PIC_DATA));
                store.setStoreStartTime(c.getString(TAG_START));
                store.setStoreFinishTime(c.getString(TAG_FINISH));
                store.setStoreMaxTime(c.getString(TAG_MAXTIME));
                store.setStoreAddress(c.getString(TAG_ADDR));
                store.setX(c.getString(TAG_X));
                store.setY(c.getString(TAG_Y));

                storeArrayList.add(store);
            }

        } catch (Exception e) {
            handler.sendEmptyMessage(STORE_NETWORK_FAILER);
        }

        return storeArrayList;
    }

}
