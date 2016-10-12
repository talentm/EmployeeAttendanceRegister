package talss.employeeattendanceregister.admin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import model.Employee;
import talss.employeeattendanceregister.R;

/**
 *
 * Created by Talent on 7/25/2016.
 */
public class AdminEmployeesAdapter extends ArrayAdapter<Employee>
{
	public AdminEmployeesAdapter(Context context, ArrayList<Employee> employees)
	{
		super(context, 0, employees);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Employee employee = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_employee, parent, false);
		}
		// Lookup view for data population
		TextView nameAndSurname = (TextView) convertView.findViewById(R.id.employee_name_surname);
		TextView designation = (TextView) convertView.findViewById(R.id.employee_designation);
		ImageView employeeProfile = (ImageView) convertView.findViewById(R.id.employee_profile);

		// Populate the data into the template view using the data object
		nameAndSurname.setText(employee.getEmployeeName()+" "+employee.getEmployeeSurname());
		designation.setText(employee.getEmployeeDesignation());

		byte[] employeeImageBytes = employee.getEmployeeImageBytes();
		Bitmap bitmap = BitmapFactory.decodeByteArray(employeeImageBytes, 0, employeeImageBytes.length);
		employeeProfile.setImageBitmap(bitmap);

		return convertView;
	}
}
