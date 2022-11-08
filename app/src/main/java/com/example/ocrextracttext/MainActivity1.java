package com.example.ocrextracttext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ocrextracttext.api.ApiService_login;
import com.example.ocrextracttext.api.ResponePost_login;
import com.example.ocrextracttext.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.SharedPreferences;

import java.io.Console;

public class MainActivity1 extends AppCompatActivity {
    private EditText email, password;
    private Button btnlogin, btnregister;
    private TextView postresultt;
    private CheckBox luumatkhau;
    String Email,Pass,ID;
    String trang_thai="key_user";


//    SharedPreferences pre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        email =findViewById(R.id.email);
        password=findViewById(R.id.password);
        btnlogin=findViewById(R.id.btnlogin);
        btnregister=findViewById(R.id.btnregister);
//        postresultt=findViewById(R.id.postresultt);
        luumatkhau= findViewById(R.id.luumatkhau);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                Email=email.getText().toString().trim();
                Pass=password.getText().toString().trim();

                if(validateEmail()&&validatePassword()) {


                    sendpost();
                }else {
                    Toast.makeText(MainActivity1.this,"Nhập đúng định dạng",Toast.LENGTH_SHORT).show();                }
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity1.this,MainActivity2.class);
                startActivity(intent);
            }
        });



    }
    private void sendpost(){
        //     Post post= new Post(null," ",Email," "," "," ",Pass," "," "," "," ");
//        Post_login post = new Post_login(Email,Pass,ID);
//        Post_login post =new Post_login("duongduykhang01@gmail.com","12354khang123","Khang12345678");
Post_login post = new Post_login(Email,ID,Pass);
        ApiService_login.apiserver.sendPost(post).enqueue(new Callback<ResponePost_login>() {


            @Override
            public void onResponse(Call<ResponePost_login> call, Response<ResponePost_login> response) {
//                Log.i("Respone " +response.body().text);
                System.out.println("Respone " +response.body().data_post);
                if(response.body().data_post!=null){
                    SharedPreferences sharedPreferences=getSharedPreferences("trang_thai",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString(trang_thai,response.body().data_post.key_user);
                    editor.putString("folder_id",response.body().data_post.folderId);
                    System.out.println("Hello"+response.body().data_post.folderId);
                    editor.commit();

//                    Preferences pre = new Preferences(ID,response.body().data_post.key_user);
////                    System.out.println("Respone " +response.body().data_post.key_user);
//                    System.out.println(pre.getId());
//                    System.out.println(pre.getKey_user());

                    Toast.makeText(MainActivity1.this, response.body().text, Toast.LENGTH_SHORT).show();
                Intent in = new Intent(MainActivity1.this,MainActivity.class);
                startActivity(in);
                }
                else{
                    Toast.makeText(MainActivity1.this, response.body().text, Toast.LENGTH_SHORT).show();
                }
                ResponePost_login postresult= response.body();
//                if(postresult!=null){
//                    postresultt.setText(postresult.toString());
//                }
            }

            @Override
            public void onFailure(Call<ResponePost_login> call, Throwable t) {
                Toast.makeText(MainActivity1.this, "Error",Toast.LENGTH_SHORT).show();

            }
        });
    }
    private Boolean validateEmail() {
        Email=email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (Email.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!Email.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
//            email.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = password.getText().toString().trim();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)&& password.length()<8) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
//            password.setErrorEnabled(false);
            return true;
        }
    }

}
