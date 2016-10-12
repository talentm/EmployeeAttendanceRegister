package helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import backend.AdminHandler;
import backend.AttendanceHandler;
import backend.EmployeeHandler;
import model.Admin;
import model.Attendance;
import model.Employee;

/**
 * Created by Talent on 7/22/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "register.db";
	private static final String TAG = DatabaseHelper.class.getSimpleName();

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		Log.d(TAG, "onCreate started ======================>");

		sqLiteDatabase.execSQL(AdminHandler.createAdminTable());
		sqLiteDatabase.execSQL(EmployeeHandler.createEmployeeTable());
		sqLiteDatabase.execSQL(EmployeeHandler.createAdminEmployeeTable());
		sqLiteDatabase.execSQL(AttendanceHandler.createAttendanceTable());
		sqLiteDatabase.execSQL(AttendanceHandler.createEmployeeAttendanceTable());

		Log.d(TAG, "onCreate finished ======================>");
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
	{
		Log.d(TAG, "SQLiteDatabase onUpgrade from " + oldVersion +"to " + newVersion);
		sqLiteDatabase.execSQL("DROP IF TABLE EXISTS " + Admin.TABLE);
		sqLiteDatabase.execSQL("DROP IF TABLE EXISTS " + Employee.TABLE);
		sqLiteDatabase.execSQL("DROP IF TABLE EXISTS " + "admin__employee");
		sqLiteDatabase.execSQL("DROP IF TABLE EXISTS " + Attendance.TABLE);
		sqLiteDatabase.execSQL("DROP IF TABLE EXISTS " + "employee__attendance");

		onCreate(sqLiteDatabase);
	}
}
