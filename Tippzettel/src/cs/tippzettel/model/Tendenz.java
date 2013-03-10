package cs.tippzettel.model;

public enum Tendenz {
	HEIMSIEG(1),
	AUSWAERTSSIEG(2),
	UNENTSCHIEDEN(0),
	UNDEFINIERT(-1);

	private int number;

	private Tendenz(int number) {
		this.number = number;
	}

	public int getNumber() {
		return this.number;
	}

}
