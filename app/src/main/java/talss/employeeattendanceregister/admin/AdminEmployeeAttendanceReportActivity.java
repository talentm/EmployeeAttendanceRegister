package talss.employeeattendanceregister.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import backend.EmployeeHandler;
import model.Attendance;
import model.Employee;
import services.LoginLocalStoreService;
import talss.employeeattendanceregister.R;
import talss.lib.DateTime;
import talss.tests.FinalAppBarTestActivity;

public class
AdminEmployeeAttendanceReportActivity extends AppCompatActivity implements View.OnClickListener
{
	private final String TAG = AdminEmployeeAttendanceReportActivity.class.getSimpleName();

	private EditText dateFrom, dateTo;
	private DatePickerDialog dateFromDialog, dateToDialog;
	private SimpleDateFormat dateFormat;

	private ArrayList<Attendance> attendances = new ArrayList<>();
	private AdminEmployeeAttendancesAdapter adminEmployeeAttendancesAdapter;
	private ListView attendancesListView;

	private Button report;
	private TextView totalTime;
	private String dateFromString = "";
	private String dateToString = "";
	private String totalTimeSpent ="";
	private LoginLocalStoreService localStoreService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_employee_attendance_report);
		localStoreService = new LoginLocalStoreService(this);
		dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		setUIComponents();
		setDateTimeField();
		setTitle("Attendance report");

		String currentDate = getCurreDate();
		String last7DaysDate = getLast7DaysDate(currentDate);

		new ReportTask().execute(last7DaysDate, currentDate);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_menu, menu);

		MenuItem searchItem = menu.findItem(R.id.menu_search_item);
		searchItem.setVisible(false);

		MenuItem editItem = menu.findItem(R.id.action_edit);
		editItem.setVisible(false);

		MenuItem deleteItem = menu.findItem(R.id.action_delete);
		deleteItem.setVisible(false);

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		Log.d(TAG, "onOptionsItemSelected");
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
			case R.id.action_logout:
				loggingOut();
				return true;

			case android.R.id.home:
				onBackPressed();
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


	private String getLast7DaysDate(String currentDate)
	{
		Calendar calendar = Calendar.getInstance();
		try
			{
				Date date = dateFormat.parse(currentDate);
				calendar.setTime(date);
			} catch (ParseException e)
			{
				e.printStackTrace();
			}
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		Date newDate = calendar.getTime();
		String dateFrom = dateFormat.format(newDate);
		return dateFrom;
	}

	private void setUIComponents()
	{
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		dateFrom = (EditText) findViewById(R.id.employee_report_date_from_text);
		dateFrom.setInputType(InputType.TYPE_NULL);
		dateFrom.requestFocus();
		dateFrom.setOnClickListener(this);

		dateTo = (EditText) findViewById(R.id.employee_report_date_to_text);
		dateTo.setInputType(InputType.TYPE_NULL);
		dateTo.requestFocus();
		dateTo.setOnClickListener(this);

		report = (Button) findViewById(R.id.employee_attendance_report_button);
		report.setOnClickListener(this);

		totalTime = (TextView) findViewById(R.id.attendance_report_total_time_text);
		totalTime.setText("Total time spent : ");
	}

	private void setDateTimeField()
	{

		Calendar newCalendar = Calendar.getInstance();
		dateFromDialog = new DatePickerDialog(this,R.style.DialogTheme ,new DatePickerDialog.OnDateSetListener()
		{

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
			{
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				dateFrom.setText(dateFormat.format(newDate.getTime()));
				dateFromString = dateFrom.getText().toString();
			}

		},
		newCalendar.get(Calendar.YEAR),
		newCalendar.get(Calendar.MONTH),
		newCalendar.get(Calendar.DAY_OF_MONTH));

		dateToDialog = new DatePickerDialog(this,R.style.DialogTheme ,new DatePickerDialog.OnDateSetListener()
		{

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
			{
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				dateTo.setText(dateFormat.format(newDate.getTime()));
				dateToString = dateTo.getText().toString();
			}

		},
		newCalendar.get(Calendar.YEAR),
		newCalendar.get(Calendar.MONTH),
		newCalendar.get(Calendar.DAY_OF_MONTH));
	}

	@Override
	public void onClick(View view)
	{
		if(view == dateFrom)
		{
			dateFromDialog.show();
		}
		else if(view == dateTo)
		{
			dateToDialog.show();
		}
		else if(view == report)
		{
			boolean dateAfter = isDateAfter();
			if(dateFromString.equals("Date from") || dateFromString.isEmpty())
			{
				Toast.makeText(this, "Please select date from", Toast.LENGTH_LONG);
			}
			else if(dateToString.equals("Date to") || dateToString.isEmpty())
			{
				Toast.makeText(this, "Please select date to", Toast.LENGTH_LONG);
			}
			else if(!isDateAfter())
			{
				Toast.makeText(this, "Select a valid date from", Toast.LENGTH_LONG);
			}
			else
			{
				getAttendanceReport(dateFromString, dateToString);
			}
		}
	}

	private void getAttendanceReport(String dateFrom, String dateTo)
	{
		new ReportTask().execute(dateFrom, dateTo);
	}

	private boolean isDateAfter()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		boolean after = false;
		try
		{
			Date fromDate = format.parse(dateFromString);
			Date toDate = format.parse(dateToString);
			after = toDate.after(fromDate);
			return after;
		} catch (ParseException e)
		{
			e.printStackTrace();
			return after;
		}
	}

	private String getCurreDate()
	{
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		return day+"-"+month+"-"+year;
	}

	private class ReportTask extends AsyncTask<Object, Void, Void>
	{

		@Override
		protected Void doInBackground(Object... params)
		{
			Log.v(TAG, "doInBackground");
			Bundle bundle = getIntent().getExtras();
			Employee employee = (Employee) bundle.get("employee");

			String fromDate = (String) params[0];
			String toDate = (String) params[1];

			if(employee!=null)
			{
				attendances = EmployeeHandler.getEmployeeAttendanceList(employee, fromDate, toDate);

				int sum = 0;
				for (Attendance attendance : attendances)
				{
					int intTimeDifference = DateTime.getIntTimeDifference(attendance.getAttendanceCheckIn(), attendance.getAttendanceCheckOut());
					sum+=intTimeDifference;
				}

				totalTimeSpent = DateTime.getHoursAndMinutesFromMinutes(sum);

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid)
		{
			Log.v(TAG, "onPostExecute");
			super.onPostExecute(aVoid);
			setListAdapter();
		}
	}

	private void setListAdapter()
	{
		adminEmployeeAttendancesAdapter = new AdminEmployeeAttendancesAdapter(this, attendances);
		attendancesListView = (ListView) findViewById(R.id.attendance_list_view);
		attendancesListView.setAdapter(adminEmployeeAttendancesAdapter);
		totalTime.setText("Total time spent : "+totalTimeSpent);
	}

}
