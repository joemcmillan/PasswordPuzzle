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

public class JoinedMulti extends Activity {

    private static final int NOTIFY_ME_ID = 1987;
	String red, blue, teammate, oponent, Sender, hint;
	TextView word, hintList, teamOne, teamTwo, redScore, blueScore, descrip;
	EditText HINT;
	ImageView SEND, SENDGUESS;
	String info[];
	int scorered, scoreblue;
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
	String this_word;
	BroadcastReceiver receiverOne;
	NotificationManager mgr;
	Notification note;
	PendingIntent i;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playmulti);
		
		descrip = (TextView)findViewById(R.id.description);
		SEND = (ImageView)findViewById(R.id.sendhint);
		SENDGUESS = (ImageView)findViewById(R.id.sendguess);
		HINT = (EditText)findViewById(R.id.hint);
		word = (TextView)findViewById(R.id.word);
		hintList = (TextView)findViewById(R.id.hintList);
		teamOne = (TextView)findViewById(R.id.teamone);
		teamTwo = (TextView)findViewById(R.id.teamtwo);
		redScore = (TextView)findViewById(R.id.scoreRed);
		blueScore = (TextView)findViewById(R.id.scoreBlue);
		
		scorered = 0;
		scoreblue = 0;
		
		Intent intent = getIntent();
		red = intent.getStringExtra("red");
		blue = intent.getStringExtra("blue");
		teammate = intent.getStringExtra("teammate");
		oponent = intent.getStringExtra("oponent");
		Log.i("jm", red);
		Log.i("jm", oponent);
		
		teamOne.setText(red);
		teamTwo.setText(blue);
		redScore.setText("0");
		blueScore.setText("0");
		
		mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
		 
        note = new Notification(R.drawable.password,
                 "Welcome", System.currentTimeMillis());
        PendingIntent i = PendingIntent.getActivity(this, 0, new Intent(
                this, JoinedMulti.class), Notification.FLAG_ONGOING_EVENT);
 
        note.setLatestEventInfo(this, "SecretPassword", "WordyGame with Friends",
                i);
        // note.number = ++count;
        note.flags |= Notification.FLAG_ONGOING_EVENT;
 
        mgr.notify(NOTIFY_ME_ID, note);
 
    
		
		IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(1000);
        
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
                        if(Sender.contains(teammate)){
                        	notifyMe();
                        	Log.i("info", info[0]);
                        	abortBroadcast();
                        	redScore.setText(info[1]);
                        	scorered = Integer.parseInt(info[1]);
                        	blueScore.setText(info[2]);
                        	scoreblue = Integer.parseInt(info[2]);
                        	if(info[0].contains("quit")){
                        		
                        		View v = null;
                        		quitGame(v);
                        	}
                        	if(!info[1].equals("red") && !info[1].equals("blue") && !info[1].equals("tie")){
                        	
                        	
                        	StringBuilder bob = new StringBuilder();
                        	for(int i = 3; i < info.length; i++){
                        		bob.append(info[i]).append(" ");
                        		i++;
                        	}
                        	hintList.setText(bob.toString());
                        	if((info.length%2)==1){ 
                        		descrip.setText("Give Hints Based on this word:");
                            	SEND.setVisibility(View.VISIBLE);
                            	word.setText(info[0]);
                            	info[info.length-1] = info[info.length-1].toLowerCase();
                        		if(info[info.length-1].equals(info[0])){
                        			scorered += 10;
                        			if(scorered == 30){
                        				View v = null;
                        				quitGame(v);
                        			}
                        			hintList.setText("");
                        			redScore.setText(String.valueOf(scorered));
                        			Random r = new Random();
                        			this_word = words[r.nextInt(numWords)];
                        			word.setText(this_word);
                        			String[] temp = new String[3];
                        			temp[0]=this_word;
                        			temp[1] = String.valueOf(scorered);
                        			temp[2]= info[2];
                        			info = temp;
                        		}
                        			
                        	}
                        	else{SENDGUESS.setVisibility(View.VISIBLE);
                        	word.setText("");
                        	descrip.setText("What is your guess based on the hintlist?");}
                         }
                        }
                        
                         
                        
                         
                    }
                }
        };
        Log.i("rec", "red");
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
		if((info.length%2)==0){
			SENDGUESS.setVisibility(View.INVISIBLE);
			if(hint.equals(info[0])){
				word.setText("correct!.. waiting for opponents");
			}
			else{word.setText("sorry incorrect... waiting for opponents");}
		}
		else{SEND.setVisibility(View.INVISIBLE);}
		StringBuilder message = new StringBuilder();
		for(int i=0; i < (info.length); i++){
		message.append(info[i]).append(":");
		}
		message.append(hint);
		hint = message.toString();
		note.setLatestEventInfo(this, "SecretPassword", "waiting",
                i);
		mgr.notify(NOTIFY_ME_ID, note);
		SmsManager.getDefault().sendTextMessage(oponent, null, hint, null,null);
		HINT.setText("");
		hintList.setText("");
	}
	
	public void quitGame(View v){
		unregisterReceiver(receiverOne);
		mgr.cancel(NOTIFY_ME_ID);
		
		if((scorered > scoreblue)){
			String message = "quit:" + String.valueOf(scorered) + ":" + String.valueOf(scoreblue);
			SmsManager.getDefault().sendTextMessage(oponent, null, message, null,null);
			Intent i = new Intent(this, RedWins.class);
			startActivity(i);
		}
		else if ((scoreblue > scorered)){
			String message = "quit:" + String.valueOf(scorered) + ":" + String.valueOf(scoreblue);
			SmsManager.getDefault().sendTextMessage(oponent, null, message, null,null);
			Intent i = new Intent(this, BlueWins.class);
			startActivity(i);
		}else{
			String message = "quit:" + String.valueOf(scorered) + ":" + String.valueOf(scoreblue);
			SmsManager.getDefault().sendTextMessage(oponent, null, message, null,null);
			Intent i = new Intent(this, Tie.class);
			startActivity(i);
		}
	}
	
	@Override
	public void onBackPressed() {
		mgr.cancel(NOTIFY_ME_ID);
		SmsManager.getDefault().sendTextMessage(oponent, null, "quit:tie:", null,null);
		Intent intent = new Intent(this, Main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}