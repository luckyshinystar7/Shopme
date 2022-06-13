package com.shopme.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.service.ArticleService;
import com.shopme.admin.service.MenuService;
import com.shopme.common.entity.article.Article;
import com.shopme.common.entity.menu.Menu;

@Controller
public class MenuController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);
	
	private final String defaultRedirectURL = "redirect:/menus";
	
	private MenuService menuService;
	
	private ArticleService articleService;
	
	@Autowired
	public MenuController(MenuService menuService, ArticleService articleService) {
		super();
		this.menuService = menuService;
		this.articleService = articleService;
	}

	@GetMapping("/menus")
	public String listAll(Model model) {
		
		LOGGER.info("MenuController | listAll is called");
		List<Menu> listMenuItems = menuService.listAll();
		
		LOGGER.info("MenuController | listAll | listMenuItems size :" + listMenuItems.size());
		model.addAttribute("listMenuItems", listMenuItems);

		return "menus/menu_items";
	}
	
	@GetMapping("menus/new")
	public String newMenu(Model model) {
		
		LOGGER.info("MenuController | newMenu is called");
		
		List<Article> listArticles = articleService.listArticlesForMenu();
		
		LOGGER.info("MenuController | newMenu | listArticles : " + listArticles.toString());

		model.addAttribute("menu", new Menu());
		model.addAttribute("listArticles", listArticles);
		model.addAttribute("pageTitle", "Create New Menu Item");
		
		LOGGER.info("MenuController | newMenu | pageTitle : " + "Create New Menu Item");

		return "menus/menu_form";
	}

	@PostMapping("/menus/save")
	public String saveMenu(Menu menu, RedirectAttributes ra) {
		
		LOGGER.info("MenuController | saveMenu is called");
		
		menuService.save(menu);
		
		ra.addFlashAttribute("messageSuccess", "The menu item has been saved successfully.");
		
		LOGGER.info("MenuController | saveMenu | messageSuccess : " + "The menu item has been saved successfully.");

		return defaultRedirectURL;
	}
}
