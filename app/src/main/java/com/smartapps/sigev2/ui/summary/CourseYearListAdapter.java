package com.smartapps.sigev2.ui.summary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.smartapps.sigev2.R;
import com.smartapps.sigev2.models.courseyear.pojo.CourseYearStudentsCount;
import com.smartapps.sigev2.ui.studentlist.StudentsListByCourseActivity;

import java.util.List;

public class CourseYearListAdapter extends RecyclerView.Adapter<CourseYearListAdapter.CourseYearListHolder> {

    private final Context context;

    class CourseYearListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView courseYear;
        private final TextView inscriptos;

        ItemClickListener onItemClickListener;

        private CourseYearListHolder(View itemView) {
            super(itemView);
            courseYear = itemView.findViewById(R.id.textView);
            inscriptos = itemView.findViewById(R.id.inscriptos);
            itemView.setOnClickListener(this);
        }

        public void setOnItemClickListener(ItemClickListener onItemClickListener) {
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
    private List<CourseYearStudentsCount> courseYearStudentsCounts; // Cached copy of words

    public CourseYearListAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public CourseYearListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new CourseYearListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseYearListHolder holder, int position) {
        CourseYearStudentsCount current = courseYearStudentsCounts.get(position);
        holder.courseYear.setText(current.CourseYearName);
        holder.inscriptos.setText(String.format("Inscriptos: %d", current.Cant));
        holder.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                CourseYearStudentsCount selected = courseYearStudentsCounts.get(position);
//                if (selected.Cant == 0) {
//                    new MaterialDialog.Builder(context)
//                            .title("Aviso")
//                            .content("No hay alumnos en este año")
//                            .positiveText("Aceptar")
//                            .onPositive((d, which) -> {
//                                d.dismiss();
//                            })
//                            .show();
//                } else {
                Intent intent = new Intent(context, StudentsListByCourseActivity.class);
                intent.putExtra("courseYearId", selected.CourseYearId);
                intent.putExtra("courseYearName", selected.CourseYearName);
                context.startActivity(intent);
//                }
            }
        });
    }

    public void setCourseYears(List<CourseYearStudentsCount> courseYearStudentsCounts) {
        this.courseYearStudentsCounts = courseYearStudentsCounts;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (courseYearStudentsCounts != null)
            return courseYearStudentsCounts.size();
        else return 0;
    }

    interface ItemClickListener {
        void onClick(View view, int position);
    }
}


