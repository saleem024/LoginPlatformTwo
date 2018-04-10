package com.techkshetrainfo.cltenotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techkshetrainfo.cltenotes.Api.ApiClient;
import com.techkshetrainfo.cltenotes.Api.ApiInterface;
import com.techkshetrainfo.cltenotes.Models.Notification;
import com.techkshetrainfo.cltenotes.Models.NotificationResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tps on 7/24/2017.
 */

public class NotificationsActivityAlert extends AppCompatActivity {
    boolean register_user=true;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_alert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());

        SharedPreferences log_sesion = getSharedPreferences("log_session", MODE_PRIVATE);
        String loggedin = log_sesion.getString("loggedin", "0");
        if(loggedin.equals("0")) {
            register_user = false;
        }
        getnotifications();


    }

    @Override
    public void onBackPressed() {
        if (session.isLoggedIn()) {
            Intent intent = new Intent(NotificationsActivityAlert.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
        } else {
            Intent intent = new Intent(NotificationsActivityAlert.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
        }




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void getnotifications() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Call<NotificationResponse> call = apiService.getnotification();

        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                List<Notification> notify = response.body().getNotification();
                LinearLayout options_layout = (LinearLayout) findViewById(R.id.notification_list);
                options_layout.removeAllViews();
                for (int i = 0; i < notify.size(); i++) {
                    LayoutInflater inflater = LayoutInflater.from(NotificationsActivityAlert.this);
                    View to_add = inflater.inflate(R.layout.notification_element,
                            options_layout, false);

                    TextView not_title = (TextView) to_add.findViewById(R.id.notification_title);
                    TextView not_msg = (TextView) to_add.findViewById(R.id.notification_msg);
                    TextView not_date = (TextView) to_add.findViewById(R.id.notification_date);
                    // TextView comments = (TextView) to_add.findViewById(R.id.user_comments);
                    long unixSeconds = Long.parseLong(notify.get(i).getDate());
                    Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
                    String formattedDate = sdf.format(date);
                    not_title.setText(notify.get(i).getNotificationTitle().toString());
                    not_msg.setText(notify.get(i).getNotificationMessage().toString());
                    not_date.setText(formattedDate);

                    options_layout.addView(to_add);

                }

            }


            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                CommonFunctions.show_dialog(NotificationsActivityAlert.this, getResources().getString(R.string.title_interner), getResources().getString(R.string.internet_error));

            }
        });
    }

}

