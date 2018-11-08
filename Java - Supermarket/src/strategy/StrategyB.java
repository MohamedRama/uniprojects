package strategy;

import item.Item;
import item.ItemList.ItemIterator;
import item.WishList;

public class StrategyB implements Strategy{

	@Override
	public Item execute(WishList wishlist) {

		ItemIterator it = wishlist.new ItemIterator();
		if(it.hasNext()){
			return it.next();
		}
		return null;
	}

}
