package sku.jvj.seatcock.Server;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import sku.jvj.seatcock.Interactor.SignUpInteractorImpl;

import static sku.jvj.seatcock.Interface.SignUp.SignUpInteractor.USER_NETWORK_FAILER;
import static sku.jvj.seatcock.Interface.SignUp.SignUpInteractor.USER_NETWORK_SUCCESS;

/**
 * Created by Android Hong on 2016-09-04.
 */
public class UserData {

    private SignUpInteractorImpl.SignUpHandler handler;

    public UserData(SignUpInteractorImpl.SignUpHandler handler) {
        this.handler = handler;
    }

    /**
     * 회원가입 메소드
     * @param kakao_id
     * @param kakao_name
     * @param jubun
     * @param phone_num
     * @param profilepath
     */
    public void insertToUser(String kakao_id, String kakao_name, String jubun, String phone_num, String profilepath) {
        class InsertData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected String doInBackground(String... params) {
                try {
                    String kakao_id = (String) params[0];
                    String kakao_name = (String) params[1];
                    String jubun = (String) params[2];
                    String phone_num = (String) params[3];
                    String profilepath = (String) params[4];

                    String link = "http://14.63.213.157/dongimg/insert_user.php";

                    String data = URLEncoder.encode("kakao_id", "UTF-8") + "=" + URLEncoder.encode(kakao_id, "UTF-8");
                    data += "&" + URLEncoder.encode("kakao_name", "UTF-8") + "=" + URLEncoder.encode(kakao_name, "UTF-8");
                    data += "&" + URLEncoder.encode("jubun", "UTF-8") + "=" + URLEncoder.encode(jubun, "UTF-8");
                    data += "&" + URLEncoder.encode("phone_num", "UTF-8") + "=" + URLEncoder.encode(phone_num, "UTF-8");
                    data += "&" + URLEncoder.encode("profilepath", "UTF-8") + "=" + URLEncoder.encode(profilepath, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line ;

                    // Read Server Response
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
                    handler.sendEmptyMessage(USER_NETWORK_FAILER);
                } else if(result.trim().equals("failure")){
                    handler.sendEmptyMessage(USER_NETWORK_FAILER);
                }else {
                    handler.sendEmptyMessage(USER_NETWORK_SUCCESS);
                }
            }
        }

        InsertData insertData = new InsertData();
        insertData.execute(kakao_id, kakao_name, jubun, phone_num, profilepath);
    }

    /**
     * 토큰 업데이트 메소드
     * @param kakao_id
     * @param user_token
     */
    public static void updateToUser(String kakao_id, String user_token){

        class UpdateData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String kakao_id = (String) params[0];
                    String user_token = (String) params[1];

                    String link = "http://14.63.213.157/dongimg/update_token.php";
                    String data = URLEncoder.encode("kakao_id", "UTF-8") + "=" + URLEncoder.encode(kakao_id, "UTF-8");
                    data += "&" + URLEncoder.encode("user_token", "UTF-8") + "=" + URLEncoder.encode(user_token, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line ;

                    // Read Server Response
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

        UpdateData updateData = new UpdateData();
        updateData.execute(kakao_id, user_token);
    }

    /**
     * 토큰 삭제 메소드
     * @param kakao_id
     */
    public static void deleteToUser(String kakao_id){
        class DeleteData extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String kakao_id = (String) params[0];

                    String link = "http://14.63.213.157/dongimg/delete_token.php";
                    String data = URLEncoder.encode("kakao_id", "UTF-8") + "=" + URLEncoder.encode(kakao_id, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line ;

                    // Read Server Response
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

        DeleteData updateData = new DeleteData();
        updateData.execute(kakao_id);
    }
}
