package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
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
import zero.programmer.data.kendaraan.api.RetroServer;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.Vehicle;
import zero.programmer.data.kendaraan.model.VehicleData;
import zero.programmer.data.kendaraan.response.ResponseOneData;

public class EditVehicleActivity extends AppCompatActivity {

    private Intent intent;
    private String registrationNumber, name, merk, chassisNumber, machineNumber,
                policeNumber, location, condition, purchaseDateFormat;
    private Date purchaseDate;
    private Long acquisitionValue;
    private Boolean isBorrow;
    private EditText editTextRegistrationNumber, editTextName, editTextMerk, editTextChassisNumber,
                editTextMachineNumber, editTextPoliceNumber,editTextPurchaseDate, editTextValue,
                editTextLocation;
    private Spinner spinnerCondition;
    private Button buttonUpdate;

    private final ApiRequest apiRequest = RetroServer.getRetrofit().create(ApiRequest.class);

    @SuppressLint("SimpleDateFormat")
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);

        // get view edit
        editTextRegistrationNumber = findViewById(R.id.et_vehicle_registration_number_edit);
        editTextName = findViewById(R.id.et_vehicle_name_edit);
        editTextMerk = findViewById(R.id.et_vehicle_merk_edit);
        editTextChassisNumber = findViewById(R.id.et_vehicle_chassis_number_edit);
        editTextMachineNumber = findViewById(R.id.et_vehicle_machine_number_edit);
        editTextPoliceNumber = findViewById(R.id.et_vehicle_police_number_edit);
        editTextPurchaseDate = findViewById(R.id.et_vehicle_purchase_date_edit);
        editTextValue = findViewById(R.id.et_vehicle_value_edit);
        editTextLocation = findViewById(R.id.et_vehicle_location_edit);
        spinnerCondition = findViewById(R.id.spinner_condition_vehicle_edit);
        buttonUpdate = findViewById(R.id.button_update_vehicle_action);

        intent = getIntent();
        registrationNumber = intent.getStringExtra("registrationNumber");

        setDataToEditText();

        buttonUpdate.setOnClickListener(v -> {
            updateDataVehicle();
        });
    }

    private void setDataToEditText(){

        Call<ResponseOneData<Vehicle>> getDetailVehicle = apiRequest.getVehicle(ApiKeyData.getApiKey(), registrationNumber);

        getDetailVehicle.enqueue(new Callback<ResponseOneData<Vehicle>>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<ResponseOneData<Vehicle>> call, Response<ResponseOneData<Vehicle>> response) {
                try {
                    Vehicle vehicle = response.body().getData();

                    editTextRegistrationNumber.setText(registrationNumber);
                    // set agar no registrasi tidak bisa di edit
                    editTextRegistrationNumber.setInputType(InputType.TYPE_NULL);
                    editTextRegistrationNumber.setTextColor(R.color.dark_gray);

                    editTextName.setText(vehicle.getName());
                    editTextMerk.setText(vehicle.getMerk());
                    editTextChassisNumber.setText(vehicle.getChassisNumber());
                    editTextMachineNumber.setText(vehicle.getMachineNumber());
                    editTextPoliceNumber.setText(vehicle.getPoliceNumber());

                    String purchaseDateFormat = dateFormat.format(vehicle.getPurchaseDate());
                    editTextPurchaseDate.setText(purchaseDateFormat);

                    editTextValue.setText(String.valueOf(vehicle.getAcquisitionValue()));
                    editTextLocation.setText(vehicle.getLocation());
                    isBorrow = vehicle.getBorrow();

                    // set condition to spinner
                    String conditionString = vehicle.getCondition();
                    ArrayAdapter adapterSpinnerCondition = (ArrayAdapter) spinnerCondition.getAdapter();
                    int positionSpinnerCondition = adapterSpinnerCondition.getPosition(conditionString);
                    spinnerCondition.setSelection(positionSpinnerCondition);


                } catch (NullPointerException e){
                    Toast.makeText(EditVehicleActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOneData<Vehicle>> call, Throwable t) {
                Toast.makeText(EditVehicleActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateDataVehicle(){

        if (editTextRegistrationNumber.getText().toString().trim().equals("")){
            editTextRegistrationNumber.setError("Nomor registrasi tidak boleh kosong");
        } else if (editTextName.getText().toString().trim().equals("")){
            editTextName.setError("Nama tidak boleh kosong");
        } else if (editTextMerk.getText().toString().trim().equals("")){
            editTextMerk.setError("Merk tidak boleh kosong");
        } else if (editTextPurchaseDate.getText().toString().trim().equals("")){
            editTextPurchaseDate.setError("Tanggal beli tidak boleh kosong");
        } else if (editTextValue.getText().toString().trim().equals("")){
            editTextValue.setError("Nilai peroleh tidak boleh kosong");
        }  else if (editTextLocation.getText().toString().trim().equals("")){
            editTextLocation.setError("Lokasi tidak boleh kosong");
        } else if (spinnerCondition.getSelectedItem().toString().equals("Kondisi")){
            Toast.makeText(this, "Silahkan pilih kondisi kendaraan", Toast.LENGTH_SHORT).show();
        } else {
            registrationNumber = editTextRegistrationNumber.getText().toString();
            name = editTextName.getText().toString();
            merk = editTextMerk.getText().toString();

            if (editTextChassisNumber.getText().toString().trim().equals("")){
                chassisNumber = "";
            } else {
                chassisNumber = editTextChassisNumber.getText().toString();
            }
            if (editTextMachineNumber.getText().toString().trim().equals("")){
                machineNumber = "";
            } else {
                machineNumber = editTextMachineNumber.getText().toString();
            }
            if (editTextMachineNumber.getText().toString().trim().equals("")){
                policeNumber = "";
            } else {
                policeNumber = editTextPoliceNumber.getText().toString();
            }

            location = editTextLocation.getText().toString();
            acquisitionValue = Long.parseLong(editTextValue.getText().toString());
            condition = spinnerCondition.getSelectedItem().toString();

            boolean isDateFormatValid = true;

            try {
                purchaseDate = dateFormat.parse(editTextPurchaseDate.getText().toString());
                purchaseDateFormat = dateFormat.format(purchaseDate);

            } catch (Exception e) {
                Toast.makeText(this, "Format tanggal beli harus seperti yyyy-MM-dd (2021-01-01)", Toast.LENGTH_SHORT).show();
                isDateFormatValid = false;
            }

            if (isDateFormatValid) {
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

                Call<ResponseOneData<Vehicle>> updateVehicle = apiRequest.updateVehicle(ApiKeyData.getApiKey(), vehicleData);

                updateVehicle.enqueue(new Callback<ResponseOneData<Vehicle>>() {
                    @Override
                    public void onResponse(Call<ResponseOneData<Vehicle>> call, Response<ResponseOneData<Vehicle>> response) {
                        List<String> messages = response.body().getMessages();
                        Toast.makeText(EditVehicleActivity.this, messages.get(0), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseOneData<Vehicle>> call, Throwable t) {
                        Toast.makeText(EditVehicleActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}