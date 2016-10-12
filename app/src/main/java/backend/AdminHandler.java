package backend;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;

import helpers.DatabaseManager;
import model.Admin;

/**
 * Created by Talent on 7/22/2016.
 */
public class AdminHandler
{
	private static final String TAG = AdminHandler.class.getSimpleName();

	public AdminHandler()
	{
	}

	public static String createAdminTable()
	{
		String sql = "CREATE TABLE " + Admin.TABLE + "( "
					+ Admin.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ Admin.NAME + " TEXT, "
					+ Admin.SURNAME + " TEXT, "
					+ Admin.EMAIL +" TEXT, "
					+ Admin.PASSWORD + " TEXT, "
					+ Admin.PHOTO+" TEXT, "
				    + Admin.COMPANY+" TEXT )";

		return sql;
	}
	public static int addAdmin(Admin admin)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		ContentValues values = new ContentValues();
		values.put(Admin.NAME, admin.getAdminName());
		values.put(Admin.SURNAME, admin.getAdminSurname());
		values.put(Admin.EMAIL, admin.getAdminEmail());
		values.put(Admin.PASSWORD, admin.getAdminPassword());
		values.put(Admin.PHOTO, admin.getAdminImageBytes());
		values.put(Admin.COMPANY, admin.getAdminCompany());

		int insert = (int) db.insert(Admin.TABLE, null, values);
		DatabaseManager.getInstance().closeDatabase();
		return insert;
	}

	private Bitmap getImage()
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		Cursor c = db.rawQuery("", null);
		Bitmap map = null;
		if(c.moveToNext())
		{
			byte[] image = c.getBlob(0);
			 map = BitmapFactory.decodeByteArray(image, 0, image.length);
		}
		return map;
	}

	public static int editAdmin(Admin admin)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		ContentValues values = new ContentValues();
		values.put(Admin.NAME, admin.getAdminName());
		values.put(Admin.SURNAME, admin.getAdminSurname());
		values.put(Admin.EMAIL, admin.getAdminEmail());
		values.put(Admin.PASSWORD, admin.getAdminPassword());
		values.put(Admin.PHOTO, admin.getAdminImageBytes());
		values.put(Admin.COMPANY, admin.getAdminCompany());

		int update = db.update(Admin.TABLE, values, "ID = ?", new String[]{admin.getAdminId()});
		DatabaseManager.getInstance().closeDatabase();
		return update;
	}

	public static int deleteAdmin(Admin admin)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		int delete = db.delete(Admin.TABLE, "ID = ?", new String[]{admin.getAdminId()});
		DatabaseManager.getInstance().closeDatabase();
		return delete;
	}

	public static Admin getAdmin(String adminId)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		Admin admin = new Admin();

		String sql = "SELECT * " +
					 "FROM " + Admin.TABLE +" " +
					 "WHERE " + Admin.ID + "=" + adminId;
		Log.d(TAG, sql);
		Cursor result = db.rawQuery(sql, null);
		DatabaseManager.getInstance().closeDatabase();

		if(result!=null)
		{
			admin.setAdminId(result.getString(0));
			admin.setAdminName(result.getString(1));
			admin.setAdminSurname(result.getString(2));
			admin.setAdminEmail(result.getString(3));
			admin.setAdminPassword(result.getString(4));
			admin.setAdminImageBytes(result.getBlob(5));
			admin.setAdminCompany(result.getString(6));
		}
		return admin;
	}

	public static Admin getAdmin(Admin admin)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		String sql = "SELECT * " +
				"FROM " + Admin.TABLE +" " +
				"WHERE " + Admin.EMAIL+ " = '" + admin.getAdminEmail() + "'"+
				" AND " + Admin.PASSWORD + " = '" + admin.getAdminPassword() + "'";

		Log.d(TAG, sql);
		Cursor result = db.rawQuery(sql, null);
		DatabaseManager.getInstance().closeDatabase();

		//Just getting one admin. need a fix
		Admin resultAdmin = null;
		while (result.moveToNext())
		{
			resultAdmin = new Admin();
			resultAdmin.setAdminId(result.getString(0));
			resultAdmin.setAdminName(result.getString(1));
			resultAdmin.setAdminSurname(result.getString(2));
			resultAdmin.setAdminEmail(result.getString(3));
			resultAdmin.setAdminPassword(result.getString(4));
			resultAdmin.setAdminImageBytes(result.getBlob(5));
			resultAdmin.setAdminCompany(result.getString(6));
		}
		return resultAdmin;
	}


	public static boolean AdminExists(Admin admin)
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

		String sql = "SELECT * " +
					"FROM " + Admin.TABLE +" " +
					"WHERE " + Admin.EMAIL+ " = '" + admin.getAdminEmail() + "'"+
				    " AND " + Admin.PASSWORD + " = '" + admin.getAdminPassword() + "'";

		Log.d(TAG, sql);
		Cursor result = db.rawQuery(sql, null);
		DatabaseManager.getInstance().closeDatabase();
		if(result.getCount()>0)
		{
			return true;
		}
		return false;
	}

	public static ArrayList<Admin> getAdminList()
	{
		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		ArrayList<Admin> adminList = new ArrayList<>();
		String sql = "SELECT * " +
				     "FROM " + Admin.TABLE;
		Log.d(TAG, sql);
		Cursor result =  db.rawQuery(sql, null);
		DatabaseManager.getInstance().closeDatabase();

		while (result.moveToNext())
		{
			Admin admin = new Admin();
			admin.setAdminId(result.getString(0));
			admin.setAdminName(result.getString(1));
			admin.setAdminSurname(result.getString(2));
			admin.setAdminEmail(result.getString(3));
			admin.setAdminPassword(result.getString(4));
			admin.setAdminImageBytes(result.getBlob(5));
			admin.setAdminCompany(result.getString(6));

			//populate the list
			adminList.add(admin);
		}
		return adminList;
	}
}
