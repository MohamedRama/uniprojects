package model;

import java.util.ArrayList;
import java.util.List;

import item.Item;
import item.ItemList.ItemIterator;
import item.ShoppingCart;
import item.WishList;
import model.departments.BookDepartment;
import model.departments.MusicDepartment;
import model.departments.SoftwareDepartment;
import model.departments.VideoDepartment;
import strategy.Strategy;

public class Customer implements Observer{
		
	
	
	private String nume;
	
	private ShoppingCart shoppingCart;
	private WishList wishList = new WishList();
	
	private List<Notification> notificari = new ArrayList<>();
	
	public String getNume() {
		return nume;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	
	
	
	public void addNotificare(Notification notificare){
		
		notificari.add(notificare);
		// efectul notificarii
		
		// gasim produsul in listele clientului
		ItemIterator it = shoppingCart.new ItemIterator();
		Item itemCautatInSC = null;
		while(it.hasNext()){
			Item itemDinSC = it.next();
			if(itemDinSC.getID().equals(notificare.getIdProdus())){ // produsul din notificare este in SC
				itemCautatInSC = itemDinSC;
				break;
			}
		}
		if(itemCautatInSC != null){
			// TEST: modificam produsul in SC nu tinem cont de departament
			Department dept = itemCautatInSC.getDepartament();
			
			// aplicam inca o data accept pe SC?
			if(dept instanceof BookDepartment){
				shoppingCart.visit((BookDepartment)dept);
			}else if(dept instanceof MusicDepartment){
				shoppingCart.visit((MusicDepartment)dept);
			}else if(dept instanceof SoftwareDepartment){
				shoppingCart.visit((SoftwareDepartment)dept);
			}else if(dept instanceof VideoDepartment){
				shoppingCart.visit((VideoDepartment)dept);
			}
			
			
			// sau
			// reducem pur si simplu pretul item-ului cautat
		}
	}
	
	public Customer(ShoppingCart shoppingCart){
		this.shoppingCart = shoppingCart;
	}
	
	
	
	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}
	
	
	public WishList getWishList() {
		return wishList;
	}
	
	public void setWishList(WishList wishList) {
		this.wishList = wishList;
	}
	
	
	
	
	public List<Notification> getNotificari() {
		return notificari;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nume == null) ? 0 : nume.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (nume == null) {
			if (other.nume != null)
				return false;
		} else if (!nume.equals(other.nume))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return nume;
	}
	
	
	
}
