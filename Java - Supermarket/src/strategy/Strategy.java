package strategy;

import item.Item;
import item.WishList;

public interface Strategy {

	public Item execute(WishList wishlist);
	
}
