package com.cop.serviceimpl.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cop.serviceimpl.service.event.*;
import com.cop.model.database.Costtotal;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Order;
import com.cop.model.database.Orderheader;
import com.cop.model.database.Transaction;
import com.cop.repository.transaction.CostTotalRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.serviceapi.service.CostTotalService;
import com.cop.serviceapi.service.MMTotalService;
import com.cop.serviceapi.service.OrderHeaderService;
import com.cop.serviceapi.service.OrderTransactionService;
import com.cop.serviceimpl.service.event.CNQR;
import com.cop.serviceimpl.service.event.CNVR;
import com.cop.serviceimpl.service.event.CTRGoodsIssue;
import com.cop.serviceimpl.service.event.DNQR;
import com.cop.serviceimpl.service.event.DNVR;
import com.cop.serviceimpl.service.event.Production;
import com.cop.serviceimpl.service.event.Purchase;
import com.cop.serviceimpl.service.event.PurchaseCNQR;
import com.cop.serviceimpl.service.event.PurchaseCNVR;
import com.cop.serviceimpl.service.event.PurchaseDNQR;
import com.cop.serviceimpl.service.event.PurchaseDNVR;
import com.cop.serviceimpl.service.event.PurchaseInvoiceBooking;
import com.cop.serviceimpl.service.event.PurchaseServiceRequest;
import com.cop.serviceimpl.service.event.Rework;
import com.cop.serviceimpl.service.event.SaleOrder;
import com.cop.serviceimpl.service.event.StockTransportOrder;
import com.cop.serviceimpl.service.event.Subcon;
import com.cop.serviceimpl.service.event.Transfer;

@Service
public class OrderTransactionServiceImpl implements OrderTransactionService {

	@Autowired
	OrderHeaderService ohs;

	@Autowired
	CostTotalService cts;

	@Autowired
	MMTotalService mts;

	@Autowired
	Subcon sc;

	@Autowired
	Production prod;
	
	@Autowired
	Rework rework;
	
	@Autowired
	Purchase pur;
	
	@Autowired
	Transfer trns;

	@Autowired
	PurchaseServiceRequest psr;

	@Autowired
	PurchaseInvoiceBooking pib;

	@Autowired
	CostTotalRepository ctr;
	
	@Autowired
	CTRGoodsIssue ctri;

	@Autowired
	MMTotalRespository mtr;

	@Autowired
	StockTransportOrder sto;

	@Autowired
	SaleOrder so;
	
	@Autowired
	PurchaseDNQR dnqr;
	
	@Autowired
	PurchaseCNQR cnqr;
	
	@Autowired
	PurchaseDNVR dnvr;
	
	@Autowired
	PurchaseCNVR cnvr;
	
	@Autowired
	InventoryLoss il;
	
	@Autowired
	InventoryGain ig;
	
	@Autowired
	CNVR cnvr1;
	
	@Autowired
	DNVR dnvr1;
	
	
	@Autowired
	DNQR dnqr1;
	
	@Autowired
	CNQR cnqr1;
	
	@Autowired
	InvoiceBooking ib;
	
	@Autowired
	ServiceRequest sr;
	
	@Autowired
	SaleDeliveryReturn sdt;
	
	@Autowired
	CTRDirectExpenses ctrde;

	@Override
	public void createAndSaveOrderTransactions(List<Transaction> transactions) {
		Iterator<Transaction> itr = transactions.iterator();
		List<Transaction> purchaseGoodRecieveList = new ArrayList<>();
		List<Transaction> purchaseInvoiceBookingList = new ArrayList<>();
		List<Transaction> purchaseServiceRequestList = new ArrayList<>();
		List<Transaction> openingStockList = new ArrayList<>();
		List<Transaction> vendorStockList = new ArrayList<>();
		List<Transaction> subconList = new ArrayList<>();
		List<Transaction> productionList = new ArrayList<>();
		List<Transaction> stocktransportorderList = new ArrayList<>();
		List<Transaction> stocktransportorderDEList = new ArrayList<>();
		List<Transaction> stocktransportorderPRList = new ArrayList<>();
		List<Transaction> saleorderList = new ArrayList<>();
		List<Transaction> sdtList = new ArrayList<>();
		List<Transaction> reworkList = new ArrayList<>();
		List<Transaction> ctriList=new ArrayList<>();
		List<Transaction> ctrdeList=new ArrayList<>();
		List<Transaction> dnqrList=new ArrayList<>();
		List<Transaction> cnqrList=new ArrayList<>();
		List<Transaction> dnvrList=new ArrayList<>();
		List<Transaction> cnvrList=new ArrayList<>();
		
		List<Transaction> dnqr1List=new ArrayList<>();
		List<Transaction> cnqr1List=new ArrayList<>();
		List<Transaction> dnvr1List=new ArrayList<>();
		List<Transaction> cnvr1List=new ArrayList<>();
		List<Transaction> ibList=new ArrayList<>();
		List<Transaction> srList=new ArrayList<>();
		
		List<Transaction> btList=new ArrayList<>();
		List<Transaction> mtList=new ArrayList<>();
		List<Transaction> stList=new ArrayList<>();
		List<Transaction> ptList=new ArrayList<>();
		List<Transaction> sotList=new ArrayList<>();
		
		List<Transaction> igList=new ArrayList<>();
		List<Transaction> ilList=new ArrayList<>();
		while (itr.hasNext()) {
			Transaction transaction = itr.next();
			if (transaction.getCobjtype() != null) {
				if (transaction.getTrnsevent() != null)
					switch (transaction.getCobjtype() + ":" + transaction.getTrnsevent()) {
					case "PUR:GR":
						purchaseGoodRecieveList.add(transaction);
						break;
					case "PUR:IB":
						purchaseInvoiceBookingList.add(transaction);
						break;
					case "PUR:SR":
						purchaseServiceRequestList.add(transaction);
						break;
					case "PUR:DNQR":
						dnqrList.add(transaction);
						break;
					case "PUR:DNVR":
						dnvrList.add(transaction);
						break;
					case "PUR:CNQR":
						cnqrList.add(transaction);
						break;
					case "PUR:CNVR":
						cnvrList.add(transaction);
						break;
					case "JOB:GR":
						subconList.add(transaction);
						break;
					case "JOB:GI":
						subconList.add(transaction);
						break;
					case "JOB:PC":
						subconList.add(transaction);
						break;
					case "JOB:CNVR":
						subconList.add(transaction);
						break;
					case "JOB:DNVR":
						subconList.add(transaction);
						break;
					case "JOB:CNQR":
						subconList.add(transaction);
						break;
					case "JOB:DNQR":
						subconList.add(transaction);
						break;
					case "JOB:IB":
						subconList.add(transaction);
						break;
					case "JOB:SR":
						subconList.add(transaction);
						break;
					case "PROD:GR":
						productionList.add(transaction);
						break;
					case "PROD:GI":
						productionList.add(transaction);
						break;
					case "PROD:BP":
						productionList.add(transaction);
						break;

					case "PROD:OC":
						productionList.add(transaction);
						break;
					case "PROD:OR":
						productionList.add(transaction);
						break;
					case "STO:STO":
						stocktransportorderList.add(transaction);
						break;
					case "STO:DE":
						stocktransportorderList.add(transaction);
						break;
					case "STO:PR":
						stocktransportorderPRList.add(transaction);
						break;
					case "SALE:SD":
						saleorderList.add(transaction);
						break;
					case "REWORK:GI":
						reworkList.add(transaction);
						break;
					case "REWORK:GR":
						reworkList.add(transaction);
						break;
					case "REWORK:OC":
						reworkList.add(transaction);
						break;
						
					case"CTR:GI":
						ctriList.add(transaction);
						break;
					case"CTR:DE":
						ctrdeList.add(transaction);
						break;
					case"SRO:SDT":
						sdtList.add(transaction);
						break;
					default:
						System.out.println("Please verify the Order Type and Event!");
					}

			} else if (transaction.getTrnsevent() != null)
				switch (transaction.getTrnsevent().toString()) {
				case "OS":
					openingStockList.add(transaction);
					break;
				case "VS":
					vendorStockList.add(transaction);
					break;
				case "MT":
					mtList.add(transaction);
					break;
				case "BT":
					btList.add(transaction);
					break;
				case "PT":
					ptList.add(transaction);
					break;
				case "ST":
					stList.add(transaction);
					break;
				case "SOT":
					sotList.add(transaction);
					break;
				case "IG":
					igList.add(transaction);
					break;
				case "IL":
					ilList.add(transaction);
					break;
				default:
					System.out.println("Please verify the Event!");
				}
		}
		if (purchaseGoodRecieveList.size() > 0) {
			pur.processPurchaseRecords(purchaseGoodRecieveList);
			}
		if (purchaseInvoiceBookingList.size() > 0) {
			pur.processPurchaseRecords(purchaseInvoiceBookingList);
		}
		if (purchaseServiceRequestList.size() > 0) {
			pur.processPurchaseRecords(purchaseServiceRequestList);
			
		}
		
		if (dnqrList.size() > 0) {
			pur.processPurchaseRecords(dnqrList);
		}
		if (cnqrList.size() > 0) {
			pur.processPurchaseRecords(cnqrList);
		}
		if(dnvrList.size()>0)
		{
			pur.processPurchaseRecords(dnvrList);
		}
		if(cnvrList.size()>0)
		{
			pur.processPurchaseRecords(cnvrList);
		}
		
		if(btList.size()>0)
			trns.processTransferRecords(btList);
		
		if(mtList.size()>0)
			trns.processTransferRecords(mtList);
		
		if(ptList.size()>0)
			trns.processTransferRecords(ptList);
		
		if(stList.size()>0)
			trns.processTransferRecords(stList);
		
		if(sotList.size()>0)
			trns.processTransferRecords(sotList);
		
		if (openingStockList.size() > 0) {
			List<Mmtotal> mmTotalTransactions = new ArrayList<>();
			for (Transaction source : openingStockList) {
				Mmtotal target = new Mmtotal();
				BeanUtils.copyProperties(source, target);
				mmTotalTransactions.add(target);
			}
			mts.saveMMTotalTransactions(mmTotalTransactions);
		}
		if (vendorStockList.size() > 0) {
			List<Mmtotal> mmTotalTransactions = new ArrayList<>();
			for (Transaction source : vendorStockList) {
				Mmtotal target = new Mmtotal();
				BeanUtils.copyProperties(source, target);
				mmTotalTransactions.add(target);
			}
			mts.saveMMTotalTransactions(mmTotalTransactions);
		}
		
		
		if (sdtList.size() > 0) {
			for (Transaction source : sdtList) {
				Mmtotal target = new Mmtotal();
				BeanUtils.copyProperties(source, target);
				Mmtotal mmtotal = sdt.generateMMTotal(target);
				System.out.println(saleorderList);
				mtr.save(mmtotal);
			}}
		
		if (igList.size() > 0) {
			for (Transaction source : igList) {
				Mmtotal target = new Mmtotal();
				BeanUtils.copyProperties(source, target);
				Mmtotal mmtotal = ig.generateMMTotal(target);
				System.out.println(saleorderList);
				mtr.save(mmtotal);
			}}
			
			if (ilList.size() > 0) {
				for (Transaction source : ilList) {
					Mmtotal target = new Mmtotal();
					BeanUtils.copyProperties(source, target);
					Mmtotal mmtotal = il.generateMMTotal(target);
					System.out.println(saleorderList);
					mtr.save(mmtotal);
				}}
		if (subconList.size() > 0)
			sc.processSubconRecords(subconList);
		if(cnqr1List.size()>0)
			sc.processSubconRecords(cnqr1List);
		if(dnqr1List.size()>0)
			sc.processSubconRecords(dnqr1List);
		if(cnvr1List.size()>0)
			sc.processSubconRecords(cnvr1List);
		if(dnvr1List.size()>0)
			sc.processSubconRecords(dnvr1List);
		if(ibList.size()>0)
			sc.processSubconRecords(ibList);
		if(srList.size()>0)
			sc.processSubconRecords(srList);
		
		

		if (productionList.size() > 0)
			prod.processProductionRecords(productionList);
		
		if (reworkList.size() > 0)
			rework.processReworkRecords(reworkList);

		if (stocktransportorderList.size() > 0) {
			List<Costtotal> costtotalTransactions = new ArrayList();
			List<Mmtotal> mmTotalTransactions = new ArrayList<>();
			for (Transaction source : stocktransportorderList) {

				Costtotal Ctarget = new Costtotal();
				BeanUtils.copyProperties(source, Ctarget);
				if (Ctarget.getTrnsevent().equals("DE")) {
					Costtotal costtotal = sto.generateCostTotal(Ctarget);
					ctr.save(costtotal);
				}

				else {
					Mmtotal target = new Mmtotal();
					BeanUtils.copyProperties(source, target);
					// mmTotalTransactions.add(target);
					Mmtotal mmtotal = sto.generateMMTotal(target);
					mtr.save(mmtotal);
					Mmtotal mmTotalContra = new Mmtotal();
					BeanUtils.copyProperties(mmtotal, mmTotalContra);
					mmTotalContra.setMmtotalid(null);
					mmTotalContra = sto.generateMMTotalContra(mmTotalContra);
					mtr.save(mmTotalContra);

				}

			}
			// mts.saveMMTotalTransactions(mmTotalTransactions);
		}
		if (stocktransportorderPRList.size() > 0) {
			sto.processSTOPRRecords(stocktransportorderPRList);
			/*
			 * for (Transaction source : stocktransportorderPRList) { Mmtotal target = new
			 * Mmtotal(); BeanUtils.copyProperties(source, target); Mmtotal mmtotalPR =
			 * sto.generateMMTotalPurchaseRecipet(target); mtr.save(mmtotalPR); }
			 */
		}
		
		
		
		if (saleorderList.size() > 0) {
			for (Transaction source : saleorderList) {
				Mmtotal target = new Mmtotal();
				BeanUtils.copyProperties(source, target);
				Mmtotal mmtotal = so.generateMMTotal(target);
				System.out.println(saleorderList);
				mtr.save(mmtotal);
			}
			}
			if (ctriList.size() > 0) {
				for (Transaction source : ctriList) {
					Mmtotal target = new Mmtotal();
					BeanUtils.copyProperties(source, target);
					Mmtotal mmtotal = ctri.generateMMTotal(target);
					System.out.println(saleorderList);
					mtr.save(mmtotal);
				}
			
				
		}
			if (ctrdeList.size() > 0) {
				for (Transaction source : ctrdeList) {
					Costtotal target = new Costtotal();
					BeanUtils.copyProperties(source, target);
					Costtotal costtotal = ctrde.generateCosttotal(target);
					System.out.println(saleorderList);
					ctr.save(costtotal);
				}}

	}

	@Override
	public void createAndSaveOrders(List<Order> orders) {
		List<Orderheader> orderHeaders = new ArrayList<>();
		System.out.println("Before Copy Orders = " + orders.size() + "| OrderHeader = " + orderHeaders.size());
		for (Order source : orders) {
			Orderheader target = new Orderheader();
			BeanUtils.copyProperties(source, target);
			orderHeaders.add(target);
		}
		System.out.println("After Copy Orders = " + orders.size() + "| OrderHeader = " + orderHeaders.size());
		System.out.println(orders.get(0).toString());
		System.out.println(orderHeaders.get(0).toString());
		ohs.saveOrderHeaders(orderHeaders);
	}

}
