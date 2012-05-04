package pdstore.sparql;

import java.util.List;
import java.util.Map;

import pdstore.generic.PDChange;
import pdstore.generic.Pairable;

public class ResultElement<TransactionType extends Comparable<TransactionType>, InstanceType, RoleType extends Pairable<RoleType>> {
	List<PDChange<TransactionType, InstanceType, RoleType>> whereTuples;
	List<List<PDChange<TransactionType, InstanceType, RoleType>>> optionalTuples;
	Map<Variable, InstanceType> variableAssignment;

	public ResultElement(List<PDChange<TransactionType, InstanceType, RoleType>> whereTuples,
	List<List<PDChange<TransactionType, InstanceType, RoleType>>> optionalTuples,
	Map<Variable, InstanceType> variableAssignment) {
		
	}
}
