package model;

import java.io.Serializable;

/**
 * Created by Talent on 7/22/2016.
 */
public class Admin implements Serializable
{
	public static final String TAG = Admin.class.getSimpleName();
	public static final String TABLE = "admin";

	//Table column names
	public static final String ID = "admin_id";
	public static final String NAME = "admin_name";
	public static final String SURNAME = "admin_surname";
	public static final String EMAIL = "admin_email";
	public static final String PASSWORD = "admin_password";
	public static final String PHOTO = "admin_photo";
	public static final String COMPANY = "admin_company";


	private String adminId;
	private String adminName;
	private String adminSurname;
	private String adminEmail;
	private String adminPassword;
	private byte[] adminImageBytes;

	public String getAdminCompany()
	{
		return adminCompany;
	}

	public void setAdminCompany(String adminCompany)
	{
		this.adminCompany = adminCompany;
	}

	private String adminCompany;

	public String getAdminId()
	{
		return adminId;
	}

	public void setAdminId(String adminId)
	{
		this.adminId = adminId;
	}

	public String getAdminName()
	{
		return adminName;
	}

	public void setAdminName(String adminName)
	{
		this.adminName = adminName;
	}

	public String getAdminSurname()
	{
		return adminSurname;
	}

	public void setAdminSurname(String adminSurname)
	{
		this.adminSurname = adminSurname;
	}

	public String getAdminEmail()
	{
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail)
	{
		this.adminEmail = adminEmail;
	}

	public String getAdminPassword()
	{
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword)
	{
		this.adminPassword = adminPassword;
	}

	public byte[] getAdminImageBytes()
	{
		return adminImageBytes;
	}

	public void setAdminImageBytes(byte[] adminImageBytes)
	{
		this.adminImageBytes = adminImageBytes;
	}
}
