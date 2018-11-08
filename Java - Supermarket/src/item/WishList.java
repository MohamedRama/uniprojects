package item;

import java.util.Comparator;

import strategy.Strategy;

public class WishList extends ItemList {

	private Strategy strategy;
	
	
	
	public WishList() {
		super(new Comparator<Item>() { // clasa anonima

			@Override
			public int compare(Item item1, Item item2) {
				// Produsele din WishList se vor sorta alfabetic, dupa denumirea lor.
				return item1.getNume().compareTo(item2.getNume());
			}

		});
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	

}
