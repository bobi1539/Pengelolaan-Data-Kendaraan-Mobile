package zero.programmer.data.kendaraan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

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
    private String fullName, employeeNumber, position, vehicleName, merk, policeNumber,
            driverName, phoneNumber, necessity, borrowDate, returnDate, destination, borrowStatusVariable;

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

        // intent
        intent = getIntent();
        setTextFromIntentToEditText();
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
}