package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.api.ApiRequest;
import zero.programmer.data.kendaraan.api.RetroServer;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.User;
import zero.programmer.data.kendaraan.response.ResponseOneData;
import zero.programmer.data.kendaraan.utils.RoleId;

public class EditUserActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextFullName, editTextEmployeeNumber,
            editTextPosition, editTextWorkUnit;
    private Spinner spinnerRoleId;
    private Button buttonUpdateUser;
    private String username, fullName, employeeNumber, position, workUnit;
    private RoleId roleId;

    private final ApiRequest apiRequest = RetroServer.getRetrofit().create(ApiRequest.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // get view layout
        editTextUsername = findViewById(R.id.et_user_username_edit);
        editTextFullName = findViewById(R.id.et_user_full_name_edit);
        editTextEmployeeNumber = findViewById(R.id.et_user_employee_number_edit);
        editTextPosition = findViewById(R.id.et_user_position_edit);
        editTextWorkUnit = findViewById(R.id.et_user_work_unit_edit);
        spinnerRoleId = findViewById(R.id.spinner_user_role_id_edit);
        buttonUpdateUser = findViewById(R.id.button_update_user);

        // ambil username dari intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        // set Data
        setDataToEditText();
    }

    private void setDataToEditText(){

        Call<ResponseOneData<User>> getDetailUser = apiRequest.getUser(ApiKeyData.getApiKey(), username);

        getDetailUser.enqueue(new Callback<ResponseOneData<User>>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<ResponseOneData<User>> call, Response<ResponseOneData<User>> response) {

                try {

                    // ambil detail user dari response
                    User userDetail = response.body().getData();

                    // set edit text username agar tidak bisa di edit
                    editTextUsername.setInputType(InputType.TYPE_NULL);
                    editTextUsername.setTextColor(R.color.dark_gray);

                    // set text dari response
                    editTextUsername.setText(userDetail.getUsername());
                    editTextFullName.setText(userDetail.getFullName());
                    editTextEmployeeNumber.setText(userDetail.getEmployeeNumber());
                    editTextPosition.setText(userDetail.getPosition());
                    editTextWorkUnit.setText(userDetail.getWorkUnit());


                } catch (NullPointerException e){
                    Toast.makeText(EditUserActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseOneData<User>> call, Throwable t) {
                Toast.makeText(EditUserActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });

    }
}