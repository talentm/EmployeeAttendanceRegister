package talss.lib;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Talent on 7/26/2016.
 */
public class DialogAlert
{

	public static void showAlert(Activity activity, String message, String dialogTitle) {

		TextView title = new TextView(activity);
		title.setText(dialogTitle);
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.RED);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		// builder.setTitle("Title");
		builder.setCustomTitle(title);
		// builder.setIcon(R.drawable.alert_36);

		builder.setMessage(message);

		builder.setCancelable(false);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}

		});

		AlertDialog alert = builder.create();
		alert.show();
	}

//	public void showMessage(String title)
//	{
//		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//		alertDialog.setTitle("title");
//		alertDialog.setMessage("Welcome to AndroidHive.info");
//
//		// Setting Icon to Dialog
//		//alertDialog.setIcon(R.drawable.tick);
//
//		alertDialog.setButton("OK", new DialogInterface.OnClickListener()
//		{
//			public void onClick(DialogInterface dialog, int which)
//			{
//				// Write your code here to execute after dialog closed
//			}
//		});
//
//		alertDialog.show();
//	}
}
