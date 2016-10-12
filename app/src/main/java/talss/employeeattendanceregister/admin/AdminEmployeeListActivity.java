package talss.employeeattendanceregister.admin;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import backend.EmployeeHandler;
import model.Admin;
import model.Employee;
import services.LoginLocalStoreService;
import talss.employeeattendanceregister.R;

public class AdminEmployeeListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener
{
	private final String TAG = AdminEmployeeListActivity.class.getSimpleName();

	private ArrayList<Employee> employees = new ArrayList<>();
	private AdminEmployeesAdapter adminEmployeesAdapter;
	private ListView employeesListView;
	//private SearchView employeeSearchView;
	private LoginLocalStoreService localStoreService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_employee_list);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle("Users list");
		localStoreService = new LoginLocalStoreService(this);
		new PopulateListTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_bar_menu, menu);

		MenuItem searchItem = menu.findItem(R.id.menu_search_item);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setOnQueryTextListener(this);

		MenuItem editItem = menu.findItem(R.id.action_edit);
		editItem.setVisible(false);

		MenuItem deleteItem = menu.findItem(R.id.action_delete);
		deleteItem.setVisible(false);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Log.d(TAG, "onOptionsItemSelected");

		switch (item.getItemId())
		{
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

	private class PopulateListTask extends AsyncTask<Object, Void, Void>
	{

		@Override
		protected Void doInBackground(Object... params)
		{
			Log.d(TAG, "doInBackground");
			Admin admin = getAdminFromPreviousActivity();
			if(admin!=null)
			{
				employees = EmployeeHandler.getAdminEmployeeList(admin);
			}
		return null;
		}

		@Override
		protected void onPostExecute(Void aVoid)
		{
			super.onPostExecute(aVoid);
			setUIComponents();
		}

	}

	private Admin getAdminFromPreviousActivity()
	{
		Bundle bundle = getIntent().getExtras();
		return (Admin) bundle.get("admin");
	}

	private void setUIComponents()
	{
		adminEmployeesAdapter = new AdminEmployeesAdapter(this, employees);
		employeesListView = (ListView) findViewById(R.id.employee_list_view);
		employeesListView.setAdapter(adminEmployeesAdapter);
		employeesListView.setOnItemClickListener(this);
//		employeeSearchView = (SearchView) findViewById(R.id.employee_search_view);
//		employeeSearchView.setOnQueryTextListener(this);
	}


	private ArrayList<Employee> getDummyData()
	{
		ArrayList<Employee> dummyData = new ArrayList<>();

		for (int i = 1; i <=10; i++)
		{
			Employee employee = new Employee();
			employee.setEmployeeName("Talent "+ i*11);
			employee.setEmployeeSurname("Mataba " + i*12);
			employee.setEmployeeDesignation("Software Developer");
			employee.setEmployeeId(i+"");
			dummyData.add(employee);
		}
		return dummyData;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Log.v(TAG, "onItemClick");
		Employee employee = adminEmployeesAdapter.getItem(position);
		Intent intent = new Intent(getBaseContext(), AdminEmployeeViewActivity.class);
		intent.putExtra("employee", employee);
		Admin adminFromPreviousActivity = getAdminFromPreviousActivity();
		intent.putExtra("admin", adminFromPreviousActivity);
		startActivity(intent);
	}

	@Override
	public boolean onQueryTextSubmit(String query)
	{
		return false;
	}

	@Override
	public boolean onQueryTextChange(String searchTerm)
	{
		Log.d(TAG, "onQueryTextChange");
		ArrayList<Employee> filteredEmployees = getFiltered(employees, searchTerm);
		adminEmployeesAdapter = new AdminEmployeesAdapter(this, filteredEmployees);
		employeesListView.setAdapter(adminEmployeesAdapter);

		return true;
	}

	private ArrayList<Employee> getFiltered(ArrayList<Employee> employees, String searchTerm) {
		searchTerm = searchTerm.toLowerCase();

		ArrayList<Employee> filteredEmployeesList = new ArrayList<>();
		for (Employee employee : employees)
		{
			String text = employee.getEmployeeName()+" "+employee.getEmployeeSurname()+" "+employee.getEmployeeDesignation();
			text = text.toLowerCase();
			Log.d(TAG, text);
			if (text.contains(searchTerm)) {
				filteredEmployeesList.add(employee);
			}
		}
		return filteredEmployeesList;
	}

	@Override
	public void onBackPressed()
	{
		Log.v(TAG, "onBackPressed");
		loggingOut();
	}

}
