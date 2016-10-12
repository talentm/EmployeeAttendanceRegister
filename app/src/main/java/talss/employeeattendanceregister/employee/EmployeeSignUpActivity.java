package talss.employeeattendanceregister.employee;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import backend.EmployeeHandler;
import model.Admin;
import model.Employee;
import services.LoginLocalStoreService;
import talss.employeeattendanceregister.R;
import talss.employeeattendanceregister.admin.AdminLogInActivity;
import talss.lib.DataValidation;
import talss.lib.DialogAlert;
import talss.lib.GoogleMaps;

public class EmployeeSignUpActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener
{
	private static final String TAG = EmployeeSignUpActivity.class.getSimpleName();

	private AutoCompleteTextView employeeAddress;
	private Button employeeSignUpSaveButton, selectAdminButton;
	private EditText employeeName, employeeSurname, employeeEmail, employeePassword, employeeAdmin, employeeDesignation;
    private String adminId;
	private LoginLocalStoreService localStoreService;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employe_sign_up);
		localStoreService = new LoginLocalStoreService(this);
		registerUIComponents();

	}

	private void registerUIComponents()
	{
		employeeAddress = (AutoCompleteTextView) findViewById(R.id.employee_sign_up_address_edit_text);
		employeeAddress.setAdapter(new EmployeeAddressAutocompleteAdapter(this, R.layout.list_item_employee_address));
		employeeAddress.setOnItemClickListener(this);

		employeeSignUpSaveButton = (Button) findViewById(R.id.employee_sign_up_save_button);
		selectAdminButton = (Button) findViewById(R.id.employee_sign_up_admin_browse_button);
		employeeSignUpSaveButton.setOnClickListener(this);
		selectAdminButton.setOnClickListener(this);
		employeeName = (EditText) findViewById(R.id.employee_sign_up_name_edit_text);
		employeeSurname = (EditText) findViewById(R.id.employee_sign_up_surname_edit_text);
		employeeEmail = (EditText) findViewById(R.id.employee_sign_up_email_edit_text);
		employeePassword = (EditText) findViewById(R.id.employee_sign_up_password_edit_text);
		employeeAdmin = (EditText) findViewById(R.id.employee_sign_up_admin_edit_text);
		//employeeAddress = (EditText) findViewById(R.id.employee_sign_up_address_edit_text);
		employeeDesignation = (EditText) findViewById(R.id.employee_sign_up_designation_edit_text);
	}

	private Employee getEmployeeFromPreviousActivity()
	{
		Bundle bundle = getIntent().getExtras();
		Employee employee = (Employee) bundle.get("employee");
		if(employee!=null)
		{
			return employee;
		}
		return new Employee();
	}

	@Override
	public void onClick(View view)
	{
		Log.d(TAG, "onClick");
		if (view == employeeSignUpSaveButton)
		{
			String name = this.employeeName.getText().toString();
			String surname = this.employeeSurname.getText().toString();
			String email = this.employeeEmail.getText().toString();
			String password = this.employeePassword.getText().toString();
			String admin = this.employeeAdmin.getText().toString();
			String address = this.employeeAddress.getText().toString();
			String designation = this.employeeDesignation.getText().toString();

			HashMap<String, String> data = new HashMap<>();
			data.put("name", name);
			data.put("surname", surname);
			data.put("email", email);
			data.put("password", password);
			data.put("admin", admin);
			data.put("address", address);
			data.put("designation", designation);

			if(DataValidation.isValid(data))
			{
				Employee employee = getEmployeeFromPreviousActivity();
				employee.setEmployeeName(name);
				employee.setEmployeeSurname(surname);
				employee.setEmployeeEmail(email);
				employee.setEmployeePassword(password);
                employee.setEmployeeAddress(address);
				employee.setEmployeeDesignation(designation);

				PointF coordinates = GoogleMaps.getCoordinatesFromStreetAddress(address, this);
				if(coordinates!=null)
				{
					employee.setEmployeeCoordinates(coordinates.x + "," + coordinates.y);
				}

				if (EmployeeHandler.EmployeeExists(employee))
				{
					DialogAlert.showAlert(this, "There is an existing account associated with " + employee.getEmployeeEmail(), "Error");
					return;
				}

				//Fix later
				int addEmployee = EmployeeHandler.addEmployee(employee);
				if (addEmployee>0)
				{
					int linkEmployee = EmployeeHandler.linkEmployeeToAdmin(adminId, String.valueOf(addEmployee));
					if(linkEmployee<1)
					{
						DialogAlert.showAlert(this, "Could not link employee to admin", "Error");
						return;
					}

					Toast.makeText(this, "Registration completed", Toast.LENGTH_LONG);
					Intent intent = new Intent(this, AdminLogInActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
				}
			}
			else
			{
				String validation = DataValidation.getValidation(data);
				DialogAlert.showAlert(this, validation, "Error");
			}
		}
		else if (view == selectAdminButton)
		{
			Intent intent = new Intent(this, EmployeeAdminListActivity.class);
			startActivityForResult(intent, 0);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.d(TAG, "onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null)
		{
			Admin admin = (Admin) data.getSerializableExtra("admin");
			if (admin != null)
			{
				employeeAdmin.setText(admin.getAdminName() + " " + admin.getAdminSurname());
				adminId = admin.getAdminId();
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		String str = (String) parent.getItemAtPosition(position);
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

}
