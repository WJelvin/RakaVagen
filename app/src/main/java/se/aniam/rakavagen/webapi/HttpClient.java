package se.aniam.rakavagen.webapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpClient {

    private static Retrofit SLRetrofit;

    private static final String SL_BASE_URL = "https://api.sl.se";

    // Retrofit instance for fetching data from SL web api
    public static Retrofit getSLRetrofitInstance() {
        if (SLRetrofit == null) {
            SLRetrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(SL_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return SLRetrofit;
    }
}
