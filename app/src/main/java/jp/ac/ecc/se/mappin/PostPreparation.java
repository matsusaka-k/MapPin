package jp.ac.ecc.se.mappin;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.System.out;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
        Button AccountButton = findViewById(R.id.Accountbutton);
        Button HomeButton = findViewById(R.id.Homebutton);
        Button SettingButton = findViewById(R.id.Settingbutton);

        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Activityをリスタートさせる
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                finish();
                startActivity(intent);

            }
        });
        AccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //アカウント画面に遷移
                Intent intent = new Intent(getApplicationContext(), Account.class);
                finish();
                startActivity(intent);

            }
        });

        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ホーム画面に遷移
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                finish();
                startActivity(intent);

            }
        });


        SettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //設定画面に遷移
                Intent intent = new Intent(getApplicationContext(), Setting.class);
                finish();
                startActivity(intent);

            }
        });
        //インテント(home)から位置データ獲得
        Intent intent = getIntent();
        //今はsampleで値が入っている状態
        double latitude = intent.getDoubleExtra("latitude",0.0 );
        double longitude = intent.getDoubleExtra("longitude",0.0);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        // 以下は例として位置情報をログに出力するコード
        Log.d("PostPreparation", "Received latitude: " + latitude + ", longitude: " + longitude);
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

                //以下、DBに情報を送る処理

                // 投稿ボタンのクリックを処理
                String comment = comment_Text.getText().toString();
                String imageData = imageUri.toString();
                // HTTP接続用インスタンス生成
                OkHttpClient client = new OkHttpClient();
                // JSON形式でパラメータを送るようデータ形式を設定
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
                Request request = new Request.Builder()
                        .url("https://click.ecc.ac.jp/ecc/hige_map_pin/DB_select_postinfo.php/?" + "user_id=" + userID + "&" + "post_text=" + comment + "&" + "latitude=" + latitude + "&" + "longitude=" + longitude + "&" + "post_img=" + imageData)
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
                                Log.d("DEBUG", "err = " + e.getMessage());
                            }
                        });
                    }

                    ///////////////////////////////////////// リクエストが成功した場合の処理を実装//////////////////////////////////
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        out.println("レスポンスを受信しました: " + response.body());

//                        //Uri型のimageDataをFile型に変換
//                        Cursor cursor = getContentResolver().query(imageUri,null,null,null,null);
//                        cursor.moveToFirst();
//                        @SuppressLint("Range") String fileNameString = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
//                        File fileName = new File(fileNameString);
//                        imageUploadMethod(fileName);

                        //撮影した写真をbitmap型に変える
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                        //Bitmap画像を0.5倍の大きさに縮小
                        Matrix matrix = new Matrix();
                        matrix.postScale(0.5f, 0.5f);
                        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                        //Bitmap画像をbase64に変換
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        String bites64Encoded = Base64.getEncoder().encodeToString(bytes);

                        out.println("画像データをbase64に変換したものです=" + bites64Encoded);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                byte[] hoge = Base64.getDecoder().decode(bites64Encoded);
                                out.println("hogeの中身=" + hoge);
                                Bitmap huga = BitmapFactory.decodeByteArray(hoge, 0, hoge.length);
                                post_Image.setImageBitmap(huga);
                            }
                        });

                        imageUploadMethod(bites64Encoded);

                        //撮った画像をjpg形式で保存
                        //ByteArrayOutputStream jpg = new ByteArrayOutputStream();

                    }
                });


                 Intent homeintent = new Intent(PostPreparation.this, MapsActivity.class);
                startActivity(homeintent);
            }
        });

//        cancel_Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // キャンセルボタンのクリックを処理
//                finish(); // アクティビティを終了
//            }
//        });


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

    public static void imageUploadMethod(String imageData) throws IOException {

        // HTTP接続用インスタンス生成
        OkHttpClient client = new OkHttpClient();
        // JSON形式でパラメータを送るようデータ形式を設定
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        // Bodyのデータ(APIに渡したいパラメータを設定) PostSearchBoxに入力されたpost_idを渡す
        String requestBody = "{\"imageData\":\"" + imageData + "\"}";
        // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
        Request request = new Request.Builder()
                .url("https://click.ecc.ac.jp/ecc/hige_map_pin/image/ImageFileUpload.php")
                .post(RequestBody.create(requestBody, mediaType))
                .build();

        // リクエスト送信（非同期処理）
        client.newCall(request).enqueue(new Callback() {

            ///////////////////// リクエストが失敗した場合の処理を実装 /////////////////////////////////
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                out.println("リクエスト失敗");
            }

            ///////////////////// リクエストが成功した場合の処理を実装//////////////////////////////////
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                out.println("リクエスト成功");
            }
        });

    }
}
