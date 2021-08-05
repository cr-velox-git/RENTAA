package com.silverphoenix.rentaa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class propertyViewAdapter extends RecyclerView.Adapter<propertyViewAdapter.viewHolder> {

    private List<propertyViewModel> propertyViewModelList;
    private Context context;

    public propertyViewAdapter(List<propertyViewModel> propertyViewModelList, Context context) {
        this.propertyViewModelList = propertyViewModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.setData(propertyViewModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return propertyViewModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView cityFlat, address, price, installment, des, postedOn;
        private ImageView imageView;
        private Button btn;
        private FloatingActionButton floatingActionButton;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView24);
            cityFlat = itemView.findViewById(R.id.textView44);
            address = itemView.findViewById(R.id.textView45);
            price = itemView.findViewById(R.id.textView46);
            installment = itemView.findViewById(R.id.textView48);
            des = itemView.findViewById(R.id.textView50);
            postedOn = itemView.findViewById(R.id.textView51);
            btn = itemView.findViewById(R.id.button5);
            floatingActionButton = itemView.findViewById(R.id.floatingActionButton);

        }

        @SuppressLint("SetTextI18n")
        private void setData(propertyViewModel propertyViewModel) {
            Glide.with(itemView.getContext()).load(propertyViewModel.getImage()).into(imageView);
            cityFlat.setText(propertyViewModel.getCity() + " / " + propertyViewModel.getFlat_type());
            address.setText(propertyViewModel.getAddress());
            price.setText(propertyViewModel.getPrice());
            installment.setText(propertyViewModel.getInstallment());
            des.setText(propertyViewModel.getDes());
            postedOn.setText(propertyViewModel.getPosted_on());
            btn.setOnClickListener(v -> {



                Toast.makeText(itemView.getContext(), "method not implemented yet", Toast.LENGTH_SHORT).show();
            });
            floatingActionButton.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "method not implemented yet", Toast.LENGTH_SHORT).show();
            });

        }
    }
}
