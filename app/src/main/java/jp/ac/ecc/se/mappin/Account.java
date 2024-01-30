package jp.ac.ecc.se.mappin;


import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.System.out;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Account extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        //画面部品の宣言
        ImageView settingButton = findViewById(R.id.setting_image);
        ImageView icon_image = findViewById(R.id.icon_image);
        EditText name_text =findViewById(R.id.name_text);
        TextView userid_text = findViewById(R.id.userid_text);
        EditText total_text = findViewById(R.id.total_text);
        ImageView good_image = findViewById(R.id.good_image);
        ImageView heart_image = findViewById(R.id.heart_image);
        ImageView smile_image = findViewById(R.id.smile_image);
        TextView good = findViewById(R.id.number_good);
        TextView heart = findViewById(R.id.number_heart);
        TextView smile = findViewById(R.id.number_smile);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        String userID = "akaoni45";





        //-------------------リサイクラービュー↓-----------------------------------------------
        // HTTP接続用インスタンス生成
        OkHttpClient client = new OkHttpClient();
        // JSON形式でパラメータを送るようデータ形式を設定
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
        Request request = new Request.Builder()
                .url("https://click.ecc.ac.jp/ecc/hige_map_pin/DB_select_account.php/?")//+"user_id=" + userID)
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
                        e.printStackTrace(); // エラースタックトレースを表示
                        out.println("リクエスト失敗");
                        Toast.makeText(getApplicationContext(), "リクエストが失敗しました: " + e.getMessage(), LENGTH_SHORT).show();
                    }
                });
            }
            ///////////////////////////////////////// リクエストが成功した場合の処理を実装//////////////////////////////////
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                out.println("レスポンスを受信しました: " + body);
                //post_img(ユーザが投稿した画像のパス
                //画像の部分をコメントしてあるが画像もする
//                List<String> postImgList = new ArrayList<>();
                //post_text(ユーザが投稿したテキスト)
                List<String> postTextList = new ArrayList<>();
                //post_time(ユーザが投稿した時間)
                List<String> postTimeList = new ArrayList<>();
                //good(ユーザによる「いいね」の数)
                List<Integer> goodList = new ArrayList<>();
                //smile(ユーザによる「笑顔」の数)
                List<Integer> smileList = new ArrayList<>();
                //heart(ユーザによる「ハート」の数)
                List<Integer> heartList = new ArrayList<>();
                try {
                    JSONObject json = new JSONObject(body);
                    String list = json.getString("list");
                    JSONArray jsonArray = new JSONArray(list);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject postData = jsonArray.getJSONObject(i);
                        // レスポンスからデータを取得し、それぞれのリストに追加
                        //画像の部分をコメントしてあるが画像もする
                        //postImgList.add(postData.getString("post_img"));
                        postTextList.add(postData.getString("post_text"));
                        postTimeList.add(postData.getString("post_time"));
                        goodList.add(postData.getInt("good"));
                        smileList.add(postData.getInt("smile"));
                        heartList.add(postData.getInt("heart"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // onResponseメソッド内でUIスレッド上で処理を行う
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // データの作成
                        List<MyDataModel> dataList = new ArrayList<>();


                        // 取得したデータをループしてMyDataModelに追加
                        for (int i = 0; i < postTextList.size(); i++) {
                            MyDataModel data = new MyDataModel(
                                    //画像も追加する
                                  postTextList.get(i),
                                  goodList.get(i),
                                  smileList.get(i),
                                  heartList.get(i),
                                  postTimeList.get(i)
                            );
                            dataList.add(data);
                        }

                        // アダプターの設定
                        MyAdapter adapter = new MyAdapter(dataList);
                        // レイアウトマネージャーの設定
                        recyclerView.setLayoutManager(new LinearLayoutManager(Account.this));
                        recyclerView.setAdapter(adapter);

                    }
                });

            }
        });
    }
}
