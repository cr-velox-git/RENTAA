package com.silverphoenix.rentaa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //search option
    private Spinner citySpinner, flatTypeSpinner;

    //itemView
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        citySpinner = findViewById(R.id.spinner);
        flatTypeSpinner = findViewById(R.id.spinner2);
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton2);
        //........................... no internet connection layout start ............................//
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.solid_30);
        ImageView Image = loadingDialog.findViewById(R.id.loading_image);
        Glide.with(getApplicationContext()).load(R.drawable.gg).into(Image);
        //........................... no internet connection layout end ............................//

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.cityList));
        cityAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

         ArrayAdapter<String> flatAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.flatList));
        flatAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        flatTypeSpinner.setAdapter(flatAdapter);
        /*......................................................................*/

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        propertyViewAdapter adapter = new propertyViewAdapter(DataQueries.simpleList, this);
        DataQueries.database_simpleBuildView(loadingDialog, adapter);
        recyclerView.setAdapter(adapter);

        /*.......................................................................*/
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddBuildingActivity.class);
            startActivity(intent);
        });
    }

}