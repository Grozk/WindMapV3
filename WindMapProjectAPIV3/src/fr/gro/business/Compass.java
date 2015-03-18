package fr.gro.business;


public class Compass {
//implements SensorEventListener, LocationListener{
//
//
//    //Fields dealing with display classes
//    Draw draw;
//    Draw2 draw2;
//    Draw3 draw3;
//    
//    //Fields dealing with location sensor
//    LocationManager locationManager;
//    String bestProvider;
//        
//    //Fields dealing with acceleration sensor
//    Sensor sensor;
//    SensorManager sensorManager;
//    final int rate = SensorManager.SENSOR_DELAY_GAME;
//    
//    /**
//     * Method called at startup
//     * 
//     * @param savedInstanceState Satisfies "extends Activity"
//     */
//    public void onCreate(Bundle savedInstanceState) 
//    {
//        super.onCreate(savedInstanceState);
//        
//        //accel
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
//        
//        //GPS (location)
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        bestProvider = locationManager.getBestProvider(criteria, false);
//        
//        //drawing and display
//        draw = new Draw(this);
//        draw2 = new Draw2(this);
//        draw3 = new Draw3(this);
//        
//        //layout
//        LinearLayout view = new LinearLayout(this);
//        view.setOrientation(LinearLayout.VERTICAL);
//        
//        LinearLayout topView = new LinearLayout(this);
//        topView.setOrientation(LinearLayout.HORIZONTAL);
//        topView.addView(draw3, 160, 185);
//        topView.addView(draw2, 160, 185);
//        
//        view.addView(topView, 320, 185);
//        view.addView(draw, 320, 245);
//        
//        setContentView(view);
//    }
//
//    ///////////////// Basics
//    /**
//     * Called when the application is in the background
//     */
//    public void onPause()
//    {
//        super.onPause();
//        sensorManager.unregisterListener(this);
//        locationManager.removeUpdates(this);
//        //this is important because Android applications
//        //do not close, they are in the background
//        //so resources would be hogged otherwise
//    }
//    
//    /**
//     * Called when the application is started
//     * or in the foreground again
//     */
//    public void onResume()
//    {
//        super.onResume();
//        sensorManager.registerListener(this, sensor, rate);
//        locationManager.requestLocationUpdates(
//                        bestProvider, 0, 0, this);
//    }
//    
//    //==================Accel=====================
//    /**
//     * Called when the values of the acceleration sensor changes
//     * 
//     * @param e Details about the change
//     */
//        public void onSensorChanged(SensorEvent e) 
//        {
//                //update
//                Base.accelValues[0] = e.values[0];
//                Base.accelValues[1] = (-1*Math.abs(e.values[2])+90)/90; 
//                Base.accelValues[2] = (-1*Math.abs(e.values[1])+90)/90;
//                
//                //refresh displays
//                draw.invalidate();
//                draw2.invalidate();
//                draw3.invalidate();
//        }
//    
//        /**
//         * Called when accuracy of the sensor is changed
//         * 
//         * @param sen Which sensor's accuracy changed
//         * @param acc The new accuracy degree
//         */
//        public void onAccuracyChanged(Sensor sen, int acc) 
//        {
//        }
//
//        //==================GPS=====================
//        /**
//         * Called when the location changes
//         * 
//         *  @param location The new location
//         */
//        public void onLocationChanged(Location location) 
//        {
//                //update location
//                if(location != null)
//                        Base.location = new Coordinate(location.getLongitude(), location.getLatitude());
//        }
//
//        /**
//         * Called when the provider of location (satellite)
//         * is disabled
//         * 
//         * @param arg0 The provider
//         */
//        public void onProviderDisabled(String arg0) 
//        {
//        }
//
//        /**
//         * Called when the provider of location is enabled
//         * 
//         * @param arg0 The provider
//         */
//        public void onProviderEnabled(String arg0) 
//        {               
//        }
//
//        /**
//         * Called when status of the provider of location changes
//         * 
//         * @param arg0 The provider
//         * @param arg1 The status
//         * @param arg2 Additional information
//         */
//        public void onStatusChanged(String arg0, int arg1, Bundle arg2) 
//        {
//        }
//
//	
//	
}
