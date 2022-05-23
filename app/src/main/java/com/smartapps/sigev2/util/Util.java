package com.smartapps.sigev2.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.smartapps.sigev2.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Util {
    private static final String TAG = "UTIL";

    public static File getOutputMediaFile(String directorio) {
        File mediaFile = null;
        boolean band = true;
        String strSDCardPath = System.getenv("SECONDARY_STORAGE");
        if ((strSDCardPath == null) || (strSDCardPath.length() == 0)) {
            strSDCardPath = System.getenv("EXTERNAL_SDCARD_STORAGE");
            if (strSDCardPath == null || (strSDCardPath.length() == 0)) {
                strSDCardPath = System.getenv("EXTERNAL_STORAGE");
            }
        }
        if (strSDCardPath != null) {
            if (strSDCardPath.contains(":")) {
                strSDCardPath = strSDCardPath.substring(0, strSDCardPath.indexOf(":"));
            }
            File externalFilePath = new File(strSDCardPath);
            if (externalFilePath.exists() && externalFilePath.canWrite()) {
                try {
                    File mediaStorageDir = new File(strSDCardPath, directorio);
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
//                            return null;

                            band = false;
                        }

                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String timeStamp = simpleDateFormat.format(new Date());
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + UUID.randomUUID().toString() + "_" + timeStamp + ".jpg");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), directorio);
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        return null;
                    }
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String timeStamp = simpleDateFormat.format(new Date());
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + UUID.randomUUID().toString() + "_" + timeStamp + ".jpg");
            }
        } else {
            mediaFile = getOutputFileLocal(directorio);
        }

        if (!band) {
            mediaFile = getOutputFileLocal(directorio);
        }

        return mediaFile;
    }

    private static File getOutputFileLocal(String directorio) {
        File mediaFile = null;
        try {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), directorio);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }
            mediaFile = new File(mediaStorageDir.getPath() + File.separator);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return mediaFile;
    }

    public static File getOutputMediaFile2(String directorio) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), directorio);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("getOutputMediaFile2", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return new File(mediaStorageDir.getPath() + File.separator +
                UUID.randomUUID().toString() + "_" + timeStamp + ".jpg");
    }

    public static int getIndexCompressImagen(String pathFotoOriginal) {
        File file = new File(pathFotoOriginal);
        long tamanio_in_bytes = file.length();
        long tamanio_in_kbytes = tamanio_in_bytes / 1024;
        long tamanio_in_mbytes = tamanio_in_kbytes / 1024;
        int megas = Integer.parseInt(Long.toString(tamanio_in_mbytes).split("\\.")[0]);
        int quality = 100;
        if (megas >= 8) {
            quality = 35;
        } else {
            quality = -8 * megas + 100;
        }
        return quality;
    }


    public static Boolean borrarArchivo(File file) {
        boolean result;
        try {
            result = file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public static Boolean borrarArchivo(String path) {
        boolean result;
        try {
            File file = new File(path);
            result = file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public static void crearSmallFoto(String path) {
        Bitmap b = crearSmallFoto(path, 360);

        File f = new File(path.replace(".jpg", "_small.jpg").replace(".png", "_small.jpg"));
        if (f.exists()) {
            Util.borrarArchivo(f);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
        }
        b.compress(Bitmap.CompressFormat.PNG, 100, fos);

        try {
            fos.flush();
            fos.close();
            fos = null;
            f = null;
            b.recycle();
        } catch (IOException e) {
        }
    }

    public static Bitmap crearSmallFoto(String path, int maxSize) {
        //Redimensionamos
        Bitmap mBitmap = BitmapFactory.decodeFile(path);
        if (mBitmap == null) {
            return null;
        } else {
            try {
                int outWidth;
                int outHeight;
                int inWidth = mBitmap.getWidth();
                int inHeight = mBitmap.getHeight();
                if (inWidth > inHeight) {
                    outWidth = maxSize;
                    outHeight = (inHeight * maxSize) / inWidth;
                } else {
                    outHeight = maxSize;
                    outWidth = (inWidth * maxSize) / inHeight;
                }

                ExifInterface exif = new ExifInterface(path);
                int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int rotationInDegrees = exifToDegrees(rotation);

                float scaleWidth = ((float) outWidth) / mBitmap.getWidth();
                float scaleHeight = ((float) outHeight) / mBitmap.getHeight();
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                if (rotation != 0f) {
                    matrix.preRotate(rotationInDegrees);
                }

                Bitmap resizedBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);

                //return Bitmap.createScaledBitmap(mBitmap, outWidth, outHeight, false);
                return resizedBitmap;
            } catch (Exception e) {
//                Log.e(TAG, "Error al redimensionar imágen. Detalle: " + e.getMessage());
                return null;
            }
        }
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static int getAnchoPantalla(Display d) {
        int Screenwidth = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Point size = new Point();
            d.getSize(size);
            Screenwidth = size.x;
        } else {
            Screenwidth = d.getWidth();
        }
        return Screenwidth;
    }


    public static void getBitmap(String path, Context context) {
        getBitmap(path, path, context);
    }


    public static void getBitmap(String pathOrigen, String pathDestino, Context context) {

        Uri uri = Uri.fromFile(new File(pathOrigen));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1000000; // 1.0MP
            in = context.getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();


            int scale = 1;
            while ((options.outWidth * options.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d(TAG, "scale = " + scale + ", orig-width: " + options.outWidth + ",orig-height: " + options.outHeight);

            Bitmap resultBitmap = null;
            in = context.getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                options = new BitmapFactory.Options();
                options.inSampleSize = scale;
                resultBitmap = BitmapFactory.decodeStream(in, null, options);

                // resize to desired dimensions
                int height = resultBitmap.getHeight();
                int width = resultBitmap.getWidth();
                Log.d(TAG, "1th scale operation dimenions - width: " + width + ",height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                ExifInterface exif = new ExifInterface(pathOrigen);
                int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int rotationInDegrees = exifToDegrees(rotation);

                Bitmap scaledBitmap = createScaledBitmap(resultBitmap, (int) x, (int) y, true, rotation, rotationInDegrees);
                resultBitmap.recycle();
                resultBitmap = scaledBitmap;

                in.close();
                in = null;

                // save image
                try {
                    FileOutputStream out = new FileOutputStream(pathDestino);
                    resultBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    out.flush();
                    out.close();
                    out = null;
                } catch (Exception e) {
                    Log.e("Image", e.getMessage(), e);
                }
            } else {
                Util.copiarFicheros(new File(pathOrigen), new File(pathDestino));
            }
            options = null;
            System.gc();
            //return resultBitmap;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private static Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight,
                                             boolean filter, int rotation, int rotationInDegrees) {
        Matrix m = new Matrix();
        final int width = src.getWidth();
        final int height = src.getHeight();
        final float sx = dstWidth / (float) width;
        final float sy = dstHeight / (float) height;
        m.setScale(sx, sy);
        if (rotation != 0f) {
            m.preRotate(rotationInDegrees);
        }
        Bitmap b = Bitmap.createBitmap(src, 0, 0, width, height, m, filter);
        return b;
    }

    private static void copiarFicheros(File f1, File f2) {
        try {
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();
            System.out.println("Copiando fichero " + f1.toString());

        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) App.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static Integer calcularEdad(Calendar fechaActual, Calendar fechaNacimiento) {
        //Obtenemos el año, el mes y el dia actual
        Integer annioActual = (fechaActual.get(Calendar.YEAR));
        Integer mesActual = (fechaActual.get(Calendar.MONTH));
        Integer diaActual = (fechaActual.get(Calendar.DATE));
        //Obtenemos el año, el mes y el dia de nacimiento
        Integer annioNac = fechaNacimiento.get(Calendar.YEAR);
        Integer mesNac = fechaNacimiento.get(Calendar.MONTH);
        Integer diaNac = fechaNacimiento.get(Calendar.DATE);
        //Obtenemos la diferencia entre ambas fechas
        Integer diff_annio = annioActual - annioNac;
        Integer diff_mes = mesActual - mesNac;
        Integer diff_dia = diaActual - diaNac;
        if (diff_mes < 0 || (diff_mes == 0 && diff_dia < 0)) {
            diff_annio = diff_annio - 1;
        }
        return diff_annio;
    }

    public static String generateNoteOnSD(String folder, String fileName, String body) {
        String path = "";
        try {
            File root = new File(Environment.getExternalStorageDirectory(), folder);
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, fileName);
            FileWriter writer = new FileWriter(file);
            writer.append(body);
            writer.flush();
            writer.close();
            path = file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
