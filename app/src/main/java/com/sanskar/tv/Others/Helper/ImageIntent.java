package com.sanskar.tv.Others.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.sanskar.tv.Others.Helper.Constants.CAMERA_REQUEST;
import static com.sanskar.tv.Others.Helper.Constants.PICK_IMAGE_REQUEST;

public class ImageIntent {

    OnCameraClickedListener listener;

    public ImageIntent(OnCameraClickedListener listener) {
        this.listener = listener;
    }

    public static String getRealPathFromUri(Context activity, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(column_index);
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public void showImageChooser(final Activity activity) {

//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        activity.startActivityForResult(intent, Constants.PICK_GALLERY);
        final CharSequence[] options = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo from!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {


                    Intent pictureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE
                    );
                    if(pictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                        activity.startActivityForResult(pictureIntent,
                                Constants.PICK_CAMERA);
                    }


//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File file = createImageFile(activity);
//                    if (file != null) {
//                        try {
//                            cameraIntent.putExtra("return-data", false);
//                            cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                            cameraIntent.putExtra("path", currentPath);
//                            if(listener!=null){
//                                listener.onCameraImage(currentPath);
//
//                            }
//                            activity.startActivityForResult(cameraIntent, Constants.PICK_CAMERA);
//                        } catch (Exception e) {
//                            Log.e("ImageIntent",e.toString());
//                        }
//                    }
                } else if (options[item].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activity.startActivityForResult(intent, Constants.PICK_GALLERY);
                }
            }
        });
        builder.show();
    }

    public void showDialog(final Activity context) {
        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.app.AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new android.app.AlertDialog.Builder(context);
        }
        builder.setTitle("Upload Image")
                .setIcon(android.R.drawable.ic_menu_camera)
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        context.startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        context.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    }
                });
        builder.show();
    }

    public static File createImageFile(Activity activity) {
        String state = Environment.getExternalStorageState();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File mFileTemp = null;
        File directory;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            try {
                directory=new File(Environment.getExternalStorageDirectory()+"/ldir");
                if(!directory.exists()){
                    directory.mkdirs();
                }
                mFileTemp = File.createTempFile(imageFileName, ".jpg", Environment.getExternalStorageDirectory());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                directory=new File(activity.getFilesDir()+ File.separator+"ldir");
                if(!directory.exists()){
                    directory.mkdirs();
                }
                mFileTemp = File.createTempFile(imageFileName, ".jpg",activity.getFilesDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mFileTemp;
    }

    public String saveImageToSdcard(Bitmap currentImage, Activity activity) {
        String path = "";
        File file = createImageFile(activity);
        FileOutputStream fout;
        try {
            fout = new FileOutputStream(file);
            currentImage.compress(Bitmap.CompressFormat.PNG, 70, fout);
            fout.flush();
            path = file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            path = null;
        }
        return path;
    }

    public interface OnCameraClickedListener {
        void onCameraImage(String path);
    }
}
