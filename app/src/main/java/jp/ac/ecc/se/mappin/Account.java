package jp.ac.ecc.se.mappin;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class Account extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);



        //画面部品の宣言
        ImageView settingButton = findViewById(R.id.setting_image);
        ImageView icon_image = findViewById(R.id.icon_image);
        EditText name_text =findViewById(R.id.name_text);
        TextView userid_text = findViewById(R.id.userid_text);
        EditText total_text = findViewById(R.id.total_text);
        ImageView good_image = findViewById(R.id.good_image);
        ImageView heart_image = findViewById(R.id.heart_image);
        ImageView smile_image = findViewById(R.id.smile_image);
        TextView good = findViewById(R.id.number_good);
        TextView heart = findViewById(R.id.number_heart);
        TextView smile = findViewById(R.id.number_smile);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        Button AccountButton = findViewById(R.id.Accountbutton);
        Button HomeButton = findViewById(R.id.Homebutton);
        Button SettingButton = findViewById(R.id.Settingbutton);

        //マップ上のピンをクリア
        //mMap.clear();

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


    }
}











//
//public class Account extends ViewModel {
//
//        //画面部品の宣言
//        ImageView settingButton = findViewById(R.id.setting_image);
//        ImageView icon_image = findViewById(R.id.icon_image);
//        EditText name_text =findViewById(R.id.name_text);
//        TextView userid_text = findViewById(R.id.userid_text);
//        EditText total_text = findViewById(R.id.total_text);
//        ImageView good_image = findViewById(R.id.good_image);
//        ImageView heart_image = findViewById(R.id.heart_image);
//        ImageView smile_image = findViewById(R.id.smile_image);
//        TextView good = findViewById(R.id.number_good);
//        TextView heart = findViewById(R.id.number_heart);
//        TextView smile = findViewById(R.id.number_smile);
//        RecyclerView recyclerView = findViewById(R.id.recyclerview);
//
//
////        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
////        recyclerView.setLayoutManager(layoutManager);
////
////        RecyclerView.Adapter adapter = new SampleAdapter(createCellDate());
////
////        recyclerView.setAdapter(adapter);
////
////        DividerItemDecoration decorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
////        recyclerView.addItemDecoration(decorator);
//
//
//    private final MutableLiveData<String> mText;
//
//    public Account() {
//        mText = new MutableLiveData<>();
//        mText.setValue("アカウント画面");
//    }
//
//    public LiveData<String> getText() {
//        return mText;
//    }
//}

        /*　コーディングする上での注意事項
            ・エミュレータは Pixel6 API33 を使用すること
            ・命名規則
                変数宣言時は、最初の単語は小文字　続く単語の先頭は大文字にすること _(アンダーバー)を使って表記しても可
                (例)inuKawaii または inu_kawaii
                定数の宣言は、大文字とアンダースコア(_)のみを用いる
                (例)ACTION_ PERMISSION_
            ・コメントを必ずつけるように
                変数宣言時やメソッドを書いたときはその前後に必ずコメントをつけるように
            ・ネットで拾ったソースを使ってもいいが、内容を理解してからペーストするように
            ・設定ファイル(Gradle Script直下のフォルダ)は絶対に触らないこと
            ・画像はdrawableに収納すること
            ・ファイル追加時は一言松坂にください
         */
//
//
//        Button button = findViewById(R.id.backbutton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        //画面部品の宣言
//        ImageView settingButton = findViewById(R.id.setting_image);
//
//        SharedPreferences sharedPreferences;
//
//        //テストデータ
//        String getUserId = "akaoni45";
//        String user_id = "akaoni45";
//
//        //遷移元からアカウントの情報(user_id)を受け取る
//        //String getUserId = getIntent().getStringExtra("UserId");
//
//        //sharedPreferences = getSharedPreferences("loginPref",MODE_PRIVATE);
//
//        //変数の宣言
//        //String user_id = sharedPreferences.getString("userId","");
//
//        //見ている画面が自分のアカウント画面の場合画面部品を表示
//        if(getUserId.equals(user_id)){
//            settingButton.setVisibility(View.VISIBLE);
//        }
//
//
//
//    }
//}

//
//        Button follower_button  = findViewById(R.id.follower_button);
//        Button following_button = findViewById(R.id.following_button);
//
//
//        follower_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(FollowList);
//            }
//        });
