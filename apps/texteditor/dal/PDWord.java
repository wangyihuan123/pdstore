package texteditor.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "Word" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("texteditor.dal.PDWord");
 * @author PDGen
 */
public class PDWord implements PDInstance {

	public static final GUID typeId = new GUID("3c08266254d111e09c2a001e6805726d"); 

	public static final GUID roleTextId = new GUID("3c08266b54d111e09c2a001e6805726d");

	static {
		DALClassRegister.addDataClass(typeId, PDWord.class);
	}
	private PDWorkingCopy pdWorkingCopy;
	private GUID id;
	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDWord:" + name;
		else
			return "PDWord:" + id;
	}
	/**
	 * Creates an PDWord object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 */
	public PDWord(PDWorkingCopy workingCopy) {
		this(workingCopy, new GUID());
	}
	
	/**
	 * Creates an PDWord object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 * @param id GUID of the instance
	 */
	public PDWord(PDWorkingCopy workingCopy, GUID id) {
		this.pdWorkingCopy = workingCopy;
		this.id = id;
		
		
	}
	/**
	 * Loads an instance object of this type into a cache.
	 * If the instance is already in the cache, the cached instance is returned.
	 * @param PDWorkingCopy pdWorkingCopy to load the instance into
	 * @param id GUID of the instance
	 * Do not directly call this method. Use the newInstance() method in PDCache which would call this method
	 */
	public static PDWord load(PDWorkingCopy pdWorkingCopy, GUID id) {
		PDInstance instance = pdWorkingCopy.load(typeId, id);
		return (PDWord)instance;
	}

	/**
	 * Gets the pdWorkingCopy this object is stored in.
	 */
	public PDWorkingCopy getPDWorkingCopy() {
		return pdWorkingCopy;
	}
	/**
	 * Gets the GUID of the instance represented by this object.
	 */
	public GUID getId() {
		return id;
	}
	/**
	 * Gets the GUID of the type of the instance represented by this object.
	 */
	public GUID getTypeId() {
		return typeId;
	}
	/**
	 * Gets the name of this instance.
	 * In PDStore every instance can be given a name.
	 * @return name the instance name
	 * @throws PDStoreException
	 */
	public String getName() throws PDStoreException {
		return pdWorkingCopy.getName(id);
	}
	/**
	 * Sets the name of this instance.
	 * In PDStore every instance can be given a name.
	 * If the instance already has a name, the name will be overwritten.
	 * If the given name is null, an existing name will be removed.
	 * @return name the new instance name
	 * @throws PDStoreException
	 */
	public void setName(String name) throws PDStoreException {
		pdWorkingCopy.setName(id, name);
	}
	/**
	 * Removes the name of this instance.
	 * In PDStore every instance can be given a name.
	 * If the instance does not have a name, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeName() throws PDStoreException {
		pdWorkingCopy.removeName(id);
	}
	/**
	 * Gets the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * @return icon the instance icon
	 * @throws PDStoreException
	 */
	public Blob getIcon() throws PDStoreException {
		return pdWorkingCopy.getIcon(id);
	}
	/**
	 * Sets the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * If the instance already has an icon, the icon will be overwritten.
	 * If the given icon is null, an existing icon will be removed.
	 * @return icon the new instance icon
	 * @throws PDStoreException
	 */
	public void setIcon(Blob icon) throws PDStoreException {
		pdWorkingCopy.setIcon(id, icon);
	}
	/**
	 * Removes the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * If the instance does not have an icon, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeIcon() throws PDStoreException {
		pdWorkingCopy.removeIcon(id);
	}

	
	  // Code generated by PDGetSetRemoveGen:


	/**
	 * Returns the instance connected to this instance through the role "text".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public String getText() throws PDStoreException {
	 	return (String)pdWorkingCopy.getInstance(this, roleTextId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "text".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<String> getTexts() throws PDStoreException {
	 	Set<String> result = new HashSet<String>();
	 	GUID StringTypeId = new GUID("4a8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleTextId, String.class, StringTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "text".
	 * If the given instance is null, nothing happens.
	 * @param text the instance to connect
	 * @throws PDStoreException
	 */
	public void addText(String text) throws PDStoreException {

			if (text != null) {
				
				pdWorkingCopy.addLink(this.id, roleTextId, text);
			}

	}


	/**
	 * Removes the link from this instance through role "text".
	 * @throws PDStoreException
	 */
	public void removeText() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleTextId, 
			pdWorkingCopy.getInstance(this, roleTextId));
	}

	/**
	 * Removes the link from this instance through role "text" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.text.getId()
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeText(Object text) throws PDStoreException {
		if (text==null)
			return;
		pdWorkingCopy.removeLink(this.id, roleTextId, text);
	}

   /**
	 * Connects this instance to the given instance using role "text".
	 * If there is already an instance connected to this instance through role "text", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param text the instance to connect
	 * @throws PDStoreException
	 */
	public void setText(String text) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleTextId, text);	
	}
}
