package services;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import model.Admin;
import model.Employee;

/**
 * Created by Talent Mataba on 9/8/2016.
 */
public class
LoginLocalStoreService
{
	private static final String TAG = LoginLocalStoreService.class.getSimpleName();
	private static final String SHARED_PREFERENCES_NAME = "UserLogInDetails";
	SharedPreferences sharedPreferences;

	public LoginLocalStoreService(Context context)
	{
		sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
	}

	public void storeUserData(Employee employee, Admin admin)
	{
		Log.v(TAG, "storeUserData");
		SharedPreferences.Editor spEditor = sharedPreferences.edit();

		if(employee!=null)
		{
			spEditor.putString("employeeId", employee.getEmployeeId());
			spEditor.putString("employeeName", employee.getEmployeeName());
			spEditor.putString("employeeSurname", employee.getEmployeeSurname());
			spEditor.putString("employeeEmail", employee.getEmployeeEmail());
			spEditor.putString("employeePassword", employee.getEmployeePassword());
//			spEditor.putString("employeePhoto", employee.getEmployeeImageBytes());
			spEditor.putString("employeeAddress", employee.getEmployeeAddress());
			spEditor.putString("employeeDesignation", employee.getEmployeeDesignation());
		}
		else if(admin!=null)
		{
			spEditor.putString("adminId", admin.getAdminId());
			spEditor.putString("adminName", admin.getAdminName());
			spEditor.putString("adminSurname", admin.getAdminSurname());
			spEditor.putString("adminEmail", admin.getAdminEmail());
			spEditor.putString("adminPassword", admin.getAdminPassword());
			//spEditor.putString("adminPhoto", admin.getAdminImageBytes());
		}
		spEditor.commit();
	}

	public Employee getLoggedInEmployee()
	{
		Log.v(TAG, "getLoggedInEmployee");
		Employee employee = new Employee();

		String employeeId = sharedPreferences.getString("employeeId", "");
		String employeeName = sharedPreferences.getString("employeeName", "");
		String employeeSurname = sharedPreferences.getString("employeeSurname", "");
		String employeeEmail = sharedPreferences.getString("employeeEmail", "");
		String employeePassword = sharedPreferences.getString("employeePassword", "");
		String employeePhoto = sharedPreferences.getString("employeePhoto", "");
		String employeeAddress = sharedPreferences.getString("employeeAddress", "");
		String employeeDesignation = sharedPreferences.getString("employeeDesignation", "");
		if(!employeeId.isEmpty())
		{
			employee.setEmployeeId(employeeId);
			employee.setEmployeeName(employeeName);
			employee.setEmployeeSurname(employeeSurname);
			employee.setEmployeeEmail(employeeEmail);
			employee.setEmployeePassword(employeePassword);
//			employee.setEmployeeImageBytes(employeePhoto);
			employee.setEmployeeAddress(employeeAddress);
			employee.setEmployeeDesignation(employeeDesignation);
			return employee;
		}
		return null;
	}

	public Admin getLoggedInAdmin()
	{
		Log.v(TAG, "getLoggedInAdmin");
		Admin admin = new Admin();
		String adminId = sharedPreferences.getString("adminId", "");
		String adminName = sharedPreferences.getString("adminName", "");
		String adminSurname = sharedPreferences.getString("adminSurname", "");
		String adminEmail = sharedPreferences.getString("adminEmail", "");
		String adminPassword = sharedPreferences.getString("adminPassword", "");
		//String adminPhoto = sharedPreferences.getString("adminPhoto", "");
		if(!adminName.isEmpty())
		{
			admin.setAdminId(adminId);
			admin.setAdminName(adminName);
			admin.setAdminSurname(adminSurname);
			admin.setAdminEmail(adminEmail);
			admin.setAdminPassword(adminPassword);
			//admin.setAdminImageBytes(adminPhoto);
			return admin;
		}
		return null;
	}

	public void setEmployeeLoggedIn(boolean employeeLoggedIn)
	{
		Log.v(TAG, "setEmployeeLoggedIn");
		SharedPreferences.Editor spEditor = sharedPreferences.edit();
		spEditor.putBoolean("employeeLoggedIn", employeeLoggedIn);
		spEditor.commit();
	}

	public void setAdminLoggedIn(boolean adminLoggedIn)
	{
		Log.v(TAG, "setAdminLoggedIn");
		SharedPreferences.Editor spEditor = sharedPreferences.edit();
		spEditor.putBoolean("adminLoggedIn", adminLoggedIn);
		spEditor.commit();
	}

	public boolean isEmployeeLoggedIn()
	{
		return sharedPreferences.getBoolean("employeeLoggedIn", false);
	}

	public boolean isAdminLoggedIn()
	{
		return sharedPreferences.getBoolean("adminLoggedIn", false);
	}

	public void clearUserData()
	{
		Log.v(TAG, "clearUserData");
		SharedPreferences.Editor spEditor = sharedPreferences.edit();
		spEditor.clear();
		spEditor.commit();
	}
}
