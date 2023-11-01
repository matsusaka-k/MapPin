package jp.ac.ecc.se.mappin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

public class CreateAcount extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1; // リクエストコード

    ImageView imageView; //画像を表示するためのImageview
    FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount);

        Object objects;
        //アクションバーの背景色を設定
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.blue)));

        ImageView icon_image = findViewById(R.id.Icon_image); // アイコン画像を表示するImageView
        FloatingActionButton Add_icon = findViewById(R.id.Add_icon); // 画像を選択するボタン

        // ボタンのクリックリスナーを修正
        Add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ギャラリーアプリを開いて画像を選択
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });


        // ImageViewを初期化
        imageView = findViewById(R.id.Icon_image);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    // 選択した画像を読み込む
                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                    // 画像を円形に切り抜く
                    Bitmap circularBitmap = getRoundedBitmap(selectedImage);

                    // ImageViewに円形画像を設定
                    imageView.setImageBitmap(circularBitmap);
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