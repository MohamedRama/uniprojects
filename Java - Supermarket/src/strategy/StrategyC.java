package strategy;

import item.Item;
import item.WishList;

public class StrategyC implements Strategy{

	@Override
	public Item execute(WishList wishlist) {
		return wishlist.getLatestItem();
	}

}
