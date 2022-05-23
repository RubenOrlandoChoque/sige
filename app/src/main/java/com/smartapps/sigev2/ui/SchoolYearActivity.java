package com.smartapps.sigev2.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.smartapps.sigev2.R;
import com.smartapps.sigev2.models.schoolyear.SchoolYear;
import com.smartapps.sigev2.models.schoolyear.SchoolYearViewModel;
import com.smartapps.sigev2.ui.schoolyear.SchoolYearAdapter;

import java.util.List;

public class SchoolYearActivity extends AppCompatActivity {

    private SchoolYearViewModel schoolYearViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_year);
        ((TextView) findViewById(R.id.txt_ab_title)).setText("Seleccionar Año Escolar");

        schoolYearViewModel = ViewModelProviders.of(this).get(SchoolYearViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final SchoolYearAdapter adapter = new SchoolYearAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        schoolYearViewModel.getAll().observe(SchoolYearActivity.this, new Observer<List<SchoolYear>>() {
            @Override
            public void onChanged(@Nullable List<SchoolYear> schoolYears) {
                adapter.setCourseYears(schoolYears);
                adapter.notifyDataSetChanged();
            }
        });
    }
}