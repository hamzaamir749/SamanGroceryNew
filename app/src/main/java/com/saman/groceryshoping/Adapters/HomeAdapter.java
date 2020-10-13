package com.saman.groceryshopping.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.saman.groceryshopping.HelperClasses.Cart;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.ShareHelper;
import com.saman.groceryshopping.HelperClasses.URLHelper;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.MainActivity;
import com.saman.groceryshopping.Models.ProductsModel;
import com.saman.groceryshopping.Models.ProductsModelDetails;
import com.saman.groceryshopping.ProductDetailActivity;
import com.saman.groceryshopping.ProductDetailsVeriationActivity;
import com.saman.groceryshopping.R;
import com.saman.groceryshopping.Utils.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.modelViewHolder> {

    List<ProductsModel> list;
    Context context;
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;
    ShareHelper shareHelper;
    MainActivity home;

    public HomeAdapter(List<ProductsModel> list, Context context) {
        this.list = list;
        this.context = context;
        dialog = new LoadingDialog(context);
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
        shareHelper = new ShareHelper(context);
        home = new MainActivity();
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_feature_product, parent, false);
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
        if (product.isVeriations()) {
            holder.addCart.setText("View Details");
        } else {
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
        }
        if (product.getActivitytype().equals("fev")){
            holder.cancel.setVisibility(View.VISIBLE);
        }
        if (product.isLike()) {
            holder.fev.setImageResource(R.drawable.animated_heart);
        }
        if (product.getActualprice().equals("")) {
            holder.actual.setVisibility(View.GONE);
        } else {
            holder.actual.setText("RS " + product.getActualprice());
            holder.actual.setVisibility(View.VISIBLE);
            holder.actual.setPaintFlags(holder.actual.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (product.getDiscount().equals("")) {
            holder.discount.setVisibility(View.INVISIBLE);
        } else {
            holder.discount.setText(product.getDiscount() + " off");
        }
        holder.itemquantity.setText(product.getQuantityUnit());
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.DELETE_FEV, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("loginres", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            if (status) {
                                list.remove(product);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, "Item Already have been added to the list", Toast.LENGTH_SHORT).show();
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
        holder.itemprice.setText("RS " + product.getmPrice());
        holder.itemname.setText(product.getmName());
        Picasso.get().load(product.getmImage()).into(holder.itemImage);
        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.isVeriations()) {
                    Intent intent = new Intent(context.getApplicationContext(), ProductDetailsVeriationActivity.class);
                    intent.putExtra("product_id", String.valueOf(product.getmID()));
                    shareHelper.putKey("productdiscount", product.getmImage());
                    shareHelper.putKey("productactual", product.getActualprice());
                    context.startActivity(intent);

                } else {
                    Log.d("class", product.getmName());
                    product.setQuantity(1);
                    product.setPrice(Double.valueOf(price));
                    addToCart(product);
                    home.ShowBadge(UserSessionManager.cart.getTotalItems());
                    holder.total.setText(String.valueOf(UserSessionManager.cart.getItemQuantity(product.getmID())));
                    holder.plusminusRelative.setVisibility(View.VISIBLE);
                    holder.addCart.setVisibility(View.GONE);
                }
            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    shareHelper.putKey("productdiscount", product.getmImage());
                    shareHelper.putKey("productactual", product.getActualprice());

                    Intent intent = new Intent(context.getApplicationContext(), ProductDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("liked", liked);
                    context.startActivity(intent);
                }
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
                addToCart(product);
                product.setPrice(Double.valueOf(price));
                product.setQuantity(UserSessionManager.cart.getItemQuantity(product.getmID()));
                int qty = (int) product.getQuantity();
                holder.total.setText(String.valueOf(qty));
                home.ShowBadge(UserSessionManager.cart.getTotalItems());
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSessionManager.cart.removeFromCart(product);
                int qty = (int) UserSessionManager.cart.getItemQuantity(product.getmID());
                if (UserSessionManager.cart.getTotalItems() == 0) {
                    //    UserSessionManager.cart.removeBadge();
                    home.ShowBadge(0);
                    UserSessionManager.cart.productList.clear();
                    UserSessionManager.cart = null;
                } else {
                    //  UserSessionManager.cart.setBadge(UserSessionManager.cart.getTotalItems() + "");
                }

                if (qty == 0) {
                    holder.plusminusRelative.setVisibility(View.GONE);
                    holder.addCart.setVisibility(View.VISIBLE);
                    return;
                }
                product.setPrice(Double.valueOf(price));
                holder.total.setText("" + qty);
                home.ShowBadge(UserSessionManager.cart.getTotalItems());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void addToCart(ProductsModel product) {
        if (UserSessionManager.cart == null) {
            UserSessionManager.cart = new Cart(context);
        }
        UserSessionManager.cart.addToCart(product);

        int totalItems = UserSessionManager.cart.getTotalItems();
        UserSessionManager.cart.setBadge("" + totalItems);
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage, btnPlus, btnMinus, fev, cancel;
        TextView itemname, itemquantity, itemprice, total, actual, discount;
        Button addCart;
        RelativeLayout plusminusRelative;
        CardView item;
        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            item=itemView.findViewById(R.id.home_feature_item);
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
            actual = itemView.findViewById(R.id.home_feature_actual);
            discount = itemView.findViewById(R.id.home_feature_off);
        }
    }
}
