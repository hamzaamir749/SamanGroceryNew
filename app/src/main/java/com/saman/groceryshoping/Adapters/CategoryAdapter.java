package com.saman.groceryshopping.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.saman.groceryshopping.Categories.SubCategoriesActivity;
import com.saman.groceryshopping.Models.CategoriesModel;
import com.saman.groceryshopping.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.modelViewHolder> {
    Context context;
    List<CategoriesModel> list;

    public CategoryAdapter(Context context, List<CategoriesModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_home_sub_categories_layout, parent, false);
        modelViewHolder mvh = new modelViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        Picasso.get().load(list.get(position).getImage()).into(holder.imageView);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SubCategoriesActivity.class);
                intent.putExtra("cate_id",list.get(position).getId());
                intent.putExtra("cate_name",list.get(position).getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        LinearLayout item;
        TextView name;
        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            item=itemView.findViewById(R.id.item_layout);
            name=itemView.findViewById(R.id.category_name);
            imageView=itemView.findViewById(R.id.category_image);
        }
    }
}
