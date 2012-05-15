package cms;

import cms.dal.PDCMSOperation;
import diagrameditor.dal.PDOperation;
import pdstore.GUID;
import pdstore.dal.PDInstance;
import pdstore.generic.GenericLinkedList;

public class CMSOperationList extends GenericLinkedList<GUID, Object, GUID, PDCMSOperation> {

	public CMSOperationList(Class<PDCMSOperation> javaType, PDInstance parentInstance, GUID collectionRole, GUID element, GUID nextRole) {
		super(javaType, parentInstance, collectionRole, element, nextRole);
		// TODO Auto-generated constructor stub
	}

}
