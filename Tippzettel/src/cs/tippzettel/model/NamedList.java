package cs.tippzettel.model;

import java.util.ArrayList;

public class NamedList<E extends IIdObjekt> extends ArrayList<E> {

	public E get(String id) {
		int size = size();
		for (int i = 0; i < size; i++) {
			E object = get(i);
			if (id.equals(object.getId())) {
				return object;
			}
		}
		return null;
	}

}
