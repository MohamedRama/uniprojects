package item;

import model.Department;

public class Item {

	private Integer ID;
	private String nume;
	private Double pret;
	
	private Department departament;	

	public Item(Integer iD, String nume, Double pret, Department departament) {
		ID = iD;
		this.nume = nume;
		this.pret = pret;
		this.departament = departament;
	}
	
	
	public Item(Integer iD, String nume, Double pret) {
		ID = iD;
		this.nume = nume;
		this.pret = pret;
	}
	
	public Item(Item other){
		this.ID = new Integer(other.ID);
		this.nume = new String(other.nume);
		this.pret = new Double(other.pret);
		this.departament = other.departament;
	}

	public Item() {
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

	public Double getPret() {
		return pret;
	}

	public void setPret(Double pret) {
		this.pret = pret;
	}

	public Department getDepartament() {
		return departament;
	}

	public void setDepartament(Department departament) {
		this.departament = departament;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
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
		Item other = (Item) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return nume + ";" + ID + ";" + pret;
	}
	
	
	
}
