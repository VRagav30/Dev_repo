
package com.cop.serviceimpl.service.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Caval;
import com.cop.model.database.Costtotal;
import com.cop.model.database.Oprn;
import com.cop.model.database.Oprnprice;
import com.cop.model.database.Orderheader;
import com.cop.model.database.Prcsstep;
import com.cop.repository.transaction.CostTotalRepository;
import com.cop.repository.transaction.OprnRepository;
import com.cop.repository.transaction.OprnpriceRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.PrcsstepRepository;
import com.cop.repository.transaction.PrcsstepopsRepository;
import com.cop.serviceapi.service.CostTotalService;

@Component
public class ReworkOperationConfirmation {

	@Autowired
	CostTotalRepository ctr;
	@Autowired
	OprnpriceRepository opr;
	@Autowired
	CostTotalService cs;
	@Autowired
	OprnRepository or;
	@Autowired
	PrcsstepRepository psr;
	@Autowired
	PrcsstepopsRepository psor;
	@Autowired
	OrderHeaderRepository Ohr;

	private Orderheader oh;

	public Costtotal generateCostTotal(Costtotal ct) {
		ct = cs.initializeCostTotal(ct);
		// BUOM
		// BUOMQTY

		// DOCUOM
		// List<Prcsstepops> opsList=psor.findByOpname(ct.getItemcode());
		List<Oprn> oprnList = or.findAllByOprn(ct.getItemcode());
		ct.setDocuom(oprnList.get(0).getOpuom());
		ct.setBuom(ct.getDocuom());
		ct.setBuomqty(ct.getDocqty());

		List<Orderheader> ohList = Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
		oh = ohList.get(0);
		// PRTNRCODE
		//List<Prcsstep> pList = psr.findAllbyprcsnum(oh.getPrscnum());
		//ct.setPrtnrcode(pList.get(0).getPrcsctr());
		// ORDAMOUNT
		List<Oprnprice> oplist = opr.findAllByorgncodecctrandoprn(ct.getOrgncode(), ct.getPrtnrcode(),
				ct.getItemcode(),"P");
		ct.setOrdamount(ct.getDocqty().multiply(oplist.get(0).getTotprice()).divide(oplist.get(0).getPriceuom()));
		// AMOUNT IN CURRO
		ct.setAmountincurro(ct.getOrdamount());
		// COSTACC
		ct.setCostacc(oprnList.get(0).getCostacc());

		return ct;

	}

	public Costtotal generateCostTotalContra(Costtotal ct) {
		// ct.setCostid(null);
		ct.setObjcode(ct.getPrtnrcode());
		ct.setTrnsevent("OC - Contra");
		ct.setCobjtype("CTR");
		String partnerCode = ct.getObjcode() + "" + ct.getObjitnum();
		//List<Prcsstep> pList = psr.findAllbyprcsnum(oh.getPrscnum());
		
		ct.setPrtnrcode(partnerCode);
		ct.setDocqty(ct.getDocqty().negate());
		ct.setBuomqty(ct.getBuomqty().negate());
		ct.setOrdamount(ct.getOrdamount().negate());
		ct.setAmountincurro(ct.getAmountincurro().negate());
		return ct;
	}

}
