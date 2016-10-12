package talss.employeeattendanceregister;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import helpers.DatabaseHelper;
import helpers.DatabaseManager;

/**
 * Created by Talent on 7/23/2016.
 */
public class StartApp extends Application
{
	private static Context context;
	private static DatabaseHelper dbHelper;

	@Override
	public void onCreate()
	{
		super.onCreate();
		context = this.getApplicationContext();
		dbHelper = new DatabaseHelper(this);

		DatabaseManager.initializeInstance(dbHelper);
		SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
	}

	public static Context getContext()
	{
		return context;
	}
}
