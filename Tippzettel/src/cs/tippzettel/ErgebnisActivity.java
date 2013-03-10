package cs.tippzettel;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import cs.tippzettel.model.Spiel;
import cs.tippzettel.model.Team;

public class ErgebnisActivity extends Activity {

	private int index;
	private List<Spiel> spiele;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ergebnis);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// Show the Up button in the action bar.
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		this.index = -1;
		spiele = TippConnection.getOffeneSpiele();
		updateSpiel();
	}

	private void updateSpiel() {
		this.index++;
		if (spiele.size() == this.index) {
			// alle Ergebnisse eingegeben
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return;
		}
		TextView heimText = (TextView) findViewById(R.id.team1);
		Team heimTeam = spiele.get(this.index).getHeimTeam();
		heimText.setText(heimTeam.getBeschreibung());
		TextView auswText = (TextView) findViewById(R.id.team2);
		Team auswTeam = spiele.get(this.index).getAuswTeam();
		auswText.setText(auswTeam.getBeschreibung());
		ImageButton heimWappen = (ImageButton) findViewById(R.id.wappen1);
		ImageButton auswWappen = (ImageButton) findViewById(R.id.wappen2);
		heimWappen.setImageDrawable(getResources().getDrawable(TeamsIcons.getIcon(heimTeam.getKurzBeschreibung())));
		auswWappen.setImageDrawable(getResources().getDrawable(TeamsIcons.getIcon(auswTeam.getKurzBeschreibung())));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_ergebnis, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void reset(View v) {
		TextView text = (TextView) findViewById(R.id.tore1);
		text.setText("0");
		TextView text2 = (TextView) findViewById(R.id.tore2);
		text2.setText("0");
		enableReset(false);
		enableSkip(true);
	}

	public void skip(View v) {
		reset(v);
		updateSpiel();
	}

	public void save(View v) {
		Spiel spiel = this.spiele.get(this.index);
		TextView h = (TextView) findViewById(R.id.tore1);
		TextView a = (TextView) findViewById(R.id.tore2);
		boolean erfolg = TippConnection.speichereSpiel(spiel.getId(), h.getText().toString(), a.getText().toString());
		if (erfolg) {
			updateSpiel();
			reset(v);
		}
	}

	public void heimtor(View v) {
		TextView text = (TextView) findViewById(R.id.tore1);
		String old = text.getText().toString();
		Integer newTore = Integer.valueOf(old) + 1;
		text.setText(newTore.toString());
		enableSkip(false);
		enableReset(true);
	}

	public void auswtor(View v) {
		TextView text = (TextView) findViewById(R.id.tore2);
		String old = text.getText().toString();
		Integer newTore = Integer.valueOf(old) + 1;
		text.setText(newTore.toString());
		enableSkip(false);
		enableReset(true);
	}

	private void enableReset(boolean enable) {
		((Button) findViewById(R.id.reset)).setEnabled(enable);
	}

	private void enableSkip(boolean enable) {
		((Button) findViewById(R.id.skip)).setEnabled(enable);
	}

}
