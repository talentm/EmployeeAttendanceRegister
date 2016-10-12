package talss.employeeattendanceregister.employee;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import talss.lib.HttpGet;

/**
 * Created by Talent on 8/13/2016.
 */
public class EmployeeAddressAutocompleteAdapter extends ArrayAdapter<String> implements Filterable
{
	private static final String TAG = EmployeeAddressAutocompleteAdapter.class.getSimpleName();
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	private static final String API_KEY = "AIzaSyAQ-8LgIpAUDSKHz92VWIjm3CcW-f5ypWM";

	private ArrayList<String> resultList;
	private String jsonResults;

	public EmployeeAddressAutocompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public String getItem(int index) {
		return resultList.get(index);
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null)
				{
					// Retrieve the getAutoCompletionList results.
					new DownloadTask().execute(constraint.toString());

					// Assign the data to the FilterResults
					if(resultList!=null)
					{
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results != null && results.count > 0)
				{
					notifyDataSetChanged();
				} else
				{
					notifyDataSetInvalidated();
				}
			}
		};
		return filter;
	}


	@NonNull
	private URL getUrl(String input) throws Exception
	{
		StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
		sb.append("?key=" + API_KEY);
		//	sb.append("&components=country:"+getCountryCode());
		sb.append("&input=" + URLEncoder.encode(input, "utf8"));

		return new URL(sb.toString());
	}

	private class DownloadTask extends AsyncTask<Object, Void, Void>
	{
		@Override
		protected Void doInBackground(Object... params)
		{
			Log.d(TAG, "doInBackground");
			String input = (String) params[0];
			getRemoteData(input);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid)
		{
			Log.d(TAG, "onPostExecute");
			super.onPostExecute(aVoid);
			serializeJson();
		}
	}

	private void getRemoteData(String input)
	{
		try
		{
			URL url = getUrl(input);
			String downloadString = HttpGet.getDownloadString(url);
			jsonResults = downloadString;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void serializeJson()
	{
		try
		{
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults);
			JSONArray jsonPredictions = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<>(jsonPredictions.length());
			for (int i = 0; i < jsonPredictions.length(); i++)
			{
				System.out.println(jsonPredictions.getJSONObject(i).getString("description"));
				System.out.println("============================================================");
				resultList.add(jsonPredictions.getJSONObject(i).getString("description"));
			}
		}
		catch (JSONException e) {
			Log.e(TAG, "Cannot process JSON results", e);
		}
	}

}
