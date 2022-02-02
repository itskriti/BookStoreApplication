package com.example.bookstoreapplication.home.fragments.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.response.Category;
import com.example.bookstoreapplication.categoryPage.CategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {


    List<Category> categories;
    LayoutInflater inflater;
    Context context;
    boolean showImage;



    public CategoryAdapter(List<Category> categories, Context context, Boolean showImage){
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.showImage = showImage;

    }

    //inflate layout of book category
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(showImage)
            return new CategoryViewHolder(inflater.inflate(R.layout.book_category, parent, false));
        else
            return new CategoryViewHolder(inflater.inflate(R.layout.item_category_no_image, parent, false));
    }


    //set elements from create view holder
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.catNameTV.setText(categories.get(position).getName());
        Picasso.get().load(categories.get(position).getCategoryImage()).into(holder.catIV);
        holder.categoryItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra(CategoryActivity.CATEGORY_DATA_KEY, categories.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    } //storing size of list

    class CategoryViewHolder extends RecyclerView.ViewHolder{
        CircleImageView catIV;
        TextView catNameTV;
        LinearLayout categoryItemLL;


        //constructor
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            catIV = itemView.findViewById(R.id.catIV);
            catNameTV = itemView.findViewById(R.id.catNameTV);
            categoryItemLL = itemView.findViewById(R.id.categoryItemLL);



        }
    }
}
