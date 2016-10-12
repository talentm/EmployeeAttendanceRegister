package talss.employeeattendanceregister.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import model.Attendance;
import talss.employeeattendanceregister.R;

/**
 * Created by Talent on 8/27/2016.
 */
public class
AdminEmployeeAttendancesAdapter extends ArrayAdapter<Attendance>
{
	public AdminEmployeeAttendancesAdapter(Context context, ArrayList<Attendance> attendances)
	{
		super(context,0, attendances);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Attendance attendance = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_attendance, parent, false);
		}
		// Lookup view for data population
		TextView date = (TextView) convertView.findViewById(R.id.employee_attendance_date);
		TextView checkIn = (TextView) convertView.findViewById(R.id.employee_attendance_check_in_time);
		TextView checkOut = (TextView) convertView.findViewById(R.id.employee_attendance_check_out_time);
		TextView totalHours = (TextView) convertView.findViewById(R.id.employee_attendance_hours_spent);

		// Populate the data into the template view using the data object
		date.setText("Date : "+attendance.getAttendanceDate());
		checkIn.setText("Check in : "+attendance.getAttendanceCheckIn());
		checkOut.setText("Check out : "+attendance.getAttendanceCheckOut());
		String timeDifference = getTimeDifference(attendance.getAttendanceCheckIn(), attendance.getAttendanceCheckOut());
		totalHours.setText("Time : "+timeDifference);

		return convertView;
	}

	private String getTimeDifference(String checkIn, String checkOut)
	{
		String[]checkOutTime = checkOut.split(":");
		int checkOutHours = Integer.parseInt(checkOutTime[0]);
		int checkOutMins = Integer.parseInt(checkOutTime[1]);

		String[]checkInTime = checkIn.split(":");
		int checkInHours = Integer.parseInt(checkInTime[0]);
		int checkInMins = Integer.parseInt(checkInTime[1]);

		int diffMins = checkOutMins - checkInMins;
		int diffHours = checkOutHours - checkInHours;

		if(diffMins<0)
		{
			diffMins+=60;
			diffHours-=1;
			if(diffHours == -24)
				diffHours = 0;
		}

		return diffHours+"h " + diffMins +"m ";
	}
}
