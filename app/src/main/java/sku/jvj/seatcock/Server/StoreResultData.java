package sku.jvj.seatcock.Server;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.SearchResultInteractorImpl;
import sku.jvj.seatcock.Model.Store;

import static sku.jvj.seatcock.Interface.SearchResult.SearchResultInteractor.SR_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.SearchResult.SearchResultInteractor.SR_NETWORK_SUCCESS;

/**
 * Created by Android Hong on 2016-08-24.
 */
public class StoreResultData {

    private SearchResultInteractorImpl.SearchResultHandler handler;

    //php변수를 입력
    private static final String TAG_RESULTS = "result";
    private static final String TAG_STORE_ID = "store_id";
    private static final String TAG_STORE_NAME = "store_name";
    private static final String TAG_AVG_GPA = "avg_gpa";
    private static final String TAG_REVIEW_COUNT = "review_count";
    private static final String TAG_USE_SEAT = "use_seat";
    private static final String TAG_TOTAL_SEAT = "total_seat";
    private static final String TAG_DISTANCE = "distance";
    private static final String TAG_NOR_CHK = "nor_chk";
    private static final String TAG_ZONE_CHK = "zone_chk";

    private static final String TAG_START = "start";
    private static final String TAG_FINISH = "finish";
    private static final String TAG_MAXTIME = "maxtime";
    private static final String TAG_PIC_DATA = "pic_data";
    private static final String TAG_ADDR = "addr";

    // 주소
    private static final String URL = "http://14.63.213.157/dongimg/search_v4.0.php";
    private static ArrayList<Store> storeArrayList = new ArrayList<>();

    private static JSONArray jsonArray = null;
    private String myJSONString;

    public StoreResultData(SearchResultInteractorImpl.SearchResultHandler handler) {
        this.handler = handler;
    }

    /**
     * 검색어로 값 불러오기
     *
     * @param search
     */
    public void getData(String search, double latitude, double longitude) {
        class GetData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String store_id = params[0];
                    String latitude = params[1];
                    String longitude = params[2];

                    String data = URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(store_id, "UTF-8");
                    data += "&" + URLEncoder.encode("usr_lati", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8");
                    data += "&" + URLEncoder.encode("usr_longi", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8");
                    java.net.URL url = new URL(URL);
                    URLConnection conn = url.openConnection();

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
                } catch (IllegalArgumentException e) {
                    return new String("failOfTimeOut");
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSONString = result;
                if (result.trim().equals("failOfTimeOut")) {
                    handler.sendEmptyMessage(SR_NETWORK_FAILER);
                }else if(result.trim().equals("failure")){
                    handler.sendEmptyMessage(SR_NETWORK_FAILER);
                } else {
                    handler.sendEmptyMessage(SR_NETWORK_SUCCESS);
                }
            }
        }
        GetData getData = new GetData();
        getData.execute(search, Double.toString(latitude), Double.toString(longitude));
    }

    /**
     * 값 저장하고 각종 컴포넌트에 초기화해주기
     */
    public ArrayList<Store> setData() {
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            storeArrayList.clear();

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

                store.setX(c.getString("x"));
                store.setY(c.getString("y"));

                storeArrayList.add(store);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return storeArrayList;
    }
}
