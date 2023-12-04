package com.example.prospector;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PointAdapter extends RecyclerView.Adapter<PointAdapter.PointViewHolder> {
    ArrayList<Point> points;

    public PointAdapter(ArrayList<Point> points) {
        this.points = points;
    }

    public class PointViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView ordView;
        TextView searchView;
        TextView madView;
        TextView indexView;
        TextView commentView;

        public PointViewHolder(View view) {
            super(view);
            ordView = (TextView) view.findViewById(R.id.ord_item);
            searchView = (TextView) view.findViewById(R.id.search_item);
            madView = (TextView) view.findViewById(R.id.mad_item);
            indexView = (TextView) view.findViewById(R.id.index_item);
            commentView = (TextView) view.findViewById(R.id.comment_item);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select the Action");
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Править");
            menu.add(this.getAdapterPosition(), v.getId(), 1, "Удалить");
        }

    }


    @NonNull
    @Override
    public PointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_list_item, parent, false);
        return new PointViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointViewHolder holder, int position) {
        holder.ordView.setText(points.get(position).getOrd());
        holder.searchView.setText(points.get(position).getSearch());
        holder.madView.setText(points.get(position).getMad());
        holder.indexView.setText(points.get(position).getIndex());
        holder.commentView.setText(points.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return points.size();
    }
}
