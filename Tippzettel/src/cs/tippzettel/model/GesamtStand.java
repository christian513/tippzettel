package cs.tippzettel.model;

public class GesamtStand {
	Integer position;
	Tipper tipper;
	Integer punkte;
	Integer diff;

	public GesamtStand(Integer position, Tipper tipper, Integer punkte, Integer diff) {
		super();
		this.position = position;
		this.tipper = tipper;
		this.punkte = punkte;
		this.diff = diff;
	}

	public Integer getPosition() {
		return position;
	}

	public Integer getPunkte() {
		return punkte;
	}

	public Tipper getTipper() {
		return tipper;
	}

	public Integer getDiff() {
		return diff;
	}
}
