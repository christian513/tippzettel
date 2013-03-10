package cs.tippzettel.model;

public class Ergebnis {

	private int heimTore = -1;

	private int auswTore = -1;

	public Ergebnis() {
	}

	public Ergebnis(int heimTore, int auswTore) {
		this.heimTore = heimTore;
		this.auswTore = auswTore;
	}

	public void setHeimTore(int heimTore) {
		this.heimTore = heimTore;
	}

	public void setAuswTore(int auswTore) {
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

}
