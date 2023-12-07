package jp.ac.ecc.se.mappin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Start extends AppCompatActivity {

    Button loginbutton;
    Button createbutton;
    ImageView imageView;
    SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SharedPreferencesからログイン情報を取得
        loginPreferences = getSharedPreferences("loginPref", MODE_PRIVATE);
        boolean isLoggedIn = loginPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // ログイン情報がある場合は直接Home画面へ遷移
            startHomeActivity();
            return; // onCreateメソッドを終了する
        }

        // 画面の表示
        setContentView(R.layout.activity_start);

        loginbutton = findViewById(R.id.loginButton);
        createbutton = findViewById(R.id.createButton);
        imageView = findViewById(R.id.logoView);

        // ログインボタンクリック時の処理
        loginbutton.setOnClickListener(v -> {
            // ログイン画面へ遷移
            Intent intent = new Intent(Start.this, Login.class);
            startActivity(intent);
        });

        // 新規登録画面へ遷移
        createbutton.setOnClickListener(v -> {
            Intent intent = new Intent(Start.this, CreateAcount.class);
            startActivity(intent);
        });
    }

    // Home画面へ遷移
    private void startHomeActivity() {
        Intent intent = new Intent(Start.this, Home.class);
        finish();
        startActivity(intent);
    }
}
