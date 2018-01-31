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

import sku.jvj.seatcock.Interactor.ContentsInteractorImpl;
import sku.jvj.seatcock.Model.Store;

import static sku.jvj.seatcock.Interface.Store.Contents.ContentsInteractor.CON_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.Store.Contents.ContentsInteractor.CON_NETWORK_SUCCESS;

public class StoreContentsData {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private JSONArray jsonArray = null;


    //php변수를 입력
    private static final String TAG_RESULTS = "result";
    private static final String TAG_STORE_NAME = "store_name";
    private static final String TAG_DISTANCE = "distance";
    private static final String TAG_START = "start";
    private static final String TAG_FINISH = "finish";
    private static final String TAG_NOR_CHK = "nor_chk";
    private static final String TAG_ZONE_CHK = "zone_chk";
    private static final String TAG_ADDR = "addr";
    private static final String TAG_X = "x";
    private static final String TAG_Y = "y";
    private static final String TAG_PHONE_NUM = "phone_num";
    private static final String TAG_INTRO = "intro";
    private static final String TAG_AVG_GPA = "avg_gpa";
    private static final String TAG_TRADEMARK = "trademark";
    private static final String TAG_HOMEPAGE = "homepage";
    private static final String TAG_AVG_5 = "avg_5";
    private static final String TAG_AVG_4 = "avg_4";
    private static final String TAG_AVG_3 = "avg_3";
    private static final String TAG_AVG_2 = "avg_2";
    private static final String TAG_AVG_1 = "avg_1";

    private static final String URL = "http://14.63.213.157/dongimg/store_info_v5.0.php";
    private ContentsInteractorImpl.StoreContents handler;
    private String myJSONString;
    private Store store;

    /**
     * 생성자
     * @param handler
     */
    public StoreContentsData(ContentsInteractorImpl.StoreContents handler) {
        this.handler = handler;
    }

    /**
     * 점포ID로 값 불러오기
     * @param store_id
     */
    public void getData(String store_id) {
        class GetData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String store_id = (String) params[0];

                    String data = URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(store_id, "UTF-8");
                    URL url = new URL(URL);
                    URLConnection conn = url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();

                }catch (IllegalArgumentException e ){
                    return new String("failOfTimeOut");
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSONString = result;
                if(result.trim().equals("failOfTimeOut")){
                    handler.sendEmptyMessage(CON_NETWORK_FAILER);
                }else if(result.trim().equals("failure")){
                    handler.sendEmptyMessage(CON_NETWORK_FAILER);
                }else{
                    handler.sendEmptyMessage(CON_NETWORK_SUCCESS);
                }

            }
        }
        GetData getData = new GetData();
        getData.execute(store_id);
    }

    /**
     * 값 저장하고 각종 컴포넌트에 초기화해주기
     */
    public Store setData() {
        store = new Store();
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                store.setStoreName(c.getString(TAG_STORE_NAME));
                store.setStoreDistance(Integer.parseInt(c.getString(TAG_DISTANCE)));
                store.setStoreStartTime(c.getString(TAG_START));
                store.setStoreFinishTime(c.getString(TAG_FINISH));
                store.setNormalReservation(c.getString(TAG_NOR_CHK));
                store.setZoneReservation(c.getString(TAG_ZONE_CHK));
                store.setStoreAddress(c.getString(TAG_ADDR));
                store.setX(c.getString(TAG_X));
                store.setY(c.getString(TAG_Y));
                store.setPhoneNumber(c.getString(TAG_PHONE_NUM));
                store.setStoreIntro(c.getString(TAG_INTRO));

                store.setHomepage(c.getString(TAG_HOMEPAGE));
                store.setGpa(Float.parseFloat(c.getString(TAG_AVG_GPA)));
                store.setStoreTrademark(c.getString(TAG_TRADEMARK));
                store.setGpa_five(Integer.parseInt(c.getString(TAG_AVG_5)));
                store.setGpa_four(Integer.parseInt(c.getString(TAG_AVG_4)));
                store.setGpa_three(Integer.parseInt(c.getString(TAG_AVG_3)));
                store.setGpa_two(Integer.parseInt(c.getString(TAG_AVG_2)));
                store.setGpa_one(Integer.parseInt(c.getString(TAG_AVG_1)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return store;
    }
}
