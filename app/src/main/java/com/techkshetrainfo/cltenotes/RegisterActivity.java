package com.techkshetrainfo.cltenotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.techkshetrainfo.cltenotes.Api.ApiClient;
import com.techkshetrainfo.cltenotes.Api.ApiInterface;
import com.techkshetrainfo.cltenotes.Models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity{

    EditText email, password, phone, user_name, confirm_pass;
    Button register;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = (Button) findViewById(R.id.btn_reg);
        email = (EditText) findViewById(R.id.user_email);
        password = (EditText) findViewById(R.id.user_password);
        phone = (EditText) findViewById(R.id.user_phone);
        user_name = (EditText) findViewById(R.id.user_name);
        confirm_pass = (EditText) findViewById(R.id.confirm_pass);
        login = (TextView) findViewById(R.id.tv_login);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate_user();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
//        finish();
//
//    }
    private void validate_user() {

        if (email.getText().length() == 0) {
            email.setError(getString(R.string.email_error));

        } else if (email.getText().length() == 0 || !email.getText().toString().trim().matches(getString(R.string.emailpattern))) {

            email.setError(getString(R.string.email_error));
        } else if (!CommonFunctions.isValidMobile(phone.getText().toString())) {

            phone.setError(getString(R.string.contact_empty));
        } else if (password.getText().toString().length() == 0 || password.length() < 6) {
            password.setError("Password cannot be less than 6 characters!");
        } else if (password.getText().length() == 0 || !password.getText().toString().equals(confirm_pass.getText().toString())) {

            password.setError(getString(R.string.confirm_password_error));
        } else {
            if (CommonFunctions.isnetworkavailable(RegisterActivity.this)) {

                register_user();
            } else {

                Toast.makeText(this, R.string.internet_error, Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void register_user() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Call<LoginResponse> call = apiService.register_user(email.getText().toString().trim(), password.getText().toString().trim(), user_name.getText().toString().trim(), phone.getText().toString().trim());

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().getCode().equals("101")) {
                    SharedPreferences.Editor editor = getSharedPreferences("log_session", MODE_PRIVATE).edit();
                    editor.putString("loggedin", "1");
                    editor.putString("user_email", email.getText().toString().trim());
                    editor.putString("user_name", user_name.getText().toString().trim());
                    editor.putString("user_phone", phone.getText().toString().trim());
                    editor.commit();
                  //  get_payment_detail(email.getText().toString().trim());

                } else {

                    //Toast.makeText(RegisterActivity.this, R.string.invalid_credential_msg, Toast.LENGTH_LONG).show();
                    Toast.makeText(RegisterActivity.this, "User Already Exists", Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

    }

//    private void get_payment_detail(String email_id) {
//
//
//        final DatabaseHandler db = new DatabaseHandler(this);
//        ApiInterface apiService =
//                ApiClient.getClient().create(ApiInterface.class);
//
//
//        Call<PaymentResponse> call = apiService.get_payment(email_id);
//        call.enqueue(new Callback<PaymentResponse>() {
//            @Override
//            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
//                List<Payment> payment = response.body().getPayment();
//                for (int i = 0; i < payment.size(); i++) {
//
//                    int pid = Integer.parseInt(payment.get(i).getPid());
//                    int mid = Integer.parseInt(payment.get(i).getMid());
//                    float amount = Float.parseFloat(payment.get(i).getAmount());
//                    int paid_date = Integer.parseInt(payment.get(i).getPaidDate());
//                    String payment_gateway = payment.get(i).getPaymentGateway();
//                    String payment_status = payment.get(i).getPaymentStatus();
//                    String transaction_id = payment.get(i).getTransactionId();
//                    String device_id = payment.get(i).getDeviceId();
//                    String email = payment.get(i).getEmail();
//                    String name = payment.get(i).getName();
//
//                    db.payment_data(pid, mid, amount, paid_date, name, payment_gateway, payment_status, transaction_id, device_id, email);
//
//
//                }
//                db.close();
//                Intent inr = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(inr);
//                Toast.makeText(RegisterActivity.this, R.string.login_success, Toast.LENGTH_LONG).show();
//                finish();
//
//            }
//
//            @Override
//            public void onFailure(Call<PaymentResponse> call, Throwable t) {
//
//            }
//        });
//
//
//    }


}
