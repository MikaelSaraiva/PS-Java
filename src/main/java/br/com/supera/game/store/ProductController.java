package br.com.supera.game.store;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<String> addToCart(@RequestParam(value = "productId", defaultValue = "") String productId,
			@RequestParam(value = "cartId", defaultValue = "0") Long cartId) {
		Connect connection = new Connect();
		ArrayList<Product> products = new ArrayList<Product>();
		products = connection.selectProducts(productId);

		if (cartId == 0) {
			cartId = new Random().nextLong();
		}

		JSONObject checkout = calculateCheckout(products);

		if (!products.isEmpty()) {
			connection.insertProducts(products, checkout, cartId);
		}

		JSONObject result = new JSONObject();
		result.put("cartId", cartId);
		result.put("products", convertProductToJsonString(products));
		result.put("checkout", checkout);

		return new ResponseEntity<String>(result.toString(), HttpStatus.OK);
	}

	@GetMapping("/remove")
	public ResponseEntity<String> removeToCart(@RequestParam(value = "productId", defaultValue = "") Long productId,
			@RequestParam(value = "all", defaultValue = "false") Boolean all,
			@RequestParam(value = "cartId", defaultValue = "0") Long cartId) {
		Connect connection = new Connect();
		List<Product> products = new ArrayList<Product>();
		JSONObject result = new JSONObject();
		products = connection.selectCartProducts(cartId);

		if (all) {
			products = new ArrayList<Product>();
		} else {
			for (Product product : products) {
				if (product.id == productId) {
					products.remove(product);
					break;
				}
			}
		}

		JSONObject checkout = calculateCheckout(products);

		result.put("cartId", cartId);
		result.put("products", convertProductToJsonString(products));
		result.put("checkout", checkout);

		connection.updateCart(result);

		return new ResponseEntity<String>(result.toString(), HttpStatus.OK);
	}

	@GetMapping("/checkout")
	public ResponseEntity<String> checkout(@RequestParam(value = "cartId", defaultValue = "0") Long cartId) {
		Connect connection = new Connect();
		ArrayList<Product> cartProducts = new ArrayList<Product>();
		cartProducts.addAll(connection.selectCartProducts(cartId));

		JSONObject result = calculateCheckout(cartProducts);

		return new ResponseEntity<String>(result.toString(), HttpStatus.OK);
	}

	@GetMapping("/sort")
	public ResponseEntity<String> sort(@RequestParam(value = "sortMethod", defaultValue = "name") String sortMethod,
			@RequestParam(value = "order", defaultValue = "crescent") String order,
			@RequestParam(value = "productIds", defaultValue = "") String productIds) {

		ArrayList<Product> products;

		if (productIds.length() == 0) {
			products = new Connect().selectAll();
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

		JSONObject resultJson = new JSONObject();
		resultJson.put("products", convertProductToJsonString(result));

		return new ResponseEntity<String>(resultJson.toString(), HttpStatus.OK);
	}

	public String convertProductToJsonString(List<Product> products) {
		ArrayList<String> result = new ArrayList<String>();

		for (Product product : products) {

			result.add(product.getJson().toString());
		}

		return result.toString();
	}

	public JSONObject calculateCheckout(List<Product> products) {
		BigDecimal total = new BigDecimal("0");
		BigDecimal subtotal = new BigDecimal("0");
		BigDecimal shipping = new BigDecimal("0");

		for (Product product : products) {
			if (subtotal.compareTo(new BigDecimal("250.0")) == -1) {
				shipping.add(new BigDecimal(10));
			} else if (subtotal.compareTo(new BigDecimal("250.0")) == 0) {
				shipping = new BigDecimal("0");
			}
			subtotal.add(product.price);
		}

		total = subtotal.add(shipping);

		JSONObject result = new JSONObject();
		result.put("subtotal", subtotal);
		result.put("shipping", shipping);
		result.put("total", total);

		return result;
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

}
