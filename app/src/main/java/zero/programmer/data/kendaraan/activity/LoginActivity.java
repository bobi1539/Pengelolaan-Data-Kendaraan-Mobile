package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.model.LoginData;
import zero.programmer.data.kendaraan.response.ResponseOneData;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // get view layout
        editTextUsername = findViewById(R.id.et_login_username);
        editTextPassword = findViewById(R.id.et_login_password);
        buttonLogin = findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser(){
        if (validateInput()){

            LoginData loginData = new LoginData(username, password);

            Call<ResponseOneData<LoginData>> checkLoginUser = GetConnection.apiRequest.loginUser(
                    ApiKeyData.getApiKey(), loginData);

            checkLoginUser.enqueue(new Callback<ResponseOneData<LoginData>>() {
                @Override
                public void onResponse(Call<ResponseOneData<LoginData>> call, Response<ResponseOneData<LoginData>> response) {

                    if (response.code() == 200){
                        try {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, response.body().getMessages().get(0), Toast.LENGTH_SHORT).show();

                        } catch (NullPointerException e){
                            Toast.makeText(LoginActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.code() == 404){
                        Toast.makeText(LoginActivity.this, "Username belum terdaftar", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 403){
                        Toast.makeText(LoginActivity.this, "Password yang anda masukan salah", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login gagal", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseOneData<LoginData>> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error + t", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private boolean validateInput(){
        if (editTextUsername.getText().toString().trim().equals("")){
            editTextUsername.setError("Username tidak boleh kosong");
            return false;
        } else if (editTextPassword.getText().toString().trim().equals("")){
            editTextPassword.setError("Password tidak boleh kosong");
            return false;
        } else {
            username = editTextUsername.getText().toString();
            password = editTextPassword.getText().toString();
            return true;
        }
    }
}