package ru.lexa.mimimetr.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "pairs")
public class Pair {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	private int aId;
	private int bId;

	public Pair() {
	}

	public Pair(int aId, int bId) {
		this.aId = aId;
		this.bId = bId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public int getaId() {
		return aId;
	}

	public void setaId(int aId) {
		this.aId = aId;
	}

	public int getbId() {
		return bId;
	}

	public void setbId(int bId) {
		this.bId = bId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Pair pair = (Pair) o;
		return aId == pair.aId && bId == pair.bId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(aId, bId);
	}
}
