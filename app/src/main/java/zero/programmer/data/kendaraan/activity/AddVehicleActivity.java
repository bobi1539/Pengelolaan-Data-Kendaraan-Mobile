package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.api.ApiRequest;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.api.RetroServer;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.Vehicle;
import zero.programmer.data.kendaraan.model.VehicleData;
import zero.programmer.data.kendaraan.response.ResponseOneData;

public class AddVehicleActivity extends AppCompatActivity {

    private EditText editTextRegistrationNumber, editTextName, editTextMerk,
            editTextChassisNumber, editTextMachineNumber, editTextPoliceNumber,
            editTextPurchaseDate, editTextValue, editTextLocation;
    private Spinner spinnerCondition;
    private Button buttonInsertVehicle;
    private String registrationNumber, name, merk, chassisNumber, machineNumber, policeNumber,
            location, condition, purchaseDateFormat;
    private Date purchaseDate;
    private Long acquisitionValue;
    private final Boolean isBorrow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        // get view
        editTextRegistrationNumber = findViewById(R.id.et_vehicle_registration_number);
        editTextName = findViewById(R.id.et_vehicle_name);
        editTextMerk = findViewById(R.id.et_vehicle_merk);
        editTextChassisNumber = findViewById(R.id.et_vehicle_chassis_number);
        editTextMachineNumber = findViewById(R.id.et_vehicle_machine_number);
        editTextPoliceNumber = findViewById(R.id.et_vehicle_police_number);
        editTextPurchaseDate = findViewById(R.id.et_vehicle_purchase_date);
        editTextValue = findViewById(R.id.et_vehicle_value);
        editTextLocation = findViewById(R.id.et_vehicle_location);
        spinnerCondition = findViewById(R.id.spinner_condition_vehicle);
        buttonInsertVehicle = findViewById(R.id.button_insert_vehicle);

        buttonInsertVehicle.setOnClickListener(v -> inserDataVehicle());
    }

    private void inserDataVehicle(){

        if (validateInput()){
            VehicleData vehicleData = new VehicleData(
                    registrationNumber,
                    name,
                    merk,
                    chassisNumber,
                    machineNumber,
                    policeNumber,
                    purchaseDateFormat,
                    acquisitionValue,
                    location,
                    condition,
                    isBorrow
            );

            Call<ResponseOneData<Vehicle>> insertVehicle = GetConnection.apiRequest.createVehicle(ApiKeyData.getApiKey(), vehicleData);

            insertVehicle.enqueue(new Callback<ResponseOneData<Vehicle>>() {
                @Override
                public void onResponse(Call<ResponseOneData<Vehicle>> call, Response<ResponseOneData<Vehicle>> response) {
                    List<String> messages = response.body().getMessages();
                    Toast.makeText(AddVehicleActivity.this, messages.get(0), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseOneData<Vehicle>> call, Throwable t) {
                    Toast.makeText(AddVehicleActivity.this, "On Failure : " + t, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInput(){
        if (editTextRegistrationNumber.getText().toString().trim().equals("")){
            editTextRegistrationNumber.setError("Nomor registrasi tidak boleh kosong");
            return false;
        } else if (editTextName.getText().toString().trim().equals("")){
            editTextName.setError("Nama tidak boleh kosong");
            return false;
        } else if (editTextMerk.getText().toString().trim().equals("")){
            editTextMerk.setError("Merk tidak boleh kosong");
            return false;
        } else if (editTextPurchaseDate.getText().toString().trim().equals("")){
            editTextPurchaseDate.setError("Tanggal beli tidak boleh kosong");
            return false;
        } else if (editTextValue.getText().toString().trim().equals("")){
            editTextValue.setError("Nilai peroleh tidak boleh kosong");
            return false;
        }  else if (editTextLocation.getText().toString().trim().equals("")){
            editTextLocation.setError("Lokasi tidak boleh kosong");
            return false;
        } else if (spinnerCondition.getSelectedItem().toString().equals("Kondisi")){
            Toast.makeText(this, "Silahkan pilih kondisi kendaraan", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            registrationNumber = editTextRegistrationNumber.getText().toString();
            name = editTextName.getText().toString();
            merk = editTextMerk.getText().toString();

            if (editTextChassisNumber.getText().toString().trim().equals("")) {
                chassisNumber = "";
            } else {
                chassisNumber = editTextChassisNumber.getText().toString();
            }
            if (editTextMachineNumber.getText().toString().trim().equals("")) {
                machineNumber = "";
            } else {
                machineNumber = editTextMachineNumber.getText().toString();
            }
            if (editTextMachineNumber.getText().toString().trim().equals("")) {
                policeNumber = "";
            } else {
                policeNumber = editTextPoliceNumber.getText().toString();
            }
            location = editTextLocation.getText().toString();
            acquisitionValue = Long.parseLong(editTextValue.getText().toString());
            condition = spinnerCondition.getSelectedItem().toString();

            if (registrationNumber.contains(" ")){
                Toast.makeText(this, "Nomor registrasi tidak boleh ada spasi", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    purchaseDate = dateFormat.parse(editTextPurchaseDate.getText().toString());
                    purchaseDateFormat = dateFormat.format(purchaseDate);
                    return true;

                } catch (Exception e) {
                    Toast.makeText(this, "Format tanggal beli harus seperti yyyy-MM-dd (2021-01-01)", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
    }
}