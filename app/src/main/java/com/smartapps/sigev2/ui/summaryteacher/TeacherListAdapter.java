package com.smartapps.sigev2.ui.summaryteacher;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.smartapps.sigev2.R;
import com.smartapps.sigev2.models.teacher.pojo.TeacherListData;
import com.smartapps.sigev2.ui.TeacherActivity;

import java.util.List;

public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.StudentsListHolder> {

    private final Context context;

    class StudentsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView courseYear;
        private final TextView inscriptos;

        ItemClickListener onItemClickListener;

        private StudentsListHolder(View itemView) {
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
    private List<TeacherListData> teacherListData;

    public TeacherListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public StudentsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new StudentsListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StudentsListHolder holder, int position) {
        TeacherListData current = teacherListData.get(position);
        holder.courseYear.setText(String.format("%s, %s", current.LastName, current.FirstName));
        holder.inscriptos.setText(current.DocumentNumber);
        holder.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                TeacherListData selected = teacherListData.get(position);
                Intent intent = new Intent(context, TeacherActivity.class);
                intent.putExtra("teacherId", selected.Id);
                context.startActivity(intent);
            }
        });
    }

    public void setTeacherListData(List<TeacherListData> teacherListData) {
        this.teacherListData = teacherListData;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (teacherListData != null)
            return teacherListData.size();
        else return 0;
    }

    interface ItemClickListener {
        void onClick(View view, int position);
    }
}


