package jp.ac.ecc.se.mappin;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Override
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

        SharedPreferences pref = getSharedPreferences("saveData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();


        // クリックリスナーの設定
        post_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 投稿ボタンのクリックを処理
                String location = location_Text.getText().toString();
                String comment = comment_Text.getText().toString();

                // データを投稿するためのロジックを追加（例: サーバーに送信、データベースに保存）

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
        });
    }
}
