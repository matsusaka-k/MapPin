package jp.ac.ecc.se.mappin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    Button button;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView errUserID;
    int errFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        button = findViewById(R.id.loginButton);
        button = findViewById(R.id.createButton);
//        EditText inputUserID = findViewById(R.id.inputUserID); // ユーザIDのEditText
//        EditText inputPassword = findViewById(R.id.inputPassword); // パスワードのEditText
//        errUserID = findViewById(R.id.errUserID); // エラーメッセージ表示用TextView

        // ログインデータ取得
        preferences = getSharedPreferences("logPref", MODE_PRIVATE);
        editor = preferences.edit();
        String savedUserID = preferences.getString("userID", ""); // キーとデフォルト値を指定
        String savedPassword = preferences.getString("password", ""); // キーとデフォルト値を指定

//        // ログインボタンのクリックアクション
//        findViewById(R.id.loginButton).setOnClickListener(v -> {
//            String userID = inputUserID.getText().toString();
//            String password = inputPassword.getText().toString();

//            // ユーザIDの確認（2 0文字以内の半角英数字）
//            if (!userID.matches("^[A-Za-z0-9]+$") || !userID.matches(".{6,20}")) {
//                errUserID.setText("半角英数字, 6文字以上20文字以内で入力");
//                errFlag++;
//                return;
//            }
//
//            // パスワードの確認（8文字以上14文字の半角英数字）
//            if (!password.matches("^[A-Za-z0-9]+$") || !password.matches(".{8,14}")) {
//                errUserID.setText("半角英数字, 8文字以上14文字以内で入力");
//                errFlag++;
//                return;
//            }
//
//            // ログイン処理（ここではダミーの正しいユーザー名とパスワードでログイン）
//            if (userID.equals(savedUserID) && password.equals(savedPassword)) {
//                // ログイン成功時のアクションをここに追加
//
//                // ホーム画面に遷移
//                Intent intent = new Intent(Login.this, Home.class);
//                startActivity(intent);
//            } else {
//                // ログインエラー処理
//                errUserID.setText("ユーザIDまたはパスワードが間違っています");
//                errFlag++;
//            }
//        });
    }
}
