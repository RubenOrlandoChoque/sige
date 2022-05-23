package com.smartapps.sigev2.ui.studentlist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.smartapps.sigev2.R;
import com.smartapps.sigev2.models.students.StudentViewModel;
import com.smartapps.sigev2.models.students.pojo.StudentListData;
import com.smartapps.sigev2.ui.newstudent.StudentTutorActivity;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StudentsListAdapter extends RecyclerView.Adapter<StudentsListAdapter.StudentsListHolder> {

    private final Context context;
    private StudentViewModel studentViewModel;
    class StudentsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView courseYear;
        private final TextView inscriptos;

        StudentsListAdapter.ItemClickListener onItemClickListener;

        private StudentsListHolder(View itemView) {
            super(itemView);
            courseYear = itemView.findViewById(R.id.textView);
            inscriptos = itemView.findViewById(R.id.inscriptos);
            itemView.setOnClickListener(this);
        }

        private void setOnItemClickListener(StudentsListAdapter.ItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(view, getAdapterPosition());
            }
        }
    }

    private final LayoutInflater mInflater;
    private List<StudentListData> studentsCounts; // Cached copy of words

    public StudentsListAdapter(Context context, StudentViewModel studentViewModel) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.studentViewModel = studentViewModel;
    }

    @Override
    public StudentsListAdapter.StudentsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new StudentsListAdapter.StudentsListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StudentsListHolder holder, int position) {
        StudentListData current = studentsCounts.get(position);
        holder.courseYear.setText(String.format("%s, %s", current.LastName, current.FirstName));
        holder.inscriptos.setText(current.DocumentNumber);
        holder.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                StudentListData selected = studentsCounts.get(position);
                studentViewModel.createTempData(selected.Id, false).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<StudentViewModel.TempData>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(StudentViewModel.TempData tempData) {
                                Intent intent = new Intent(context, StudentTutorActivity.class);
                                intent.putExtra("studentId", selected.Id);
                                intent.putExtra("action", StudentTutorActivity.EDIT_STUDENT);
                                intent.putExtra("newStudent", tempData.newStudent);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("e", "e");
                            }
                        });
            }
        });
    }

    public void setCourseYears(List<StudentListData> studentsCounts) {
        this.studentsCounts = studentsCounts;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (studentsCounts != null)
            return studentsCounts.size();
        else return 0;
    }

    interface ItemClickListener {
        void onClick(View view, int position);
    }
}


