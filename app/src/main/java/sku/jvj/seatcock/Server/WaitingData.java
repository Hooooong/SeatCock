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

import sku.jvj.seatcock.Interactor.WaitingInteractorImpl;

import static sku.jvj.seatcock.Interface.Waiting.WaitingInteractor.WAIT_NETWORK_EXIST;
import static sku.jvj.seatcock.Interface.Waiting.WaitingInteractor.WAIT_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.Waiting.WaitingInteractor.WAIT_NETWORK_ISSUANCE;
import static sku.jvj.seatcock.Interface.Waiting.WaitingInteractor.WAIT_NETWORK_NOISSUANCE;
import static sku.jvj.seatcock.Interface.Waiting.WaitingInteractor.WAIT_NETWORK_SUCCESS;

/**
 * Created by Android Hong on 2016-10-31.
 */

public class WaitingData {

    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 15000;
    private static final String URL = "http://14.63.213.157/dongimg/waitingNumber.php";
    private static final String URLIN = "http://14.63.213.157/dongimg/insert_wait.php";

    private static final String TAG_RESULTS = "result";
    private static final String TAG_TICNUM= "ticNum";
    private static final String TAG_CNTWAIT = "cntWait";
    private static final String TAG_SEAT_TIME = "seat_time";

    private  WaitingInteractorImpl.WaitingHandler handler;
    private String myJSONString;
    private JSONArray jsonArray;
    private ArrayList<String> watingData;
    public WaitingData(WaitingInteractorImpl.WaitingHandler handler) {
        this.handler = handler;
    }


    /**
     * 점포ID로 대기번호 정보 불러오기
     */
    public void getWaitingData(String store_id) {
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
                if (result.equals("failOfTimeOut")) {
                    handler.sendEmptyMessage(WAIT_NETWORK_FAILER);
                } else if(result.equals("failure")) {
                    handler.sendEmptyMessage(WAIT_NETWORK_FAILER);
                }else {
                    handler.sendEmptyMessage(WAIT_NETWORK_SUCCESS);
                }
            }
        }
        GetData getData = new GetData();
        getData.execute(store_id);
    }

    //
    public ArrayList<String> setWatingData() {
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            watingData = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject c = jsonArray.getJSONObject(i);
                String ticketNum = c.getString(TAG_TICNUM);
                watingData.add(ticketNum);
                String waitingPerson = c.getString(TAG_CNTWAIT);
                watingData.add(waitingPerson);
                String seatTime = c.getString(TAG_SEAT_TIME);
                watingData.add(seatTime);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return watingData;
    }

    public void issuanceWaitingTicket(String store_id, String kakao_id, String person) {
        class InsertData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String store_id = (String) params[0];
                    String kakao_id = (String) params[1];
                    String person = (String) params[2];

                    String data = URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(store_id, "UTF-8");
                    data +="&" + URLEncoder.encode("kakao_id", "UTF-8") + "=" + URLEncoder.encode(kakao_id, "UTF-8");
                    data +="&" + URLEncoder.encode("res_person", "UTF-8") + "=" + URLEncoder.encode(person, "UTF-8");

                    URL url = new URL(URLIN);
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
                if (result.trim().equals("failOfTimeOut")) {
                    handler.sendEmptyMessage(WAIT_NETWORK_NOISSUANCE);
                }else if(result.trim().equals("failure")) {
                    handler.sendEmptyMessage(WAIT_NETWORK_NOISSUANCE);
                }else if(result.trim().equals("exist")){
                    handler.sendEmptyMessage(WAIT_NETWORK_EXIST);
                }else{
                    handler.sendEmptyMessage(WAIT_NETWORK_ISSUANCE);
                }
            }
        }

        InsertData task = new InsertData();
        task.execute(store_id,kakao_id,person);
    }
}
