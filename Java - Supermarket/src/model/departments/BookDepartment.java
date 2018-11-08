package model.departments;

import item.ShoppingCart;
import model.Department;

public class BookDepartment extends Department{

	/***
	 * Metoda scade 10% de la pretul fiecarui produs din ShoppingCart ce tine de acest departament
	 */
	@Override
	public void accept(ShoppingCart shoppingCart) {
		shoppingCart.visit(this);
	}

}
