package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.BorrowVehicle;
import zero.programmer.data.kendaraan.entitites.Driver;
import zero.programmer.data.kendaraan.entitites.User;
import zero.programmer.data.kendaraan.entitites.Vehicle;
import zero.programmer.data.kendaraan.model.BorrowVehicleData;
import zero.programmer.data.kendaraan.model.SendCustomData;
import zero.programmer.data.kendaraan.response.ResponseOneData;
import zero.programmer.data.kendaraan.session.SessionManager;

public class AddBorrowVehiclePersonalActivity extends AppCompatActivity {

    private EditText editTextVehicleName, editTextMerk, editTextPoliceNumber, editTextNecessity,
            editTextBorrowDate, editTextReturnBorrow, editTextDestination;
    private Button buttonInserBorrowPersonal;

    private Calendar calendar = Calendar.getInstance();
    private String necessity, borrowDate, returnDate, destination;
    private final String borrowType = "PRIBADI";

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_borrow_vehicle_personal);

        // inject session manager
        sessionManager = new SessionManager(this);

        // get view layout
        editTextVehicleName = findViewById(R.id.et_vehicle_name_personal);
        editTextMerk = findViewById(R.id.et_vehicle_merk_personal);
        editTextPoliceNumber = findViewById(R.id.et_vehicle_police_number_personal);
        editTextNecessity = findViewById(R.id.et_borrow_necessity_personal);
        editTextBorrowDate = findViewById(R.id.et_borrow_borrow_date_personal);
        editTextReturnBorrow = findViewById(R.id.et_borrow_return_date_personal);
        editTextDestination = findViewById(R.id.et_borrow_destination_personal);
        buttonInserBorrowPersonal = findViewById(R.id.button_insert_borrow_personal);

        // set Data from custom data
        editTextVehicleName.setText(SendCustomData.vehicleName);
        editTextMerk.setText(SendCustomData.merk);
        editTextPoliceNumber.setText(SendCustomData.policeNumber);

        // jika edit borrow date click
        editTextBorrowDate.setOnClickListener(v -> getBorrowDate());
        // jika edit return date click
        editTextReturnBorrow.setOnClickListener(v -> getReturnDate());

        // click save
        buttonInserBorrowPersonal.setOnClickListener(v -> addBorrowVehicle());

    }

    private void getBorrowDate(){
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddBorrowVehiclePersonalActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String dayString;
                String monthString;

                // cek jika day and month kurang dari 10
                if (dayOfMonth > 0 && dayOfMonth < 10){
                    dayString = "0" + dayOfMonth;
                } else {
                    dayString = String.valueOf(dayOfMonth);
                }
                if (month > 0 && month < 10){
                    monthString = "0" + month;
                } else {
                    monthString = String.valueOf(month);
                }
                borrowDate = year + "-" + monthString + "-" +dayString;
                String borrowDateForEditText = dayString + "-" + monthString + "-" + year;
                editTextBorrowDate.setText(borrowDateForEditText);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void getReturnDate(){
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddBorrowVehiclePersonalActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String dayString;
                String monthString;

                // cek jika day and month kurang dari 10
                if (dayOfMonth > 0 && dayOfMonth < 10){
                    dayString = "0" + dayOfMonth;
                } else {
                    dayString = String.valueOf(dayOfMonth);
                }
                if (month > 0 && month < 10){
                    monthString = "0" + month;
                } else {
                    monthString = String.valueOf(month);
                }
                returnDate = year + "-" + monthString + "-" +dayString;
                String borrowDateForEditText = dayString + "-" + monthString + "-" + year;
                editTextReturnBorrow.setText(borrowDateForEditText);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void addBorrowVehicle(){
        if (validateInput()){

            Vehicle vehicle = new Vehicle(SendCustomData.registrationNumber);
            User user = new User(sessionManager.getUserSessionDetail().get(SessionManager.KEY_USERNAME));

            BorrowVehicleData borrowVehicleData = new BorrowVehicleData(
                    user, vehicle, null, borrowType, necessity, borrowDate, returnDate, destination
            );

            Call<ResponseOneData<BorrowVehicle>> createBorrowVehicle = GetConnection.apiRequest.createBorrowVehicle(
                    ApiKeyData.getApiKey(), borrowVehicleData
            );

            createBorrowVehicle.enqueue(new Callback<ResponseOneData<BorrowVehicle>>() {
                @Override
                public void onResponse(Call<ResponseOneData<BorrowVehicle>> call, Response<ResponseOneData<BorrowVehicle>> response) {
                    if (response.code() == 404){
                        Toast.makeText(AddBorrowVehiclePersonalActivity.this, "Salah satu data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 400){
                        Toast.makeText(AddBorrowVehiclePersonalActivity.this, "Something error", Toast.LENGTH_SHORT).show();
                    } else {
                        try {

                            Toast.makeText(AddBorrowVehiclePersonalActivity.this, response.body().getMessages().get(0), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddBorrowVehiclePersonalActivity.this, BorrowPersonalActivity.class));
                            finish();

                        } catch (NullPointerException e){
                            Toast.makeText(AddBorrowVehiclePersonalActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseOneData<BorrowVehicle>> call, Throwable t) {
                    Toast.makeText(AddBorrowVehiclePersonalActivity.this, "Error : " + t, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInput(){
        if (editTextNecessity.getText().toString().trim().equals("")){
            editTextNecessity.setError("Keperluan tidak boleh kosong");
            return false;
        } else if (editTextBorrowDate.getText().toString().trim().equals("")){
            editTextBorrowDate.setError("Tanggal pinjam tidak boleh kosong");
            return false;
        } else if (editTextReturnBorrow.getText().toString().trim().equals("")){
            editTextReturnBorrow.setError("Tanggal kembali tidak boleh kosong");
            return false;
        } else if (editTextDestination.getText().toString().trim().equals("")){
            editTextDestination.setError("Tempat tujuan tidak boleh kosong");
            return false;
        } else {
            necessity = editTextNecessity.getText().toString();
            destination = editTextDestination.getText().toString();
            return true;
        }
    }
}