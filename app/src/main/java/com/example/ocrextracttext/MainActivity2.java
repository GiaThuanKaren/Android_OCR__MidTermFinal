package com.example.ocrextracttext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocrextracttext.api.ApiService_register;
import com.example.ocrextracttext.api.ResponePost_register;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {
    private EditText edemail, edname, edpassword;
    private Button btnregister;
    private TextView postresultt;

    String ID,email,pass,eemail,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        edpassword = (EditText) findViewById(R.id.idpassword);
        edemail = (EditText) findViewById(R.id.idemail);
        edname = (EditText) findViewById(R.id.idname);
        btnregister = (Button) findViewById(R.id.btnregister);


    }
    public void btnregister(View view){
        ID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        System.out.println(ID);
        name=edname.getText().toString();
        pass=edpassword.getText().toString();
        eemail=edemail.getText().toString();
//        validateUsername();
//        validateEmail();
//        validatePassword();
    if(validateEmail()&&validatePassword()&&validateEmail()){
        sendpost();
    }else {
      Toast.makeText(MainActivity2.this,"Nhập đúng định dạng",Toast.LENGTH_SHORT).show();
    }
//        eddeviceid.setText("lô");
    }

    private void sendpost(){
        //     Post post= new Post(null," ",Email," "," "," ",Pass," "," "," "," ");
//        TelephonyManager telephonyManager=(TelephonyManager)getSystemService(TELECOM_SERVICE);
//        String m_deviceId=telephonyManager.getDeviceId();

        Post_register post= new Post_register(ID,name,eemail,pass);
//        Post_register post=new Post_register("12665244664","thanhchan","thanhch2an791huy4nhtanpphat@gmail.com","1");

        ApiService_register.apiserver.sendPost(post).enqueue(new Callback<ResponePost_register>() {
            @Override
            public void onResponse(Call<ResponePost_register> call, Response<ResponePost_register> response) {
//                Log.i("Respone " +response.body().text);

                if(response.body().statusCode!=null) {
                    System.out.println("Respone " +response.body().text);
                    Toast.makeText(MainActivity2.this,response.body().text , Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(MainActivity2.this, MainActivity1.class);
                    startActivity(in);
                }else{
                    Toast.makeText(MainActivity2.this,response.body().text , Toast.LENGTH_SHORT).show();
                }

//                if(postresult!=null){
//                    postresultt.setText(postresult.toString());
//                }

            }

            @Override
            public void onFailure(Call<ResponePost_register> call, Throwable t) {
                Toast.makeText(MainActivity2.this, "Error",Toast.LENGTH_SHORT).show();

            }
        });
    }
    private Boolean validateUsername() {
        name = edname.getText().toString().trim();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (name.isEmpty()) {
            edemail.setError("Field cannot be empty");
            return false;
        } else if (name.length() >= 15) {
            edname.setError("Username too long");
            return false;
        } else if (!name.matches(noWhiteSpace)) {
            edname.setError("White Spaces are not allowed");
            return false;
        } else {
            edname.setError(null);
//            edname.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateEmail() {
       String eemail=edemail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (eemail.isEmpty()) {
            edemail.setError("Field cannot be empty");
            return false;
        } else if (!eemail.matches(emailPattern)) {
            edemail.setError("Invalid email address");
            return false;
        } else {
            edemail.setError(null);
//            email.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = edpassword.getText().toString().trim();
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
            edpassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)&& edpassword.length()<8) {
            edpassword.setError("Password is too weak");
            return false;
        } else {
            edpassword.setError(null);
//            password.setErrorEnabled(false);
            return true;
        }
    }
}
