package fjm12b.cs.fsu.edu.passwordpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RedWins extends Activity {
protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.redwon);
}
public void home(View v){
	Intent intent = new Intent(this, Main.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	startActivity(intent);
	
}
}