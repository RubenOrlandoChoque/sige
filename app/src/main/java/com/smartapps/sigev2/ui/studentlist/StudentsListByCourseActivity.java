package com.smartapps.sigev2.ui.studentlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.Config;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.classes.SyncResult;
import com.smartapps.sigev2.models.students.StudentViewModel;
import com.smartapps.sigev2.models.students.pojo.StudentListData;
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

public class StudentsListByCourseActivity extends AppCompatActivity implements View.OnClickListener {
    private StudentViewModel studentViewModel;
    private int courseYearId = 0;

    private ProgressDialog dialog;
    private TeacherViewModel teacherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list_by_course);

        teacherViewModel = ViewModelProviders.of(this).get(TeacherViewModel.class);
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);

        courseYearId = getIntent().getIntExtra("courseYearId", -1);
        String courseYearName = getIntent().getStringExtra("courseYearName");
        RecyclerView recyclerView = findViewById(R.id.recyclerview_students_list);
        ((TextView) findViewById(R.id.txt_ab_title)).setText(String.format("Alumnos del %s año", courseYearName));
        ((TextView) findViewById(R.id.newStudentByCourseYear)).setText(String.format("Nuevo Alumno del %s año", courseYearName));
        ((TextView) findViewById(R.id.empty_view)).setText(String.format("No hay alumnos en %s año", courseYearName));
        final StudentsListAdapter adapter = new StudentsListAdapter(this, studentViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        studentViewModel.getStudentList(courseYearId, SharedConfig.getShiftId()).observe(this, new Observer<List<StudentListData>>() {
            @Override
            public void onChanged(@Nullable List<StudentListData> studentData) {
                adapter.setCourseYears(studentData);
                adapter.notifyDataSetChanged();
                if (studentData.size() > 0) {
                    findViewById(R.id.recyclerview_students_list).setVisibility(View.VISIBLE);
                    findViewById(R.id.empty_view).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.lblStudentCount)).setText(Html.fromHtml("Alumnos inscriptos: " + "<b>" + studentData.size() + "</>"));
                } else {
                    findViewById(R.id.recyclerview_students_list).setVisibility(View.GONE);
                    findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                }
            }
        });
        findViewById(R.id.newStudentByCourseYear).setOnClickListener(this);

        findViewById(R.id.sync_button).setVisibility(View.VISIBLE);
        findViewById(R.id.sync_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((dialog == null || !dialog.isShowing())) {
                    dialog = ProgressDialog.show(StudentsListByCourseActivity.this, "",
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
                            new MaterialDialog.Builder(StudentsListByCourseActivity.this)
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
                            new MaterialDialog.Builder(StudentsListByCourseActivity.this)
                                    .title("Error al sincronizar")
                                    .content("Hubo un error al sincronizar los datos.\nPor favor intente nuevamente.")
                                    .positiveText("Aceptar")
                                    .negativeText("Detalle")
                                    .onPositive((d, which) -> {
                                        d.dismiss();
                                    })
                                    .onNegative((d, which) -> {
                                        d.dismiss();
                                        Intent intent = new Intent(StudentsListByCourseActivity.this, LogActivity.class);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newStudentByCourseYear:
                studentViewModel.createTempData("", false).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<StudentViewModel.TempData>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(StudentViewModel.TempData tempData) {
                                Intent intent = new Intent(StudentsListByCourseActivity.this, StudentTutorActivity.class);
                                intent.putExtra("studentId", tempData.studentId);
                                intent.putExtra("courseYearId", courseYearId);
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
