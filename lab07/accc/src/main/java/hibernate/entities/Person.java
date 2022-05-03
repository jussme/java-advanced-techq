package hibernate.entities;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name="person")
@Table(name="Persons", schema = "Accounting")
public class Person extends BaseEntity{
	
	@Column(name="firstName", length=50, nullable=false, unique=false)
	private String firstName;
	
	@Column(name="lastName", length=50, nullable=false, unique=false)
	private String lastName;
	
	@OneToMany(mappedBy="payingPerson")//payment is the owning side
	private Set<Payment> payments;
	
	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Person(Map<String, String> propertyValue) {
		firstName = propertyValue.get("firstName");
		lastName = propertyValue.get("lastName");
	}

	@Override
	public List<String> toStrings() {
		return Arrays.asList(String.valueOf(this.getId()),
				firstName,
				lastName);
	}
	
	public Person() {}

	@Override
	public List<String> getProperties() {
		return Arrays.asList("Id", "firstName", "lastName");
	}
}
