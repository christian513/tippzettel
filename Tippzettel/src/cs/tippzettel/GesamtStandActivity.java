package cs.tippzettel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import cs.tippzettel.model.GesamtStand;
import cs.tippzettel.model.Tipprunde;

public class GesamtStandActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesamt_stand);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		reload();
	}

	private void reload() {
		Tipprunde runde = Tipprunde.instance;
		TableLayout tl = (TableLayout) findViewById(R.id.tabelle);
		View header = tl.getChildAt(0);
		tl.removeAllViews();
		tl.addView(header);

		GesamtStand[] gesamtStaende = TippConnection.getGesamtStand();
		for (GesamtStand stand : gesamtStaende) {
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			TextView position = new TextView(this);
			position.setTextSize(18);
			position.setText(stand.getPosition().toString());
			LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layout.setMargins(0, 0, 0, 10);
			position.setLayoutParams(layout);
			position.setGravity(Gravity.CENTER_HORIZONTAL);
			tr.addView(position);

			TextView name = new TextView(this);
			name.setText(stand.getTipper().getName());
			name.setTextSize(18);
			if (stand.getTipper().equals(runde.getAngemeldeterTipper())) {
				name.setBackgroundColor(getResources().getColor(R.color.gold));
			} else {
				name.setBackgroundColor(getResources().getColor(R.color.white));
			}
			name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			name.setGravity(Gravity.CENTER_HORIZONTAL);
			tr.addView(name);

			TextView punkte = new TextView(this);
			punkte.setText(stand.getPunkte().toString());
			punkte.setTextSize(18);
			punkte.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			punkte.setGravity(Gravity.CENTER_HORIZONTAL);
			tr.addView(punkte);

			if (stand.getDiff() != 0) {
				ImageView tendenzView = new ImageView(this);
				LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(0, 10, 0, 0);
				tendenzView.setLayoutParams(layoutParams);
				if (stand.getDiff() < 0) {
					tendenzView.setImageResource(R.drawable.uparrow);
				} else {
					tendenzView.setImageResource(R.drawable.downarrow);
				}
				tr.addView(tendenzView);
			}

			tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_gesamt_stand, menu);
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
