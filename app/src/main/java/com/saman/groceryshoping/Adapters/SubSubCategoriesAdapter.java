package com.saman.groceryshopping.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.saman.groceryshopping.Models.CategoryModel;
import com.saman.groceryshopping.R;

import java.util.List;

public class SubSubCategoriesAdapter extends RecyclerView.Adapter<SubSubCategoriesAdapter.modelViewHolder> {
    Context context;
    List<CategoryModel> list;

    public SubSubCategoriesAdapter(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_category_layout, parent, false);
        modelViewHolder mvh = new modelViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class modelViewHolder extends RecyclerView.ViewHolder{
        CardView item;
        TextView name;
        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            item=itemView.findViewById(R.id.item_layout);
            name=itemView.findViewById(R.id.category_name);
        }
    }
}
