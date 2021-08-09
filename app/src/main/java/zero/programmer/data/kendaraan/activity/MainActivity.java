package zero.programmer.data.kendaraan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.fragment.BorrowFragment;
import zero.programmer.data.kendaraan.fragment.DriverFragment;
import zero.programmer.data.kendaraan.fragment.ProfileFragment;
import zero.programmer.data.kendaraan.fragment.UserFragment;
import zero.programmer.data.kendaraan.fragment.VehicleFragment;
import zero.programmer.data.kendaraan.session.SessionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // cek jika sudah login
        if (!new SessionManager(MainActivity.this).isLogin()){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(navigationListener);

        // Setting Profile Fragment as main Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, new ProfileFragment())
                .commit();
    }

    // Listener Navigation Bar
    private final NavigationBarView.OnItemSelectedListener navigationListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.item_person:
                    selectedFragment = new ProfileFragment();
                    break;
                case R.id.item_vehicle:
                    selectedFragment = new VehicleFragment();
                    break;
                case R.id.item_create:
                    selectedFragment = new BorrowFragment();
                    break;
                case R.id.item_driver:
                    selectedFragment = new DriverFragment();
                    break;
                case R.id.item_user:
                    selectedFragment = new UserFragment();
                    break;
            }

            // Begin transaction
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_layout, selectedFragment)
                    .commit();

            return true;
        }
    };
}