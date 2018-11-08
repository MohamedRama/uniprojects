package model.departments;

import item.ShoppingCart;
import model.Department;

public class MusicDepartment extends Department{

	/**
	 * Metoda adauga la buget (in ShoppingCart)!!!! 10% din valoarea totalului produselor ce apartin departamentului
	 * si sunt in cosul de cumparaturi
	 */
	@Override
	public void accept(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		shoppingCart.visit(this);
	}

}
