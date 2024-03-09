package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Costtotal;
import com.cop.repository.transaction.CostTotalRepository;
import com.cop.repository.transaction.FydPeriodRepository;
import com.cop.repository.transaction.OprnRepository;
import com.cop.repository.transaction.OprnpriceRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.serviceapi.service.CostTotalService;
@Component
public class ProdOperationRevaluation {
	
	@Autowired
	CostTotalService cs;
	@Autowired
	OrderHeaderRepository ohr;
	
	@Autowired
	CostTotalRepository ctr;
	
	@Autowired
	FydPeriodRepository fpr;
	@Autowired
	OprnpriceRepository opr;
	public List<Costtotal> generateCostTotal(Costtotal ct) {
			
		List<Costtotal> oclist= ctr.findbyObjcode(ct.getObjcode(),ct.getObjitnum(),"OC");
		List<Costtotal> orList=new ArrayList<>();
		
		Date today = new Date(); 
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int month = cal.get(Calendar.MONTH); 
		int year = cal.get(Calendar.YEAR);
		
		for(Costtotal oc:oclist)
		{	Costtotal OR=new Costtotal();
			BeanUtils.copyProperties(oc, OR);
			BigDecimal ocOrdamount=oc.getOrdamount();
			OR.setCosttotalid(null);
			OR.setTrnsevent("OR");
			OR.setDate(fpr.findbyPeriod(OR.getPeriod()).get(0).getTodate());
			OR.setDocdesc(month+"/"+year);
			OR.setRefdocnum(new BigDecimal(oc.getDocnumber().toString()+""+oc.getDocitnum().toString()));
			BigDecimal totprice=opr.findAllByorgncodecctrandoprn(oc.getOrgncode(), oc.getPrtnrcode(), oc.getItemcode(), "A").get(0).getTotprice();
			BigDecimal priceuom=opr.findAllByorgncodecctrandoprn(oc.getOrgncode(), oc.getPrtnrcode(), oc.getItemcode(), "A").get(0).getPriceuom();
			//BigDecimal Ocost=ohr.findAllByOrdernum(oc.getObjcode(), oc.getObjitnum()).get(0).getTotcost();
			BigDecimal ratio=totprice.divide(priceuom);			
			OR.setOrdamount((OR.getBuomqty().multiply(totprice)).divide(priceuom).subtract(ocOrdamount));
			OR.setAmountincurro(OR.getOrdamount());
			OR.setUsercode("SYSTEM");
			OR.setCreatedtime(new Timestamp(new Date().getTime()));
			OR.setPlcomp(null);
			orList.add(OR);
			
			}
		System.out.println("gen Insert list size is "+orList.size());
		return orList;
	}
	
	public Costtotal generateCostTotalContra(Costtotal ct) {
		
		Costtotal orc=new Costtotal();
		BeanUtils.copyProperties(ct, orc);
		orc.setCosttotalid(null);
		orc.setObjcode(ct.getPrtnrcode());
		orc.setTrnsevent("OR-CONTRA");
		orc.setOrdamount(ct.getOrdamount().negate());
		orc.setAmountincurro(orc.getOrdamount());
		orc.setPrtnrcode(ct.getObjcode());
		orc.setPlcomp(null);
		return orc;
	}
	

}
