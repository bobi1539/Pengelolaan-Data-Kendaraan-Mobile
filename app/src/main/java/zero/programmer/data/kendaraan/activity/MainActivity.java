package zero.programmer.data.kendaraan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
import zero.programmer.data.kendaraan.utils.RoleId;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    private MenuItem itemDriver, itemUser;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        // cek jika sudah login
        if (!sessionManager.isLogin()){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(navigationListener);

        itemDriver = bottomNavigationView.getMenu().findItem(R.id.item_driver);
        itemUser = bottomNavigationView.getMenu().findItem(R.id.item_user);

        // cek role id
        validateRoleIdAccess();

        // Setting Profile Fragment as main Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, new ProfileFragment())
                .commit();
    }

    private void validateRoleIdAccess(){
        if (!sessionManager.getRoleId().equals(RoleId.ADMIN.toString())){
            itemDriver.setVisible(false);
            itemUser.setVisible(false);
        }
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