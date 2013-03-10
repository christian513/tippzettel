package cs.tippzettel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import cs.tippzettel.model.GesamtStand;
import cs.tippzettel.model.Spiel;
import cs.tippzettel.model.Spieltag;
import cs.tippzettel.model.SpieltagPosition;
import cs.tippzettel.model.Team;
import cs.tippzettel.model.Tipper;
import cs.tippzettel.model.Tipprunde;

public class TippConnection {

	private static String newCol = "##";
	private static String newLine = "#####";
	private static String baseUrl = "http://www.verschmitztes.de/tippen/android.php?cmd=";
	private static String TIPPZETTEL = "Tippzettel";

	public static boolean speichereSpiel(String spielId, String heimtore, String auswtore) {
		Tipprunde runde = Tipprunde.instance;
		String param = "speichere_ergebnis&benutzer=" + runde.getAngemeldeterTipper().getId() + "&runde="
				+ runde.getId() + "&spiel=" + spielId + "&heimtore=" + heimtore + "&auswtore=" + auswtore;
		String ret = query(param);
		return Boolean.valueOf(ret);
	}

	public static List<Spiel> getOffeneSpiele() {
		List<Spiel> spiele = new ArrayList<Spiel>();
		Tipprunde runde = Tipprunde.instance;
		String param = "offene_spiele&runde=" + runde.getId();
		String ret = query(param);
		if (ret.equals("")) {
			return spiele;
		}
		String[] spieleLines = ret.split(newLine);
		for (String spielLine : spieleLines) {
			String[] details = spielLine.split(newCol);
			Team heim = new Team(details[1], details[3], details[2]);
			Team ausw = new Team(details[4], details[6], details[5]);
			Spiel s = new Spiel(details[0]);
			s.setHeimTeam(heim);
			s.setAuswTeam(ausw);
			spiele.add(s);
		}
		return spiele;
	}

	public static Spieltag ladeSpieltagPunkte(int tag) {
		Tipprunde runde = Tipprunde.instance;
		String tagStr = tag > -1 ? "&spieltag=" + tag : "";
		String ret = query("spieltag_punkte&runde=" + runde.getId() + tagStr);
		String[] lines = ret.split(newLine);
		if (tag == -1) {
			tag = Integer.valueOf(lines[0]);
		}
		String stag = lines[0];
		Spieltag spieltag = new Spieltag(runde, stag);
		String[] tipper = lines[1].split(newCol);
		String[] punkte = lines[2].split(newCol);
		for (int i = 0; i < tipper.length; i++) {
			if (tipper[i].equals("")) {
				continue;
			}
			Tipper t = runde.getTipper(tipper[i]);
			String p = punkte[i];
			spieltag.setPunkte(t, Integer.valueOf(p));
		}
		return spieltag;
	}

	public static boolean speichereTipp(String spiel, String tipp, boolean risiko) {
		Tipprunde runde = Tipprunde.instance;
		Tipper tipper = runde.getAngemeldeterTipper();
		String id = runde.getId();
		String ret = query("speichere_tipp&runde=" + id + "&spiel_id=" + spiel + "&tipp=" + tipp + "&benutzer="
				+ tipper.getId() + "&risiko=" + risiko + "&passwort=" + tipper.getPasswort());
		return ret.equals("ok");
	}

	public static List<Spiel> getTippabgabeSpiele() {
		List<Spiel> spiele = new ArrayList<Spiel>();
		Tipprunde runde = Tipprunde.instance;
		Tipper tipper = runde.getAngemeldeterTipper();
		String id = runde.getId();
		String text = query("spiele_tippabgabe&runde=" + id + "&benutzer=" + tipper.getId());
		String[] spieleStr = text.split(newLine);
		for (String spiel : spieleStr) {
			if (spiel == null || spiel.equals("")) {
				continue;
			}
			String[] details = spiel.split(newCol);
			Team heim = new Team(details[1], details[3], details[2]);
			Team ausw = new Team(details[4], details[6], details[5]);
			Spiel s = new Spiel(details[0]);
			s.setHeimTeam(heim);
			s.setAuswTeam(ausw);
			spiele.add(s);
		}
		return spiele;
	}

	public static SpieltagPosition getSpieltagPosition() {
		Tipprunde runde = Tipprunde.instance;
		Tipper tipper = runde.getAngemeldeterTipper();
		String id = runde.getId();
		String text = query("aktueller_spieltag&runde=" + id + "&benutzer=" + tipper.getId());
		String[] lines = text.split(newLine);
		String[] line1 = lines[0].split(newCol);
		String[] line2 = lines[1].split(newCol);
		String spieltag = line1[0];
		int punkte = Integer.valueOf(line1[1]).intValue();
		int pos = Integer.valueOf(line1[2]).intValue();
		int schnitt = Integer.valueOf(line1[3]).intValue();
		String sieger = line2[1];
		for (int i = 2; i < line2.length; i++) {
			sieger += "," + line2[i];
		}
		return new SpieltagPosition(spieltag, punkte, pos, schnitt, line2[0], sieger);
	}

	public static boolean checkConnectivity(Activity activity) {
		ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			disableThreadCheck();
			return true;
		} else {
			return false;
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private static void disableThreadCheck() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			// only for android older than gingerbread
			// TODO make a better solution with threads
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	private static HttpEntity readStream(String extension) {
		try {
			HttpClient hc = new DefaultHttpClient();
			HttpPost post = new HttpPost(baseUrl + extension);
			// WifiManager manager = (WifiManager)
			// getSystemService(Context.WIFI_SERVICE);
			// manager.setWifiEnabled(true);
			HttpResponse rp = hc.execute(post);
			if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// String str = EntityUtils.toString(rp.getEntity());
				return rp.getEntity();
			} else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String query(String extension) {
		try {
			Log.d(TIPPZETTEL, extension);
			String str = EntityUtils.toString(readStream(extension));
			if (str != null && str.length() > 0) {
				Log.d(TIPPZETTEL, str);
			}
			return str;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static GesamtStand[] getGesamtStand() {
		Tipprunde runde = Tipprunde.instance;
		String id = runde.getId();
		String text = query("gesamt&runde=" + id);
		String[] lines = text.split(newLine);
		GesamtStand[] ret = new GesamtStand[lines.length];
		int pos = 0;
		int lastPoints = -99;
		for (int i = 0; i < lines.length; i++) {
			String current = lines[i];
			String[] line = current.split(newCol);
			Tipper t = runde.getTipper(line[0]);
			int punkte = Integer.valueOf(line[1]);
			int diff = Integer.valueOf(line[2]);
			if (punkte != lastPoints) {
				pos++;
				lastPoints = punkte;
			}
			ret[i] = new GesamtStand(pos, t, punkte, diff);
		}
		return ret;
	}

	public static GesamtStand[] getGesamtStandKurz() {
		Tipprunde runde = Tipprunde.instance;
		Tipper angemeldeterTipper = runde.getAngemeldeterTipper();
		String id = runde.getId();
		String text = query("gesamt&runde=" + id);
		String[] lines = text.split(newLine);
		int pos = 1;
		int anzahl = runde.getTippers().size();
		for (String line : lines) {
			String[] columns = line.split(newCol);
			if (columns.length == 3) {
				String name = columns[0];
				if (name.equals(angemeldeterTipper.getId())) {
					break;
				}
			}
			pos++;
		}
		List<Integer> positions = new ArrayList<Integer>();
		if (pos <= 3) {
			positions.add(1);
			positions.add(2);
			positions.add(3);
			positions.add(4);
		} else if (pos == anzahl) {
			positions.add(1);
			positions.add(pos - 2);
			positions.add(pos - 1);
			positions.add(pos);
		} else {
			positions.add(1);
			positions.add(pos - 1);
			positions.add(pos);
			positions.add(pos + 1);
		}

		GesamtStand[] ret = new GesamtStand[positions.size()];

		for (int i = 0; i < positions.size(); i++) {
			Integer posi = positions.get(i);
			String current = lines[posi - 1];
			String[] line = current.split(newCol);
			Tipper t = runde.getTipper(line[0]);
			int punkte = Integer.valueOf(line[1]);
			ret[i] = new GesamtStand(posi, t, punkte, 0);
		}
		return ret;
	}

	public static List<Tipprunde> getTipprunden() {
		String tipprundenStr = TippConnection.query("tipprunden");
		List<Tipprunde> runden = new ArrayList<Tipprunde>();
		String[] rundenLines = tipprundenStr.split(newLine);
		for (String rundenLine : rundenLines) {
			String[] rundeLine = rundenLine.split(newCol);
			String id = rundeLine[0];
			String text = rundeLine[1];
			Tipprunde runde = new Tipprunde(id, text);
			for (int i = 2; i < rundeLine.length; i++) {
				Tipper tipper = new Tipper(rundeLine[i], rundeLine[i]);
				runde.addTipper(tipper);
			}
			runden.add(runde);
		}
		return runden;
	}

	public static Tipprunde getTipprunde(String tipprundeId) {
		String text = query("tipprunden&runde=" + tipprundeId);
		String[] rundeLine = text.split(newCol);
		String id = rundeLine[0];
		String rtext = rundeLine[1];
		Tipprunde runde = new Tipprunde(id, rtext);
		for (int i = 2; i < rundeLine.length; i++) {
			Tipper tipper = new Tipper(rundeLine[i], rundeLine[i]);
			runde.addTipper(tipper);
		}
		return runde;
	}

}
