package talss.employeeattendanceregister.employee;

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

import model.Admin;
import talss.employeeattendanceregister.R;

/**
 * Created by Talent on 7/31/2016.
 */
public class EmployeeAdminsAdapter extends ArrayAdapter<Admin>
{
	public EmployeeAdminsAdapter(Context context, ArrayList<Admin> admins)
	{
		super(context, 0, admins);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Admin admin = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_admin, parent, false);
		}
		// Lookup view for data population
		TextView nameAndSurname = (TextView) convertView.findViewById(R.id.admin_name_surname);
		TextView companyName = (TextView) convertView.findViewById(R.id.admin_company_name);
		ImageView adminProfile = (ImageView) convertView.findViewById(R.id.admin_profile);

		// Populate the data into the template view using the data object
		nameAndSurname.setText(admin.getAdminName()+" "+admin.getAdminSurname());
        companyName.setText(admin.getAdminCompany());
		byte[] adminImageBytes = admin.getAdminImageBytes();
		Bitmap bitmap = BitmapFactory.decodeByteArray(adminImageBytes, 0, adminImageBytes.length);
		adminProfile.setImageBitmap(bitmap);

		return convertView;
	}
}