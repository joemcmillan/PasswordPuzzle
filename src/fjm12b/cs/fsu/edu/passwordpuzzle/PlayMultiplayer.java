package fjm12b.cs.fsu.edu.passwordpuzzle;

import java.util.Random;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayMultiplayer extends Activity {

    private static final int NOTIFY_ME_ID = 1987;
	String mynumber,
			redTeam,
			phonetwo,
			blueTeam,
			phonethree,
			phonefour,
			this_word,
			hint;
	String[] words = {"ball", "bat", "bed", "book", "boy", "bun", "can", "cake", "cap", 
			"car", "cat", "cow", "cub", "cup", "dad", "day", "dog", "doll", "dust", "fan", 
			"feet", "girl", "gun", "hall", "hat", "hen", "jar", "kite", "man", "map", "men", 
			"mom", "pan", "pet", "pie", "pig", "pot", "rat", "son", "sun", "toe", "tub", "van",
			"actor", "airplane", "army", "baseball", "beef", "birthday", "boy", "brush", "butter", 
			"cast", "cave", "cherry", "cracker", "dinner", "elbow", "face", "fireman", "flavor", 
			"gate", "glove", "glue", "goldfish", "goose", "grain", "hair", "haircut", "hobbies", "holiday", 
			"hot", "mailbox", "number", "pail", "pancake", "pear", "pest", "popcorn", "queen", "quiet ", 
			"rainstorm", "street", "sugar", "toothpaste", "wood"};
	int numWords = 87;
	int next,
	redscore, bluescore;
	TextView word, hintList, teamOne, teamTwo, redScore, blueScore, descrip;
	EditText HINT;
	ImageView SEND;
	String Sender;
	String[] info;
	Random r;
	int first = 0;
	BroadcastReceiver receiverOne;
	NotificationManager mgr;
	Notification note;
	PendingIntent i;
	String welcome = "SecretPassword session has begun! Please open the app now to begin playing! Basic info:";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playmulti);
		
		redscore = 0;
		bluescore = 0;
		
		descrip = (TextView)findViewById(R.id.description);
		SEND = (ImageView)findViewById(R.id.sendhint);
		HINT = (EditText)findViewById(R.id.hint);
		word = (TextView)findViewById(R.id.word);
		hintList = (TextView)findViewById(R.id.hintList);
		teamOne = (TextView)findViewById(R.id.teamone);
		teamTwo = (TextView)findViewById(R.id.teamtwo);
		redScore = (TextView)findViewById(R.id.scoreRed);
		blueScore = (TextView)findViewById(R.id.scoreBlue);
		
		Intent intent = getIntent();
		mynumber = intent.getStringExtra("mn");
		redTeam = intent.getStringExtra("red");
		phonetwo = intent.getStringExtra("nf");
		blueTeam = intent.getStringExtra("blue");
		phonethree = intent.getStringExtra("ns");
		phonefour = intent.getStringExtra("nt");
		
		descrip.setText("Give Hints Based on this word:");
		teamOne.setText(redTeam);
		teamTwo.setText(blueTeam);
		
		String messageOne = new StringBuilder(welcome).append(redTeam).append(":").append(mynumber).append(":").append(blueTeam).append(":").append(phonethree).toString();
		String messageTwo = new StringBuilder(welcome).append(redTeam).append(":").append(phonetwo).append(":").append(blueTeam).append(":").append(phonefour).toString();
		String messageThree = new StringBuilder(welcome).append(redTeam).append(":").append(phonethree).append(":").append(blueTeam).append(":").append(mynumber).toString();
		//text people display name and phone number
		
		SmsManager.getDefault().sendTextMessage(phonetwo, null, messageOne, null,null);
		SmsManager.getDefault().sendTextMessage(phonethree, null, messageTwo, null,null);
		SmsManager.getDefault().sendTextMessage(phonefour, null, messageThree, null,null);
		
		r = new Random();
		this_word = words[r.nextInt(numWords)];
		word.setText(this_word);
		SEND.setVisibility(View.VISIBLE);
		
		IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(1000);
        
        mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
 
        note = new Notification(R.drawable.password,
                 "Welcome", System.currentTimeMillis());
        i = PendingIntent.getActivity(this, 0, new Intent(
                this, PlayMultiplayer.class), Notification.FLAG_ONGOING_EVENT);
 
        note.setLatestEventInfo(this, "SecretPassword", "WordyGame with Friends",
                i);
        // note.number = ++count;
        note.flags |= Notification.FLAG_ONGOING_EVENT;
 
        mgr.notify(NOTIFY_ME_ID, note);
 
    
        receiverOne = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
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
                        info = message.split(":");
                        Log.i("tag", message);
                        Log.i("tag", Sender);
                        // prevent any other broadcast receivers from receiving broadcast
                        Sender = Sender.replaceAll("[^\\d.]", "");
                        if(Sender.contains(phonefour)){
                        	redScore.setText(info[1]);
                        	redscore = Integer.parseInt(info[1]);
                        	blueScore.setText(info[2]);
                        	bluescore = Integer.parseInt(info[2]);
                        	if(info[0].contains("quit")){
                        		View v = null;
                        		quitGame(v);
                        	}
                        	
                        	if(!info[1].equals("red") && !info[1].equals("blue") && !info[1].equals("tie")){
                        	abortBroadcast();
                        	word.setText(info[0]);
                        	
                        	StringBuilder bob = new StringBuilder();
                        	for(int i = 3; i < info.length; i++){
                        		 bob.append(info[i]).append(" ");
                        		 i++;
                        	}
                        	
                        	hintList.setText(bob.toString());
                        	SEND.setVisibility(View.VISIBLE);
                        	info[info.length-1] = info[info.length-1].toLowerCase();
                        	notifyMe();
                        	if(info[info.length-1].equals(info[0])){
                        		this_word = words[r.nextInt(numWords)];
                        		word.setText(this_word);
                        		hintList.setText(" ");
                        		bluescore += 10;
                        		if(bluescore == 30){
                    				View v = null;
                    				quitGame(v);
                    			}
                        		info[2] = String.valueOf(bluescore);
                        		blueScore.setText(info[2]);
                        		String[] temp = new String[3];     
                        		temp[0]= this_word;                 
                        		temp[1] = info[1];
                        		temp[2]= String.valueOf(bluescore);                  
                        		info = temp;                       
                        	}
                         }
                         
                        }
                         
                        
                         
                    }
                }
        };
        registerReceiver(receiverOne, filter);
		
		
		
	}
	public void notifyMe(){
		note.setLatestEventInfo(this, "SecretPassword", "its your turn",
                i);
		mgr.notify(NOTIFY_ME_ID, note);
	}
	public void sendHint(View v){
		hint = HINT.getText().toString();
		hint = hint.toLowerCase();
		if(hint.length() > 12){
			hint = hint.substring(0, 12);
		}
		StringBuilder message = new StringBuilder();
		if(first==0){
		message.append(this_word).append(":").append(String.valueOf(redscore)).append(":").append(String.valueOf(bluescore)).append(":");
		}
		else{
			for(int i=0; i < (info.length); i++){
				message.append(info[i]).append(":"); 
				}                                    
		}
		message.append(hint);
		hint = message.toString();
		SmsManager.getDefault().sendTextMessage(phonetwo, null, hint, null,null);
		note.setLatestEventInfo(this, "SecretPassword", "waiting",
                i);
		mgr.notify(NOTIFY_ME_ID, note);
		SEND.setVisibility(View.INVISIBLE);
		HINT.setText("");
		hintList.setText("");
	}
	
	public void quitGame(View v){
		unregisterReceiver(receiverOne);
		mgr.cancel(NOTIFY_ME_ID);
		if(redscore > bluescore){
			String message = "quit:" + String.valueOf(redscore) + ":" + String.valueOf(bluescore);
			Intent i = new Intent(this, RedWins.class);

			SmsManager.getDefault().sendTextMessage(phonetwo, null, message, null,null);
			startActivity(i);
		}
		else if (bluescore > redscore){
			String message = "quit:" + String.valueOf(redscore) + ":" + String.valueOf(bluescore);

			SmsManager.getDefault().sendTextMessage(phonetwo, null, message, null,null);
			Intent i = new Intent(this, BlueWins.class);
			startActivity(i);
		}else{
			String message = "quit:" + String.valueOf(redscore) + ":" + String.valueOf(bluescore);

			SmsManager.getDefault().sendTextMessage(phonetwo, null, message, null,null);
			Intent i = new Intent(this, Tie.class);
			startActivity(i);
		}
	}
	
	@Override
	public void onBackPressed() {
		mgr.cancel(NOTIFY_ME_ID);
		SmsManager.getDefault().sendTextMessage(phonetwo, null, "quit:tie:", null,null);
		Intent intent = new Intent(this, Main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}