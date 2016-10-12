package talss.employeeattendanceregister.attendance;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import model.Employee;
import services.LoginLocalStoreService;
import talss.employeeattendanceregister.R;
import talss.employeeattendanceregister.admin.AdminLogInActivity;
import talss.lib.DialogAlert;
import talss.lib.GoogleMaps;

public class EmployeeAttendanceMapActivity extends FragmentActivity implements
		OnMapReadyCallback,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		GoogleMap.OnMarkerDragListener,
		GoogleMap.OnMapLongClickListener,
		View.OnClickListener,
		LocationListener
{

	private final String TAG = EmployeeAttendanceMapActivity.class.getSimpleName();
	private LoginLocalStoreService localStoreService;
	private LocationRequest mLocationRequest;

	//Google ApiClient
	private GoogleApiClient googleApiClient;
	//Our Map
	private GoogleMap mMap;

	//To store longitude and latitude from map
	private double longitude;
	private double latitude;

	private TextView locationStatus;
	private Button logYourTime;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_attendance_map);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		//Initializing googleapi client
		googleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();

		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create()
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setInterval(10 * 1000)        // 10 seconds, in milliseconds
				.setFastestInterval(1 * 1000); // 1 second, in milliseconds

		locationStatus = (TextView) findViewById(R.id.location_status);
		logYourTime = (Button) findViewById(R.id.employee_attendance_log_hours);
        logYourTime.setOnClickListener(this);
		localStoreService = new LoginLocalStoreService(this);
	}

	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap)
	{
		//Initializing our map
		mMap = googleMap;
		//Creating a random coordinate
		LatLng latLng = new LatLng(-34, 151);
		//Adding marker to that coordinate
		mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		//Setting onMarkerDragListener to track the marker drag
		mMap.setOnMarkerDragListener(this);
		//Adding a long click listener to the map
		mMap.setOnMapLongClickListener(this);
	}
	@Override
	public void onConnected(Bundle bundle)
	{
		Log.d("", "OnConnected");
		getCurrentLocation();
		setWorkLocationStatus();
	}

	private Employee getEmployee()
	{
		Bundle bundle = getIntent().getExtras();
		Employee employee = (Employee) bundle.get("employee");
		return employee;
	}

	private void setWorkLocationStatus()
	{
		Log.v(TAG, "setWorkLocationStatus");
		Employee employee = getEmployee();
		if (employee != null)
		{
			String employeeWorkCoordinates = employee.getEmployeeCoordinates();
			Log.v(TAG, "=============================> "+employee.getEmployeeName()+"  "+ employee.getEmployeeAddress()+"  "+ employee.getEmployeeCoordinates());
			if (employeeWorkCoordinates != null && !employeeWorkCoordinates.isEmpty())
			{
				String[] split = employeeWorkCoordinates.split(",");
				double workLocationLat = Double.parseDouble(split[0]);
				double workLocationLong = Double.parseDouble(split[1]);
//				int m = GoogleMaps.getDistanceBetweenTwoLocations(workLocationLat, workLocationLong, latitude, longitude);
//				System.out.println("Distance in metres is =========================================>"+m+"   "+workLocationLat +","+ workLocationLong+"  "+ latitude+","+longitude);

				boolean within = GoogleMaps.isWithin(workLocationLat, workLocationLong, latitude, longitude, 600);
				within = true;
				if (within)
				{
					locationStatus.setText("You are within the work location range. Proceed to log your time.");
					logYourTime.setEnabled(true);
				} else
				{
					locationStatus.setText("You are not within the work location range. Wait until you reach your work location to log your time.");
				    logYourTime.setEnabled(false);
				}
			}
			else
			{
				PointF coordinatesFromStreetAddress = GoogleMaps.getCoordinatesFromStreetAddress(employee.getEmployeeAddress(), this);
				if(coordinatesFromStreetAddress!=null)
				{
					boolean within = GoogleMaps.isWithin(coordinatesFromStreetAddress.x, coordinatesFromStreetAddress.y, latitude, longitude, 600);
					within = true;
					if (within)
					{
						locationStatus.setText("You are within your work location range. Proceed to log your time.");
						logYourTime.setEnabled(true);
					} else
					{
						locationStatus.setText("You are not within your work location range. Wait until you reach your location range to log your time.");
						logYourTime.setEnabled(false);
					}
				}
				else
				{
					locationStatus.setText("Notify your admin to set your work address ");
					logYourTime.setEnabled(false);
				}
			}
		}
	}

	@Override
	public void onConnectionSuspended(int i)
	{

	}

	@Override
	public void onClick(View view)
	{
		if(view == logYourTime)
		{
			Intent intent = new Intent(this, EmployeeAttendanceActivity.class);
			Employee employee = getEmployee();
			Log.v(TAG, "Employee is : "+ employee.getEmployeeId()+" "+employee.getEmployeeName());
			if(employee != null)
			{
				intent.putExtra("employee", employee);
				startActivity(intent);
			}
		}
	}


	@Override
	public void onConnectionFailed(ConnectionResult connectionResult)
	{

	}

	@Override
	public void onMapLongClick(LatLng latLng)
	{
		//Clearing all the markers
		mMap.clear();

		//Adding a new marker to the current pressed position we are also making the draggable true
		mMap.addMarker(new MarkerOptions()
				.position(latLng)
				.draggable(true));
	}

	@Override
	public void onMarkerDragStart(Marker marker)
	{

	}

	@Override
	public void onMarkerDrag(Marker marker)
	{

	}

	@Override
	public void onMarkerDragEnd(Marker marker)
	{
		//Getting the coordinates
		latitude = marker.getPosition().latitude;
		longitude = marker.getPosition().longitude;

		//Moving the map
		moveMap();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		//googleApiClient.connect();
		if (googleApiClient != null)
		{
			googleApiClient.connect();
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		googleApiClient.disconnect();
	}

	//Getting current location
	private void getCurrentLocation()
	{
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		{
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
		if (location == null) {
			LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);

		}
		else
		{
			//If everything went fine lets get latitude and longitude
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			moveMap();
			//Toast.makeText(this, latitude + " WORKS " + location + "", Toast.LENGTH_LONG).show();
		}
	}

	//Function to move the map
	private void moveMap()
	{
		Log.d("","moveMap");
		//String to display current latitude and longitude
		String msg = latitude + ", "+longitude;

		//Creating a LatLng Object to store Coordinates
		LatLng latLng = new LatLng(latitude, longitude);

		//Adding marker to map
		mMap.addMarker(new MarkerOptions()
				.position(latLng) //setting position
				.draggable(true) //Making the marker draggable
				.title("Current Location")); //Adding a title

		//Moving the camera
		mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

		//Animating the camera
		mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

		//Displaying current coordinates in toast
//		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressed()
	{
		Log.v(TAG, "onBackPressed");
		loggingOut();
	}

	private void loggingOut()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Logging out");
		alertDialogBuilder
				.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id)
					{
						localStoreService.clearUserData();
						logOut();
					}
				})
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void logOut()
	{
		Intent intent = new Intent(this, AdminLogInActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}


	@Override
	public void onLocationChanged(Location location)
	{
		setWorkLocationStatus();
	}
}
