package cs.tippzettel.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class Spieltag extends IdObjekt {

	private final NamedList<Spiel> spiele = new NamedList<Spiel>();

	private Date startDatum;

	private final Tipprunde runde;

	private Map<Tipper, Integer> punkte;

	public Spieltag(Tipprunde runde, String id) {
		super(id);
		this.runde = runde;
		this.punkte = new HashMap<Tipper, Integer>();
	}

	public void addSpiel(Spiel tag) {
		this.spiele.add(tag);
	}

	public NamedList<Spiel> getSpiele() {
		return this.spiele;
	}

	public Spiel getSpiel(String id) {
		return this.spiele.get(id);
	}

	public boolean isKomplett() {
		for (Spiel spiel : this.spiele) {
			if (!spiel.hatErgebnis()) {
				return false;
			}
		}
		return true;
	}

	public Date getStartDatum() {
		return this.startDatum;
	}

	public void setStartDatum(Date startDatum) {
		this.startDatum = startDatum;
	}

	public Spieltag getNächsterSpieltag() {
		try {
			int nummer = Integer.parseInt(getId());
			return this.runde.getSpieltag(new Integer(nummer + 1).toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public Spieltag getVorigerSpieltag() {
		try {
			int nummer = Integer.parseInt(getId());
			return this.runde.getSpieltag(new Integer(nummer - 1).toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public void setPunkte(Tipper t, Integer p) {
		this.punkte.put(t, p);
	}

	public List<Tipper> getTipperAbsteigendNachPunkten() {
		List<Tipper> tippers = new ArrayList<Tipper>();
		ValueComparator comp = new ValueComparator(this.punkte);
		Map<Tipper, Integer> sorted = new TreeMap<Tipper, Integer>(comp);
		sorted.putAll(this.punkte);
		Set<Entry<Tipper, Integer>> entries = sorted.entrySet();
		for (Entry<Tipper, Integer> entry : entries) {
			tippers.add(entry.getKey());
		}
		return tippers;
	}

	public Integer getPunkte(Tipper t) {
		return this.punkte.get(t);
	}

	class ValueComparator implements Comparator<Tipper> {

		Map<Tipper, Integer> base;

		public ValueComparator(Map<Tipper, Integer> base) {
			this.base = base;
		}

		// Note: this comparator imposes orderings that are inconsistent with
		// equals.
		public int compare(Tipper a, Tipper b) {
			if (base.get(a) < base.get(b)) {
				return 1;
			} else {
				return -1;
			}
		}

	}

}