package com.saman.groceryshopping.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.saman.groceryshopping.HelperClasses.ShareHelper;
import com.saman.groceryshopping.Models.ProductsModel;
import com.saman.groceryshopping.Models.ProductsModelDetails;
import com.saman.groceryshopping.ProductDetailActivity;
import com.saman.groceryshopping.ProductDetailsVeriationActivity;
import com.saman.groceryshopping.R;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.modelViewHolder> {
    List<ProductsModel> list;
    Context context;
    ShareHelper shareHelper;

    public GalleryAdapter(List<ProductsModel> list, Context context) {
        this.list = list;
        this.context = context;
        shareHelper = new ShareHelper(context);
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_gallery_image_layout, parent, false);
        modelViewHolder mvh = new modelViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int position) {
        final ProductsModel product = list.get(position);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean liked = false;
                if (product.isLike()) {
                    liked = true;
                }
                if (product.isVeriations()) {
                    Intent intent = new Intent(context.getApplicationContext(), ProductDetailsVeriationActivity.class);
                    intent.putExtra("product_id", String.valueOf(product.getmID()));
                    shareHelper.putKey("productdiscount", product.getDiscount());
                    shareHelper.putKey("productactual", product.getActualprice());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("liked", liked);
                    context.startActivity(intent);
                } else {

                    new ProductsModelDetails(product.getmID(), product.getmName(), product.getmPrice(), product.getmImage(), "home", "");
                    shareHelper.putKey("productid", String.valueOf(product.getmID()));
                    shareHelper.putKey("productname", product.getmName());
                    shareHelper.putKey("productprice", product.getmPrice());
                    shareHelper.putKey("productimage", product.getmImage());
                    shareHelper.putKey("productquantity", product.getQuantityUnit());
                    shareHelper.putKey("productdiscount", product.getDiscount());
                    shareHelper.putKey("productactual", product.getActualprice());

                    Intent intent = new Intent(context.getApplicationContext(), ProductDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("liked", liked);
                    context.startActivity(intent);
                }
            }
        });
        Picasso.get().load(list.get(position).getmImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
