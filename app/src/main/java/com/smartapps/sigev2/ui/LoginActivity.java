package com.smartapps.sigev2.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.error.ANError;
import com.microblink.MicroblinkSDK;
import com.microblink.licence.exception.InvalidLicenceKeyException;
import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.models.courseyear.CourseYearViewModel;
import com.smartapps.sigev2.models.division.DivisionViewModel;
import com.smartapps.sigev2.models.documentations.DocumentationViewModel;
import com.smartapps.sigev2.models.documenttypes.DocumentTypesViewModel;
import com.smartapps.sigev2.models.educationlevels.EducationLevelsViewModel;
import com.smartapps.sigev2.models.persongenders.PersonGendersViewModel;
import com.smartapps.sigev2.models.relationship.RelationshipViewModel;
import com.smartapps.sigev2.models.schoolyear.SchoolYearViewModel;
import com.smartapps.sigev2.models.shifts.ShiftViewModel;
import com.smartapps.sigev2.models.studentconditions.StudentConditionsViewModel;
import com.smartapps.sigev2.models.students.StudentViewModel;
import com.smartapps.sigev2.models.typeemployee.TypeEmployeeViewModel;
import com.smartapps.sigev2.services.AuthService;
import com.google.android.material.textfield.TextInputEditText;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private CourseYearViewModel courseYearViewModel;
    private DivisionViewModel divisionViewModel;
    private RelationshipViewModel relationshipViewModel;
    private PersonGendersViewModel personGendersViewModel;
    private EducationLevelsViewModel educationLevelsViewModel;
    private DocumentTypesViewModel documentTypesViewModel;
    private StudentConditionsViewModel studentConditionsViewModel;
    private ShiftViewModel shiftViewModel;
    private TypeEmployeeViewModel typeEmployeeViewModel;
    private DocumentationViewModel documentationViewModel;
    private StudentViewModel studentViewModel;
    private SchoolYearViewModel schoolYearViewModel;


    private ProgressDialog dialog;
    private TextInputEditText user;
    private TextInputEditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        courseYearViewModel = ViewModelProviders.of(this).get(CourseYearViewModel.class);
        divisionViewModel = ViewModelProviders.of(this).get(DivisionViewModel.class);
        relationshipViewModel = ViewModelProviders.of(this).get(RelationshipViewModel.class);
        personGendersViewModel = ViewModelProviders.of(this).get(PersonGendersViewModel.class);
        educationLevelsViewModel = ViewModelProviders.of(this).get(EducationLevelsViewModel.class);
        documentTypesViewModel = ViewModelProviders.of(this).get(DocumentTypesViewModel.class);
        studentConditionsViewModel = ViewModelProviders.of(this).get(StudentConditionsViewModel.class);
        shiftViewModel = ViewModelProviders.of(this).get(ShiftViewModel.class);
        typeEmployeeViewModel = ViewModelProviders.of(this).get(TypeEmployeeViewModel.class);
        documentationViewModel = ViewModelProviders.of(this).get(DocumentationViewModel.class);
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        schoolYearViewModel = ViewModelProviders.of(this).get(SchoolYearViewModel.class);

        user = findViewById(R.id.input_user_name);
        pass = findViewById(R.id.input_password);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViewById(R.id.signinBtn).setOnClickListener(this);
        checkPermissions();

        findViewById(R.id.styledBackground).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
                View root = inflater.inflate(R.layout.dialog_url, null);
                ((EditText) root.findViewById(R.id.url_server)).setText(SharedConfig.getServerUrl());
                builder.setView(root)
                        .setPositiveButton("Aceptar", (dialog, id) -> {
                            SharedConfig.setServerUrl(((EditText) root.findViewById(R.id.url_server)).getText().toString());
                        })
                        .setNegativeButton("Cancelar", (dialog, id) -> dialog.cancel());
                builder.create().show();
                return false;
            }
        });
    }

    private void checkPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        Disposable subscribe = rxPermissions
                .request(Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signinBtn:
                if (isValid()) {
                    dialog = new ProgressDialog(LoginActivity.this, R.style.CustomDialogStyle);

                    dialog.setTitle("");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setMessage(" Validando usuario.");
                    dialog.setIndeterminate(true);
                    dialog.show();
                    new AuthService().login(user.getText().toString(), pass.getText().toString(), new AuthService.OnLoginEvent() {
                        @SuppressLint("CheckResult")
                        @Override
                        public void successLogin() {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            dialog = new ProgressDialog(LoginActivity.this, R.style.CustomDialogStyle);
                            dialog.setTitle("");
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setMessage("Descargando datos.");
                            dialog.setIndeterminate(true);
                            dialog.show();
                            Observable.combineLatest(courseYearViewModel.rxDownload(), divisionViewModel.rxDownload(), relationshipViewModel.rxDownload(), personGendersViewModel.rxDownload(), shiftViewModel.rxDownload(), typeEmployeeViewModel.rxDownload(), documentationViewModel.rxDownload(), studentViewModel.rxDownload(),
                                    (cy, d, r, pg, s, te, doc, st) -> {
                                        Result re = new Result();
                                        re.divisions = d;
                                        re.courseYears = cy;
                                        re.personGenders = pg;
                                        re.relationships = r;
                                        re.shift = s;
                                        re.typeEmployees = te;
                                        re.documentations = doc;
                                        re.students = st;
                                        //re.schoolYears = sy;
                                        return re;
                                    })
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            result -> {
                                                Single<Boolean> insert = Single.create(emitter -> {
                                                    // Save data
                                                    try {
                                                        courseYearViewModel.save(result.courseYears);
                                                        divisionViewModel.save(result.divisions);
                                                        relationshipViewModel.save(result.relationships);
                                                        personGendersViewModel.save(result.personGenders);
                                                        shiftViewModel.save(result.shift);
                                                        typeEmployeeViewModel.save(result.typeEmployees);
                                                        documentationViewModel.save(result.documentations);
                                                        studentViewModel.save(result.students);
                                                        //schoolYearViewModel.save(result.schoolYears);
                                                        emitter.onSuccess(true);
                                                    } catch (Exception e) {
                                                        emitter.onError(e);
                                                    }
                                                });
                                                insert.subscribeOn(Schedulers.newThread())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(s -> {

                                                            /////////////////////
                                                            Observable.combineLatest(documentTypesViewModel.rxDownload(), schoolYearViewModel.rxDownload(), educationLevelsViewModel.rxDownload(), studentConditionsViewModel.rxDownload(),
                                                                    (dt, sy, el, sc) -> {
                                                                        Result re = new Result();
                                                                        re.documentTypes = dt;
                                                                        re.schoolYears = sy;
                                                                        re.educationLevels = el;
                                                                        re.studentConditions = sc;
                                                                        return re;
                                                                    })
                                                                    .subscribeOn(Schedulers.newThread())
                                                                    .observeOn(AndroidSchedulers.mainThread())
                                                                    .subscribe(
                                                                            result1 -> {
                                                                                Single<Boolean> insert1 = Single.create(emitter -> {
                                                                                    // Save data
                                                                                    try {
                                                                                        documentTypesViewModel.save(result1.documentTypes);
                                                                                        schoolYearViewModel.save(result1.schoolYears);
                                                                                        educationLevelsViewModel.save(result1.educationLevels);
                                                                                        studentConditionsViewModel.save(result1.studentConditions);
                                                                                        emitter.onSuccess(true);
                                                                                    } catch (Exception e) {
                                                                                        emitter.onError(e);
                                                                                    }
                                                                                });
                                                                                insert1.subscribeOn(Schedulers.newThread())
                                                                                        .observeOn(AndroidSchedulers.mainThread())
                                                                                        .subscribe(ss -> {
                                                                                            if (dialog != null && dialog.isShowing()) {
                                                                                                dialog.dismiss();
                                                                                            }
                                                                                            Intent intent = new Intent(LoginActivity.this, SchoolYearActivity.class);
                                                                                            startActivity(intent);
                                                                                            finish();
                                                                                        }, throwable -> {
                                                                                            if (dialog != null && dialog.isShowing()) {
                                                                                                dialog.dismiss();
                                                                                            }
                                                                                        });
                                                                            },
                                                                            error -> {
                                                                                if (dialog != null && dialog.isShowing()) {
                                                                                    dialog.dismiss();
                                                                                }
                                                                                new MaterialDialog.Builder(LoginActivity.this)
                                                                                        .title("Error")
                                                                                        .content("Hubo un error al descargar los datos.\nPor favor intente nuevamente")
                                                                                        .positiveText("Aceptar")
                                                                                        .show();
                                                                            });
                                                            /////////////////////
                                                            /*if (dialog != null && dialog.isShowing()) {
                                                                dialog.dismiss();
                                                            }
                                                            Intent intent = new Intent(LoginActivity.this, SchoolYearActivity.class);
                                                            startActivity(intent);
                                                            finish();*/
                                                        }, throwable -> {
                                                            if (dialog != null && dialog.isShowing()) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                            },
                                            error -> {
                                                if (dialog != null && dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                new MaterialDialog.Builder(LoginActivity.this)
                                                        .title("Error")
                                                        .content("Hubo un error al descargar los datos.\nPor favor intente nuevamente")
                                                        .positiveText("Aceptar")
                                                        .show();
                                            });
                        }

                        @Override
                        public void errorLogin(String msg) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            new MaterialDialog.Builder(LoginActivity.this)
                                    .title("Error")
                                    .content("Hubo un error al iniciar sesión.\nPor favor intente nuevamente")
                                    .positiveText("Aceptar")
                                    .show();
                        }
                    });
                }
                break;
        }
    }

    private boolean isValid() {
        String txtUser = user.getText().toString();
        String txtPass = pass.getText().toString();
        if (txtUser.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Debe ingresar el usuario.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (txtPass.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Debe ingresar la contraseña.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private class Result {
        JSONObject documentTypes;
        JSONObject divisions;
        JSONObject courseYears;
        JSONObject personGenders;
        JSONObject relationships;
        JSONObject shift;
        JSONObject typeEmployees;
        JSONObject documentations;
        JSONObject students;
        JSONObject schoolYears;
        JSONObject educationLevels;
        JSONObject studentConditions;
    }
}