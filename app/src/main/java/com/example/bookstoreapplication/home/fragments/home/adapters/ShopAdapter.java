package com.example.bookstoreapplication.home.fragments.home.adapters;

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

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.response.Book;
import com.example.bookstoreapplication.singleBookPage.SingleBookActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {
    List<Book> bookDataList;
    LayoutInflater layoutInflater;
    Context context;
    Boolean isCart = false;
    Boolean removeEnabled = true;
    CartItemClick cartItemClick;
//    WishlistItemClick wishlistItemClick;
    Boolean isWishlist = false;

    public ShopAdapter(List<Book> bookDataList, Context context, Boolean isCart, Boolean isWishlist){
        this.bookDataList = bookDataList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.isCart = isCart;
        this.isWishlist = isWishlist;
    }

    public  void setCartItemClick(CartItemClick cartItemClick){
     this.cartItemClick = cartItemClick;
    }
//
//    public void setWishlistItemClick(WishlistItemClick wishlistItemClick){
//        this.wishlistItemClick = wishlistItemClick;
//    }


    public interface CartItemClick {
        public void onRemoveCart(int position);
    }

//    public interface WishlistItemClick {
//        public void onRemoveWishlist(int position);
//    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(isCart)
            return new ShopViewHolder(layoutInflater.inflate(R.layout.book_cart, parent, false));
//        else if(isWishlist)
//            return new ShopViewHolder(layoutInflater.inflate(R.layout.book_wishlist, parent, false));
        else
            return new ShopViewHolder(layoutInflater.inflate(R.layout.book_list, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ShopViewHolder holder, int position) {
        holder.nameTV.setText(bookDataList.get(position).getName());
        if (bookDataList.get(position).getDiscountPrice() == null || bookDataList.get(position).getDiscountPrice() == 0){
            holder.priceTV.setVisibility(View.GONE);
            holder.discountPrice.setText("Rs. "+bookDataList.get(position).getPrice() + "");
        }
        else
            holder.discountPrice.setText("Rs. "+bookDataList.get(position).getDiscountPrice()+"");
        holder.priceTV.setText(bookDataList.get(position).getPrice()+"");

        Picasso.get().load(bookDataList.get(position).getImages().get(0)).into(holder.bookIV);
        holder.singleBookLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookPage = new Intent(context, SingleBookActivity.class);
                bookPage.putExtra(SingleBookActivity.key, bookDataList.get(holder.getAdapterPosition()));
                context.startActivity(bookPage);
            }
        });

        if(isCart){
            if(removeEnabled)
                holder.removeCartIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cartItemClick.onRemoveCart(holder.getAdapterPosition());

                    }
                });
            else {
                holder.removeCartIV.setVisibility(View.GONE);
                holder.singleBookLL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                setMargins(holder.singleBookLL, 0, 0, 16, 0);
            }
            holder.quantityTV.setText(bookDataList.get(position).getCartQuantity() + "");
        }

//        if (isWishlist) {
//            if (removeEnabled)
//                holder.removeCartIV.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        wishlistItemClick.onRemoveWishlist(holder.getAdapterPosition());
//                    }
//                });
//            else {
//                holder.removeCartIV.setVisibility(View.GONE);
//                holder.singleBookLL.setLayoutParams( new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
//                setMargins(holder.singleBookLL,0,0,16,0);
//            }
//            holder.quantityTV.setText(bookDataList.get(position).getCartQuantity() + "");
//        }
    }

    public static void setMargins(View v, int l , int t, int r, int b){
        if(v.getLayoutParams() instanceof  ViewGroup.MarginLayoutParams){
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    @Override
    public int getItemCount() {
        return bookDataList.size();
    }

    public void setRemoveEnabled(boolean removeEnabled) {
        this.removeEnabled = removeEnabled;
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder{
        ImageView bookIV, removeCartIV, removeWishlistIV;
        TextView nameTV, authorTV, priceTV, discountPrice, quantityTV;
        LinearLayout singleBookLL;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            bookIV = itemView.findViewById(R.id.bookIV);
            nameTV = itemView.findViewById(R.id.bookNameTV);
            authorTV = itemView.findViewById(R.id.authorTV);
            singleBookLL = itemView.findViewById(R.id.singleBookLL);
            priceTV = itemView.findViewById(R.id.oldPriceTV);
            discountPrice = itemView.findViewById(R.id.discountPriceTV);
            if(isCart){
                removeCartIV = itemView.findViewById(R.id.removeCartIV);
                quantityTV = itemView.findViewById(R.id.quantityTV);
            }

//            if(isWishlist) {
//                removeWishlistIV = itemView.findViewById(R.id.removeWishlistIV);
//                quantityTV = itemView.findViewById(R.id.quantityTV);
//            }
        }
    }
}
