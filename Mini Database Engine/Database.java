

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Database implements MyDatabase {

	private int numWorkerThreads;
	public List<Table> tables = Collections.synchronizedList(new ArrayList<>());

	private ExecutorService exe = null;

	@Override
	public void initDb(int numWorkerThreads) {
		this.numWorkerThreads = numWorkerThreads;
		exe = Executors.newFixedThreadPool(numWorkerThreads);
	}

	@Override
	public void stopDb() {
		exe.shutdown();
	}

	@Override
	public void createTable(String tableName, String[] columnNames, String[] columnTypes) {
		Table table = new Table();
		table.setTableName(tableName);
		table.setColumnNames(columnNames);
		table.setColumnTypes(columnTypes);
		tables.add(table);
	}

	@Override
	public ArrayList<ArrayList<Object>> select(String tableName, String[] columnsMinMaxSum, String where) {
		boolean singleResult = SelectorService.singleResult(columnsMinMaxSum);

		ArrayList<ArrayList<Object>> randurileSelectate = new ArrayList<>();
		ArrayList<ArrayList<Object>> randurileSelectateSingleResult = new ArrayList<>();

		Table table = SelectorService.getTableByName(this, tableName);

		List<Integer> indecsi = new ArrayList<>(); // 0 3 4 19
		List<String> indecsiOperatii = new ArrayList<>(); // sum sum max sum
		// !!!! indecsiOperatii ar trebui sa aiba exact atatea elemente cat are
		// si un rand din tabel
		// 3291 32019 4354309 3219
		// SELECT nume, pret from produse

		for (String columnName : columnsMinMaxSum) {
			Integer indexColoana = null;
			if (singleResult) {

				// columnsMinMaxSum VS
				indexColoana = table.getColumnNamesList()
						.indexOf(SelectorService.getNumeColoanaFromOperation(columnName));
				indecsiOperatii.add(columnName.split("\\(")[0]);
			} else {
				indexColoana = table.getColumnNamesList().indexOf(columnName);
			}
			indecsi.add(indexColoana);
		}

		// ArrayList<ArrayList<Object>> rezultate =
		// SelectorService.getRowsOkayForWhere(table, where, 0,
		// table.getRows().size(), indecsi, singleResult, columnsMinMaxSum);

		List<Future<ArrayList<ArrayList<Object>>>> futures = new ArrayList<>();
		// impartim tabela in cate thread-uri avem (numThreads) si pe urma sa
		// apelam in fiecare thread getRowsOkayForWhere

		int size = table.getRows().size();
		Future<ArrayList<ArrayList<Object>>> f1 = null, f2 = null, f3 = null, f4 = null;

		boolean necesarParalelizare = false;
		if (size > numWorkerThreads) {
			// 100
			// 0 - 24
			// 25- 49
			// 50 - 74
			// 75 - 100
			necesarParalelizare = true;

			f1 = exe.submit(new WorkerThreadSelect(table, 0, size / 4, where, singleResult, indecsi, columnsMinMaxSum));
			f2 = exe.submit(
					new WorkerThreadSelect(table, size / 4, size / 2, where, singleResult, indecsi, columnsMinMaxSum));
			f3 = exe.submit(new WorkerThreadSelect(table, size / 2, size * 3 / 4, where, singleResult, indecsi,
					columnsMinMaxSum));
			f4 = exe.submit(
					new WorkerThreadSelect(table, size * 3 / 4, size, where, singleResult, indecsi, columnsMinMaxSum));

			futures.add(f1);
			futures.add(f2);
			futures.add(f3);
			futures.add(f4);
		} else {
			if (size > 0) {
				f1 = exe.submit(new WorkerThreadSelect(table, 0, size, where, singleResult, indecsi, columnsMinMaxSum));
			} else {
			}
		}

		ArrayList<ArrayList<Object>> rezultateFinale = new ArrayList<>();

		if (singleResult) {
			try {

				if (necesarParalelizare) {

					ArrayList<ArrayList<Object>> rezultate1 = f1.get();
					ArrayList<ArrayList<Object>> rezultate2 = f2.get();

					ArrayList<ArrayList<Object>> rezultate3 = f3.get();
					ArrayList<ArrayList<Object>> rezultate4 = f4.get();

					ArrayList<ArrayList<Object>> merge1 = SingleResultService.mergeSingleResults(rezultate1, rezultate2,
							columnsMinMaxSum);
					ArrayList<ArrayList<Object>> merge2 = SingleResultService.mergeSingleResults(rezultate3, rezultate4,
							columnsMinMaxSum);

					rezultateFinale = SingleResultService.mergeSingleResults(merge1, merge2, columnsMinMaxSum);
				} else {
					if (size > 0) {
						rezultateFinale = f1.get();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

			if (necesarParalelizare) {
				for (Future<ArrayList<ArrayList<Object>>> future : futures) {
					try {
						ArrayList<ArrayList<Object>> rezultatPartial = future.get();
						rezultateFinale.addAll(rezultatPartial);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				try {
					rezultateFinale.addAll(f1.get());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return rezultateFinale;
	}

	private boolean isInserting = false;
	private boolean isUpdating = false;

	private boolean debug = true;

	@Override
	public void update(String tableName, ArrayList<Object> values, String where) {
		Table table = SelectorService.getTableByName(this, tableName);

		if (debug) {
			return;
		}
		// synchronized (table) {
		// while (isInserting) {
		// try {
		// wait();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// isUpdating = true;
		// notifyAll();

		List<Integer> positions = SelectorService.getRowsForUpdate(table, where);
		for (Integer pozitie : positions) {
			table.getRows().set(pozitie, values);
		}	

		// isUpdating = false;
		// notifyAll();
	}

	@Override
	public void insert(String tableName, ArrayList<Object> values) {
		Table table = SelectorService.getTableByName(this, tableName);

		// synchronized (table) {
		// while (isUpdating) {
		// try {
		// wait();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// }
		// isInserting = true;
		// notifyAll();
		table.getRows().add(values); // adaugam noul rand
		// isInserting = false;
		// notifyAll();
	}

	@Override
	public void startTransaction(String tableName) {
	}

	@Override
	public void endTransaction(String tableName) {
	}

}
