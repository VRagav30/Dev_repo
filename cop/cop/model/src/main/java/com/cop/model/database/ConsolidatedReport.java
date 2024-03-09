package com.cop.model.database;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsolidatedReport {
	
	public String plant;
	public String objcode;
	public String material;
	public String stock_type;
	public String sale_order;
	public String valsub;
	public String batch;
	public String procurement_receipts;
	public String job_receipts;
	public String production_receipts;
	public String sto_receipts;
	public String stointransit_receipts;
	public String transfer_recipts;
	public String procurement_return;
	public String job_consumption;
	public String production_consumption;
	public String sto_delivery;
	public String transfer_delivery;
	public String sale_delivery;
	public String costcenter_consumption;


}
