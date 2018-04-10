package com.techkshetrainfo.cltenotes.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

  public static final String BASE_URL = "http://techkshetrainfo.com/cltebook/index.php/api/";

  //public static final String BASE_URL = "http://www.techkshetrainfo.com/keonicsapp/keonicsadmin/index.php/api/";

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}