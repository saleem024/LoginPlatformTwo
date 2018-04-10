package com.techkshetrainfo.cltenotes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.techkshetrainfo.cltenotes.Api.ApiClient;
import com.techkshetrainfo.cltenotes.Api.ApiInterface;
import com.techkshetrainfo.cltenotes.Models.LoginResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private AlertDialog dialog;
    private FirebaseAuth firebaseAuth;
    private static long back_pressed;
    //    private EditText et_old_password, et_new_password;
    EditText new_pass, old_pass, comfirn_pass;
    private TextView tv_name, tv_email, tv_message;
    private ShareActionProvider mShareActionProvider;

    private ProgressDialog pDialog;
    private SharedPreferences pref;
    private ProgressBar progress;
    private SessionManager session;

    private String app_url3 = "Replace with playstore link";

    private String name, email;

    //private ExamPracticeFragment.UserLoginTask mAuthTask = null;

    View parentLayout;
    AutoCompleteTextView mEmail;
    EditText mPassword;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_one:
                    Toast.makeText(MainActivity.this, "Bottom Navigation One", Toast.LENGTH_SHORT).show();

                    return true;
                case R.id.action_two:
                    Toast.makeText(MainActivity.this, "Bottom Navigation Two", Toast.LENGTH_SHORT).show();

                    return true;
                case R.id.action_three:
                    Toast.makeText(MainActivity.this, "Bottom Navigation Three", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_four:
                    Toast.makeText(MainActivity.this, "Bottom Navigation Four", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_five:
                    Toast.makeText(MainActivity.this, "Bottom Navigation Five", Toast.LENGTH_SHORT).show();

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        session = new SessionManager(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();

        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();

        //     name
        name = user.get(SessionManager.KEY_NAME);

        // email
        email = user.get(SessionManager.KEY_EMAIL);
        pref = getPreferences(0);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        tv_name = (TextView) header.findViewById(R.id.tv_name);
        tv_email = (TextView) header.findViewById(R.id.tv_email);
        tv_name.setText(name);
        tv_email.setText(email);

        parentLayout = findViewById(R.id.root_view);


        if (savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_one, 0);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        if (getIntent().getExtras() != null) {
//
//            for (String key : getIntent().getExtras().keySet()) {
//                String value = getIntent().getExtras().getString(key);
//
//                if (key.equals("MainActivity") && value.equals("True")) {
//                    Intent intent = new Intent(this, MainActivity.class);
//                    intent.putExtra("value", value);
//                    startActivity(intent);
//                    finish();
//                }
//
//            }
//        }


    }

    private void showDialogs() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        old_pass = (EditText) view.findViewById(R.id.et_old_password);
        new_pass = (EditText) view.findViewById(R.id.et_new_password);
        comfirn_pass = (EditText) view.findViewById(R.id.confirm_password);
        tv_message = (TextView) view.findViewById(R.id.tv_message);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Change Password");
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String old_password = old_pass.getText().toString();
                String new_password = new_pass.getText().toString();
                String confirm_password = comfirn_pass.getText().toString();
                if (!old_password.isEmpty() && !new_password.isEmpty() && !confirm_password.isEmpty()) {

                    progress.setVisibility(View.VISIBLE);
                    if (isNetworkAvailable()) {
                        change_password();


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setCancelable(false);
                        builder.setTitle("No Internet");
                        builder.setMessage("Please Check Your Internet Connection!");

                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                // finish();
                            }
                        });


                        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
                        dialog.show();
                        Toast.makeText(MainActivity.this, "Network Unavailable!", Toast.LENGTH_LONG).show();
                    }

                } else {

                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Fields are empty");
                }
            }
        });
    }


    private void change_password() {

        SharedPreferences log_sesion = getSharedPreferences("log_session", MODE_PRIVATE);


        String user_email = log_sesion.getString("user_email", "0");
        String old_password = old_pass.getText().toString();
        String new_password = new_pass.getText().toString();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Call<LoginResponse> call = apiService.change_password(user_email, old_password, new_password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                String success_code = response.body().getCode().toString();

                if (success_code.equals("101")) {
                    Toast.makeText(MainActivity.this, R.string.pass_change_success, Toast.LENGTH_SHORT).show();
                    old_pass.setText("");
                    new_pass.setText("");
                    comfirn_pass.setText("");
                    dialog.dismiss();
                } else {

                    Toast.makeText(MainActivity.this, R.string.pass_not_change, Toast.LENGTH_SHORT).show();
                    old_pass.setText("");
                    new_pass.setText("");
                    comfirn_pass.setText("");
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.pass_not_change, Toast.LENGTH_SHORT).show();

            }
        });


    }

//    private void changePasswordProcess(String email, String old_password, String new_password) {
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URLL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
//
//        User user = new User();
//        user.setEmail(email);
//        user.setOld_password(old_password);
//        user.setNew_password(new_password);
//        ServerRequest request = new ServerRequest();
//        request.setOperation(Constants.CHANGE_PASSWORD_OPERATION);
//        request.setUser(user);
//        Call<ServerResponse> response = requestInterface.operation(request);
//
//        response.enqueue(new Callback<ServerResponse>() {
//            @Override
//            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
//
//                ServerResponse resp = response.body();
//                if (resp.getResult().equals(Constants.SUCCESS)) {
//                    progress.setVisibility(View.GONE);
//                    tv_message.setVisibility(View.GONE);
//                    dialog.dismiss();
//                    Snackbar.make(parentLayout, resp.getMessage(), Snackbar.LENGTH_LONG).show();
//
//                } else {
//                    progress.setVisibility(View.GONE);
//                    tv_message.setVisibility(View.VISIBLE);
//                    tv_message.setText(resp.getMessage());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ServerResponse> call, Throwable t) {
//
//                Log.d(Constants.TAG, "failed");
//                progress.setVisibility(View.GONE);
//                tv_message.setVisibility(View.VISIBLE);
//                tv_message.setText(t.getLocalizedMessage());
//
//            }
//        });
//    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }


    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure want to exit!");
        builder.setCancelable(true);
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Exit!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//            finish();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_item_share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url3 + " ");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

                break;

            case R.id.menu_item_notification:
                Toast.makeText(MainActivity.this, "Notification", Toast.LENGTH_SHORT).show();
//                Intent intentNotification = new Intent(this, NotificationsActivity.class);
//                this.startActivity(intentNotification);
//                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                break;
            case R.id.menu_item_privacy_policy:
                Toast.makeText(MainActivity.this, "Privacy Policy", Toast.LENGTH_SHORT).show();
//                Intent intentPrivacyPolicy = new Intent(this, PrivacyPolicyMainActivity.class);
//                this.startActivity(intentPrivacyPolicy);
//                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                break;
            case R.id.menu_item_about_us:
                Toast.makeText(MainActivity.this, "About Us", Toast.LENGTH_SHORT).show();
//                Intent intentAboutUs = new Intent(this, AboutUsMainActivity.class);
//                this.startActivity(intentAboutUs);
//                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                break;
            case R.id.menu_item_contact_us:
                Toast.makeText(MainActivity.this, "Contact Us", Toast.LENGTH_SHORT).show();
//                Intent intentContactUs = new Intent(this, ContactUsMainActivity.class);
//                this.startActivity(intentContactUs);
//                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                break;
            case R.id.menu_item_change_password:
                showDialogs();
                break;

            default:
                return (toggle.onOptionsItemSelected(item)) || super.onOptionsItemSelected(item);
        }
        return true;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.nav_one) {
            Toast.makeText(MainActivity.this, "Navigation One", Toast.LENGTH_SHORT).show();


        }

        if (id == R.id.nav_two) {
            Toast.makeText(MainActivity.this, "Navigation Two", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.nav_three) {
            Toast.makeText(MainActivity.this, "Navigation Three", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_four) {
            Toast.makeText(MainActivity.this, "Navigation Four", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.nav_five) {
            Toast.makeText(MainActivity.this, "Navigation Five", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_logout) {


            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Do You Want To Logout!");
            builder.setCancelable(true);
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        logout();
                    }
                    //startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, app_url3 + " ");
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        return false;

    }


    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("log_session", MODE_PRIVATE).edit();
        editor.putString("loggedin", "0");
        editor.putString("user_email", "");
        editor.putString("user_name", "");
        editor.putString("user_phone", "");
        editor.commit();
        session.logoutUser();
        Toast.makeText(this, "Logged Out", Toast.LENGTH_LONG).show();
        goToLogin();
    }

    private void goToLogin() {

        startActivity(new Intent(this, LoginActivity.class));
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
}