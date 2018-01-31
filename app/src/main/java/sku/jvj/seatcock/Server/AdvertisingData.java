package sku.jvj.seatcock.Server;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.MainInteractorImpl;
import sku.jvj.seatcock.Model.Advertising;

import static sku.jvj.seatcock.Interface.Main.MainInteractor.AD_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.Main.MainInteractor.AD_NETWORK_SUCCESS;

/**
 * Created by Android Hong on 2016-10-18.
 */

public class AdvertisingData {

    private ArrayList<Advertising> advertisingArrayList;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ADTITLE = "AdTitle";
    private static final String TAG_ADCONTENT = "AdContent";
    private static final String TAG_ADPIC = "AdPic";

    private static final String URL = "http://14.63.213.157/dongimg/Advertising.php";

    private JSONArray jsonArray = null;
    private MainInteractorImpl.MainHandler handler;
    private String myJSONString;

    public AdvertisingData(MainInteractorImpl.MainHandler handler) {
        this.handler = handler;
    }

    /**
     * 광고불러오기
     */
    public void getData() {
        class GetData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(URL);
                    URLConnection conn = url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setDoOutput(true);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }catch (IllegalArgumentException e){
                    return new String("failOfTimeOut");
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSONString = result;
                if(result.trim().equals("failOfTimeOut")){
                    handler.sendEmptyMessage(AD_NETWORK_FAILER);
                }else if(result.trim().equals("failure")) {
                    handler.sendEmptyMessage(AD_NETWORK_FAILER);
                }else{
                    handler.sendEmptyMessage(AD_NETWORK_SUCCESS);
                }
            }
        }

        GetData getData = new GetData();
        getData.execute();
    }

    /**
     * 값 저장하고 각종 컴포넌트에 초기화해주기
     */
    public ArrayList<Advertising> setData() {
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            advertisingArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                Advertising advertising = new Advertising();
                advertising.setAdTitle(c.getString(TAG_ADTITLE));
                advertising.setAdContent(c.getString(TAG_ADCONTENT));
                advertising.setAdPicture("http://14.63.213.157/dongimg/advertising/"+c.getString(TAG_ADPIC));

                advertisingArrayList.add(advertising);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  advertisingArrayList;
    }

}
