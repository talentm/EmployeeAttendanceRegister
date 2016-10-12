package talss.employeeattendanceregister.employee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import backend.AdminHandler;
import model.Admin;
import services.LoginLocalStoreService;
import talss.employeeattendanceregister.R;
import talss.employeeattendanceregister.admin.AdminLogInActivity;

public class EmployeeAdminListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener
{
	private final String TAG = EmployeeAdminListActivity.class.getSimpleName();

	private ArrayList<Admin> admins = new ArrayList<>();
	private EmployeeAdminsAdapter employeeAdminsAdapter;
	private ListView adminsListView;
	private LoginLocalStoreService localStoreService;
//	private SearchView adminsSearchView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_admin_list);
		localStoreService = new LoginLocalStoreService(this);
		setUIComponents();
		setTitle("Admins list");
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

		MenuItem logoutItem = menu.findItem(R.id.action_logout);
		logoutItem.setVisible(false);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
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

	private void setUIComponents()
	{
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		admins = AdminHandler.getAdminList();
		//ArrayList<Admin> admins = getDummyData();
		employeeAdminsAdapter = new EmployeeAdminsAdapter(this, admins);
		adminsListView = (ListView) findViewById(R.id.admin_list_view);
		adminsListView.setAdapter(employeeAdminsAdapter);
		adminsListView.setOnItemClickListener(this);
//		adminsSearchView = (SearchView) findViewById(R.id.admin_search_view);
//		adminsSearchView.setOnQueryTextListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Log.d(TAG, "onItemClick");
		Admin admin = employeeAdminsAdapter.getItem(position);
		Intent intent = new Intent();
		intent.putExtra("admin", admin);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onQueryTextSubmit(String query)
	{
		return false;
	}

	@Override
	public boolean onQueryTextChange(String searchTerm)
	{
		ArrayList<Admin> filteredList = getFilteredList(admins, searchTerm);
		employeeAdminsAdapter = new EmployeeAdminsAdapter(this, filteredList);
		adminsListView.setAdapter(employeeAdminsAdapter);
		return true;
	}

	private ArrayList<Admin> getFilteredList(ArrayList<Admin> admins, String searchTerm)
	{
		searchTerm = searchTerm.toLowerCase();

		ArrayList<Admin> filteredList = new ArrayList<>();
		for(Admin admin : admins)
		{
			String text = admin.getAdminName() + " " + admin.getAdminSurname();
			text = text.toLowerCase();
			Log.d(TAG, text);
			if(text.contains(searchTerm))
				filteredList.add(admin);
		}
		return filteredList;
	}


	private ArrayList<Admin> getDummyData()
	{
		ArrayList<Admin> dummyData = new ArrayList<>();

		for (int i = 1; i <=10; i++)
		{
			Admin admin = new Admin();
			admin.setAdminName("Talent "+ i*11);
			admin.setAdminSurname("Mataba " + i*12);
			//admin.setAdminDesignation("Software Developer");
			admin.setAdminId(i+"");
			dummyData.add(admin);
		}
		return dummyData;
	}
}
