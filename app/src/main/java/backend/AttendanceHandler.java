package backend;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import helpers.DatabaseManager;
import model.Admin;
import model.Attendance;
import model.Employee;

/**
 * Created by Talent on 7/22/2016.
 */
public class AttendanceHandler
{
	private static final String TAG = AttendanceHandler.class.getSimpleName();
	public AttendanceHandler()
	{
	}

	public static String createAttendanceTable()
	{
		String sql = "CREATE TABLE " + Attendance.TABLE + "( "
					+ Attendance.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ Attendance.CHECK_IN + " TEXT, "
					+ Attendance.CHECK_OUT + " TEXT, "
				    + Attendance.DATE + " TEXT )";
		Log.v(TAG, "createAttendanceTable");
		return sql;
	}

	public static String createEmployeeAttendanceTable()
	{
		String sql = "CREATE TABLE employee__attendance ( " + Employee.ID + " TEXT, " + Attendance.ID + " TEXT ) ";
		Log.v(TAG, "createEmployeeAttendanceTable");
		return sql;
	}

	public static int addAttendance(Attendance attendance)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		ContentValues values = new ContentValues();
		values.put(Attendance.CHECK_IN, attendance.getAttendanceCheckIn());
		values.put(Attendance.CHECK_OUT, attendance.getAttendanceCheckOut());
		values.put(Attendance.DATE, attendance.getAttendanceDate());

		int insert =  (int) db.insert(Attendance.TABLE, null, values);
		DatabaseManager.getInstance().closeDatabase();
		return insert;
	}

	public static void editAttendance(Attendance attendance)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//		ContentValues values = new ContentValues();
//		if(attendance.getAttendanceCheckIn()!=null)
//		{
//			values.put(Attendance.CHECK_IN, attendance.getAttendanceCheckIn());
//		}
//		if(attendance.getAttendanceCheckOut()!=null)
//		{
//			values.put(Attendance.CHECK_OUT, attendance.getAttendanceCheckOut());
//		}
//		if(attendance.getAttendanceDate()!=null)
//		{
//			values.put(Attendance.DATE, attendance.getAttendanceDate());
//		}

		String sql = "UPDATE attendance SET attendance_check_out = "+"'"+attendance.getAttendanceCheckOut()+"'"+ " WHERE attendance_id = "+"'"+attendance.getAttendanceId()+"'";
//		int update = db.update(Attendance.TABLE, values, " attendance_id = ? ", new String[]{attendance.getAttendanceId()});
		Log.v(TAG, sql);
		db.execSQL(sql);
		DatabaseManager.getInstance().closeDatabase();
	}

	public static Attendance getAttendance(String attendanceDate, String employeeId)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		Attendance attendance = null;

		String sql = "SELECT attendance.attendance_id, attendance.attendance_check_in, attendance.attendance_check_out, attendance.attendance_date  " +
					 "FROM " + Attendance.TABLE +" " +
				     "INNER JOIN employee__attendance " +
				     "ON employee__attendance.attendance_id = attendance.attendance_id " +
					 "WHERE " + Attendance.DATE + "=" + "'"+attendanceDate+"'" +" AND "+ Employee.ID + " = '" + employeeId +"'";

//		"SELECT attendance.attendance_id, attendance.attendance_check_in, attendance.attendance_check_out, attendance.attendance_date " +
//				"FROM " + Attendance.TABLE + " " +
//				"INNER JOIN employee__attendance ON employee__attendance.attendance_id = attendance.attendance_id " +
//				"WHERE " + Employee.ID + " = '" + employee.getEmployeeId() +"'";

		Log.d(TAG, sql);
		Cursor result = db.rawQuery(sql, null);
		DatabaseManager.getInstance().closeDatabase();

		while (result.moveToNext())
		{
			attendance = new Attendance();
			attendance.setAttendanceId(result.getString(0));
			attendance.setAttendanceCheckIn(result.getString(1));
			attendance.setAttendanceCheckOut(result.getString(2));
			attendance.setAttendanceDate(result.getString(3));
		}
		return attendance;
	}

	public static int linkAttendanceToEmployee(String attendanceId, String employeeId)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		ContentValues values = new ContentValues();
		values.put(Attendance.ID, attendanceId);
		values.put(Employee.ID, employeeId);
		int insert = (int) db.insert("employee__attendance", null, values);
		DatabaseManager.getInstance().closeDatabase();
		return insert;
	}
}
