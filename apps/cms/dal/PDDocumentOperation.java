package cms.dal;

import java.util.*;
import pdstore.*;
import pdstore.dal.*;

/**
 * Data access class to represent instances of type "DocumentOperation" in memory.
 * Note that this class needs to be registered with PDCache by calling:
 *    Class.forName("cms.dal.PDDocumentOperation");
 * @author PDGen
 */
public class PDDocumentOperation implements PDInstance {

	public static final GUID typeId = new GUID("0a83d542999e11e18ad8d8a25e8c53de"); 

	public static final GUID roleOpStringId = new GUID("0a83d552999e11e18ad8d8a25e8c53de");
	public static final GUID roleOpOffsetId = new GUID("0a83d550999e11e18ad8d8a25e8c53de");
	public static final GUID roleOpUserId = new GUID("0a83d54f999e11e18ad8d8a25e8c53de");
	public static final GUID roleOpTypeId = new GUID("0a83d54e999e11e18ad8d8a25e8c53de");
	public static final GUID roleOpDocumentId = new GUID("0a83d553999e11e18ad8d8a25e8c53de");
	public static final GUID roleOpLengthId = new GUID("0a83d551999e11e18ad8d8a25e8c53de");

	static {
		register();
	}
	
	/**
	 * Registers this DAL class with the PDStore DAL layer.
	 */
	public static void register() {
		DALClassRegister.addDataClass(typeId, PDDocumentOperation.class);
	}
	
	private PDWorkingCopy pdWorkingCopy;
	private GUID id;

	public String toString() {
		String name = getName();
		if(name!=null)
			return "PDDocumentOperation:" + name;
		else
			return "PDDocumentOperation:" + id;
	}
	
	/**
	 * Creates an PDDocumentOperation object representing the given instance in the given cache.
	 * @param workingCopy the working copy the instance should be in
	 */
	public PDDocumentOperation(PDWorkingCopy workingCopy) {
		this(workingCopy, new GUID());
	}
	
	/**
	 * Creates an PDDocumentOperation object representing the given instance in the given copy.
	 * @param workingCopy the working copy the instance should be in
	 * @param id GUID of the instance
	 */
	public PDDocumentOperation(PDWorkingCopy workingCopy, GUID id) {
		this.pdWorkingCopy = workingCopy;
		this.id = id;
		
		// set the has-type link for this instance
		GUID transaction = pdWorkingCopy.getTransaction();
		pdWorkingCopy.getStore().setType(transaction, id, typeId);
	}

	/**
	 * Loads an instance object of this type into a cache.
	 * If the instance is already in the cache, the cached instance is returned.
	 * @param PDWorkingCopy pdWorkingCopy to load the instance into
	 * @param id GUID of the instance
	 * Do not directly call this method. Use the newInstance() method in PDCache which would call this method
	 */
	public static PDDocumentOperation load(PDWorkingCopy pdWorkingCopy, GUID id) {
		PDInstance instance = pdWorkingCopy.load(typeId, id);
		return (PDDocumentOperation)instance;
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
	 * Gets a textual label for this instance, for use in UIs.
	 * @return a textual label for the instance
	 */
	public String getLabel() {
		return pdWorkingCopy.getLabel(id);
	}
	
	/**
	 * Gets the name of this instance.
	 * In PDStore every instance can be given a name.
	 * @return name the instance name
	 * @throws PDStoreException
	 */
	public String getName() {
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
	public void setName(String name) {
		pdWorkingCopy.setName(id, name);
	}

	/**
	 * Removes the name of this instance.
	 * In PDStore every instance can be given a name.
	 * If the instance does not have a name, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeName() {
		pdWorkingCopy.removeName(id);
	}

	/**
	 * Gets the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * @return icon the instance icon
	 * @throws PDStoreException
	 */
	public Blob getIcon() {
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
	public void setIcon(Blob icon) {
		pdWorkingCopy.setIcon(id, icon);
	}

	/**
	 * Removes the icon of this instance.
	 * In PDStore every instance can be given an icon.
	 * If the instance does not have an icon, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeIcon() {
		pdWorkingCopy.removeIcon(id);
	}
	

	/**
	 * Returns the instance connected to this instance through the role "OpString".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public String getOpString() throws PDStoreException {
	 	return (String)pdWorkingCopy.getInstance(this, roleOpStringId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "OpString".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<String> getOpStrings() throws PDStoreException {
	 	Set<String> result = new HashSet<String>();
	 	GUID StringTypeId = new GUID("4a8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleOpStringId, String.class, StringTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "OpString".
	 * If the given instance is null, nothing happens.
	 * @param opString the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpString(String opString) throws PDStoreException {

			if (opString != null) {
				
				pdWorkingCopy.addLink(this.id, roleOpStringId, opString);
			}

	}

	/**
	 * Connects this instance to the given instances using role "OpString".
	 * If the given collection of instances is null, nothing happens.
	 * @param opString the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addOpStrings(Collection<String> opStrings) throws PDStoreException {
		if (opStrings == null)
			return;

		for (String instance : opStrings)
			addOpString(instance);
	}


	/**
	 * Removes the link from this instance through role "OpString".
	 * @throws PDStoreException
	 */
	public void removeOpString() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOpStringId, 
			pdWorkingCopy.getInstance(this, roleOpStringId));
	}

	/**
	 * Removes the link from this instance through role "OpString" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpString(Object opString) throws PDStoreException {
		if (opString == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOpStringId, opString);
	}


   /**
	 * Connects this instance to the given instance using role "OpString".
	 * If there is already an instance connected to this instance through role "OpString", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opString the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpString(String opString) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOpStringId, opString);	
	}


	/**
	 * Returns the instance connected to this instance through the role "OpOffset".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public Long getOpOffset() throws PDStoreException {
	 	return (Long)pdWorkingCopy.getInstance(this, roleOpOffsetId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "OpOffset".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<Long> getOpOffsets() throws PDStoreException {
	 	Set<Long> result = new HashSet<Long>();
	 	GUID LongTypeId = new GUID("4b8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleOpOffsetId, Long.class, LongTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "OpOffset".
	 * If the given instance is null, nothing happens.
	 * @param opOffset the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpOffset(Long opOffset) throws PDStoreException {

			if (opOffset != null) {
				
				pdWorkingCopy.addLink(this.id, roleOpOffsetId, opOffset);
			}

	}

	/**
	 * Connects this instance to the given instances using role "OpOffset".
	 * If the given collection of instances is null, nothing happens.
	 * @param opOffset the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addOpOffsets(Collection<Long> opOffsets) throws PDStoreException {
		if (opOffsets == null)
			return;

		for (Long instance : opOffsets)
			addOpOffset(instance);
	}


	/**
	 * Removes the link from this instance through role "OpOffset".
	 * @throws PDStoreException
	 */
	public void removeOpOffset() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOpOffsetId, 
			pdWorkingCopy.getInstance(this, roleOpOffsetId));
	}

	/**
	 * Removes the link from this instance through role "OpOffset" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpOffset(Object opOffset) throws PDStoreException {
		if (opOffset == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOpOffsetId, opOffset);
	}


   /**
	 * Connects this instance to the given instance using role "OpOffset".
	 * If there is already an instance connected to this instance through role "OpOffset", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opOffset the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpOffset(Long opOffset) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOpOffsetId, opOffset);	
	}


	/**
	 * Returns the instance connected to this instance through the role "OpUser".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDUser getOpUser() throws PDStoreException {
	 	return (PDUser)pdWorkingCopy.getInstance(this, roleOpUserId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "OpUser".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDUser> getOpUsers() throws PDStoreException {
	 	Set<PDUser> result = new HashSet<PDUser>();
	 	GUID PDUserTypeId = new GUID("0a83d540999e11e18ad8d8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleOpUserId, PDUser.class, PDUserTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "OpUser".
	 * If the given instance is null, nothing happens.
	 * @param opUser the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpUser(GUID opUser) throws PDStoreException {

			if (opUser != null) {
				
				pdWorkingCopy.addLink(this.id, roleOpUserId, opUser);
			}

	}


	/**
	 * Connects this instance to the given instance using role "OpUser".
	 * If the given instance is null, nothing happens.
	 * @param opUser the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpUser(PDUser opUser) throws PDStoreException {
		if (opUser != null) {
			addOpUser(opUser.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "OpUser".
	 * If the given collection of instances is null, nothing happens.
	 * @param opUser the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addOpUsers(Collection<PDUser> opUsers) throws PDStoreException {
		if (opUsers == null)
			return;
		
		for (PDUser instance : opUsers)
			addOpUser(instance);	
	}

	/**
	 * Removes the link from this instance through role "OpUser".
	 * @throws PDStoreException
	 */
	public void removeOpUser() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOpUserId, 
			pdWorkingCopy.getInstance(this, roleOpUserId));
	}

	/**
	 * Removes the link from this instance through role "OpUser" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpUser(Object opUser) throws PDStoreException {
		if (opUser == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOpUserId, opUser);
	}

	/**
	 * Removes the links from this instance through role "OpUser" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpUsers(Collection<PDUser> opUsers) throws PDStoreException {
		if (opUsers == null)
			return;
		
		for (PDUser instance : opUsers)
			pdWorkingCopy.removeLink(this.id, roleOpUserId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "OpUser".
	 * If there is already an instance connected to this instance through role "OpUser", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opUser the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpUser(GUID opUser) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOpUserId, opUser);	
	}
	/**
	 * Connects this instance to the given instance using role "OpUser".
	 * If there is already an instance connected to this instance through role "OpUser", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opUser the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpUser(PDUser opUser) throws PDStoreException {
		setOpUser(opUser.getId());
	}



	/**
	 * Returns the instance connected to this instance through the role "OpType".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public Long getOpType() throws PDStoreException {
	 	return (Long)pdWorkingCopy.getInstance(this, roleOpTypeId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "OpType".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<Long> getOpTypes() throws PDStoreException {
	 	Set<Long> result = new HashSet<Long>();
	 	GUID LongTypeId = new GUID("4b8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleOpTypeId, Long.class, LongTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "OpType".
	 * If the given instance is null, nothing happens.
	 * @param opType the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpType(Long opType) throws PDStoreException {

			if (opType != null) {
				
				pdWorkingCopy.addLink(this.id, roleOpTypeId, opType);
			}

	}

	/**
	 * Connects this instance to the given instances using role "OpType".
	 * If the given collection of instances is null, nothing happens.
	 * @param opType the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addOpTypes(Collection<Long> opTypes) throws PDStoreException {
		if (opTypes == null)
			return;

		for (Long instance : opTypes)
			addOpType(instance);
	}


	/**
	 * Removes the link from this instance through role "OpType".
	 * @throws PDStoreException
	 */
	public void removeOpType() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOpTypeId, 
			pdWorkingCopy.getInstance(this, roleOpTypeId));
	}

	/**
	 * Removes the link from this instance through role "OpType" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpType(Object opType) throws PDStoreException {
		if (opType == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOpTypeId, opType);
	}


   /**
	 * Connects this instance to the given instance using role "OpType".
	 * If there is already an instance connected to this instance through role "OpType", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opType the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpType(Long opType) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOpTypeId, opType);	
	}


	/**
	 * Returns the instance connected to this instance through the role "OpDocument".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public PDDocument getOpDocument() throws PDStoreException {
	 	return (PDDocument)pdWorkingCopy.getInstance(this, roleOpDocumentId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "OpDocument".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<PDDocument> getOpDocuments() throws PDStoreException {
	 	Set<PDDocument> result = new HashSet<PDDocument>();
	 	GUID PDDocumentTypeId = new GUID("0a83d541999e11e18ad8d8a25e8c53de");
		pdWorkingCopy.getInstances(this, roleOpDocumentId, PDDocument.class, PDDocumentTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "OpDocument".
	 * If the given instance is null, nothing happens.
	 * @param opDocument the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpDocument(GUID opDocument) throws PDStoreException {

			if (opDocument != null) {
				
				pdWorkingCopy.addLink(this.id, roleOpDocumentId, opDocument);
			}

	}


	/**
	 * Connects this instance to the given instance using role "OpDocument".
	 * If the given instance is null, nothing happens.
	 * @param opDocument the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpDocument(PDDocument opDocument) throws PDStoreException {
		if (opDocument != null) {
			addOpDocument(opDocument.getId());
		}		
	}
	
	/**
	 * Connects this instance to the given instance using role "OpDocument".
	 * If the given collection of instances is null, nothing happens.
	 * @param opDocument the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addOpDocuments(Collection<PDDocument> opDocuments) throws PDStoreException {
		if (opDocuments == null)
			return;
		
		for (PDDocument instance : opDocuments)
			addOpDocument(instance);	
	}

	/**
	 * Removes the link from this instance through role "OpDocument".
	 * @throws PDStoreException
	 */
	public void removeOpDocument() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOpDocumentId, 
			pdWorkingCopy.getInstance(this, roleOpDocumentId));
	}

	/**
	 * Removes the link from this instance through role "OpDocument" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpDocument(Object opDocument) throws PDStoreException {
		if (opDocument == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOpDocumentId, opDocument);
	}

	/**
	 * Removes the links from this instance through role "OpDocument" to the instances 
	 * in the given Collection, if the links exist.
	 * If there are no such links or the collection argument is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpDocuments(Collection<PDDocument> opDocuments) throws PDStoreException {
		if (opDocuments == null)
			return;
		
		for (PDDocument instance : opDocuments)
			pdWorkingCopy.removeLink(this.id, roleOpDocumentId, instance);
	}

   /**
	 * Connects this instance to the given instance using role "OpDocument".
	 * If there is already an instance connected to this instance through role "OpDocument", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opDocument the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpDocument(GUID opDocument) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOpDocumentId, opDocument);	
	}
	/**
	 * Connects this instance to the given instance using role "OpDocument".
	 * If there is already an instance connected to this instance through role "OpDocument", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opDocument the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpDocument(PDDocument opDocument) throws PDStoreException {
		setOpDocument(opDocument.getId());
	}



	/**
	 * Returns the instance connected to this instance through the role "OpLength".
	 * @return the connected instance
	 * @throws PDStoreException
	 */
	 public Long getOpLength() throws PDStoreException {
	 	return (Long)pdWorkingCopy.getInstance(this, roleOpLengthId);
	 }

	/**
	 * Returns the instance(s) connected to this instance through the role "OpLength".
	 * @return the connected instance(s)
	 * @throws PDStoreException
	 */
	 public Collection<Long> getOpLengths() throws PDStoreException {
	 	Set<Long> result = new HashSet<Long>();
	 	GUID LongTypeId = new GUID("4b8a986c4062db11afc0b95b08f50e2f");
		pdWorkingCopy.getInstances(this, roleOpLengthId, Long.class, LongTypeId, result);
	 	return result;
	 }
	 
   /**
	 * Connects this instance to the given instance using role "OpLength".
	 * If the given instance is null, nothing happens.
	 * @param opLength the instance to connect
	 * @throws PDStoreException
	 */
	public void addOpLength(Long opLength) throws PDStoreException {

			if (opLength != null) {
				
				pdWorkingCopy.addLink(this.id, roleOpLengthId, opLength);
			}

	}

	/**
	 * Connects this instance to the given instances using role "OpLength".
	 * If the given collection of instances is null, nothing happens.
	 * @param opLength the Collection of instances to connect
	 * @throws PDStoreException
	 */
	public void addOpLengths(Collection<Long> opLengths) throws PDStoreException {
		if (opLengths == null)
			return;

		for (Long instance : opLengths)
			addOpLength(instance);
	}


	/**
	 * Removes the link from this instance through role "OpLength".
	 * @throws PDStoreException
	 */
	public void removeOpLength() throws PDStoreException {
		pdWorkingCopy.removeLink(this.id, roleOpLengthId, 
			pdWorkingCopy.getInstance(this, roleOpLengthId));
	}

	/**
	 * Removes the link from this instance through role "OpLength" to the given instance, if the link exists.
	 * If there is no such link, nothing happens.
	 * If the given instance is null, nothing happens.
	 * @throws PDStoreException
	 */
	public void removeOpLength(Object opLength) throws PDStoreException {
		if (opLength == null)
			return;
		pdWorkingCopy.removeLink(this.id, roleOpLengthId, opLength);
	}


   /**
	 * Connects this instance to the given instance using role "OpLength".
	 * If there is already an instance connected to this instance through role "OpLength", the link will be overwritten.
	 * If the given instance is null, an existing link is removed."
	 * @param opLength the instance to connect
	 * @throws PDStoreException
	 */
	public void setOpLength(Long opLength) throws PDStoreException {
		pdWorkingCopy.setLink(this.id,  roleOpLengthId, opLength);	
	}
}
