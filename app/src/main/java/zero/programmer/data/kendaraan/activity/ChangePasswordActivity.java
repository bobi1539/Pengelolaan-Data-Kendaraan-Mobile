package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.User;
import zero.programmer.data.kendaraan.response.ResponseOneData;
import zero.programmer.data.kendaraan.session.SessionManager;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextNewPassword, editTextRepeatNewPassword;
    private Button buttonUpdatePassword;
    private String username, newPassword, repeatNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editTextUsername = findViewById(R.id.et_password_username);
        editTextNewPassword = findViewById(R.id.ed_new_password);
        editTextRepeatNewPassword = findViewById(R.id.et_repeat_new_password);
        buttonUpdatePassword = findViewById(R.id.button_update_password);

        // set username ke edit text
        SessionManager sessionManager = new SessionManager(ChangePasswordActivity.this);
        username = sessionManager.getUserSessionDetail().get(SessionManager.KEY_USERNAME);
        editTextUsername.setText(username);

        // click button update password
        buttonUpdatePassword.setOnClickListener(v -> updatePassword());

    }

    private void updatePassword(){
        if (validateInput()){

            // map username dan password untuk dikirim ke server
            Map<Object, Object> dataPassword = new HashMap<>();
            dataPassword.put("password", newPassword);

            Call<ResponseOneData<User>> updatePassword = GetConnection.apiRequest.updateUser(
                    ApiKeyData.getApiKey(),
                    username,
                    dataPassword
            );

            updatePassword.enqueue(new Callback<ResponseOneData<User>>() {
                @Override
                public void onResponse(Call<ResponseOneData<User>> call, Response<ResponseOneData<User>> response) {
                    try {
                        Toast.makeText(ChangePasswordActivity.this, "Password berhasil diganti", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();
                    } catch (NullPointerException e){
                        Toast.makeText(ChangePasswordActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseOneData<User>> call, Throwable t) {
                    Toast.makeText(ChangePasswordActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private boolean validateInput(){
        if (editTextNewPassword.getText().toString().trim().equals("")){
            editTextNewPassword.setError("Password baru tidak boleh kosong");
            return false;
        } else if (editTextRepeatNewPassword.getText().toString().trim().equals("")){
            editTextRepeatNewPassword.setError("Ulangi password tidak boleh kosong");
            return false;
        } else {
            newPassword = editTextNewPassword.getText().toString();
            repeatNewPassword = editTextRepeatNewPassword.getText().toString();
            if (newPassword.length() < 8){
                Toast.makeText(this, "Password minimal 8 karakter", Toast.LENGTH_SHORT).show();
                return false;
            } else if (newPassword.contains(" ")){
                Toast.makeText(this, "Password tidak boleh ada spasi", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!newPassword.equals(repeatNewPassword)){
                Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        }
    }
}