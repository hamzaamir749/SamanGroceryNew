package com.saman.groceryshopping.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.saman.groceryshopping.Categories.SubCategoriesActivity;
import com.saman.groceryshopping.Fragments.CartFragment;
import com.saman.groceryshopping.Fragments.Fragment_yourBill;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.URLHelper;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.MainActivity;
import com.saman.groceryshopping.Models.ProductsModel;
import com.saman.groceryshopping.R;
import com.saman.groceryshopping.Utils.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.modelViewHolder> {

    List<ProductsModel> list;
    Context context;
    MainActivity home;
    SubCategoriesActivity subCategoriesActivity;
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;
    public CartAdapter(List<ProductsModel> list, Context context) {
        this.list = list;
        home=new MainActivity();
        subCategoriesActivity=new SubCategoriesActivity();
        this.context = context;
        dialog = new LoadingDialog(context);
        userSessionManager=new UserSessionManager(context);
        session=userSessionManager.getSessionDetails();
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_checkout_list, parent, false);
        modelViewHolder mvh = new modelViewHolder(view);
        return mvh;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(@NonNull final modelViewHolder holder, int position) {
        final ProductsModel product = list.get(position);
        final String price = product.getmPrice();
        if (UserSessionManager.cart != null) {
            int quantity = UserSessionManager.cart.getItemQuantity(product.getmID());
            if (quantity != 0) {
                holder.total.setText(String.valueOf(UserSessionManager.cart.getItemQuantity(product.getmID())));
                holder.plusminusRelative.setVisibility(View.VISIBLE);
                holder.addCart.setVisibility(View.GONE);
            } else {
                holder.plusminusRelative.setVisibility(View.GONE);
                holder.addCart.setVisibility(View.VISIBLE);
            }
        }
        holder.itemquantity.setText(product.getQuantityUnit());
        holder.itemprice.setText("RS " + product.getmPrice());
        holder.itemname.setText(product.getmName());
        Picasso.get().load(product.getmImage()).into(holder.itemImage);
        if (product.isLike()) {
            holder.fev.setImageResource(R.drawable.animated_heart);
        }
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSessionManager.cart.removeFromCart(product);
                list.remove(product);
                new CartFragment().setTotalPrice();
                notifyDataSetChanged();
                if (UserSessionManager.cart.getTotalPrice() == 0) {
                    new CartFragment().checkLayout.setVisibility(View.GONE);
                    new Fragment_yourBill().linearLayout.setVisibility(View.VISIBLE);
                }
                home.ShowBadge(UserSessionManager.cart.getTotalItems());

            }
        });
        holder.fev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.ADD_FEV, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("loginres", response);
                        try {
                            JSONObject object;
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            if (status) {
                                holder.fev.setImageResource(R.drawable.animated_heart);
                                holder.fev.setClickable(false);
                            } else {
                                Toast.makeText(context, "Go to Favorites to  remove Item.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("loginexception", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("loginerror", error.toString());
                        dialog.dismiss();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("user_id", session.getId());
                        map.put("product_id", String.valueOf(product.getmID()));
                        return map;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.getCache().clear();
                queue.add(stringRequest);
            }
        });
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qty = (int) product.getQuantity();
                qty++;
                UserSessionManager.cart.addToCart(product);
                holder.total.setText(String.valueOf(qty));
                new CartFragment().setTotalPrice();
                if (!holder.btnMinus.isEnabled()) {
                    holder.btnMinus.setEnabled(true);
                }
                home.ShowBadge(UserSessionManager.cart.getTotalItems());
               //home.ShowBadge(UserSessionManager.cart.getTotalItems());
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = (int) product.getQuantity();
                qty--;
                if (qty > 0) {
                    UserSessionManager.cart.removeFromCart(product);
                    holder.total.setText(String.valueOf(qty));
                    new CartFragment().setTotalPrice();
                    if (qty < 2) {
                        holder.btnMinus.setEnabled(false);
                    }
                }
                home.ShowBadge(UserSessionManager.cart.getTotalItems());
              //  subCategoriesActivity.ShowBadge(UserSessionManager.cart.getTotalItems());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage, btnPlus, btnMinus, fev, cancel;
        TextView itemname, itemquantity, itemprice, total;
        Button addCart;
        RelativeLayout plusminusRelative;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            cancel = itemView.findViewById(R.id.home_feature_cancel);
            itemImage = itemView.findViewById(R.id.home_feature_image);
            btnPlus = itemView.findViewById(R.id.home_feature_add);
            btnMinus = itemView.findViewById(R.id.home_feature_minus);
            fev = itemView.findViewById(R.id.home_feature_fev);
            itemname = itemView.findViewById(R.id.home_feature_name);
            itemquantity = itemView.findViewById(R.id.home_feature_quantity);
            itemprice = itemView.findViewById(R.id.home_feature_price);
            addCart = itemView.findViewById(R.id.home_feature_addcart);
            plusminusRelative = itemView.findViewById(R.id.home_feature_plusminusView);
            total = itemView.findViewById(R.id.home_feature_total);
        }
    }
}
