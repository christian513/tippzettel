package cs.tippzettel.model;

public class Tipp {

	private Tipper tipper;

	private Ergebnis ergebnis;

	private final Spiel spiel;

	private String tipp;

	private Integer punkte;

	public Tipp(Spiel spiel) {
		this.spiel = spiel;
	}

	public void setPunkte(Integer punkte) {
		this.punkte = punkte;
	}

	public Integer getPunkte() {
		return punkte;
	}

	public Tipper getTipper() {
		return this.tipper;
	}

	public void setTipper(Tipper tipper) {
		this.tipper = tipper;
	}

	public Spiel getSpiel() {
		return this.spiel;
	}

	public String getTipp() {
		return tipp;
	}

	public void setTipp(String tipp) {
		this.tipp = tipp;
	}

}
