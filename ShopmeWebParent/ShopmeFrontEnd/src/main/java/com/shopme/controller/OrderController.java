package com.shopme.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.service.CustomerService;
import com.shopme.service.OrderService;
import com.shopme.util.CustomerShoppingCartAddressShippingOrderUtil;

@Controller
public class OrderController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	
	private OrderService orderService;
	
	private CustomerService customerService;

	@Autowired
	public OrderController(OrderService orderService, CustomerService customerService) {
		super();
		this.orderService = orderService;
		this.customerService = customerService;
	}
	
	
	@GetMapping("/orders")
	public String listFirstPage(Model model, HttpServletRequest request) throws CustomerNotFoundException {
		
		LOGGER.info("OrderController | listFirstPage is called");
		
		return listOrdersByPage(model, request, 1, "orderTime", "desc", null);
	}

	@GetMapping("/orders/page/{pageNum}")
	public String listOrdersByPage(Model model, HttpServletRequest request,
						@PathVariable(name = "pageNum") int pageNum,
						String sortField, String sortDir, String orderKeyword
			) throws CustomerNotFoundException {
		
		LOGGER.info("OrderController | listOrdersByPage is called");
		
		Customer customer = CustomerShoppingCartAddressShippingOrderUtil.getAuthenticatedCustomer(request,customerService);
		
		LOGGER.info("OrderController | listOrdersByPage | customer : " + customer.toString());

		Page<Order> page = orderService.listForCustomerByPage(customer, pageNum, sortField, sortDir, orderKeyword);
		
		LOGGER.info("OrderController | listOrdersByPage | page : " + page.toString());
		
		List<Order> listOrders = page.getContent();
		

		LOGGER.info("OrderController | listOrdersByPage | totalPages : " + page.getTotalPages());
		LOGGER.info("OrderController | listOrdersByPage | totalItems : " + page.getTotalElements());
		LOGGER.info("OrderController | listOrdersByPage | currentPage : " + pageNum);
		LOGGER.info("OrderController | listOrdersByPage | listOrders : " + listOrders.toString());
		LOGGER.info("OrderController | listOrdersByPage | sortField : " + sortField);
		LOGGER.info("OrderController | listOrdersByPage | sortDir : " + sortDir);
		LOGGER.info("OrderController | listOrdersByPage | orderKeyword : " + orderKeyword);
		LOGGER.info("OrderController | listOrdersByPage | reverseSortDir : " + (sortDir.equals("asc") ? "desc" : "asc"));

		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("listOrders", listOrders);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("orderKeyword", orderKeyword);
		model.addAttribute("moduleURL", "/orders");
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		
		long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
		
		model.addAttribute("startCount", startCount);
		
		LOGGER.info("OrderController | listOrdersByPage | startCount : " + startCount);

		long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
		
		LOGGER.info("OrderController | listOrdersByPage | endCount : " + endCount);
		
		
		LOGGER.info("OrderController | listOrdersByPage | endCount : " + endCount);
		LOGGER.info("OrderController | listOrdersByPage | page.getTotalElements() : " + page.getTotalElements());
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		LOGGER.info("OrderController | listOrdersByPage | endCount : " + endCount);

		model.addAttribute("endCount", endCount);

		return "order/orders_customer";		
	}
}
