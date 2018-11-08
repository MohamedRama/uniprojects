package item;

import java.util.Collection;
import java.util.Comparator;
import java.util.ListIterator;
/**
 * Lista dublu inlantuita sortata
 * @author
 */
public class ItemList {

	private Node<Item> firstElement;
	private Item latestItem = null;
	private int size = 0;

	private static class Node<T> {
		private T data;

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}

		private Node<T> next = null;
		private Node<T> previous = null;
	}

	private Comparator<Item> comparator;

	public ItemList(Comparator<Item> comparator) {
		this.comparator = comparator;
	}
	
	public Item getLatestItem() {
		return latestItem;
	}

	public void setLatestItem(Item latestItem) {
		this.latestItem = latestItem;
	}

	// depinde de sortare
	public boolean add(Item element) {
		latestItem = element;
		displayIL();
		if (firstElement == null) {
//			System.out.println("ADDING FIRST ELEMENT: " + element.getNume());
			firstElement = new Node<>();
			firstElement.setData(new Item(element));
			size++;
			return true;
		} else if (firstElement.next == null) {
//			System.out.println("ADDING SECOND ELEMENT " + element.getNume());
			// lista cu un singur element in ea

			if (comparator.compare(element, firstElement.data) >= 0) {
				firstElement.next = new Node<Item>();
				firstElement.next.data = new Item(element);
				firstElement.next.previous = firstElement;
			} else {
				Node<Item> newNode = new Node<>();
				newNode.data = new Item(element);
				newNode.next = firstElement;
				firstElement.previous = newNode;
				firstElement = newNode;
			}
			size++;
			return true;
		} else {
			// lista cu minim doua elemente
//			System.out.println("ADDING ANOTHER ELEMENT");
			// mai avem elemente in lista si trebuie sa adaugam item-ul la locul  corespunzator
			int pozitieAdaugare = 0;

			Node<Item> currentNode = firstElement;

			if (comparator.compare(element, currentNode.data) < 0) {
//				System.out.println("0ADAUGAM la pozitia 0 PE: " + element.getNume());
				Node<Item> nodNou = new Node<Item>();
				nodNou.data = new Item(element);
				nodNou.next = firstElement;
				firstElement.previous = nodNou;
				firstElement = nodNou;
				size++;
				return true;
			} else {
				currentNode = firstElement;
				while (currentNode != null) {
					if (comparator.compare(element, currentNode.data) > 0) {
						pozitieAdaugare++;
					} else {
						break;
					}
					currentNode = currentNode.next;
				}
				
				currentNode = firstElement;
				for (int i = 0; i < pozitieAdaugare; i++) {
					currentNode = currentNode.next;
				}
				Node<Item> nodNou = new Node<Item>();
				nodNou.data = new Item(element);

				if (pozitieAdaugare == size) {
					// adaugam la sfarsit
//					System.out.println("xADAUGAM " + element.getNume() + " LA SFARSIT");
					currentNode = firstElement;
					while (currentNode.next != null) {
						currentNode = currentNode.next;
					}
					// currentNode - penultimul element din lista
					currentNode.next = nodNou;
					nodNou.previous = currentNode;
					size++;
				} else {
					// adaugam la mijloc

					currentNode = firstElement;
					for (int i = 0; i < pozitieAdaugare; i++) {
						currentNode = currentNode.next;
					}
				
					if(currentNode == null){
						return false;
					}
					
					Node<Item> next = currentNode.next;

					Node<Item> previous = currentNode.previous;
					if (previous != null) {
						previous.next = nodNou;
						nodNou.previous = previous;

					}

					nodNou.next = currentNode;
					currentNode.previous = nodNou;

					size++;
				}
				return true;

			}
		}
	}

	public int size() {
		return size;
	}

	public String displayIL() {
		Node<Item> currentNode = firstElement;
		String res = "[";
		while (currentNode != null) {
			res += currentNode.getData().getNume() + "{" + currentNode.getData().getPret() + "}" + ", ";
			currentNode = currentNode.next;
		}
		res += "]";
		return res;
	}

	public boolean addAll(Collection<? extends Item> c) {
		for (Item i : c) {
			add(i);
		}
		return true;
	}

	public Item getItem(int index) {
		Node<Item> nodCautat = getNode(index);
		return nodCautat.data;
	}

	public Node<Item> getNode(int index) {
		Node<Item> currentNode = firstElement;
		int pozitie = 0;
		while (currentNode != null) {
			if (pozitie == index) {
				return currentNode;
			}
			currentNode = currentNode.next;
			pozitie++;
		}
		// nu avem index-ul

		throw new IndexOutOfBoundsException("Nu putem accesa pozitia: " + index);
	}

	public int indexOf(Item item) {
		if (!contains(item)) {
			return -1;
		}
		if (isEmpty())
			return -1;
		int index = 0;
		Node<Item> currentNode = firstElement;
		while (currentNode != null) {
			if (currentNode.data != null && currentNode.data.equals(item)) {
				return index;
			}
			currentNode = currentNode.next;
			index++;
		}
		return -1;
	}

	public int indexOf(Node<Item> node) {
		throw new UnsupportedOperationException();
	}

	public boolean contains(Node<Item> node) {
		return contains(node.data);
	}

	public boolean contains(Item item) {
		Node<Item> currentNode = firstElement;
		if (isEmpty())
			return false;
		while (currentNode != null) {
			if (currentNode.data != null && currentNode.data.equals(item)) {
				return true;
			}
			currentNode = currentNode.next;
		}
		return false;
	}

	public Item remove(int index) {
		Node<Item> nodDeSters = getNode(index);

		Node<Item> previous = null;
		Node<Item> next = null;
		if (nodDeSters.previous != null)
			previous = nodDeSters.previous;
		if (nodDeSters.next != null)
			next = nodDeSters.next;

		if (previous != null && next != null) {
			previous.next = next;
			next.previous = previous;
		} else if (previous == null) {
			firstElement.next.previous = null;
			firstElement = firstElement.next;
		} else if (next == null) {
			previous.next = null;

		}
		size--;
		return nodDeSters.data;
	}

	public boolean remove(Item item) {
//		System.out.println("REMOVING ITEM: " + item.getNume());
		int index = indexOf(item);
		if (index == -1)
			return false;
		remove(index);
		return true;
	}

	public boolean removeAll(Collection<? extends Item> collection) {
		throw new UnsupportedOperationException();
	}

	public boolean isEmpty() {
		return firstElement == null;
	}

	public Double getTotalPrice() {
		double totalPrice = 0;
		Node<Item> currentNode = firstElement;
		if (isEmpty())
			return null;
		while (currentNode != null) {
			totalPrice += currentNode.data.getPret();
			currentNode = currentNode.next;
		}
		return totalPrice == 0 ? null : totalPrice;
	}

	public class ItemIterator implements ListIterator<Item> {

		private int position;

		@Override
		public void add(Item item) {
			ItemList.this.add(item);
		}

		@Override
		public boolean hasNext() {
			if (position == size) {
				return false;
			}
			return true;
		}

		@Override
		public boolean hasPrevious() {
			Node<Item> nodCurent = getNode(position);
			if (nodCurent.previous != null) {
				return true;
			}
			return false;
		}

		@Override
		public Item next() {
			return getItem(position++);
		}

		@Override
		public int nextIndex() {return 0;}

		@Override
		public Item previous() {return null;}

		@Override
		public int previousIndex() {return 0;}

		@Override
		public void remove() {}

		@Override
		public void set(Item arg0) {}

	}
	@Override
	public String toString() {
		return displayIL();
	}	
}