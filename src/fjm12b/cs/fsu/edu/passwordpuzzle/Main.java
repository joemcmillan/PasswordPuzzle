package fjm12b.cs.fsu.edu.passwordpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main extends Activity {
protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
}
	public void startMulti(View v){
		Intent join = new Intent(this, MultiplayerSignup.class);
		startActivity(join);
	}
	
	public void join(View v){
		Intent join = new Intent(this, Join.class);
		startActivity(join);
	}
	
	public void play(View v){
		Intent join = new Intent(this, PickTeams.class);
		startActivity(join);
	}
	
	public void instruct(View v){
		Intent join = new Intent(this, Instructions.class);
		startActivity(join);
	}
	
}