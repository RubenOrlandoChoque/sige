package com.smartapps.sigev2.ui.newstudent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;
import com.microblink.entities.recognizers.blinkid.imageoptions.extension.FullDocumentImageExtensionEntityInterface;
import com.microblink.entities.recognizers.successframe.SuccessFrameGrabberRecognizer;
import com.microblink.hardware.camera.CameraType;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.BlinkIdUISettings;
import com.microblink.util.RecognizerCompatibility;
import com.microblink.util.RecognizerCompatibilityStatus;
import com.smartapps.sigev2.R;
import com.smartapps.sigev2.models.documenttypes.DocumentTypes;
import com.smartapps.sigev2.models.documenttypes.DocumentTypesViewModel;
import com.smartapps.sigev2.models.people.Person;
import com.smartapps.sigev2.models.people.PersonViewModel;
import com.smartapps.sigev2.models.persongenders.PersonGenders;
import com.smartapps.sigev2.models.persongenders.PersonGendersViewModel;
import com.smartapps.sigev2.models.relationship.RelationshipViewModel;
import com.smartapps.sigev2.models.relationship.pojo.RelationshipData;
import com.smartapps.sigev2.models.tutor.Tutor;
import com.smartapps.sigev2.models.tutor.TutorViewModel;
import com.smartapps.sigev2.models.tutor.pojo.TutorData;
import com.smartapps.sigev2.models.tutorStudents.pojo.TutorStudentRelationshipData;
import com.smartapps.sigev2.ui.scanner.SimpleScannerActivity;
import com.smartapps.sigev2.util.Evento;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lib.kingja.switchbutton.SwitchMultiButton;

import static android.widget.Toast.LENGTH_SHORT;
import static br.com.zbra.androidlinq.Linq.stream;
import static com.microblink.MicroblinkSDK.getApplicationContext;

public class TutorActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    public static final int SCANNER_ACTIVITY_RESULT = 3;
    private static final int SCANNER2_ACTIVITY_RESULT= 4;
    private PersonGendersViewModel personGendersViewModel;
    private DocumentTypesViewModel documentTypesViewModel;
    private TutorViewModel tutorViewModel;
    private RelationshipViewModel relationshipViewModel;
    private PersonViewModel personViewModel;
    List<RelationshipData> relationships = new ArrayList<>();
    List<PersonGenders> personGendersList;
    List<DocumentTypes> documentTypesList;

    String[] personGendersString;
    String[] documentTypesString;

    TextView dateTextView;
    ImageButton dateButton;
    ImageButton copyStudentAddressButton;
    //ImageButton copyStudentPhoneNumber;
    //ImageButton copyStudentMobilePhoneNumber;
    //ImageButton copyStudentAnotherPhoneNumber;
    SimpleDateFormat simpleDateFormat;

    String tutorId = "";
    String studentId = "";
    TextWatcher textWatcher;
    String scannerResult = "";
    String studentAddress = "";
    String studentLocation = "";
    String studentPhoneNumber = "";
    String studentMobilePhoneNumber = "";
    String studentAnotherPhoneNumber = "";

    //BlinkID Scanner
    private BlinkIdCombinedRecognizer mRecognizer;
    private RecognizerBundle mRecognizerBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);

        Intent intent = getIntent();
        if (intent != null) {
            tutorId = intent.getStringExtra("tutorId");
            studentId = intent.getStringExtra("studentId");
            studentAddress = intent.getStringExtra("studentAddress");
            studentLocation = intent.getStringExtra("studentLocation");
            studentPhoneNumber = intent.getStringExtra("studentPhoneNumber");
            studentMobilePhoneNumber = intent.getStringExtra("studentMobilePhoneNumber");
            studentAnotherPhoneNumber = intent.getStringExtra("studentAnotherPhoneNumber");
        }

        personGendersViewModel = ViewModelProviders.of(this).get(PersonGendersViewModel.class);
        documentTypesViewModel = ViewModelProviders.of(this).get(DocumentTypesViewModel.class);
        tutorViewModel = ViewModelProviders.of(this).get(TutorViewModel.class);
        relationshipViewModel = ViewModelProviders.of(this).get(RelationshipViewModel.class);
        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);

        ((EditText) findViewById(R.id.input_firstame)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ((EditText) findViewById(R.id.input_lastname)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        dateButton = findViewById(R.id.set_date_button);
        copyStudentAddressButton = findViewById(R.id.copy_student_address);
//        copyStudentPhoneNumber = findViewById(R.id.copy_student_phone_number);
//        copyStudentMobilePhoneNumber = findViewById(R.id.copy_student_mobile_phone_number);
//        copyStudentAnotherPhoneNumber = findViewById(R.id.copy_student_another_phone_number);
        dateTextView = findViewById(R.id.input_birthdate);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dateButton.setOnClickListener(view -> showDate(1980, 0, 1, R.style.DatePickerSpinner));
        copyStudentAddressButton.setOnClickListener(v -> copyStudentAddress());
//        copyStudentPhoneNumber.setOnClickListener(v -> copyStudentPhoneNumber());
//        copyStudentMobilePhoneNumber.setOnClickListener(v -> copyStudentMobilePhoneNumber());
//        copyStudentAnotherPhoneNumber.setOnClickListener(v -> copyStudentAnotherPhoneNumber());
        personGendersViewModel.getAll().observe(this, new Observer<List<PersonGenders>>() {
            @Override
            public void onChanged(@Nullable List<PersonGenders> personGenders) {
                if (personGenders.size() > 0) {
                    personGendersString = stream(personGenders).select(PersonGenders::getDescription).toList().toArray(new String[personGenders.size()]);
                    personGendersList = personGenders;
                    ((SwitchMultiButton) findViewById(R.id.gender)).setText(personGendersString);
                    ((SwitchMultiButton) findViewById(R.id.gender)).clearSelection();
                    loadRelationShip();
                } else {
                    finishLoadEvent("No se cargó correctamente el catalogo de sexos de persona.");
                }
            }
        });

        documentTypesViewModel.getAll().observe(this, new Observer<List<DocumentTypes>>() {
            @Override
            public void onChanged(@Nullable List<DocumentTypes> documentTypes) {
                if (documentTypes.size() > 0) {
                    documentTypesString = stream(documentTypes).select(DocumentTypes::getDescription).toList().toArray(new String[documentTypes.size()]);
                    documentTypesList = documentTypes;
                    ((SwitchMultiButton) findViewById(R.id.doc_type)).setText(documentTypesString);
                    ((SwitchMultiButton) findViewById(R.id.doc_type)).clearSelection();
                } else {
                    finishLoadEvent("No se cargó correctamente el catálogo de tipos de documento.");
                }
            }
        });

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; ++i)
                {
                    if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*").matcher(String.valueOf(source.charAt(i))).matches())
                    {
                        return "";
                    }
                }

                return null;
            }
        };

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

                            /*((EditText) findViewById(R.id.input_phone_number)).setText(person.getPhoneNumber());
                            ((EditText) findViewById(R.id.input_mobile_phone_number)).setText(person.getMobilePhoneNumber());
                            ((EditText) findViewById(R.id.input_another_phone_number)).setText(person.getAnotherContactPhone());*/
                            ((EditText) findViewById(R.id.input_address)).setText(person.getAddress());
                            ((EditText) findViewById(R.id.input_location)).setText(person.getLocation());
                            ((EditText) findViewById(R.id.input_nacionality)).setText(person.getNationality());

                            // buscar datos del tutor
                            searchTutorData(person.getId());

                            tutorId = person.getId();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        };

        ((SwitchMultiButton) findViewById(R.id.doc_type)).setOnSwitchListener((position, tabText) -> {
            EditText edit = findViewById(R.id.input_document_number);
            findViewById(R.id.personal_data_group).setVisibility(View.VISIBLE);
            if (position == 0) {
                edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                edit.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(8)});
            } else {
                edit.setInputType(InputType.TYPE_CLASS_TEXT);
                edit.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(15)});
            }

            edit.requestFocus();
            //TODO falta desplegar keyboard
            findViewById(R.id.tutorcontactdata).setVisibility(View.VISIBLE);
            findViewById(R.id.tutorotherdata).setVisibility(View.VISIBLE);
        });

        ((EditText) findViewById(R.id.input_document_number)).addTextChangedListener(textWatcher);
        findViewById(R.id.save_header_tutor).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.scan_student).setOnClickListener(this);

        loadAutocomplete();
        setBlink();
    }

    private void loadAutocomplete() {
        AutoCompleteTextView tvOcupations = findViewById(R.id.input_ocupation);
        AutoCompleteTextView tvLocationAddress = findViewById(R.id.input_location);

        String[] locations = getResources().getStringArray(R.array.location_array);
        String[] ocupations = getResources().getStringArray(R.array.ocupations_array);

        ArrayAdapter<String> adapterLocations =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, locations);
        tvLocationAddress.setAdapter(adapterLocations);

        ArrayAdapter<String> adapterOcupations =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, ocupations);
        tvOcupations.setAdapter(adapterOcupations);
    }

    private void copyStudentAddress() {
        new MaterialDialog.Builder(this)
                .title("Copiar")
                .content("¿Utilizar domicilio del alumno?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    try {
                        ((EditText) findViewById(R.id.input_address)).setText(studentAddress);
                        ((EditText) findViewById(R.id.input_location)).setText(studentLocation);
                    } catch (Exception e) {

                    }
                    d.dismiss();
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }

    private void copyStudentPhoneNumber() {
        new MaterialDialog.Builder(this)
                .title("Copiar")
                .content("¿Utilizar Nro. de Teléfono del alumno?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    try {
                        ((EditText) findViewById(R.id.input_phone_number)).setText(studentPhoneNumber);
                    } catch (Exception e) {

                    }
                    d.dismiss();
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }

    private void copyStudentMobilePhoneNumber() {
        new MaterialDialog.Builder(this)
                .title("Copiar")
                .content("¿Utilizar Nro. de Celular del alumno?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    try {
                        ((EditText) findViewById(R.id.input_mobile_phone_number)).setText(studentMobilePhoneNumber);
                    } catch (Exception e) {

                    }
                    d.dismiss();
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }

    private void copyStudentAnotherPhoneNumber() {
        new MaterialDialog.Builder(this)
                .title("Copiar")
                .content("¿Utilizar Otro Teléfono de contácto del alumno?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    try {
                        ((EditText) findViewById(R.id.input_another_phone_number)).setText(studentAnotherPhoneNumber);
                    } catch (Exception e) {

                    }
                    d.dismiss();
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }

    private void loadRelationShip() {
        relationshipViewModel.getAll().observe(this, new Observer<List<RelationshipData>>() {
            @Override
            public void onChanged(@Nullable List<RelationshipData> relationships) {
                if (relationships != null && relationships.size() > 0) {
                    ((SwitchMultiButton) findViewById(R.id.relationship_switch)).setText("Madre", "Padre", "Tutor");
                    Spinner spinner2 = findViewById(R.id.relationship_select);
                    RelationshipData r = new RelationshipData();
                    r.setDescription("Seleccione una opcion");
                    r.setId(-1);
                    relationships.add(0, r);
                    ArrayAdapter<RelationshipData> dataAdapter = new ArrayAdapter<>(TutorActivity.this,
                            android.R.layout.simple_spinner_item, relationships);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(dataAdapter);
                    TutorActivity.this.relationships = relationships;

                    loadData();
                } else {
                    finishLoadEvent("No se cargó correctamente el catalogo de parentescos.");
                }
            }
        });
    }

    private void loadData() {
        // load data
        if (tutorId != null && !tutorId.isEmpty()) { //buscar profesor
            tutorViewModel.getTutorDataTemp(tutorId, studentId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<TutorData>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(TutorData tutorData) {
                    // success

                    DocumentTypes dt = stream(documentTypesList).where(e -> e.getId() == tutorData.getDocumentTypeId()).firstOrNull();
                    int pos = 0;
                    if (dt != null) {
                        pos = documentTypesList.indexOf(dt);
                    }
                    ((SwitchMultiButton) findViewById(R.id.doc_type)).setSelectedTab(pos);

                    ((EditText) findViewById(R.id.input_firstame)).setText(tutorData.getFirstName());
                    ((EditText) findViewById(R.id.input_lastname)).setText(tutorData.getLastName());
                    ((EditText) findViewById(R.id.input_document_number)).removeTextChangedListener(textWatcher);
                    ((EditText) findViewById(R.id.input_document_number)).setText(tutorData.getDocumentNumber());
                    ((EditText) findViewById(R.id.input_document_number)).addTextChangedListener(textWatcher);
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    if (tutorData.getBirthdate() != null) {
                        ((EditText) findViewById(R.id.input_birthdate)).setText(format.format(tutorData.getBirthdate()));
                    }
                    PersonGenders pg = stream(personGendersList).where(e -> e.getId() == tutorData.getPersonGenderId()).firstOrNull();
                    pos = 0;
                    if (pg != null) {
                        pos = personGendersList.indexOf(pg);
                    }
                    ((SwitchMultiButton) findViewById(R.id.gender)).setSelectedTab(pos);

                    /*((EditText) findViewById(R.id.input_phone_number)).setText(tutorData.getPhoneNumber());
                    ((EditText) findViewById(R.id.input_mobile_phone_number)).setText(tutorData.getMobilePhoneNumber());
                    ((EditText) findViewById(R.id.input_another_phone_number)).setText(tutorData.getAnotherContactPhone());*/
                    ((EditText) findViewById(R.id.input_address)).setText(tutorData.getAddress());
                    ((EditText) findViewById(R.id.input_location)).setText(tutorData.getLocation());
                    ((EditText) findViewById(R.id.input_nacionality)).setText(tutorData.getNationality());

                    ((EditText) findViewById(R.id.input_ocupation)).setText(tutorData.getOcupation());

                    RelationshipData rd = stream(TutorActivity.this.relationships).where(e -> e.getId() == tutorData.getRelationshipId()).firstOrNull();
                    pos = 0;
                    if (rd != null) {
                        pos = TutorActivity.this.relationships.indexOf(rd);
                    }
                    ((Spinner) findViewById(R.id.relationship_select)).setSelection(pos);
                    finishLoadEvent("");
                }

                @Override
                public void onError(Throwable e) {
                    finishLoadEvent("Ocurrió un error al cargar el tutor");
                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            finishLoadEvent("");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_header_tutor:
                save();
                break;
            case R.id.btn_back:
                onBack();
                break;
            case R.id.scan_student: {

                // check if BlinkID is supported on the device,
                if(blinkIdSupported()) {
                    startScanning();
                } else {
                    callScanner();
                }
                break;
            }
        }
    }

    private boolean blinkIdSupported() {
        boolean result;
        RecognizerCompatibilityStatus status = RecognizerCompatibility.getRecognizerCompatibilityStatus(getApplicationContext());

        if (status == RecognizerCompatibilityStatus.RECOGNIZER_SUPPORTED) {
            result = true; //Toast.makeText(getApplicationContext(), "BlinkID is supported!", Toast.LENGTH_LONG).show();
        } else if (status == RecognizerCompatibilityStatus.NO_CAMERA) {
            result =  false; //Toast.makeText(getApplicationContext(), "BlinkID is supported only via Direct API!", Toast.LENGTH_LONG).show();
        } else if (status == RecognizerCompatibilityStatus.PROCESSOR_ARCHITECTURE_NOT_SUPPORTED) {
            result =  false; //Toast.makeText(getApplicationContext(), "BlinkID is not supported on current processor architecture!", Toast.LENGTH_LONG).show();
        } else {
            result =  false;//Toast.makeText(getApplicationContext(), "BlinkID is not supported! Reason: " + status.name(), Toast.LENGTH_LONG).show();
        }
        //Recognizer[] recArray = ...;
        if(!RecognizerCompatibility.cameraHasAutofocus(CameraType.CAMERA_BACKFACE, getApplicationContext())) {
            result =  false;
        }
        return result;
    }

    private void setBlink() {
        // create BlinkIdCombinedRecognizer
        mRecognizer = new BlinkIdCombinedRecognizer();

        // set to true to obtain images containing full document
        mRecognizer.setReturnFullDocumentImage(true);
        mRecognizer.setReturnFaceImage(true);

        // wrap recognizer in SuccessFrameGrabberRecognizer to obtain successful frames (full last frame on which scan has succeeded)
        SuccessFrameGrabberRecognizer successFrameGrabberRecognizer = new SuccessFrameGrabberRecognizer(mRecognizer);

        // bundle recognizers into RecognizerBundle
        mRecognizerBundle = new RecognizerBundle(successFrameGrabberRecognizer);
    }

    // method within MyActivity from previous step
    public void startScanning() {
        // Settings for BlinkIdActivity
        try{
            BlinkIdUISettings settings = new BlinkIdUISettings(mRecognizerBundle);

            //enable capturing success frame in full camera resolution
            settings.enableHighResSuccessFrameCapture(true);

            // Start activity
            ActivityRunner.startActivityForResult(this, SCANNER2_ACTIVITY_RESULT, settings);
            //startActivityForResult(this, SCANNER2_ACITIVTY_RESULT, settings);
        } catch(Exception ex) {
            Toast.makeText(this, "Error StudentFragment= " + ex.getMessage(), LENGTH_SHORT).show();
        }

    }

    private void onBack() {
        new MaterialDialog.Builder(this)
                .title("Info")
                .content("¿Desea salir sin guardar los cambios?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    d.dismiss();
                    finish();
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void save() {
        TutorData tutorData = getData();
        String result = validar(tutorData);
        if (result.isEmpty()) {
            tutorViewModel.saveTempTutor(tutorData, studentId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<String>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(String s) {
                    finish();
                }

                @Override
                public void onError(Throwable e) {

                }
            });
        } else {
            new MaterialDialog.Builder(this)
                    .title("Error")
                    .content(result)
                    .positiveText("Aceptar")
                    .onPositive((d, which) -> d.dismiss())
                    .show();
        }
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

                        // buscar la persona y traer los datos no escaneados por el dni (Siempre tomar los datos del dni excepto los que no se escanean)
                        personViewModel.getByDni(result.getDocumentNumber()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Person>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Person person) {
                                if (person != null) {
                                    ((EditText) findViewById(R.id.input_phone_number)).setText(person.getPhoneNumber());
                                    ((EditText) findViewById(R.id.input_mobile_phone_number)).setText(person.getMobilePhoneNumber());
                                    ((EditText) findViewById(R.id.input_another_phone_number)).setText(person.getAnotherContactPhone());
                                    ((EditText) findViewById(R.id.input_address)).setText(person.getAddress());
                                    ((EditText) findViewById(R.id.input_location)).setText(person.getLocation());
                                    ((EditText) findViewById(R.id.input_nacionality)).setText(person.getNationality());

                                    // buscar datos del tutor
                                    searchTutorData(person.getId());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });

                    } catch (Exception e) {
                        new MaterialDialog.Builder(this)
                                .title("Error")
                                .content("Posible error al mostrar los datos escaneados. Por favor verifique si son correctos. Caso contrario intentelo nuevamente.")
                                .positiveText("Aceptar")
                                .onPositive((d, which) -> d.dismiss())
                                .show();
                    }
                } else {
                    new MaterialDialog.Builder(this)
                            .title("Error")
                            .content("Hubo un error al escanear los datos. Intentelo nuevamente.")
                            .positiveText("Aceptar")
                            .onPositive((d, which) -> d.dismiss())
                            .show();
                }
            }
        } else {
            if (requestCode == SCANNER2_ACTIVITY_RESULT) {
                escanearDNI(data);
            }
        }
    }

    private void escanearDNI(Intent data) {
        // updates bundled recognizers with results that have arrived
        mRecognizerBundle.loadFromIntent(data);
        //handleScanResult();
        //mRecognizerBundle.loadFromIntent(data);
        Recognizer firstRecognizer = mRecognizerBundle.getRecognizers()[0];
        SuccessFrameGrabberRecognizer successFrameGrabberRecognizer = (SuccessFrameGrabberRecognizer) firstRecognizer;
        //get wrapped recognizer
        BlinkIdCombinedRecognizer blinkIdRecognizer = (BlinkIdCombinedRecognizer) successFrameGrabberRecognizer.getSlaveRecognizer();

        // now every recognizer object that was bundled within RecognizerBundle
        // has been updated with results obtained during scanning session
        // you can get the result by invoking getResult on recognizer
        BlinkIdCombinedRecognizer.Result result = mRecognizer.getResult();

        try {
            if (!result.getClassInfo().isEmpty())  {
                if (result.getClassInfo().getCountry().name().equals("ARGENTINA")) {
                    ((SwitchMultiButton) findViewById(R.id.doc_type)).setSelectedTab(0);
                } else {
                    ((SwitchMultiButton) findViewById(R.id.doc_type)).setSelectedTab(1);
                }
            }
            ((EditText) findViewById(R.id.input_firstame)).setText(result.getFirstName());
            ((EditText) findViewById(R.id.input_lastname)).setText(result.getLastName());
            ((EditText) findViewById(R.id.input_document_number)).removeTextChangedListener(textWatcher);
            ((EditText) findViewById(R.id.input_document_number)).setText(result.getDocumentNumber());
            ((EditText) findViewById(R.id.input_document_number)).addTextChangedListener(textWatcher);

            //Fecha de nacimiento
            String fecha = result.getDateOfBirth().getOriginalDateString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = sdf.parse(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFinal = sdf.format(date);
            ((EditText) findViewById(R.id.input_birthdate)).setText(fecha);
            //*Fecha de nacimiento

            //Sexo
            int sex = -1;
            if (result.getSex().equals("M")) {
                sex = 0;
            } else if (result.getSex().equals("F")) {
                sex = 1;
            } else {
                sex = 2;
            }
            ((SwitchMultiButton) findViewById(R.id.gender)).setSelectedTab(sex);
            //*Sexo

            // buscar la persona y traer los datos no escaneados por el dni (Siempre tomar los datos del dni excepto los que no se escanean)
            personViewModel.getByDni(result.getDocumentNumber()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Person>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(Person person) {
                    if (person != null) {
                        /*((EditText) findViewById(R.id.input_phone_number)).setText(person.getPhoneNumber());
                        ((EditText) findViewById(R.id.input_mobile_phone_number)).setText(person.getMobilePhoneNumber());
                        ((EditText) findViewById(R.id.input_another_phone_number)).setText(person.getAnotherContactPhone());*/
                        ((EditText) findViewById(R.id.input_address)).setText(person.getAddress());
                        ((EditText) findViewById(R.id.input_location)).setText(person.getLocation());
                        ((EditText) findViewById(R.id.input_nacionality)).setText(person.getNationality());

                        // buscar datos del tutor
                        searchTutorData(person.getId());
                    }
                }

                @Override
                public void onError(Throwable e) {

                }
            });

        } catch (Exception e) {
            new MaterialDialog.Builder(this)
                    .title("Error")
                    .content("Posible error al mostrar los datos escaneados. Por favor verifique si son correctos. Caso contrario intentelo nuevamente.")
                    .positiveText("Aceptar")
                    .onPositive((d, which) -> d.dismiss())
                    .show();
        }

    }

    private void searchTutorData(String tutorId) {
        tutorViewModel.getTutor(tutorId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<Tutor>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Tutor tutorData) {
                // success
                ((EditText) findViewById(R.id.input_ocupation)).setText(tutorData.getOcupation());
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        });

        tutorViewModel.getTutorStudentRelationship(tutorId, studentId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<TutorStudentRelationshipData>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(TutorStudentRelationshipData tutorStudentRelationshipData) {
                // success
                RelationshipData rd = stream(TutorActivity.this.relationships).where(e -> e.getId() == tutorStudentRelationshipData.RelationshipId).firstOrNull();
                int pos = 0;
                if (rd != null) {
                    pos = TutorActivity.this.relationships.indexOf(rd);
                }
                ((Spinner) findViewById(R.id.relationship_select)).setSelection(pos);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        dateTextView.setText(simpleDateFormat.format(calendar.getTime()));
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
                .context(this)
                .callback(TutorActivity.this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(Integer.parseInt(DateFormat.format("yyyy", date).toString()), Integer.parseInt(DateFormat.format("M", date).toString()) - 1, Integer.parseInt(DateFormat.format("d", date).toString()))
                .build()
                .show();
    }

    public void finishLoadEvent(String event) {
        EventBus.getDefault().post(new Evento("finishLoadData", event));
    }

    public void callScanner() {
        Intent i = new Intent(this, SimpleScannerActivity.class);
        startActivityForResult(i, SCANNER_ACTIVITY_RESULT);
    }

    private String validar(TutorData tutorData) {
        if (tutorData.getFirstName().isEmpty()) {
            return "Debe ingresar nombre del tutor";
        }
        if (tutorData.getLastName().isEmpty()) {
            return "Debe ingresar apellido del tutor";
        }
        if (tutorData.getDocumentNumber().isEmpty()) {
            return "Debe ingresar número de documento del tutor";
        }
        if (tutorData.getPersonGenderId() == -1) {
            return "Debe ingresar sexo del tutor";
        }
        if (tutorData.getBirthdate() == null) {
            return "Debe ingresar fecha de nacimiento del tutor";
        }

        if (tutorData.getRelationshipId() == -1) {
            return "Debe ingresar el parentesco del tutor";
        }

        return "";
    }

    private TutorData getData() {
        TutorData tutorData = new TutorData();
        tutorData.setId(tutorId);
        tutorData.setFirstName(((EditText) findViewById(R.id.input_firstame)).getText().toString());
        tutorData.setLastName(((EditText) findViewById(R.id.input_lastname)).getText().toString());
        tutorData.setDocumentNumber(((EditText) findViewById(R.id.input_document_number)).getText().toString());

        int posDocType = ((SwitchMultiButton) findViewById(R.id.doc_type)).getSelectedTab();
        if (posDocType != -1) {
            DocumentTypes docType = documentTypesList.get(posDocType);
            tutorData.setDocumentTypeId(docType.getId());
        } else {
            tutorData.setPersonGenderId(posDocType);
        }

        int posGender = ((SwitchMultiButton) findViewById(R.id.gender)).getSelectedTab();
        if (posGender != -1) {
            PersonGenders personGenders = personGendersList.get(posGender);
            tutorData.setPersonGenderId(personGenders.getId());
        } else {
            tutorData.setPersonGenderId(posGender);
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String bd = ((EditText) findViewById(R.id.input_birthdate)).getText().toString();
            tutorData.setBirthdate(format.parse(bd));
        } catch (Exception ignored) {
        }

        //tutorData.setPhoneNumber(((EditText) findViewById(R.id.input_phone_number)).getText().toString());
        //tutorData.setMobilePhoneNumber(((EditText) findViewById(R.id.input_mobile_phone_number)).getText().toString());
        //tutorData.setAnotherContactPhone(((EditText) findViewById(R.id.input_another_phone_number)).getText().toString());
        tutorData.setAddress(((EditText) findViewById(R.id.input_address)).getText().toString());
        tutorData.setLocation(((EditText) findViewById(R.id.input_location)).getText().toString());
        tutorData.setNationality(((EditText) findViewById(R.id.input_nacionality)).getText().toString());
        tutorData.setOcupation(((EditText) findViewById(R.id.input_ocupation)).getText().toString());

        RelationshipData relationshipData = (RelationshipData) ((Spinner) findViewById(R.id.relationship_select)).getSelectedItem();
        tutorData.setRelationshipId(relationshipData.getId());

        tutorData.setScannerResult(this.scannerResult);

        return tutorData;
    }


    public void disableComponents() {
        studentId = null;
        ((EditText) findViewById(R.id.input_firstame)).setEnabled(false);
        ((EditText) findViewById(R.id.input_lastname)).setEnabled(false);
        ((EditText) findViewById(R.id.input_document_number)).setEnabled(false);
        ((SwitchMultiButton) findViewById(R.id.gender)).setEnabled(false);
        ((EditText) findViewById(R.id.input_birthdate)).setEnabled(false);
        ((EditText) findViewById(R.id.input_phone_number)).setEnabled(false);
        ((EditText) findViewById(R.id.input_mobile_phone_number)).setEnabled(false);
        ((EditText) findViewById(R.id.input_another_phone_number)).setEnabled(false);
        ((EditText) findViewById(R.id.input_address)).setEnabled(false);
        ((EditText) findViewById(R.id.input_location)).setEnabled(false);
        ((EditText) findViewById(R.id.input_nacionality)).setEnabled(false);
        ;
        ((SwitchMultiButton) findViewById(R.id.curso_switch)).setEnabled(false);
    }
}
