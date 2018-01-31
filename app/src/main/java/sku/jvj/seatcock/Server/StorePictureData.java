package sku.jvj.seatcock.Server;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.StoreInteractorImpl;

import static sku.jvj.seatcock.Interface.Store.StoreInterator.PIC_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.Store.StoreInterator.PIC_NETWORK_SUCCESS;

/**
 * Created by Android Hong on 2016-09-09.
 */
public class StorePictureData {

    private StoreInteractorImpl.StorePictureHandler handler;
    private ArrayList<String> pictureArrayList;

    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 15000;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_STORE_PIC = "store_pic";

    private static final String URL = "http://14.63.213.157/dongimg/storepic.php";

    private JSONArray jsonArray = null;
    private String myJSONString;


    public StorePictureData(StoreInteractorImpl.StorePictureHandler handler) {
        this.handler = handler;
    }

    /**
     * 점포ID로 사진 불러오기
     *
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
                } catch (IOException e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSONString = result;
                if(result.trim().equals("failOfTimeOut")){
                    handler.sendEmptyMessage(PIC_NETWORK_FAILER);
                }else if(result.trim().equals("failure")){
                    handler.sendEmptyMessage(PIC_NETWORK_FAILER);
                }else{
                    handler.sendEmptyMessage(PIC_NETWORK_SUCCESS);
                }
            }
        }
        GetData getData = new GetData();
        getData.execute(store_id);
    }

    /**
     * 값 저장하고 각종 컴포넌트에 초기화해주기
     */
    public ArrayList<String> setData() {
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            pictureArrayList = new ArrayList<>();
            pictureArrayList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                pictureArrayList.add("http://14.63.213.157/dongimg/storeimg/" + c.getString(TAG_STORE_PIC));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pictureArrayList;
    }
}
