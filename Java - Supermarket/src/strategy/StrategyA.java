package strategy;

import item.Item;
import item.ItemList.ItemIterator;
import item.WishList;

public class StrategyA implements Strategy{

	
	@Override
	public Item execute(WishList wishlist) {
		ItemIterator it = wishlist.new ItemIterator();
		int indexProdusIeftin = 0;
		double pretProdusIeftin = Double.MAX_VALUE;
		int index = 0;
		while(it.hasNext()){
			Item item = it.next();
			if(item.getPret() < pretProdusIeftin){
				indexProdusIeftin = index;
				pretProdusIeftin = item.getPret();
			}
			index++;
		}
		
		if(pretProdusIeftin == Double.MAX_VALUE){
			return null;
		}
		
		return wishlist.getItem(indexProdusIeftin);
	}

}
