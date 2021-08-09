package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.api.ApiRequest;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.api.RetroServer;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.User;
import zero.programmer.data.kendaraan.response.ResponseOneData;
import zero.programmer.data.kendaraan.utils.RoleId;

public class AddUserActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextRepeatPassword,
            editTextFullName, editTextEmployeeNumber, editTextPosition, editTextWorkUnit;
    private Spinner spinnerRoleId;
    private Button buttonInsertUser;

    private String username, password, repeatPassword, fullName, employeeNumber, position, workUnit;
    private RoleId roleId;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // get view layout
        editTextUsername = findViewById(R.id.et_user_username);
        editTextPassword = findViewById(R.id.et_user_password);
        editTextRepeatPassword = findViewById(R.id.et_user_repeat_password);
        editTextFullName = findViewById(R.id.et_user_full_name);
        editTextEmployeeNumber = findViewById(R.id.et_user_employee_number);
        editTextPosition = findViewById(R.id.et_user_position);
        editTextWorkUnit = findViewById(R.id.et_user_work_unit);
        spinnerRoleId = findViewById(R.id.spinner_user_role_id);
        buttonInsertUser = findViewById(R.id.button_insert_user);

        buttonInsertUser.setOnClickListener(v -> insertDataUser());
    }

    private void insertDataUser(){
        if (validateInput()){

            User userData = new User(
                    username,
                    password,
                    fullName,
                    employeeNumber,
                    position,
                    workUnit,
                    roleId
            );

            Call<ResponseOneData<User>> insertUserData = GetConnection.apiRequest.createUser(ApiKeyData.getApiKey(), userData);
            insertUserData.enqueue(new Callback<ResponseOneData<User>>() {
                @Override
                public void onResponse(Call<ResponseOneData<User>> call, Response<ResponseOneData<User>> response) {

                    if (response.code() == 400){
                        Toast.makeText(
                                AddUserActivity.this,
                                "Data dengan username : " + username + " telah tersedia",
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        try {
                            Toast.makeText(AddUserActivity.this
                                    , response.body().getMessages().get(0)
                                    , Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            finish();
                        } catch (NullPointerException e) {
                            Toast.makeText(AddUserActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseOneData<User>> call, Throwable t) {
                    Toast.makeText(AddUserActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
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
        } else if (editTextRepeatPassword.getText().toString().trim().equals("")){
            editTextRepeatPassword.setError("Ulangi password tidak boleh kosong");
            return false;
        } else if (editTextFullName.getText().toString().trim().equals("")){
            editTextFullName.setError("Nama lengkap tidak boleh kosong");
            return false;
        } else if (editTextEmployeeNumber.getText().toString().trim().equals("")){
            editTextEmployeeNumber.setError("NPK tidak boleh kosong");
            return false;
        } else if (editTextPosition.getText().toString().trim().equals("")){
            editTextPosition.setError("Jabatan tidak boleh kosong");
            return false;
        } else if (editTextWorkUnit.getText().toString().trim().equals("")){
            editTextWorkUnit.setError("Unit kerja tidak boleh kosong");
            return false;
        } else if (spinnerRoleId.getSelectedItem().toString().trim().equals("Role Id")){
            Toast.makeText(this, "Silahkan pilih role id", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            username = editTextUsername.getText().toString();
            password = editTextPassword.getText().toString();
            repeatPassword = editTextRepeatPassword.getText().toString();
            fullName = editTextFullName.getText().toString();
            employeeNumber = editTextEmployeeNumber.getText().toString();
            position = editTextPosition.getText().toString();
            workUnit = editTextWorkUnit.getText().toString();
            roleId = RoleId.valueOf(spinnerRoleId.getSelectedItem().toString());

            if (username.contains(" ")){
                Toast.makeText(this, "Username tidak boleh ada spasi", Toast.LENGTH_SHORT).show();
                return false;
            } else if (password.length() < 8){
                Toast.makeText(this, "Password minimal 8 karakter", Toast.LENGTH_SHORT).show();
                return false;
            } else if (password.contains(" ")){
                Toast.makeText(this, "Password tidak boleh ada spasi", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!password.trim().equals(repeatPassword)){
                Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        }
    }
}