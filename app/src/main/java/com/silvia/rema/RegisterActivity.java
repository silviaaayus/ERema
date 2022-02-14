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
import com.silvia.rema.databinding.ActivityRegisterBinding;

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

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;


    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;


    String[] jekel = {"L","P"};
    String tempJekel;
    String jekels,nama,tgl_lahir,username,pass,alamat,email;
    API api;
    int flagAkun = 99;
    Bitmap bitmap, decoded;
    String fileFoto;

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
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());
        AndroidNetworking.initialize(this);
        api = new API();

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        binding.tgllahirMhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        ArrayAdapter<String> Adokter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,jekel);
        Adokter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.edtJekelMhs.setAdapter(Adokter);

        binding.edtJekelMhs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.pilihfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(RegisterActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    alertDialogCameraPermission();
                }
                else{
                    showPictureDialog();
                }
            }
        });



        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.pssMhs.getText().toString().equals(binding.konfirmasipass.getText().toString())){
                 tambahData();


                }

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
                binding.tgllahirMhs.setText(dateFormatter.format(newDate.getTime()));

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.show();
    }

    public void tambahData(){
        AndroidNetworking.post(api.URL_REGISTER)
                .addBodyParameter("email", binding.emailMhs.getText().toString())
                .addBodyParameter("password",binding.pssMhs.getText().toString())
                .addBodyParameter("nama", binding.edtNamaMhs.getText().toString())
                .addBodyParameter("tgl_lahir", binding.tgllahirMhs.getText().toString())
                .addBodyParameter("alamat", binding.alamatMhs.getText().toString())
                .addBodyParameter("jekel", tempJekel)
                .addBodyParameter("foto_profil",fileFoto)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //Handle Response
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            if (status.equalsIgnoreCase("sukses")||status.equalsIgnoreCase("ada")){

                                Toast.makeText(getApplicationContext(),message , Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //memunculkan Toast saat data berhasil ditambahkan

                    }
                    @Override
                    public void onError(ANError error) {
                        //Handle Error

                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Data gagal ditambahkan", Toast.LENGTH_SHORT).show();
                        //memunculkan Toast saat data gagal ditambahkan
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
        binding.foto.setImageBitmap(decoded);
        binding.register.setVisibility(View.VISIBLE);
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
            binding.foto.setImageBitmap(FixBitmap);
            byteArray = byteArrayOutputStream.toByteArray();

            fileFoto = Base64.encodeToString(byteArray, Base64.DEFAULT);
            binding.register.setVisibility(View.VISIBLE);
            success = 1;
//                saveImage(FixBitmap);
        }


    }
    // Untuk permission nyo, mintak izin akses kamera
    private void alertDialogCameraPermission() {
        new androidx.appcompat.app.AlertDialog.Builder(RegisterActivity.this)
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
                Toast.makeText(RegisterActivity.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();
            }
        }
    }

    // untuk muncul dialog pilihan mau upload foto atau dokumen
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(RegisterActivity.this);
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





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




}