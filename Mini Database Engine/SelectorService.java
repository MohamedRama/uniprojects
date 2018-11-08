

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SelectorService {


	public static Cache cache = new Cache();
	/**
	 * Metoda care gaseste indecsii liniilor care ar trebui returnate ca urmare
	 * a unui <code>SELECT</code> sau <code>UPDATE</code>
	 * 
	 * @param table
	 *            tabelul caruia ii aplicam operatia de SELECT / UPDATE
	 * @param where
	 *            conditia
	 * @return pozitiile randurilor
	 */
	public static ArrayList<ArrayList<Object>> getRowsOkayForWhere(Table table, String where, int start, int end, List<Integer> indecsi, boolean singleResult, String[] columnsMinMaxSum) {
		// ArrayList<ArrayList<Object>> whereResults = new ArrayList<>();
		
		ArrayList<ArrayList<Object>> randurileSelectate = new ArrayList<>();
		
		// rowPositions este util pentru CACHE
		List<Integer> rowPositions = new ArrayList<>();
		
		// TODO: ineficient
		ArrayList<ArrayList<Object>> listaRanduri = new ArrayList<>();
		listaRanduri.addAll(table.getRows());
//		Iterator<ArrayList<Object>> it = listaRanduri.iterator();
		
		
//		System.out.println("---TABEL---");
//		System.out.println(listaRanduri);
		
		ArrayList<Integer> rezultate = new ArrayList<>();  
		for(int i=0; i<columnsMinMaxSum.length; i++){
			rezultate.add(null);
		}
		
		for(int i=start; i<end; i++){
//			System.out.println("ACUM PROCESAM RANDUL: " + i);

			ArrayList<Object> row = listaRanduri.get(i); //it.next();
			if (SelectorService.isOkayForWhere(table, row, where)) {
				rowPositions.add(i);
				
				
				ArrayList<Object> randCurent = procesareRandPentruSGsauML(i, indecsi, singleResult, row);
				randurileSelectate.add(randCurent);
				// if single result - procesam
				// else nu facem nimic
				if(singleResult){
					SingleResultService.calculCelula(rezultate, table, columnsMinMaxSum, row);
//					SingleResultService.mergeSingleResults(randurileSelectate,randurileSelectate, columnsMinMaxSum);
				}
			}
		}
		
		if(!singleResult){
			return randurileSelectate;
		}else{
			ArrayList<Object> convert = new ArrayList<>();
			convert.addAll(rezultate);
			ArrayList<ArrayList<Object>> singleResultFinal = new ArrayList<>();
			singleResultFinal.add(convert);
			return singleResultFinal;
		}
		
	}

	
	
	public static Table getTableByName(Database database, String tableName) {
		Table table = null;
		for (Table t : database.tables) {
			if (t.getTableName().equals(tableName)) {
				table = t;
				break;
			}
		}
		return table;
	}

	
//	public static void computeValue(Map<String, Integer>valori, List<Integer> indecsi, ArrayList<Object> rand){
//		
//	}
	
//	public ArrayList<ArrayList<Object>>
	public static ArrayList<Object> procesareRandPentruSGsauML(int pozitieRand, List<Integer> indecsi, boolean singleResult, ArrayList<Object> randIn){
		
		if(singleResult){
			ArrayList<Object> celuleSelectate = new ArrayList<Object>();

			for (Integer index : indecsi) {
				celuleSelectate.add(randIn.get(index));
			}
			return celuleSelectate;
		}else{
			return randIn;
		}
	}
	
	/**
	 * Metoda care gaseste indecsii liniilor care ar trebui returnate ca urmare
	 * a unui <code>SELECT</code> sau <code>UPDATE</code>
	 * 
	 * @param table
	 *            tabelul caruia ii aplicam operatia de SELECT / UPDATE
	 * @param where
	 *            conditia
	 * @return pozitiile randurilor
	 */
	public static List<Integer> getRowsForUpdate(Table table, String where) {
		// ArrayList<ArrayList<Object>> whereResults = new ArrayList<>();
		List<Integer> rowPositions = new ArrayList<>();
		int index = 0;
		
		// TODO: ineficient
		ArrayList<ArrayList<Object>> listaRanduri = new ArrayList<>();
		listaRanduri.addAll(table.getRows());
		Iterator<ArrayList<Object>> it = listaRanduri.iterator();
		
		
//		System.out.println("---TABEL---");
//		System.out.println(listaRanduri);
		
		while (it.hasNext()) {
//			System.out.println("BEFORE "+ index);

			ArrayList<Object> row = it.next();
			if (SelectorService.isOkayForWhere(table, row, where)) {
				rowPositions.add(index);
			}
//			System.out.println("AFTER "+ index);
			index++;
		}
		// for(ArrayList<Object> row : table.getRows()){
		// if(SelectorService.isOkayForWhere(table, row, where)){
		// rowPositions.add(index);
		// }
		// index++;
		// }
		return rowPositions;
	}
	
	
	public static boolean isOkayForWhere(Table table, ArrayList<Object> row, String where) {
		String elemente[] = where.split(" "); // ID > 3
		String numeColoana = elemente[0];
		String operator = elemente[1];
		String valoare = elemente[2];
		
//		System.out.println(table.getColumnNamesList() + " " + numeColoana);
//		System.out.println("IOFW: " + Arrays.toString(elemente));
		int indexColoana = table.getColumnNamesList().indexOf(numeColoana);
		String tipColoana = table.getColumnTypesList().get(indexColoana);
		
//		System.out.println("RAND: ");
//		List<Object> temp = Arrays.asList(row);
//		System.out.println(temp); // TODO: delete
		
		// TODO: verifica de ce null?
		if(row == null){
			return false;
		}
		Object cellValue = row.get(indexColoana);
		
		switch (tipColoana) {
		case "string":
			String valueS = (String) cellValue;
			if (valueS.equals(valoare)) {
				return true;
			} else {
				return false;
			}
		case "int":
			int valueI = (int) cellValue;
			int elementCuCareComparam = Integer.valueOf(valoare);
			if (operator.equals("=") || operator.equals("==") || operator.equals("===")) {
				return valueI == elementCuCareComparam;
			} else if (operator.equals(">")) {
				return valueI > elementCuCareComparam;
			} else if (operator.equals("<")) {
				return valueI < elementCuCareComparam;
			}
		case "bool":
			boolean valueB = (boolean) cellValue;
			if (valueB == Boolean.valueOf(valoare)) {
				return true;
			} else {
				return false;
			}
		}

		throw new RuntimeException("COLOANA TREBUIE SA AIBA UN TIP");
		// value OPERATIE valoare

	}

	public static boolean singleResult(String[] opereations) {
		for (String op : opereations) {
//			System.out.println("OPE = " + op);
			// if(!op.contains("(")){
			// continue;
			// }
			// if (op.contains("(") &&
			// operatoriUnari.contains(op.split("(")[0])) {
			// return true;
			// }
			if (op.contains("(")) {
				return true;
			}
		}
		return false;
	}

	// max(salariu)
	public static String getNumeColoanaFromOperation(String operation) {
//		System.out.println("OPERATIE: " + operation);
		String res = operation.substring(operation.indexOf('(') + 1, operation.indexOf(')'));
		return res;
	}

//	public static void main(String[] args) {
//		System.out.println(getNumeColoanaFromOperation("max(salariu)"));
//	}

}
