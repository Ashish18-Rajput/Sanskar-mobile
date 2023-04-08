package com.sanskar.tv.Others.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Environment;

import java.io.File;

public class ReferenceWrapper {
	private Context context;
	private static ReferenceWrapper wrapper;
	private Typeface regularFont,lightFont, semiBoldFont;
	public Bitmap recentBitmap;

	private ReferenceWrapper(Context context) {
		this.context = context;
	}

	public static ReferenceWrapper getInstance(Context activity) {
		if (wrapper == null) {
			wrapper = new ReferenceWrapper(activity);
		}
		return wrapper;
	}

	public File getBaseUrl() {
		String state = Environment.getExternalStorageState();
		File directory = null;
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File extStorageAppBasePath = new File(Environment.getExternalStorageDirectory() +
			          File.separator + "Android" + File.separator + "data" +
			          File.separator + context.getPackageName());
			 directory=new File(extStorageAppBasePath+"/txData");
			if(!directory.exists()){
				directory.mkdir();
			}
				//mFileTemp = File.createTempFile(imageFileName, ".jpg", directory);
			//boolean created =mFileTemp.createNewFile();
			
		} else {
				File extStorageAppBasePath = new File(context.getFilesDir() +
				          File.separator + "Android" + File.separator + "data" +
				          File.separator + context.getPackageName());
				 directory=new File(extStorageAppBasePath+"/txData");
				if(!directory.exists()){
					directory.mkdir();
				}
				//mFileTemp=new File(directory, imageFileName);
				//boolean created =mFileTemp.createNewFile();
			
		}
		return directory;
	}

	public Typeface getLightFont() {
		if(lightFont==null){
			lightFont= Typeface.createFromAsset(context.getAssets(),"light.ttf");
		}
		return lightFont;
	}

	public Typeface getSemiBoldFont() {
		if(semiBoldFont==null){
		semiBoldFont= Typeface.createFromAsset(context.getAssets(),"semibold.ttf");
		}
		return semiBoldFont;
	}

	public Typeface getRegularFont() {
		if(regularFont==null){
			regularFont= Typeface.createFromAsset(context.getAssets(),"regular.ttf");
		}
		return regularFont;
	}
}
	


