package com.example.localtasker.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localtasker.Constants;
import com.example.localtasker.R;
import com.example.localtasker.admin.FragmentCatCRUD;
import com.example.localtasker.models.TaskCat;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAllTasksCat extends RecyclerView.Adapter<AdapterAllTasksCat.Holder> {

    private Context context;
    private List<TaskCat> taskCatList;

    public AdapterAllTasksCat(Context context, List<TaskCat> taskCatList) {
        this.context = context;
        this.taskCatList = taskCatList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_cat, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        final TaskCat cat = taskCatList.get(holder.getAdapterPosition());

        if (cat.getCategoryImageUrl() != null && !cat.getCategoryImageUrl().equals("") && !cat.getCategoryImageUrl().equals("null"))
            Picasso.get().load(cat.getCategoryImageUrl()).placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background).centerInside().fit().into(holder.serviceImage);

        holder.serviceName.setText(cat.getCategoryName());

        holder.cardServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.STRING_TASKS_CAT_OBJ, cat);

                FragmentCatCRUD fragmentCatCRUD = FragmentCatCRUD.getInstance();
                fragmentCatCRUD.setArguments(bundle);

                fragmentCatCRUD.show(((FragmentActivity) context).getSupportFragmentManager(), "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskCatList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        CardView cardServices;
        ImageView serviceImage;
        TextView serviceName;

        public Holder(@NonNull View itemView) {
            super(itemView);

            cardServices = itemView.findViewById(R.id.cardServices);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            serviceName = itemView.findViewById(R.id.serviceName);

        }
    }
}
