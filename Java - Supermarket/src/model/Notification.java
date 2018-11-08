package model;

public class Notification {

	public enum NotificationType{
		ADD, REMOVE, MODIFY
	}
	
	private NotificationType tipNotificare;
	private Integer idDepartament;
	private Integer idProdus;

	public Notification(NotificationType tipNotificare, Integer idDepartament, Integer idProdus) {
		this.tipNotificare = tipNotificare;
		this.idDepartament = idDepartament;
		this.idProdus = idProdus;
	}

	public NotificationType getTipNotificare() {
		return tipNotificare;
	}

	public Integer getIdDepartament() {
		return idDepartament;
	}

	public Integer getIdProdus() {
		return idProdus;
	}

	@Override
	public String toString() {
		return tipNotificare + ";" + idProdus +";"+ idDepartament;
	}
	
}
