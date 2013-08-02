package fjm12b.cs.fsu.edu.passwordpuzzle;

import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Gameplay extends Activity {
	String[] players = new String[4];
	String redTeam,
	blueTeam;
	TextView clueWord = null,
			redTeamPoints,
			blueTeamPoints;
	ImageView loading,
			gameStart,
			redX,
			redCheck,
			blueX,
			blueCheck;
	WebView wv;
	String[] words = {"ball", "bat", "bed", "book", "boy", "bun", "can", "cake", "cap", "car", "cat", "cow", "cub", "cup", "dad", "day", "dog", "doll", "dust", "fan", "feet", "girl", "gun", "hall", "hat", "hen", "jar", "kite", "man", "map", "men", "mom", "pan", "pet", "pie", "pig", "pot", "rat", "son", "sun", "toe", "tub", "van"};
	int numWords = 43;
	int next,
	pointsRed,
	pointsBlue,
	countdown,
	points;
	TextView clueGiverRed, clueGiverBlue;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		next = 0;
		countdown = 6;
		
		clueWord = (TextView)findViewById(R.id.clueWordDisplay);
		redTeamPoints = (TextView)findViewById(R.id.redPoints);
		blueTeamPoints = (TextView)findViewById(R.id.bluePoints);
		clueGiverRed = (TextView)findViewById(R.id.clueGiverRed);
		clueGiverBlue = (TextView)findViewById(R.id.clueGiverBlue);
		pointsRed = 0;
		pointsBlue = 0;
		loading = (ImageView)findViewById(R.id.loading);
		gameStart = (ImageView)findViewById(R.id.startGame);
		redCheck = (ImageView)findViewById(R.id.redTeamCheck);
		redX = (ImageView)findViewById(R.id.redTeamX);
		blueCheck = (ImageView)findViewById(R.id.blueTeamCheck);
		blueX = (ImageView)findViewById(R.id.blueTeamX);
		Intent intent = this.getIntent();
		players[0] = intent.getStringExtra("first");
		players[1] = intent.getStringExtra("second");
		players[2] = intent.getStringExtra("third");
		players[3] = intent.getStringExtra("fourth");
		redTeam = intent.getStringExtra("red");
		blueTeam = intent.getStringExtra("blue");
		clueGiverRed.setText(players[0]);
		clueGiverBlue.setText(players[2]);
		
	}
	
	public void findDefinition(View v){
		
        loading.setVisibility(View.VISIBLE);
        wv = (WebView) findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setVisibility(View.GONE);
        wv.loadUrl ("http://www.online-utility.org/english/dictionary.jsp?word=" + words[next]);
        
        
	
	
	wv.setWebViewClient(new WebViewClient(){

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        	
            loading.setVisibility(View.GONE);
            wv.setVisibility(View.VISIBLE);
            wv.scrollTo(0, 470);
        	
            
        }

    });
	}
	
	public void beginGame(View v){
		gameStart.setVisibility(View.GONE);
		//wv.setVisibility(View.GONE);
		Random rand = new Random(); 
		next = rand.nextInt(numWords);

		clueWord.setText(words[next]);
		Typeface type = Typeface.createFromAsset(getAssets(),"JungleFeverNF.ttf"); 
		   clueWord.setTypeface(type);
		   
		   redX.setVisibility(View.VISIBLE);
			 redCheck.setVisibility(View.VISIBLE);
		
	}
	
	public void shuffleWord(View v){
		Random rand = new Random(); 
		next = rand.nextInt(numWords);

		clueWord.setText(words[next]);
		Typeface type = Typeface.createFromAsset(getAssets(),"JungleFeverNF.ttf"); 
		   clueWord.setTypeface(type);
	}
	
	public void redTeamRight(View v){
		pointsRed += 1;
		if(pointsRed == 6){
			Intent i = new Intent(this, RedWins.class);
			startActivity(i);
		}
		redTeamPoints.setText(String.valueOf(pointsRed));
		
		 redX.setVisibility(View.INVISIBLE);
		 redCheck.setVisibility(View.INVISIBLE);
		 blueX.setVisibility(View.VISIBLE);
		 blueCheck.setVisibility(View.VISIBLE);
		 shuffleWord(v);
	}
	
	public void redTeamWrong(View v){
		countdown--;
		if(countdown == 0){
			countdown=6;
			shuffleWord(v);
		}
		 blueX.setVisibility(View.VISIBLE);
		 blueCheck.setVisibility(View.VISIBLE);
		 redX.setVisibility(View.INVISIBLE);
		 redCheck.setVisibility(View.INVISIBLE);
	}
	
	public void blueTeamRight(View v){
		pointsBlue += 1;
		blueTeamPoints.setText(String.valueOf(pointsBlue));
		if(pointsBlue == 6){
			Intent i = new Intent(this, BlueWins.class);
			startActivity(i);
		}
		 blueX.setVisibility(View.INVISIBLE);
		 blueCheck.setVisibility(View.INVISIBLE);
		 shuffleWord(v);
		 redX.setVisibility(View.VISIBLE);
		 redCheck.setVisibility(View.VISIBLE);
	}
	
	public void blueTeamWrong(View v){
		countdown--;
		if(countdown == 0){
			countdown = 6; 
			shuffleWord(v);
		}
		 redX.setVisibility(View.VISIBLE);
		   redCheck.setVisibility(View.VISIBLE);
		 blueX.setVisibility(View.INVISIBLE);
		   blueCheck.setVisibility(View.INVISIBLE);
	}
	
}
