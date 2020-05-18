package se.aniam.rakavagen.LiveData;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import se.aniam.rakavagen.models.Station;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

public class DistanceLiveDataTestInstrumented {

    @Mock
    LocationLiveData locationLiveData;

    @Mock
    MutableLiveData<Station> stationMutableLiveData;

    private Location location;

    private DistanceLiveData distanceLiveData;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        location = new Location("mock");
        location.setLatitude(59.2608781);
        location.setLongitude(18.048557);
        Station station = new Station("Högdalen", 59.263201, 18.042558);
        given(locationLiveData.getValue()).willReturn(location);
        given(stationMutableLiveData.getValue()).willReturn(station);
        distanceLiveData = new DistanceLiveData(locationLiveData, stationMutableLiveData);
    }

    @Test
    public void computeDistance() {
        assertEquals(429, distanceLiveData.computeDistance(locationLiveData.getValue(), stationMutableLiveData.getValue()), 1);
    }
}