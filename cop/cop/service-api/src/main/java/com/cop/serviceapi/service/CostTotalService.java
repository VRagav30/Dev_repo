package com.cop.serviceapi.service;

import java.util.List;
import java.util.Map;

import com.cop.model.database.Costtotal;

public interface CostTotalService {

	List<String> createAndSaveCostTotal(List<Map<String, Object>> records);

	List<Costtotal> saveCostTotalTransactions(List<Costtotal> costTotalTransactions);

	Costtotal saveCostTotalTransaction(Costtotal costTotalTransaction);

	Costtotal initializeCostTotal(Costtotal costTotal);

}
