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

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    List<Book> bookDataList;
    LayoutInflater inflater;
    Context context;
    //  WishLisItemClick wishlistItemClick;
    WishlistCartItemClick  wishlistCartItemClick ;
    Boolean removeEnabled = true;



    public WishlistAdapter(List<Book> bookDataList, Context context){
        this.bookDataList = bookDataList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }


    public void setWishCartItemClick(WishlistCartItemClick wishlistCartItemClick) {
        this.wishlistCartItemClick = wishlistCartItemClick;
    }

    public void setRemoveEnabled(Boolean removeEnabled) {

        this.removeEnabled = removeEnabled;
    }



    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WishlistViewHolder(inflater.inflate(R.layout.book_wishlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        holder.WishProductNameTV.setText(bookDataList.get(position).getName());
        if (bookDataList.get(position).getDiscountPrice() == null || bookDataList.get(position).getDiscountPrice() == 0){
            holder.WishOldPriceTV.setVisibility(View.GONE);
            holder.WishDiscountPriceTV.setText("Rs." + bookDataList.get(position).getPrice() + "");
        } else
            holder.WishDiscountPriceTV.setText("Rs." + bookDataList.get(position).getDiscountPrice());
        holder.WishOldPriceTV.setText(bookDataList.get(position).getPrice() + "");



        Picasso.get().load(bookDataList.get(position).getImages().get(0)).into(holder.wishProductIV);
        holder.WishListMainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent productPage = new Intent(context, SingleBookActivity.class);
                System.out.println(bookDataList.get(holder.getAdapterPosition()).getImages());
                productPage.putExtra(SingleBookActivity.SINGLE_DATA_KEY, bookDataList.get(holder.getAdapterPosition()));
                context.startActivity(productPage);

            }
        });
    }

    @Override
    public int getItemCount() {
        return bookDataList.size();
    }

    public class WishlistViewHolder extends RecyclerView.ViewHolder {
        ImageView wishProductIV, WishRemoveIV;
        TextView WishProductNameTV, WishOldPriceTV, WishDiscountPriceTV;
        LinearLayout WishlistCartLL,WishListMainLL;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            wishProductIV = itemView.findViewById(R.id.bookIV);
            WishRemoveIV = itemView.findViewById(R.id.removeWishlistIV);
            WishProductNameTV = itemView.findViewById(R.id.bookNameTV);
            WishOldPriceTV = itemView.findViewById(R.id.oldPriceTV);
            WishDiscountPriceTV = itemView.findViewById(R.id.discountPriceTV);
//            WishlistCartLL = itemView.findViewById(R.id.addToCartLL);
            WishListMainLL = itemView.findViewById(R.id.singleBookLL);
        }
    }


    public interface WishlistCartItemClick {
        public void onRemoveCart(int position);
    }
}


