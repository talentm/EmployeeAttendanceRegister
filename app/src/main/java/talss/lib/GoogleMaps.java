package talss.lib;

import android.content.Context;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.util.List;

/**
 * Created by Talent on 8/14/2016.
 */
public class GoogleMaps
{
	private static final String TAG = GoogleMaps.class.getSimpleName();

	public static PointF getCoordinatesFromStreetAddress(String address, Context context)
	{
		Geocoder geocoder = new Geocoder(context);
		List<Address> addresses = null;
		try
		{
			addresses = geocoder.getFromLocationName(address, 1);
			Log.v(TAG, "getCoordinatesFromStreetAddress "+ address + " "+ addresses.size());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		PointF point = new PointF();
		if(addresses.size() > 0)
		{
			float latitude= (float) addresses.get(0).getLatitude();
			float longitude= (float) addresses.get(0).getLongitude();
			point.set(latitude, longitude);
		}
		return point;
	}

	public static int getDistanceBetweenTwoLocations(double centerLat, double centerLong, double testLat, double testLong)
	{
		Location center = new Location("");
		center.setLatitude(centerLat);
		center.setLongitude(centerLong);

		Location test = new Location("");
		test.setLatitude(testLat);
		test.setLongitude(testLong);

		double distanceInMeters = center.distanceTo(test);
		return (int) distanceInMeters;
	}

	public static boolean isWithin(double centerLat, double centerLong, double testLat, double testLong, int radius)
	{
		int distanceBetweenTwoLocations = getDistanceBetweenTwoLocations(centerLat, centerLong, testLat, testLong);
		return distanceBetweenTwoLocations <= radius;
	}
}
