package com.smartapps.sigev2.ui.newstudent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.NonSwipeableViewPager;
import com.smartapps.sigev2.models.relationship.RelationshipViewModel;
import com.smartapps.sigev2.models.students.pojo.StudentData;
import com.smartapps.sigev2.models.tutor.TutorViewModel;
import com.smartapps.sigev2.models.tutor.pojo.TutorData;

import java.util.List;

public class TutorsListFragment extends Fragment {
    private TutorViewModel tutorViewModel;
    private RelationshipViewModel relationshipViewModel;
    View rootView;
    RecyclerView tutorsList;
    LiveData<List<TutorData>> studentTutors;
    ViewGroup container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.container = container;
        rootView = inflater.inflate(R.layout.fragment_tutors_list, container, false);
        tutorViewModel = ViewModelProviders.of(this).get(TutorViewModel.class);
        relationshipViewModel = ViewModelProviders.of(this).get(RelationshipViewModel.class);


        TutorsListAdapter tutorsListAdapter = new TutorsListAdapter(getLayoutInflater(), tutorData -> {
            Intent intent = new Intent(getContext(), TutorActivity.class);
            intent.putExtra("studentId", ((NonSwipeableViewPager) container).getStudentId());
            intent.putExtra("tutorId", tutorData.getId());

            try {
                Fragment page = this.getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":0");
                if (page instanceof StudentFragment) {
                    StudentData studentData = ((StudentFragment) page).getData();
                    if(studentData.getAnotherContactPhone() != null){
                        intent.putExtra("studentAddress", studentData.getAddress());
                        intent.putExtra("studentLocation", studentData.getLocation());
                        intent.putExtra("studentPhoneNumber", studentData.getPhoneNumber());
                        intent.putExtra("studentMobilePhoneNumber", studentData.getMobilePhoneNumber());
                        intent.putExtra("studentAnotherPhoneNumber", studentData.getAnotherContactPhone());
                    }
                }
            }catch (Exception e){

            }
            getActivity().startActivity(intent);
        });
        tutorsList = rootView.findViewById(R.id.tutors_list);
        tutorsList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        tutorsList.setAdapter(tutorsListAdapter);

        relationshipViewModel.getAll().observe(this, relationships -> {
            if (relationships != null && relationships.size() > 0) {
                tutorsListAdapter.setRelationShips(relationships);
                tutorsListAdapter.notifyDataSetChanged();
            }
        });

        studentTutors = tutorViewModel.getStudentTutors(((NonSwipeableViewPager) container).getStudentId());
        studentTutors.observe(this, tutorData -> {
            ((TutorsListAdapter) tutorsList.getAdapter()).setItems(tutorData);
            rootView.findViewById(R.id.no_tutors).setVisibility(tutorData != null && tutorData.size() > 0 ? View.GONE : View.VISIBLE);
        });

        rootView.findViewById(R.id.add_tutor).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TutorActivity.class);
            intent.putExtra("studentId", ((NonSwipeableViewPager) container).getStudentId());
            try {
                Fragment page = this.getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":0");
                if (page instanceof StudentFragment) {
                    StudentData studentData = ((StudentFragment) page).getData();
                    if(studentData.getAnotherContactPhone() != null){
                        intent.putExtra("studentAddress", studentData.getAddress());
                        intent.putExtra("studentLocation", studentData.getLocation());
                        intent.putExtra("studentPhoneNumber", studentData.getPhoneNumber());
                        intent.putExtra("studentMobilePhoneNumber", studentData.getMobilePhoneNumber());
                        intent.putExtra("studentAnotherPhoneNumber", studentData.getAnotherContactPhone());
                    }
                }
            }catch (Exception e){

            }
            getActivity().startActivity(intent);
        });

        return rootView;
    }

    public void resetData() {
        studentTutors.removeObservers(this);
        studentTutors = tutorViewModel.getStudentTutors(((NonSwipeableViewPager) container).getStudentId());
        studentTutors.observe(this, tutorData -> {
            ((TutorsListAdapter) tutorsList.getAdapter()).setItems(tutorData);
            rootView.findViewById(R.id.no_tutors).setVisibility(tutorData != null && tutorData.size() > 0 ? View.GONE : View.VISIBLE);
        });
    }
}
