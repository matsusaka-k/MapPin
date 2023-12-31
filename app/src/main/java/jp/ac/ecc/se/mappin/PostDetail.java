package jp.ac.ecc.se.mappin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PostDetail extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

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
    }
}