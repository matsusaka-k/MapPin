package jp.ac.ecc.se.mappin;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.System.out;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UserPost extends AppCompatActivity {

    //各部品の宣言
    String user_id;
    String user_name;
    String icon_img;
    String post_img;
    String post_text;
    String post_time;
    int goodCount;
    int heartCount;
    int smileCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_post);

        //画面部品の宣言
        ImageView setPostImage = findViewById(R.id.setPostImage);
        EditText postText = findViewById(R.id.postText);
        TextView userNameView = findViewById(R.id.userNameView);
        TextView postTimeView = findViewById(R.id.postTimeView);
        //入力不可にする
        postText.setKeyListener(null);
        ImageView userIconView = findViewById(R.id.userIconView);
        //各リアクションボタン
        ImageButton heartButtton = findViewById(R.id.heartButtton);
        ImageButton goodButton = findViewById(R.id.goodButton);
        ImageButton smilebutton = findViewById(R.id.smilebutton);
        ImageButton backButton = findViewById(R.id.backButton);
        //各リアクションボタンの反応(いいね数みたいな)
        TextView goodCountView = findViewById(R.id.goodCountView);
        TextView heartCountView = findViewById(R.id.heartCountView);
        TextView smileCountView = findViewById(R.id.smileCountView);

        Intent intent = getIntent();


        //Intentで送られたPostの緯度経度を取得
        double longitude = intent.getDoubleExtra("longitude", 0);
        double latitude = intent.getDoubleExtra("latitude", 0);

        //戻るボタン押下で元の画面に(MapsActivity)戻る
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }

        });




        //各リアクションボタンの押下でDBに　1いいね　を追加 また押下の際アニメーション
        goodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goodCount+=1;
                goodCountView.setText("+"+goodCount);

                // HTTP接続用インスタンス生成
                OkHttpClient goodButttonClient = new OkHttpClient();

                // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
                Request goodRequest = new Request.Builder()
                        .url("https://click.ecc.ac.jp/ecc/hige_map_pin/DB_goodReactionChange.php/?" + "latitude=" + latitude + "&" + "longitude=" + longitude)
                        .get()
                        .build();

                // リクエスト送信（非同期処理）

                goodButttonClient.newCall(goodRequest).enqueue(new Callback() {
                    ///////////////////////////////// リクエストが失敗した場合の処理を実装 /////////////////////////////////////////////
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // runOnUiThreadメソッドを使うことでUIを操作することができる。(postメソッドでも可)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("リクエスト失敗");
                                Toast.makeText(getApplicationContext(), "リクエストが失敗しました: " + e.getMessage(), LENGTH_SHORT).show();
                            }
                        });
                    }

                    ///////////////////////////////////////// リクエストが成功した場合の処理を実装//////////////////////////////////
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        out.println("これはgoodの.......................");
                        String body = response.body().string();
                        System.out.println("レスポンスを受信しました: " + body);
                        out.println(response);
                    }
                });
            }
        });





        heartButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                heartCount+=1;
                heartCountView.setText("+"+heartCount);

                // HTTP接続用インスタンス生成
                OkHttpClient heartButttonClient = new OkHttpClient();

                // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
                Request heartRequest = new Request.Builder()
                        .url("https://click.ecc.ac.jp/ecc/hige_map_pin/DB_heaertReactionChange.php/?" + "latitude=" + latitude + "&" + "longitude=" + longitude)
                        .get()
                        .build();

                // リクエスト送信（非同期処理）

                heartButttonClient.newCall(heartRequest).enqueue(new Callback() {
                    ///////////////////////////////// リクエストが失敗した場合の処理を実装 /////////////////////////////////////////////
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // runOnUiThreadメソッドを使うことでUIを操作することができる。(postメソッドでも可)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("リクエスト失敗");
                                Toast.makeText(getApplicationContext(), "リクエストが失敗しました: " + e.getMessage(), LENGTH_SHORT).show();
                            }
                        });
                    }

                    ///////////////////////////////////////// リクエストが成功した場合の処理を実装//////////////////////////////////
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        out.println("これはheartの.......................");
                        String body = response.body().string();
                        System.out.println("レスポンスを受信しました: " + body);
                        out.println(response);
                    }
                });
            }
        });

        smilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                smileCount+=1;
                smileCountView.setText("+"+smileCount);

                // HTTP接続用インスタンス生成
                OkHttpClient smileButttonClient = new OkHttpClient();

                // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
                Request smileRequest = new Request.Builder()
                        .url("https://click.ecc.ac.jp/ecc/hige_map_pin/DB_smileReactionChange.php/?" + "latitude=" + latitude + "&" + "longitude=" + longitude)
                        .get()
                        .build();

                // リクエスト送信（非同期処理）

                smileButttonClient.newCall(smileRequest).enqueue(new Callback() {
                    ///////////////////////////////// リクエストが失敗した場合の処理を実装 /////////////////////////////////////////////
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // runOnUiThreadメソッドを使うことでUIを操作することができる。(postメソッドでも可)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("リクエスト失敗");
                                Toast.makeText(getApplicationContext(), "リクエストが失敗しました: " + e.getMessage(), LENGTH_SHORT).show();
                            }
                        });
                    }

                    ///////////////////////////////////////// リクエストが成功した場合の処理を実装//////////////////////////////////
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        out.println("これはgoodの.......................");
                        String body = response.body().string();
                        System.out.println("レスポンスを受信しました: " + body);
                        out.println(response);
                    }
                });
            }
        });


        // HTTP接続用インスタンス生成
        OkHttpClient client = new OkHttpClient();
        // JSON形式でパラメータを送るようデータ形式を設定
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");


        // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
        Request request = new Request.Builder()
                .url("https://click.ecc.ac.jp/ecc/hige_map_pin/DBUserPost.php/?" + "latitude=" + latitude + "&" + "longitude=" + longitude)
                .get()
                .build();

        // リクエスト送信（非同期処理）

        client.newCall(request).enqueue(new Callback() {
            ///////////////////////////////// リクエストが失敗した場合の処理を実装 /////////////////////////////////////////////
            @Override
            public void onFailure(Call call, IOException e) {
                // runOnUiThreadメソッドを使うことでUIを操作することができる。(postメソッドでも可)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("リクエスト失敗");
                        Toast.makeText(getApplicationContext(), "リクエストが失敗しました: " + e.getMessage(), LENGTH_SHORT).show();
                    }
                });
            }

            ///////////////////////////////////////// リクエストが成功した場合の処理を実装//////////////////////////////////
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                System.out.println("レスポンスを受信しました: " + body);

                out.println(response);
                try {
                    String str = "{\"name\":\"John\",\"age\":\"30\"}";
                    JSONObject json = new JSONObject(body);
                    String list = json.getString("list");
                    JSONArray jsonArray = new JSONArray(list);

                    user_id = jsonArray.getJSONObject(0).getString("user_id");
                    user_name = jsonArray.getJSONObject(0).getString("user_name");
                    icon_img = jsonArray.getJSONObject(0).getString("icon_img");
                    post_img = jsonArray.getJSONObject(0).getString("post_img");
                    post_text = jsonArray.getJSONObject(0).getString("post_text");
                    post_time = jsonArray.getJSONObject(0).getString("post_time");
                    goodCount = jsonArray.getJSONObject(0).getInt("good");
                    smileCount = jsonArray.getJSONObject(0).getInt("smile");
                    heartCount = jsonArray.getJSONObject(0).getInt("heart");


                    //余分なバッククォートをきりとり
                    icon_img = icon_img.replace("'","");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //投稿画像のセット
                        Glide.with(getApplicationContext()).load("https://click.ecc.ac.jp/ecc/hige_map_pin/image/user_post/"+post_img).into(setPostImage);
                        //アイコン画像のセット
                        // Bitmapを円形に切り抜くメソッド
                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load("https://click.ecc.ac.jp/ecc/hige_map_pin/image/user_icon/"+icon_img)
                                .into(new BitmapImageViewTarget(userIconView) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        userIconView.setImageDrawable(circularBitmapDrawable);
                                    }
                                });

                        //ユーザ名のセット
                        userNameView.setText(user_name);
                        //投稿時間のセット
                        postTimeView.setText(post_time);
                        //投稿文章のセット
                        postText.setText(post_text);
                        //各リアクションの数をセット
                        goodCountView.setText("+"+goodCount);
                        heartCountView.setText("+"+heartCount);
                        smileCountView.setText("+"+smileCount);
                    }
                });
            }
        });
    }
}

