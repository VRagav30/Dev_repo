package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Mmtotal;
import com.cop.model.database.Numberrange;
import com.cop.model.database.Transaction;
import com.cop.repository.transaction.BatchRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OrderHeaderRepository;

@Component
public class Transfer {

	@Autowired
	OrderHeaderRepository Ohr;
	@Autowired
	BatchRepository batr;
	@Autowired
	NumberRangeRepository nrr;
	@Autowired
	BatchTransfer bt;
	@Autowired
	PlantToPlantTransfer pt;
	@Autowired
	SaleOrderTransfer sot;
	@Autowired
	StocktypeTransfer st;
	@Autowired
	MaterialTransfer matt;
	@Autowired
	MMTotalRespository mtr;
	private Numberrange nr;

	public void processTransferRecords(List<Transaction> transferRecords) {

		Map<String, List<Transaction>> map = splitByTrnsevent(transferRecords);
		Iterator<String> itr = map.keySet().iterator();

		List<Mmtotal> btMTTransactions = new ArrayList<>();
		List<Mmtotal> ptMTTransaction = new ArrayList<>();
		List<Mmtotal> sotMTTransaction = new ArrayList<>();
		List<Mmtotal> stMTTransaction = new ArrayList<>();
		List<Mmtotal> mtMTTransaction = new ArrayList<>();
		for (String str : map.keySet()) {
			switch (str) {
			case "BT":
				for (Transaction source : map.get("BT")) {
					Long countS = batr.findAllbyBatch(source.getSbatch(), source.getSmaterial());
					Long countT = batr.findAllbyBatch(source.getTbatch(), source.getSmaterial());
					if (countS > 0 && countT > 0) {
						Mmtotal mt = new Mmtotal();
						Mmtotal mtc = new Mmtotal();
						BeanUtils.copyProperties(source, mt);
						Mmtotal mtMMTotal = bt.generateMMTotal(mt);
						mtMTTransaction.add(mtMMTotal);

						BeanUtils.copyProperties(mt, mtc);
						Mmtotal mtContra = bt.generateMMTotalContra(mtc);
						mtMTTransaction.add(mtContra);
					} else {
						System.out.println("Please enter a valid batchnumber");
					}
				}
				break;

			case "MT":
				for (Transaction source : map.get("MT")) {

					Mmtotal mt = new Mmtotal();
					Mmtotal mtc = new Mmtotal();
					BeanUtils.copyProperties(source, mt);
					Mmtotal mtMMTotal = matt.generateMMTotal(mt);
					mtMTTransaction.add(mtMMTotal);

					BeanUtils.copyProperties(mt, mtc);
					Mmtotal mtContra = matt.generateMMTotalContra(mtc);
					mtMTTransaction.add(mtContra);
					System.out.println("adding to list " + mtContra.getTrnsevent() + " " + mtContra.getMmtotalid());
					System.out.println("find me" + mtMTTransaction.toString());
				}
				break;

			case "PT":
				for (Transaction source : map.get("PT")) {

					Mmtotal mt = new Mmtotal();
					Mmtotal mtc = new Mmtotal();
					BeanUtils.copyProperties(source, mt);
					Mmtotal mtMMTotal = pt.generateMMTotal(mt);
					mtMTTransaction.add(mtMMTotal);

					BeanUtils.copyProperties(mt, mtc);
					Mmtotal mtContra = pt.generateMMTotalContra(mtc);
					mtMTTransaction.add(mtContra);
				}
				break;
			case "ST":
				for (Transaction source : map.get("ST")) {

					/*
					 * Long count= Ohr.findAllbySoassignment(source.getSaleorder()); if(count>0 &&
					 * source.getStocktype().equalsIgnoreCase("S")) {
					 */
					Mmtotal mt = new Mmtotal();
					Mmtotal mtc = new Mmtotal();
					BeanUtils.copyProperties(source, mt);
					Mmtotal mtMMTotal = st.generateMMTotal(mt);
					mtMTTransaction.add(mtMMTotal);

					BeanUtils.copyProperties(mt, mtc);
					Mmtotal mtContra = st.generateMMTotalContra(mtc);
					mtMTTransaction.add(mtContra);
					/*
					 * } else { System.out.
					 * println("Please Create Order for of type Saleorder in Orderheader with soassignment value"
					 * ); }
					 */
				}
				break;
			case "SOT":
				for (Transaction source : map.get("SOT")) {
					Long countS = Ohr.findAllbySoassignment(source.getSso(), "SALE");
					Long countT = Ohr.findAllbySoassignment(source.getTso(), "SALE");
					if ((countS > 0 && countT > 0 && source.getSstype().equalsIgnoreCase("S"))) {
						Mmtotal mt = new Mmtotal();
						Mmtotal mtc = new Mmtotal();
						BeanUtils.copyProperties(source, mt);
						Mmtotal mtMMTotal = sot.generateMMTotal(mt);
						mtMTTransaction.add(mtMMTotal);

						BeanUtils.copyProperties(mt, mtc);
						Mmtotal mtContra = sot.generateMMTotalContra(mtc);
						mtMTTransaction.add(mtContra);
					} else {
						System.out.println("Please check the ordernumber entered!!");
					}
				}
				break;
			default:
				System.out.println("Incorrect transfer Event");
			}

			List<Numberrange> nrList = nrr.findAllByNumobject("MMTOTALDOC");
			nr = nrList.get(0);
			BigDecimal currentNum = (nr.getCurrentnumber() == null ? nr.getRangefrom()
					: new BigDecimal(nr.getCurrentnumber().intValue() + 1));
			int objItNum = 10;
			new BigDecimal(0);
			System.out.println("Size : " + mtMTTransaction.size());
			for (Mmtotal mt : mtMTTransaction) {

				mt.setDocnumber(currentNum);
				mt.setDocitnum(new BigDecimal(objItNum));
				objItNum += 10;
				System.out.println("Transfer Event " + mt.getTrnsevent());
				System.out.println("ID :  " + mt.getMmtotalid());
				mtr.save(mt);
			}

			nr.setCurrentnumber(currentNum);
			nrr.save(nr);
		}
	}

	private Map<String, List<Transaction>> splitByTrnsevent(List<Transaction> transferRecords) {
		Map<String, List<Transaction>> map = new HashMap<>();
		Iterator<Transaction> itr = transferRecords.iterator();
		while (itr.hasNext()) {
			Transaction transaction = itr.next();
			if (map.containsKey(transaction.getTrnsevent())) {

				List<Transaction> tempList = map.get(transaction.getTrnsevent());
				tempList.add(transaction);
				map.put(transaction.getTrnsevent(), tempList);
			} else {
				List<Transaction> tempList = new ArrayList<>();
				tempList.add(transaction);
				map.put(transaction.getTrnsevent(), tempList);
			}
		}
		return map;
	}

}
