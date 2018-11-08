package model;

import java.util.ArrayList;
import java.util.List;

import item.Item;
import item.ShoppingCart;

public class Store {

	private String nume;
	private List<Department> departments = new ArrayList<>();
	private List<Customer> customers = new ArrayList<>();

	public Item getItemFromDepartment(Integer itemId){
		for(Department dept : departments){
			Item search = dept.getDepartmentItemByID(itemId);
			if(search != null){
				return search;
			}
		}
		return null;
	}
	
	public void enter(Customer cs){
		customers.add(cs);
	}
	public void exit(Customer cs){
		customers.remove(cs);
	}
	
	// getShoppingCart(Double) intoarce un obiect de tip ShoppingCart, avand
	// asociat bugetul
	// indicat, ce urmeaza a fi folosit de catre client;

	public List<Customer> getCustomers() {
		return customers;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void addDepartment(Department dept) {
		departments.add(dept);
	}

	public Department getDepartment(Integer id) {
		for (Department dept : departments) {
			if (dept.getID().equals(id)) {
				return dept;
			}
		}
		return null;
	}
	
	public ShoppingCart getShoppingCart(double suma){
		return new ShoppingCart(suma);
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	@Override
	public String toString() {
		return "Store [nume=" + nume + ", departments=" + departments + ", customers=" + customers + "]";
	}
	
}
