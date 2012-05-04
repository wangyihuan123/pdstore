package pdstore.sparql;

import java.util.List;

import pdstore.GUID;
import pdstore.generic.PDChange;
import pdstore.generic.Pairable;
import nz.ac.auckland.se.genoupe.tools.StatefulIterator;

public class OptionalIterator<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>>
		extends
		StatefulIterator<ResultElement<TransactionType, InstanceType, RoleType>> {

	CartesianIndexIterator<GUID, Object, GUID> whereIterator;

	public OptionalIterator(
			CartesianIndexIterator<GUID, Object, GUID> whereIterator) {
		this.whereIterator = whereIterator;
	}

	public ResultElement<TransactionType, InstanceType, RoleType> computeNext() {
		if (!whereIterator.hasNext())
			return null;
		List<PDChange<GUID, Object, GUID>> whereResult = whereIterator.next();
		// TODO ...
		return null;
	}
}
