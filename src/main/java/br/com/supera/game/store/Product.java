package br.com.supera.game.store;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
}
