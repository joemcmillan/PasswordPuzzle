package fjm12b.cs.fsu.edu.passwordpuzzle;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;




public class Join extends Activity {

	Button playerone,
	playertwo,
	playerthree;
	String DisplayName,
	PhoneNumber,
	Sender;
	BroadcastReceiver receiver;
	TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joingame);
		
		tv = (TextView)findViewById(R.id.verify);
	playerone = (Button)findViewById(R.id.joinplayer);
}
	public void onClick(View v){
		Intent it= new Intent(Intent.ACTION_PICK,  Contacts.CONTENT_URI);
        startActivityForResult(it, 10);

	}
	
	 @Override
	    public void onActivityResult(int reqCode, int resultCode, Intent
	   data){
	     super.onActivityResult(reqCode, resultCode, data);

	     switch(reqCode){
	        case (10):
	          if (resultCode == Activity.RESULT_OK)
	          {
	              Uri contactData = data.getData();
	              Cursor c = managedQuery(contactData, null, null, null, null);
	           if (c.moveToFirst()) {
	           String id =   
	             c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

	           String hasPhone =
	           c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

	           if (hasPhone.equalsIgnoreCase("1")) {
	          Cursor phones = getContentResolver().query( 
	                       ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
	                       ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id, 
	                       null, null);
	             phones.moveToFirst();
	             PhoneNumber = phones.getString(phones.getColumnIndex("data1"));
	             phones.moveToFirst();
	             Cursor names = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,ContactsContract.Data.CONTACT_ID + " = " + id, null, null);
	             names.moveToFirst();
	             DisplayName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	             playerone.setText(DisplayName);
	             tv.setText("waiting on room to fill");
	             String s = "Game has begun! Press notification to get started :" + PhoneNumber;
	             SmsManager.getDefault().sendTextMessage(PhoneNumber, null, s, null,null);
	             Log.i("send2host", "Join");
	             
	             IntentFilter filter = new IntentFilter();
	             filter.addAction("android.provider.Telephony.SMS_RECEIVED");
	             filter.setPriority(1000);
	             
	             receiver = new BroadcastReceiver() {
	                 @Override
	                 public void onReceive(Context context, Intent intent) {
	                     Log.i("choseme", PhoneNumber);
	                     Log.i("choseme", DisplayName);
	                     
	                         Bundle bundle = intent.getExtras();
	                         if (bundle != null) {
	                             // get sms objects
	                             Object[] pdus = (Object[]) bundle.get("pdus");
	                             if (pdus.length == 0) {
	                                 return;
	                             }
	                             // large message might be broken into many
	                             SmsMessage[] messages = new SmsMessage[pdus.length];
	                             StringBuilder sb = new StringBuilder();
	                             for (int i = 0; i < pdus.length; i++) {
	                                 messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
	                                 sb.append(messages[i].getMessageBody());
	                             }
	                             Sender = messages[0].getOriginatingAddress();
	                             String message = sb.toString();
	                             String[] separated = message.split(":");
	                             Log.i("sep1", separated[0]);
	                             Log.i("separated2", separated[1]);
	                             Log.i("tag", Sender);
	                             // prevent any other broadcast receivers from receiving broadcast
	                             Sender = Sender.replaceAll("[^\\d.]", "");
	                             PhoneNumber = PhoneNumber.replaceAll("[^\\d.]", "");
	                             if(Sender.contains(PhoneNumber)){
	                            	 Log.i("abort", "aborted");
	                              this.abortBroadcast();
	                              unregisterReceiver(this);
	                              
	                              launch(separated[1], separated[2], separated[3], separated[4]);    
	                             }
	                              
	                         }
	                     }
	             };
	             registerReceiver(receiver, filter);
	             
	           }
}
	          }}}
	 
	 public void launch(String redTeam, String teammate, String blueTeam, String oponent){
		 
		 Intent i = new Intent(this, JoinedMulti.class);
		 i.putExtra("teammate", teammate);
		 i.putExtra("red", redTeam);
		 i.putExtra("blue", blueTeam);
		 i.putExtra("oponent", oponent);
		 startActivity(i);
		 
	 }
	 
	 
}