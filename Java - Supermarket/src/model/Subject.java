package model;

public interface Subject {
	
	public void addObserver(Customer observer);
	public void removeObserver(Customer observer);
	public void notifyAllObservers(Notification notification);
	
}
