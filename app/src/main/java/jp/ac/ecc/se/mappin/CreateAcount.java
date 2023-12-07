package jp.ac.ecc.se.mappin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class CreateAcount extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1; // リクエストコード

    ImageView imageView; // 画像を表示するためのImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount);

        EditText ID_text = findViewById(R.id.ID_text);
        EditText Name_text = findViewById(R.id.Name_text);
        EditText Address_text = findViewById(R.id.Address_text);
        EditText Password_text = findViewById(R.id.Password_text);
        Button createButton = findViewById(R.id.getIcon_Button);

        // アクションバーの背景色を設定
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.blue)));

        ImageView icon_image = findViewById(R.id.Icon_image); // アイコン画像を表示するImageView
        FloatingActionButton add_icon = findViewById(R.id.Add_icon); // 画像を選択するボタン

        // ボタンのクリックリスナーを修正
        add_icon.setOnClickListener(new View.OnClickListener() {
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

        /**
         *  ユーザが入力したデータが正しく入力されているか確認
         */

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ユーザID
                if (!isValidID(ID_text.getText().toString().trim())) {
                    ID_text.setError("IDは半角英数字, 6文字以上10文字以内で入力");
                } else {
                    ID_text.setError(null);
                }

                // ユーザ名
                if (!isValidName(Name_text.getText().toString().trim())) {
                    Name_text.setError("ユーザ名はひらがな,半角英数字, 6文字以上10文字以内で入力");
                } else {
                    Name_text.setError(null);
                }

                // メールアドレス
                if (!isValidAddress(Address_text.getText().toString().trim())) {
                    Address_text.setError("正しいメールアドレスを入力してください");
                } else {
                    Address_text.setError(null);
                }

                // パスワード
                if (!isValidPassword(Password_text.getText().toString().trim())) {
                    Password_text.setError("パスワードは半角英数字, 8文字以上20文字以内で入力");
                } else {
                    Password_text.setError(null);
                }

                // 空欄の時
                if (ID_text.getText().toString().trim().isEmpty()) {
                    ID_text.setError("入力欄に空きがあります");
                }

                if (Name_text.getText().toString().trim().isEmpty()) {
                    Name_text.setError("入力欄に空きがあります");
                }

                if (Address_text.getText().toString().trim().isEmpty()) {
                    Address_text.setError("入力欄に空きがあります");
                }

                if (Password_text.getText().toString().trim().isEmpty()) {
                    Password_text.setError("入力欄に空きがあります");
                }

                // 入力が正しい場合
                if (isValidID(ID_text.getText().toString().trim()) &&
                        isValidName(Name_text.getText().toString().trim()) &&
                        isValidAddress(Address_text.getText().toString().trim()) &&
                        isValidPassword(Password_text.getText().toString().trim())) {

                    // ユーザ情報を保存
                    saveUserData(ID_text.getText().toString().trim(), Password_text.getText().toString().trim());

                    // Home画面へ遷移
                    startHomeActivity();
                }
            }
        });
    }

    private boolean isValidID(String inputText) {
        return inputText.matches("^[A-Za-z0-9]{6,10}$");
    }

    private boolean isValidName(String inputText) {
        return inputText.matches("[ぁ-んァ-ン一-龯a-zA-Z0-9]{6,10}");
    }

    private boolean isValidAddress(String inputText) {
        return inputText.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    }

    private boolean isValidPassword(String inputText) {
        return inputText.matches("^[A-Za-z0-9]{8,20}$");
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
    private void saveUserData(String userID, String password) {
        // SharedPreferencesにユーザー情報を保存
        SharedPreferences preferences = getSharedPreferences("loginPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId", userID);
        editor.putString("password", password);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void startHomeActivity() {
        // Home画面へ遷移
        Intent intent = new Intent(CreateAcount.this, Home.class);
        startActivity(intent);
        finish();  // 新規登録画面を終了
    }
}


