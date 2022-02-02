package com.example.bookstoreapplication.checkout.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.response.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    List<Address> addressList;
    Context context;
    LayoutInflater inflater;
    OnAddressItemClickListener onAddressItemClickListener;


    public AddressAdapter(List<Address> addressList, Context context){
        this.addressList = addressList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressViewHolder(inflater.inflate(R.layout.item_address,parent, false));
    }

    public  void setOnAddressItemClickListener(OnAddressItemClickListener onAddressItemClickListener){
        this.onAddressItemClickListener = onAddressItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);
        holder.cityStreetTV.setText(address.getCity() + " " +address.getStreet());
        holder.provinceTV.setText((address.getProvince()));
        holder.decTV.setText(address.getDescription());
        holder.addressLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddressItemClickListener.onAddressClick(holder.getAdapterPosition(), addressList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder{
        TextView cityStreetTV, provinceTV, decTV;
        LinearLayout addressLL;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            cityStreetTV = itemView.findViewById(R.id.cityStreetAddTV);
            provinceTV = itemView.findViewById(R.id.provinceAddTV);
            decTV = itemView.findViewById(R.id.descAddTV);
            addressLL = itemView.findViewById(R.id.addressAddLL);
        }
    }

    public interface OnAddressItemClickListener {
        public void onAddressClick(int position, Address address);
    }


}

