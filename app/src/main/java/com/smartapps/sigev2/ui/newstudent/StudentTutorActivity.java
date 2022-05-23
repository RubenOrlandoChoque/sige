package com.smartapps.sigev2.ui.newstudent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.NonSwipeableViewPager;
import com.smartapps.sigev2.models.students.StudentViewModel;
import com.smartapps.sigev2.models.students.pojo.StudentData;
import com.smartapps.sigev2.util.Evento;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StudentTutorActivity extends AppCompatActivity {

    public static final int ADD_STUDENT = 1;
    public static final int EDIT_STUDENT = 2;

    private NonSwipeableViewPager mViewPager;
    private ProgressDialog dialog;
    private StudentViewModel studentViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    int loaded = 0;
    private TextView titleHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_student_tutor);

        if ((dialog == null || !dialog.isShowing())) {
            dialog = ProgressDialog.show(this, "",
                    "Cargando", true);
        }
        loaded = 0;

        // params
        String studentId = getIntent().getStringExtra("studentId");
        int courseYearId = getIntent().getIntExtra("courseYearId", -1);
        int action = getIntent().getIntExtra("action", StudentTutorActivity.ADD_STUDENT);
        boolean newStudent = getIntent().getBooleanExtra("newStudent", true);
//        View models
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), studentId);

        mViewPager = findViewById(R.id.container);
        mViewPager.setStudentId(studentId);
        mViewPager.setCourseYearId(courseYearId);
        mViewPager.setAction(action);
        mViewPager.setNewStudent(newStudent);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        titleHeader = findViewById(R.id.title_header);
        titleHeader.setText("Alumno");

        BottomNavigationBar bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar.clearAll();

        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED); //"MODE_DEFAULT", "MODE_FIXED", "MODE_SHIFTING", "MODE_FIXED_NO_TITLE", "MODE_SHIFTING_NO_TITLE"
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE); //"BACKGROUND_STYLE_DEFAULT", "BACKGROUND_STYLE_STATIC", "BACKGROUND_STYLE_RIPPLE"

        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_school_black_24dp, "Alumno").setActiveColorResource(R.color.orange));//.setBadgeItem(numberBadgeItem))
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_people_black_24dp, "Tutores").setActiveColorResource(R.color.orange));

        bottomNavigationBar.initialise();
        bottomNavigationBar.selectTab(0, true);

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        mViewPager.setCurrentItem(0);
                        titleHeader.setText("Alumno");
                        break;
                    case 1:
                        mViewPager.setCurrentItem(1);
                        titleHeader.setText("Tutores");
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

        findViewById(R.id.save_header_student_tutor).setOnClickListener(v -> {
            save();
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> {
            onBack();
        });
    }

    public void save() {
        Fragment s = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":0");
        if ((dialog == null || !dialog.isShowing())) {
            dialog = ProgressDialog.show(this, "", "Guardando", true);
        }

        StudentData studentData = ((StudentFragment) s).getData();
        studentViewModel.validar(studentData).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String tempData) {
                        saveOnSuccess(studentData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        new MaterialDialog.Builder(StudentTutorActivity.this)
                                .title("Error")
                                .content(Html.fromHtml(e.getMessage()))
                                .positiveText("Aceptar")
                                .onPositive((d, which) -> d.dismiss())
                                .show();
                    }
                });
    }

    private void saveOnSuccess(StudentData studentData) {
        studentViewModel.saveStudent(studentData).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                new MaterialDialog.Builder(StudentTutorActivity.this)
                        .title("Guardado Correcto")
                        .canceledOnTouchOutside(false)
                        .content("Datos guardados correctamente")
                        .positiveText("Aceptar")
                        .onPositive((d, which) -> {
                            d.dismiss();
                            if (mViewPager.getAction() == StudentTutorActivity.ADD_STUDENT) {
                                StudentTutorActivity.this.nuevoAlumnoMismoTutor();
                            } else {
                                studentViewModel.deleteAllTempDataSingle().subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new SingleObserver<Boolean>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onSuccess(Boolean result) {
                                                StudentTutorActivity.this.finish();
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }
                                        });
                            }
                        })
                        .show();
            }

            @Override
            public void onError(Throwable e) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                new MaterialDialog.Builder(StudentTutorActivity.this)
                        .title("Error")
                        .content("Hubo un error al guardar los datos. Intente nuevamente. Si el error persiste contacte con el adminsitrador.")
                        .positiveText("Aceptar")
                        .onPositive((d, which) -> d.dismiss())
                        .show();
            }
        });
    }

    private void nuevoAlumnoMismoTutor() {
        new MaterialDialog.Builder(StudentTutorActivity.this)
                .title("")
                .content("¿Desea cargar un nuevo alumno con el mismo tutor?")
                .positiveText("Si")
                .negativeText("no")
                .onPositive((d, which) -> {
                    d.dismiss();
                    studentViewModel.createTempData("", true).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<StudentViewModel.TempData>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(StudentViewModel.TempData tempData) {
                                    Intent intent = new Intent(StudentTutorActivity.this, StudentTutorActivity.class);
                                    intent.putExtra("studentId", tempData.studentId);
                                    intent.putExtra("action", StudentTutorActivity.ADD_STUDENT);
                                    intent.putExtra("newStudent", tempData.newStudent);
                                    StudentTutorActivity.this.startActivity(intent);
                                    StudentTutorActivity.this.finish();
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                })
                .onNegative((dialog, which) -> {
                    studentViewModel.deleteAllTempDataSingle().subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Boolean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Boolean result) {
                                    dialog.dismiss();
                                    StudentTutorActivity.this.finish();
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                })
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(Evento event) {
        switch (event.eventName) {
            case "finishLoadData":
                loaded++;
                if (event.data.toString().isEmpty()) {
                    if (loaded == 1) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    new MaterialDialog.Builder(StudentTutorActivity.this)
                            .title("Error")
                            .content(event.data.toString())
                            .positiveText("Aceptar")
                            .onPositive((d, which) -> {
                                d.dismiss();
                                finish();
                            })
                            .show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mDisposable.clear();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack(){
        new MaterialDialog.Builder(StudentTutorActivity.this)
                .title("Info")
                .content("¿Salir sin guardar los cambios?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    studentViewModel.deleteAllTempDataSingle().subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Boolean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Boolean result) {
                                    d.dismiss();
                                    StudentTutorActivity.this.finish();
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }
}
