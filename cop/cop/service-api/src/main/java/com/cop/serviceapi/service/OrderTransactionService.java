package com.cop.serviceapi.service;

import java.util.List;

import com.cop.model.database.Order;
import com.cop.model.database.Transaction;

public interface OrderTransactionService {

	void createAndSaveOrderTransactions(List<Transaction> transactions);

	void createAndSaveOrders(List<Order> orders);

}
