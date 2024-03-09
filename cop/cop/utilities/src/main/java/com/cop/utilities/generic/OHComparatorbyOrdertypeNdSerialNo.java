package com.cop.utilities.generic;

import java.util.Comparator;

import com.cop.model.database.Orderheader;

public class OHComparatorbyOrdertypeNdSerialNo implements Comparator<Orderheader> {
	@Override
	public int compare(Orderheader o1, Orderheader o2) {
		System.out.println(o1.getObjtype()+""+o2.getObjtype()+"comparing objtype");
		int value1 = o1.getObjtype().compareTo(o2.getObjtype());
		if (value1 == 0) {
			int value2 = o1.getPlant().compareTo(o2.getPlant());
			if (value2 == 0)
				return o1.getObjitnum().compareTo(o2.getObjitnum());
			else
				return value2;
		}
		return value1;
	}
}