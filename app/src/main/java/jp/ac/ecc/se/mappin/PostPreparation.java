package jp.ac.ecc.se.mappin;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.System.out;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PostPreparation extends AppCompatActivity {
    final int CAMERA_RESULT = 100;
    Uri imageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
            ImageView post_Image = findViewById(R.id.post_image);
            post_Image.setImageURI(imageUri);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_preparation);

        Button post_Button = findViewById(R.id.post_Button);
        Button cancel_Button = findViewById(R.id.cansel_Button);
        TextView location_Text = findViewById(R.id.location_text);
        TextView comment_Text = findViewById(R.id.comment_text);
        ImageView location_Image = findViewById(R.id.location_image);
        ImageView post_Image = findViewById(R.id.post_image);
        Button camera_Button = findViewById(R.id.camera_Button);

        //インテント(home)から位置データ獲得
        Intent intent = getIntent();
        //今はsampleで値が入っている状態
        double latitude = intent.getDoubleExtra("latitude", 34.70701210088185);
        double longitude = intent.getDoubleExtra("longitude", 135.5023673548298);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        //location_textを更新
        if (location_Text != null) {
            String locationInfo = "緯度: " + latitude + "\n経度: " + longitude;
//            location_Text.setText(locationInfo);
            // 逆ジオコーディングを実行して住所を取得
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    String addressText = "住所: " + address.getAddressLine(0);
                    location_Text.append("\n" + addressText);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String userID = "akaoni45";

        // クリックリスナーの設定
        post_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 投稿ボタンのクリックを処理
                String comment = comment_Text.getText().toString();
                String imageData = imageUri.toString();
                // HTTP接続用インスタンス生成
                OkHttpClient client = new OkHttpClient();
                // JSON形式でパラメータを送るようデータ形式を設定
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                //string型でしかパラメータは送れない
                //String  mylatitude = String.valueOf(latitude);
                //String mylongitude = String.valueOf(longitude);
//                out.println("https://click.ecc.ac.jp/ecc/hige_map_pin/DB_select_postinfo.php/?"+"user_id="+userID + "&" + "post_text=" + comment + "&" + "latitude=" + latitude + "&" + "longitude=" + longitude + "&" + "post_img=" + imageData);
                // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
                Request request = new Request.Builder()
                        .url("https://click.ecc.ac.jp/ecc/hige_map_pin/DB_select_postinfo.php/?"+"user_id="+userID + "&" + "post_text=" + comment + "&" + "latitude=" + latitude + "&" + "longitude=" + longitude + "&" + "post_img=" + imageData)
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
                                out.println("リクエスト失敗");
                                Toast.makeText(getApplicationContext(), "リクエストが失敗しました: " + e.getMessage(), LENGTH_SHORT).show();
                            }
                        });
                    }

                    ///////////////////////////////////////// リクエストが成功した場合の処理を実装//////////////////////////////////
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        out.println("レスポンスを受信しました: " + response.body());
                    }
                });

                Intent homeintent = new Intent(PostPreparation.this,Home.class);
                startActivity(homeintent);
            }
        });

        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // キャンセルボタンのクリックを処理
                finish(); // アクティビティを終了
            }
        });


        camera_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // != PackageManager.PERMISSION_GRANTEDなので、許可されていないという条件分岐
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // 許可されていない場合、リクエストを行う
                    ActivityCompat.requestPermissions(PostPreparation.this, new String[]{android.Manifest.permission.CAMERA}, 100);
                } else {
                    // 許可されていれば、カメラ起動
                    startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
                    // カメラを起動して写真を撮影
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //ファイル名
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String fileName = "Training_" + timestamp + ".jpg";
                    //パラメータ設定
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    //保存場所
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    // カメラのIntentにパラメータセット
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CAMERA_RESULT);
                }
            }
        });
    }
}