package com.smartapps.sigev2.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.models.people.Person;
import com.smartapps.sigev2.models.people.PersonViewModel;
import com.smartapps.sigev2.models.persongenders.PersonGenders;
import com.smartapps.sigev2.models.persongenders.PersonGendersViewModel;
import com.smartapps.sigev2.models.teacher.TeacherViewModel;
import com.smartapps.sigev2.models.teacher.pojo.TeacherData;
import com.smartapps.sigev2.models.typeemployee.TypeEmployee;
import com.smartapps.sigev2.models.typeemployee.TypeEmployeeViewModel;
import com.smartapps.sigev2.ui.scanner.SimpleScannerActivity;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lib.kingja.switchbutton.SwitchMultiButton;

import static br.com.zbra.androidlinq.Linq.stream;

public class TeacherActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    private ProgressDialog dialog;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private PersonGendersViewModel personGendersViewModel;
    private TeacherViewModel teacherViewModel;
    public static final int SCANNER_ACTIVITY_RESULT = 3;
    private PersonViewModel personViewModel;
    private TypeEmployeeViewModel typeEmplPersonViewModel;
    TextWatcher textWatcher;

    List<PersonGenders> personGendersList;
    String[] personGendersString;

    List<TypeEmployee> typeEmployeesList;
    String[] typeEmployeesString;

    SimpleDateFormat simpleDateFormat;
    TextView dateTextView;
    TextView hireDateTextView;
    ImageButton dateButton;
    Button hireDateButton;
    private String teacherId = "";
    String scannerResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        ((TextView) findViewById(R.id.txt_ab_title)).setText("Profesor");

        personGendersViewModel = ViewModelProviders.of(this).get(PersonGendersViewModel.class);
        teacherViewModel = ViewModelProviders.of(this).get(TeacherViewModel.class);
        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        typeEmplPersonViewModel = ViewModelProviders.of(this).get(TypeEmployeeViewModel.class);

        ((EditText) findViewById(R.id.input_lastname)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ((EditText) findViewById(R.id.input_firstame)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        dateButton = findViewById(R.id.set_date_button);
        hireDateButton = findViewById(R.id.set_hiredate_button);
        dateTextView = findViewById(R.id.input_birthdate);
        hireDateTextView = findViewById(R.id.input_hiredate);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate(1980, 0, 1, R.style.DatePickerSpinner);
            }
        });

        hireDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHireDate(1980, 0, 1, R.style.DatePickerSpinner);
            }
        });
        personGendersViewModel.getAll().observe(this, new Observer<List<PersonGenders>>() {
            @Override
            public void onChanged(@Nullable List<PersonGenders> personGenders) {
                personGendersString = stream(personGenders).select(PersonGenders::getDescription).toList().toArray(new String[personGenders.size()]);
                personGendersList = personGenders;
                ((SwitchMultiButton) findViewById(R.id.gender)).setText(personGendersString);
                ((SwitchMultiButton) findViewById(R.id.gender)).clearSelection();

                loadTypeEmployees();
            }
        });

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String dni = s.toString();
                personViewModel.getByDni(dni).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Person>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Person person) {
                        if (person != null) {
                            ((EditText) findViewById(R.id.input_firstame)).setText(person.getFirstName());
                            ((EditText) findViewById(R.id.input_lastname)).setText(person.getLastName());
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            if (person.getBirthdate() != null) {
                                ((EditText) findViewById(R.id.input_birthdate)).setText(format.format(person.getBirthdate()));
                            }
                            PersonGenders pg = stream(personGendersList).where(e -> e.getId() == person.getPersonGenderId()).firstOrNull();
                            int pos = 0;
                            if (pg != null) {
                                pos = personGendersList.indexOf(pg);
                            }
                            ((SwitchMultiButton) findViewById(R.id.gender)).setSelectedTab(pos);

                            ((EditText) findViewById(R.id.input_phone_number)).setText(person.getPhoneNumber());
                            ((EditText) findViewById(R.id.input_mobile_phone_number)).setText(person.getMobilePhoneNumber());
                            ((EditText) findViewById(R.id.input_address)).setText(person.getAddress());

                            teacherId = person.getId();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        };

        ((EditText) findViewById(R.id.input_document_number)).addTextChangedListener(textWatcher);
    }

    private void loadTypeEmployees() {
        typeEmplPersonViewModel.getAll().observe(this, new Observer<List<TypeEmployee>>() {
            @Override
            public void onChanged(@Nullable List<TypeEmployee> typeEmployees) {
                typeEmployeesString = stream(typeEmployees).select(TypeEmployee::getDescription).toList().toArray(new String[typeEmployees.size()]);
                typeEmployeesList = typeEmployees;
                ((SwitchMultiButton) findViewById(R.id.typeEmployee)).setText(typeEmployeesString);
                ((SwitchMultiButton) findViewById(R.id.typeEmployee)).clearSelection();

                loadTeacherData();
            }
        });
    }

    private void loadTeacherData() {
        // load data
        teacherId = getIntent().getStringExtra("teacherId");
        if (teacherId != null && !teacherId.isEmpty()) { //buscar estudiante
            teacherViewModel.getTeacher(teacherId, SharedConfig.getShiftId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<TeacherData>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(TeacherData teacherData) {
                    // success
                    ((EditText) findViewById(R.id.input_firstame)).setText(teacherData.getFirstName());
                    ((EditText) findViewById(R.id.input_lastname)).setText(teacherData.getLastName());
                    ((EditText) findViewById(R.id.input_document_number)).removeTextChangedListener(textWatcher);
                    ((EditText) findViewById(R.id.input_document_number)).setText(teacherData.getDocumentNumber());
                    ((EditText) findViewById(R.id.input_document_number)).addTextChangedListener(textWatcher);

                    ((EditText) findViewById(R.id.input_cuil_number)).setText(teacherData.getCUIL());

                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    if (teacherData.getBirthdate() != null) {
                        ((EditText) findViewById(R.id.input_birthdate)).setText(format.format(teacherData.getBirthdate()));
                    }

                    if (teacherData.getHireDate() != null) {
                        ((EditText) findViewById(R.id.input_hiredate)).setText(format.format(teacherData.getHireDate()));
                    }

                    TypeEmployee typeEmployee = stream(typeEmployeesList).where(e -> e.getId() == teacherData.getTypeEmployeeId()).firstOrNull();
                    int posTypeEmployee = 0;
                    if (typeEmployee != null) {
                        posTypeEmployee = typeEmployeesList.indexOf(typeEmployee);
                    }
                    ((SwitchMultiButton) findViewById(R.id.typeEmployee)).setSelectedTab(posTypeEmployee);

                    PersonGenders personGender = stream(personGendersList).where(e -> e.getId() == teacherData.getPersonGenderId()).firstOrNull();
                    int posPersonGender = 0;
                    if (personGender != null) {
                        posPersonGender = personGendersList.indexOf(personGender);
                    }
                    ((SwitchMultiButton) findViewById(R.id.gender)).setSelectedTab(posPersonGender);

                    ((EditText) findViewById(R.id.input_phone_number)).setText(teacherData.getPhoneNumber());
                    ((EditText) findViewById(R.id.input_mobile_phone_number)).setText(teacherData.getMobilePhoneNumber());
                    ((EditText) findViewById(R.id.input_address)).setText(teacherData.getAddress());
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {
                    // no rows
                }
            });
        }
    }

    @Override
    public void onClick(View view) {

    }

    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        String bd = ((EditText) findViewById(R.id.input_birthdate)).getText().toString().trim();
        Date date;
        if (bd.isEmpty()) {
            date = new Date();
        } else {
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                date = format.parse(bd);
            } catch (Exception ignored) {
                date = new Date();
            }
        }
        new SpinnerDatePickerDialogBuilder()
                .context(TeacherActivity.this)
                .callback((view, year1, monthOfYear1, dayOfMonth1) -> {
                    Calendar calendar = new GregorianCalendar(year1, monthOfYear1, dayOfMonth1);
                    dateTextView.setText(simpleDateFormat.format(calendar.getTime()));
                })
                .spinnerTheme(spinnerTheme)
                .defaultDate(Integer.parseInt(DateFormat.format("yyyy", date).toString()), Integer.parseInt(DateFormat.format("M", date).toString()) - 1, Integer.parseInt(DateFormat.format("d", date).toString()))
                .build()
                .show();
    }

    void showHireDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        String bd = ((EditText) findViewById(R.id.input_hiredate)).getText().toString().trim();
        Date date;
        if (bd.isEmpty()) {
            date = new Date();
        } else {
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                date = format.parse(bd);
            } catch (Exception ignored) {
                date = new Date();
            }
        }
        new SpinnerDatePickerDialogBuilder()
                .context(TeacherActivity.this)
                .callback(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        hireDateTextView.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                })
                .spinnerTheme(spinnerTheme)
                .defaultDate(Integer.parseInt(DateFormat.format("yyyy", date).toString()), Integer.parseInt(DateFormat.format("M", date).toString()) - 1, Integer.parseInt(DateFormat.format("d", date).toString()))
                .build()
                .show();
    }

    public void scan(View view) {
        Intent i = new Intent(this, SimpleScannerActivity.class);
        startActivityForResult(i, SCANNER_ACTIVITY_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCANNER_ACTIVITY_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                Object object = data.getExtras().getSerializable("result");
                String rawResult = data.getExtras().getString("rawResult");
                this.scannerResult = rawResult != null ? rawResult : "";
                if (object instanceof Person) {
                    Person result = (Person) data.getExtras().getSerializable("result");
                    try {
                        ((EditText) findViewById(R.id.input_firstame)).setText(result.getFirstName());
                        ((EditText) findViewById(R.id.input_lastname)).setText(result.getLastName());
                        ((EditText) findViewById(R.id.input_document_number)).removeTextChangedListener(textWatcher);
                        ((EditText) findViewById(R.id.input_document_number)).setText(result.getDocumentNumber());
                        ((EditText) findViewById(R.id.input_document_number)).addTextChangedListener(textWatcher);
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        ((EditText) findViewById(R.id.input_birthdate)).setText(format.format(result.getBirthdate()));
                        ((SwitchMultiButton) findViewById(R.id.gender)).setSelectedTab(result.getPersonGenderId() - 1);
                    } catch (Exception e) {
                        new MaterialDialog.Builder(TeacherActivity.this)
                                .title("Error")
                                .content("Posible error al mostrar los datos escaneados. Por favor verifique si son correctos. Caso contrario intentelo nuevamente.")
                                .positiveText("Aceptar")
                                .onPositive((d, which) -> d.dismiss())
                                .show();
                    }
                } else {
                    new MaterialDialog.Builder(TeacherActivity.this)
                            .title("Error")
                            .content("Hubo un error al escanear los datos. Intentelo nuevamente.")
                            .positiveText("Aceptar")
                            .onPositive((d, which) -> d.dismiss())
                            .show();
                }
            }
        }
    }

    public void save(View view) {
        if ((dialog == null || !dialog.isShowing())) {
            dialog = ProgressDialog.show(this, "",
                    "Guardando", true);
        }

        TeacherData teacherData = new TeacherData();
        teacherData.setId(teacherId);
        teacherData.setFirstName(((EditText) findViewById(R.id.input_firstame)).getText().toString());
        teacherData.setLastName(((EditText) findViewById(R.id.input_lastname)).getText().toString());
        teacherData.setDocumentNumber(((EditText) findViewById(R.id.input_document_number)).getText().toString());

        int posGender = ((SwitchMultiButton) findViewById(R.id.gender)).getSelectedTab();
        if (posGender != -1) {
            PersonGenders personGenders = personGendersList.get(posGender);
            teacherData.setPersonGenderId(personGenders.getId());
        } else {
            teacherData.setPersonGenderId(posGender);
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String bd = ((EditText) findViewById(R.id.input_birthdate)).getText().toString();
            teacherData.setBirthdate(format.parse(bd));
        } catch (Exception e) {
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String bd = ((EditText) findViewById(R.id.input_hiredate)).getText().toString();
            teacherData.setHireDate(format.parse(bd));
        } catch (Exception e) {
        }

        teacherData.setCUIL(((EditText) findViewById(R.id.input_cuil_number)).getText().toString());

        teacherData.setPhoneNumber(((EditText) findViewById(R.id.input_phone_number)).getText().toString());
        teacherData.setMobilePhoneNumber(((EditText) findViewById(R.id.input_mobile_phone_number)).getText().toString());
        teacherData.setAddress(((EditText) findViewById(R.id.input_address)).getText().toString());

        int posTypeEmployee = ((SwitchMultiButton) findViewById(R.id.typeEmployee)).getSelectedTab();
        if (posTypeEmployee != -1) {
            TypeEmployee typeEmployee = typeEmployeesList.get(posTypeEmployee);
            teacherData.setTypeEmployeeId(typeEmployee.getId());
        } else {
            teacherData.setTypeEmployeeId(posTypeEmployee);
        }

        teacherData.setScannerResult(this.scannerResult);

        String result = validar(teacherData);
        if (result.isEmpty()) {
            mDisposable.add(teacherViewModel.saveTeacher(teacherData)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() ->
                            {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                new MaterialDialog.Builder(TeacherActivity.this)
                                        .title("Guardado Correcto")
                                        .content("Datos guardados correctamente")
                                        .canceledOnTouchOutside(false)
                                        .positiveText("Aceptar")
                                        .onPositive((d, which) -> {
                                            d.dismiss();
                                            TeacherActivity.this.finish();
                                        })
                                        .show();
                            },
                            throwable -> {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                new MaterialDialog.Builder(TeacherActivity.this)
                                        .title("Error")
                                        .content("Hubo un error al guardar los datos. Intente nuevamente. Si el error persiste contacte con el adminsitrador.")
                                        .positiveText("Aceptar")
                                        .onPositive((d, which) -> {
                                            d.dismiss();
                                        })
                                        .show();
                            }));
        } else {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            new MaterialDialog.Builder(TeacherActivity.this)
                    .title("Error")
                    .content(result)
                    .positiveText("Aceptar")
                    .onPositive((d, which) -> {
                        d.dismiss();
                    })
                    .show();
        }


    }

    private String validar(TeacherData teacherData) {
        if (teacherData.getFirstName().isEmpty()) {
            return "Debe ingresar nombre del profesor";
        }
        if (teacherData.getLastName().isEmpty()) {
            return "Debe ingresar apellido del profesor";
        }
        if (teacherData.getDocumentNumber().isEmpty()) {
            return "Debe ingresar número de documento del profesor";
        }
        if (teacherData.getPersonGenderId() == -1) {
            return "Debe ingresar sexo del profesor";
        }
        if (teacherData.getBirthdate() == null) {
            return "Debe ingresar fecha de nacimiento del profesor";
        }
        if (teacherData.getTypeEmployeeId() == -1) {
            return "Debe ingresar el carácter del cargo del profesor";
        }
        return "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // clear all the subscriptions
        mDisposable.clear();
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(TeacherActivity.this)
                .title("")
                .content("¿Salir sin guardar los cambios?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    d.dismiss();
                    finish();
                })
                .onNegative((d, which) -> {
                    d.dismiss();
                })
                .show();
    }
}
