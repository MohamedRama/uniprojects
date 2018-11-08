package item;

import java.util.Comparator;
import java.util.List;

import model.Visitor;
import model.departments.BookDepartment;
import model.departments.MusicDepartment;
import model.departments.SoftwareDepartment;
import model.departments.VideoDepartment;

public class ShoppingCart extends ItemList implements Visitor{

	private double buget;

	public ShoppingCart(double buget) {
		super(new Comparator<Item>(){ // clasa anonima

			@Override
			public int compare(Item item1, Item item2) {
				if(item1.getPret() == item2.getPret()){
					return item1.getNume().compareTo(item2.getNume());
				}
				
				return (int)(item1.getPret().doubleValue() - item2.getPret().doubleValue());
			}
			
		});
		
		this.buget = buget;
	}

	public double getBuget() {
		return buget;
	}
	/***
	 * Metoda scade 10% de la pretul fiecarui produs din ShoppingCart ce tine de acest departament
	 */
	@Override
	public void visit(BookDepartment department) {
		List<Item> produse = department.getItems();
		for(Item i : produse){
			if(contains(i)){
				int index = indexOf(i);
				Item itemDinSC = getItem(index);
				itemDinSC.setPret(itemDinSC.getPret() * 0.9);
			}
		}
	}
	/**
	 * Metoda adauga la buget 10% din valoarea totalului produselor ce apartin departamentului
	 * si sunt in cosul de cumparaturi
	 */
	@Override
	public void visit(MusicDepartment department) {
		List<Item> produse = department.getItems();
		double deAdaugat = 0;
		for(Item i : produse){
			if(contains(i)){
				int index = indexOf(i);
				Item itemDinSC = getItem(index);
				// itemDinSC.setPret(itemDinSC.getPret() * 0.9);
				deAdaugat += itemDinSC.getPret() * 0.1;
			}
		}
		buget += deAdaugat;
	}
	/***
	 * Metoda modifica pretul fiecarui produs din ShoppingCart ce apartine departamentului:
	 * 	retucere de 20% daca bugetul nu mai permite cumpararea unui alt produs din acest departament
	 *  :: Daca bugetul ramas disponibil pentru acel cos de cumparaturi este mai mic decat
	 *  	pretul celui mai ieftin produs ce apartine departamentului software
	 */
	@Override
	public void visit(SoftwareDepartment department) {
		List<Item> produse = department.getItems();
		boolean putemCumpara = false;
		for(Item i : produse){
			if(buget >= i.getPret()){
				putemCumpara = true;
			}
		}
		if(!putemCumpara){
			// aplicam reducerea
			for(Item i : produse){
				if(contains(i)){
					int index = indexOf(i);
					Item itemDinSC = getItem(index);
					 itemDinSC.setPret(itemDinSC.getPret() * 0.8);
				}
			}
		}
	}
	/**
	 * Metoda modifica pretul fiecarui produs din ShoppingCart ce apartine acestui departament
	 * daca valoarea totalului produselor ce apartin departamentului este mai mare decat cel
	 * mai scump produs din acest departament: reducere 15%
	 * Se va adauga la bugetul cosului de cumparaturi 5% din valoarea totalului produselor ce apartin departamentului
	 * (irrespective of first condition)
	 */
	@Override
	public void visit(VideoDepartment department) {
		
		double sumaPreturiVideo = 0; // valoarea totalului produselor ce apartin departamentului
		for(Item i : department.getItems()){
			if(contains(i)){
				int index = indexOf(i);
				Item itemDinSC = getItem(index);
				sumaPreturiVideo += itemDinSC.getPret();
				
			}
		}
		
		double pretCelMaiScump = -1; // pretul celui mai scump produs din acest departament
		for(Item i : department.getItems()){
			if(i.getPret() > pretCelMaiScump){
				pretCelMaiScump = i.getPret();
			}
		}
		
		double suma5 = 0;
		if(sumaPreturiVideo > pretCelMaiScump){
			for(Item i : department.getItems()){
				if(contains(i)){
					int index = indexOf(i);
					Item itemDinSC = getItem(index);
					suma5 += itemDinSC.getPret();
					itemDinSC.setPret(itemDinSC.getPret() * 0.85);
				}
			}
		}
		buget += suma5;
	}
}