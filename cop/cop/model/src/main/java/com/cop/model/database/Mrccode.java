package com.cop.model.database;

import java.io.Serializable;

public class Mrccode implements Serializable{
	
	private String itemcode;
	private String plant;
	private String pricetype;
	
	public Mrccode() {
		super();
		
	}
	
	public Mrccode(String itemcode, String plant, String pricetype) {
		super();
		this.itemcode = itemcode;
		this.plant = plant;
		this.pricetype = pricetype;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemcode == null) ? 0 : itemcode.hashCode());
		result = prime * result + ((plant == null) ? 0 : plant.hashCode());
		result = prime * result + ((pricetype == null) ? 0 : pricetype.hashCode());
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
		Mrccode other = (Mrccode) obj;
		if (itemcode == null) {
			if (other.itemcode != null)
				return false;
		} else if (!itemcode.equals(other.itemcode))
			return false;
		if (plant == null) {
			if (other.plant != null)
				return false;
		} else if (!plant.equals(other.plant))
			return false;
		if (pricetype == null) {
			if (other.pricetype != null)
				return false;
		} else if (!pricetype.equals(other.pricetype))
			return false;
		return true;
	}
	
	
	

}
