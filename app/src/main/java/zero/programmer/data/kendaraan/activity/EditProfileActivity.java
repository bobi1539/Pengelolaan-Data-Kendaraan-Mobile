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

import zero.programmer.data.kendaraan.R;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextFullName, editTextEmployeeNumber,
            editTextPosition, editTextWorkUnit, editTextRoleId;
    private Button buttonUpdateProfile;
    private String username, fullName, employeeNumber, position, workUnit, roleId;

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

        // set data from intent
        setDataFromIntent();
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
}