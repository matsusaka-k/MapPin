package jp.ac.ecc.se.mappin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SampleConnect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_connect);

        //以下、DB_test.phpに対応するプログラム

        //変数の初期化
        EditText PostSearchBox = findViewById(R.id.PostSearchBox);          //ポストID名検索box
        ImageView sampleUserImage = findViewById(R.id.sampleUserImage);     //ユーザアイコン
        ImageView SamplePostImage = findViewById(R.id.SamplePostImage);     //ポスト画像
        TextView PostTextMultiLine = findViewById(R.id.PostTextMultiLine);  //投稿本文
        Button searchButton = findViewById(R.id.searchButton);              //検索ボタン
        TextView userName = findViewById(R.id.userName);                    //ユーザ名

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // HTTP接続用インスタンス生成
                OkHttpClient client = new OkHttpClient();
                // JSON形式でパラメータを送るようデータ形式を設定
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                // Bodyのデータ(APIに渡したいパラメータを設定) PostSearchBoxに入力されたpost_idを渡す
                String requestBody = "{\"postId\":\"" + PostSearchBox.getText() + "\"}";
                // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
                Request request = new Request.Builder()
                        .url("https://click.ecc.ac.jp/ecc/hige_map_pin/DB_select_test")
                        .post(RequestBody.create(requestBody, mediaType))
                        .build();
/**/
                // リクエスト送信（非同期処理）
                client.newCall(request).enqueue(new Callback() {
/**/
///////////////////////////////// リクエストが失敗した場合の処理を実装 /////////////////////////////////////////////
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        // runOnUiThreadメソッドを使うことでUIを操作することができる。(postメソッドでも可)

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PostTextMultiLine.setText("リクエストが失敗しました: " + e.getMessage());
                            }
                        });
                    }

/////////////////////////////////////////// リクエストが成功した場合の処理を実装//////////////////////////////////
                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        String body = response.body().string();
                        System.out.println("レスポンスを受信しました: " + body);
                        //user_nameの列データを取得する変数
                        String user_name = "";
                        //icon_imgの列データを取得する変数
                        String icon_img = "";
                        //post_imgの列データを取得する変数
                        String post_img = "";
                        //post_textの列データを取得する変数
                        String post_text = "";

                        try {
                            JSONObject json = new JSONObject(body);
                            String list = json.getString("list");
                            JSONArray jsonArray = new JSONArray(list);
                            for(int i = 0; i < jsonArray.length(); i++){
                                user_name += jsonArray.getJSONObject(i).getString("user_name");
                                icon_img += jsonArray.getJSONObject(i).getString("icon_img");
                                post_img += jsonArray.getJSONObject(i).getString("post_img");
                                post_text += jsonArray.getJSONObject(i).getString("post_text");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // postメソッドを使うことでUIを操作することができる。(runOnUiThreadメソッドでも可)
                        String finalUserName = user_name;
                        String finalIconImg = icon_img;
                        String finalPostImg = post_img;
                        String finalPostText = post_text;

                        userName.post(new Runnable() {
                            @Override
                            public void run() {
                                userName.setText(finalUserName);
                                System.out.println("run->" + userName.getText());
                            }
                        });

                        PostTextMultiLine.post(new Runnable() {
                            @Override
                            public void run() {
                                PostTextMultiLine.setText(finalPostText);
                                System.out.println("PostTextMultiLine->" + PostTextMultiLine.getText());
                            }

                        });

                        SamplePostImage.post(new Runnable() {
                            @Override
                            public void run() {
                                //URL post_url = null;
                              //InputStream istreamPost=null;
///**/
//                                HttpURLConnection urlCon = null;
                                try {
//                                    post_url = new URL("https://click.ecc.ac.jp/ecc/hige_map_pin/image/user_post/"+finalPostImg);
//                                    //post_url = new URL("https://icooon-mono.com/i/icon_00275/icon_002751_64.png");
//                                    urlCon = (HttpURLConnection) post_url.openConnection();
//                                    urlCon.setRequestMethod("GET");
//                                    urlCon.setInstanceFollowRedirects(false);
//                                    istreamPost = urlCon.getInputStream();
//                                    Bitmap bmp = BitmapFactory.decodeStream(istreamPost);
                                    Glide.with(getApplicationContext()).load("https://click.ecc.ac.jp/ecc/hige_map_pin/image/user_post/"+finalPostImg).into(SamplePostImage);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
 /**/
                                //InputStream istreamPost = null;
                                /*
                                try {
                                    post_url = new URL("https://click.ecc.ac.jp/ecc/hige_map_pin/image/user_post/"+finalPostImg);
                                    istreamPost = post_url.openStream();
                                    Toast.makeText(getApplicationContext(),"#####",Toast.LENGTH_SHORT).show();
                                    //InputStream stream = context.getContentResolver().openInputStream(uri);
//                                    istreamPost = post_url.openStream();
//                                    InputStream stream = getContentResolver().openInputStream(post_url);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/
                                //Bitmap post_bmp = BitmapFactory.decodeStream(istreamPost);
                                //ビットマップをImageViewに設定
                                //sampleUserImage.setImageBitmap(post_bmp);
                            }
                        });

                        sampleUserImage.post(new Runnable() {
                            @Override
                            public void run() {
                                //画像のURL
                                URL icon_url = null;
                                try {
                                    Glide.with(getApplicationContext()).load("https://click.ecc.ac.jp/ecc/hige_map_pin/image/user_icon/"+finalIconImg).into(sampleUserImage);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
 /**/
                });//callback end
/**/
            }//onClick end
        });

    }
}