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

import sku.jvj.seatcock.Interactor.MenuInteractorImpl;
import sku.jvj.seatcock.Model.StoreMenu;
import sku.jvj.seatcock.Util.Util;

import static sku.jvj.seatcock.Interface.Store.Menu.MenuInteractor.MENU_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.Store.Menu.MenuInteractor.MENU_NETWORK_SUCCESS;

/**
 * Created by Android Hong on 2016-08-18.
 */
public class StoreMenuData {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    //php변수를 입력
    private static final String TAG_RESULTS = "result";
    private static final String TAG_MENU_NUM = "menu_num";
    private static final String TAG_MENU_NAME = "menu_name";
    private static final String TAG_MANU_PIC = "menu_pic";
    private static final String TAG_MENU_PRICE = "menu_price";

    private static final String URL = "http://14.63.213.157/dongimg/menu_info.php";


    private MenuInteractorImpl.StoreMenu handler;
    private JSONArray jsonArray = null;
    private String myJSONString;

    /**
     * 생성자
     * @param handler
     */
    public StoreMenuData(MenuInteractorImpl.StoreMenu handler) {
        this.handler = handler;
    }

    /**
     * 점포ID로 메뉴 불러오기
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
                    handler.sendEmptyMessage(MENU_NETWORK_FAILER);
                }else if(result.trim().equals("failure")){
                    handler.sendEmptyMessage(MENU_NETWORK_FAILER);
                }else{
                    handler.sendEmptyMessage(MENU_NETWORK_SUCCESS);
                }
            }
        }

        GetData getData = new GetData();
        getData.execute(store_id);
    }

    /**
     * 값 저장하기
     */
    public ArrayList<StoreMenu> setData() {
        ArrayList<StoreMenu> storeMenuArrayList =  new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(myJSONString);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                StoreMenu storeMenu = new StoreMenu();
                storeMenu.setMenuNum(c.getString(TAG_MENU_NUM));
                storeMenu.setMenuName(c.getString(TAG_MENU_NAME));
                storeMenu.setMenuPrice(Util.getMoney(c.getString(TAG_MENU_PRICE)));
                storeMenu.setImageResourceId(c.getString(TAG_MANU_PIC));

                storeMenuArrayList.add(storeMenu);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return storeMenuArrayList;
    }

}
