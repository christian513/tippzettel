package cs.tippzettel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cs.tippzettel.model.GesamtStand;
import cs.tippzettel.model.SpieltagPosition;
import cs.tippzettel.model.TippabgabeStatus;
import cs.tippzettel.model.Tipprunde;

public class MainActivity extends Activity {

	private static final int LOGIN = 1;
	private static final String SCHEDULED_TIPPABGABE_SPIELTAG = "SCHEDULED_TIPPABGABE_SPIELTAG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if (!isInitialized()) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, LOGIN);
		} else if (TippConnection.checkConnectivity(this)) {
			setupTipprunde(null);
		}
	}

	private void scheduleTippabgabeNotification(String spieltag, String tage, String stunden) {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		if (!alreadyScheduledFor(Tipprunde.instance, spieltag)) {
			Editor edit = preferences.edit();
			edit.putString(createTippabgabeScheduleKey(Tipprunde.instance), spieltag);
			edit.commit();
			Calendar calendar = Calendar.getInstance();

			long offset = 0;
			if (!tage.equals("0")) {
				Long MILLIS_HOURS = 1000L * 60L * 60L;
				Long MILLIS_DAYS = 24L * MILLIS_HOURS;
				Long day = Long.valueOf(tage);
				day -= 1L;
				// erinnerung 1 Tag vorher
				offset = day * MILLIS_DAYS + Long.valueOf(stunden) * MILLIS_HOURS;
			}
			long reminderTime = System.currentTimeMillis() + offset;
			calendar.setTimeInMillis(reminderTime);
			SimpleDateFormat format = new SimpleDateFormat();
			Log.d("Tippzettel", "Tippabgabe reminder at " + format.format(calendar.getTime()));
			AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
			int id = (int) System.currentTimeMillis();
			Intent intent = new Intent(this, TippNotificationReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == LOGIN) {
			setupTipprunde(data);
			// reload();
		}
	}

	private boolean isInitialized() {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		return !preferences.getString(getString(R.string.tipprundeid), "").equals("");
	}

	private void reload() {
		reloadNaechsterSpieltag();
		reloadLetzterSpieltag();
		reloadGesamtStand();
	}

	@SuppressLint("NewApi")
	private void reloadNaechsterSpieltag() {
		TippabgabeStatus status = TippConnection.getTippabgabeStatus();
		Button abgabeButton = (Button) findViewById(R.id.tippabgabe);
		if (status.isGetippt()) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				abgabeButton.setBackground(getResources().getDrawable(R.drawable.tippabgabe_selector_button));
			} else {
				abgabeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.tippabgabe_selector_button));
			}
			abgabeButton.setText("Tipps abgegeben");
			abgabeButton.setEnabled(false);
		} else {
			String dauer = "";
			String tage = status.getTage();
			String stunden = status.getStunden();
			String spieltag = status.getSpieltag();
			scheduleTippabgabeNotification(spieltag, tage, stunden);
			if (tage.equals("0")) {
				dauer = stunden + " Stunden";
			} else {
				if (!stunden.equals("0")) {
					Double std = Double.valueOf(stunden);
					double rat = std / 24;
					rat += Double.valueOf(tage);
					rat = Math.round(rat * 10.0) / 10.0;
					dauer = rat + " Tage";
				} else {
					dauer = tage + " Tage";
				}
			}
			abgabeButton.setText(Html.fromHtml("Tippen <small>(" + dauer + ")</small>"));
			abgabeButton.setEnabled(true);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				abgabeButton.setBackground(getResources().getDrawable(R.drawable.tippabgabe_selector_red));
			} else {
				abgabeButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.tippabgabe_selector_red));
			}

		}

		// ((Button) findViewById(R.id.tippabgabe)).setText(Html
		// .fromHtml("Tippabgabe<br/><font size='-3'>(11 Tage und 4 Stunden)</font>"));
	}

	private void reloadGesamtStand() {
		GesamtStand[] staende = TippConnection.getGesamtStandKurz();

		zeigePosition(staende[0], R.id.gesposition, R.id.gesname, R.id.gespunkte);
		zeigePosition(staende[1], R.id.ges2position, R.id.ges2name, R.id.ges2punkte);
		zeigePosition(staende[2], R.id.ges3position, R.id.ges3name, R.id.ges3punkte);
		zeigePosition(staende[3], R.id.ges4position, R.id.ges4name, R.id.ges4punkte);
	}

	private void zeigePosition(GesamtStand gesamtStand, int gesposition, int gesname, int gespunkte) {
		TextView position = (TextView) findViewById(gesposition);
		TextView name = (TextView) findViewById(gesname);
		TextView punkte = (TextView) findViewById(gespunkte);

		position.setText(gesamtStand.getPosition().toString());
		name.setText(gesamtStand.getTipper().getId());
		if (gesamtStand.getTipper().equals(Tipprunde.instance.getAngemeldeterTipper())) {
			name.setTypeface(null, Typeface.BOLD);
			punkte.setTypeface(null, Typeface.BOLD);
			position.setTypeface(null, Typeface.BOLD);
		} else {
			name.setTypeface(null, Typeface.NORMAL);
			punkte.setTypeface(null, Typeface.NORMAL);
			position.setTypeface(null, Typeface.NORMAL);
		}
		punkte.setText(gesamtStand.getPunkte().toString());
	}

	@SuppressLint("NewApi")
	private void reloadLetzterSpieltag() {
		SpieltagPosition sp = TippConnection.getSpieltagPosition();
		// ((TextView) findViewById(R.id.tag)).setText(sp.getNummer() + ".");
		((TextView) findViewById(R.id.punkte)).setText(sp.getPunkte().toString());
		((TextView) findViewById(R.id.position)).setText(sp.getPosition().toString());
		String siegerText = sp.getSieger() + " (" + sp.getMax() + ")";
		((TextView) findViewById(R.id.sieger)).setText(siegerText);
		int schnitt = sp.getSchnitt();
		int punkte = sp.getPunkte().intValue();
		double performance = schnitt > 0 ? (double) punkte / schnitt : 0;
		ImageView pview = (ImageView) findViewById(R.id.performance);
		if (performance >= 1.5) {
			pview.setImageResource(R.drawable.daumen_hoch);
		} else if (performance >= 1.25) {
			pview.setImageResource(R.drawable.daumen_neutral_hoch);
		} else if (performance < 0.5) {
			pview.setImageResource(R.drawable.daumen_runter);
		} else if (performance < 0.75) {
			pview.setImageResource(R.drawable.daumen_neutral_runter);
		} else {
			pview.setImageResource(R.drawable.daumen_neutral);
		}

		boolean offeneSpiele = TippConnection.hatOffeneSpiele();
		Button ergebnisse = (Button) findViewById(R.id.ergebniseingabe);
		ergebnisse.setEnabled(offeneSpiele);
		if (offeneSpiele) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				ergebnisse.setBackground(getResources().getDrawable(R.drawable.tippabgabe_selector_red));
			} else {
				ergebnisse.setBackgroundDrawable(getResources().getDrawable(R.drawable.tippabgabe_selector_red));
			}
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				ergebnisse.setBackground(getResources().getDrawable(R.drawable.tippabgabe_selector_button));
			} else {
				ergebnisse.setBackgroundDrawable(getResources().getDrawable(R.drawable.tippabgabe_selector_button));
			}
		}

	}

	private void setupTipprunde(Intent intent) {
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		String tipprundeId = null;
		String benutzer = null;
		String password = null;
		if (isInitialized()) {
			// read from preferences
			tipprundeId = preferences.getString(getString(R.string.tipprundeid), "");
			benutzer = preferences.getString(getString(R.string.benutzer), "");
			password = preferences.getString(getString(R.string.Passwort), "");
		} else {
			// first usage, read from intent and save to preferences
			Editor edit = preferences.edit();
			tipprundeId = intent.getStringExtra(getString(R.string.tipprundeid));
			edit.putString(getString(R.string.tipprundeid), tipprundeId);
			benutzer = intent.getStringExtra(getString(R.string.benutzer));
			edit.putString(getString(R.string.benutzer), benutzer);
			password = intent.getStringExtra(getString(R.string.Passwort));
			edit.putString(getString(R.string.Passwort), password);
			edit.commit();
		}
		Tipprunde.instance = TippConnection.getTipprunde(tipprundeId);
		Tipprunde.instance.setAngemeldeterTipper(benutzer, password);
		setTitle(Tipprunde.instance.getBeschreibung());
	}

	public void tippabgabe(View view) {
		Intent intent = new Intent(this, TippabgabeActivity.class);
		startActivity(intent);
	}

	public void spieltagdetails(View view) {
		Intent intent = new Intent(this, SpieltagActivity.class);
		startActivity(intent);
	}

	public void gesamtdetails(View view) {
		Intent intent = new Intent(this, GesamtStandActivity.class);
		startActivity(intent);
	}

	public void ergebnisse(View view) {
		Intent intent = new Intent(this, ErgebnisActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.rundelogin) {
			SharedPreferences preferences = getPreferences(MODE_PRIVATE);
			Editor edit = preferences.edit();
			edit.remove(getString(R.string.tipprundeid));
			edit.remove(getString(R.string.benutzer));
			edit.remove(getString(R.string.Passwort));
			edit.commit();
			startActivityForResult(new Intent(this, LoginActivity.class), LOGIN);
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isInitialized()) {
			reload();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private boolean alreadyScheduledFor(Tipprunde runde, String spieltag) {
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		String key = createTippabgabeScheduleKey(runde);
		return prefs.getString(key, "0").equals(spieltag);
	}

	private String createTippabgabeScheduleKey(Tipprunde runde) {
		String key = SCHEDULED_TIPPABGABE_SPIELTAG + "_" + runde.getId() + "_" + runde.getAngemeldeterTipper().getId();
		return key;
	}
}
