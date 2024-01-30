package jp.ac.ecc.se.mappin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Account extends ViewModel {


    private final MutableLiveData<String> mText;

    public Account() {
        mText = new MutableLiveData<>();
        mText.setValue("アカウント画面");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

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
