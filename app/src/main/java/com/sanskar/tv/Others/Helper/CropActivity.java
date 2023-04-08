package com.sanskar.tv.Others.Helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

import com.sanskar.tv.CustomViews.CropImageView;
import com.sanskar.tv.R;

import java.io.File;
import java.io.FileOutputStream;

public class CropActivity extends Activity {

	private String currentPhotoPath;
	private CropImageView imageToCrop;
	private int targetSize;
	private Bitmap currentImage;
	private RelativeLayout imageProgress;
	//private String resultAs="path";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.crop_layout);
		imageToCrop = findViewById(R.id.cropImageView);
		imageProgress= findViewById(R.id.imageProgress);
		if (getIntent().getExtras() != null) {
			Bundle bundle=getIntent().getExtras();
			/*if(bundle.containsKey("resultAs")) {
				resultAs = bundle.getString("resultAs");
			}*/
			if(bundle.getBoolean("mode",false)){
				imageToCrop.setCropMode(CropImageView.CropMode.RATIO_3_4);
			}
			currentPhotoPath=bundle.getString(Constants.IMAGE_PATH);
		}
		targetSize = getResources().getDisplayMetrics().widthPixels;
		if (currentPhotoPath != null && !currentPhotoPath.equals("")) {
			new DecodeTask().execute();
		} else {
			currentImage = ReferenceWrapper.getInstance(this).recentBitmap;
			if (currentImage != null) {
				imageToCrop.setImageBitmap(currentImage);
			}
		}
		findViewById(R.id.buttonCancleCrop).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		findViewById(R.id.buttonSaveImage).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSavedClick();
			}
		});
		findViewById(R.id.rotate).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageToCrop.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
			}
		});
	}
	
	
	class DecodeTask extends AsyncTask<Void,Void,Void> {
		@Override
		protected Void doInBackground(Void... params) {
				try {
				currentImage = decodeSampledBitmap(targetSize);				
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				Log.e("Setting bitmap", "OutOfMemoryError");
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("Setting bitmap", " Error");
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(currentImage!=null){
				imageToCrop.setImageBitmap(currentImage);
			}
		}
	}

	private Bitmap decodeSampledBitmap(int size) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(currentPhotoPath, options);
		options.inSampleSize = calculateInSampleSize(options, size);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(currentPhotoPath, options);
	}

	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqWidth || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqWidth && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	/*private Bitmap getOrientedBitmap() {
		ExifInterface exifInterface = null;
		try {
			exifInterface = new ExifInterface(currentPhotoPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Matrix matrix = new Matrix();
		int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
		if (orientation == 6) {
			matrix.postRotate(90);
			Log.e("orientation", "" + 6);
			currentImage = Bitmap.createBitmap(currentImage, 0, 0, targetSize, targetSize, matrix, false);
		} else if (orientation == 3) {
			Log.e("orientation", "" + 3);
			matrix.postRotate(180);
			currentImage = Bitmap.createBitmap(currentImage, 0, 0, targetSize, targetSize, matrix, false);
		} else if (orientation == 8) {
			Log.e("orientation", "" + 8);
			matrix.postRotate(-90);
			currentImage = Bitmap.createBitmap(currentImage, 0, 0, targetSize, targetSize, matrix, false);
		}
		return currentImage;
	}
*/
	
	private void onSavedClick() {
		try {
			imageProgress.setVisibility(View.VISIBLE);
			currentImage = imageToCrop.getCroppedBitmap();
			/*if (resultAs.equals("bitmap")) {
				if(currentImage!=null){
			     	ReferenceWrapper.getInstance(this).recentBitmap = currentImage;
				}
				setResult(RESULT_OK);
				finish();
			} else {*/
				findViewById(R.id.buttonSaveImage).setEnabled(false);
				new DoBackgroungJob().execute();
			//}
		} catch (Throwable e) {
			Log.e("Exception","during image crop");
			findViewById(R.id.buttonSaveImage).setEnabled(true);
			e.printStackTrace();
		}
	}
	
	/* public static Uri getUri(Context context, Bitmap bitmap) {
	        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
	        return  path!=null ? Uri.parse(path) : null;
	    }*/
	
	class DoBackgroungJob extends AsyncTask<Void, Bitmap,String> {
		@Override
		protected String doInBackground(Void... params) {
			String path=null;
			if (currentImage != null) {
				File file = ImageIntent.createImageFile(CropActivity.this);
				if(file!=null){
				FileOutputStream fout;
				try {
					fout = new FileOutputStream(file);
					currentImage.compress(Bitmap.CompressFormat.PNG, 70, fout);
					fout.flush();
					//path=Uri.fromFile(file);
					path=file.getAbsolutePath();
				} catch (Exception e) {
					e.printStackTrace();
					path=null;
				}
				if (currentImage != null) {
					currentImage.recycle();
				}
			}
			}
			else{
				return null;
			}
			return path;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			imageProgress.setVisibility(View.GONE);
			findViewById(R.id.buttonSaveImage).setEnabled(true);
			if (result != null) {
				Intent data = new Intent();
				data.putExtra(Constants.IMAGE_PATH, result);
				setResult(RESULT_OK, data);
			} else {
				ToastUtil.showShortToast(CropActivity.this, "Insufficient storage on device");
				setResult(RESULT_CANCELED);
			}
			finish();
		}
	}

	/*
	 * private void onSavedClick() { if(imageToCrop.getDrawable()==null){
	 * setResult(RESULT_CANCELED); finish(); } else{
	 * imageToCrop.setDrawingCacheEnabled(true); Bitmap mainImage =
	 * imageToCrop.getDrawingCache(); try{ int[] cord=getCoordinates();
	 * RelativeLayout v=(RelativeLayout)findViewById(111); currentImage =
	 * Bitmap.createBitmap(mainImage,cord[0],cord[1],v.getMeasuredWidth(),v.
	 * getMeasuredHeight()); } catch(OutOfMemoryError error){
	 * error.printStackTrace(); } File file=createImageFile();
	 * 
	 * FileOutputStream fout; try { fout = new FileOutputStream(file);
	 * currentImage.compress(Bitmap.CompressFormat.PNG, 80,fout); fout.flush();
	 * } catch (Exception e) { e.printStackTrace(); } Intent data = new
	 * Intent(); data.putExtra(Constants.IMAGE_PATH,file.getPath());
	 * setResult(RESULT_OK, data); if(currentImage!=null){
	 * currentImage.recycle(); } finish(); } }
	 */

	/*@Override
	protected void onDestroy() {
		if (currentImage != null) {
			currentImage.recycle();
			currentImage = null;
		}
		super.onDestroy();
	}*/
}
