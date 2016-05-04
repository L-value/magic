package com.picture.utils;

/*-
 * Authors      : harry 
 *
 * Created Date : Nov 6, 2012
 *  
 * Mojob Limited Inc.  All rights reserved.
 *
 */


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* Utils for Android Gallery app */

public class GalleryUtils {

    private GalleryUtils() {

    }


    /**
     * Generate a image file name based on date
     */
    public static String generateImageName() {
        return System.currentTimeMillis() + ".jpg";
    }

    /**
     * Get file name from its full path
     *
     * @param fileFullPath Full path of a File
     * @return file name of a File.
     */
    public static final String getFileName(String fileFullPath) {

        if (fileFullPath == null) {
            return "";
        }
        return fileFullPath.substring(fileFullPath.lastIndexOf("/") + 1);
    }

    /**
     * Get file path of an Uri. Note that the file path is linux system file
     * path.
     *
     * @param uri      Resource Uri
     * @return The file path
     */
    public static String getFilePath(Activity activity, Uri uri) {

        String[] proj = {MediaColumns.DATA};

        @SuppressWarnings("deprecation")
        Cursor cursor = activity.managedQuery(uri, /*- Resource Uri */
                proj, /*-Columns to return*/
                null, /*-Where clause*/
                null, /*-Where clause selection*/
                null /* Order by */
        );

        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    /**
     * Get image thumbnail of an image in Gallery app.
     *
     * @param c   Context
     * @param uri Image Uri
     * @return The thumbnail as Bitmap
     */
    public static Bitmap getImageThumbnail(Context c, Uri uri) {

        String uriStr = uri.toString();


        // read from file Uri
        if (uriStr.startsWith("file://")) {
            File uriFile = new File(uri.getPath());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            return BitmapFactory.decodeFile(uriFile.getAbsolutePath(), options);
        }

        // Read from content Uri
        long id = Long.parseLong(uri.getLastPathSegment());

        ContentResolver contentProvider = c.getContentResolver();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;

        Bitmap imageThumbnail = MediaStore.Images.Thumbnails.getThumbnail(contentProvider,
                id,
                MediaStore.Video.Thumbnails.MINI_KIND,

                options);

        return imageThumbnail;

    }

    /**
     * Get video thumbnail of videos from Gallery.
     *
     * @param c   Context
     * @param uri Image Uri
     * @return The thumbnail as Bitmap
     */
    public static Bitmap getVideoThumbnail(Context c, Uri uri) {


        long id = Long.parseLong(uri.getLastPathSegment());

        ContentResolver contentProvider = c.getContentResolver();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;

        Bitmap videoThumbnail = MediaStore.Images.Thumbnails.getThumbnail(contentProvider,
                id,
                MediaStore.Video.Thumbnails.MINI_KIND,
                options);
        return videoThumbnail;

    }

    /**
     * @see http://sylvana.net/jpegcrop/exif_orientation.html
     */
    public static Bitmap rotateBitmap(String src, Bitmap bitmap) {

        try {
            int orientation = getExifOrientation(src);

            if (orientation == 1) {

                return bitmap;
            }

            Matrix matrix = new Matrix();
            switch (orientation) {
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case 6:
                    matrix.setRotate(90);
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case 8:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }

            try {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(),
                        bitmap.getHeight(),
                        matrix, true);
                bitmap.recycle();
                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static int getExifOrientation(String src) throws IOException {

        int orientation = 1;

        try {
            /**
             * if your are targeting only api level >= 5 ExifInterface exif =
             * new ExifInterface(src); orientation =
             * exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class.forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass.getConstructor(String.class);
                Object exifInstance = exifConstructor.newInstance(src);
                Method getAttributeInt = exifClass.getMethod("getAttributeInt",
                        String.class,
                        int.class);
                Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance,
                        tagOrientation,
                        1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return orientation;
    }

    /**
     * http://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework/20559175#20559175
     *
     * @param applicationContext
     * @param uri
     * @return
     */
    public static String getImagePathByUri(Context applicationContext, Uri uri) {

        

        ContentResolver contentResolver = applicationContext.getContentResolver();

        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = contentResolver.query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public static Bitmap getImageByUri(Context applicationContext, Uri resourceUri) {
        String filePath = getImagePathByUri(applicationContext, resourceUri);
        return getSmallBitmap(filePath);
    }


    public static Bitmap getSmallBitmap(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);

        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);

        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;

    }


    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }



}
