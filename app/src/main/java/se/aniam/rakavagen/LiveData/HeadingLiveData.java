package se.aniam.rakavagen.LiveData;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


import androidx.lifecycle.MutableLiveData;

public class HeadingLiveData extends MutableLiveData<Float> implements SensorEventListener {

    private GeomagneticField geoField;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor magneticFieldSensor;

    private float[] gravity = new float[3];
    private float[] geomagnetic = new float[3];
    private float[] R = new float[9];
    private float[] I = new float[9];

    private float azimuth;

    /**
     * HeadingLiveData contains the devices current heading. This is calculated using the phones
     * accelerometer and its magnetic field sensor. Uses application context to access device sensors
     * and the current location to calculate the magnetic field at the current latitude/longitude.
     * @param context
     * @param currentLocation
     */
    public HeadingLiveData(final Context context, LocationLiveData currentLocation) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        geoField = new GeomagneticField(
                (float) currentLocation.getValue().getLatitude(),
                (float) currentLocation.getValue().getLongitude(),
                (float) currentLocation.getValue().getAltitude(),
                System.currentTimeMillis());

        sensorManager.registerListener(this, accelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magneticFieldSensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * When a sensor in the device is changed this method is triggered and calculates the
     * current heading which is then placed in the LiveData and emitted to all observers.
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.90f;

        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                gravity[0] = alpha * gravity[0] + (1 - alpha)
                        * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha)
                        * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha)
                        * event.values[2];
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                geomagnetic[0] = alpha * geomagnetic[0] + (1 - alpha)
                        * event.values[0];
                geomagnetic[1] = alpha * geomagnetic[1] + (1 - alpha)
                        * event.values[1];
                geomagnetic[2] = alpha * geomagnetic[2] + (1 - alpha)
                        * event.values[2];
            }

            boolean success = SensorManager.getRotationMatrix(R, I, gravity,
                    geomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = (float) Math.toDegrees(orientation[0]); // orientation
                if (geoField != null) {
                    azimuth += geoField.getDeclination();
                }

                if (azimuth < 0) {
                    azimuth = azimuth + 360;
                }
                setValue(azimuth);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not used
    }

}
