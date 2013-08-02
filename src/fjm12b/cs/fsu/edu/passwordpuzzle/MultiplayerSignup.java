package fjm12b.cs.fsu.edu.passwordpuzzle;

import java.util.ArrayList;

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
import android.widget.Toast;

public class MultiplayerSignup extends Activity {
	final static int PICK_CONTACT = 1234;
	TextView playerone,
	playertwo,
	playerthree;
	String DisplayNameOne,
		   DisplayNameTwo,
		   DisplayNameThree,
		   DisplayNameFour,
			PhoneNumberOne,
			PhoneNumberTwo,
			PhoneNumberThree,
			PhoneNumberFour,
			MyNumber,
			Sender;
	BroadcastReceiver receiver;
	EditText myname;
	int guests;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addplayers);
		playerone = (TextView)findViewById(R.id.playeroneName);
		playertwo = (TextView)findViewById(R.id.playertwoName);
		playerthree = (TextView)findViewById(R.id.playerthreeName);
		myname = (EditText)findViewById(R.id.myName);
		guests = 0;
	}
	
	
    public void onClick(View v) {

        Intent it= new Intent(Intent.ACTION_PICK,  Contacts.CONTENT_URI);
        startActivityForResult(it, 1);

    }
    
    public void pickAgain(View v){
    	Intent it= new Intent(Intent.ACTION_PICK,  Contacts.CONTENT_URI);
        startActivityForResult(it, 2);
    }
    public void pickAgainAgain(View v){
    	Intent it= new Intent(Intent.ACTION_PICK,  Contacts.CONTENT_URI);
        startActivityForResult(it, 3);
    }
    
    
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent
   data){
     super.onActivityResult(reqCode, resultCode, data);

     switch(reqCode){
        case (1):
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
             PhoneNumberOne = phones.getString(phones.getColumnIndex("data1"));
             phones.moveToFirst();
             Cursor names = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,ContactsContract.Data.CONTACT_ID + " = " + id, null, null);
             names.moveToFirst();
             DisplayNameOne = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
             playerone.setText(DisplayNameOne);
             String s = myname.getText().toString();
             
             StringBuilder sb = new StringBuilder();
             String welcome = "Welcome you have been invited to play SecretPassword with "+ s;
             String help = "If you have not already downloaded the app you can find it in the Google Playstore. Open app and select accept invite to get started.";
             SmsManager sms = SmsManager.getDefault();
             sms.sendTextMessage(PhoneNumberOne, null, welcome, null,null);
             sms.sendTextMessage(PhoneNumberOne, null, help, null,null);             
             //receiver = new JoinBroadcastReceiver();
             IntentFilter filter = new IntentFilter();
             filter.addAction("android.provider.Telephony.SMS_RECEIVED");
             filter.setPriority(1000);
             
             BroadcastReceiver receiverOne = new BroadcastReceiver() {
                 @Override
                 public void onReceive(Context context, Intent intent) {
                     Log.i("tag", PhoneNumberOne);
                     Log.i("tag", DisplayNameOne);
                     
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
                             String[] info = message.split(":");
                             Log.i("tag", message);
                             Log.i("tag", Sender);
                             // prevent any other broadcast receivers from receiving broadcast
                             Sender = Sender.replaceAll("[^\\d.]", "");
                             PhoneNumberOne = PhoneNumberOne.replaceAll("[^\\d.]", "");
                             
                             if(Sender.contains(PhoneNumberOne) && info.length == 2){
                            	 MyNumber = message.replaceAll("[^\\d.]", "");
                            	 PhoneNumberOne = Sender;
                              this.abortBroadcast();
                              guests++;
                              Log.i("guests", String.valueOf(guests));
                              	if(guests == 3){
                                  unregisterReceiver(this);
                                 
                                  launch();   
                              	}
                              }
                             
                              if(Sender.contains(PhoneNumberTwo) && info.length == 2){

                             	 MyNumber = message.replaceAll("[^\\d.]", "");
                            	  PhoneNumberTwo = Sender;
                            	  abortBroadcast();
                                  guests++;
                                  Log.i("guests", String.valueOf(guests));
                                  if(guests == 3){
                                	  unregisterReceiver(this);
                                	 
                                	  launch();
                                  }
                            	  
                              }
                              
                              if(Sender.contains(PhoneNumberThree) && info.length == 2){

                             	 MyNumber = message.replaceAll("[^\\d.]", "");
                            	  PhoneNumberThree = Sender;
                            	  abortBroadcast();
                                  guests++;
                                  Log.i("guests", String.valueOf(guests));
                                  if(guests == 3){
                                	  unregisterReceiver(this);
                                	  
                                	  launch();  
                                  }
                              }
                             
                              
                         }
                     }
             };
             registerReceiver(receiverOne, filter);
             
             
             
           }
           }
          }
        break;
        case (2):
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
               PhoneNumberTwo = phones.getString(phones.getColumnIndex("data1"));
               PhoneNumberTwo = PhoneNumberTwo.replaceAll("[^\\d.]", "");
               phones.moveToFirst();
               Cursor names = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,ContactsContract.Data.CONTACT_ID + " = " + id, null, null);
               names.moveToFirst();
               DisplayNameTwo = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
               //Log.i("names", cNumber);
               playertwo.setText(DisplayNameTwo);
               //Log.i("names", GivenName);
               String s = myname.getText().toString();
               StringBuilder sb = new StringBuilder();
               sb.append("Welcome you have been invited to play SecretPassword with ").append(s).append(". If you have not already downloaded the app you can find it in the Google Playstore. Please open app and select accept invite to get started.");
               String welcome = sb.toString();
               SmsManager.getDefault().sendTextMessage(PhoneNumberTwo, null, welcome, null,null);
               
               
             }
             }
            }
        break;
        case (3):
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
               PhoneNumberThree = phones.getString(phones.getColumnIndex("data1"));
               PhoneNumberThree = PhoneNumberThree.replaceAll("[^\\d.]", "");
               phones.moveToFirst();
               Cursor names = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null,ContactsContract.Data.CONTACT_ID + " = " + id, null, null);
               names.moveToFirst();
               DisplayNameThree = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
               Log.i("names", PhoneNumberThree);
               playerthree.setText(DisplayNameThree);
               //Log.i("names", GivenName);
               String s = myname.getText().toString();
               StringBuilder sb = new StringBuilder();
               sb.append("Welcome you have been invited to play SecretPassword with ").append(s).append(". If you have not already downloaded the app you can find it in the Google Playstore. Please open app and select accept invite to get started.");
               String welcome = sb.toString();
               SmsManager.getDefault().sendTextMessage(PhoneNumberThree, null, welcome, null,null);
               
              
             }
             }
            }
        break;
     }
}//onActivityResult
    
    public void launch(){
    	
    	String redTeam = new StringBuilder(myname.getText().toString()).append(" & ").append(DisplayNameOne).toString();
    	String blueTeam = new StringBuilder(DisplayNameTwo).append(" & ").append(DisplayNameThree).toString();
    	
    	Intent i = new Intent(this, PlayMultiplayer.class);
    	i.putExtra("mn", MyNumber);
    	i.putExtra("red", redTeam);
    	i.putExtra("nf", PhoneNumberOne);
    	i.putExtra("blue", blueTeam);
    	i.putExtra("ns", PhoneNumberTwo);
    	i.putExtra("nt", PhoneNumberThree);
    	startActivity(i);
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}