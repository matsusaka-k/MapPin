package jp.ac.ecc.se.mappin;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.System.out;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends FragmentActivity implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap mMap;
    private Marker marker;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    //TextView textView = findViewById(R.id.textView);
    double Mylatitude; //自身のデバイスの緯度
    double Mylongitude; //自身のデバイスの経度
    String baseURL = "https://click.ecc.ac.jp/ecc/hige_map_pin/DB_ShowMap.php"; //url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button PostButton = findViewById(R.id.PostButton);
        PostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,PostPreparation.class);
                startActivity(intent);
            }
        });

        //マップ上のピンをクリア
        //mMap.clear();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    //位置情報
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Mylatitude = location.getLatitude();
                    Mylongitude = location.getLongitude();
                    // ここで取得した緯度(latitude)と経度(longitude)を使用できます
                    // 例えば、Toastで表示する場合
                    Toast.makeText(Home.this, "緯度: " + Mylatitude + ", 経度: " + Mylongitude, Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(Home.this);
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "位置情報の許可が拒否されました 許可してください", LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng tokyo = new LatLng(35.6804, 139.7690);

        //現在の位置情報の取得
        if (currentLocation != null) {
            LatLng sydney = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("現在地"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // ピンがクリックされたときの処理
                    getLocationInfo(marker.getPosition());
                    Intent UserPostIntent = new Intent(getApplicationContext(),UserPost.class);
                    Intent MyAccountintent = new Intent(getApplicationContext(),Account.class);
                    //もし押されたマーカーが自身の現在地の場合、Account画面に遷移
                    if((sydney.latitude == marker.getPosition().latitude)&&(sydney.longitude == marker.getPosition().longitude)){
                        out.println("yyyyyyyyyyyyyyyyyyyyyyyyy");
                        startActivity(MyAccountintent);
                    }else {
                        startActivity(UserPostIntent);
                        return false;
                    }
                    return false;
                }
            });
            //ためしに
            //mMap.setOnPoiClickListener(this);

            //ここでカメラの位置を調整できる
            CameraUpdate locationUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 15.0f);
            mMap.moveCamera(locationUpdate);
//        }
//    }

            //表示するピンを絞る
            //デバイスの現在地から半径2.5kmの範囲にあるピンのみを表示させる
            double topLatitude = Mylatitude + 0.02247491451263;
            double bottomLatitude = Mylatitude - 0.02247491451263;
            double rightLongitude = Mylongitude + 0.0274402060767;
            double leftLongitude = Mylongitude - 0.0274402060767;


            // HTTP接続用インスタンス生成
            OkHttpClient client = new OkHttpClient();
            // JSON形式でパラメータを送るようデータ形式を設定
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
            Request request = new Request.Builder()
                    .url("https://click.ecc.ac.jp/ecc/hige_map_pin/DB_ShowMap.php/?"+"topLatitude="+topLatitude+"&"+"bottomLatitude="+bottomLatitude+"&"+"rightLongitude="+rightLongitude+"&"+"leftLongitude="+leftLongitude)
                    .get()
                    .build();

            // リクエスト送信（非同期処理）

            client.newCall(request).enqueue(new Callback() {
                ///////////////////////////////// リクエストが失敗した場合の処理を実装 /////////////////////////////////////////////
                @Override
                public void onFailure(Call call, IOException e) {
                    // runOnUiThreadメソッドを使うことでUIを操作することができる。(postメソッドでも可)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("リクエスト失敗");
                            Toast.makeText(getApplicationContext(), "リクエストが失敗しました: " + e.getMessage(), LENGTH_SHORT).show();
                        }
                    });
                }
                ///////////////////////////////////////// リクエストが成功した場合の処理を実装//////////////////////////////////
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    System.out.println("レスポンスを受信しました: " + body);
                    out.println("https://click.ecc.ac.jp/ecc/hige_map_pin/DB_ShowMap.php/?"+"topLatitude="+topLatitude+"&"+"bottomLatitude="+bottomLatitude+"&"+"rightLongitude="+rightLongitude+"&"+"leftLongitude="+leftLongitude);

                    //post_img(ユーザが投稿した画像のパス)を保存するリスト
                    List<String> postImgList = new ArrayList<>();
                    //user_name(ユーザの名前)
                    List<String> userNameList = new ArrayList<>();
                    //userId(ユーザID)
                    List<String> userIdList = new ArrayList<>();
                    //latitude(他ユーザのポストを投稿した場所:緯度)
                    List<Double> latitudeList = new ArrayList<>();
                    //longitude(他ユーザのポストを投稿した場所:緯度)
                    List<Double> longitudeList = new ArrayList<>();

                    try {
                        out.println("aaaaa");
                        String str = "{\"name\":\"John\",\"age\":\"30\"}";
                        JSONObject json = new JSONObject(body);
                        out.println("bbbbb");
                        String list = json.getString("list");
                        out.println("ccccc");
                        JSONArray jsonArray = new JSONArray(list);
                        out.println("ddddd");
                        int list_length =jsonArray.length();
                        out.println(jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            postImgList.add(jsonArray.getJSONObject(i).getString("post_img"));
                            latitudeList.add(jsonArray.getJSONObject(i).getDouble("latitude"));
                            longitudeList.add(jsonArray.getJSONObject(i).getDouble("longitude"));
                            userNameList.add(jsonArray.getJSONObject(i).getString("user_name"));
                            userIdList.add(jsonArray.getJSONObject(i).getString("user_id"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //postImgListになにも要素が入っていない場合はマーカーを追加しない(ifがないとListにnullエラー)
                            if(postImgList.size()!=0) {
                                int count = 0;
                                //postImgListの要素の数だけマーカーを追加する
                                for (count = 0; count < postImgList.size(); count++) {
                                    int finalCount = count;
                                    Glide.with(getApplicationContext())
                                            .asBitmap()
                                            .load("https://click.ecc.ac.jp/ecc/hige_map_pin/image/user_post/"+postImgList.get(count))
                                            .override(100,200) //画像のリサイズ
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    // 取得したBitmapを使用してマーカーを設定
                                                    mMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng(latitudeList.get(finalCount),(longitudeList.get(finalCount))))
                                                            .icon(BitmapDescriptorFactory.fromBitmap(resource)));
                                                }
                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    // Optional: ロードがクリアされたときの処理を追加できます
                                                }
                                            });
                                }
                            }
                        }
                    });
                }
            });
        }
    }


    // ピンがクリックされたときに位置情報を取得して表示
    private void getLocationInfo(LatLng location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String locationInfo = address.getAddressLine(0) +
                        "\nLatitude: " + address.getLatitude() +
                        "\nLongitude: " + address.getLongitude();
                Toast.makeText(this, locationInfo, Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addMarker(LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }
        Toast.makeText(Home.this, "緯度: " + Mylatitude + ", 経度: " + Mylongitude, Toast.LENGTH_SHORT).show();
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("現在地"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }


    //場所の情報の取得
//    @Override
    public void onPoiClick(PointOfInterest poi) {
        Toast.makeText(this, "場所：" +
                        poi.name,
//                        + "\nID:" + poi.placeId +
//                        "\nLatitude:" + poi.latLng.latitude +
//                        " Longitude:" + poi.latLng.longitude,
                LENGTH_SHORT).show();
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
//        Button contribution_button = findViewById(R.id.contribution_button);

//        contribution_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(PostPreparation);
//            }
//        });
}