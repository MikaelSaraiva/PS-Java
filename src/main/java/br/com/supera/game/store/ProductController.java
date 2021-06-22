package br.com.supera.game.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

	@GetMapping("/status")
	public String status() {
		return "OK";
	}

	@GetMapping("/add")
	public ArrayList<Product> addToCart(@RequestParam(value = "productId", defaultValue = "") String productId) {
		Connect connection = new Connect();
		ArrayList<Product> product = new ArrayList<Product>();
		product = connection.selectProducts(productId);

		if (!product.isEmpty()) {
			connection.insertProduct("cart", product.get(0));
		}

		return connection.selectAll("cart");
	}

	@GetMapping("/remove")
	public ArrayList<Product> removeToCart(@RequestParam(value = "productId", defaultValue = "") String productId,
			@RequestParam(value = "all", defaultValue = "false") Boolean all) {
		Connect connection = new Connect();
		ArrayList<Product> cart = new ArrayList<Product>();
		cart = connection.selectAll("cart");

		if (all) {
			for (Product product : cart) {
				connection.deleteProduct("cart", String.valueOf(product.id));
			}
			return connection.selectAll("cart");
		}

		if (!cart.isEmpty()) {
			connection.deleteProduct("cart", productId);
		}

		return connection.selectAll("cart");
	}

	interface Comparable {
		public boolean compare(Product productA, Product productB);
	}

	public class PriceGreaterThan implements Comparable {
		public boolean compare(Product productA, Product productB) {
			int result = productA.price.compareTo(productB.price);
			return (result == 1);
		}
	}

	public class ScoreGreaterThan implements Comparable {
		public boolean compare(Product productA, Product productB) {
			return (productA.score > productB.score);
		}
	}

	public class SortZA implements Comparable {
		public boolean compare(Product productA, Product productB) {
			int result = productA.name.compareTo(productB.name);
			return (result > 0);
		}
	}

	public static ArrayList<Product> mergeSort(ArrayList<Product> productsList, int size, Comparable sorter) {
		if (size < 2) {
			return productsList;
		}
		int mid = size / 2;
		ArrayList<Product> productsA = new ArrayList<Product>();
		ArrayList<Product> productsB = new ArrayList<Product>();

		for (int i = 0; i < mid; i++) {
			productsA.add(productsList.get(i));
		}
		for (int i = mid; i < size; i++) {
			productsB.add(productsList.get(i));
		}
		ArrayList<Product> sortedA = mergeSort(productsA, mid, sorter);
		ArrayList<Product> sortedB = mergeSort(productsB, size - mid, sorter);

		return merge(sortedA, sortedB, mid, size - mid, sorter);
	}

	public static ArrayList<Product> merge(ArrayList<Product> productsA, ArrayList<Product> productsB, int left,
			int right, Comparable sorter) {

		ArrayList<Product> result = new ArrayList<Product>();

		int i = 0, j = 0;
		while (i < left && j < right) {
			if (sorter.compare(productsB.get(j), productsA.get(i))) {
				result.add(productsA.get(i));
				i++;
			} else {
				result.add(productsB.get(j));
				j++;
			}
		}
		while (i < left) {
			result.add(productsA.get(i));
			i++;
		}
		while (j < right) {
			result.add(productsB.get(j));
			j++;
		}

		return result;
	}

	@GetMapping("/sort")
	public List<Product> sort(@RequestParam(value = "sortMethod", defaultValue = "name") String sortMethod,
			@RequestParam(value = "order", defaultValue = "crescent") String order,
			@RequestParam(value = "productIds", defaultValue = "") String productIds) {

		ArrayList<Product> products;

		if (productIds.length() == 0) {
			products = new Connect().selectAll("products");
		} else {
			products = new Connect().selectProducts(productIds);
		}

		Comparable sorter;

		switch (sortMethod) {

			case ("price"):
				sorter = new PriceGreaterThan();
				break;

			case ("score"):
				sorter = new ScoreGreaterThan();
				break;

			default:
				sorter = new SortZA();

		}

		List<Product> result = mergeSort(products, products.size(), sorter);

		if (order.equals("decrescent")) {
			Collections.reverse(result);
		}

		return result;
	}
}
