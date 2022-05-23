package com.smartapps.sigev2.ui.newstudent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.Config;
import com.smartapps.sigev2.classes.NonSwipeableViewPager;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.models.courseyear.CourseYearViewModel;
import com.smartapps.sigev2.models.courseyear.pojo.CourseYearAndAllDivisions;
import com.smartapps.sigev2.models.division.Division;
import com.smartapps.sigev2.models.documentations.Documentation;
import com.smartapps.sigev2.models.documentations.DocumentationViewModel;
import com.smartapps.sigev2.models.documenttypes.DocumentTypes;
import com.smartapps.sigev2.models.documenttypes.DocumentTypesViewModel;
import com.smartapps.sigev2.models.educationlevels.EducationLevels;
import com.smartapps.sigev2.models.educationlevels.EducationLevelsViewModel;
import com.smartapps.sigev2.models.educationlevels.pojo.EducationLevelData;
import com.smartapps.sigev2.models.people.Person;
import com.smartapps.sigev2.models.people.PersonViewModel;
import com.smartapps.sigev2.models.persongenders.PersonGenders;
import com.smartapps.sigev2.models.persongenders.PersonGendersViewModel;
import com.smartapps.sigev2.models.relationship.RelationshipViewModel;
import com.smartapps.sigev2.models.relationship.pojo.RelationshipData;
import com.smartapps.sigev2.models.studentconditions.StudentConditions;
import com.smartapps.sigev2.models.studentconditions.StudentConditionsViewModel;
import com.smartapps.sigev2.models.studentdocumentations.StudentDocumentationViewModel;
import com.smartapps.sigev2.models.students.StudentViewModel;
import com.smartapps.sigev2.models.students.pojo.StudentData;
import com.smartapps.sigev2.ui.MainActivity;
import com.smartapps.sigev2.ui.fullscrenpreview.FullScreenImageActivity;
import com.smartapps.sigev2.ui.multimedia.CustomCameraActivity;
import com.smartapps.sigev2.ui.scanner.SimpleScannerActivity;
import com.smartapps.sigev2.util.Evento;
import com.smartapps.sigev2.util.Util;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;
import com.microblink.entities.recognizers.successframe.SuccessFrameGrabberRecognizer;
import com.microblink.hardware.camera.CameraType;
import com.microblink.image.Image;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.BlinkIdUISettings;
import com.microblink.util.RecognizerCompatibility;
import com.microblink.util.RecognizerCompatibilityStatus;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static br.com.zbra.androidlinq.Linq.stream;
import static com.microblink.MicroblinkSDK.getApplicationContext;
import static com.smartapps.sigev2.App.context;
import static com.smartapps.sigev2.util.Util.hideKeyboard;

public class StudentFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final int SCANNER_ACTIVITY_RESULT = 2;
    private static final int SCANNER2_ACTIVITY_RESULT= 3;
    private static final int CUSTOM_CAMERA_REQUEST_CODE_ANVERSO = 1000;
    private static final int CUSTOM_CAMERA_REQUEST_CODE_REVERSO = 1001;
    private StudentViewModel studentViewModel;
    private CourseYearViewModel courseYearViewModel;
    private PersonGendersViewModel personGendersViewModel;
    private DocumentTypesViewModel documentTypesViewModel;
    private StudentDocumentationViewModel studentDocumentationViewModel;
    private StudentConditionsViewModel studentConditionsViewModel;
    private EducationLevelsViewModel educationLevelsViewModel;
    private PersonViewModel personViewModel;
    private DocumentationViewModel documentationViewModel;
    TextWatcher textWatcher;

    List<CustomCourseYear> courseYearsList;
    List<Division> divisionsList;
    List<PersonGenders> personGendersList;
    List<DocumentTypes> documentTypesList;
    List<StudentConditions> studentConditionsList;
    List<EducationLevelData> educationLevelsList;
    String[] courseYearString;
    String[] divisionsString;
    String[] personGendersString;
    String[] documentTypesString;
    String[] studentConditionsString;
    TextView dateTextView;
    ImageButton dateButton;
    SimpleDateFormat simpleDateFormat;
    View rootView;
    int courseYearId = 0;
    String idImgProfile = "";
    String idCardFront = "";
    String idCardBack = "";
    String scannerResult = "";
    ViewGroup container = null;

    ImageButton copyCountryBirthButton;
    ImageButton copyProvinceBirthButton;
    ImageButton copyDepartmentBirthButton;
    ImageButton copyLocationBirthButton;
    ImageButton cleanStudentAddressButton;

    //BlinkID Scanner
    private BlinkIdCombinedRecognizer mRecognizer;
    private RecognizerBundle mRecognizerBundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // bind wiews models
        this.container = container;

        courseYearViewModel = ViewModelProviders.of(this).get(CourseYearViewModel.class);
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        personGendersViewModel = ViewModelProviders.of(this).get(PersonGendersViewModel.class);
        documentTypesViewModel = ViewModelProviders.of(this).get(DocumentTypesViewModel.class);
        documentationViewModel = ViewModelProviders.of(this).get(DocumentationViewModel.class);
        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        studentDocumentationViewModel = ViewModelProviders.of(this).get(StudentDocumentationViewModel.class);
        studentConditionsViewModel = ViewModelProviders.of(this).get(StudentConditionsViewModel.class);
        educationLevelsViewModel = ViewModelProviders.of(this).get(EducationLevelsViewModel.class);

        rootView = inflater.inflate(R.layout.fragment_student, container, false);
        ((EditText) rootView.findViewById(R.id.input_lastname)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ((EditText) rootView.findViewById(R.id.input_firstame)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        rootView.findViewById(R.id.input_layout_age).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.input_layout_another_phone_number).setVisibility(View.VISIBLE);
        dateButton = rootView.findViewById(R.id.set_date_button);
        dateTextView = rootView.findViewById(R.id.input_birthdate);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dateButton.setOnClickListener(view -> showDate(1980, 0, 1, R.style.DatePickerSpinner));

        rootView.findViewById(R.id.input_layout_belongs_ethnic_group_observation).setVisibility(View.GONE);
        rootView.findViewById(R.id.input_layout_has_health_problems_observation).setVisibility(View.GONE);
        rootView.findViewById(R.id.input_layout_has_legal_problems_observation).setVisibility(View.GONE);

        ((SwitchMultiButton) rootView.findViewById(R.id.condition_switch)).clearSelection();
        ((SwitchMultiButton) rootView.findViewById(R.id.has_brother_switch)).clearSelection();
        ((SwitchMultiButton) rootView.findViewById(R.id.belongs_ethnic_group)).clearSelection();
        ((SwitchMultiButton) rootView.findViewById(R.id.has_health_problems)).clearSelection();
        ((SwitchMultiButton) rootView.findViewById(R.id.has_legal_problems)).clearSelection();

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

        ((SwitchMultiButton) rootView.findViewById(R.id.condition_switch)).setOnSwitchListener((position, tabText) -> {
            rootView.findViewById(R.id.peopledata).setVisibility(View.VISIBLE);
        });

        ((SwitchMultiButton) rootView.findViewById(R.id.doc_type)).setOnSwitchListener((position, tabText) -> {
                EditText edit = rootView.findViewById(R.id.input_document_number);
                rootView.findViewById(R.id.personal_data_group).setVisibility(View.VISIBLE);
                if (position == 0) {
                    edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                    edit.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(8)});
                } else {
                    edit.setInputType(InputType.TYPE_CLASS_TEXT);
                    edit.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(15)});
                }

                edit.requestFocus();
                //TODO falta desplegar keyboard
                rootView.findViewById(R.id.placebirth).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.contactdata).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.otherdata).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.coursedata).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.docdata).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.photodata).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.paymentdata).setVisibility(View.VISIBLE);
        });

        ((SwitchMultiButton) rootView.findViewById(R.id.belongs_ethnic_group)).setOnSwitchListener((position, tabText) -> {
            if (position == 0) {
                rootView.findViewById(R.id.input_layout_belongs_ethnic_group_observation).setVisibility(View.VISIBLE);
            } else {
                rootView.findViewById(R.id.input_layout_belongs_ethnic_group_observation).setVisibility(View.GONE);
            }
        });

        ((SwitchMultiButton) rootView.findViewById(R.id.has_health_problems)).setOnSwitchListener((position, tabText) -> {
            if (position == 0) {
                rootView.findViewById(R.id.input_layout_has_health_problems_observation).setVisibility(View.VISIBLE);
            } else {
                rootView.findViewById(R.id.input_layout_has_health_problems_observation).setVisibility(View.GONE);
            }
        });

        ((SwitchMultiButton) rootView.findViewById(R.id.has_legal_problems)).setOnSwitchListener((position, tabText) -> {
            if (position == 0) {
                rootView.findViewById(R.id.input_layout_has_legal_problems_observation).setVisibility(View.VISIBLE);
            } else {
                rootView.findViewById(R.id.input_layout_has_legal_problems_observation).setVisibility(View.GONE);
            }
        });

        loadDocumentations();

        setBlink();

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

                            DocumentTypes dt = stream(documentTypesList).where(e -> e.getId() == person.getDocumentTypeId()).firstOrNull();
                            int pos = 0;
                            if (dt != null) {
                                pos = documentTypesList.indexOf(dt);
                            }
                            ((SwitchMultiButton) rootView.findViewById(R.id.doc_type)).setSelectedTab(pos);

                            ((EditText) rootView.findViewById(R.id.input_firstame)).setText(person.getFirstName());
                            ((EditText) rootView.findViewById(R.id.input_lastname)).setText(person.getLastName());
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            if (person.getBirthdate() != null) {
                                ((EditText) rootView.findViewById(R.id.input_birthdate)).setText(format.format(person.getBirthdate()));
                            }
                            PersonGenders pg = stream(personGendersList).where(e -> e.getId() == person.getPersonGenderId()).firstOrNull();
                            pos = 0;
                            if (pg != null) {
                                pos = personGendersList.indexOf(pg);
                            }
                            ((SwitchMultiButton) rootView.findViewById(R.id.gender)).setSelectedTab(pos);

                            ((EditText) rootView.findViewById(R.id.input_phone_number)).setText(person.getPhoneNumber());
                            ((EditText) rootView.findViewById(R.id.input_mobile_phone_number)).setText(person.getMobilePhoneNumber());
                            ((EditText) rootView.findViewById(R.id.input_another_phone_number)).setText(person.getAnotherContactPhone());
                            ((EditText) rootView.findViewById(R.id.input_address)).setText(person.getAddress());
                            ((EditText) rootView.findViewById(R.id.input_location)).setText(person.getLocation());
                            ((EditText) rootView.findViewById(R.id.input_nacionality)).setText(person.getNationality());

                            int beg = person.isBelongsEthnicGroup() ? 0 : 1;
                            ((SwitchMultiButton) rootView.findViewById(R.id.belongs_ethnic_group)).setSelectedTab(beg);

                            int hhp = person.isHasHealthProblems() ? 0 : 1;
                            ((SwitchMultiButton) rootView.findViewById(R.id.has_health_problems)).setSelectedTab(hhp);

                            int hlp = person.isHasLegalProblems() ? 0 : 1;
                            ((SwitchMultiButton) rootView.findViewById(R.id.has_legal_problems)).setSelectedTab(hlp);


                            if (person.isBelongsEthnicGroup()) {
                                ((EditText) rootView.findViewById(R.id.input_belongs_ethnic_group_observation)).setText(person.getEthnicName());
                            }
                            if (person.isHasHealthProblems()) {
                                ((EditText) rootView.findViewById(R.id.input_has_health_problems_observation)).setText(person.getDescriptionHealthProblems());
                            }
                            if (person.isHasLegalProblems()) {
                                ((EditText) rootView.findViewById(R.id.input_has_legal_problems_observation)).setText(person.getDescriptionLegalProblems());
                            }
                            // buscar datos como alumno
                            searchStudentData(person.getId());
                            // se encontro un nuevo alumno,
                            // se debe modificar el studentId global y en la base de datos temporal
                            updateTempData(person.getId());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        };

        ((EditText) rootView.findViewById(R.id.input_document_number)).addTextChangedListener(textWatcher);

        rootView.findViewById(R.id.img_add_anverso_dni).setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), CustomCameraActivity.class);
            i.putExtra("directorioFoto", Config.DIRECTORIO_FOTOS);
            Config.BANDERA_FOTO = true; // Indica que se está sacando una foto.
            startActivityForResult(i, CUSTOM_CAMERA_REQUEST_CODE_ANVERSO);
        });

        rootView.findViewById(R.id.img_add_reverso_dni).setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), CustomCameraActivity.class);
            i.putExtra("directorioFoto", Config.DIRECTORIO_FOTOS);
            Config.BANDERA_FOTO = true; // Indica que se está sacando una foto.
            startActivityForResult(i, CUSTOM_CAMERA_REQUEST_CODE_REVERSO);
        });

        rootView.findViewById(R.id.img_anverso_dni).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);
            if (!idCardFront.isEmpty()) {
                intent.putExtra("path", idCardFront);
                startActivity(intent);
            }
        });

        rootView.findViewById(R.id.img_reverso_dni).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);
            if (!idCardBack.isEmpty()) {
                intent.putExtra("path", idCardBack);
                startActivity(intent);
            }
        });

        rootView.findViewById(R.id.img_profile).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);
            if (!idImgProfile.isEmpty()) {
                intent.putExtra("path", idImgProfile);
                startActivity(intent);
            }
        });

        rootView.findViewById(R.id.scan_student).setOnClickListener(this);

        loadAutocomplete();
        loadEducationLevels();

        /*TextView tv1 = rootView.findViewById(R.id.input_phone_number);
        Spinner spinner1 = rootView.findViewById(R.id.phone_type1_select);
        tv1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    //hideKeyboard();
                    hideKeyboard(tv1);
                    textView.clearFocus();
                    spinner1.requestFocus();
                    spinner1.performClick();
                }
                return true;
            }
        });*/

        copyCountryBirthButton = rootView.findViewById(R.id.copy_country_birth);
        copyProvinceBirthButton = rootView.findViewById(R.id.copy_province_birth);
        copyDepartmentBirthButton = rootView.findViewById(R.id.copy_department_birth);
        copyLocationBirthButton = rootView.findViewById(R.id.copy_location_birth);
        cleanStudentAddressButton = rootView.findViewById(R.id.clean_student_address);
        copyCountryBirthButton.setOnClickListener(v -> copyCountryBirth());
        copyProvinceBirthButton.setOnClickListener(v -> copyProvinceBirth());
        copyDepartmentBirthButton.setOnClickListener(v -> copyDepartmentBirth());
        copyLocationBirthButton.setOnClickListener(v -> copyLocationBirth());
        cleanStudentAddressButton.setOnClickListener(v -> cleanStudentAddress());

        TextView tvMother = (TextView)rootView.findViewById(R.id.lbl_highest_educ_mother);
        TextView tvFather = (TextView)rootView.findViewById(R.id.lbl_highest_educ_father);
        tvMother.setText(Html.fromHtml("Máximo nivel educativo alcanzado de la <b><font color='red'>MADRE</font></b> o tutor"));
        tvFather.setText(Html.fromHtml("Máximo nivel educativo alcanzado del <b><font color='red'>PADRE</font></b> o tutor"));

        return rootView;
    }

    private void loadAutocomplete() {
        AutoCompleteTextView tvCountries = rootView.findViewById(R.id.input_country_birth);
        AutoCompleteTextView tvProvinces = rootView.findViewById(R.id.input_province_birth);
        AutoCompleteTextView tvDepartments = rootView.findViewById(R.id.input_department_birth);
        AutoCompleteTextView tvLocationsBirth = rootView.findViewById(R.id.input_location_birth);
        AutoCompleteTextView tvLocationAddress = rootView.findViewById(R.id.input_location);
        AutoCompleteTextView tvPhoneNumber1 = rootView.findViewById(R.id.input_belonging_phone_1);
        AutoCompleteTextView tvPhoneNumber2 = rootView.findViewById(R.id.input_belonging_phone_2);
        AutoCompleteTextView tvPhoneNumber3 = rootView.findViewById(R.id.input_belonging_phone_3);
        AutoCompleteTextView tvSchoolOrigin = rootView.findViewById(R.id.input_school_origin);

        String[] countries = getResources().getStringArray(R.array.contries_array);
        String[] provinces = getResources().getStringArray(R.array.provinces_array);
        String[] departments = getResources().getStringArray(R.array.departments_array);
        String[] belongingPhone = getResources().getStringArray(R.array.belonging_array);
        String[] locations = getResources().getStringArray(R.array.location_array);
        String[] schools = getResources().getStringArray(R.array.schools_array);

        ArrayAdapter<String> adapterCountries =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, countries);
        ArrayAdapter<String> adapterProvinces =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, provinces);
        ArrayAdapter<String> adapterDepartments =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, departments);
        ArrayAdapter<String> adapterLocations =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, locations);
        ArrayAdapter<String> adapterBelonging =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, belongingPhone);
        ArrayAdapter<String> adapterSchools =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, schools);

        tvCountries.setAdapter(adapterCountries);
        tvProvinces.setAdapter(adapterProvinces);
        tvDepartments.setAdapter(adapterDepartments);
        tvPhoneNumber1.setAdapter(adapterBelonging);
        tvPhoneNumber2.setAdapter(adapterBelonging);
        tvPhoneNumber3.setAdapter(adapterBelonging);
        tvLocationAddress.setAdapter(adapterLocations);
        tvLocationsBirth.setAdapter(adapterLocations);
        tvSchoolOrigin.setAdapter(adapterSchools);

        ///////////
        Spinner spinnerPhoneType1 = rootView.findViewById(R.id.phone_type1_select);
        Spinner spinnerPhoneType2 = rootView.findViewById(R.id.phone_type2_select);
        Spinner spinnerPhoneType3 = rootView.findViewById(R.id.phone_type3_select);
        String[] phoneTypes = getResources().getStringArray(R.array.phone_type_array);
        ArrayAdapter<String> adapterPhoneTypes = new ArrayAdapter<>(getApplicationContext(),
                R.layout.spinner_item, phoneTypes);
        adapterPhoneTypes.setDropDownViewResource(R.layout.spinner_item);
        spinnerPhoneType1.setAdapter(adapterPhoneTypes);
        spinnerPhoneType2.setAdapter(adapterPhoneTypes);
        spinnerPhoneType3.setAdapter(adapterPhoneTypes);
    }

    private void loadEducationLevels() {
        educationLevelsViewModel.getAll().observe(getActivity(), new Observer<List<EducationLevelData>>() {
            @Override
            public void onChanged(@Nullable List<EducationLevelData> educationLevels) {
                if (educationLevels != null && educationLevels.size() > 0) {
                    Spinner highEducMotherSpinner = rootView.findViewById(R.id.high_educ_level_mother_select);
                    Spinner highEducFatherSpinner = rootView.findViewById(R.id.high_educ_level_father_select);
                    EducationLevelData educLevel = new EducationLevelData();
                    educLevel.setDescription("Seleccione una opción");
                    educLevel.setId(-1);
                    educationLevels.add(0, educLevel);
                    ArrayAdapter<EducationLevelData> dataAdapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, educationLevels);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    highEducMotherSpinner.setAdapter(dataAdapter);
                    highEducFatherSpinner.setAdapter(dataAdapter);
                    StudentFragment.this.educationLevelsList = educationLevels;
                    //loadData();
                } else {
                    finishLoadEvent("No se cargó correctamente el catalogo de parentescos.");
                }
            }
        });
    }

    private void loadDocumentations() {
        documentationViewModel.getAll().observe(getActivity(), new Observer<List<Documentation>>() {
            @Override
            public void onChanged(@Nullable List<Documentation> documentations) {
                if (documentations.size() > 0) {
                    LinearLayout documentationsContainer = rootView.findViewById(R.id.documentations_container);
                    for (Documentation documentation : documentations) {
                        CheckBox cb = new CheckBox(getActivity());
                        cb.setText(Html.fromHtml(documentation.getDescription()));
                        cb.setTag(documentation.getId());
                        documentationsContainer.addView(cb);
                    }
                    loadPersonGenders();
                } else {
                    finishLoadEvent("No se cargó correctamente el catalogo de documentación.");
                }
            }
        });
    }

    private void loadDocumentTypes() {
        documentTypesViewModel.getAll().observe(getActivity(), new Observer<List<DocumentTypes>>() {
            @Override
            public void onChanged(@Nullable List<DocumentTypes> documentTypes) {
                if (documentTypes.size() > 0) {
                    documentTypesString = stream(documentTypes).select(DocumentTypes::getDescription).toList().toArray(new String[documentTypes.size()]);
                    documentTypesList = documentTypes;
                    ((SwitchMultiButton) rootView.findViewById(R.id.doc_type)).setText(documentTypesString);
                    ((SwitchMultiButton) rootView.findViewById(R.id.doc_type)).clearSelection();
                    loadStudentConditions();
                } else {
                    finishLoadEvent("No se cargó correctamente el catálogo de tipos de documento.");
                }
            }
        });
    }

    private void loadStudentConditions() {
        studentConditionsViewModel.getAll().observe(getActivity(), new Observer<List<StudentConditions>>() {
            @Override
            public void onChanged(@Nullable List<StudentConditions> studentConditions) {
                if (studentConditions.size() > 0) {
                    studentConditionsString = stream(studentConditions).select(StudentConditions::getDescription).toList().toArray(new String[studentConditions.size()]);
                    studentConditionsList = studentConditions;
                    ((SwitchMultiButton) rootView.findViewById(R.id.condition_switch)).setText(studentConditionsString);
                    ((SwitchMultiButton) rootView.findViewById(R.id.condition_switch)).clearSelection();
                    loadCourseDivisions();
                } else {
                    finishLoadEvent("No se cargó correctamente el catálogo de tipos de documento.");
                }
            }
        });
    }

    private void loadPersonGenders() {
        personGendersViewModel.getAll().observe(getActivity(), new Observer<List<PersonGenders>>() {
            @Override
            public void onChanged(@Nullable List<PersonGenders> personGenders) {
                if (personGenders.size() > 0) {
                    personGendersString = stream(personGenders).select(PersonGenders::getDescription).toList().toArray(new String[personGenders.size()]);
                    personGendersList = personGenders;
                    ((SwitchMultiButton) rootView.findViewById(R.id.gender)).setText(personGendersString);
                    ((SwitchMultiButton) rootView.findViewById(R.id.gender)).clearSelection();
                    loadDocumentTypes();
                } else {
                    finishLoadEvent("No se cargó correctamente el catalogo de sexos de persona.");
                }
            }
        });
    }

    private void loadCourseDivisions() {
        courseYearViewModel.getCourseYearsDivisionsByShiftId(SharedConfig.getShiftId()).observe(getActivity(), new Observer<List<CourseYearAndAllDivisions>>() {
            @Override
            public void onChanged(@Nullable List<CourseYearAndAllDivisions> courseYears) {
                if (courseYears.size() > 0) {
                    // agrupar las divisiones por curso, refactorizar esta parte tratando de usar consultas de ROOM
                    List<CustomCourseYear> ccy = new ArrayList<>();
                    for (CourseYearAndAllDivisions cyd : courseYears) {
                        CustomCourseYear customCourseYear = stream(ccy).firstOrNull(e -> e.Id == cyd.CourseYearId);
                        if (customCourseYear == null) {
                            customCourseYear = new CustomCourseYear(cyd.CourseYearId, cyd.CourseYearName);
                            ccy.add(customCourseYear);
                        }
                        customCourseYear.divisions.add(new CustomDivision(cyd.DivisionId, cyd.DivisionName));
                    }
                    // fin agrupamiento

                    courseYearString = stream(ccy).select(c -> c.CourseYearName).toList().toArray(new String[ccy.size()]);
                    courseYearsList = ccy;
                    ((SwitchMultiButton) rootView.findViewById(R.id.curso_switch)).setText(courseYearString);
                    ((SwitchMultiButton) rootView.findViewById(R.id.curso_switch)).clearSelection();
                    ((SwitchMultiButton) rootView.findViewById(R.id.curso_switch)).setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
                        @Override
                        public void onSwitch(int position, String tabText) {
                            CustomCourseYear c = courseYearsList.get(position);
                            divisionsString = stream(c.divisions).select(e -> e.DivisionName).toList().toArray(new String[c.divisions.size()]);
                            calcularEdad();
                            if (divisionsString.length > 0) {
                                if (divisionsString.length > 1) {
                                    rootView.findViewById(R.id.lbl_division).setVisibility(View.VISIBLE);
                                    rootView.findViewById(R.id.division_switch).setVisibility(View.VISIBLE);
                                    rootView.findViewById(R.id.unique_division).setVisibility(View.GONE);
                                    ((SwitchMultiButton) rootView.findViewById(R.id.division_switch)).setText(divisionsString);
                                    ((SwitchMultiButton) rootView.findViewById(R.id.division_switch)).clearSelection();
                                } else {
                                    rootView.findViewById(R.id.lbl_division).setVisibility(View.VISIBLE);
                                    rootView.findViewById(R.id.division_switch).setVisibility(View.GONE);
                                    rootView.findViewById(R.id.unique_division).setVisibility(View.VISIBLE);
                                    ((TextView) rootView.findViewById(R.id.unique_division)).setText(divisionsString[0]);
                                }
                            } else {
                                finishLoadEvent("No existen Cursos para asignar.");
                            }
                        }
                    });
                    rootView.findViewById(R.id.unique_division).setVisibility(View.GONE);
                    rootView.findViewById(R.id.division_switch).setVisibility(View.GONE);
                    loadStudentData();
                } else {
                    finishLoadEvent("No existen Cursos para asignar.");
                }
            }
        });
    }

    private void loadStudentData() {
        String studentId = ((NonSwipeableViewPager) container).getStudentId();
        boolean newStudent = ((NonSwipeableViewPager) container).isNewStudent();
        int action = ((NonSwipeableViewPager) container).getAction();
        if (action == StudentTutorActivity.EDIT_STUDENT) { //buscar estudiante
            studentViewModel.getTempStudent(studentId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<StudentData>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(StudentData studentData) {
                    // success
                    DocumentTypes dt = stream(documentTypesList).where(e -> e.getId() == studentData.getDocumentTypeId()).firstOrNull();
                    int pos = 0;
                    if (dt != null) {
                        pos = documentTypesList.indexOf(dt);
                    }
                    ((SwitchMultiButton) rootView.findViewById(R.id.doc_type)).setSelectedTab(pos);

                    StudentConditions sc = stream(studentConditionsList).where(e -> e.getId() == studentData.ConditionId).firstOrNull();
                    int posSc = 0;
                    if (sc != null) {
                        posSc = studentConditionsList.indexOf(sc);
                    }
                    ((SwitchMultiButton) rootView.findViewById(R.id.condition_switch)).setSelectedTab(posSc);

                    ((EditText) rootView.findViewById(R.id.input_firstame)).setText(studentData.getFirstName());
                    ((EditText) rootView.findViewById(R.id.input_lastname)).setText(studentData.getLastName());
                    ((EditText) rootView.findViewById(R.id.input_document_number)).removeTextChangedListener(textWatcher);
                    ((EditText) rootView.findViewById(R.id.input_document_number)).setText(studentData.getDocumentNumber());
                    ((EditText) rootView.findViewById(R.id.input_document_number)).addTextChangedListener(textWatcher);
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    if (studentData.getBirthdate() != null) {
                        ((EditText) rootView.findViewById(R.id.input_birthdate)).setText(format.format(studentData.getBirthdate()));
                    }

                    PersonGenders pg = stream(personGendersList).where(e -> e.getId() == studentData.getPersonGenderId()).firstOrNull();
                    pos = 0;
                    if (pg != null) {
                        pos = personGendersList.indexOf(pg);
                    }
                    ((SwitchMultiButton) rootView.findViewById(R.id.gender)).setSelectedTab(pos);


                    ((EditText) rootView.findViewById(R.id.input_phone_number)).setText(studentData.getPhoneNumber());
                    ((EditText) rootView.findViewById(R.id.input_mobile_phone_number)).setText(studentData.getMobilePhoneNumber());
                    ((EditText) rootView.findViewById(R.id.input_another_phone_number)).setText(studentData.getAnotherContactPhone());
                    ((EditText) rootView.findViewById(R.id.input_address)).setText(studentData.getAddress());
                    ((EditText) rootView.findViewById(R.id.input_location)).setText(studentData.getLocation());
                    ((EditText) rootView.findViewById(R.id.input_nacionality)).setText(studentData.getNationality());

                    ((EditText) rootView.findViewById(R.id.input_country_birth)).setText(studentData.getCountryBirth());
                    ((EditText) rootView.findViewById(R.id.input_province_birth)).setText(studentData.getProvinceBirth());
                    ((EditText) rootView.findViewById(R.id.input_department_birth)).setText(studentData.getDepartmentBirth());
                    ((EditText) rootView.findViewById(R.id.input_location_birth)).setText(studentData.getLocationBirth());

                    ((EditText) rootView.findViewById(R.id.input_mail)).setText(studentData.getMail());

                    Spinner spinner1 = ((Spinner) rootView.findViewById(R.id.phone_type1_select));
                    Spinner spinner2 = ((Spinner) rootView.findViewById(R.id.phone_type2_select));
                    Spinner spinner3 = ((Spinner) rootView.findViewById(R.id.phone_type3_select));
                    ArrayAdapter adapter = (ArrayAdapter) spinner1.getAdapter();
                    if (studentData.getPhoneType1() != "") {
                        spinner1.setSelection(adapter.getPosition(studentData.getPhoneType1()));
                    }

                    if (studentData.getPhoneType2() != "") {
                        spinner2.setSelection(adapter.getPosition(studentData.getPhoneType2()));
                    }

                    if (studentData.getPhoneType3() != "") {
                        spinner3.setSelection(adapter.getPosition(studentData.getPhoneType3()));
                    }

                    ((EditText) rootView.findViewById(R.id.input_belonging_phone_1)).setText(studentData.PhoneBelongs1);
                    ((EditText) rootView.findViewById(R.id.input_belonging_phone_2)).setText(studentData.PhoneBelongs2);
                    ((EditText) rootView.findViewById(R.id.input_belonging_phone_3)).setText(studentData.OtherPhoneBelongs);


                    ((EditText) rootView.findViewById(R.id.input_school_origin)).setText(studentData.SchoolOrigin);
                    ((EditText) rootView.findViewById(R.id.input_another_phone_number_owner)).setText(studentData.OtherPhoneBelongs);
                    ((EditText) rootView.findViewById(R.id.input_observation)).setText(studentData.Observation);


                    int hb = studentData.HasBrothersOfSchoolAge ? 0 : 1;
                    ((SwitchMultiButton) rootView.findViewById(R.id.has_brother_switch)).setSelectedTab(hb);

                    int beg = studentData.isBelongsEthnicGroup() ? 0 : 1;
                    ((SwitchMultiButton) rootView.findViewById(R.id.belongs_ethnic_group)).setSelectedTab(beg);

                    int hhp = studentData.isHasHealthProblems() ? 0 : 1;
                    ((SwitchMultiButton) rootView.findViewById(R.id.has_health_problems)).setSelectedTab(hhp);

                    int hlp = studentData.isHasLegalProblems() ? 0 : 1;
                    ((SwitchMultiButton) rootView.findViewById(R.id.has_legal_problems)).setSelectedTab(hlp);

                    if (studentData.isBelongsEthnicGroup()) {
                        ((EditText) rootView.findViewById(R.id.input_belongs_ethnic_group_observation)).setText(studentData.getEthnicName());
                    }
                    if (studentData.isHasHealthProblems()) {
                        ((EditText) rootView.findViewById(R.id.input_has_health_problems_observation)).setText(studentData.getDescriptionHealthProblems());
                    }
                    if (studentData.isHasLegalProblems()) {
                        ((EditText) rootView.findViewById(R.id.input_has_legal_problems_observation)).setText(studentData.getDescriptionLegalProblems());
                    }

                    int selectedCourse = -1;
                    int selectedDivision = -1;

                    courseYearId = ((NonSwipeableViewPager) container).getCourseYearId();
                    if (courseYearId != -1) {
                        CustomCourseYear courseYear = stream(courseYearsList).where(e -> e.Id == courseYearId).firstOrNull();
                        if (courseYear != null) {
                            selectedCourse = courseYearsList.indexOf(courseYear);
                        }
                    }

                    for (CustomCourseYear cy : courseYearsList) {
                        CustomDivision division = stream(cy.divisions).where(e -> e.Id == studentData.DivisionId).firstOrNull();
                        if (division != null) {
                            selectedCourse = courseYearsList.indexOf(cy);
                            selectedDivision = cy.divisions.indexOf(division);
                            break;
                        }
                    }

                    if (selectedCourse != -1) {
                        ((SwitchMultiButton) rootView.findViewById(R.id.curso_switch)).setSelectedTab(selectedCourse);
                        ((SwitchMultiButton) rootView.findViewById(R.id.division_switch)).setSelectedTab(selectedDivision);
                    }

                    calcularEdad();
                    idCardBack = studentData.IdCardBack;
                    idCardFront = studentData.IdCardFront;
                    idImgProfile = studentData.IdImgProfile;
                    if (idCardFront != null && !idCardFront.isEmpty()) {
                        new LoadImagen(idCardFront, rootView.findViewById(R.id.img_anverso_dni)).execute();
                    }
                    if (idCardBack != null && !idCardBack.isEmpty()) {
                        new LoadImagen(idCardBack, rootView.findViewById(R.id.img_reverso_dni)).execute();
                    }

                    if (idImgProfile != null && !idImgProfile.isEmpty()) {
                        new LoadImagen(idImgProfile, rootView.findViewById(R.id.img_profile)).execute();
                    }

                    // load documentations
                    studentDocumentationViewModel.getDocumentationsTemp(studentId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<List<Integer>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(List<Integer> documentations) {
                            // success
                            studentData.documentations = documentations;
                            finishLoadEvent("");
                            LinearLayout documentationsContainer = rootView.findViewById(R.id.documentations_container);
                            for (int documentation : studentData.documentations) {
                                View v = documentationsContainer.findViewWithTag(documentation);
                                if (v instanceof CheckBox) {
                                    CheckBox checkBox = (CheckBox) v;
                                    checkBox.setChecked(true);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            finishLoadEvent("Ocurrió un error al cargar el alumno");
                        }

                        @Override
                        public void onComplete() {
                            finishLoadEvent("Ocurrió un error al cargar el alumno");
                        }
                    });

                    EducationLevelData elM = stream(educationLevelsList).where(e -> e.getId() == studentData.HighestEducLevelMotherId).firstOrNull();
                    pos = 0;
                    if (elM != null) {
                        pos = educationLevelsList.indexOf(elM);
                    }
                    ((Spinner) rootView.findViewById(R.id.high_educ_level_mother_select)).setSelection(pos);

                    EducationLevelData elF = stream(educationLevelsList).where(e -> e.getId() == studentData.HighestEducLevelFatherId).firstOrNull();
                    pos = 0;
                    if (elF != null) {
                        pos = educationLevelsList.indexOf(elF);
                    }
                    ((Spinner) rootView.findViewById(R.id.high_educ_level_father_select)).setSelection(pos);
                }

                @Override
                public void onError(Throwable e) {
                    finishLoadEvent("Ocurrió un error al cargar el alumno");
                }

                @Override
                public void onComplete() {
                    finishLoadEvent("Ocurrió un error al cargar el alumno");
                }
            });
        } else {
            courseYearId = ((NonSwipeableViewPager) container).getCourseYearId();
            if (courseYearId != 0) {
                int selectedCourse = -1;
                CustomCourseYear courseYear = stream(courseYearsList).where(e -> e.Id == courseYearId).firstOrNull();
                if (courseYear != null) {
                    selectedCourse = courseYearsList.indexOf(courseYear);
                    ((SwitchMultiButton) rootView.findViewById(R.id.curso_switch)).setSelectedTab(selectedCourse);
                }
            }
            finishLoadEvent("");
        }
    }

    private void calcularEdad() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String bd = ((EditText) rootView.findViewById(R.id.input_birthdate)).getText().toString();
            Calendar calendarFechaNacimiento = Calendar.getInstance();
            calendarFechaNacimiento.setTime(format.parse(bd));
            Calendar calendarFechaActual = Calendar.getInstance();

            calendarFechaActual.setTime(new Date());
            Integer edad = Util.calcularEdad(calendarFechaActual, calendarFechaNacimiento);
            EditText editText = rootView.findViewById(R.id.input_age);
            editText.setText(String.format("%d años", edad));
            if (ageControl(edad)) {
                rootView.findViewById(R.id.lbl_age_control).setVisibility(View.VISIBLE);
            } else {
                rootView.findViewById(R.id.lbl_age_control).setVisibility(View.GONE);
            }
        } catch (Exception ignored) {

        }
    }

    //Verifica sobreedad de un alumno
    private boolean ageControl(int edad) {
        boolean result = false;
        int pos = ((SwitchMultiButton) rootView.findViewById(R.id.curso_switch)).getSelectedTab();
        if (pos != -1) {
            CustomCourseYear c = courseYearsList.get(pos);

            if (c.CourseYearName.contains("1") && edad>16) {
                result = true;
            } else {
                if (c.CourseYearName.contains("2") && edad>17) {
                    result = true;
                } else {
                    if (c.CourseYearName.contains("3") && edad>18) {
                        result = true;
                    } else {
                        if (c.CourseYearName.contains("4") && edad>19) {
                            result = true;
                        } else {
                            if (c.CourseYearName.contains("5") && edad>20) {
                                result = true;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public void finishLoadEvent(String event) {
        EventBus.getDefault().post(new Evento("finishLoadData", event));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SCANNER2_ACTIVITY_RESULT:
                    escanearDNI(data);
                    break;
                case CUSTOM_CAMERA_REQUEST_CODE_ANVERSO:
                    String path = data.getStringExtra("filePath") != null ? data.getStringExtra("filePath") : "";
                    if (!path.isEmpty()) {
                        idCardFront = path;
                        new LoadImagen(path, rootView.findViewById(R.id.img_anverso_dni)).execute();
                    } else {
                        new MaterialDialog.Builder(getActivity())
                                .title("Error")
                                .content("No se pudo registrar la foto correctamente, intentelo nuevamente.")
                                .positiveText("Aceptar")
                                .onPositive((d, which) -> d.dismiss())
                                .show();
                    }
                    break;
                case CUSTOM_CAMERA_REQUEST_CODE_REVERSO:
                    path = data.getStringExtra("filePath") != null ? data.getStringExtra("filePath") : "";
                    if (!path.isEmpty()) {
                        idCardBack = path;
                        new LoadImagen(path, rootView.findViewById(R.id.img_reverso_dni)).execute();
                    } else {
                        new MaterialDialog.Builder(getActivity())
                                .title("Error")
                                .content("No se pudo registrar la foto correctamente, intentelo nuevamente.")
                                .positiveText("Aceptar")
                                .onPositive((d, which) -> d.dismiss())
                                .show();
                    }
                    break;
                case SCANNER_ACTIVITY_RESULT:
                    Object object = data.getExtras().getSerializable("result");
                    String rawResult = data.getExtras().getString("rawResult");
                    this.scannerResult = rawResult != null ? rawResult : "";
                    if (object instanceof Person) {
                        Person result = (Person) data.getExtras().getSerializable("result");
                        try {
                            ((EditText) rootView.findViewById(R.id.input_firstame)).setText(result.getFirstName());
                            ((EditText) rootView.findViewById(R.id.input_lastname)).setText(result.getLastName());
                            ((EditText) rootView.findViewById(R.id.input_document_number)).removeTextChangedListener(textWatcher);
                            ((EditText) rootView.findViewById(R.id.input_document_number)).setText(result.getDocumentNumber());
                            ((EditText) rootView.findViewById(R.id.input_document_number)).addTextChangedListener(textWatcher);
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            ((EditText) rootView.findViewById(R.id.input_birthdate)).setText(format.format(result.getBirthdate()));
                            ((SwitchMultiButton) rootView.findViewById(R.id.gender)).setSelectedTab(result.getPersonGenderId() - 1);
                            calcularEdad();

                            // buscar la persona y traer los datos no escaneados por el dni (Siempre tomar los datos del dni excepto los que no se escanean)
                            personViewModel.getByDni(result.getDocumentNumber()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Person>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Person person) {
                                    if (person != null) {
                                        ((EditText) rootView.findViewById(R.id.input_phone_number)).setText(person.getPhoneNumber());
                                        ((EditText) rootView.findViewById(R.id.input_mobile_phone_number)).setText(person.getMobilePhoneNumber());
                                        ((EditText) rootView.findViewById(R.id.input_another_phone_number)).setText(person.getAnotherContactPhone());
                                        ((EditText) rootView.findViewById(R.id.input_address)).setText(person.getAddress());
                                        ((EditText) rootView.findViewById(R.id.input_location)).setText(person.getLocation());
                                        ((EditText) rootView.findViewById(R.id.input_nacionality)).setText(person.getNationality());

                                        int beg = person.isBelongsEthnicGroup() ? 0 : 1;
                                        ((SwitchMultiButton) rootView.findViewById(R.id.belongs_ethnic_group)).setSelectedTab(beg);

                                        int hhp = person.isHasHealthProblems() ? 0 : 1;
                                        ((SwitchMultiButton) rootView.findViewById(R.id.has_health_problems)).setSelectedTab(hhp);

                                        int hlp = person.isHasLegalProblems() ? 0 : 1;
                                        ((SwitchMultiButton) rootView.findViewById(R.id.has_legal_problems)).setSelectedTab(hlp);

                                        if (person.isBelongsEthnicGroup()) {
                                            ((EditText) rootView.findViewById(R.id.input_belongs_ethnic_group_observation)).setText(person.getEthnicName());
                                        }
                                        if (person.isHasHealthProblems()) {
                                            ((EditText) rootView.findViewById(R.id.input_has_health_problems_observation)).setText(person.getDescriptionHealthProblems());
                                        }
                                        if (person.isHasLegalProblems()) {
                                            ((EditText) rootView.findViewById(R.id.input_has_legal_problems_observation)).setText(person.getDescriptionLegalProblems());
                                        }

                                        // buscar datos como alumno
                                        searchStudentData(person.getId());
                                        updateTempData(person.getId());
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });

                        } catch (Exception e) {
                            new MaterialDialog.Builder(getActivity())
                                    .title("Error")
                                    .content("Posible error al mostrar los datos escaneados. Por favor verifique si son correctos. Caso contrario intentelo nuevamente.")
                                    .positiveText("Aceptar")
                                    .onPositive((d, which) -> d.dismiss())
                                    .show();
                        }
                    } else {
                        new MaterialDialog.Builder(getActivity())
                                .title("Error")
                                .content("Hubo un error al escanear los datos. Intentelo nuevamente.")
                                .positiveText("Aceptar")
                                .onPositive((d, which) -> d.dismiss())
                                .show();
                    }
                    break;
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

        if (result.getResultState() == Recognizer.Result.State.Valid) {
            if (!result.getClassInfo().isEmpty())  {
                if (result.getClassInfo().getCountry().name().equals("ARGENTINA")) {
                    ((SwitchMultiButton) rootView.findViewById(R.id.doc_type)).setSelectedTab(0);
                } else {
                    ((SwitchMultiButton) rootView.findViewById(R.id.doc_type)).setSelectedTab(1);
                }
            }

            ((EditText) rootView.findViewById(R.id.input_firstame)).setText(result.getFirstName());
            ((EditText) rootView.findViewById(R.id.input_lastname)).setText(result.getLastName());
            ((EditText) rootView.findViewById(R.id.input_document_number)).removeTextChangedListener(textWatcher);
            ((EditText) rootView.findViewById(R.id.input_document_number)).setText(result.getDocumentNumber());
            ((EditText) rootView.findViewById(R.id.input_document_number)).addTextChangedListener(textWatcher);
            //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = result.getDateOfBirth().getOriginalDateString();

            ////////////////

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = sdf.parse(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFinal = sdf.format(date);
            ////////////////

            //String formato = format.format(result.getDateOfBirth());
            ((EditText) rootView.findViewById(R.id.input_birthdate)).setText(fecha);
            ((EditText) rootView.findViewById(R.id.input_address)).setText(result.getAddress());
            ((EditText) rootView.findViewById(R.id.input_nacionality)).setText(result.getNationality());
            int sex = -1;
            if (result.getSex().equals("M")) {
                sex = 0;
            } else if (result.getSex().equals("F")) {
                sex = 1;
            } else {
                sex = 2;
            }
            ((SwitchMultiButton) rootView.findViewById(R.id.gender)).setSelectedTab(sex);
            calcularEdad();
        }

        String folderImage = getApplicationContext().getFilesDir() + "/scanDocuments" + "/" + result.getDocumentNumber();
//        String profileFolderImage = getApplicationContext().getApplicationInfo().dataDir + Constants.profilesPath;
        String profileFolderImage = getApplicationContext().getFilesDir() + "/profiles";
        try {
            storeImage(folderImage, "successImage", successFrameGrabberRecognizer.getResult().getSuccessFrame());
            storeImage(folderImage, "fullDocumentImageFront", blinkIdRecognizer.getResult().getFullDocumentFrontImage());
            storeImage(folderImage, "fullDocumentImageBack", blinkIdRecognizer.getResult().getFullDocumentBackImage());

            storeImage(folderImage, "profileImage", blinkIdRecognizer.getResult().getFaceImage());
            storeImage(profileFolderImage, blinkIdRecognizer.getResult().getDocumentNumber(), blinkIdRecognizer.getResult().getFaceImage());

            ImageView imgFront = (ImageView) rootView.findViewById(R.id.img_anverso_dni);
            ImageView imgBack = (ImageView) rootView.findViewById(R.id.img_reverso_dni);
            ImageView imgProfile = (ImageView) rootView.findViewById(R.id.img_profile);
            //ImageView img1 = (ImageView) findViewById(R.id.img2);
            //img2 = (ImageView) findViewById(R.id.img3);

            //StudentRepository.getInstance().setImageUrl(imgFront, profileFolderImage + "/" + result.getDocumentNumber() + ".jpg");
            String fileNameProfileImg = profileFolderImage + "/" + result.getDocumentNumber() + ".jpg";
            Bitmap myBitmap = BitmapFactory.decodeFile(fileNameProfileImg);
            imgProfile.setImageBitmap(myBitmap);
            idImgProfile = fileNameProfileImg;

            String fileNameCardFront = folderImage + "/" + "fullDocumentImageFront" + ".jpg";
            Bitmap myBitmap2 = BitmapFactory.decodeFile(fileNameCardFront);
            imgFront.setImageBitmap(myBitmap2);
            idCardFront = fileNameCardFront;

            String fileNameCardBack = folderImage + "/" + "fullDocumentImageBack" + ".jpg";
            Bitmap myBitmap3 = BitmapFactory.decodeFile(fileNameCardBack);
            imgBack.setImageBitmap(myBitmap3);
            idCardBack = fileNameCardBack;
            //storeHighResImage(folderImage, data);
        } catch (Exception e) {
            //Timber.e("Error imagenes: %s", e.getMessage());
            Log.d(TAG, "Error imagenes: %s" + e.getMessage());
            //makeText(this, "ERROR " + e.getMessage(), LENGTH_SHORT).show();
        }
    }

    private void updateTempData(String newSudentId) {
        studentViewModel.updateStudenTempData(((NonSwipeableViewPager) container).getStudentId(), newSudentId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String tempData) {
                        ((NonSwipeableViewPager) container).setStudentId(newSudentId);
                        Fragment page = StudentFragment.this.getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":1");
                        if (page instanceof TutorsListFragment) {
                            ((TutorsListFragment) page).resetData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void searchStudentData(String personId) {
        studentViewModel.getStudent(personId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<StudentData>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(StudentData studentData) {
                // success
                ((EditText) rootView.findViewById(R.id.input_school_origin)).setText(studentData.SchoolOrigin);
                ((EditText) rootView.findViewById(R.id.input_another_phone_number_owner)).setText(studentData.OtherPhoneBelongs);
                ((EditText) rootView.findViewById(R.id.input_observation)).setText(studentData.Observation);

                int hb = studentData.HasBrothersOfSchoolAge ? 0 : 1;
                ((SwitchMultiButton) rootView.findViewById(R.id.has_brother_switch)).setSelectedTab(hb);

                int selectedCourse = -1;
                int selectedDivision = -1;
                for (CustomCourseYear cy : courseYearsList) {
                    CustomDivision division = stream(cy.divisions).where(e -> e.Id == studentData.DivisionId).firstOrNull();
                    if (division != null) {
                        selectedCourse = courseYearsList.indexOf(cy);
                        selectedDivision = cy.divisions.indexOf(division);
                        break;
                    }
                }

                if (selectedCourse != -1) {
                    ((SwitchMultiButton) rootView.findViewById(R.id.curso_switch)).setSelectedTab(selectedCourse);
                    ((SwitchMultiButton) rootView.findViewById(R.id.division_switch)).setSelectedTab(selectedDivision);
                }
                calcularEdad();
                // load documentations
                studentDocumentationViewModel.getDocumentations(personId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Integer> documentations) {
                        // success
                        studentData.documentations = documentations;
                        finishLoadEvent("");
                        LinearLayout documentationsContainer = rootView.findViewById(R.id.documentations_container);
                        for (int documentation : studentData.documentations) {
                            View v = documentationsContainer.findViewWithTag(documentation);
                            if (v instanceof CheckBox) {
                                CheckBox checkBox = (CheckBox) v;
                                checkBox.setChecked(true);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

                // load image if exits
                idCardFront = studentData.IdCardFront == null ? "" : studentData.IdCardFront;
                idCardBack = studentData.IdCardBack == null ? "" : studentData.IdCardBack;
                idImgProfile = studentData.IdImgProfile == null ? "" : studentData.IdImgProfile;

                if (!idCardFront.isEmpty()) {
                    new LoadImagen(idCardFront, rootView.findViewById(R.id.img_anverso_dni)).execute();
                }
                if (!idCardBack.isEmpty()) {
                    new LoadImagen(idCardBack, rootView.findViewById(R.id.img_reverso_dni)).execute();
                }
                if (!idImgProfile.isEmpty()) {
                    new LoadImagen(idImgProfile, rootView.findViewById(R.id.img_profile)).execute();
                }
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
    public void onClick(View view) {
        switch (view.getId()) {
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

        if (SharedConfig.isKeyValid()) {
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
            if(!RecognizerCompatibility.cameraHasAutofocus(CameraType.CAMERA_BACKFACE, getApplicationContext())) {
                result =  false;
            }
        } else {
            result = false;
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
            Toast.makeText(getActivity(), "Error StudentFragment= " + ex.getMessage(), LENGTH_SHORT).show();
        }

    }

    public void callScanner() {
        Intent i = new Intent(getActivity(), SimpleScannerActivity.class);
        startActivityForResult(i, SCANNER_ACTIVITY_RESULT);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        dateTextView.setText(simpleDateFormat.format(calendar.getTime()));
        calcularEdad();
    }

    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        String bd = ((EditText) rootView.findViewById(R.id.input_birthdate)).getText().toString().trim();
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
                .context(getActivity())
                .callback(StudentFragment.this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(Integer.parseInt(DateFormat.format("yyyy", date).toString()), Integer.parseInt(DateFormat.format("M", date).toString()) - 1, Integer.parseInt(DateFormat.format("d", date).toString()))
                .build()
                .show();
    }

    public StudentData getData() {
        StudentData studentData = new StudentData();
        studentData.setId(((NonSwipeableViewPager) container).getStudentId());
        studentData.setFirstName(((EditText) rootView.findViewById(R.id.input_firstame)).getText().toString());
        studentData.setLastName(((EditText) rootView.findViewById(R.id.input_lastname)).getText().toString());
        studentData.setDocumentNumber(((EditText) rootView.findViewById(R.id.input_document_number)).getText().toString());

        int posDocType = ((SwitchMultiButton) rootView.findViewById(R.id.doc_type)).getSelectedTab();
        if (posDocType != -1) {
            DocumentTypes docType = documentTypesList.get(posDocType);
            studentData.setDocumentTypeId(docType.getId());
        } else {
            studentData.setDocumentTypeId(posDocType);
        }

        int posGender = ((SwitchMultiButton) rootView.findViewById(R.id.gender)).getSelectedTab();
        if (posGender != -1) {
            PersonGenders personGenders = personGendersList.get(posGender);
            studentData.setPersonGenderId(personGenders.getId());
        } else {
            studentData.setPersonGenderId(posGender);
        }

        int posCondition = ((SwitchMultiButton) rootView.findViewById(R.id.condition_switch)).getSelectedTab();
        if (posCondition != -1) {
            StudentConditions studentConditions = studentConditionsList.get(posCondition);
            studentData.ConditionId = studentConditions.getId();
        } else {
            studentData.ConditionId = posCondition;
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String bd = ((EditText) rootView.findViewById(R.id.input_birthdate)).getText().toString();
            studentData.setBirthdate(format.parse(bd));
        } catch (Exception e) {
        }

        EducationLevelData educLevelDataF = (EducationLevelData) ((Spinner) rootView.findViewById(R.id.high_educ_level_father_select)).getSelectedItem();
        studentData.HighestEducLevelFatherId = educLevelDataF.getId();
        EducationLevelData educLevelDataM = (EducationLevelData) ((Spinner) rootView.findViewById(R.id.high_educ_level_mother_select)).getSelectedItem();
        studentData.HighestEducLevelMotherId = educLevelDataM.getId();

        studentData.setPhoneNumber(((EditText) rootView.findViewById(R.id.input_phone_number)).getText().toString());
        studentData.setPhoneType1(((Spinner) rootView.findViewById(R.id.phone_type1_select)).getSelectedItem().toString());
        studentData.setMobilePhoneNumber(((EditText) rootView.findViewById(R.id.input_mobile_phone_number)).getText().toString());
        studentData.setPhoneType2(((Spinner) rootView.findViewById(R.id.phone_type2_select)).getSelectedItem().toString());
        studentData.setAnotherContactPhone(((EditText) rootView.findViewById(R.id.input_another_phone_number)).getText().toString());
        studentData.setPhoneType3(((Spinner) rootView.findViewById(R.id.phone_type3_select)).getSelectedItem().toString());
        studentData.setAddress(((EditText) rootView.findViewById(R.id.input_address)).getText().toString());
        studentData.setLocation(((EditText) rootView.findViewById(R.id.input_location)).getText().toString());
        studentData.setNationality(((EditText) rootView.findViewById(R.id.input_nacionality)).getText().toString());

        studentData.setCountryBirth(((AutoCompleteTextView) rootView.findViewById(R.id.input_country_birth)).getText().toString());
        studentData.setProvinceBirth(((AutoCompleteTextView) rootView.findViewById(R.id.input_province_birth)).getText().toString());
        studentData.setDepartmentBirth(((AutoCompleteTextView) rootView.findViewById(R.id.input_department_birth)).getText().toString());
        studentData.setLocationBirth(((AutoCompleteTextView) rootView.findViewById(R.id.input_location_birth)).getText().toString());

        studentData.SchoolOrigin = ((EditText) rootView.findViewById(R.id.input_school_origin)).getText().toString();
        studentData.PhoneBelongs1 = ((EditText) rootView.findViewById(R.id.input_belonging_phone_1)).getText().toString();
        studentData.PhoneBelongs2 = ((EditText) rootView.findViewById(R.id.input_belonging_phone_2)).getText().toString();
        studentData.OtherPhoneBelongs = ((EditText) rootView.findViewById(R.id.input_belonging_phone_3)).getText().toString();
        studentData.setMail(((EditText) rootView.findViewById(R.id.input_mail)).getText().toString());
        studentData.Observation = ((EditText) rootView.findViewById(R.id.input_observation)).getText().toString();
        studentData.documentations.clear();

        LinearLayout documentationsContainer = rootView.findViewById(R.id.documentations_container);
        for (int i = 0; i < documentationsContainer.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) documentationsContainer.getChildAt(i);
            int documenation = (int) checkBox.getTag();
            if (!studentData.documentations.contains(documenation) && checkBox.isChecked()) {
                studentData.documentations.add(documenation);
            }
        }

        int pos = ((SwitchMultiButton) rootView.findViewById(R.id.curso_switch)).getSelectedTab();
        if (pos != -1) {
            CustomCourseYear c = courseYearsList.get(pos);
            if (divisionsString.length > 1) {
                int pd = ((SwitchMultiButton) rootView.findViewById(R.id.division_switch)).getSelectedTab();
                if (pd != -1) {
                    CustomDivision division = c.divisions.get(pd);
                    studentData.DivisionId = division.Id;
                } else {
                    studentData.DivisionId = pd;
                }
            } else {
                CustomDivision division = c.divisions.get(0);
                studentData.DivisionId = division.Id;
            }
        } else {
            studentData.DivisionId = pos;
        }

        studentData.IdCardBack = idCardBack;
        studentData.IdCardFront = idCardFront;
        studentData.IdImgProfile = idImgProfile;
        int hasBrother = ((SwitchMultiButton) rootView.findViewById(R.id.has_brother_switch)).getSelectedTab();
        if (hasBrother != -1) {
            studentData.HasBrothersOfSchoolAge = hasBrother == 0;
            studentData.HasBrothersOfSchoolAgeAnswered = true;
        } else {
            studentData.HasBrothersOfSchoolAgeAnswered = false;
        }

        int hasEthnic = ((SwitchMultiButton) rootView.findViewById(R.id.belongs_ethnic_group)).getSelectedTab();
        if (hasEthnic != -1) {
            studentData.setBelongsEthnicGroup(hasEthnic == 0);
            studentData.BelongsEthnicGroupAnswered = true;
            if (studentData.isBelongsEthnicGroup()) {
                studentData.setEthnicName(((EditText) rootView.findViewById(R.id.input_belongs_ethnic_group_observation)).getText().toString());
            } else {
                studentData.setEthnicName("");
            }
        } else {
            studentData.BelongsEthnicGroupAnswered = false;
            studentData.setEthnicName("");
        }

        int hasHealth = ((SwitchMultiButton) rootView.findViewById(R.id.has_health_problems)).getSelectedTab();
        if (hasHealth != -1) {
            studentData.setHasHealthProblems(hasHealth == 0);
            studentData.HasHealthProblemsAnswered = true;
            if (studentData.isHasHealthProblems()) {
                studentData.setDescriptionHealthProblems(((EditText) rootView.findViewById(R.id.input_has_health_problems_observation)).getText().toString());
            } else {
                studentData.setDescriptionHealthProblems("");
            }
        } else {
            studentData.HasHealthProblemsAnswered = false;
            studentData.setDescriptionHealthProblems("");
        }

        int hasLegal = ((SwitchMultiButton) rootView.findViewById(R.id.has_legal_problems)).getSelectedTab();
        if (hasLegal != -1) {
            studentData.setHasLegalProblems(hasLegal == 0);
            studentData.HasLegalProblemsAnswered = true;
            if (studentData.isHasLegalProblems()) {
                studentData.setDescriptionLegalProblems(((EditText) rootView.findViewById(R.id.input_has_legal_problems_observation)).getText().toString());
            } else {
                studentData.setDescriptionLegalProblems("");
            }
        } else {
            studentData.HasLegalProblemsAnswered = false;
            studentData.setDescriptionLegalProblems("");
        }

        studentData.setScannerResult(this.scannerResult);
        return studentData;
    }

    public void clearComponents(String studentId) {
        ((EditText) rootView.findViewById(R.id.input_firstame)).setText("");
        ((EditText) rootView.findViewById(R.id.input_lastname)).setText("");
        ((EditText) rootView.findViewById(R.id.input_document_number)).setText("");
        ((SwitchMultiButton) rootView.findViewById(R.id.gender)).clearSelection();
        ((EditText) rootView.findViewById(R.id.input_birthdate)).setText("");
        ((EditText) rootView.findViewById(R.id.input_phone_number)).setText("");
        ((EditText) rootView.findViewById(R.id.input_mobile_phone_number)).setText("");
        ((EditText) rootView.findViewById(R.id.input_another_phone_number)).setText("");
        ((EditText) rootView.findViewById(R.id.input_address)).setText("");
        ((EditText) rootView.findViewById(R.id.input_location)).setText("");
        ((EditText) rootView.findViewById(R.id.input_school_origin)).setText("");
        ((EditText) rootView.findViewById(R.id.input_another_phone_number_owner)).setText("");
        ((EditText) rootView.findViewById(R.id.input_observation)).setText("");
        ((EditText) rootView.findViewById(R.id.input_nacionality)).setText("");
        ((SwitchMultiButton) rootView.findViewById(R.id.curso_switch)).clearSelection();
        ((SwitchMultiButton) rootView.findViewById(R.id.has_brother_switch)).clearSelection();
        ((SwitchMultiButton) rootView.findViewById(R.id.belongs_ethnic_group)).clearSelection();
        ((SwitchMultiButton) rootView.findViewById(R.id.has_health_problems)).clearSelection();
        ((SwitchMultiButton) rootView.findViewById(R.id.has_legal_problems)).clearSelection();
        rootView.findViewById(R.id.unique_division).setVisibility(View.GONE);
        rootView.findViewById(R.id.division_switch).setVisibility(View.GONE);
        ((SwitchMultiButton) rootView.findViewById(R.id.division_switch)).clearSelection();

        LinearLayout documentationsContainer = rootView.findViewById(R.id.documentations_container);
        for (int i = 0; i < documentationsContainer.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) documentationsContainer.getChildAt(i);
            checkBox.setChecked(false);
        }

        idCardBack = "";
        idCardFront = "";
        idImgProfile = "";
        new ClearImagen(rootView.findViewById(R.id.img_anverso_dni)).execute();
        new ClearImagen(rootView.findViewById(R.id.img_reverso_dni)).execute();
        new ClearImagen(rootView.findViewById(R.id.img_profile)).execute();
    }

    public void scrollTop() {
        ((ScrollView) rootView.findViewById(R.id.scroll_student)).fullScroll(ScrollView.FOCUS_UP);
    }


    private class LoadImagen extends AsyncTask<String, Integer, Boolean> {
        public String path;
        private ImageView imgView;

        public LoadImagen(String _path, ImageView _imgView) {
            path = _path;
            imgView = _imgView;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (path != null && !path.isEmpty()) {
                Glide.with(getActivity())
                        .load(path)
                        .centerCrop()
                        .error(R.drawable.image_no_available)
                        .placeholder(R.drawable.loading)
                        .into(imgView);
                System.gc();
            }
        }

        @Override
        protected void onPreExecute() {

        }
    }

    private class ClearImagen extends AsyncTask<String, Integer, Boolean> {
        private ImageView imgView;

        public ClearImagen(ImageView _imgView) {
            imgView = _imgView;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return true;
        }

        protected void onPostExecute(Boolean result) {
            Glide.with(getActivity())
                    .load(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .error(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.loading)
                    .into(imgView);
            System.gc();
        }

        @Override
        protected void onPreExecute() {

        }
    }

    private class CustomCourseYear {
        public int Id;
        public String CourseYearName;

        public CustomCourseYear(int Id, String courseYearName) {
            this.Id = Id;
            this.CourseYearName = courseYearName;
        }

        public List<CustomDivision> divisions = new ArrayList<>();
    }

    private class CustomDivision {
        public int Id;
        public String DivisionName;

        public CustomDivision(int Id, String divisionName) {
            this.Id = Id;
            this.DivisionName = divisionName;
        }
    }

    @NonNull
    private String createImagesFolder(String imagesDir) {
        // we will save images to 'myImages' folder on external storage
        File f = new File(imagesDir);
        if (!f.exists()) f.mkdirs();
        return imagesDir;
    }

    private void storeImage(String path, String imageName, @Nullable Image image) {
        if (image == null) {
            return;
        }

        String output = createImagesFolder(path);
        String filename = output + "/" + imageName + ".jpg";
        Bitmap bitmap = image.convertToBitmap();
        if (bitmap == null) {
            //Timber.e("%s: Bitmap is null", LOG_TAG);
            Log.d(TAG, "%s: Bitmap is null");
            return;
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
            boolean success = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            if (!success) {
                Log.d(TAG, "%s: Failed to compress bitmap!");
                //Timber.e("%s: Failed to compress bitmap!", LOG_TAG);
                try {
                    fos.close();
                } catch (IOException ignored) {
                } finally {
                    fos = null;
                }
                new File(filename);
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "%s: Failed to save image. Msg: %s");
            //Timber.e("%s: Failed to save image. Msg: %s", LOG_TAG, e.toString());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignored) {
                }
            }
        }
        // after this line, image gets disposed. If you want to save it
        // for later, you need to clone it with image.clone()
    }

    private void copyCountryBirth() {
        String tmp = "";
        new MaterialDialog.Builder(getActivity())
                .title("Copiar")
                .content("¿Es ARGENTINA el país de nacimiento?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    try {
                        ((EditText) rootView.findViewById(R.id.input_country_birth)).setText("Argentina");
                    } catch (Exception e) {

                    }
                    d.dismiss();
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }

    private void copyProvinceBirth() {
        String tmp = "";
        new MaterialDialog.Builder(getActivity())
                .title("Copiar")
                .content("¿Es Salta la provincia de nacimiento?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    try {
                        ((EditText) rootView.findViewById(R.id.input_country_birth)).setText("Argentina");
                        ((EditText) rootView.findViewById(R.id.input_province_birth)).setText("Salta");
                    } catch (Exception e) {

                    }
                    d.dismiss();
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }

    private void copyDepartmentBirth() {
        String tmp = "";
        new MaterialDialog.Builder(getActivity())
                .title("Copiar")
                .content("¿Es Capital (Salta) el departamento de nacimiento?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    try {
                        ((EditText) rootView.findViewById(R.id.input_country_birth)).setText("Argentina");
                        ((EditText) rootView.findViewById(R.id.input_province_birth)).setText("Salta");
                        ((EditText) rootView.findViewById(R.id.input_department_birth)).setText("Capital (Salta)");
                    } catch (Exception e) {

                    }
                    d.dismiss();
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }

    private void copyLocationBirth() {
        String tmp = "";
        new MaterialDialog.Builder(getActivity())
                .title("Copiar")
                .content("¿Es Salta Capital la localidad de nacimiento?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    try {
                        ((EditText) rootView.findViewById(R.id.input_country_birth)).setText("Argentina");
                        ((EditText) rootView.findViewById(R.id.input_province_birth)).setText("Salta");
                        ((EditText) rootView.findViewById(R.id.input_department_birth)).setText("Capital (Salta)");
                        ((EditText) rootView.findViewById(R.id.input_location_birth)).setText("Salta Capital");
                    } catch (Exception e) {

                    }
                    d.dismiss();
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }

    private void cleanStudentAddress() {
        String tmp = "";
        new MaterialDialog.Builder(getActivity())
                .title("Borrar")
                .content("¿Desea borrar el domicilio actual?")
                .positiveText("Si")
                .negativeText("No")
                .onPositive((d, which) -> {
                    try {
                        EditText et = ((EditText) rootView.findViewById(R.id.input_address));
                        et.setText("");
                        et.setFocusableInTouchMode(true);
                        et.requestFocus();
                        final InputMethodManager inputMethodManager = (InputMethodManager) context
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
                    } catch (Exception e) {

                    }
                    d.dismiss();
                })
                .onNegative((d, which) -> d.dismiss())
                .show();
    }
}