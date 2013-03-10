package cs.tippzettel;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import cs.tippzettel.model.NamedList;
import cs.tippzettel.model.Tipper;
import cs.tippzettel.model.Tipprunde;

public class LoginActivity extends Activity {

	private List<Tipprunde> runden = new ArrayList<Tipprunde>();
	private Editable lastPwd;
	private String lastBenutzer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	private void readTipprunden() {
		// Show the Up button in the action bar.
		// TODO enable for newer versions?
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		runden = TippConnection.getTipprunden();
		String[] rundenStr = new String[runden.size()];
		for (int i = 0; i < runden.size(); i++) {
			rundenStr[i] = runden.get(i).getBeschreibung();
		}
		Spinner rundenSpinner = (Spinner) findViewById(R.id.tipprunde);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rundenStr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rundenSpinner.setAdapter(adapter);

		rundenSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Spinner spinner = (Spinner) arg0;
				int selectedItemId = spinner.getSelectedItemPosition();
				Tipprunde tipprunde = runden.get(selectedItemId);
				NamedList<Tipper> tippers = tipprunde.getTippers();
				String[] names = new String[tippers.size()];
				for (int i = 0; i < tippers.size(); i++) {
					names[i] = tippers.get(i).getName();
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg0.getContext(),
						android.R.layout.simple_spinner_item, names);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				Spinner benutzerSpinner = (Spinner) findViewById(R.id.benutzer);
				benutzerSpinner.setAdapter(adapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_login, menu);
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

	public void anmelden(View view) {
		Spinner benutzerSpinner = (Spinner) findViewById(R.id.benutzer);
		EditText passwortText = (EditText) findViewById(R.id.passwort);
		String benutzer = benutzerSpinner.getSelectedItem().toString();
		String pwd = passwortText.getText().toString();
		String url = "anmelden&benutzer=" + benutzer + "&passwort=" + passwortText.getText();
		String ret = TippConnection.query(url);
		if ("ok".equals(ret)) {
			SharedPreferences preferences = getPreferences(MODE_PRIVATE);
			this.lastPwd = passwortText.getText();
			this.lastBenutzer = benutzer;
			Spinner rundeSpinner = (Spinner) findViewById(R.id.tipprunde);
			int selectedItemId = rundeSpinner.getSelectedItemPosition();
			Tipprunde tipprunde = runden.get(selectedItemId);
			Editor edit = preferences.edit();
			Intent intent = new Intent();
			edit.putString(getString(R.string.tipprundeid), tipprunde.getId());
			edit.commit();

			intent.putExtra(getString(R.string.tipprundeid), tipprunde.getId());
			intent.putExtra(getString(R.string.benutzer), benutzer);
			intent.putExtra(getString(R.string.Passwort), pwd);
			setResult(RESULT_OK, intent);
			finish();
		} else {
			passwortText.setText("");
			passwortText.setError(getResources().getString(R.string.falschespwd));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (TippConnection.checkConnectivity(this)) {
			readTipprunden();
		} else {
			// TODO no data and no connection
		}
	}
}
