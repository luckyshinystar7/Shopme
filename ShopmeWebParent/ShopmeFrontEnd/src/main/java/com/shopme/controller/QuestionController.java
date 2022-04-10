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

import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.question.Question;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.service.ProductService;
import com.shopme.service.QuestionService;

@Controller
public class QuestionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);
	
	private QuestionService questionService;
	
	private ProductService productService;
	
	@Autowired
	public QuestionController(QuestionService questionService, ProductService productService) {
		super();
		this.questionService = questionService;
		this.productService = productService;
	}

	@GetMapping("/questions/{productAlias}") 
	public String listQuestionsOfProduct(@PathVariable(name = "productAlias") String productAlias,
			Model model, HttpServletRequest request) throws ProductNotFoundException {
		
		LOGGER.info("QuestionController | listQuestionsOfProduct is called");
		
		return listQuestionsOfProductByPage(model, request, productAlias, 1, "votes", "desc");
	}
	
	@GetMapping("/questions/{productAlias}/page/{pageNum}") 
	public String listQuestionsOfProductByPage(
				Model model, HttpServletRequest request,
				@PathVariable(name = "productAlias") String productAlias,
				@PathVariable(name = "pageNum") int pageNum,
				String sortField, String sortDir) throws ProductNotFoundException {
		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage is called");
		
		Page<Question> page = questionService.listQuestionsOfProduct(productAlias, pageNum, sortField, sortDir);
		List<Question> listQuestions = page.getContent();
		Product product = productService.getProduct(productAlias);

		LOGGER.info("QuestionController | listQuestionsOfProductByPage | totalPages : " + page.getTotalPages());
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | totalItems : " + page.getTotalElements());
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | currentPage : " + pageNum);
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | sortField : " + sortField);
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | sortDir : " + sortDir);
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | reverseSortDir : " + (sortDir.equals("asc") ? "desc" : "asc"));
					
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | listQuestions size : " + listQuestions.size());
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | product name : " + product.getName());
		
		model.addAttribute("listQuestions", listQuestions);
		model.addAttribute("product", product);

		long startCount = (pageNum - 1) * QuestionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING + 1;
		model.addAttribute("startCount", startCount);
		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | startCount : " + startCount);

		long endCount = startCount + QuestionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING - 1;
		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | endCount : " + endCount);
		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | endCount : " + endCount);
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | page.getTotalElements() : " + page.getTotalElements());
		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | endCount > page.getTotalElements() : "
		+ (endCount > page.getTotalElements()));
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		model.addAttribute("endCount", endCount);
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | endCount : " + endCount);
		
		LOGGER.info("QuestionController | listQuestionsOfProductByPage | page.getTotalPages() : " + page.getTotalPages());

		if (page.getTotalPages() > 1) {
			LOGGER.info("QuestionController | listQuestionsOfProductByPage | pageTitle : " + "Page " + pageNum + " | Questions for product: " + product.getName());
			model.addAttribute("pageTitle", "Page " + pageNum + " | Questions for product: " + product.getName());
		} else {
			LOGGER.info("QuestionController | listQuestionsOfProductByPage | pageTitle : " + "Questions for product: " + product.getName());
			model.addAttribute("pageTitle", "Questions for product: " + product.getName());
		}		

		return "product/product_questions";
	}
}
