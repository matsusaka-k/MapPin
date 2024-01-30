package jp.ac.ecc.se.mappin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class Account extends AppCompatActivity {

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


//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        RecyclerView.Adapter adapter = new SampleAdapter(createCellDate());
//
//        recyclerView.setAdapter(adapter);
//
//        DividerItemDecoration decorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(decorator);



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