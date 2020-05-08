package se.aniam.rakavagen.LiveData;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import org.junit.Test;

import se.aniam.rakavagen.models.Station;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


import static org.junit.Assert.*;

public class BearingLiveDataTest {

    @Test
    public void computeBearing() {
        LocationLiveData mockLoc = mock(LocationLiveData.class);
        MutableLiveData<Station> mockStation = mock(MutableLiveData.class);
        BearingLiveData bld = new BearingLiveData(mockLoc, mockStation);

        Location loc = mock(Location.class);

        given(loc.getLatitude()).willReturn(59.2608781);
        given(loc.getLongitude()).willReturn(18.048557);

        Station station = new Station("Högdalen", 59.263049, 18.042573);

        assertEquals(305.3693789695906, bld.computeBearing(loc, station), 0);
    }
}