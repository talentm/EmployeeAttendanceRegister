package talss.employeeattendanceregister.admin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import talss.employeeattendanceregister.R;
import talss.employeeattendanceregister.employee.EmployeeSignUpActivity;
import talss.employeeattendanceregister.employee.EmployeeUploadPhotoActivity;

public class ChooseActivity extends Activity implements View.OnClickListener
{

	private ImageButton adminButton;
	private ImageButton employeeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose);

		setUIComponents();
	}

	private void setUIComponents()
	{
		adminButton = (ImageButton) findViewById(R.id.admin_button);
		employeeButton = (ImageButton) findViewById(R.id.employee_button);
		adminButton.setOnClickListener(this);
		employeeButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		if(view == adminButton)
		{
			Intent registerIntent = new Intent(this ,AdminUploadPhotoActivity.class);
			startActivity(registerIntent);
		}
		else if (view == employeeButton)
		{
			Intent registerIntent = new Intent(this ,EmployeeUploadPhotoActivity.class);
			startActivity(registerIntent);
		}
	}
}
