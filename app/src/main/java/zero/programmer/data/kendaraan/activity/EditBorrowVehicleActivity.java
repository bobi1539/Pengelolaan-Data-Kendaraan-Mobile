package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import zero.programmer.data.kendaraan.R;

public class EditBorrowVehicleActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextEmployeeNumber, editTextPosition, editTextVehicleName,
            editTextMerk, editTextPoliceNumber, editTextDriverName, editTextPhoneNumber,
            editTextNecessity, editTextBorrowDate, editTextReturnDate, editTextDestination;
    private TextView textViewDriverTitle, subTitleEditBorrow;
    private TextInputLayout textInputLayoutDriverName, textInputLayoutPhoneNumber;
    private Spinner spinnerBorrowStatus;
    private Button buttonUpdateData;

    private Intent intent;

    private Integer idBorrow;
    private String necessity, borrowDate, returnDate, destination, borrowStatus;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_borrow_vehicle);

        // get view
        editTextFullName = findViewById(R.id.et_user_full_name_edit_borrow);
        editTextEmployeeNumber = findViewById(R.id.et_user_employee_number_edit_borrow);
        editTextPosition = findViewById(R.id.et_user_position_edit_borrow);
        editTextVehicleName = findViewById(R.id.et_vehicle_name_edit_borrow);
        editTextMerk = findViewById(R.id.et_vehicle_merk_edit_borrow);
        editTextPoliceNumber = findViewById(R.id.et_vehicle_police_number_edit_borrow);
        editTextDriverName = findViewById(R.id.et_driver_full_name_edit_borrow);
        editTextPhoneNumber = findViewById(R.id.et_driver_phone_number_edit_borrow);
        editTextNecessity = findViewById(R.id.et_borrow_necessity_edit_borrow);
        editTextBorrowDate = findViewById(R.id.et_borrow_date_edit_borrow);
        editTextReturnDate = findViewById(R.id.et_return_date_edit_borrow);
        editTextDestination = findViewById(R.id.et_destination_edit_borrow);
        spinnerBorrowStatus = findViewById(R.id.spinner_status_edit_borrow);
        textViewDriverTitle = findViewById(R.id.driver_title_edit_borrow);
        subTitleEditBorrow = findViewById(R.id.sub_title_edit_borrow);
        textInputLayoutDriverName = findViewById(R.id.til_driver_full_name_edit_borrow);
        textInputLayoutPhoneNumber = findViewById(R.id.til_driver_phone_number_edit_borrow);
        buttonUpdateData = findViewById(R.id.button_update_borrow);

        // intent
        intent = getIntent();
        setTextFromIntentToEditText();

        // when edit borrow date click
        editTextBorrowDate.setOnClickListener(v -> getBorrowDate());
        // when edit return date click
        editTextReturnDate.setOnClickListener(v -> getReturnDate());

        // click button
        buttonUpdateData.setOnClickListener(v -> updateBorrowVehicle());
    }

    private void setTextFromIntentToEditText(){
        editTextFullName.setText(intent.getStringExtra("fullName"));
        editTextEmployeeNumber.setText(intent.getStringExtra("employeeNumber"));
        editTextPosition.setText(intent.getStringExtra("position"));
        editTextVehicleName.setText(intent.getStringExtra("vehicleName"));
        editTextMerk.setText(intent.getStringExtra("merk"));
        editTextPoliceNumber.setText(intent.getStringExtra("policeNumber"));

        if (intent.getStringExtra("borrowType").equals("DINAS")){
            editTextDriverName.setText(intent.getStringExtra("driverName"));
            editTextPhoneNumber.setText(intent.getStringExtra("fullName"));
        } else {
            subTitleEditBorrow.setText(R.string.borrow_personal);
            textViewDriverTitle.setVisibility(View.GONE);
            textInputLayoutDriverName.setVisibility(View.GONE);
            textInputLayoutPhoneNumber.setVisibility(View.GONE);
        }

        editTextNecessity.setText(intent.getStringExtra("necessity"));
        editTextBorrowDate.setText(intent.getStringExtra("borrowDate"));
        editTextReturnDate.setText(intent.getStringExtra("returnDate"));
        editTextDestination.setText(intent.getStringExtra("destination"));

        ArrayAdapter adapter = (ArrayAdapter) spinnerBorrowStatus.getAdapter();
        int positionSpinner = adapter.getPosition(intent.getStringExtra("borrowStatusVariable"));
        spinnerBorrowStatus.setSelection(positionSpinner);
    }

    private void getBorrowDate(){
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                EditBorrowVehicleActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                EditBorrowVehicleActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                editTextReturnDate.setText(borrowDateForEditText);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void updateBorrowVehicle(){
        if (validateInput()){
            Toast.makeText(this, "valid", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(){
        if (editTextNecessity.getText().toString().trim().equals("")){
            editTextNecessity.setError("Keperluan tidak boleh kosong");
            return false;
        } else if (editTextBorrowDate.getText().toString().trim().equals("")){
            editTextBorrowDate.setError("Tanggal pinjam tidak boleh kosong");
            return false;
        } else if (editTextReturnDate.getText().toString().trim().equals("")){
            editTextReturnDate.setError("Tanggal kembali tidak boleh kosong");
            return false;
        } else if (editTextDestination.getText().toString().trim().equals("")){
            editTextDestination.setError("Tempat tujuan tidak boleh kosong");
            return false;
        } else if (spinnerBorrowStatus.getSelectedItem().toString().trim().equals("Status Peminjaman")){
            Toast.makeText(this, "Silahkan pilih status peminjaman", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            necessity = editTextNecessity.getText().toString();
            destination = editTextDestination.getText().toString();
            return true;
        }
    }
}