package cs.tippzettel.model;

public class Ergebnis {

	private Integer heimTore = -1;

	private Integer auswTore = -1;

	public Ergebnis() {
	}

	public Ergebnis(Integer heimTore, Integer auswTore) {
		this.heimTore = heimTore;
		this.auswTore = auswTore;
	}

	public void setHeimTore(Integer heimTore) {
		this.heimTore = heimTore;
	}

	public void setAuswTore(Integer auswTore) {
		this.auswTore = auswTore;
	}

	public Tendenz getTendenz() {
		if (this.heimTore == -1 || this.auswTore == -1) {
			return Tendenz.UNDEFINIERT;
		}
		if (this.heimTore > this.auswTore) {
			return Tendenz.HEIMSIEG;
		} else if (this.heimTore < this.auswTore) {
			return Tendenz.AUSWAERTSSIEG;
		} else {
			return Tendenz.UNENTSCHIEDEN;
		}
	}

	@Override
	public String toString() {
		return this.heimTore + ":" + this.auswTore;
	}

}
