package jp.ac.ecc.se.mappin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText userIdEditText;
    EditText passwordEditText;
    Button loginButton;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userIdEditText = findViewById(R.id.UserId);
        passwordEditText = findViewById(R.id.Password);
        loginButton = findViewById(R.id.LoginButton);

        preferences = getSharedPreferences("loginPref", MODE_PRIVATE);

        loginButton.setOnClickListener(v -> {
            // 入力されたユーザーIDとパスワードを取得
            String enteredUserId = userIdEditText.getText().toString();
            String enteredPassword = passwordEditText.getText().toString();

            // SharedPreferencesから保存されているユーザー情報を取得
            String storedUserId = preferences.getString("userId", "");
            String storedPassword = preferences.getString("password", "");

            // 入力されたユーザーIDとパスワードと、保存されている情報を比較
            if (enteredUserId.equals(storedUserId) && enteredPassword.equals(storedPassword)) {
                // ログイン成功時の処理
                startHomeActivity();
            } else {
                // ログイン失敗時の処理
                showError("ログインに失敗しました");
            }
        });
    }

    private void startHomeActivity() {
        Intent intent = new Intent(Login.this, Home.class);
        finish(); // ログイン画面を終了
        startActivity(intent);
    }

    private void showError(String message) {
        // エラーメッセージを表示する処理を実装
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
