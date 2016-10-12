package talss.employeeattendanceregister.attendance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import backend.AttendanceHandler;
import model.Attendance;
import model.Employee;
import services.LoginLocalStoreService;
import talss.employeeattendanceregister.R;
import talss.employeeattendanceregister.admin.AdminLogInActivity;

public class

EmployeeAttendanceActivity extends AppCompatActivity implements View.OnClickListener
{
	private static final String TAG = EmployeeAttendanceActivity.class.getSimpleName();

	private TextView checkInText;
	private TextView checkOutText;
	private TextView fullDateText;
//	private TextView totalTimeText;
	private ImageButton checkInButton;
	private ImageButton checkOutButton;
	private String currentTime;
	private String currentShortTime;
	private String currentFullTime;
	private String currentDate;
	private LoginLocalStoreService localStoreService;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_attendance);
		localStoreService = new LoginLocalStoreService(this);
		setUIComponents();
		startClockThread();
		setTimeLoggingButtons();
		setTitle("Log time");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_bar_menu, menu);

		MenuItem searchItem = menu.findItem(R.id.menu_search_item);
		searchItem.setVisible(false);

		MenuItem editItem = menu.findItem(R.id.action_edit);
		editItem.setVisible(false);

		MenuItem deleteItem = menu.findItem(R.id.action_delete);
		deleteItem.setVisible(false);

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

		checkInText = (TextView) findViewById(R.id.employee_attendance_check_in_text);
		checkOutText = (TextView) findViewById(R.id.employee_attendance_check_out_text);
		fullDateText = (TextView) findViewById(R.id.employee_attendance_full_date);
		//totalTimeText = (TextView) findViewById(R.id.employee_attendance_hours_spent_text);
		checkInButton = (ImageButton) findViewById(R.id.employee_attendance_check_in_button);
		checkOutButton = (ImageButton) findViewById(R.id.employee_attendance_check_out_button);
		checkInButton.setOnClickListener(this);
		checkOutButton.setOnClickListener(this);

	}

	private void setTimeLoggingButtons()
	{
		currentDate = getCurreDate();
		Employee employee = getEmployee();
		Attendance attendance = AttendanceHandler.getAttendance(currentDate, employee.getEmployeeId());
		if(attendance!=null)
		{
			if(attendance.getAttendanceCheckIn()!=null)
			{
				checkInText.setText("Check in time : " + attendance.getAttendanceCheckIn());
				checkInButton.setEnabled(false);
				checkInButton.setImageResource(R.drawable.check_in_grey);
			}

			if(attendance.getAttendanceCheckOut()!=null)
			{
				checkOutText.setText("Check out time : " + attendance.getAttendanceCheckOut());
				checkOutButton.setEnabled(false);
				checkOutButton.setImageResource(R.drawable.check_out_grey);
			}

//			if(attendance.getAttendanceCheckIn()!=null && attendance.getAttendanceCheckOut()!=null)
//			{
//				String timeDifference = getTimeDifference(attendance.getAttendanceCheckIn(), attendance.getAttendanceCheckOut());
//				totalTimeText.setText("Total time spent : " + timeDifference);
//			}
		}
	}

	private Employee getEmployee()
	{
		Bundle bundle = getIntent().getExtras();
		Employee employee = (Employee) bundle.get("employee");
		return employee;
	}

	private void startClockThread()
	{
		Thread myThread = null;
		Runnable myRunnableThread = new CountDownRunner();
		myThread= new Thread(myRunnableThread);
		myThread.start();
	}

	public void doWork()
	{
		runOnUiThread(new Runnable()
		{
			public void run() {
				try
				{
					Calendar calendar = Calendar.getInstance();

					currentDate = getCurreDate();
					int hours = calendar.get(Calendar.HOUR_OF_DAY);
					int minutes = calendar.get(Calendar.MINUTE);
					int seconds = calendar.get(Calendar.SECOND);
					currentTime = hours + ":" + minutes + ":" + seconds;
					currentShortTime = hours + ":" + minutes;
					currentFullTime = currentDate +" "+currentTime;
//					long parsedDateTime = DateFormat.parseDateTime(currentFullTime);
//					String formattedHumanDate = DateFormat.formatDateTime(parsedDateTime);
//					fullDateText.setText(formattedHumanDate);
					fullDateText.setText(currentFullTime);
				}
				catch (Exception e) {}
			}
		});
	}

	class CountDownRunner implements Runnable
	{

		// @Override
		public void run()
		{
			while(!Thread.currentThread().isInterrupted()){
				try
				{
					doWork();
					Thread.sleep(1000); // Pause of 1 Second
				} catch (InterruptedException e)
				{
					Thread.currentThread().interrupt();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void onClick(View view)
	{
		if(view == checkInButton)
		{
			checkInText.setText("Check in time : " + currentShortTime);
		    checkInButton.setEnabled(false);
			checkInButton.setImageResource(R.drawable.check_in_grey);

			Attendance attendance = new Attendance();
			attendance.setAttendanceCheckIn(currentShortTime);
			attendance.setAttendanceDate(currentDate);

			int attendanceId = AttendanceHandler.addAttendance(attendance);
			Employee employee = getEmployee();
			Log.v(TAG, employee +"    "+attendanceId);
			if(attendanceId > 0 && employee != null)
			{
				AttendanceHandler.linkAttendanceToEmployee(String.valueOf(attendanceId), employee.getEmployeeId());
			}
		}
		else
		if(view == checkOutButton)
		{
			if(!checkInButton.isEnabled())
			{
				Employee employee = getEmployee();
				Attendance attendance = AttendanceHandler.getAttendance(currentDate, employee.getEmployeeId());

				attendance.setAttendanceCheckOut(currentShortTime);
				//edit the last entry of attendance table
				AttendanceHandler.editAttendance(attendance);

				checkOutText.setText("Check out time : " + currentShortTime);
//				String timeDifference = getTimeDifference(attendance.getAttendanceCheckIn(), currentShortTime);
//				totalTimeText.setText("Total time spent : " + timeDifference);
				checkOutButton.setEnabled(false);
				checkOutButton.setImageResource(R.drawable.check_out_grey);

			}
			else
			{
				Toast.makeText(getBaseContext(), "You need to check in first", Toast.LENGTH_LONG);
			}
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

	private String getTimeDifference(String checkIn, String checkOut)
	{
		Log.v(TAG, "getTimeDifference " + checkIn+" "+checkOut);
		int diffHours = 0;
		int diffMinutes = 0;
		try
		{
			String[] checkOutTime = checkOut.split(":");
			int checkOutHours = Integer.parseInt(checkOutTime[0]);
			int checkOutMinutes = Integer.parseInt(checkOutTime[1]);

			String[] checkInTime = checkIn.split(":");
			int checkInHours = Integer.parseInt(checkInTime[0]);
			int checkInMinutes = Integer.parseInt(checkInTime[1]);

			diffMinutes = checkOutMinutes - checkInMinutes;
			diffHours = checkOutHours - checkInHours;

			if (diffMinutes < 0)
			{
				diffMinutes += 60;
				diffHours -= 1;
				if (diffHours == -24)
				{
					diffHours = 0;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return diffHours+"h " + diffMinutes +"m ";
	}
}
