package model;

import model.departments.BookDepartment;
import model.departments.MusicDepartment;
import model.departments.SoftwareDepartment;
import model.departments.VideoDepartment;

public interface Visitor {

	public void visit(BookDepartment department);
	public void visit(MusicDepartment department);
	public void visit(SoftwareDepartment department);
	public void visit(VideoDepartment department);
	
}
