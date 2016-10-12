package talss.lib;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Talent on 10/6/2016.
 */

public class DataValidation
{
	public static String getData()
	{
		return null;
	}

	public static String getValidation(HashMap<String, String> data)
	{
		Set<String> keys = data.keySet();
		String error = "";
		for (String key : keys)
		{
			String value = data.get(key);
			if(key.equals("password") && value.length() < 4)
			{
				error += toUpperCaseFirstLetter(key) + " must have at least 4 characters \n";
			}
			else if(key.equals("email") && !validateEmail(value))
			{
				error += toUpperCaseFirstLetter(key) + " is invalid \n";
			}
			else if(value.isEmpty())
			{
				error += toUpperCaseFirstLetter(key) + " is required \n";
			}
		}
		return error;
	}

	public static boolean isValid(HashMap<String, String> data)
	{
		String validation = getValidation(data);
		return validation.isEmpty();
	}

	private static String toUpperCaseFirstLetter(String string)
	{
		if(string.length()>1) return string.substring(0, 1).toUpperCase() + string.substring(1);
		return "";
	}


	private static boolean validateEmail(String email)
	{
		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

}
