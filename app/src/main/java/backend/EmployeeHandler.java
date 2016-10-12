package backend;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import helpers.DatabaseManager;
import model.Admin;
import model.Attendance;
import model.Employee;

/**
 *
 * Created by Talent on 7/22/2016.
 *
 */
public class EmployeeHandler
{
	private final static String TAG = EmployeeHandler.class.getSimpleName();

	public EmployeeHandler()
	{
	}

	public static String createEmployeeTable()
	{
		String sql = "CREATE TABLE " + Employee.TABLE + "( "
				+ Employee.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Employee.NAME + " TEXT, "
				+ Employee.SURNAME + " TEXT, "
				+ Employee.EMAIL +" TEXT, "
				+ Employee.PASSWORD + " TEXT, "
				+ Employee.PHOTO+" TEXT, "
				+ Employee.ADDRESS +" TEXT, "
				+ Employee.DESIGNATION+" TEXT, "
				+ Employee.COORDINATES+" TEXT )";

		Log.d(TAG, "createEmployeeTable");
		return sql;
	}

	public static String createAdminEmployeeTable()
	{
		String sql = "CREATE TABLE " + "admin__employee" + "( "
				+ Admin.ID + " INTEGER , "
				+ Employee.ID + " INTEGER )";

		Log.d(TAG, "createAdminEmployeeTable");
		return sql;
	}

	public static int linkEmployeeToAdmin(String adminId, String employeeId)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		ContentValues values = new ContentValues();
		values.put(Admin.ID, adminId);
		values.put(Employee.ID, employeeId);
		int insert = (int) db.insert("admin__employee", null, values);
		DatabaseManager.getInstance().closeDatabase();
		return insert;
	}
	public static int addEmployee(Employee employee)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		ContentValues values = new ContentValues();
		values.put(Employee.NAME, employee.getEmployeeName());
		values.put(Employee.SURNAME, employee.getEmployeeSurname());
		values.put(Employee.EMAIL, employee.getEmployeeEmail());
		values.put(Employee.PASSWORD, employee.getEmployeePassword());
		values.put(Employee.PHOTO, employee.getEmployeeImageBytes());
		values.put(Employee.ADDRESS, employee.getEmployeeAddress());
		values.put(Employee.DESIGNATION, employee.getEmployeeDesignation());
		values.put(Employee.COORDINATES, employee.getEmployeeCoordinates());

		int insert = (int) db.insert(Employee.TABLE, null, values);
		DatabaseManager.getInstance().closeDatabase();
		return insert;
	}

	public static void editEmployee(Employee employee)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//		ContentValues values = new ContentValues();
//		values.put(Employee.NAME, employee.getEmployeeName());
//		values.put(Employee.SURNAME, employee.getEmployeeSurname());
//		values.put(Employee.EMAIL, employee.getEmployeeEmail());
//		values.put(Employee.PASSWORD, employee.getEmployeePassword());
//		values.put(Employee.PHOTO, employee.getEmployeeImageBytes());
//		values.put(Employee.ADDRESS, employee.getEmployeeAddress());
//		values.put(Employee.DESIGNATION, employee.getEmployeeDesignation());
//		values.put(Employee.COORDINATES, employee.getEmployeeCoordinates());
//		int update = db.update(Employee.TABLE, values, "ID = ?", new String[]{employee.getEmployeeId()});

		String sql = "UPDATE " + Employee.TABLE + " SET "+Employee.NAME+" = '"+employee.getEmployeeName()+"', "+Employee.SURNAME+" = '"+employee.getEmployeeSurname()+"', "+Employee.EMAIL+" = '"+employee.getEmployeeEmail()+"', "+Employee.PASSWORD
				+" = '"+employee.getEmployeePassword()+"', "+Employee.PHOTO+" = '"+employee.getEmployeeImageBytes()+"', "+Employee.ADDRESS+" = '"+employee.getEmployeeAddress()+"', "+Employee.DESIGNATION+" = '"+employee.getEmployeeDesignation()+"', "+Employee.COORDINATES+" = '"+employee.getEmployeeCoordinates()+"'"+ " WHERE "+Employee.ID+" = '"+employee.getEmployeeId()+"'" ;

		Log.d(TAG, sql);
		db.execSQL(sql);
		DatabaseManager.getInstance().closeDatabase();
	}

	public static void deleteEmployee(Employee employee)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		String sql = "DELETE FROM "+Employee.TABLE+ " WHERE "+Employee.ID+" = '"+employee.getEmployeeId()+"'";
		db.execSQL(sql);
		Log.d(TAG, sql);
		DatabaseManager.getInstance().closeDatabase();
	}

//	public Employee getEmployee(String employeeId)
//	{
//		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//		Employee employee = new Employee();
//
//		String sql = "SELECT * " +
//				"FROM " + Employee.TABLE +" " +
//				"WHERE " + Employee.ID + "=" + employeeId;
//		Log.d(TAG, sql);
//		Cursor result = db.rawQuery(sql, null);
//        DatabaseManager.getInstance().closeDatabase();
//
//		if(result!=null)
//		{
//			employee.setEmployeeId(result.getString(0));
//			employee.setEmployeeName(result.getString(1));
//			employee.setEmployeeSurname(result.getString(2));
//			employee.setEmployeeEmail(result.getString(3));
//			employee.setEmployeePassword(result.getString(4));
//			employee.setEmployeeImageBytes(result.getString(5));
//			employee.setEmployeeAddress(result.getString(6));
//			employee.setEmployeeDesignation(result.getString(7));
//			employee.setEmployeeCoordinates(result.getString(8));
//
//		}
//		return employee;
//	}

	public static boolean EmployeeExists(Employee employee)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

		String sql = "SELECT * " +
				"FROM " + Employee.TABLE +" " +
				"WHERE " + Employee.EMAIL+ " = '" + employee.getEmployeeEmail() + "'"+
				" AND " + Employee.PASSWORD + " = '" + employee.getEmployeePassword() + "'";

		Log.d(TAG, sql);
		Cursor result = db.rawQuery(sql, null);
		DatabaseManager.getInstance().closeDatabase();

		if(result.getCount()>0)
		{
			return true;
		}
		return false;
	}

	public static Employee getEmployee(Employee employee)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

		String sql = "SELECT * " +
				"FROM " + Employee.TABLE +" " +
				"WHERE " + Employee.EMAIL+ " = '" + employee.getEmployeeEmail() + "'"+
				" AND " + Employee.PASSWORD + " = '" + employee.getEmployeePassword() + "'";

		Log.d(TAG, sql);
		Cursor result = db.rawQuery(sql, null);
		DatabaseManager.getInstance().closeDatabase();

		//Just getting one employee. need a fix
		Employee resultEmployee = null;
		while (result.moveToNext())
		{
			resultEmployee = new Employee();
			resultEmployee.setEmployeeId(result.getString(0));
			resultEmployee.setEmployeeName(result.getString(1));
			resultEmployee.setEmployeeSurname(result.getString(2));
			resultEmployee.setEmployeeEmail(result.getString(3));
			resultEmployee.setEmployeePassword(result.getString(4));
			resultEmployee.setEmployeeImageBytes(result.getBlob(5));
			resultEmployee.setEmployeeAddress(result.getString(6));
			resultEmployee.setEmployeeDesignation(result.getString(7));
			resultEmployee.setEmployeeCoordinates(result.getString(8));
		}
		return resultEmployee;
	}


	public  static ArrayList<Attendance> getEmployeeAttendanceList(Employee employee, String dateFrom, String dateTo)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		ArrayList<Attendance> attendanceList = new ArrayList<>();
		String sql =
				"SELECT attendance.attendance_id, attendance.attendance_check_in, attendance.attendance_check_out, attendance.attendance_date " +
				"FROM " + Attendance.TABLE + " " +
				"INNER JOIN employee__attendance ON employee__attendance.attendance_id = attendance.attendance_id " +
				"WHERE " + Employee.ID + " = '" + employee.getEmployeeId() +"'" +" AND " + Attendance.DATE +" >=  '"+dateFrom+"' " +" AND " + Attendance.DATE +" <= '"+dateTo+"' ";
		Log.v(TAG, sql);

		Cursor result =  db.rawQuery(sql, null);
		DatabaseManager.getInstance().closeDatabase();

		while (result.moveToNext())
		{
			Attendance attendance = new Attendance();
			attendance.setAttendanceId(result.getString(0));
			attendance.setAttendanceCheckIn(result.getString(1));
			attendance.setAttendanceCheckOut(result.getString(2));
			attendance.setAttendanceDate(result.getString(3));

			//populate the list
			attendanceList.add(attendance);
		}
		return attendanceList;
	}

	public static ArrayList<Employee> getAdminEmployeeList(Admin admin)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		ArrayList<Employee> employeeList = new ArrayList<>();
		String sql =
				"SELECT employee.employee_id, employee.employee_name, employee_surname,employee_email, employee_password, employee_photo, employee_address, employee_designation, employee_coordinates   " +
				" FROM " + Employee.TABLE + " " +
				"INNER JOIN admin__employee ON admin__employee.employee_id = employee.employee_id " +
				"WHERE " +Admin.ID+ " = '" + admin.getAdminId() + "'";

		Log.d(TAG, sql);
		Cursor result =  db.rawQuery(sql, null);
        DatabaseManager.getInstance().closeDatabase();

		while (result.moveToNext())
		{
			Employee employee = new Employee();
			employee.setEmployeeId(result.getString(0));
			employee.setEmployeeName(result.getString(1));
			employee.setEmployeeSurname(result.getString(2));
			employee.setEmployeeEmail(result.getString(3));
			employee.setEmployeePassword(result.getString(4));
			employee.setEmployeeImageBytes(result.getBlob(5));
			employee.setEmployeeAddress(result.getString(6));
			employee.setEmployeeDesignation(result.getString(7));
			employee.setEmployeeCoordinates(result.getString(8));


			//populate the list
			employeeList.add(employee);
		}
		return employeeList;
	}
}
