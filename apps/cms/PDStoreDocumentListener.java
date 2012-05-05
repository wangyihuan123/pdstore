package cms;

import java.util.List;

import pdstore.GUID;
import pdstore.generic.PDChange;
import pdstore.generic.PDCoreI;
import pdstore.notify.PDListenerAdapter;

public class PDStoreDocumentListener extends PDListenerAdapter<GUID, Object, GUID> {

	public PDStoreDocumentListener() {
		super();
	}

	@Override
	public void transactionCommitted(
			List<PDChange<GUID, Object, GUID>> transaction,
			List<PDChange<GUID, Object, GUID>> matchedChanges, PDCoreI<GUID, Object, GUID> core) {
		for (PDChange<GUID, Object, GUID> change : transaction) {
			System.out.println("Change: " + change);
		}
	}


}