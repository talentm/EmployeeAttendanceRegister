package talss.lib;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Talent on 8/13/2016.
 */
public class HttpGet
{
	private static final String TAG = HttpGet.class.getSimpleName();

	public static String getDownloadString(URL url) throws Exception
	{
		Log.d(TAG, "getDownloadString");
		URLConnection connection = url.openConnection();
		InputStream inputStream = connection.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] byteArray = new byte[1024];
		int length;
		while ((length = inputStream.read(byteArray)) > -1)
		{
			baos.write(byteArray, 0, length);
		}

		byte[] output = baos.toByteArray();
		String downloadString = new String(output);
		return downloadString;
	}
}
