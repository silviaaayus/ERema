package com.silvia.rema;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.silvia.rema.databinding.ActivityLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity  extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {
    private ActivityLoginBinding binding;
    API api;
    TinyDB tinyDB;

//Gmail
    private static final String TAG = "LoginActivity";
    String id_user,nama_mhs,jekel,tgl_lahir,alamat_mhs,email,username,password,idToken,foto;

    private int request_code = 0;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        api = new API();
        AndroidNetworking.initialize(this);
        tinyDB = new TinyDB(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        binding.btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validasiInput()) {
                    getLogin();
                }
            }
        });

        binding.loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        if (tinyDB.getBoolean("keyLogin")){
            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

    }


    private boolean validasiInput() {
        boolean value= true;

        String password1 = binding.edtPass.getText().toString();
        String username1 = binding.edtusername.getText().toString();
        if (username1.equalsIgnoreCase("") ){
            binding.edtusername.setError("Email Kosong");
            binding.edtusername.requestFocus();
            value = false;
        }
        if (password1.equalsIgnoreCase("") ){
            binding.edtPass.setError("Password Kosong");
            binding.edtPass.requestFocus();
            value = false;
        }

        return value;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account){

        if (account != null){
            idToken = account.getIdToken();

            id_user = account.getId();
            email = account.getEmail();
            nama_mhs = account.getDisplayName();
            foto = String.valueOf(account.getPhotoUrl());
            jekel="Null";
            tgl_lahir = "Null";
            alamat_mhs = "Null";
            Log.e("email",email);

            jsonInsertUser();
//            Toast.makeText(this, "Sudah Login", Toast.LENGTH_SHORT).show();
        }else{
//            Toast.makeText(this, "Belum Login", Toast.LENGTH_SHORT).show();
        }

    }

    private void jsonInsertUser(){

        AndroidNetworking.post(api.URL_INSERT_GMAIL)
                .addBodyParameter("nama", nama_mhs )
                .addBodyParameter("email", email)
                .addBodyParameter("jekel", jekel)
                .addBodyParameter("tgl_lahir", tgl_lahir)
                .addBodyParameter("alamat", alamat_mhs)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Handle Response
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase("sukses")||status.equalsIgnoreCase("ada")) {
                                getId();

//
                            } else {
                                signOut();
                                Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    @Override
                    public void onError(ANError error) {
                        //Handle Error
                        signOut();
                        Toast.makeText(getApplicationContext(),"Data gagal ditambahkan", Toast.LENGTH_SHORT).show();
                        //memunculkan Toast saat data gagal ditambahkan
                    }
                });
    }



    public void getId(){
        Toast.makeText(this, "Loading . . .", Toast.LENGTH_SHORT).show();
             AndroidNetworking.get(api.URL_ID+email)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("sukses")) {
                                JSONArray res = response.getJSONArray("res");
                                JSONObject data = res.getJSONObject(0);

                                id_user = data.getString("id");
                                Log.e("jumlah",""+id_user);
                                tinyDB.putString("keyIdUser",id_user);
                                tinyDB.putString("keyNamaMhs",nama_mhs);
                                tinyDB.putString("keyJekel",jekel);
                                tinyDB.putString("keyAlamat",alamat_mhs);
                                tinyDB.putString("keyTglLahir",tgl_lahir);
                                tinyDB.putString("keyEmail",email);
                                tinyDB.putString("keyFotoGmail",foto);


                                tinyDB.putBoolean("keyGoogleSignin",true);
                                tinyDB.putBoolean("keyLogin",true);
                                Log.e("id",id_user);


                                Toast.makeText(LoginActivity.this, "Login Sukses. Silahkan Update Profil Pada Menu Profil", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }else {
                                signOut();

                                Toast.makeText(LoginActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tampil menu","response:"+anError);
                        signOut();
                    }
                });

    }

    private void getLogin() {
        Toast.makeText(this, "Loading . . .", Toast.LENGTH_SHORT).show();
        AndroidNetworking.post(api.URL_LOGIN)
                .addBodyParameter("email", binding.edtusername.getText().toString())
                .addBodyParameter("password", binding.edtPass.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int stat = response.getInt("status");

                            if (stat == 1){
                                JSONObject res = response.getJSONObject("data");
                                String tgllahir = res.getString("tgl_lahir");



                                tinyDB.putString("keyIdUser",res.getString("id"));
                                tinyDB.putString("keyNamaMhs",res.getString("nama"));
                                tinyDB.putString("keyJekel",res.getString("jekel"));
                                tinyDB.putString("keyAlamat",res.getString("alamat"));
                                tinyDB.putString("keyTglLahir",tgllahir);
                                tinyDB.putString("keyEmail",res.getString("email"));
                                tinyDB.putString("keyFoto",api.HOST_FOTO+res.getString("foto_profil"));


                                tinyDB.putBoolean("keyGoogleSignin",false);
                                tinyDB.putBoolean("keyLogin",true);


                                Toast.makeText(LoginActivity.this, "Login Sukses", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                            else {
                                Toast.makeText(LoginActivity.this, stat, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("eror", "code :"+anError);
                        Toast.makeText(LoginActivity.this, ""+anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
}