package sku.jvj.seatcock.Server;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import sku.jvj.seatcock.Interactor.SearchInteractorInpl;
import sku.jvj.seatcock.Model.SearchWord;

import static sku.jvj.seatcock.Interface.Main.MainInteractor.STORE_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.Main.MainInteractor.STORE_NETWORK_SUCCESS;

/**
 * Created by Android Hong on 2016-08-24.
 *
 */
public class KeywordData {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private static final String URLIn = "http://14.63.213.157/dongimg/popula.php";
    private static final String URLOut = "http://14.63.213.157/dongimg/popula_rank.php";
    private static final String TAG_RESULTS = "result";
    private static final String TAG_KEYWORD = "keyword";

    private static ArrayList<SearchWord> searchRankList;
    private static JSONArray jsonArray = null;
    private SearchInteractorInpl.SearchHandler handler;
    private String myJSONString;

    public KeywordData(SearchInteractorInpl.SearchHandler handler) {
        this.handler = handler;
    }

    /**
     * 검색결과 저장하기
     * @param search
     */
    public void insertToKeyword(String search) {
        class InsertData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try {
                    String keyword = (String) params[0];

                    String data = URLEncoder.encode("keyword", "UTF-8") + "=" + URLEncoder.encode(keyword, "UTF-8");
                    URL url = new URL(URLIn);
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
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(search);
    }

    /**
     * 인기순위 가지고오기
     */
    public void KeywordGetData() {
        class GetData extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                BufferedReader bufferedReader;
                try {
                    URL url = new URL(URLOut);
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
        getData.execute();

    }

    public ArrayList<SearchWord> setKeyxwordSetData() {
        searchRankList = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                String number = Integer.toString(i+1);
                SearchWord searchRank = new SearchWord(number, c.getString(TAG_KEYWORD));
                searchRankList.add(searchRank);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchRankList;
    }
}