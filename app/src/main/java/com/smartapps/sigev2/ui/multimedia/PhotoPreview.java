package com.smartapps.sigev2.ui.multimedia;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.Config;
import com.smartapps.sigev2.util.Util;
import com.isseiaoki.simplecropview.CropImageView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import io.reactivex.CompletableSource;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class PhotoPreview extends Activity {

    private static final int CROP_FROM_CAMERA = 2;
    private static final String TAG = "PhotoPreview";
    public static String newPath = "";
    CropImageView mCropView;
    private String filePath;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    private Uri mSourceUri = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multimedia_preview_photo_v2);

        Intent in = getIntent();
        filePath = in.getStringExtra("filePath");
        mCropView = findViewById(R.id.multimedia_preview_photo_preview);
        mCropView.setCompressFormat(Bitmap.CompressFormat.JPEG);

        File imgFile = new File(filePath);
        Disposable disposable = new RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        if (imgFile.exists()) {
                            mSourceUri = getOutputMediaFileUri(imgFile);
                            loadImage(mSourceUri);
                        }
                    } else {
                        Toast.makeText(PhotoPreview.this,
                                "No tiene permiso para obtener fotos de la galeria.",
                                Toast.LENGTH_LONG).show();
                    }
                });

        ImageButton cropButton = findViewById(R.id.multimedia_preview_photo_crop);
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage();
            }
        });

        ImageButton rotateButton = findViewById(R.id.multimedia_preview_photo_rotate);
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
            }
        });

        ImageButton acceptButton = findViewById(R.id.btn_aceptar);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int anchoCrop = (int) mCropView.getActualCropRect().width();
                int altoCrop = (int) mCropView.getActualCropRect().height();
                int anchoImageReal = mCropView.getImageBitmap().getWidth();
                int altoImageReal = mCropView.getImageBitmap().getHeight();

                if (anchoCrop == anchoImageReal && altoCrop == altoImageReal) {
                    Intent intent = new Intent();
                    intent.putExtra("filePath", newPath);
                    setResult(RESULT_OK, intent);
                    System.gc();
                    finish();
                } else {
                    cropImageAnSave();
                }
            }
        });

        ImageButton cancelButton = findViewById(R.id.btn_cancelar);
        cancelButton.setOnClickListener(v -> onBackPressed());

        ImageButton cancelButton2 = findViewById(R.id.multimedia_preview_photo_button_cancel);
        cancelButton2.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Log.i(TAG, "RESULT ES DISTINTO DE OK EN ONACTIVITYRESULT.");
            return;
        }
        switch (requestCode) {
            case CROP_FROM_CAMERA:
                Log.i("ActivityResult>>>", "CROP_FROM_CAMERA");
                Bundle extras = data.getExtras();
                if (extras != null) {
                    String new_path = extras.getString("filePath");
                    Log.i("ActivityResult>>>", new_path);
                    Bitmap imgBitmap = Bitmap.createBitmap(BitmapFactory.decodeFile(new_path));
                    int altura = imgBitmap.getHeight();
                    Display d = getWindowManager().getDefaultDisplay();
                    int Screenwidth = Util.getAnchoPantalla(d);
                    int nuevaAltura = (Screenwidth * altura) / imgBitmap.getWidth();
                    imgBitmap = null;

                    Bitmap pq = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(new_path), Screenwidth, nuevaAltura, true);
                    mCropView.setImageBitmap(pq);
                    filePath = new_path;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Util.borrarArchivo(filePath);
        System.gc();
        finish();
    }

    private Disposable loadImage(final Uri uri) {
        return new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(@NonNull Boolean granted)
                            throws Exception {
                        return granted;
                    }
                })
                .flatMapCompletable(new Function<Boolean, CompletableSource>() {
                    @Override
                    public CompletableSource apply(@NonNull Boolean aBoolean)
                            throws Exception {
                        return mCropView.load(uri)
                                .executeAsCompletable();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
    }

    private Disposable cropImage() {
        return mCropView.crop(mSourceUri)
                .executeAsSingle()
                .flatMap(new Function<Bitmap, SingleSource<Uri>>() {
                    final File filePhoto = getOutputFileLocal();
                    final Uri uri = getOutputMediaFileUri(filePhoto);

                    @Override
                    public SingleSource<Uri> apply(@NonNull Bitmap bitmap)
                            throws Exception {
                        Log.e("error>", "d");
                        return mCropView.save(bitmap)
                                .compressFormat(mCompressFormat)
                                .executeAsSingle(uri);
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable)
                            throws Exception {
                        //showProgress();
                        Log.e("error>", "d");
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Util.borrarArchivo(filePath);
                        filePath = newPath;
                        //dismissProgress();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(@NonNull Uri uri) throws Exception {
                        mSourceUri = uri;
                        loadImage(uri);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable)
                            throws Exception {
                        Log.e("error crop>>>>", (throwable.getMessage() != null ? throwable.getMessage() : ""));
                    }
                });
    }

    private Disposable cropImageAnSave() {
        return mCropView.crop(mSourceUri)
                .executeAsSingle()
                .flatMap(new Function<Bitmap, SingleSource<Uri>>() {
                    final File filePhoto = getOutputFileLocal();
                    final Uri uri = getOutputMediaFileUri(filePhoto);

                    @Override
                    public SingleSource<Uri> apply(@NonNull Bitmap bitmap)
                            throws Exception {
                        return mCropView.save(bitmap)
                                .compressFormat(mCompressFormat)
                                .executeAsSingle(uri);
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable)
                            throws Exception {
                        //showProgress();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Util.borrarArchivo(filePath);
                        filePath = newPath;
                        //dismissProgress();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(@NonNull Uri uri) throws Exception {
                        Intent intent = new Intent();
                        intent.putExtra("filePath", newPath);
                        setResult(RESULT_OK, intent);
                        System.gc();
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable)
                            throws Exception {
                        Log.e("error crop and save>>>>", (throwable.getMessage() != null ? throwable.getMessage() : ""));
                    }
                });
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

    public Uri getOutputMediaFileUri(final File filePhoto) {
        PhotoPreview.newPath = filePhoto != null ? filePhoto.getAbsolutePath() : "";
        return Uri.fromFile(filePhoto);
    }
}