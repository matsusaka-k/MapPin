package jp.ac.ecc.se.mappin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Account extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //画面部品の宣言
        ImageView settingButton = findViewById(R.id.setting_image);

        SharedPreferences sharedPreferences;

        //テストデータ
        String getUserId = "akaoni45";
        String user_id = "akaoni45";

        //遷移元からアカウントの情報(user_id)を受け取る
        //String getUserId = getIntent().getStringExtra("UserId");

        //sharedPreferences = getSharedPreferences("loginPref",MODE_PRIVATE);

        //変数の宣言
        //String user_id = sharedPreferences.getString("userId","");

        //見ている画面が自分のアカウント画面の場合画面部品を表示
        if(getUserId.equals(user_id)){
            settingButton.setVisibility(View.VISIBLE);
        }



    }
}