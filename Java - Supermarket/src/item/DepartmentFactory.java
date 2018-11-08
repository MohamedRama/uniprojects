package item;

import model.Department;
import model.departments.BookDepartment;
import model.departments.MusicDepartment;
import model.departments.SoftwareDepartment;
import model.departments.VideoDepartment;

public class DepartmentFactory {

	public static Department createDepartment(String deptName, Integer deptId){
		Department dept = null;
		switch(deptName){
		case "BookDepartment":
			dept = new BookDepartment();
			break;
		case "MusicDepartment":
			dept = new MusicDepartment();
			break;
		case "SoftwareDepartment":
			dept = new SoftwareDepartment();
			break;
		case "VideoDepartment":
			dept = new VideoDepartment();
			break;
			default:
				throw new RuntimeException("Departament Inexistent");
		}
		dept.setID(deptId);
		dept.setNume(deptName);
		return dept;
		
	}
	
	public static Item createProduct(String productName, int productId, double productPrice){
		Item item = new Item(productId, productName, productPrice);
		return item;
	}
	
}
