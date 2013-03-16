package cs.tippzettel;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import cs.tippzettel.model.Spieltag;
import cs.tippzettel.model.Tipper;
import cs.tippzettel.model.Tipprunde;

public class SpieltagActivity extends Activity implements OnTouchListener {

	private int spieltag = -1;
	private int aktSpieltag = -1;
	private Intent spieltagSpielerIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spieltag);
		// Show the Up button in the action bar.
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		findViewById(R.id.rootlayout).setOnTouchListener(this);
		reload();
		aktSpieltag = spieltag;
		this.spieltagSpielerIntent = new Intent(this, SpieltagSpielerActivity.class);
		spieltagSpielerIntent.putExtra("spieltag", Integer.valueOf(this.aktSpieltag).toString());
	}

	private void reload() {
		Tipprunde runde = Tipprunde.instance;
		TableLayout tl = (TableLayout) findViewById(R.id.tabelle);
		View header = tl.getChildAt(0);
		tl.removeAllViews();
		tl.addView(header);
		Spieltag spieltag = TippConnection.ladeSpieltagPunkte(this.spieltag);
		this.spieltag = Integer.valueOf(spieltag.getId());
		TextView v = (TextView) findViewById(R.id.spieltag);
		v.setText(spieltag.getId());

		List<Tipper> tippers = spieltag.getTipperAbsteigendNachPunkten();
		Integer pos = 0;
		Integer lastPoints = -99;
		for (Tipper tipper : tippers) {
			Integer points = spieltag.getPunkte(tipper);
			if (points != lastPoints) {
				pos++;
				lastPoints = points;
			}
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			TextView position = new TextView(this);
			position.setTextSize(18);
			position.setText(pos.toString());
			LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layout.setMargins(0, 0, 0, 10);
			position.setLayoutParams(layout);
			position.setGravity(Gravity.CENTER_HORIZONTAL);
			tr.addView(position);

			TextView name = new TextView(this);
			name.setText(tipper.getName());
			name.setTextSize(18);
			name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String tipperName = (String) ((TextView) v).getText();
					spieltagSpielerIntent.putExtra("tipper", tipperName);
					startActivity(spieltagSpielerIntent);
				}
			});
			if (tipper.equals(runde.getAngemeldeterTipper())) {
				name.setBackgroundColor(getResources().getColor(R.color.gold));
			} else {
				name.setBackgroundColor(getResources().getColor(R.color.white));
			}
			name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			name.setGravity(Gravity.CENTER_HORIZONTAL);
			tr.addView(name);

			TextView punkte = new TextView(this);
			punkte.setText(points.toString());
			punkte.setTextSize(18);
			punkte.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			punkte.setGravity(Gravity.CENTER_HORIZONTAL);
			tr.addView(punkte);

			tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_spieltag, menu);
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
	public boolean onTouch(View view, MotionEvent event) {
		int MIN_DISTANCE = 100;
		float downX = 0, downY = 0, upX, upY;
		System.out.println(event.getAction());
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			downX = event.getX();
			downY = event.getY();
			return true;
		}
		case MotionEvent.ACTION_UP: {
			upX = event.getX();
			upY = event.getY();

			float deltaX = downX - upX;
			float deltaY = downY - upY;

			// swipe horizontal?
			if (Math.abs(deltaX) > MIN_DISTANCE) {
				// left or right
				if (deltaX < 0) {
					this.onLeftToRightSwipe();
					return true;
				}
				if (deltaX > 0) {
					this.onRightToLeftSwipe();
					return true;
				}
			} else {
				return false; // We don't consume the event
			}

			// swipe vertical?
			if (Math.abs(deltaY) > MIN_DISTANCE) {
				// top or down
				if (deltaY < 0) {
					// this.onTopToBottomSwipe();
					return true;
				}
				if (deltaY > 0) {
					// this.onBottomToTopSwipe();
					return true;
				}
			} else {
				// Log.i(logTag, "Swipe was only " + Math.abs(deltaX) +
				// " long, need at least " + MIN_DISTANCE);
				return false; // We don't consume the event
			}

			return true;
		}
		}
		return false;
	}

	private void onRightToLeftSwipe() {
		nextSpieltag(null);
	}

	public void nextSpieltag(View v) {
		this.spieltag++;
		if (spieltag > aktSpieltag) {
			spieltag = 1;
		}
		spieltagSpielerIntent.putExtra("spieltag", Integer.valueOf(this.spieltag).toString());
		reload();
	}

	private void onLeftToRightSwipe() {
		previousSpieltag(null);
	}

	public void previousSpieltag(View v) {
		this.spieltag--;
		if (this.spieltag <= 0) {
			this.spieltag = this.aktSpieltag;
		}
		spieltagSpielerIntent.putExtra("spieltag", Integer.valueOf(this.spieltag).toString());
		reload();
	}

}
