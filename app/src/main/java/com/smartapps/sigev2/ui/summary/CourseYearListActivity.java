package com.smartapps.sigev2.ui.summary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.Config;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.classes.SyncResult;
import com.smartapps.sigev2.models.courseyear.CourseYearViewModel;
import com.smartapps.sigev2.models.courseyear.pojo.CourseYearStudentsCount;
import com.smartapps.sigev2.models.students.StudentViewModel;
import com.smartapps.sigev2.models.teacher.TeacherViewModel;
import com.smartapps.sigev2.services.Upload;
import com.smartapps.sigev2.ui.LogActivity;
import com.smartapps.sigev2.ui.newstudent.StudentTutorActivity;
import com.smartapps.sigev2.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CourseYearListActivity extends AppCompatActivity implements View.OnClickListener {
    private CourseYearViewModel courseYearViewModel;

    private ProgressDialog dialog;
    private StudentViewModel studentViewModel;
    private TeacherViewModel teacherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_year_list);

        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        teacherViewModel = ViewModelProviders.of(this).get(TeacherViewModel.class);

        String shit = SharedConfig.getShiftDescription();
        ((TextView) findViewById(R.id.txt_ab_title)).setText(String.format("Turno: %s", shit) + " - "+ "Año: " + SharedConfig.getCurrentSchoolYear());

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final CourseYearListAdapter adapter = new CourseYearListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.newStudent).setOnClickListener(this);

        courseYearViewModel = ViewModelProviders.of(this).get(CourseYearViewModel.class);
        courseYearViewModel.getAllWithStudentsCount(SharedConfig.getShiftId()).observe(CourseYearListActivity.this, new Observer<List<CourseYearStudentsCount>>() {
            @Override
            public void onChanged(@Nullable List<CourseYearStudentsCount> courseYears) {
                adapter.setCourseYears(courseYears);
                adapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.sync_button).setVisibility(View.VISIBLE);
        findViewById(R.id.sync_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((dialog == null || !dialog.isShowing())) {
                    dialog = ProgressDialog.show(CourseYearListActivity.this, "",
                            "" +
                                    "Subiendo datos", true);
                }
                new Upload().Sync(studentViewModel, teacherViewModel, new Upload.UploadListner() {
                    @Override
                    public void finish(SyncResult successImages, SyncResult successStudent, boolean successTeacher) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (successImages.result && successStudent.result && successTeacher) {
                            new MaterialDialog.Builder(CourseYearListActivity.this)
                                    .title("Sincronización completa")
                                    .content("Los datos se sincronizaron correctamente")
                                    .canceledOnTouchOutside(false)
                                    .positiveText("Aceptar")
                                    .onPositive((d, which) -> {
                                        d.dismiss();
                                    })
                                    .show();
                        } else {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
                            String nameFile = "log_Sync_" + simpleDateFormat.format(new Date()) + ".txt";
                            final String path = Util.generateNoteOnSD(Config.DIRECTORIO_LOG, nameFile, successImages.errors + "\n" + successStudent.errors);
                            new MaterialDialog.Builder(CourseYearListActivity.this)
                                    .title("Error al sincronizar")
                                    .content("Hubo un error al sincronizar los datos.\nPor favor intente nuevamente.")
                                    .positiveText("Aceptar")
                                    .negativeText("Detalle")
                                    .onPositive((d, which) -> {
                                        d.dismiss();
                                    })
                                    .onNegative((d, which) -> {
                                        d.dismiss();
                                        Intent intent = new Intent(CourseYearListActivity.this, LogActivity.class);
                                        intent.putExtra("path", path);
                                        startActivity(intent);
                                    })
                                    .show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newStudent: {
                studentViewModel.createTempData("", false).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<StudentViewModel.TempData>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(StudentViewModel.TempData tempData) {
                                Intent intent = new Intent(CourseYearListActivity.this, StudentTutorActivity.class);
                                intent.putExtra("studentId", tempData.studentId);
                                intent.putExtra("action", StudentTutorActivity.ADD_STUDENT);
                                intent.putExtra("newStudent", tempData.newStudent);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                break;
            }
        }
    }
}
