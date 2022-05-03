package hibernate.entities;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name="event")
@Table(name="Events", schema = "Accounting")
public class Event extends BaseEntity{
	
	@Column(name="Event_name", length=50, nullable=false, unique=false)
	private String name;
	
	@Column(name="Event_location", length=50, nullable=false, unique=false)
	private String location;
	
	@Column(name="Event_datetime", nullable=true, unique=false)
	private LocalDateTime date;
	
	@OneToMany(mappedBy="eventt")//installment is the owning side
	private Set<Installment> installments;
	
	@OneToMany(mappedBy="coveredEvent")
	private Set<Payment> payments;

	public Event(String name, String location, LocalDateTime date) {
		this.name = name;
		this.location = location;
		this.date = date;
	}
	
	public Event(Map<String, String> propertyValue) {
		name = (String) propertyValue.get("name");
		location = (String) propertyValue.get("location");
		date = LocalDateTime.parse(propertyValue.get("date"));
	}
	
	public Event() {}

	@Override
	public List<String> toStrings() {
		return Arrays.asList(String.valueOf(this.getId()),
				name,
				location,
				String.valueOf(date));
	}

	@Override
	public List<String> getProperties() {
		return Arrays.asList("Id", "name", "location", "date");
	}
}
