package p3.jpa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data

@Table(name="dogs")
public class Dog {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String type;

	public Dog(String name, String type) {
		super();
		this.name = name;
		this.type = type;

	}
}

