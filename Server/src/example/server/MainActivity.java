package example.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

	
        LocationManager locationManager;
		
		DatagramPacket packet;
		int minBufferSize;
	    int port=50005;
	    WifiManager wifiMgr;
	    
		
	    ImageView iv1;
	    ImageView iv2;
	    Button jbt1;
	    
	    String ipAdd="";
	    Vector<String>v=new Vector<String>(2);
	    String temp="";
	    
	    TextView tv3;
	    Handler handler;
	    
	    Timer t1;
	    boolean status=false;
	    
	    Random r = new Random();
	    
	    
	    Button jbtAddIP;
	    Button jbtOK;
        EditText jtfIP;	    
	    Vector<String>vIP=new Vector<String>();
	    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	        iv1=(ImageView)findViewById(R.id.imageView1);
	        iv2=(ImageView)findViewById(R.id.imageView2);
	        jbt1=(Button)findViewById(R.id.button1);
	        tv3=(TextView)findViewById(R.id.textView3);
	        jbtAddIP=(Button)findViewById(R.id.button2);
	        jbtOK=(Button)findViewById(R.id.button3);
	        jtfIP=(EditText)findViewById(R.id.editText2);
	        
	        
	        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1, this);
			
			
	        handler=new Handler();
	        
	        wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
			  
	        CheckValidty();
		    
		    jbtAddIP.setOnClickListener(new View.OnClickListener() {
				
						public void onClick(View arg0) {
							vIP.add(jtfIP.getText().toString());
							jtfIP.setText("");
						}
					});
		    
		    
		    jbtOK.setOnClickListener(new View.OnClickListener() {
				
						public void onClick(View arg0) {
							if(vIP.size()>0)
								jbt1.setEnabled(true);
						}
					});
		    
		    jbt1.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View arg0) {
					if(status){
						status=false;
						jbt1.setText("Start");
						}
					else{ status=true;jbt1.setText("Stop");}
				}
			});
		    
  
		    t1=new Timer();
		    t1.scheduleAtFixedRate(new TimerTask() {
				
				@Override
				public void run() {

					if(status){
						
							try{
								
								String stran= v.elementAt(0);
								byte[] arr =stran.getBytes();
								
								Thread.sleep(1000);
								
								for(int i=0;i<vIP.size();i++){
									
									DatagramSocket sock= new DatagramSocket();
				 	    			DatagramPacket packet;
				 	                                      
				 	                InetAddress ip = InetAddress.getByName(vIP.elementAt(i));
				 	  
				 	                packet = new DatagramPacket (arr,arr.length,ip,port);
				 	                sock.send(packet);
				 	             
				 	                sock.disconnect();
				 	                sock.close();
								}
								
								}catch(Exception sdfd){}
					}
				}
			}, 200,200);
		    
		    
	}
	
	
	
	public void onLocationChanged(Location location) {
		   
		String str = "Latitude: "+location.getLatitude()+" \nLongitude: "+location.getLongitude();
		Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();
		v.removeAllElements();
		v.add(str);
	}

	public void onProviderDisabled(String provider) {
		Toast.makeText(getBaseContext(), "Gps turned off ", Toast.LENGTH_LONG).show();
	}

	public void onProviderEnabled(String provider) {
		Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
	}
	
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	

	public void CheckValidty(){

	    	
	  	  if(wifiMgr.isWifiEnabled()){
			  
	  	       status=false; 
	           iv2.setVisibility(ImageView.VISIBLE);
	           iv1.setVisibility(ImageView.INVISIBLE);
	  	}else{
	  		
	  		  Toast.makeText(getBaseContext(),"Your WiFi adapter turned off !!!",Toast.LENGTH_LONG).show();
	  		  iv1.setVisibility(ImageView.VISIBLE);
	  		  iv2.setVisibility(ImageView.INVISIBLE);
	  		  status=false;
	  	}
	}



	
	protected void onPause() {
		super.onPause();
		
		status=false;
	}

	protected void onStop() {
		super.onStop();
		
		status=false;
	}

	
	
	
}