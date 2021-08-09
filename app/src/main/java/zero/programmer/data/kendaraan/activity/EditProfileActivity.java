package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextFullName, editTextEmployeeNumber,
            editTextPosition, editTextWorkUnit, editTextRoleId;
    private Button buttonUpdateProfile;
    private String username, fullName, employeeNumber, position, workUnit;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // set intent
        intent = getIntent();

        // get view
        editTextUsername = findViewById(R.id.et_profile_username_edit);
        editTextFullName = findViewById(R.id.et_profile_full_name_edit);
        editTextEmployeeNumber = findViewById(R.id.et_profile_employee_number_edit);
        editTextPosition = findViewById(R.id.et_profile_position_edit);
        editTextWorkUnit = findViewById(R.id.et_profile_work_unit_edit);
        editTextRoleId = findViewById(R.id.et_profile_role_id_edit);
        buttonUpdateProfile = findViewById(R.id.button_update_profile);

        // set data from intent
        setDataFromIntent();

        buttonUpdateProfile.setOnClickListener(v -> updateDataProfile());


    }

    @SuppressLint("ResourceAsColor")
    private void setDataFromIntent(){
        editTextUsername.setText(intent.getStringExtra("username"));
        editTextFullName.setText(intent.getStringExtra("fullName"));
        editTextEmployeeNumber.setText(intent.getStringExtra("employeeNumber"));
        editTextPosition.setText(intent.getStringExtra("position"));
        editTextWorkUnit.setText(intent.getStringExtra("workUnit"));
        editTextRoleId.setText(intent.getStringExtra("roleId"));
    }

    private void updateDataProfile(){
        if (validateInput()){

            // membuat map untuk data yang akan di kirim ke server
            Map<Object, Object> dataProfile = new HashMap<>();
            dataProfile.put("fullName", fullName);
            dataProfile.put("employeeNumber", employeeNumber);
            dataProfile.put("position", position);
            dataProfile.put("workUnit", workUnit);

            Call<ResponseOneData<User>> updateProfile = GetConnection.apiRequest.updateUser(
                    ApiKeyData.getApiKey(),
                    username,
                    dataProfile
            );
            updateProfile.enqueue(new Callback<ResponseOneData<User>>() {
                @Override
                public void onResponse(Call<ResponseOneData<User>> call, Response<ResponseOneData<User>> response) {
                    try {

                        Toast.makeText(EditProfileActivity.this, response.body().getMessages().get(0), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();
                        
                    } catch (NullPointerException e){
                        Toast.makeText(EditProfileActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseOneData<User>> call, Throwable t) {
                    Toast.makeText(EditProfileActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private boolean validateInput(){
        if (editTextFullName.getText().toString().trim().equals("")){
            editTextFullName.setError("Nama lengkap tidak boleh kosong");
            return false;
        } else if (editTextEmployeeNumber.getText().toString().trim().equals("")){
            editTextEmployeeNumber.setError("NPK tidak boleh kosong");
            return false;
        } if (editTextPosition.getText().toString().trim().equals("")){
            editTextPosition.setError("Nama lengkap tidak boleh kosong");
            return false;
        } if (editTextWorkUnit.getText().toString().trim().equals("")){
            editTextWorkUnit.setError("Nama lengkap tidak boleh kosong");
            return false;
        } else {
            username = editTextUsername.getText().toString();
            fullName = editTextFullName.getText().toString();
            employeeNumber = editTextEmployeeNumber.getText().toString();
            position = editTextPosition.getText().toString();
            workUnit = editTextWorkUnit.getText().toString();
            return true;
        }
    }
}