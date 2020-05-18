package se.aniam.rakavagen.webapi;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.aniam.rakavagen.models.RetrievedStations;

import static org.junit.Assert.*;

public class ApiServiceTest {

    private ApiService apiService;
    private CountDownLatch latch;

    /**
     * This method tests the applications API calls. For now this will do, but I'm aware that testing
     * against a real API is not optimal. This should be rewritten in the future to mock the web API.
     *
     * @throws InterruptedException
     */
    @Test
    public void getClosestStation() throws InterruptedException {

        // Testing best case scenario -----------------------------------------------------------------------------
        latch = new CountDownLatch(1);
        // Lat/Lng of Högdalen centrum
        String lat = "59.263689";
        String lng = "18.041363";

        apiService = HttpClient.getSLRetrofitInstance().create(ApiService.class);
        Call<RetrievedStations> correctCall = apiService.getClosestStation(lat, lng);

        correctCall.enqueue(new Callback<RetrievedStations>() {
            @Override
            public void onResponse(Call<RetrievedStations> call, Response<RetrievedStations> response) {

                RetrievedStations stations = response.body();
                assertNotNull("correctCall: Stations was null", stations);
                assertTrue("correctCall: Station list size was not 1", stations.getStopLocationOrCoordLocation().size() == 1);
                assertTrue("correctCall: Name was not Högdalen", stations.getStopLocationOrCoordLocation().get(0).getStopLocation().getName().equals("Högdalen"));
                latch.countDown();

            }

            @Override
            public void onFailure(Call<RetrievedStations> call, Throwable t) {
                assertTrue("correctCall: onFailure triggered", false);
                latch.countDown();
            }
        });
        assertTrue("correctCall timed out", latch.await(5000, TimeUnit.MILLISECONDS));


        // Testing no station scenario -----------------------------------------------------------------------------
        latch = new CountDownLatch(1);
        // Lat/Lng of Tyresta nationalpark
        lat = "59.156904";
        lng = "18.271764";

        apiService = HttpClient.getSLRetrofitInstance().create(ApiService.class);
        Call<RetrievedStations> noStationCall = apiService.getClosestStation(lat, lng);

        noStationCall.enqueue(new Callback<RetrievedStations>() {
            @Override
            public void onResponse(Call<RetrievedStations> call, Response<RetrievedStations> response) {

                RetrievedStations stations = response.body();
                assertNotNull("noStationCall: Response was null", stations);
                assertNotNull("noStationCall: serverVersion was null", stations.getServerVersion());
                assertNotNull("noStationCall: dialectVersion was null", stations.getDialectVersion());
                assertNotNull("noStationCall: requestId was null", stations.getRequestId());
                assertNull("noStationCall: getStopLocationOrCoordLocation() was not null", stations.getStopLocationOrCoordLocation());
                latch.countDown();

            }

            @Override
            public void onFailure(Call<RetrievedStations> call, Throwable t) {
                assertTrue("noStationCall: onFailure triggered", false);
                latch.countDown();
            }
        });
        assertTrue("noStationCall timed out", latch.await(5000, TimeUnit.MILLISECONDS));
    }
}