package it.polito.tdp.artsmia.model;

public class ArtConnessi implements Comparable<ArtConnessi>{

	private Integer art1;
	private Integer art2;
	private Integer peso;
	
	public ArtConnessi(Integer art1, Integer art2, Integer peso) {
		super();
		this.art1 = art1;
		this.art2 = art2;
		this.peso = peso;
	}

	public Integer getArt1() {
		return art1;
	}

	public void setArt1(Integer art1) {
		this.art1 = art1;
	}

	public Integer getArt2() {
		return art2;
	}

	public void setArt2(Integer art2) {
		this.art2 = art2;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	@Override
	//ordine decrescente
	public int compareTo(ArtConnessi o) {
		// TODO Auto-generated method stub
		return -this.peso.compareTo(o.getPeso());
	}
	
	
	
	
	
}
