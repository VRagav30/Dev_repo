package com.cop.serviceapi.service.costing;

import java.math.BigDecimal;
import java.util.List;

import com.cop.model.database.Actualtotal;
import com.cop.model.database.CostingUpload;
public interface ActualTotalService {
	
	void saveActualTotalTransaction(List<Actualtotal> actaulTotalTransactions);
	
	Actualtotal intializeActualTotal(BigDecimal docNum, BigDecimal docItNum);
	
	void performActualCosting(List<CostingUpload> cuList);

}
