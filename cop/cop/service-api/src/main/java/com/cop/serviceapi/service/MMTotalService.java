package com.cop.serviceapi.service;

import java.util.List;

import com.cop.model.database.Costtotal;
import com.cop.model.database.Mmtotal;

public interface MMTotalService {

	void saveMMTotalTransactionsFromCostTotal(List<Costtotal> costTotalTransactions);

	void saveMMTotalTransactions(List<Mmtotal> mmTotalTransactions);

	Mmtotal intializeMMTotal(Mmtotal mmTotal);

}
