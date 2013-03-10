package cs.tippzettel.model;

public class IdObjekt implements IIdObjekt {

	private String id;

	public IdObjekt(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IdObjekt) {
			IdObjekt iobj = (IdObjekt) obj;
			return getId().equals(iobj.getId());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public String toString() {
		return this.id;
	}

}
