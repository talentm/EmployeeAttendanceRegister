package talss.lib;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Talent on 10/11/2016.
 */

public class DateFormat
{
	private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final SimpleDateFormat sdfHumanDate = new SimpleDateFormat("EEE d MMM yyyy");
	private static final SimpleDateFormat sdfHumanTime = new SimpleDateFormat("h:mm a");
	private static final SimpleDateFormat sdfHumanDateTime = new SimpleDateFormat("EEE d MMM yyyy, h:mm a");

	private static long timeOffSet = Calendar.getInstance().getTimeZone().getOffset(System.currentTimeMillis());


	public static long parseDate(String time)
	{
		try
		{
			synchronized (sdfDate)
			{
				return sdfDate.parse(time).getTime();
			}
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	public static long parseTime(String time)
	{
		try
		{
			synchronized (sdfTime)
			{
				return sdfTime.parse(time).getTime();
			}
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	public static long parseDateTime(String time)
	{
		try
		{
			synchronized (sdfDateTime)
			{
				return sdfDateTime.parse(time).getTime();
			}
		}
		catch (Exception e)
		{
			return 0;
		}
	}


	public static String formatDate(long time)
	{
		synchronized (sdfDate)
		{
			return sdfDate.format(new Date(time));
		}
	}

	public static String formatTime(long time)
	{
		synchronized (sdfTime)
		{
			return sdfTime.format(new Date(time));
		}
	}

	public static String formatDateTime(long time)
	{

		synchronized (sdfDateTime)
		{
			return sdfDateTime.format(new Date(time));
		}

	}

	public static String formatHumanDate(long time)
	{
		synchronized (sdfHumanDate)
		{
			return sdfHumanDate.format(new Date(time));
		}
	}

	public static String formatHumanTime(long time)
	{
		synchronized (sdfHumanTime)
		{
			return sdfHumanTime.format(new Date(time));
		}
	}

	public static String formatHumanDateTime(long time)
	{

		synchronized (sdfHumanDateTime)
		{
			return sdfHumanDateTime.format(new Date(time));
		}

	}


	public static long getUtcTime()
	{
		long time = System.currentTimeMillis();
		return timeOffSet - time;
	}

	public static String getUctTimeString()
	{
		return DateFormat.formatDateTime(getUtcTime());
	}
}
