package com.smartapps.sigev2.ui.multimedia;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.smartapps.sigev2.BuildConfig;
import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.Config;
import com.smartapps.sigev2.util.Util;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CustomCameraActivity extends Activity {

    public static boolean BANDERA_FOTO = false;
    public static final String DIRECTORIO_FOTOS = "SiGE" + File.separator + "images";

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE_2 = 102;

    private static final String TAG = "CustomCameraActivity";

    private Uri fileUri; // file url to store image/video
    private Uri file;
    private File filePhoto;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multimedia_camera_blank);

        Intent in = getIntent();
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        if (!isDeviceSupportCamera()) {
                            Toast.makeText(getApplicationContext(),
                                    "Ups! Tu dispostitivo no tiene soporte de cámara.",
                                    Toast.LENGTH_LONG).show();
                            // will close the app if the device does't have camera
                            finish();
                        } else {
                            if (Config.BANDERA_FOTO) {
                                captureImage();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No tiene permiso para usar la cámara.",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /*
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void captureImage() {
        Config.BANDERA_FOTO = false;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


//        if (Build.VERSION.SDK_INT >= 24) {
        fileUri = getOutputMediaFileUri_Nugat();
//        }else {
//            fileUri = getOutputMediaFileUri();
//        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        try {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setClipData(ClipData.newRawUri(null, fileUri));
        } catch (Exception e) {
            Toast.makeText(this, "Error = " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // start the image capture Intent

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Falló Inicio de cámara", Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getOutputMediaFileUri_Nugat() {
        filePhoto = getOutputMediaFile_Nugat();

        Uri photoURI = FileProvider.getUriForFile(CustomCameraActivity.this, getPackageName() + ".provider", filePhoto);
        return photoURI;
    }

    private File getOutputMediaFile_Nugat() {
        File mediaFile = null;
        mediaFile = getOutputFileLocal();
        return mediaFile;
    }

    public File getOutputFileLocal() {
        File mediaFile = null;
        try {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), Config.DIRECTORIO_FOTOS);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    UUID.randomUUID().toString() + "_" + timeStamp + ".jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mediaFile;
    }

    public Uri getOutputMediaFileUri() {
        Uri rta = Uri.EMPTY;
        filePhoto = getOutputFileLocal();
        if (filePhoto == null) {//FALLA
//            ACRA.getErrorReporter().handleException(new ExcepcionCreandoArchivos("Falla al armar la ubicacion destino de la foto " + ubicacionFoto));
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                return FileProvider.getUriForFile(CustomCameraActivity.this, BuildConfig.APPLICATION_ID + ".provider", filePhoto);
            } else {
                return Uri.fromFile(filePhoto);
            }
        }
        return rta;
    }

    /*
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//         if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE || requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE_2) {
            if (resultCode == RESULT_OK) {
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                Log.e(TAG, "La captura de imágen fue cancelada.");
                finish();
            } else {
                Log.e(TAG, "Ups. Falló la captura de imágen.");
            }
        }
    }

    /*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            Intent photoPreviewIntent;
            photoPreviewIntent = new Intent(this, PhotoPreview.class);
            photoPreviewIntent.putExtra("filePath", filePhoto.getPath());
            String _path = filePhoto.getPath();
            fileUri = null;

            redimensionarImagen(_path);

            photoPreviewIntent.putExtra("callingActivity", "");
            photoPreviewIntent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(photoPreviewIntent);
            finish();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void redimensionarImagen(String path) {

        Util.getBitmap(path, this);
    }
}