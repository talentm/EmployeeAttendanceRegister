package talss.employeeattendanceregister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import talss.employeeattendanceregister.admin.AdminLogInActivity;
import talss.employeeattendanceregister.employee.EmployeeLogInActivity;

public class
WelcomeActivity extends Activity implements View.OnClickListener
{

	private ImageButton admin, employee;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		registerUIComponents();
	}

	private void registerUIComponents()
	{
		admin = (ImageButton) findViewById(R.id.admin_button);
		employee = (ImageButton) findViewById(R.id.employee_button);
		admin.setOnClickListener(this);
		employee.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		if(view == admin)
		{
			Intent adminIntent = new Intent(getBaseContext(), AdminLogInActivity.class);
			startActivity(adminIntent);
		}
		else if (view == employee)
		{
			Intent employeeIntent = new Intent(getBaseContext(), EmployeeLogInActivity.class);
			startActivity(employeeIntent);
		}
	}
}
