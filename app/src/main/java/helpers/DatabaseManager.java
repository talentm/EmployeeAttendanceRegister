package helpers;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Talent on 7/23/2016.
 */
public class DatabaseManager
{
	private Integer databaseOpeningCounter = 0;

	private static DatabaseManager instance;
	private static SQLiteOpenHelper sqLiteOpenHelper;
	private SQLiteDatabase sqLiteDatabase;

	public static synchronized void initializeInstance(SQLiteOpenHelper helper)
	{
		if (instance == null)
		{
			instance = new DatabaseManager();
			sqLiteOpenHelper = helper;
		}
	}

	public static synchronized DatabaseManager getInstance() {
		if (instance == null)
		{
			throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
					" is not initialized, call initializeInstance(..) method first.");
		}
		return instance;
	}

	public synchronized SQLiteDatabase openDatabase()
	{
		databaseOpeningCounter +=1;
		if(databaseOpeningCounter == 1)
		{
			// Opening new database
			sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
		}
		return sqLiteDatabase;
	}

	public synchronized void closeDatabase()
	{
		databaseOpeningCounter -=1;
		if(databaseOpeningCounter == 0)
		{
			// Closing database
			sqLiteDatabase.close();
		}
	}
}
