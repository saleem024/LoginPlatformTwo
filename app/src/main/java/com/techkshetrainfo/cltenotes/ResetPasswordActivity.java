package com.techkshetrainfo.cltenotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.techkshetrainfo.cltenotes.Api.ApiClient;
import com.techkshetrainfo.cltenotes.Api.ApiInterface;
import com.techkshetrainfo.cltenotes.Models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText froget_email;
    Button forget_btn,back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        forget_btn = (Button) findViewById(R.id.forget_btn);
        froget_email = (EditText) findViewById(R.id.forget_email);
        back_btn= (Button) findViewById(R.id.btn_back);
        forget_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonFunctions.isnetworkavailable(ResetPasswordActivity.this)){

                    if(validate()){
                        reset_password();
                    }

                }else{

                    Toast.makeText(ResetPasswordActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();

                }
            }
        });
back_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
    }
});

    }
//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(ResetPasswordActivity.this, HomeActivity.class));
//        finish();
//
//    }
    private void reset_password() {


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Call<LoginResponse> call = apiService.reset_password(froget_email.getText().toString());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                String success_code=response.body().getCode().toString();

                if(success_code.equals("101")){
                    Toast.makeText(ResetPasswordActivity.this, R.string.pass_reset_success, Toast.LENGTH_SHORT).show();
                    froget_email.setText("");
                    Bundle bundle = new Bundle();

                    bundle.putString("message", " ");


                    Intent login_intent= new Intent(ResetPasswordActivity.this,LoginActivity.class);
                    login_intent.putExtras(bundle);
                    startActivity(login_intent);

                }else{

                    Toast.makeText(ResetPasswordActivity.this, R.string.pass_not_reset, Toast.LENGTH_SHORT).show();
                    froget_email.setText("");

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, R.string.pass_not_reset, Toast.LENGTH_SHORT).show();

            }
        });


    }

    private boolean validate() {
        if(!CommonFunctions.isValidMail(froget_email.getText().toString())){
            froget_email.setError(getString(R.string.email_error));
        }


        return true;
    }

}
