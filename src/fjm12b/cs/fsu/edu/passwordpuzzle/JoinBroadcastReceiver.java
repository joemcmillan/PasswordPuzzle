package fjm12b.cs.fsu.edu.passwordpuzzle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import fjm12b.cs.fsu.edu.passwordpuzzle.MultiplayerSignup;

public class JoinBroadcastReceiver extends BroadcastReceiver {

	private final static String TAG = "MyBroadcastReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
	    
		Log.i(TAG, "Broadcast received:" + intent.toString());
		
		Toast.makeText(
				context, 
				"Broadcast Received!", 
				Toast.LENGTH_LONG).show();
		context.unregisterReceiver(this);
		
	}

}