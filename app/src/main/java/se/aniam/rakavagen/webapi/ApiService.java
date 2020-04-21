package se.aniam.rakavagen.webapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import se.aniam.rakavagen.models.RetrievedStations;

public interface ApiService {

    @GET("/api2/nearbystopsv2.json?key=2f9a3e4e1cd94a4c9c2b6927a1cdf370&maxNo=1&r=2000&products=2")
    Call<RetrievedStations> getClosestStation(@Query("originCoordLat") String lat, @Query("originCoordLong") String lng);

}
