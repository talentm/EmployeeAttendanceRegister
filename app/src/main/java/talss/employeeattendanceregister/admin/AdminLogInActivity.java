package talss.employeeattendanceregister.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import backend.AdminHandler;
import backend.EmployeeHandler;
import model.Admin;
import model.Employee;
import services.LoginLocalStoreService;
import talss.employeeattendanceregister.R;
import talss.employeeattendanceregister.attendance.EmployeeAttendanceMapActivity;
import talss.lib.DialogAlert;
import talss.lib.GoogleMaps;

/**
 * This is actually the main Login Activity
 */
public class AdminLogInActivity extends Activity implements View.OnClickListener
{
	private static final String TAG = AdminLogInActivity.class.getSimpleName();

	private Button registerButton, loginButton;
	private EditText emailText, passwordText;
	private LoginLocalStoreService localStoreService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_log_in);
		localStoreService = new LoginLocalStoreService(this);
		registerUIComponents();
	}

	private void registerUIComponents()
	{
		emailText = (EditText) findViewById(R.id.admin_email_edit_text);
		passwordText = (EditText) findViewById(R.id.admin_password_edit_text);
		registerButton = (Button) findViewById(R.id.admin_register_button);
		loginButton = (Button) findViewById(R.id.admin_login_button);
		registerButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		emailText.setText("");
		passwordText.setText("");
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		authenticate();

	}

	private void authenticate()
	{
		Log.v(TAG, "authenticate "+ localStoreService.isEmployeeLoggedIn());
		if(localStoreService.isEmployeeLoggedIn())
		{
			Employee loggedInEmployee = localStoreService.getLoggedInEmployee();
			if(loggedInEmployee!=null)
			{
				Intent intent = new Intent(this, EmployeeAttendanceMapActivity.class);
				intent.putExtra("employee", loggedInEmployee);
				startActivity(intent);
			}
		}
		else if(localStoreService.isAdminLoggedIn())
		{
			Admin admin = localStoreService.getLoggedInAdmin();
			if(admin!=null)
			{
				Intent intent = new Intent(this, AdminEmployeeListActivity.class);
				intent.putExtra("admin", admin);
				startActivity(intent);
			}
		}
	}

	@Override
	public void onClick(View view)
	{
		if(view == registerButton)
		{
			Intent registerIntent = new Intent(getBaseContext(), ChooseActivity.class);
			startActivity(registerIntent);
			//int distanceBetweenTwoLocations = GoogleMaps.getDistanceBetweenTwoLocations(-25.845833, 28.193416, -25.84468701970849, 28.1947539802915);
//			int distanceBetweenTwoLocations = GoogleMaps.getDistanceBetweenTwoLocations(-25.859525, 28.207015, -25.859756, 28.206993);
//			Log.v(TAG, "The distance between two locations is "+distanceBetweenTwoLocations);
		}//specify the type of usernames and passwords for admin and employee //To do
		else if(view == loginButton)
		{
			Admin admin = new Admin();
			admin.setAdminEmail(emailText.getText().toString());
			admin.setAdminPassword(passwordText.getText().toString());
			Admin adminResult = AdminHandler.getAdmin(admin);
			if(adminResult!=null)
			{
				Intent logInIntent = new Intent(getBaseContext(), AdminEmployeeListActivity.class);
				logInIntent.putExtra("admin", adminResult);
				startActivity(logInIntent);

				localStoreService.storeUserData(null, adminResult);
				localStoreService.setAdminLoggedIn(true);
			}
			//else DialogAlert.showAlert(this, "Email or password entered is incorrect", "Error");
			else
			{
				Employee employee = new Employee();
				employee.setEmployeeEmail(emailText.getText().toString());
				employee.setEmployeePassword(passwordText.getText().toString());
				Employee employeeResult = EmployeeHandler.getEmployee(employee);
				if(employeeResult!=null)
				{
					Intent logInIntent = new Intent(getBaseContext(), EmployeeAttendanceMapActivity.class);
					logInIntent.putExtra("employee", employeeResult);
					startActivity(logInIntent);

					localStoreService.storeUserData(employeeResult, null);
					localStoreService.setEmployeeLoggedIn(true);
				}
				else DialogAlert.showAlert(this, "Email or password entered is incorrect", "Error");
			}
		}
	}
}
