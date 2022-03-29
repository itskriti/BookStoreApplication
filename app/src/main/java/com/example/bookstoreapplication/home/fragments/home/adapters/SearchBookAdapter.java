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
    List<Book> booksList;
    List<Book> authorName;
    LayoutInflater layoutInflater;
    Context context;

    public SearchBookAdapter(List<Book> booksList, Context context) {
        this.booksList = booksList;
        authorName = new ArrayList<>(booksList);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Book> suggestions = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                suggestions.addAll(authorName);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Book item : authorName){
                    if(item.getName().toLowerCase().contains(filterPattern));
                    suggestions.add(item);
                }
            }

            FilterResults results = new FilterResults();
            results.values = suggestions;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            booksList.clear();
            booksList.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };

    @NonNull
    @Override
    public SearchBookAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(layoutInflater.inflate(R.layout.search_books, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchBookAdapter.SearchViewHolder holder, int position) {
        holder.bookNameSearch.setText(booksList.get(position).getName());
        holder.bookPriceSearch.setText("Rs. " + booksList.get(position).getDiscountPrice() + "");
        Picasso.get().load(booksList.get(position).getImages().get(0)).into(holder.bookImageSearch);

        holder.searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SingleBookActivity.class);
                intent.putExtra(SingleBookActivity.SINGLE_DATA_KEY, booksList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView bookNameSearch, bookPriceSearch;
        ImageView bookImageSearch;
        LinearLayout searchLayout;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImageSearch = itemView.findViewById(R.id.bookImageSearch);
            bookNameSearch = itemView.findViewById(R.id.bookNameSearch);
            bookPriceSearch = itemView.findViewById(R.id.bookPriceSearch);
            searchLayout = itemView.findViewById(R.id.searchLayout);
        }
    }
}
