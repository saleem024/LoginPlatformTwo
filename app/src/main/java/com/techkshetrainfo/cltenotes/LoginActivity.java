package com.techkshetrainfo.cltenotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.techkshetrainfo.cltenotes.Api.ApiClient;
import com.techkshetrainfo.cltenotes.Api.ApiInterface;
import com.techkshetrainfo.cltenotes.Models.LoginResponse;
import com.techkshetrainfo.cltenotes.Models.Payment;
import com.techkshetrainfo.cltenotes.Models.PaymentResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity  {


    EditText email, password;
    Button  login;

//    String message = "";

    TextView forget_pass,register;
    String mid = "0";
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        register = (TextView) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.user_email);
        forget_pass = (TextView) findViewById(R.id.forget_pass);
        password = (EditText) findViewById(R.id.user_password);

        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            email.setText(loginPreferences.getString("username", ""));
            password.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate_user();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });

        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forget = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(forget);
            }
        });



    }
//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//        finish();
//    }
    private void validate_user() {

        if (!CommonFunctions.isValidMail(email.getText().toString())) {
            email.setError(getString(R.string.email_error));
        } else if (password.getText().toString().length() == 0 || password.length() < 5) {
            password.setError("Password cannot be less than 5 characters!");
        } else {

            if (CommonFunctions.isnetworkavailable(LoginActivity.this)) {
                login_user(email.getText().toString().trim(), password.getText().toString().trim());
            } else {
                Toast.makeText(this, R.string.internet_error, Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void login_user(String email_id, String pass) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);



        Call<LoginResponse> call = apiService.login_user(email_id, pass);
        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", email_id);
            loginPrefsEditor.putString("password", pass);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().getCode().equals("101")) {
                    SharedPreferences.Editor editor = getSharedPreferences("log_session", MODE_PRIVATE).edit();
                    editor.putString("loggedin", "1");
                    editor.putString("user_email", email.getText().toString().trim());
                    editor.putString("user_name", "");
                    editor.putString("user_phone", "");
                    editor.commit();
                    session.createLoginSession("",email.getText().toString().trim());
                   get_payment_detail(email.getText().toString().trim());


                } else {

                    Toast.makeText(LoginActivity.this, R.string.invalid_credential_msg, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, R.string.internet_error, Toast.LENGTH_LONG).show();

            }
        });


    }

    private void get_payment_detail(String email_id) {


        final DatabaseHandler db = new DatabaseHandler(this);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Call<PaymentResponse> call = apiService.get_payment(email_id);

        call.enqueue(new Callback<PaymentResponse>() {

            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                List<Payment> payment = response.body().getPayment();
                db.clear_paymet();
                for (int i = 0; i < payment.size(); i++) {

                    int pid = Integer.parseInt(payment.get(i).getPid());
                    int mid = Integer.parseInt(payment.get(i).getMid());
                    float amount = Float.parseFloat(payment.get(i).getAmount());
                    int paid_date = Integer.parseInt(payment.get(i).getPaidDate());
                    String payment_gateway = payment.get(i).getPaymentGateway();
                    String payment_status = payment.get(i).getPaymentStatus();
                    String transaction_id = payment.get(i).getTransactionId();
                    String device_id = payment.get(i).getDeviceId();
                    String email = payment.get(i).getEmail();
                    String name = payment.get(i).getName();

                    db.payment_data(pid, mid, amount, paid_date, name, payment_gateway, payment_status, transaction_id, device_id, email);


                }
                db.close();
                if (mid.equals("0")) {
                    Intent inr = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(inr);
                    Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Bundle bundle_rating = new Bundle();
                    bundle_rating.putString("m_id", mid);
                    Intent rating = new Intent(LoginActivity.this, MainActivity.class);
                    rating.putExtras(bundle_rating);
                    startActivity(rating);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {

            }
        });


    }

}
