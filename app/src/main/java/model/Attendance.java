package model;

/**
 * Created by Talent on 7/22/2016.
 */
public class Attendance
{
	public static final String TAG = Attendance.class.getSimpleName();
	public static final String TABLE = "attendance";

	//Table columns
	public static final String ID = "attendance_id";
	public static final String CHECK_IN= "attendance_check_in";
	public static final String CHECK_OUT = "attendance_check_out";
	public static final String DATE = "attendance_date";

	private String attendanceId;
	private String attendanceCheckIn;
	private String attendanceCheckOut;

	public String getAttendanceDate()
	{
		return attendanceDate;
	}

	public void setAttendanceDate(String attendanceDate)
	{
		this.attendanceDate = attendanceDate;
	}

	private String attendanceDate;

	public String getAttendanceId()
	{
		return attendanceId;
	}

	public void setAttendanceId(String attendanceId)
	{
		this.attendanceId = attendanceId;
	}

	public String getAttendanceCheckIn()
	{
		return attendanceCheckIn;
	}

	public void setAttendanceCheckIn(String attendanceCheckIn)
	{
		this.attendanceCheckIn = attendanceCheckIn;
	}

	public String getAttendanceCheckOut()
	{
		return attendanceCheckOut;
	}

	public void setAttendanceCheckOut(String attendanceCheckOut)
	{
		this.attendanceCheckOut = attendanceCheckOut;
	}
}
