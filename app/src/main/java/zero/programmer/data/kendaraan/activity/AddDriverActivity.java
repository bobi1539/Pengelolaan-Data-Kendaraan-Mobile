package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.api.ApiRequest;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.api.RetroServer;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.Driver;
import zero.programmer.data.kendaraan.response.ResponseOneData;

public class AddDriverActivity extends AppCompatActivity {

    private EditText editTextIdDriver, editTextFullName, editTextPhoneNumber, editTextAddress;
    private Button buttonInsertDriver;

    private String idDriver, fullName, phoneNumber, address;
    private final Boolean isOnDuty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);

        // get view layout
        editTextIdDriver = findViewById(R.id.et_driver_id);
        editTextFullName = findViewById(R.id.et_driver_full_name);
        editTextPhoneNumber = findViewById(R.id.et_driver_phone_number);
        editTextAddress = findViewById(R.id.et_driver_address);
        buttonInsertDriver = findViewById(R.id.button_insert_driver);

        buttonInsertDriver.setOnClickListener(v -> insertDataDriver());
    }

    private void insertDataDriver(){
        if (validateInput()){

            Driver driverData = new Driver(
                    idDriver,
                    fullName,
                    phoneNumber,
                    address,
                    isOnDuty
            );

            Call<ResponseOneData<Driver>> insertDataDriver = GetConnection
                    .apiRequest
                    .createDriver(ApiKeyData.getApiKey(), driverData);

            insertDataDriver.enqueue(new Callback<ResponseOneData<Driver>>() {
                @Override
                public void onResponse(Call<ResponseOneData<Driver>> call, Response<ResponseOneData<Driver>> response) {

                    if (response.code() == 400){
                        Toast.makeText(AddDriverActivity.this
                                , "Data dengan id driver : " + driverData.getIdDriver() + " telah tersedia"
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            Toast.makeText(AddDriverActivity.this
                                    , response.body().getMessages().get(0)
                                    , Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            finish();

                        } catch (NullPointerException e) {
                            Toast.makeText(AddDriverActivity.this, "Erro : " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseOneData<Driver>> call, Throwable t) {
                    Toast.makeText(AddDriverActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInput(){
        if (editTextIdDriver.getText().toString().trim().equals("")){
            editTextIdDriver.setError("Id sopir tidak boleh kosong");
            return false;
        } else if (editTextFullName.getText().toString().trim().equals("")){
            editTextFullName.setError("Nama lengkap tidak boleh kosong");
            return false;
        } else if (editTextPhoneNumber.getText().toString().trim().equals("")) {
            editTextPhoneNumber.setError("No handphone tidak boleh kosong");
            return false;
        } else if (editTextAddress.getText().toString().trim().equals("")) {
            editTextAddress.setError("Alamat tidak boleh kosong");
            return false;
        } else {
            idDriver = editTextIdDriver.getText().toString();
            fullName = editTextFullName.getText().toString();
            phoneNumber = editTextPhoneNumber.getText().toString();
            address = editTextAddress.getText().toString();
            if (idDriver.contains(" ")){
                Toast.makeText(this, "Id sopir tidak boleh ada spasi", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        }
    }
}