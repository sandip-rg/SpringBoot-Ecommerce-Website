package com.ecommerce.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;

@Controller
public class AdminController {
	
	public static String uploadDir = System.getProperty("user.dir")+"/src/main/resources/static/productImages";

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@GetMapping("/admin")
	public String adminHome() {
		return "adminHome";
	}

	@GetMapping("/admin/categories")
	public String getCategories(Model model) {
		List<Category> allCategories = this.categoryService.getAllCategories();
		model.addAttribute("categories", allCategories);
		return "categories";
	}

	@GetMapping("/admin/categories/add")
	public String getCategoriesAdd(Model model) {
		model.addAttribute("category", new Category());
		return "categoriesAdd";
	}

	@PostMapping("/admin/categories/add")
	public String postCategoriesAdd(@ModelAttribute("category") Category category) {
		this.categoryService.addCategory(category);
		return "redirect:/admin/categories";
	}

	@GetMapping("/admin/categories/delete/{id}")
	public String deleteCategory(@PathVariable("id") int id) {
		this.categoryService.removeCategoryById(id);
		return "redirect:/admin/categories";
	}

	@GetMapping("/admin/categories/update/{id}")
	public String updateCategory(@PathVariable("id") int id, Model model) {
		Optional<Category> category = this.categoryService.getCategoryById(id);
		if (category.isPresent()) {
			model.addAttribute("category", category.get());
			return "categoriesAdd";
		} else {
			return "404";
		}
	}

	// Product Section

	@GetMapping("/admin/products")
	public String getAllProducts(Model model) {
		List<Product> allProducts = this.productService.getAllProducts();
		model.addAttribute("products", allProducts);
		return "products";
	}

	@GetMapping("/admin/products/add")
	public String addProduct(Model model) {
		model.addAttribute("productDTO", new ProductDto());
		List<Category> allCategories = this.categoryService.getAllCategories();
		model.addAttribute("categories", allCategories);
		return "productsAdd";
	}

	@PostMapping("/admin/products/add")
	public String addProductPost(@ModelAttribute("productDTO") ProductDto productDto,
			@RequestParam("productImage") MultipartFile file, @RequestParam("imgName") String imgName)
			throws IOException {
		
		Product product = new Product();
		product.setId(productDto.getId());
		product.setName(productDto.getName());
		Category category = this.categoryService.getCategoryById(productDto.getCategoryId()).get();
		product.setCategory(category);
		product.setPrice(productDto.getPrice());
		product.setWeight(productDto.getWeight());
		product.setDescription(productDto.getDescription());
		
		String imageUUID;
		if(!file.isEmpty()) {
			imageUUID = file.getOriginalFilename();
			Path fileNameAndPath = Paths.get(uploadDir,imageUUID);
			Files.write(fileNameAndPath, file.getBytes());
		}else {
			imageUUID = imgName;
		}
		product.setImageName(imageUUID);
		
		this.productService.addProduct(product);

		return "redirect:/admin/products";
	}
	
	@GetMapping("/admin/product/delete/{id}")
	public String deleteProduct(@PathVariable("id") long id) {
		this.productService.removeProduct(id);
		return "redirect:/admin/products";
	}
	
	@GetMapping("/admin/product/update/{id}")
	public String updateProduct(@PathVariable("id") long id, Model model) {
		Product product = this.productService.getProductById(id).get();
		ProductDto productDto = new ProductDto();
		productDto.setId(product.getId());
		productDto.setName(product.getName());
		productDto.setCategoryId(product.getCategory().getId());
		productDto.setPrice(product.getPrice());
		productDto.setWeight(product.getWeight());
		productDto.setDescription(product.getDescription());
		productDto.setImageName(product.getImageName());
		
		List<Category> allCategories = this.categoryService.getAllCategories();
		model.addAttribute("categories", allCategories);
		
		model.addAttribute("productDTO", productDto);
		
		return "productsAdd";
	}
}
