package cms;

import cms.dal.PDCMSOperation;
import diagrameditor.dal.PDOperation;
import pdstore.GUID;
import pdstore.dal.PDInstance;
import pdstore.generic.GenericLinkedList;

/**
 * Basic extension of the GenericLinkedList to work with the CMS PDstore model.
 * 
 * @author Sina Masoud-Ansari (s.ansari@auckland.ac.nz)
 *
 */
public class CMSOperationList extends GenericLinkedList<GUID, Object, GUID, PDCMSOperation> {

	public CMSOperationList(Class<PDCMSOperation> javaType, PDInstance parentInstance, GUID collectionRole, GUID element, GUID nextRole) {
		super(javaType, parentInstance, collectionRole, element, nextRole);
		// TODO Auto-generated constructor stub
	}

}
