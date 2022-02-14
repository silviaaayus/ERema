package com.silvia.rema;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.silvia.rema.databinding.ActivityEditProfilBinding;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfilActivity extends AppCompatActivity {
    private ActivityEditProfilBinding binding;


    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    TinyDB tinyDB;


    String[] jekel = {"L","P"};
    String tempJekel,id_user;
    API api;
    Bitmap bitmap, decoded;

    String fileFoto,foto;

    int success =0 ;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100

    boolean imgAdd = false;
    private int DOKUMEN = 1, CAMERA = 2;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AndroidNetworking.initialize(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        api = new API();

        tinyDB = new TinyDB(this);



        id_user = tinyDB.getString("keyIdUser");


        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        binding.tgllahirMhsEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getUser();



        binding.Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfil();

            }
        });
        if (tinyDB.getBoolean("keyGoogleSignin")){
            Picasso
                    .get()
                    .load(tinyDB.getString("keyFotoGmail"))
                    .error(R.drawable.com_facebook_auth_dialog_cancel_background)
                    .placeholder(R.drawable.ic_baseline_home_24)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit()
                    .centerInside()
                    .into(binding.fotooo);
            binding.edtNamaMhsEdt.setEnabled(false);
        }else{
            Picasso
                    .get()
                    .load(tinyDB.getString("keyFoto"))
                    .error(R.drawable.com_facebook_auth_dialog_cancel_background)
                    .placeholder(R.drawable.ic_baseline_home_24)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit()
                    .centerInside()
                    .into(binding.fotooo);
        }






        binding.pilihfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(EditProfilActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    alertDialogCameraPermission();
                }
                else{
                    showPictureDialog();
                }
            }
        });

        ArrayAdapter<String> Adokter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,jekel);
        Adokter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.edtJekelMhsEdt.setAdapter(Adokter);

        binding.edtJekelMhsEdt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tempJekel = jekel[i];
                Log.e("spinner",tempJekel);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


    }
    private void showDateDialog(){

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                binding.tgllahirMhsEdt.setText(dateFormatter.format(newDate.getTime()));

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.show();
    }

    private void getUser() {
        Toast.makeText(this, "Loading . . .", Toast.LENGTH_SHORT).show();
        AndroidNetworking.post(api.URL_SELECT_USER)
                .addBodyParameter("id", id_user)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String stat = response.getString("status");

                            if (stat.equalsIgnoreCase("true")){
                                JSONArray res = response.getJSONArray("data");
                                JSONObject data = res.getJSONObject(0);
                                foto = data.getString("foto_profil");

                                        binding.edtNamaMhsEdt.setText(data.getString("nama"));
                                        binding.tgllahirMhsEdt.setText(data.getString("tgl_lahir"));
                                        binding.alamatMhsEdt.setText(data.getString("alamat"));
                                        binding.emailMhsEdt.setText(data.getString("email"));


                                        String jekels = data.getString("jekel");
                                        if (jekels.equalsIgnoreCase("L")){
                                            binding.edtJekelMhsEdt.setSelection(0);
                                        }else{
                                            binding.edtJekelMhsEdt.setSelection(1);
                                        }


                                tinyDB.putString("keyNamaMhs",data.getString("nama"));
                                tinyDB.putString("keyJekel",data.getString("jekel"));
                                tinyDB.putString("keyAlamat",data.getString("alamat"));
                                tinyDB.putString("keyTglLahir",data.getString("tgl_lahir"));
                                tinyDB.putString("keyEmail",data.getString("email"));
                                tinyDB.putString("keyFoto",api.HOST_FOTO+data.getString("foto_profil"));



                            }
                            else {
                                Toast.makeText(EditProfilActivity.this, stat, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();

                        Log.d("eror", "code :"+anError);
                        Toast.makeText(EditProfilActivity.this, ""+anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void editProfil(){
        Toast.makeText(this, "Loading . . .", Toast.LENGTH_SHORT).show();
             AndroidNetworking.post(api.URL_EDIT_PROFIL)
                .addBodyParameter("id", id_user)
                .addBodyParameter("nama", binding.edtNamaMhsEdt.getText().toString())
                .addBodyParameter("tgl_lahir", binding.tgllahirMhsEdt.getText().toString())
                .addBodyParameter("alamat", binding.alamatMhsEdt.getText().toString())
                .addBodyParameter("email", binding.emailMhsEdt.getText().toString())
                .addBodyParameter("jekel", tempJekel)
                .addBodyParameter("foto_profil", fileFoto)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String respon = response.getString("response");
                            if (respon.equalsIgnoreCase("Sukses")){
                                getUser();
                                Intent i = new Intent(EditProfilActivity.this,MainActivity.class);
                                startActivity(i);
                                Toast.makeText(EditProfilActivity.this, "Data Berhasil di Update", Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Kesalahan update, Kode 2"
                                ,Toast.LENGTH_LONG).show();
                        anError.printStackTrace();

                    }


                });
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);

        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        fileFoto = getStringImage(decoded);
        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        binding.fotooo.setImageBitmap(decoded);
        binding.Update.setVisibility(View.VISIBLE);
        success = 1;
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==DOKUMEN){
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri filePath = data.getData();
                try {
                    //mengambil fambar dari Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                    setToImageView(getResizedBitmap(bitmap, 512));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == CAMERA){
            byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap FixBitmap = (Bitmap) data.getExtras().get("data");

            FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//                FixBitmap = imageUtils.getCompressedBitmap(data.getExtras().get("data"));
//                Bitmap bt=Bitmap.createScaledBitmap(FixBitmap,640, 360, false);
//                if (noOrder.equals("1"))
//                    imageViewResult = (ImageView) findViewById(R.id.imgFoto1);
//
//                imageViewResult.setImageBitmap(FixBitmap);
//
//                binding.txtFoto.setText("Foto_bencana.jpg");
            binding.fotooo.setImageBitmap(FixBitmap);
            byteArray = byteArrayOutputStream.toByteArray();

            fileFoto = Base64.encodeToString(byteArray, Base64.DEFAULT);
            binding.Update.setVisibility(View.VISIBLE);
            success = 1;
//                saveImage(FixBitmap);
        }


    }
    // Untuk permission nyo, mintak izin akses kamera
    private void alertDialogCameraPermission() {
        new androidx.appcompat.app.AlertDialog.Builder(EditProfilActivity.this)
                .setTitle("Disclaimer Camera")
                .setMessage(
                        "Aplikasi ini menggunakan akses kamera"
                )

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 5);
                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    // Aksi dari respon pengguna dari permintaan izin
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                showPictureDialog();
            } else {
                Toast.makeText(EditProfilActivity.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();
            }
        }
    }

    // untuk muncul dialog pilihan mau upload foto atau dokumen
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(EditProfilActivity.this);
        pictureDialog.setTitle("Silahkan Pilih");
        String[] pictureDialogItems = {
                "Pilih Dokumen",
                "Ambil Foto Langsung"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Ambil File Dari Dokumen
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                                break;
                            case 1: // Ambil Foto Pake Kamera
                                Intent intentFoto = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intentFoto, CAMERA);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }




    private String fileNameDariTanggalJam(){
        String file = "";
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int jam = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        file = ""+year+month+dayOfMonth+jam+minute+second;
        return file;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}