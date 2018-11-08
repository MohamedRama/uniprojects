

import java.util.ArrayList;
import java.util.Arrays;


public class SingleResultService {

	public static void calculCelula(ArrayList<Integer> rezultate, Table table, String[] columnsMinMaxSum,
			ArrayList<Object> rand) {

		for (String cmmsop : columnsMinMaxSum) {

			String operatie = cmmsop.split("\\(")[0];
			// System.out.println("OPERATIE: " + op);
			int idx = Arrays.asList(columnsMinMaxSum).indexOf(cmmsop);
			Object celula = rand
					.get(table.getColumnNamesList().indexOf(SelectorService.getNumeColoanaFromOperation(cmmsop)));
			switch (operatie) {
			case "sum":
				if (rezultate.get(idx) == null) {
					rezultate.set(idx, (Integer) celula);
				} else {
					rezultate.set(idx, (rezultate.get(idx) + (Integer) celula));
				}
				break;
			case "avg":
				if (rezultate.get(idx) == null) {
					rezultate.set(idx, (Integer) celula);
					// rezultate.add((Integer) celula);
				} else {
					rezultate.set(idx, (rezultate.get(idx) + (Integer) celula));
				}

				// TODO: impartirea se face la final
				break;
			case "count":
				if (rezultate.get(idx) == null) {
					rezultate.set(idx, 1);
				} else {
					rezultate.set(idx, (rezultate.get(idx) + 1));
				}
				break;
			case "min":
				if (rezultate.get(idx) == null) {
					rezultate.set(idx, (Integer) celula);
				} else {
					Integer valoareVeche = rezultate.get(idx);
					if (valoareVeche > (Integer) celula) {
						rezultate.set(idx, (Integer) celula);
					}
				}
				break;
			case "max":
				if (rezultate.get(idx) == null) {
					rezultate.set(idx, (Integer) celula);
				} else {
					Integer valoareVeche = rezultate.get(idx);
					if (valoareVeche < (Integer) celula) {
						rezultate.set(idx, (Integer) celula);
					}
				}
				break;
			}
		}
	}

	public static ArrayList<ArrayList<Object>> mergeSingleResults(ArrayList<ArrayList<Object>> r1,
			ArrayList<ArrayList<Object>> r2, String[] columnsMinMaxSum) {
		
		
		ArrayList<Object> randR1 = r1.get(0);
		ArrayList<Object> randR2 = r2.get(0);
		ArrayList<ArrayList<Object>> result = new ArrayList<>();
		ArrayList<Object> randResult = new ArrayList<>();
		
		for (String cmmsop : columnsMinMaxSum) {
			randResult.add(null);
		}
		
		for (String cmmsop : columnsMinMaxSum) {
			String operatie = cmmsop.split("\\(")[0];
			int idx = Arrays.asList(columnsMinMaxSum).indexOf(cmmsop);
			
			
			Integer val1 = (Integer)randR1.get(idx);
			Integer val2 = (Integer)randR2.get(idx);
			
			int rezultatSum = 0;
			int rezultatAvg = 0;
			int rezultatCount = 0;
			int rezultatMin = 0;
			int rezultatMax = 0;

			if(val1 != null && val2 != null){
				rezultatSum = val1.intValue() + val2.intValue();
				rezultatAvg = val1.intValue() + val2.intValue();
				rezultatCount = val1.intValue() + val2.intValue();
				rezultatMin =  val1 < val2 ? val1 : val2;
				rezultatMax = val1 > val2 ? val1 : val2;
			}
			
			
			
			switch (operatie) {
			case "sum":
					randResult.set(idx,  rezultatSum);
				break;
			case "avg":
					randResult.set(idx, rezultatAvg);
				break;
			case "count":
					randResult.set(idx, rezultatCount);
				break;
			case "min":
				randResult.set(idx, rezultatMin);
				break;
			case "max":
				randResult.set(idx, rezultatMax);
				break;
			}
		}
		
		result.add(randResult);
		return result;
	}

}
