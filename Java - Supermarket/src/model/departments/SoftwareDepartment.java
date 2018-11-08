package model.departments;

import item.ShoppingCart;
import model.Department;

public class SoftwareDepartment extends Department{

	/***
	 * Metoda modifica pretul fiecarui produs din ShoppingCart ce apartine departamentului:
	 * 	retucere de 20% daca bugetul nu mai permite cumpararea unui alt produs din acest departament
	 *  :: Daca bugetul ramas disponibil pentru acel cos de cumparaturi este mai mic decat
	 *  	pretul celui mai ieftin produs ce apartine departamentului software
	 */
	@Override
	public void accept(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		shoppingCart.visit(this);
	}

}
