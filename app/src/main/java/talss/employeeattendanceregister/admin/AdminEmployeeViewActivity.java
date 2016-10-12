package talss.employeeattendanceregister.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import backend.EmployeeHandler;
import model.Admin;
import model.Employee;
import services.LoginLocalStoreService;
import talss.employeeattendanceregister.R;
import talss.employeeattendanceregister.employee.EmployeeAddressAutocompleteAdapter;

public class
AdminEmployeeViewActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener
{
	private final String TAG = AdminEmployeeViewActivity.class.getSimpleName();

	private EditText name, surname, designation;
	private AutoCompleteTextView  address;
	private Button next;

	private Employee employee;
	private ImageView employeeProfile;

	private boolean editEmployee;
	private LoginLocalStoreService localStoreService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle("User details");
		localStoreService = new LoginLocalStoreService(this);
		setContentView(R.layout.activity_admin_employee_view);
		setUIComponents();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_menu, menu);

		MenuItem searchItem = menu.findItem(R.id.menu_search_item);
		searchItem.setVisible(false);

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		Log.d(TAG, "onOptionsItemSelected");
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
			case R.id.action_edit:
				setEditTextEditable(true);
				return true;
			case R.id.action_delete:
				deleteEmployee();
				return true;
			case R.id.action_logout:
				loggingOut();
				return true;
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	private void loggingOut()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Logging out");
		alertDialogBuilder
				.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id)
					{
						localStoreService.clearUserData();
						logOut();
					}
				})
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void logOut()
	{
		Intent intent = new Intent(this, AdminLogInActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

	private void deleteEmployee()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		String name = employee.getEmployeeName() + " " + employee.getEmployeeSurname();
		alertDialogBuilder.setTitle("Delete " + name);
		alertDialogBuilder
				.setMessage("Are you sure you want to delete?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id)
					{
						goToEmployeeListActivity();
					}
				})
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void goToEmployeeListActivity()
	{
		EmployeeHandler.deleteEmployee(employee);
		Intent intent = new Intent(this, AdminEmployeeListActivity.class);

		//get admin from previous activity
		Bundle bundle = getIntent().getExtras();
		Admin admin = (Admin) bundle.get("admin");

		if(admin!=null) intent.putExtra("admin", admin);
		startActivity(intent);
	}


	private void setUIComponents()
	{
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		employeeProfile = (ImageView) findViewById(R.id.employee_profile_photo);
		name = (EditText) findViewById(R.id.employee_view_name_edit_text);
		surname = (EditText) findViewById(R.id.employee_view_surname_edit_text);
		designation = (EditText) findViewById(R.id.employee_view_designation_edit_text);

		address = (AutoCompleteTextView) findViewById(R.id.employee_view_address_edit_text);
		address.setAdapter(new EmployeeAddressAutocompleteAdapter(this, R.layout.list_item_employee_address));
		address.setOnItemClickListener(this);

		next = (Button) findViewById(R.id.employee_view_next_button);
		next.setOnClickListener(this);

		setEmployeeFields();
		setEditTextEditable(false);
	}

	private void setEditTextEditable(boolean value)
	{
		name.setClickable(value);
		name.setCursorVisible(value);
		name.setFocusable(value);
		name.setFocusableInTouchMode(value);

		surname.setClickable(value);
		surname.setCursorVisible(value);
		surname.setFocusable(value);
		surname.setFocusableInTouchMode(value);

		designation.setClickable(value);
		designation.setCursorVisible(value);
		designation.setFocusable(value);
		designation.setFocusableInTouchMode(value);

		address.setClickable(value);
		address.setCursorVisible(value);
		address.setFocusable(value);
		address.setFocusableInTouchMode(value);

		editEmployee = value;
	}

	private void setEmployeeFields()
	{
		Bundle bundle = getIntent().getExtras();
		employee = (Employee) bundle.get("employee");
		if (employee != null)
		{
			byte[] employeeImageBytes = employee.getEmployeeImageBytes();
			Bitmap bitmap = BitmapFactory.decodeByteArray(employeeImageBytes, 0, employeeImageBytes.length);
			employeeProfile.setImageBitmap(bitmap);

			name.setText(employee.getEmployeeName());
			surname.setText(employee.getEmployeeSurname());
			designation.setText(employee.getEmployeeDesignation());
			address.setText(employee.getEmployeeAddress());
		}
	}

	@Override
	public void onClick(View view)
	{
		if(view==next)
		{
			if(editEmployee)
			{
				employee.setEmployeeName(name.getText().toString());
				employee.setEmployeeSurname(surname.getText().toString());
				employee.setEmployeeDesignation(designation.getText().toString());
				employee.setEmployeeAddress(address.getText().toString());

				EmployeeHandler.editEmployee(employee);
			}

			Intent intent = new Intent(this, AdminEmployeeAttendanceReportActivity.class);
			intent.putExtra("employee", employee);
			startActivity(intent);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{

	}
}
