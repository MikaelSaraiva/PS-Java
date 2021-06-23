package br.com.supera.game.store;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.json.JSONObject;

@Entity
public class Product {

	@Id
	@GeneratedValue
	public long id;

	public String name;

	public BigDecimal price;

	public short score;

	public String image;

	public Product(int id, String name, BigDecimal price, short score, String image) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.score = score;
		this.image = image;
	}

	public JSONObject getJson() {
		JSONObject result = new JSONObject();
		result.put("id", this.id);
		result.put("name", this.name);
		result.put("price", this.price);
		result.put("score", this.score);
		result.put("image", this.image);
		
		return result;
	}
}
