package talss.employeeattendanceregister.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import backend.AdminHandler;
import model.Admin;
import services.LoginLocalStoreService;
import talss.employeeattendanceregister.R;
import talss.lib.DataValidation;
import talss.lib.DialogAlert;

public class AdminSignUpActivity extends Activity implements View.OnClickListener
{

	private Button adminSignUpSaveButton;
	private EditText adminName, adminSurname, adminEmail, adminPassword, adminCompany;
	private LoginLocalStoreService localStoreService;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_sign_up);
		localStoreService = new LoginLocalStoreService(this);
		registerUIComponents();

	}

	private void registerUIComponents()
	{
		adminSignUpSaveButton = (Button) findViewById(R.id.admin_sign_up_save_button);
		adminSignUpSaveButton.setOnClickListener(this);
		adminName = (EditText) findViewById(R.id.admin_sign_up_name_edit_text);
		adminSurname = (EditText) findViewById(R.id.admin_sign_up_surname_edit_text);
		adminEmail = (EditText) findViewById(R.id.admin_sign_up_email_edit_text);
		adminPassword = (EditText) findViewById(R.id.admin_sign_up_password_edit_text);
		adminCompany = (EditText)findViewById(R.id.admin_sign_up_company_edit_text);
	}

	@Override
	public void onClick(View view)
	{
		if(view == adminSignUpSaveButton)
		{
			String name = this.adminName.getText().toString();
			String surname = this.adminSurname.getText().toString();
			String email = this.adminEmail.getText().toString();
			String password = this.adminPassword.getText().toString();
			String company = this.adminCompany.getText().toString();

			HashMap<String, String> data = new HashMap<>();
			data.put("name", name);
			data.put("surname", surname);
			data.put("email", email);
			data.put("password", password);
			data.put("company", company);

			if(DataValidation.isValid(data))
			{
				Admin admin = getAdminFromPreviousActivity();
				admin.setAdminName(name);
				admin.setAdminSurname(surname);
				admin.setAdminEmail(email);
				admin.setAdminPassword(password);
				admin.setAdminCompany(company);

				if(AdminHandler.AdminExists(admin))
				{
					DialogAlert.showAlert(this, "There is an existing account associated with "+admin.getAdminEmail(), "Error");
					return;
				}

				int insert = AdminHandler.addAdmin(admin);
				if(insert>0)
				{
					admin.setAdminId(String.valueOf(insert));
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
	}

	private Admin getAdminFromPreviousActivity()
	{
		Bundle bundle = getIntent().getExtras();
		Admin admin = (Admin) bundle.get("admin");
		if(admin!=null)
		{
			return admin;
		}
		return new Admin();
	}

}
