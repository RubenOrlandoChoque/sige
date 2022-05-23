package com.smartapps.sigev2.ui.summaryteacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.models.teacher.TeacherViewModel;
import com.smartapps.sigev2.models.teacher.pojo.TeacherListData;
import com.smartapps.sigev2.ui.TeacherActivity;

import java.util.List;

public class TeacherListActivity extends AppCompatActivity implements View.OnClickListener {
    private TeacherViewModel teacherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);


        RecyclerView recyclerView = findViewById(R.id.recyclerview_teacher_list);
        final TeacherListAdapter adapter = new TeacherListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.newTeacher).setOnClickListener(this);

        teacherViewModel = ViewModelProviders.of(this).get(TeacherViewModel.class);
        teacherViewModel.getTeacherList(SharedConfig.getShiftId()).observe(TeacherListActivity.this, new Observer<List<TeacherListData>>() {
            @Override
            public void onChanged(@Nullable List<TeacherListData> teacherListData) {
                adapter.setTeacherListData(teacherListData);
                adapter.notifyDataSetChanged();
                if (teacherListData.size() > 0) {
                    findViewById(R.id.recyclerview_teacher_list).setVisibility(View.VISIBLE);
                    findViewById(R.id.empty_view).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.recyclerview_teacher_list).setVisibility(View.GONE);
                    findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newTeacher: {
                Intent intent = new Intent(TeacherListActivity.this, TeacherActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
