package ru.lexa.mimimetr.model;

import javax.persistence.*;

@Entity
public class Kitty {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	private double rating;
	private String name;
	private String photo;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Kitty() {
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
