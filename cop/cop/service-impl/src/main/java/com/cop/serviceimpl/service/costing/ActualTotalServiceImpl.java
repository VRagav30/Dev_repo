package com.cop.serviceimpl.service.costing;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Actualtotal;
import com.cop.model.database.CostingUpload;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.repository.transaction.ActualTotalRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.serviceapi.service.costing.ActualTotalService;
@Component
public class ActualTotalServiceImpl implements ActualTotalService {
@Autowired MMTotalRespository mtr;
@Autowired MaterialRepository mr;
@Autowired ActualTotalRepository atr;
	
	private Actualtotal at;
	private Material mat;
	@Override
	public void saveActualTotalTransaction(List<Actualtotal> actaulTotalTransactions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Actualtotal intializeActualTotal(BigDecimal docNum, BigDecimal docItNum) {
		// TODO Auto-generated method stub
		try
		{
			this.at=new Actualtotal();
			Mmtotal mt= mtr.getMmtotalbydocnum(docNum, docItNum).get(0);
			BeanUtils.copyProperties(mt, at);
			List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), mt.getPlant(), mt.getValsub());
			mat = matList.get(0);
			at.setBuqty(mt.getBuomqty());
			if(mat.getBatchcosting().equalsIgnoreCase("NO"))
			{
				at.setBatchnumber(null);
			}
			System.out.println(at.getBuqty());
			if(mt.getStocktype().equalsIgnoreCase("SV")|| mt.getStocktype().equalsIgnoreCase("OV"))
			{
				at.setVendor(mt.getVendor());
						}
			else
				at.setVendor(null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return at;
	}

	@Override
	public void performActualCosting(List<CostingUpload> cuList) {
		// TODO Auto-generated method stub
	
		for (CostingUpload cu : cuList) {
			Actualtotal act = this.intializeActualTotal(cu.getDocnum(), cu.getDocitnum());
			BeanUtils.copyProperties(cu, act);
			act.setCounter(cu.getDocnum().toString().concat(cu.getDocitnum().toString()));
			
			
			
			atr.save(act);
			
			
		}
		
		
		
	}
	
	

}
