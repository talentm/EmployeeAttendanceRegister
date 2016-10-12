package talss.employeeattendanceregister.employee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import backend.EmployeeHandler;
import model.Employee;
import services.LoginLocalStoreService;
import talss.employeeattendanceregister.R;
import talss.employeeattendanceregister.attendance.EmployeeAttendanceMapActivity;
import talss.lib.DialogAlert;

public class EmployeeLogInActivity extends Activity implements View.OnClickListener
{

	private Button registerButton, loginButton;
	private EditText emailText, passwordText;
	private LoginLocalStoreService localStoreService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_log_in);
		localStoreService = new LoginLocalStoreService(this);
		registerUIComponents();
	}

	private void registerUIComponents()
	{
		emailText = (EditText) findViewById(R.id.employee_email_edit_text);
		passwordText = (EditText) findViewById(R.id.employee_password_edit_text);
		registerButton = (Button) findViewById(R.id.employee_register_button);
		loginButton = (Button) findViewById(R.id.employee_login_button);
		registerButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		if(view == registerButton)
		{
			Intent registerIntent = new Intent("talss.employeeattendanceregister.EmployeeSignUpActivity");
			startActivity(registerIntent);
		}
		else if(view == loginButton)
		{
			Employee employee = new Employee();
			employee.setEmployeeEmail(emailText.getText().toString());
			employee.setEmployeePassword(passwordText.getText().toString());
			Employee employeeResult = EmployeeHandler.getEmployee(employee);
			if(employeeResult!=null)
			{
				localStoreService.storeUserData(employeeResult, null);
				localStoreService.setEmployeeLoggedIn(true);

				Intent logInIntent = new Intent(getBaseContext(), EmployeeAttendanceMapActivity.class);
				logInIntent.putExtra("employee", employeeResult);
				startActivity(logInIntent);
			}
			else
				DialogAlert.showAlert(this, "Email or password entered is incorrect", "Error");
		}
	}
}
