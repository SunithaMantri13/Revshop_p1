package com.project.revshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.revshop.controller.ViewProductsController;
import com.project.revshop.entity.Category;
import com.project.revshop.entity.Product;
import com.project.revshop.entity.Review;
import com.project.revshop.entity.SellerModel;
import com.project.revshop.entity.Size;
import com.project.revshop.enums.Gender;
import com.project.revshop.repository.CategoryRepository;
import com.project.revshop.repository.ProductRepository;
import com.project.revshop.repository.SellerRepository;
import com.project.revshop.repository.SizeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SizeRepository sizeRepository;

	@Autowired
	private SellerRepository sellerRepository;

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public void saveProduct(Product product) {
		productRepository.save(product);
	}

	public Optional<Category> getCategoryById(int categoryId) {
		return categoryRepository.findById(categoryId);
	}

	public List<Size> getAllSizes(int categoryId) {
		Optional<Category> category = categoryRepository.findById(categoryId);
		return sizeRepository.findByCategory(category);
	}

	public Size getSizeById(int sizeId) {
		return sizeRepository.getReferenceById(sizeId);

	}

	public Product getProductDetails(int productId) {
		return productRepository.getReferenceById(productId);
	}

	public void deleteProduct(int productId) {
		productRepository.deleteById(productId);
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product getProductById(int productId) {
		return productRepository.findById(productId).get();
	}

	public Product findById(int id) {
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent()) {
			return product.get();
		} else {
			return null;
		}
	}

	public double getRating(int id) {
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent()) {
			List<Review> reviews = product.get().getReviews();
			int total = 0;
			int count = 0;
			for (Review review : reviews) {
				total += review.getRating();
				count++;
			}
			return total / count;
		} else {
			return 0;
		}
	}

	public SellerModel getSellerById(int sellerid) {
		return sellerRepository.findById(sellerid).get();
	}

	public List<Product> getProductsByGenderInAndPriceBetween(List<Gender> genders, double minPrice, double maxPrice) {
		return productRepository.findByGenderInAndPriceBetween(genders, minPrice, maxPrice);
	}

	public List<Product> getProductsByPriceBetween(double minPrice, double maxPrice) {
		return productRepository.findByPriceBetween(minPrice, maxPrice);
	}

	public List<Product> getProductsByCategoryId(List<Integer> categoryId) {
		return productRepository.findAllByCategory_CategoryIdIn(categoryId);
	}

	public List<Product> getProductsByGenderInAndCategoryIn(List<Gender> genders, List<Integer> categoryId) {
		return productRepository.findByGenderInAndCategory_CategoryIdIn(genders, categoryId);
	}

	public List<Product> getProductsByCategoryIdSizeIdAndPriceBetween(List<Integer> categoryId, List<Integer> sizeId,
			double minPrice, double maxPrice) {
		return productRepository.findByCategory_CategoryIdInAndSize_SizeIdInAndPriceBetween(categoryId, sizeId,
				minPrice, maxPrice);
	}

	public List<Size> getSizesByCategory(List<Integer> categoryIds) {
		return sizeRepository.findByCategory_CategoryIdIn(categoryIds);
	}

	public List<Product> getProductsByGenderPriceCategoryIdSizeId(List<Gender> genders, List<Integer> categoryId,
			List<Integer> sizeId, double minPrice, double maxPrice) {
		return productRepository.findByGenderInAndCategory_CategoryIdInAndSize_SizeIdInAndPriceBetween(genders,
				categoryId, sizeId, minPrice, maxPrice);
	}

	public List<Product> getProductsByGenderCategoryIdSizeId(List<Gender> selectedGenders, List<Integer> categoryId,
			List<Integer> sizeId) {
		return productRepository.findByGenderInAndCategory_CategoryIdInAndSize_SizeIdIn(selectedGenders, categoryId,
				sizeId);
	}

	public Product getAllProductsById(Integer orderId) {
		return productRepository.findById(orderId).get();
	}

	public List<Product> getProductsBySeller(int sellerId) {
		return productRepository.findBySellerModelSellerId(sellerId);
	}
}
