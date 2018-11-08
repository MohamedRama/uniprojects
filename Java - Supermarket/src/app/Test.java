package app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import item.DepartmentFactory;
import item.Item;
import item.ItemList.ItemIterator;
import model.Customer;
import model.Department;
import model.Notification;
import model.Notification.NotificationType;
import model.Store;
import model.departments.BookDepartment;
import model.departments.MusicDepartment;
import model.departments.SoftwareDepartment;
import model.departments.VideoDepartment;
import strategy.StrategyA;
import strategy.StrategyB;
import strategy.StrategyC;

public class Test {

	private static final String PATH_TEST = "C:\\Users\\mohamed\\Desktop\\TemaPOO\\tests";

	public static Store citireStore(String path) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		String linie = null;
		Store store = new Store();
		store.setNume(br.readLine());
		while ((linie = br.readLine()) != null) {

			String numeDepartament = linie.split(";")[0];
			Integer idDept = Integer.valueOf(linie.split(";")[1]);
			int nrProduse = Integer.valueOf(br.readLine());
			Department dept = DepartmentFactory.createDepartment(numeDepartament, idDept);
			for (int i = 0; i < nrProduse; i++) {
				String produsElemente[] = br.readLine().split(";");
				Item item = DepartmentFactory.createProduct(produsElemente[0], Integer.valueOf(produsElemente[1]),
						Double.valueOf(produsElemente[2]));
				dept.addItem(item);
			}

			store.addDepartment(dept);
		}
		br.close();
		return store;

	}

	public static void citireClienti(String path, Store store) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		int nrClienti = Integer.valueOf(br.readLine());
		for (int i = 0; i < nrClienti; i++) {
			String clientElements[] = br.readLine().split(";");
			String clientName = clientElements[0];
			double bugetClient = Double.valueOf(clientElements[1]);
			String strategy = clientElements[2];

			Customer cust = new Customer(store.getShoppingCart(bugetClient));
			switch (strategy) {
			case "A":
				cust.getWishList().setStrategy(new StrategyA());
				break;
			case "B":
				cust.getWishList().setStrategy(new StrategyB());
				break;
			case "C":
				cust.getWishList().setStrategy(new StrategyC());
				break;
			}
			cust.setNume(clientName);
			store.enter(cust);
		}
		br.close();
	}

	public static boolean shouldRemoveFromObservers(Customer cust, Department dept) {
		ItemIterator it = cust.getWishList().new ItemIterator();
		boolean shouldRemove = true;
		while (it.hasNext()) {
			Item item = it.next();
			// verificam daca mai este cel putin un alt produs pe wish list care
			// sa fie in departamentul de la care vrem sa scoatem observer-ul
			if (item.getDepartament().getID().equals(dept.getID())) {
				shouldRemove = false;
			}
		}

		return shouldRemove;
	}

	public static String evaluateCommand(Store store, String command) {
		String result = null;
//		System.out.println("COMANDA: " + command);
		String commandItems[] = command.split(";");
		String commandName = commandItems[0];
		if (commandName.equals("addItem")) {
			int itemId = Integer.valueOf(commandItems[1]);
			String scwl = commandItems[2];
			String customerName = commandItems[3];

			Customer customer = store.getCustomers().stream().filter((p) -> p.getNume().equals(customerName))
					.collect(Collectors.toList()).get(0);
			Item item = store.getItemFromDepartment(itemId);
			switch (scwl) {
			case "WishList":
				customer.getWishList().add(item);
				Department dept = item.getDepartament();
				dept.addObserver(customer);
				break;
			case "ShoppingCart":
				customer.getShoppingCart().add(item);
//				System.out.println("REMOVING FROM SHOPPING CART: " + item.getNume());
				customer.getWishList().remove(item);
				if (shouldRemoveFromObservers(customer, item.getDepartament())) {
					item.getDepartament().removeObserver(customer);
				}
				break;
			default:
				break;
			}
		} else if (commandName.equals("delItem")) {

			// delItem;ItemID;ShoppingCart/WishList;CustomerName-operat
			int itemId = Integer.valueOf(commandItems[1]);
			String scwl = commandItems[2];
			String customerName = commandItems[3];
			Customer customer = store.getCustomers().stream().filter((p) -> p.getNume().equals(customerName))
					.collect(Collectors.toList()).get(0);
			Item item = store.getItemFromDepartment(itemId);
			switch (scwl) {
			case "WishList":
				customer.getWishList().remove(item);
				if (shouldRemoveFromObservers(customer, item.getDepartament())) {
					item.getDepartament().removeObserver(customer);
				}
				break;
			case "ShoppingCart":
				customer.getShoppingCart().remove(item);
				break;
			default:
				break;
			}

		} else if (commandName.equals("addProduct")) {
			int depId = Integer.valueOf(commandItems[1]);
			int itemId = Integer.valueOf(commandItems[2]);
			double itemPrice = Double.valueOf(commandItems[3]);
			String itemName = commandItems[4];
			Department dept = store.getDepartment(depId);

			Item item = new Item(itemId, itemName, itemPrice, dept);
			dept.addItem(item);
			item.getDepartament().notifyAllObservers(
					new Notification(NotificationType.ADD, item.getDepartament().getID(), item.getID()));
		
		} else if (commandName.equals("modifyProduct")) {
			// modifyProduct;DepID;ItemID;Price
			int depId = Integer.valueOf(commandItems[1]);
			int itemId = Integer.valueOf(commandItems[2]);
			double itemPrice = Double.valueOf(commandItems[3]);

			Item itemInStoreDepartment = store.getItemFromDepartment(itemId);
			itemInStoreDepartment.setPret(itemPrice);
			itemInStoreDepartment.getDepartament()
					.notifyAllObservers(new Notification(NotificationType.MODIFY, depId, itemId));

		} else if (commandName.equals("delProduct")) {

			// delProduct;ItemID - elimina produsul din magazin (daca se afla în
			// lista vreunui client, este eliminat)
			int itemId = Integer.valueOf(commandItems[1]);
			Item itemInStoreDepartment = store.getItemFromDepartment(itemId);
			itemInStoreDepartment.getDepartament().notifyAllObservers(
					new Notification(NotificationType.MODIFY, itemInStoreDepartment.getDepartament().getID(), itemId));

			List<Customer> storeCustomers = store.getCustomers();
			for (Customer cust : storeCustomers) {
				if (cust.getShoppingCart().contains(itemInStoreDepartment)) {
					int index = cust.getShoppingCart().indexOf(itemInStoreDepartment);
					cust.getShoppingCart().remove(index);
				}
				if (cust.getWishList().contains(itemInStoreDepartment)) {
					int index = cust.getWishList().indexOf(itemInStoreDepartment);
					cust.getWishList().remove(index);

					if (shouldRemoveFromObservers(cust, itemInStoreDepartment.getDepartament())) {
						itemInStoreDepartment.getDepartament().removeObserver(cust);
					}
				}
			}
			for (Department dept : store.getDepartments()) {
				dept.getItems().remove(itemInStoreDepartment);
			}
		} else if (commandName.equals("getItem")) {
			// getItem;CustomerName - întoarce produsul ales conform strategiei
			// definite în cadrul fisierului customers.txt pentru clientul  specificat
			String customerName = commandItems[1];
			Customer customer = null;
			for (Customer cust : store.getCustomers()) {
				if (cust.getNume().equals(customerName)) {
					customer = cust;
					break;
				}
			}

			// metoda getItem muta elementul din WishList in ShoppingCart
			Item itemSelected = customer.getWishList().getStrategy().execute(customer.getWishList());
			customer.getWishList().remove(itemSelected);

			if (shouldRemoveFromObservers(customer, itemSelected.getDepartament())) {
				itemSelected.getDepartament().removeObserver(customer);
			}
			customer.getShoppingCart().add(itemSelected);

		} else if (commandName.equals("getItems")) {
			// getItems;ShoppingCart/WishList;CustomerName-întoarce produsele
			// din lista clientului
			String scwl = commandItems[1];
			String customerName = commandItems[2];

			Customer customer = null;
			for (Customer cust : store.getCustomers()) {
				if (cust.getNume().equals(customerName)) {
					customer = cust;
					break;
				}
			}

			switch (scwl) {
			case "WishList":
				customer.getWishList();
				result = customer.getWishList().toString();
				break;
			case "ShoppingCart":
				result = customer.getShoppingCart().toString();
				customer.getShoppingCart();
				break;
			default:
				break;
			}

		} else if (commandName.equals("getTotal")) {
			// getTotal;ShoppingCart/WishList;CustomerName - întoarce totalul
			// produselor din cos ,ul de cumparaturi sau lista de dorint ,e
			// pentru clientul indicat;
			String scwl = commandItems[1];
			String customerName = commandItems[2];

			Customer customer = null;
			for (Customer cust : store.getCustomers()) {
				if (cust.getNume().equals(customerName)) {
					customer = cust;
					break;
				}
			}
			double total = 0;
			switch (scwl) {
			case "WishList":
				ItemIterator it = customer.getWishList().new ItemIterator();
				while (it.hasNext()) {
					Item item = it.next();
					total += item.getPret();
				}
				break;
			case "ShoppingCart":
				ItemIterator it2 = customer.getShoppingCart().new ItemIterator();
				while (it2.hasNext()) {
					Item item = it2.next();
					total += item.getPret();
				}
				break;
			default:
				break;
			}

			result = "" + total;

		} else if (commandName.equals("accept")) {
			// accept;DepID;CustomerName
			// apeleaza metoda accept pentru departamentul s ,i clientul indicat
			int depId = Integer.valueOf(commandItems[1]);
			String customerName = commandItems[2];
			Customer customer = null;
			for (Customer cust : store.getCustomers()) {
				if (cust.getNume().equals(customerName)) {
					customer = cust;
					break;
				}
			}
			Department dept = null;
			store.getDepartment(depId);
			if (dept instanceof BookDepartment) {
				customer.getShoppingCart().visit((BookDepartment) dept);
			} else if (dept instanceof MusicDepartment) {
				customer.getShoppingCart().visit((MusicDepartment) dept);
			} else if (dept instanceof SoftwareDepartment) {
				customer.getShoppingCart().visit((SoftwareDepartment) dept);
			} else if (dept instanceof VideoDepartment) {
				customer.getShoppingCart().visit((VideoDepartment) dept);
			}
		} else if (commandName.equals("getObservers")) {
			// getObservers;DepID - întoarce observatorii departamentului;
			int depId = Integer.valueOf(commandItems[1]);
			Department dept = store.getDepartment(depId);
			result = dept.getCustomers().toString();
		} else if (commandName.equals("getNotifications")) {
			// getNotifications;CustomerName - întoarce notificarile clientului
			String customerName = commandItems[1];
			Customer cust = store.getCustomers().stream().filter((c) -> c.getNume().equals(customerName))
					.collect(Collectors.toList()).get(0);
			result = cust.getNotificari().toString();
		}
		
		return result;

	}

	public static List<String> citireEvents(Store store, String path) throws IOException {
		List<String> results = new ArrayList<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		int nrEvents = Integer.valueOf(br.readLine());
		for (int i = 0; i < nrEvents; i++) {
			String command = br.readLine();
//			System.out.println("----------------------------------------COMMAND: " + command);
			String result = evaluateCommand(store, command);
			results.add(result);
		}
		br.close();
		return results;
	}

	public static void main(String[] args) throws IOException {

		String testCurent = "test00";

		String path = PATH_TEST + "\\" + testCurent;

		Store store = citireStore(path + "\\store.txt");
		citireClienti(path + "\\customers.txt", store);
		List<String> results = citireEvents(store, path + "\\events.txt");

		System.out.println("--------------REZULTATE------------");
		PrintWriter out = new PrintWriter(new FileOutputStream("rezultate.txt"));
		for (String result : results) {
			if (result != null) {
				System.out.println(result);
				out.println(result);
			}
		}
		out.flush();
		out.close();

		System.out.println("---------------REZULTATE END----------");
	}

}
