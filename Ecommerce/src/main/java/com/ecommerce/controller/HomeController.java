package com.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.global.GlobalData;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;

@Controller
public class HomeController {
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping({"/","/home"})
	public String home(Model model) {
		model.addAttribute("cartCount", GlobalData.cart.size());
		return "index";
	}
	
	@GetMapping("/shop")
	public String shop(Model model) {
		List<Category> allCategories = this.categoryService.getAllCategories();
		model.addAttribute("categories", allCategories);
		
		List<Product> allProducts = this.productService.getAllProducts();
		model.addAttribute("products", allProducts);
		
		model.addAttribute("cartCount", GlobalData.cart.size());
		return "shop";
	}
	
	@GetMapping("/shop/category/{id}")
	public String shopByCategory(@PathVariable("id") int id,Model model) {
		List<Category> allCategories = this.categoryService.getAllCategories();
		model.addAttribute("categories", allCategories);
		
		List<Product> allProducts = this.productService.getAllProductsByCategory(id);
		model.addAttribute("products", allProducts);
		model.addAttribute("cartCount", GlobalData.cart.size());
		return "shop";
	}
	
	@GetMapping("/shop/viewproduct/{id}")
	public String viewProduct(@PathVariable("id") int id,Model model) {
		Product product = this.productService.getProductById(id).get();
		model.addAttribute("product", product);
		model.addAttribute("cartCount", GlobalData.cart.size());
		return "viewProduct";
	}
}
