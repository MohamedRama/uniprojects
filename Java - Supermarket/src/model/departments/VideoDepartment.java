package model.departments;

import item.ShoppingCart;
import model.Department;

public class VideoDepartment extends Department{

	/**
	 * Metoda modifica pretul fiecarui produs din ShoppingCart ce apartine acestui departament
	 * daca valoarea totalului produselor ce apartin departamentului este mai mare decat cel
	 * mai scump produs din acest departament: reducere 15%
	 * Se va adauga la bugetul cosului de cumparaturi 5% din valoarea totalului produselor ce apartin departamentului
	 * (irrespective of first condition)
	 */
	@Override
	public void accept(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		shoppingCart.visit(this);
	}

	
	
}
