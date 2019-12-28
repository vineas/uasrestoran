package com.restaurant.alvienas;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.restaurant.alvienas.data.Constans;
import com.restaurant.alvienas.data.Session;
import com.restaurant.alvienas.model.RestaurantResponse;
import com.restaurant.alvienas.utils.DialogUtils;

public class CreateRestaurantActivity extends AppCompatActivity {

    EditText namarm, kategori, link_foto, alamat;
    Button create_restaurant;
    ProgressDialog progressDialog;
    Session session;
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        session = new Session(this);
        progressDialog = new ProgressDialog(this);
        userId = getIntent().getStringExtra("userId");
        initBinding();
        initClick();
    }

    private void initBinding() {
        namarm = findViewById(R.id.et_namarm);
        kategori = findViewById(R.id.et_kategori);
        link_foto = findViewById(R.id.et_link_foto);
        alamat = findViewById(R.id.et_alamat);
        create_restaurant = findViewById(R.id.btn_create_restaurant);
    }

    private void initClick() {
        create_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namarm.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "Nama Restaurant Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else if(kategori.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "Kategori Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else if(link_foto.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "Link Foto Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else if(alamat.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "Alamat Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    createRestaurant();
                }
            }
        });
    }
    public void createRestaurant() {
        DialogUtils.openDialog(this);
        AndroidNetworking.post(Constans.CREATE_RESTAURANT)
                .addBodyParameter("userid", userId)
                .addBodyParameter("namarm", namarm.getText().toString())
                .addBodyParameter("kategori", kategori.getText().toString())
                .addBodyParameter("link_foto", link_foto.getText().toString())
                .addBodyParameter("alamat", alamat.getText().toString())
                .build()
                .getAsObject(RestaurantResponse.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        if (response instanceof RestaurantResponse) {
                            RestaurantResponse res = (RestaurantResponse) response;
                            if (res.getStatus().equals("success")) {
                                Toast.makeText(CreateRestaurantActivity.this,"Success Create Restaurant", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(CreateRestaurantActivity.this,"Failed Create Restaurant", Toast.LENGTH_SHORT).show();
                            }
                        }
                        DialogUtils.closeDialog();
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CreateRestaurantActivity.this, "Terjadi kesalahan API", Toast.LENGTH_SHORT).show();
                        Toast.makeText(CreateRestaurantActivity.this, "Terjadi kesalahan API : "+anError.getCause().toString(), Toast.LENGTH_SHORT).show();
                        DialogUtils.closeDialog();
                    }
                });
    }
}
