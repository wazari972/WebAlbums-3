package entity.base;

import java.io.Serializable;


/**
 * This class has been automatically generated by Hibernate Synchronizer.
 * For more information or documentation, visit The Hibernate Synchronizer page
 * at http://www.binamics.com/hibernatesync or contact Joe Hudson at joe@binamics.com.
 *
 * This is an object that contains data related to the UserAlbum table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="UserAlbum"
 */

@SuppressWarnings("serial")
public abstract class BaseUserAlbum  implements Serializable {

	public static String PROP_ID = "ID";
	public static String PROP_ALBUM = "Album";
	public static String PROP_USER = "User";


	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer _iD;

	// fields
	private java.lang.Integer _album;
	private java.lang.Integer _user;


	// constructors
	public BaseUserAlbum () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseUserAlbum (java.lang.Integer _iD) {
		this.setID(_iD);
		initialize();
	}

	protected void initialize () {}



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="ID"
     */
	public java.lang.Integer getID () {
		return _iD;
	}

	/**
	 * Set the unique identifier of this class
	 * @param _iD the new ID
	 */
	public void setID (java.lang.Integer _iD) {
		this._iD = _iD;
		this.hashCode = Integer.MIN_VALUE;
	}


	/**
	 * Return the value associated with the column: Album
	 */
	public java.lang.Integer getAlbum () {
		return _album;
	}

	/**
	 * Set the value related to the column: Album
	 * @param _album the Album value
	 */
	public void setAlbum (java.lang.Integer _album) {
		this._album = _album;
	}


	/**
	 * Return the value associated with the column: User
	 */
	public java.lang.Integer getUser () {
		return _user;
	}

	/**
	 * Set the value related to the column: User
	 * @param _user the User value
	 */
	public void setUser (java.lang.Integer _user) {
		this._user = _user;
	}


	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof entity.base.BaseUserAlbum)) return false;
		else {
			entity.base.BaseUserAlbum mObj = (entity.base.BaseUserAlbum) obj;
			if (null == this.getID() || null == mObj.getID()) return false;
			else return (this.getID().equals(mObj.getID()));
		}
	}


	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getID()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getID().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		return super.toString();
	}

}