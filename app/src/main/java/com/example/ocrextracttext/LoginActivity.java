package com.example.ocrextracttext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import retrofit2.Call;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import controller.Api;
import controller.RetrofitClient;
import model.Login;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    FloatingActionButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String _deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        email = findViewById(R.id.inputEmailLogin);
        password = findViewById(R.id.inputPasswordLogin);
        login = findViewById(R.id.buttonLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataEntered();

                Login login = new Login("ThanhTien", _deviceID, email.getText().toString(), password.getText().toString());
                sendNetworkRequest(login, _deviceID);
            }
        });

    }

    private void sendNetworkRequest(Login login, String _deviceID) {
        Api apiInterface = RetrofitClient.getRetrofit().create(Api.class);
        Call<Login> call = apiInterface.login(login);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Toast.makeText(LoginActivity.this, "Call API sucessfull" + response.body(), Toast.LENGTH_SHORT).show();

                saveSharedPreferences(_deviceID);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Call API failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void checkDataEntered() {
        if (isEmpty(email)) {
            Toast t = Toast.makeText(this, "You must enter email to login", Toast.LENGTH_SHORT);
            t.show();
        }

        if (isEmpty(password)) {
            password.setError("Password is required");
        }

        if (isEmail(email) == false) {
            email.setError("Enter valid email");
        }
    }

    public void saveSharedPreferences(String _deviceID) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyFreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("_deviceID", _deviceID);
        editor.commit();
    }

    public void openSignUp(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}