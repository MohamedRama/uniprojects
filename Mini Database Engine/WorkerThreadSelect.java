

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class WorkerThreadSelect implements Callable<ArrayList<ArrayList<Object>>>{

	
	
	private int start;
	private int end;
	private Table table;
	private String where;
	private List<Integer> indecsi;
	private boolean singleResult;
	private String[] columnsMinMaxSum;

	public WorkerThreadSelect(Table table, int start, int end, String where,
			boolean singleResult, List<Integer> indecsi, String[] columnsMinMaxSum){
		this.table = table;
		this.end = end;
		this.start = start;
		this.where = where;
		this.indecsi = indecsi;
		this.singleResult = singleResult;
		this.columnsMinMaxSum = columnsMinMaxSum;
		
	}

	@Override
	public ArrayList<ArrayList<Object>> call() throws Exception {
		
		ArrayList<ArrayList<Object>> rezultate = 
				SelectorService.getRowsOkayForWhere(table, where, 0, table.getRows().size(), indecsi, singleResult, columnsMinMaxSum);

		
		return rezultate;
	}
	
	
	
}
