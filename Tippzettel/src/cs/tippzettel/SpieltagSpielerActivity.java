package cs.tippzettel;

import java.util.List;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import cs.tippzettel.model.Spiel;
import cs.tippzettel.model.Tipp;

public class SpieltagSpielerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spieltag_spieler);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// Show the Up button in the action bar.
		// getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_spieltag_spieler, menu);
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

	@Override
	protected void onResume() {
		super.onResume();
		TableLayout tl = (TableLayout) findViewById(R.id.tipptabelle);
		View header = tl.getChildAt(0);
		tl.removeAllViews();
		tl.addView(header);
		String spieltag = getIntent().getStringExtra("spieltag");
		((TextView) findViewById(R.id.spieltag)).setText(spieltag);
		String tipper = getIntent().getStringExtra("tipper");
		((TextView) findViewById(R.id.spieler)).setText(tipper);
		List<Tipp> tipps = TippConnection.getTipps(spieltag, tipper);
		for (Tipp tipp : tipps) {
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			Spiel spiel = tipp.getSpiel();
			Integer punkte = tipp.getPunkte();

			newTextView(tr, spiel.getHeimTeam().getBeschreibung() + " - " + spiel.getAuswTeam().getBeschreibung(),
					punkte);
			newTextView(tr, spiel.getErgebnis().toString(), punkte);
			newTextView(tr, tipp.getTipp(), punkte);
			if (punkte == 0) {
				newTextView(tr, "", punkte);
			} else {
				newTextView(tr, punkte.toString(), punkte);
			}
			tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}

	}

	private TextView newTextView(TableRow tr, String text, Integer punkte) {
		TextView textView = new TextView(this);
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setText(text);
		if (punkte <= 0) {
			textView.setTextColor(getResources().getColor(R.color.red));
		} else {
			textView.setTextColor(getResources().getColor(R.color.green));
		}
		textView.setTextSize(8 + punkte);
		tr.addView(textView);
		return textView;
	}
}
