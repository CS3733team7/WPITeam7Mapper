package edu.wpi.off.by.one.errors.code.model;

public class Id {
	int indice;
	int unique;
	public Id(int i, int u){
		indice = i;
		unique = u;
	}
	public Id(){
		indice = -1;
		unique = -1;
	}
	public boolean compare(Id to){
		return (to.indice == indice && to.unique == unique);
	}
	public int getIndice(){
		return indice;
	}

}
