package talss.employeeattendanceregister.admin;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import model.Admin;
import talss.employeeattendanceregister.R;

public class AdminUploadPhotoActivity extends Activity implements View.OnClickListener
{
	private static final int REQUEST_CAMERA = 0;
	private static final int SELECT_FILE = 1;

	private ImageView adminProfileImageView;
	private Button adminNextButton;
	private TextView adminUploadTextView;

	private Admin admin;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_upload_photo);

		setUIComponents();
		admin = new Admin();
		byte[] defaultImageBytes = getDefaultImageBytes();
		admin.setAdminImageBytes(defaultImageBytes);
	}

	private void setUIComponents()
	{
		adminProfileImageView = (ImageView) findViewById(R.id.admin_photo_upload_image_view);
		adminUploadTextView = (TextView) findViewById(R.id.admin_photo_upload_text);
		adminNextButton = (Button) findViewById(R.id.admin_photo_upload_next_button);
		adminNextButton.setOnClickListener(this);
		adminUploadTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if(v == adminNextButton)
		{
			Intent intent = new Intent(this, AdminSignUpActivity.class);
			intent.putExtra("admin", admin);
			startActivity(intent);
		}
		else if(v == adminUploadTextView)
		{
			selectImage();
		}
	}


	private byte[] getDefaultImageBytes()
	{
		Bitmap bitmap = ((BitmapDrawable)adminProfileImageView.getDrawable()).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] imageInBytes = stream.toByteArray();
		return imageInBytes;
	}

	private void selectImage()
	{
		final CharSequence[] items = { "Take Photo", "Choose from library", "Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add profile photo");
		builder.setItems(items, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int item)
			{
				if (items[item].equals("Take Photo"))
				{
					callCamera();
				}
				else if (items[item].equals("Choose from library"))
				{
					callGallery();
				}
				else if (items[item].equals("Cancel"))
				{
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	public void callCamera()
	{
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra("crop", "true");
		cameraIntent.putExtra("aspectX", 0);
		cameraIntent.putExtra("aspectY", 0);
		cameraIntent.putExtra("outputX", 200);
		cameraIntent.putExtra("outputY", 150);
		startActivityForResult(cameraIntent, REQUEST_CAMERA);

	}

	public void callGallery()
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 0);
		intent.putExtra("aspectY", 0);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(Intent.createChooser(intent, "Complete action using"), SELECT_FILE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode)
		{
			case REQUEST_CAMERA:
				Bundle cameraExtras = data.getExtras();
				if (cameraExtras != null) {
					Bitmap bitmapImage = cameraExtras.getParcelable("data");
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] imageInByte = stream.toByteArray();
					adminProfileImageView.setImageBitmap(bitmapImage);
					admin.setAdminImageBytes(imageInByte);
				}
				break;
			case SELECT_FILE:

				try
				{
					Uri uri = data.getData();
					Bitmap bitmapImage;
					if(uri!=null)
					{
						bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
					}
					else
					{
						bitmapImage = (Bitmap) data.getExtras().get("data");
					}
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] imageInByte = stream.toByteArray();
					adminProfileImageView.setImageBitmap(bitmapImage);
					admin.setAdminImageBytes(imageInByte);

				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				break;
		}
	}
}
