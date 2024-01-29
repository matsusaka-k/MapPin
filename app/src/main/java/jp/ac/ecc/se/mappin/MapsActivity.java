package jp.ac.ecc.se.mappin;
import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.System.out;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.CircleOptions;
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
//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
//        GoogleMap.OnPoiClickListener {
public  class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap mMap;
    private Marker marker;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    //TextView textView = findViewById(R.id.textView);
    double Mylatitude; //自身のデバイスの緯度
    double Mylongitude; //自身のデバイスの経度

    //マップの

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ImageButton restartButton = findViewById(R.id.hugaButton);

        //マップ上のピンをクリア
        //mMap.clear();




        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Activityをリスタートさせる
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                finish();
                startActivity(intent);

            }
        });

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
                    //Toast.makeText(MapsActivity.this, "緯度: " + Mylatitude + ", 経度: " + Mylongitude, Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
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

        //現在の位置情報の取得
        if (currentLocation != null) {
            LatLng sydney = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("現在地")); //icon()　で　brawableに入っている画像を使用する 例:icon(BitmapDescriptorFactory.fromResource(R.drawable.station_1))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    // ピンがクリックされたときの処理
                    getLocationInfo(marker.getPosition());
                    Intent UserPostIntent = new Intent(getApplicationContext(),UserPost.class);
                    Intent MyAccountintent = new Intent(getApplicationContext(),Account.class);

                    // どのピンが押されたか現在地で見分ける(intentでその情報をUserPostに送る)

                    // もし押されたマーカーが自身の現在地の場合、Account画面に遷移
                    if((sydney.latitude == marker.getPosition().latitude)&&(sydney.longitude == marker.getPosition().longitude)){
                        MyAccountintent.putExtra("latitude",sydney.latitude);
                        MyAccountintent.putExtra("longitude",sydney.longitude);
                        startActivity(MyAccountintent);
                    }else {
                        UserPostIntent.putExtra("latitude",marker.getPosition().latitude);
                        UserPostIntent.putExtra("longitude",marker.getPosition().longitude);
                        startActivity(UserPostIntent);
                        return false;
                    }
                    return false;
                }
            });

            //自身の現在地から半径2.5kmの範囲を円の描画(他ユーザの投稿が取得できる範囲)
            googleMap.addCircle(
                    new CircleOptions()
                            .center(new LatLng(sydney.latitude,sydney.longitude))
                            .radius(2500.2500)
                            .fillColor(Color.argb(20,188,9,0))
                            .strokeColor(Color.argb(50,9,8,8))
                            .zIndex(1.0f)
            );

            //指定した倍率以下にズームアウトしないよう制限する
            mMap.setMinZoomPreference(15.0f);

            //指定した倍率以上にズームインしないよう制限する
            mMap.setMaxZoomPreference(19.0f);


            //ここでカメラの位置を調整できる
            CameraUpdate locationUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 18.0f);
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
                        String str = "{\"name\":\"John\",\"age\":\"30\"}";
                        JSONObject json = new JSONObject(body);
                        String list = json.getString("list");
                        JSONArray jsonArray = new JSONArray(list);
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
                                            .override(90,130) //画像のリサイズ
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




    //各リアクションが多い順に


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
                //Toast.makeText(this, locationInfo, Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addMarker(LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }
        Toast.makeText(MapsActivity.this, "緯度: " + Mylatitude + ", 経度: " + Mylongitude, Toast.LENGTH_SHORT).show();
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
}
