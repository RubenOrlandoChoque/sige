package com.smartapps.sigev2.ui.newstudent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.smartapps.sigev2.R;
import com.smartapps.sigev2.models.relationship.pojo.RelationshipData;
import com.smartapps.sigev2.models.tutor.pojo.TutorData;

import java.util.ArrayList;
import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;

public class TutorsListAdapter extends RecyclerView.Adapter<TutorsListAdapter.ViewHolder> {
    LayoutInflater layoutInflater;
    List<TutorData> tutorList = new ArrayList<>();
    private List<RelationshipData> relationships = new ArrayList<>();
    private OnTutorListener onTutorListener;

    public TutorsListAdapter(LayoutInflater layoutInflater, OnTutorListener onTutorListener) {
        this.layoutInflater = layoutInflater;
        this.onTutorListener = onTutorListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_tutor, parent, false);
        return new TutorsListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TutorData tutor = tutorList.get(position);

        holder.txtNameTutor.setText(tutor.getLastName() + ", " + tutor.getFirstName());
        holder.txtDocument.setText(tutor.getDocumentNumber());
        RelationshipData relationshipData = stream(relationships).where(r -> r.getId() == tutor.getRelationshipId()).firstOrNull();
        if (relationshipData != null) {
            holder.txtRelationship.setText(relationshipData.getDescription());
        }else{
            holder.txtRelationship.setText("No especificado");
        }
        holder.cardEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTutorListener.onItemClick(tutor);
            }
        });
    }

    public void setItems(List<TutorData> tutorList) {
        this.tutorList = tutorList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public void setRelationShips(List<RelationshipData> relationships) {
        this.relationships = relationships;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNameTutor;
        public TextView txtRelationship;
        public TextView txtDocument;
        public RelativeLayout containerRelationship;
        public CardView cardEvents;

        ViewHolder(View view) {
            super(view);
            this.txtNameTutor = view.findViewById(R.id.txt_name_person);
            this.cardEvents = view.findViewById(R.id.card_events);
            this.txtRelationship = view.findViewById(R.id.txt_relationship);
            this.txtDocument = view.findViewById(R.id.txt_document);
            this.containerRelationship = view.findViewById(R.id.container_relationship);
        }
    }

    public interface OnTutorListener{
        void onItemClick(TutorData tutorData);
    }
}
