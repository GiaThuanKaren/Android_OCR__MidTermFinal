package com.example.ocrextracttext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.provider.Settings.Secure;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import controller.Api;
import controller.RetrofitClient;
import model.Register;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    EditText rePassword;
    FloatingActionButton register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String _deviceID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);


        email = findViewById(R.id.inputEmailRegister);
        password = findViewById(R.id.inputPasswordRegister);
        rePassword = findViewById(R.id.inputRePasswordRegister);
        register = findViewById(R.id.buttonRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataEntered();
                Register register = new Register("ThanhTien", _deviceID, email.getText().toString(), password.getText().toString());
                sendNetworkRequest(register);

            }
        });
    }
    private void sendNetworkRequest(Register register) {
        Api apiInterface = RetrofitClient.getRetrofit().create(Api.class);
        Call<Register> call = apiInterface.register(register);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                Toast.makeText(RegisterActivity.this, "Call API sucessfull " + response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Call API failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

        if (isEmpty(rePassword)) {
            rePassword.setError("Re-Password is required");
        }
//
//        if (password.getText().toString() != rePassword.getText().toString()) {
//            password.setError("Password and RePassword are different");
//        }

        if (isEmail(email) == false) {
            email.setError("Enter valid email");
        }
    }

    public void openSignIn(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}