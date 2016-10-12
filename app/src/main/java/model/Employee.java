package model;

import java.io.Serializable;

/**
 * Created by Talent on 7/22/2016.
 */
public class Employee implements Serializable
{
	public static final String TAG = Employee.class.getSimpleName();
	public static final String TABLE = "employee";

	//Table columns
	public static final String ID = "employee_id";
	public static final String NAME = "employee_name";
	public static final String SURNAME = "employee_surname";
	public static final String EMAIL = "employee_email";
	public static final String PASSWORD = "employee_password";
	public static final String PHOTO = "employee_photo";
	public static final String ADDRESS = "employee_address";
	public static final String DESIGNATION = "employee_designation";
	public static final String COORDINATES = "employee_coordinates";

	private String employeeId;
	private String employeeName;
	private String employeeSurname;
	private String employeeEmail;
	private String employeePassword;
	private byte[] employeeImageBytes;
	private String employeeAddress;
	private String employeeDesignation;

	public String getEmployeeCoordinates()
	{
		return employeeCoordinates;
	}

	public void setEmployeeCoordinates(String employeeCoordinates)
	{
		this.employeeCoordinates = employeeCoordinates;
	}

	private String employeeCoordinates;

	public byte[] getEmployeeImageBytes()
	{
		return employeeImageBytes;
	}

	public void setEmployeeImageBytes(byte[] employeeImageBytes)
	{
		this.employeeImageBytes = employeeImageBytes;
	}

	public String getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(String employeeId)
	{
		this.employeeId = employeeId;
	}

	public String getEmployeeName()
	{
		return employeeName;
	}

	public void setEmployeeName(String employeeName)
	{
		this.employeeName = employeeName;
	}

	public String getEmployeeSurname()
	{
		return employeeSurname;
	}

	public void setEmployeeSurname(String employeeSurname)
	{
		this.employeeSurname = employeeSurname;
	}

	public String getEmployeeEmail()
	{
		return employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail)
	{
		this.employeeEmail = employeeEmail;
	}

	public String getEmployeePassword()
	{
		return employeePassword;
	}

	public void setEmployeePassword(String employeePassword)
	{
		this.employeePassword = employeePassword;
	}

	public String getEmployeeAddress()
	{
		return employeeAddress;
	}

	public void setEmployeeAddress(String employeeAddress)
	{
		this.employeeAddress = employeeAddress;
	}

	public String getEmployeeDesignation()
	{
		return employeeDesignation;
	}

	public void setEmployeeDesignation(String employeeDesignation)
	{
		this.employeeDesignation = employeeDesignation;
	}
}
