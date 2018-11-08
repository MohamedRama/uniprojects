package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import item.Item;
import item.ShoppingCart;

public abstract class Department implements Subject{

	private Integer ID;
	private String nume;
	
	private List<Customer> clientiCumparat = new ArrayList<>();
	private List<Customer> clientiDoresc = new ArrayList<>();
	
	private List<Item> itemuriVanzare = new ArrayList<>();
	
	// client care a cumparat cel putin un produs apartinand departamentului
	public void enter(Customer c){
		clientiCumparat.add(c);
	}
	
	// clientul nu mai place departamentul
	public void exit(Customer c){
		
		// TODO: de unde se scoate clientul?
		Iterator<Customer> it = clientiCumparat.iterator();
		while(it.hasNext()){
			Customer cust = it.next();
			if(cust.equals(c)){
				it.remove();
			}
		}
	}
	/**
	 * Adaugare item la produsele departamentului
	 * @param item
	 */
	public void addItem(Item item){
		itemuriVanzare.add(item);
		item.setDepartament(this);
	}
	/**
	 * intoarce produsele departamentului
	 * @return
	 */
	public List<Item> getItems(){
		return itemuriVanzare;
	}
	public Item getDepartmentItemByName(String numeItem){
		for(Item item : itemuriVanzare){
			if(item.getNume().equals(numeItem)){
				return item;
			}
		}
		return null;
	}
	public Item getDepartmentItemByID(Integer itemId){
		for(Item item : itemuriVanzare){
			if(item.getID().equals(itemId)){
				return item;
			}
		}
		return null;
	}	
	public List<Customer> getCustomers(){
		return clientiDoresc;
	}
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}	
	public String getNume() {
		return nume;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	/**
	 * Metoda apelata cand clientul isi doreste cel putin un produs din departament
	 */
	@Override
	public void addObserver(Customer customer){
		clientiDoresc.add(customer);
	}
	/**
	 * pentru a sterge un client ca observator 
	 * trebuie apelata cand clientul nu mai are in <code>WishList</code> nicun produs din departamentul curent
	 */
	@Override
	public void removeObserver(Customer customer){
		clientiDoresc.remove(customer);
	}
	/***
	 * 
	 * Trimite notificarea catre client.
	 * Metoda apelata daca este adaugat sau eliminat un produs din departament
	 */
	@Override
	public void notifyAllObservers(Notification notification){
		for(Customer observer : clientiDoresc){
			observer.addNotificare(notification);
		}
		
		for(Customer observer : clientiCumparat){
			observer.addNotificare(notification);
		}
	}
	/**
	 * Implementarile solicita vizitarea de catre visitator
	 * @param shoppingCart
	 */
	public abstract void accept(ShoppingCart shoppingCart);

	@Override
	public String toString() {
		return "DEPT = [" + ID + ", " + nume + "]";
	}
}