package com.example.bookstoreapplication.home.fragments.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.response.Book;
import com.example.bookstoreapplication.home.fragments.home.SearchActivity;
import com.example.bookstoreapplication.singleBookPage.SingleBookActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchBookAdapter extends RecyclerView.Adapter<SearchBookAdapter.SearchViewHolder> implements Filterable {
    List<Book> bookListFull;
    List<Book> searchData;
    LayoutInflater layoutInflater;
    Context context;

    public SearchBookAdapter(List<Book> bookListFull, Context context ){
        this.bookListFull = bookListFull;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        searchData = new ArrayList<>(bookListFull);
    }

    @NonNull
    @Override
    public SearchBookAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(layoutInflater.inflate(R.layout.search_books, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchBookAdapter.SearchViewHolder holder, int position) {
        holder.SearchProductNameTV.setText(bookListFull.get(position).getName());
        holder.searchANameTV.setText(bookListFull.get(position).getAuthorName());
        holder.SearchOldPriceTV.setText("Rs. " +bookListFull.get(position).getPrice()+ "");
        holder.SearchDiscountPriceTV.setText("Rs. " + bookListFull.get(position).getDiscountPrice() +"");
        Picasso.get().load(bookListFull.get(position).getImages().get(0)).into(holder.SearchProductIV);

        holder.SearchViewLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SingleBookActivity.class);
                intent.putExtra(SingleBookActivity.key, bookListFull.get(holder.getAdapterPosition()));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        if(bookListFull != null){
            return bookListFull.size();
        }
        return 0;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Book> suggestions = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                suggestions.addAll(searchData);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Book item : searchData){
                    if(item.getName().toLowerCase().contains(filterPattern)
                            || item.getAuthorName().toLowerCase().contains(filterPattern)){
                        suggestions.add(item);

                    }

                }
            }
            FilterResults results = new FilterResults();
            results.values = suggestions;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            bookListFull.clear();
            bookListFull.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

    };




    public class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView SearchProductIV;
        TextView SearchProductNameTV, SearchOldPriceTV, SearchDiscountPriceTV, searchANameTV;
        LinearLayout SearchViewLL;


        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            SearchProductIV = itemView.findViewById(R.id.bookImageSearch);
            SearchOldPriceTV = itemView.findViewById(R.id.bookPriceSearch);
            SearchDiscountPriceTV = itemView.findViewById(R.id.SearchDiscountPriceTV);
            SearchProductNameTV = itemView.findViewById(R.id.bookNameSearch);
            searchANameTV = itemView.findViewById(R.id.searchANameTV);
            SearchViewLL = itemView.findViewById(R.id.searchLayout);
        }
    }
}
