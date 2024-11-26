//updated by bhavani
package com.project.revshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.revshop.entity.Category;
import com.project.revshop.entity.Product;
import com.project.revshop.entity.SellerModel;
import com.project.revshop.entity.Size;
import com.project.revshop.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/v1")
public class AddProductController {
	
	@Autowired
	ProductService productService;
	@GetMapping("/sizes")
	@ResponseBody
	public List<Size> getAllSizes(@RequestParam("categoryId") int categoryId) {
	    List<Size> sizes = productService.getAllSizes(categoryId);
	    return sizes;
	}
	@GetMapping("/update")
	public String showUpdateForm(@RequestParam("productId") int productId,Model model) {
		Product product = productService.getProductDetails(productId);
		List<Category> categories = productService.getAllCategories();
		model.addAttribute("categories",categories);
		model.addAttribute("product",product);
		return "updateProductToInventory";
	}
	
	@GetMapping("/addForm")
	public String showProductPage(Model model) {
		model.addAttribute("Product", new Product());
		List<Category> categories = productService.getAllCategories();
		model.addAttribute("categories",categories);
		return "addProductsToInventory";
	}
	
	@PostMapping("/update")
	public String updateProductToInventory(@ModelAttribute Product product, Model model, @RequestParam("productId") int productId, HttpSession session) {
		product.setProductId(productId);
		int sellerid = (Integer) session.getAttribute("sellerid");
		SellerModel sellerModel = productService.getSellerById(sellerid);
		product.setSellerModel(sellerModel);
		productService.saveProduct(product);
		return "redirect:/api/v1/seller/products";
	}
	
	@PostMapping("/add")
	public String addProductToInventory(@ModelAttribute Product product, Model model,@RequestParam("size") int sizeId,HttpSession session ) {
		Size size = productService.getSizeById(sizeId);
		product.setSize(size);
		int sellerid = (Integer) session.getAttribute("sellerid");
		SellerModel sellerModel = productService.getSellerById(sellerid);
        if (sellerModel == null) {
            return "redirect:/api/v1/login";
        }
		product.setSellerModel(sellerModel);
		productService.saveProduct(product);
		return "redirect:/api/v1/seller/products";
	}
	
	@PostMapping("/delete")
	public String deleteProuctFromInventory(@RequestParam("productId") int productId, HttpSession session) {
		int sellerid = (Integer) session.getAttribute("sellerid");
		SellerModel sellerModel = productService.getSellerById(sellerid);
        if (sellerModel == null) {
            return "redirect:/api/v1/login";
        }
		productService.deleteProduct(productId);
		return "redirect:/api/v1/seller/products";
	}
}

