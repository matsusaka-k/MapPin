package jp.ac.ecc.se.mappin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import jp.ac.ecc.se.mappin.databinding.ActivityBottomBinding;

public class Bottom extends AppCompatActivity {

    private ActivityBottomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBottomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // AppBarConfigurationの構築
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_account, R.id.navigation_home, R.id.navigation_notifications)
                .build();

        BottomNavigationView navView = binding.navView;

        // NavControllerとAppBarConfigurationを使用してActionBarとBottomNavigationViewをセットアップ
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}
