package or.connect.highFiveSensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private SensorManager mSensorManager;
    private Sensor mAccel;

    private boolean mInitialized;

    private float mLastX, mLastY, mLastZ;
    private final float NOISE = (float) 9.0;

    private SensorEventListener mAccelSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if (!mInitialized) {
                mLastX = x;
                mLastY = y;
                mLastZ = z;
                mInitialized = true;
            } else {
                float deltaX = (mLastX - x);
                float deltaY = (mLastY - y);
                float deltaZ = (mLastZ - z);
                if (deltaX < NOISE) deltaX = (float) 0.0;
                if (deltaY < NOISE) deltaY = (float) 0.0;
                if (deltaZ < NOISE) deltaZ = (float) 0.0;
                mLastX = x;
                mLastY = y;
                mLastZ = z;

                if ((deltaX > NOISE) && (deltaX > deltaY) && (deltaX > deltaZ)) {
                    Log.d("HighFiveSensor", event.toString() + " horizontal move");
                    Toast.makeText(getBaseContext(), "Horizontal move", Toast.LENGTH_SHORT).show();
                } else if ((deltaY > NOISE) && (deltaY > deltaX) && (deltaY > deltaZ)) {
                    Log.d("HighFiveSensor", event.toString() + " vertical move");
                    Toast.makeText(getBaseContext(), "Vertical move", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d("HighFiveSensor", sensor.toString() + " - " + accuracy);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (mAccel != null) {
            mSensorManager.registerListener(mAccelSensorListener, mAccel,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAccel != null) {
            mSensorManager.unregisterListener(mAccelSensorListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get sensor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Get the default sensor of specified type
        mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mAccel != null) {
            mSensorManager.registerListener(mAccelSensorListener, mAccel,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

