package jp.ac.ecc.se.mappin;

import static android.widget.Toast.LENGTH_SHORT;

import static java.lang.System.in;
import static java.lang.System.out;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    String baseURL = "https://click.ecc.ac.jp/ecc/hige_map_pin/DB_ShowMap.php"; //url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // ImageButton1を宣言および初期化
        ImageButton imageButton1 = findViewById(R.id.imageButton);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), UserPost.class);
                startActivity(intent);
                                            }
                                        });
//        if (imageButton1 != null) {
//            imageButton1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // 新しいレイアウトに切り替える
//                    setContentView(R.layout.user_post);
//                }
//            });
//        } else {
//            // もしImageButtonが見つからなかった場合のエラーハンドリング
//            Log.e("MapsActivity", "ImageButton1 not found");
//        }

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
                    Toast.makeText(MapsActivity.this, "緯度: " + Mylatitude + ", 経度: " + Mylongitude, Toast.LENGTH_SHORT).show();
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
            LatLng pinLocation = new LatLng(35.6895, 139.6917); // 例: 東京の座標
            LatLng sydney = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("現在地"));
            marker = mMap.addMarker(new MarkerOptions().position(pinLocation).title("Marker in Tokyo"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pinLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                  @Override
                  public boolean onMarkerClick(Marker marker) {
                      // ピンがクリックされたときの処理
                      getLocationInfo(marker.getPosition());
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


            /*いったん消す -------------------------------------------------------------------


            // HTTP接続用インスタンス生成
            OkHttpClient client = new OkHttpClient();

            // JSON形式でパラメータを送るようデータ形式を設定
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            // Bodyのデータ(APIに渡したいパラメータを設定)
            RequestBody requestBody = new FormBody.Builder()
                    //float型では無理なのでStringに変換
                    .add("{\"topLatitude\":\"", String.valueOf(topLatitude))
                    .add("{\"bottomLatitude\":\"", String.valueOf(bottomLatitude))
                    .add("{\"rightLongitude\":\"", String.valueOf(rightLongitude))
                    .add("{\"leftLongitude\":\"", String.valueOf(leftLongitude))
                    .build();

            // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
            Request request = new Request.Builder()
                    .url("https://click.ecc.ac.jp/ecc/hige_map_pin/DB_ShowMap.php")
                    .post(RequestBody.create(String.valueOf(requestBody), mediaType))
                    //.post(RequestBody.create(String.valueOf(requestBody), mediaType))
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
                    //user_nameの列データを取得する変数
                    String user_name = "";

                    try {
                        JSONObject json = new JSONObject(body);
                        String list = json.getString("list");
                        JSONArray jsonArray = new JSONArray(list);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            user_name += jsonArray.getJSONObject(i).getString("user_name");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                   // Toast.makeText(getApplicationContext(), user_name, Toast.LENGTH_LONG);
                    // postメソッドを使うことでUIを操作することができる。(runOnUiThreadメソッドでも可)
                    String finalUserName = user_name;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("UIの操作");
                            Toast.makeText(getApplicationContext(), finalUserName, Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });

            ここまで　-------------------------------------------------
            */


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
                    //user_nameの列データを取得する変数
                    String user_name = "";
                    try {
                        out.println("aaaaa");
                        String str = "{\"name\":\"John\",\"age\":\"30\"}";
                        JSONObject json = new JSONObject(body);

                        out.println("bbbbb");
                        String list = json.getString("list");
                        out.println("ccccc");
                        JSONArray jsonArray = new JSONArray(list);
                        out.println("ddddd");
                        out.println(jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            user_name += jsonArray.getJSONObject(i).getString("user_name");
                            out.println("-------------------------------------------------");
                            out.println(user_name);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Toast.makeText(getApplicationContext(), user_name, Toast.LENGTH_LONG);
                    // postメソッドを使うことでUIを操作することができる。(runOnUiThreadメソッドでも可)
                    String finalUserName = user_name;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("UIの操作");
                            out.println("finalUserNameの中身です"+finalUserName+"aaaaaaaaaaaaaaaaaaa");
                            Toast.makeText(getApplicationContext(), finalUserName, Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });




//            HttpURLConnection urlCon = null;    //
//            InputStream InputStream = null; // URL連携した戻り値を取得して保持する用 InputStream in = null; // URL連携した戻り値を取得して保持する用
//            final int BUF_SIZE = 512; // InputStreamから文字列にする際のバッファのサイズ
//            try {
//                // httpコネクションを確立し、urlを叩いて情報を取得      //?からうしろがqueryパラメータ
//                URL url = new URL("https://click.ecc.ac.jp/ecc/hige_map_pin/DB_ShowMap.php/?"+"topLatitude="+topLatitude+"&"+
//                                        "bottomLatitude="+bottomLatitude+"&"+"rightLongitude="+rightLongitude+"&"+"leftLongitude="+leftLongitude);
//                urlCon = (HttpURLConnection)url.openConnection();
//                urlCon.setRequestMethod("GET");
//                urlCon.connect();
//                //接続開始
//
//                // データを取得
//                InputStream = urlCon.getInputStream();
//
//                // InputStreamから取得したbyteデータを文字列にして保持するための変数
//                String str = new String();
//                // InputStreamからbyteデータを取得するための変数
//                byte[] buf = new byte[BUF_SIZE];
//
//                // InputStreamからのデータを文字列として取得する
//                while(true) {
//                    if( in.read(buf) <= 0)break;
//                    str += new String(buf);
//                }
//
//                final String resultStr = str;
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 結果をトーストで表示
//                        System.out.println(resultStr);
//                    }
//                });
//
//            } catch (IOException ioe ) {
//                ioe.printStackTrace();
//                Toast.makeText(this, "IOExceptionが発生しました。", LENGTH_SHORT).show();
//
//            } finally {
//
//                try {
//                    urlCon.disconnect();
//                    in.close();
//
//                } catch (IOException ioe ) {
//                    ioe.printStackTrace();
//                }
//            }


            }
        }






            // currentLocationがnullの場合の処理

//    //        ここでマーカーの色を自由に決めれる
//            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
//            mMap.addMarker(options);

    //たぶんonMapReady end

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





