package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.Driver;
import zero.programmer.data.kendaraan.response.ResponseOneData;

public class EditDriverActivity extends AppCompatActivity {

    private EditText editTextIdDriver, editTextFullName, editTextPhoneNumber, editTextAddress;
    private Button buttonUpdateDriver;
    private String idDriver, fullName, phoneNumber, address;
    private Boolean isOnDuty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_driver);

        // get view layout
        editTextIdDriver = findViewById(R.id.et_driver_id_edit);
        editTextFullName = findViewById(R.id.et_driver_full_name_edit);
        editTextPhoneNumber = findViewById(R.id.et_driver_phone_number_edit);
        editTextAddress = findViewById(R.id.et_driver_address_edit);
        buttonUpdateDriver = findViewById(R.id.button_update_driver);

        // get id driver dari intent
        Intent intent = getIntent();
        idDriver = intent.getStringExtra("idDriver");

        // set data
        setDataToEditText();

        // click button update
        buttonUpdateDriver.setOnClickListener(v -> updateDataDriver());
    }

    private void setDataToEditText(){

        Call<ResponseOneData<Driver>> getDetailDriver = GetConnection.apiRequest.getDriver(ApiKeyData.getApiKey(), idDriver);
        getDetailDriver.enqueue(new Callback<ResponseOneData<Driver>>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<ResponseOneData<Driver>> call, Response<ResponseOneData<Driver>> response) {
                try {
                    // ambil detail driver dari response
                    Driver driverDetail = response.body().getData();

                    // set edit text id driver agar tidak bisa di edit
                    editTextIdDriver.setInputType(InputType.TYPE_NULL);
                    editTextIdDriver.setTextColor(R.color.dark_gray);

                    // set text dari response
                    editTextIdDriver.setText(driverDetail.getIdDriver());
                    editTextFullName.setText(driverDetail.getFullName());
                    editTextPhoneNumber.setText(driverDetail.getPhoneNumber());
                    editTextAddress.setText(driverDetail.getAddress());

                    // set status sopir dari response
                    isOnDuty = driverDetail.getOnDuty();


                }catch (NullPointerException e){
                    Toast.makeText(EditDriverActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOneData<Driver>> call, Throwable t) {
                Toast.makeText(EditDriverActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDataDriver(){
        if (validateInput()){

            Driver driverUpdate = new Driver(idDriver, fullName, phoneNumber, address, isOnDuty);

            Call<ResponseOneData<Driver>> updateDataDriver = GetConnection.apiRequest.updateDriver(
                    ApiKeyData.getApiKey(),
                    driverUpdate
            );

            updateDataDriver.enqueue(new Callback<ResponseOneData<Driver>>() {
                @Override
                public void onResponse(Call<ResponseOneData<Driver>> call, Response<ResponseOneData<Driver>> response) {

                    try {
                        Toast.makeText(EditDriverActivity.this, response.body().getMessages().get(0), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();
                    } catch (NullPointerException e){
                        Toast.makeText(EditDriverActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseOneData<Driver>> call, Throwable t) {
                    Toast.makeText(EditDriverActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private boolean validateInput(){
        if (editTextIdDriver.getText().toString().trim().equals("")){
            editTextIdDriver.setError("Id driver tidak boleh kosong");
            return false;
        } else if (editTextFullName.getText().toString().trim().equals("")){
            editTextFullName.setError("Nama lengkap tidak boleh kosong");
            return false;
        } else if (editTextPhoneNumber.getText().toString().trim().equals("")){
            editTextPhoneNumber.setError("No handphone tidak boleh kosong");
            return false;
        } else if (editTextAddress.getText().toString().trim().equals("")){
            editTextAddress.setError("Alamat tidak boleh kosong");
            return false;
        } else {
            idDriver = editTextIdDriver.getText().toString();
            fullName = editTextFullName.getText().toString();
            phoneNumber = editTextPhoneNumber.getText().toString();
            address = editTextAddress.getText().toString();
            return true;
        }
    }
}