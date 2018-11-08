

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Table {
	
	public enum OKTypes{
		STRING, INT, BOOL
	}
	
	public enum EnumOperations{
		MAX, MIN, SUM, COUNT, AVG
	}
	
	private List<ArrayList<Object>> rows = null;
	private String tableName;
	private String[] columnNames;
	private String[] columnTypes;
	
	public Table(){
		List<ArrayList<Object>> randuriNesync = new ArrayList<ArrayList<Object>>();
		rows =  Collections.synchronizedList(randuriNesync);
//		rows = randuriNesync;
	}
	
	private List<String> columnNamesList = new ArrayList<>();
	private List<String> columnTypesList = new ArrayList<>();
	
	
	public List<ArrayList<Object>> getRows() {
		return rows;
	}
//	public void setRows(ArrayList<ArrayList<Object>> rows) {
//		this.rows = rows;
//		
//	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String[] getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
		this.columnNamesList.addAll(Arrays.asList(columnNames));
	}
	public String[] getColumnTypes() {
		return columnTypes;
	}
	public void setColumnTypes(String[] columnTypes) {
		this.columnTypes = columnTypes;
		this.columnTypesList.addAll(Arrays.asList(columnTypes));
	}
	public List<String> getColumnNamesList() {
		return columnNamesList;
	}
	public List<String> getColumnTypesList() {
		return columnTypesList;
	}

	
	
	
	
}
