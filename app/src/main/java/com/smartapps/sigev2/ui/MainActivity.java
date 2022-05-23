package com.smartapps.sigev2.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.afollestad.materialdialogs.MaterialDialog;
import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.Config;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.classes.SyncResult;
import com.smartapps.sigev2.models.students.StudentViewModel;
import com.smartapps.sigev2.models.teacher.TeacherViewModel;
import com.smartapps.sigev2.services.Upload;
import com.smartapps.sigev2.ui.multimedia.RoundedImageView;
import com.smartapps.sigev2.ui.shift.ShiftActivity;
import com.smartapps.sigev2.ui.summary.CourseYearListActivity;
import com.smartapps.sigev2.ui.summaryteacher.TeacherListActivity;
import com.smartapps.sigev2.util.Util;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    private StudentViewModel studentViewModel;
    private TeacherViewModel teacherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        teacherViewModel = ViewModelProviders.of(this).get(TeacherViewModel.class);
        setDrawerMenu();
    }

    public void onStudentsClick(View view) {
        Intent intent = new Intent(this, CourseYearListActivity.class);
        startActivity(intent);
    }

    public void onTeachersClick(View view) {
        Intent intent = new Intent(this, TeacherListActivity.class);
        startActivity(intent);
    }


    private void setDrawerMenu() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navview);

        findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        View headerView = navView.getHeaderView(0);
        RoundedImageView imgUser = headerView.findViewById(R.id.img_user_photo);
        TextView txtUser = headerView.findViewById(R.id.txt_user_name);

        if (SharedConfig.getUserShiftIds().size() > 1) {
            navView.getMenu().findItem(R.id.menu_change_shift).setVisible(true);
        } else {
            navView.getMenu().findItem(R.id.menu_change_shift).setVisible(false);
        }

        //txtUser.setText(SharedConfig.getUserName() + " " + SharedConfig.getUserSurname());
        txtUser.setText("user");
//        Glide.with(this)
//                .load(SharedConfig.getUserPhoto())
//                .asBitmap()
//                .placeholder(R.drawable.default_user)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .into(imgUser);

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_loguot:
                                closeSession();
                                break;
                            case R.id.menu_sync:
                                if ((dialog == null || !dialog.isShowing())) {
                                    dialog = ProgressDialog.show(MainActivity.this, "",
                                            "" +
                                                    "Subiendo datos", true);
                                }
                                new Upload().Sync(studentViewModel, teacherViewModel, new Upload.UploadListner() {
                                    @Override
                                    public void finish(SyncResult successImages, SyncResult successStudent, boolean successTeacher) {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.dismiss();
                                        }

                                        if(successImages.result && successStudent.result && successTeacher) {
                                            new MaterialDialog.Builder(MainActivity.this)
                                                    .title("Sincronización completa")
                                                    .content("Los datos se sincronizaron correctamente")
                                                    .canceledOnTouchOutside(false)
                                                    .positiveText("Aceptar")
                                                    .onPositive((d, which) -> {
                                                        d.dismiss();
                                                    })
                                                    .show();
                                        }else{
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
                                            String nameFile = "log_Sync_" + simpleDateFormat.format(new Date()) + ".txt";
                                            final String path = Util.generateNoteOnSD(Config.DIRECTORIO_LOG, nameFile, successImages.errors + "\n" + successStudent.errors);
                                            new MaterialDialog.Builder(MainActivity.this)
                                                    .title("Error al sincronizar")
                                                    .content("Hubo un error al sincronizar los datos.\nPor favor intente nuevamente.")
                                                    .positiveText("Aceptar")
                                                    .negativeText("Detalle")
                                                    .onPositive((d, which) -> {
                                                        d.dismiss();
                                                    })
                                                    .onNegative((d, which) -> {
                                                        d.dismiss();
                                                        Intent intent = new Intent(MainActivity.this, LogActivity.class);
                                                        intent.putExtra("path", path);
                                                        startActivity(intent);
                                                    })
                                                    .show();
                                        }
                                    }
                                });
                                break;
                            case R.id.menu_change_shift:
                                Intent intent = new Intent(MainActivity.this, ShiftActivity.class);
                                intent.putExtra("origin", "MainActivity");
                                startActivity(intent);
                                finish();
                                break;
                            case R.id.menu_change_shoolYear:
                                Intent i = new Intent(MainActivity.this, SchoolYearActivity.class);
                                i.putExtra("origin", "MainActivity");
                                startActivity(i);
                                finish();
                                break;
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(MainActivity.this)
                .title("Info")
                .content("¿Desea cerrar la aplicación?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    d.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        super.finishAndRemoveTask();
                    } else {
                        super.finish();
                    }
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }

    private void closeSession() {
        new MaterialDialog.Builder(MainActivity.this)
                .title("Info")
                .content("¿Cerrar sesión?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    d.dismiss();
                    SharedConfig.setToken("");
                    SharedConfig.setUserCode("");
                    SharedConfig.setUserName("");
                    SharedConfig.setUserPhoto("");
                    SharedConfig.setUserSurname("");
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        String shit = SharedConfig.getShiftDescription();
        ((TextView) findViewById(R.id.txt_ab_title)).setText(String.format("Turno: %s", shit + " - "+ "Año: " + SharedConfig.getCurrentSchoolYear()));
    }
}