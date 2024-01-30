package jp.ac.ecc.se.mappin;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Setting extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1; // リクエストコード

    ImageView iconImage;
    EditText nameEditText;
    Bitmap selectedImageBitmap; // 選択された画像のビットマップを保持

    //テストデータ
    //String user_id = "akaoni45";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        iconImage = findViewById(R.id.icon_image2);
        nameEditText = findViewById(R.id.name_text2);
        Button changeButton = findViewById(R.id.button);
        FloatingActionButton add_icon = findViewById(R.id.add_icon);
        Button AccountButton = findViewById(R.id.Accountbutton);
        Button HomeButton = findViewById(R.id.Homebutton);
        Button SettingButton = findViewById(R.id.Settingbutton);

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


        InputStream input = null;
        try {
            input = this.openFileInput("image.png");
        } catch (FileNotFoundException e) {
            // エラー処理
        }
        Bitmap image = BitmapFactory.decodeStream(input);
        iconImage.setImageBitmap(image);

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("loginPref",MODE_PRIVATE);
        String user_name = sharedPreferences.getString("user_name","赤鬼");
        nameEditText.setText(user_name);


        String user_id = sharedPreferences.getString("userId","akaoni45");

        add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ギャラリーアプリを開いて画像を選択
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        // 変更ボタンのクリックリスナー
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ユーザが入力した値を取得
                String newUserName = nameEditText.getText().toString();

                // HTTP接続用インスタンス生成
                OkHttpClient client = new OkHttpClient();
                // JSON形式でパラメータを送るようデータ形式を設定
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
                Request request = new Request.Builder()
                        .url("https://click.ecc.ac.jp/ecc/hige_map_pin/DB_select_setting.php/?" + "user_id="+user_id+"&user_name="+newUserName)
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
                    }
                });
                Intent homeintent = new Intent(Setting.this,Home.class);
                startActivity(homeintent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
//                String imagePath = getImageFilePath(selectedImageUri);
                try {
                    // 選択した画像を読み込む
                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                    // 画像を円形に切り抜く
                    Bitmap circularBitmap = getRoundedBitmap(selectedImage);

                    // ImageViewに円形画像を設定
                    iconImage.setImageBitmap(circularBitmap);

                    //drawableにbitmap形式の画像を保存
                    FileOutputStream out = null;
                    try {
                        // openFileOutputはContextのメソッドなのでActivity内ならばthisでOK
                        out = this.openFileOutput("image.png", this.MODE_PRIVATE);
                        circularBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    } catch (FileNotFoundException e) {
                        // エラー処理
                    } finally {
                        if (out != null) {
                            out.close();
                            out = null;
                        }
                    }
//                    uploadImage(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    // Bitmapを円形に切り抜くメソッド
    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int min = Math.min(width, height);

        // 出力用のBitmapを作成
        Bitmap output = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Rect rect = new Rect(0, 0, min, min);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 円形に切り抜いた画像を描画
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
