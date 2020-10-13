package com.saman.groceryshopping.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.saman.groceryshopping.Models.CategoriesModel;
import com.saman.groceryshopping.R;

import java.util.ArrayList;
import java.util.List;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.modelViewHolder> {
    List<CategoriesModel> list;
    Context context;
    CategoriesModel model;
    List<List<CategoriesModel>> listChild;
    ArrayList<Integer> counter = new ArrayList<Integer>();

    public HomeCategoryAdapter(List<CategoriesModel> list, List<List<CategoriesModel>> listChild, Context context) {
        this.list = list;
        this.context = context;
        this.listChild = listChild;

        for (int i = 0; i < list.size(); i++) {
            counter.add(0);
        }
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_home_collapse_layout, parent, false);
        modelViewHolder mvh = new modelViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final modelViewHolder holder, final int position) {
        model = list.get(position);
        Picasso.get().load(model.getImage()).into(holder.itemImage);
        holder.name.setText(model.getName());
        holder.description.setText(model.getDiscription());
/*
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CategoriesActivity.class);
                intent.putExtra("cate_id",list.get(position).getId());
                intent.putExtra("cate_name",list.get(position).getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
*/

        CategoryAdapter adapter = new CategoryAdapter(context,listChild.get(position));
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (counter.get(position) % 2 == 0) {
                    holder.recyclerView.setVisibility(View.VISIBLE);
                } else {
                    holder.recyclerView.setVisibility(View.GONE);
                }

                counter.set(position, counter.get(position) + 1);


            }
        });
        holder.recyclerView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView name, description;
        RelativeLayout item;
        RecyclerView recyclerView;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.home_category_name);
            description = itemView.findViewById(R.id.home_category_discription);
            itemImage = itemView.findViewById(R.id.home_category_image);
            item = itemView.findViewById(R.id.item_layout);
            recyclerView = itemView.findViewById(R.id.inside_categoriesrecycler);
        }
    }
}
