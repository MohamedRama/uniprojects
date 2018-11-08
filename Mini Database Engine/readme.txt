## Detalii implementare:
	Table.java
		reprezentarea unui tabel, incapsuland "randurile" sub forma listelor.
		
	SelectorService.java
		Clasa "helper" ce permite efectuarea operatiilor referitoare la selectare si updatare.
		Operatiile permit segmentarea listelor corespunzatoare randurilor din tabel, intrucat Database.java porneste
			mai multe instante Callable in vederea obtinerii rezultatelor sub forma Future-urilor, rezultate ulterior 
			agregate.
			
	SingleResultService.java
		Clasa "helper" aditionala ce permite returnarea rezultatelor de tip un singur rand. Acestea corespund
		query-urilor folosind functii precum min, max, avg etc.
		
	WorkerThreadSelect.java
		Clasa implementand Callable, in vederea obtinerii rezultatelor pentru operatii de tip 'select'. 


## Probleme in implementare

In momentul testarii, un select genera ClassCastException: 			
			String[] operations = {"studentName", "grade0", "grade1", "grade2", "grade3"};
Variabila 'operations' sugereaza crearea unui "ResultSet" a carui prima 'coloana' corespunde unui String. Cu toate acestea,
valorile erau castate intr-un int, ceea ce genera exceptia mai sus mentionata. In vederea rularii corespunzatoare, pentru a putea
testa am facut modificarile (ConsistencyWriterThreads.java): 
			
			valuesA.add((int)resultsA.get(1).get(1)+1);
			valuesB.add((int)resultsB.get(1).get(1)-1);
			
		De asemenea, am adaugat o verificare pentru a asigura evitarea problemelor in ceea ce priveste dimensiunea listei (cazul
in care aplicatia obtinea un "ResultSet" de dimensiune 0 (ConsistencyWriterThreads.java): 

			if(resultsA.isEmpty()){
				continue;
			}
			
