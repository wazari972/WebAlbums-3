package Entity;

import Entity.base.BaseAuteur;

/**
 * This is the object class that relates to the Auteur table.
 * Any customizations belong here.
 */
public class Auteur extends BaseAuteur {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public Auteur () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Auteur (java.lang.Integer _iD) {
		super(_iD);
	}
/*[CONSTRUCTOR MARKER END]*/
}