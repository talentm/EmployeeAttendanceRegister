package talss.lib;

import android.util.Log;

/**
 * Created by Talent on 10/6/2016.
 */

public class DateTime
{
	private static final String TAG = DateTime.class.getSimpleName();

	public static String getStringTimeDifference(String checkIn, String checkOut)
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

	public static int getIntTimeDifference(String checkIn, String checkOut)
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

		return (diffHours*60) + diffMins;
	}

	public static String getHoursAndMinutesFromMinutes(int minutes)
	{
		String totalTime = "";
		int resultHrs = minutes/60;
		int resultMins = minutes%60;
		totalTime = resultHrs+"hrs "+resultMins+"mins";

		return totalTime;
	}
}
