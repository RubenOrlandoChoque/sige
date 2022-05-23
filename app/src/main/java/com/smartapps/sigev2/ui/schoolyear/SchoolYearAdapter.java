package com.smartapps.sigev2.ui.schoolyear;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.smartapps.sigev2.R;
import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.models.schoolyear.SchoolYear;
import com.smartapps.sigev2.ui.shift.ShiftActivity;

import java.util.List;

public class SchoolYearAdapter extends RecyclerView.Adapter<SchoolYearAdapter.SchoolYeaHolder> {

    private final Context context;

    class SchoolYeaHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView schoolYear;

        SchoolYearAdapter.ItemClickListener onItemClickListener;

        private SchoolYeaHolder(View itemView) {
            super(itemView);
            schoolYear = itemView.findViewById(R.id.school_year);
            itemView.setOnClickListener(this);
        }

        public void setOnItemClickListener(SchoolYearAdapter.ItemClickListener onItemClickListener) {
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
    private List<SchoolYear> schoolYears; // Cached copy of words

    public SchoolYearAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public SchoolYearAdapter.SchoolYeaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.school_year_item, parent, false);
        return new SchoolYearAdapter.SchoolYeaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SchoolYearAdapter.SchoolYeaHolder holder, int position) {
        SchoolYear current = schoolYears.get(position);
        int schoolYearStateId = current.getSchoolYearStateId();
        String text = "";
        if (schoolYearStateId == 2) {
            text = "Año Escolar ";
        } else if (schoolYearStateId == 4) {
            text = "Incripción Año Escolar ";
        }
        holder.schoolYear.setText(text  + current.getDescription());
        holder.setOnItemClickListener(new SchoolYearAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                SchoolYear selected = schoolYears.get(position);
                SharedConfig.setCurrentSchoolYearId(selected.getId());
                SharedConfig.setCurrentSchoolYear(selected.getDescription());
                Intent intent = new Intent(context, ShiftActivity.class);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    public void setCourseYears(List<SchoolYear> SchoolYears) {
        this.schoolYears = SchoolYears;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (schoolYears != null)
            return schoolYears.size();
        else return 0;
    }

    interface ItemClickListener {
        void onClick(View view, int position);
    }
}


