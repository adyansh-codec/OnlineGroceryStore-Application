package org.softuni.onlinegrocery.web.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.onlinegrocery.domain.models.binding.CategoryAddBindingModel;
import org.softuni.onlinegrocery.domain.models.service.CategoryNewServiceModel;
import org.softuni.onlinegrocery.domain.models.service.CategoryServiceModel;
import org.softuni.onlinegrocery.domain.models.view.CategoryNewViewModel;
import org.softuni.onlinegrocery.domain.models.view.CategoryViewModel;
import org.softuni.onlinegrocery.service.CategoryNewService;
import org.softuni.onlinegrocery.service.CategoryService;
import org.softuni.onlinegrocery.util.error.CategoryNotFoundException;
import org.softuni.onlinegrocery.web.annotations.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.softuni.onlinegrocery.util.constants.AppConstants.*;

@Controller
@RequestMapping("/categories")
public class CategoryController extends BaseController {

	private final CategoryService categoryService;
	private final CategoryNewService categoryNewService;
	private final ModelMapper modelMapper;

	@Autowired
	public CategoryController(CategoryService categoryService, CategoryNewService categoryNewService,
			ModelMapper modelMapper) {
		this.categoryService = categoryService;
		this.categoryNewService = categoryNewService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/add")
	@PageTitle("Add Category")
	// @PreAuthorize("hasRole('ROLE_MODERATOR')")
	public ModelAndView addCategory(@ModelAttribute(name = MODEL) CategoryAddBindingModel categoryAddBindingModel,
			ModelAndView modelAndView) {

		return loadAndReturnModelAndView(categoryAddBindingModel, modelAndView);
	}

	/*
	 * @PostMapping("/add") // @PreAuthorize("hasRole('ROLE_MODERATOR')") public
	 * ModelAndView addCategoryConfirm(@Valid @ModelAttribute(name = MODEL)
	 * CategoryAddBindingModel model, BindingResult bindingResult, ModelAndView
	 * modelAndView) {
	 * 
	 * CategoryServiceModel categoryServiceModel = modelMapper.map(model,
	 * CategoryServiceModel.class);
	 * 
	 * if (bindingResult.hasErrors() ||
	 * categoryService.addCategory(categoryServiceModel) == null) {
	 * 
	 * return loadAndReturnModelAndView(model, modelAndView); } return
	 * redirect("/categories/all"); }
	 * 
	 * @GetMapping("/all") // @PreAuthorize("hasRole('ROLE_MODERATOR')")
	 * 
	 * @PageTitle("All Categories") public ModelAndView allCategories(ModelAndView
	 * modelAndView) {
	 * 
	 * List<CategoryViewModel> categories =
	 * mapCategoryServiceToViewModel(categoryService.findAllFilteredCategories());
	 * 
	 * modelAndView.addObject(CATEGORIES_TO_LOWER_CASE, categories);
	 * 
	 * return view("category/all-categories", modelAndView); }
	 */
	//@GetMapping("/edit/{id}")
	// @PreAuthorize("hasRole('ROLE_MODERATOR')")
	/*
	 * @PageTitle("Edit Category") public ModelAndView editCategory(@PathVariable
	 * String id, ModelAndView modelAndView) {
	 * 
	 * CategoryViewModel categoryViewModel =
	 * modelMapper.map(categoryService.findCategoryById(id),
	 * CategoryViewModel.class);
	 * 
	 * modelAndView.addObject(MODEL, categoryViewModel);
	 * 
	 * return view("category/edit-category", modelAndView); }
	 */

	@PostMapping("/edit/{id}")
	// @PreAuthorize("hasRole('ROLE_MODERATOR')")
	/*
	 * public ModelAndView editCategoryConfirm(@PathVariable String id,
	 * 
	 * @Valid @ModelAttribute(name = "model") CategoryAddBindingModel model,
	 * BindingResult bindingResult, ModelAndView modelAndView) {
	 * 
	 * CategoryServiceModel categoryServiceModel = modelMapper.map(model,
	 * CategoryServiceModel.class);
	 * 
	 * if (bindingResult.hasErrors() || categoryService.editCategory(id,
	 * categoryServiceModel) == null) {
	 * 
	 * modelAndView.addObject(MODEL, model);
	 * 
	 * return view("category/edit-category", modelAndView); }
	 * 
	 * return redirect("/categories/all"); }
	 */

	/*
	 * @GetMapping("/delete/{id}") // @PreAuthorize("hasRole('ROLE_MODERATOR')")
	 * 
	 * @PageTitle("Delete Category") public ModelAndView
	 * deleteCategory(@PathVariable String id, ModelAndView modelAndView) {
	 * 
	 * CategoryViewModel categoryViewModel =
	 * modelMapper.map(categoryService.findCategoryById(id),
	 * CategoryViewModel.class);
	 * 
	 * modelAndView.addObject(MODEL, categoryViewModel);
	 * 
	 * return view("category/delete-category", modelAndView); }
	 */
	/*
	 * @PostMapping("/delete/{id}") // @PreAuthorize("hasRole('ROLE_MODERATOR')")
	 * public ModelAndView deleteCategoryConfirm(@PathVariable String id) {
	 * 
	 * categoryService.deleteCategory(id);
	 * 
	 * return redirect("/categories/all"); }
	 */

	@GetMapping("/fetch")
	/* @PreAuthorize("hasRole('ROLE_MODERATOR')") */
	@ResponseBody
	public List<CategoryNewViewModel> fetchCategories() {
		List<CategoryNewViewModel> categories = mapCategoryNewServiceToViewModel(
				categoryNewService.findAllOrderByName());

		return categories;
	}

	private List<CategoryViewModel> mapCategoryServiceToViewModel(List<CategoryServiceModel> categoryServiceModels) {
		return categoryServiceModels.stream().map(product -> modelMapper.map(product, CategoryViewModel.class))
				.collect(Collectors.toList());
	}

	private List<CategoryNewViewModel> mapCategoryNewServiceToViewModel(
			List<CategoryNewServiceModel> categoryNewServiceModels) {
		return categoryNewServiceModels.stream().map(category -> modelMapper.map(category, CategoryNewViewModel.class))
				.collect(Collectors.toList());
	}

	private ModelAndView loadAndReturnModelAndView(CategoryAddBindingModel categoryAddBindingModel,
			ModelAndView modelAndView) {

		modelAndView.addObject(MODEL, categoryAddBindingModel);

		return view("category/add-category", modelAndView);
	}

	@ExceptionHandler({ CategoryNotFoundException.class })
	public ModelAndView handleProductNotFound(CategoryNotFoundException e) {
		ModelAndView modelAndView = new ModelAndView(ERROR);
		modelAndView.addObject(MESSAGE, e.getMessage());
		modelAndView.addObject(STATUS_CODE, e.getStatusCode());

		return modelAndView;
	}

	@GetMapping("/all/name")
	// @PreAuthorize("hasRole('ROLE_MODERATOR')")
	@PageTitle("Products")
	public ResponseEntity<List<String>> allCategoryByName(ModelAndView modelAndView) {

		List<String> names = categoryNewService.findAllOrderByName().stream().map(CategoryNewServiceModel::getName)
				.collect(Collectors.toList());

		return ResponseEntity.ok(names);
	}

	@GetMapping("/products/{categoryName}")
	@PageTitle("Category Products")
	public ModelAndView categoryProducts(@PathVariable String categoryName, ModelAndView modelAndView) {
		modelAndView.addObject("categoryName", categoryName);
		return view("category-products", modelAndView);
	}

	@GetMapping("/shopping")
	@PageTitle("Category Shopping")
	public ModelAndView categoryWiseShopping(@RequestParam(required = false) String categoryId,
			@RequestParam(required = false) String subcategoryId, ModelAndView modelAndView) {

		// Set default category name
		String categoryName = "Shop by Category";

		// If categoryId is provided, get the actual category name
		if (categoryId != null && !categoryId.isEmpty()) {
			try {
				// Get category by ID and set the name
				// This will be handled by the frontend API calls
				modelAndView.addObject("categoryId", categoryId);
			} catch (Exception e) {
				// Handle error gracefully
				modelAndView.addObject("error", "Category not found");
			}
		}

		if (subcategoryId != null && !subcategoryId.isEmpty()) {
			modelAndView.addObject("subcategoryId", subcategoryId);
		}

		modelAndView.addObject("categoryName", categoryName);

		return view("categorywiseshopping", modelAndView);
	}
}
