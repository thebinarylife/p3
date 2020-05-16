package p3.jdbctemplate.model;




import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data

@Table(value="cats")
public class Cat {
	 private Long id;
	 private String name;
	 private String type;

	public Cat(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	
	}
}
