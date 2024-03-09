package com.cop.serviceapi.service;

import java.util.List;

import com.cop.model.database.Orderheader;

public interface OrderHeaderService {

	void saveOrderHeaders(List<Orderheader> orderHeaders);

	void saveOrderHeader(Orderheader orderHeader);

}
