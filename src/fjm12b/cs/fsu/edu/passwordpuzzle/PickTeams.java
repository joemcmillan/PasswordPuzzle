package fjm12b.cs.fsu.edu.passwordpuzzle;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class PickTeams extends Activity {
	
	EditText redTeamName,
	blueTeamName,
	playerOneName,
	playerTwoName,
	playerThreeName,
	playerFourName;
	ImageView redTeamLocked,
	blueTeamLocked;
	String firstPlayer,
	secondPlayer,
	thirdPlayer,
	fourthPlayer,
	redTeam,
	blueTeam;
	Boolean[] names_entered = {false, false, false, false};
	Boolean[] teams_locked = {false, false};
	Button multiplayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		redTeamName = (EditText)findViewById(R.id.redTeamName);
		blueTeamName = (EditText)findViewById(R.id.blueTeamName);
		playerOneName = (EditText)findViewById(R.id.playerOneName);
		playerTwoName = (EditText)findViewById(R.id.playerTwoName);
		playerThreeName = (EditText)findViewById(R.id.playerThreeName);
		playerFourName = (EditText)findViewById(R.id.playerFourName);
		redTeamLocked = (ImageView)findViewById(R.id.redTeamLocked);
		blueTeamLocked = (ImageView)findViewById(R.id.blueTeamLocked);
		redTeamName.setText("Red Team");
		blueTeamName.setText("Blue Team");
		
		setTextListener();
		
	}
	
	
	public void redTeamLocked(View v){
		teams_locked[0] = true;
		redTeam = redTeamName.getText().toString();
		if(teams_locked[1] == true){
			Intent i = new Intent(PickTeams.this, Gameplay.class);
			i.putExtra("first", firstPlayer);
			i.putExtra("second", secondPlayer);
			i.putExtra("third", thirdPlayer);
			i.putExtra("fourth", fourthPlayer);
			i.putExtra("red", redTeam);
			i.putExtra("blue", blueTeam);
			startActivity(i);
		}
	}
	
	public void blueTeamLocked(View v){
		teams_locked[1] = true;
		blueTeam = blueTeamName.getText().toString();
		if(teams_locked[0] == true){
			Intent i = new Intent(PickTeams.this, Gameplay.class);
			i.putExtra("first", firstPlayer);
			i.putExtra("second", secondPlayer);
			i.putExtra("third", thirdPlayer);
			i.putExtra("fourth", fourthPlayer);
			i.putExtra("red", redTeam);
			i.putExtra("blue", blueTeam);
			startActivity(i);
		}
	}
	
	public void setTextListener(){
		playerOneName.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before, int count){}

			@Override
			public void afterTextChanged(Editable s) {
				firstPlayer = playerOneName.getText().toString();
				names_entered[0] = true;
				if(names_entered[1] == true){
					redTeamLocked.setVisibility(View.VISIBLE);
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}			
		});
		
		playerTwoName.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before, int count){}

			@Override
			public void afterTextChanged(Editable s) {
				secondPlayer = playerTwoName.getText().toString();
				names_entered[1] = true;
				if(names_entered[0] == true){
					redTeamLocked.setVisibility(View.VISIBLE);
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}			
		});
		
		playerThreeName.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before, int count){}

			@Override
			public void afterTextChanged(Editable s) {
				thirdPlayer = playerThreeName.getText().toString();
				names_entered[2] = true;
				if(names_entered[3] == true){
					blueTeamLocked.setVisibility(View.VISIBLE);
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}			
		});
		
		playerFourName.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before, int count){}

			@Override
			public void afterTextChanged(Editable s) {
				fourthPlayer = playerFourName.getText().toString();
				names_entered[3] = true;
				if(names_entered[2] == true){
					blueTeamLocked.setVisibility(View.VISIBLE);
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}			
		});
		
		
	}
	
	

}
