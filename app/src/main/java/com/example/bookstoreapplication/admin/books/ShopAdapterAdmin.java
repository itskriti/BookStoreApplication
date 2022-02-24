package com.example.bookstoreapplication.admin.books;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShopAdapterAdmin extends RecyclerView.Adapter<ShopAdapterAdmin.ShopViewHolder> {
    List<Book> bookDataList;
    LayoutInflater layoutInflater;
    Context context;

    public  ShopAdapterAdmin(List<Book> bookDataList, Context context){
        this.bookDataList = bookDataList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShopViewHolder(layoutInflater.inflate(R.layout.book_list_admin, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        holder.bookNameTV.setText(bookDataList.get(position).getName());
        if(bookDataList.get(position).getDiscountPrice() == null || bookDataList.get(position).getDiscountPrice() == 0){
            holder.priceTV.setVisibility(View.GONE);
            holder.discountPriceTV.setText("Rs. " + bookDataList.get(position).getPrice() + "");

        } else
            holder.discountPriceTV.setText("Rs. " + bookDataList.get(position).getDiscountPrice() + "");
        holder.priceTV.setText(bookDataList.get(position).getPrice() + "");
        holder.quantityTV.setText(bookDataList.get(position).getQuantity() + "Units");
        Picasso.get().load(bookDataList.get(position).getImages().get(0)).into(holder.bookIV);
        holder.mainLL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public int getItemCount() {
        return bookDataList.size();
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder{
        ImageView bookIV;
        LinearLayout mainLL;
        TextView bookNameTV, authorTV, discountPriceTV, priceTV, quantityTV;


        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            bookIV = itemView.findViewById(R.id.bookIV);
            bookNameTV = itemView.findViewById(R.id.bookNameTV);
            authorTV = itemView.findViewById(R.id.authorTV);
            mainLL = itemView.findViewById(R.id.singleBookLL);
            priceTV = itemView.findViewById(R.id.oldPriceTV);
            discountPriceTV = itemView.findViewById(R.id.discountPriceTV);
            quantityTV = itemView.findViewById(R.id.quantityTV);
        }
    }
}
