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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import cs.tippzettel.model.Spiel;
import cs.tippzettel.model.Team;

public class TippabgabeActivity extends Activity {

	private List<Spiel> spiele;
	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tippabgabe);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		spiele = TippConnection.getTippabgabeSpiele();
		this.index = -1;
		updateSpiel();
	}

	private void updateSpiel() {
		this.index++;
		((CheckBox) findViewById(R.id.risiko)).setChecked(false);
		if (spiele.size() == this.index) {
			// alle Spiele getippt
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return;
		}
		TextView heimText = (TextView) findViewById(R.id.heimtext);
		Team heimTeam = spiele.get(this.index).getHeimTeam();
		heimText.setText(heimTeam.getBeschreibung());
		TextView auswText = (TextView) findViewById(R.id.auswtext);
		Team auswTeam = spiele.get(this.index).getAuswTeam();
		auswText.setText(auswTeam.getBeschreibung());
		ImageButton heimWappen = (ImageButton) findViewById(R.id.heimwappen);
		ImageButton auswWappen = (ImageButton) findViewById(R.id.auswappen);
		heimWappen.setImageDrawable(getResources().getDrawable(TeamsIcons.getIcon(heimTeam.getKurzBeschreibung())));
		auswWappen.setImageDrawable(getResources().getDrawable(TeamsIcons.getIcon(auswTeam.getKurzBeschreibung())));
	}

	public void tippheim(View view) {
		if (speichereTipp("1", risiko())) {
			updateSpiel();
		}
	}

	public void tippausw(View view) {
		if (speichereTipp("2", risiko())) {
			updateSpiel();
		}
	}

	public void tippunentschieden(View view) {
		if (speichereTipp("0", risiko())) {
			updateSpiel();
		}
	}

	private boolean risiko() {
		CheckBox risikoBox = (CheckBox) findViewById(R.id.risiko);
		return risikoBox.isChecked();
	}

	private boolean speichereTipp(String tipp, boolean risiko) {
		return TippConnection.speichereTipp(this.spiele.get(this.index).getId(), tipp, risiko);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tippabgabe, menu);
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

}
