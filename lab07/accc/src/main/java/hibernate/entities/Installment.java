package hibernate.entities;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import base.DatabaseWrapper;

@Entity(name="installment")
@Table(name="Installments", schema = "Accounting")
public class Installment extends BaseEntity{
	
	@ManyToOne(optional=false)
	@JoinColumn(name="event_id", nullable=false)
	private Event eventt;
	
	@Column(name="number", nullable=false, unique=false)
	private int installmentNumber;
	
	@Column(name="date", nullable=false, unique=false)
	private LocalDateTime dueDate;
	
	@Column(name="amount", nullable=false, unique=false)
	private Long dueAmount;
	
	public Installment(Event event, int installmentNumber, LocalDateTime dueDate, Long dueAmount) {
		this.eventt = event;
		this.installmentNumber = installmentNumber;
		this.dueDate = dueDate;
		this.dueAmount = dueAmount;
	}
	
	public Installment() {}
	
	public Installment(Map<String, String> propertyVal) {
		eventt = DatabaseWrapper.getEntity(Event.class, Long.valueOf(propertyVal.get("Event")));
		installmentNumber = Integer.valueOf(propertyVal.get("Number"));
		dueDate = LocalDateTime.parse(propertyVal.get("DueDate"));
		dueAmount = Long.valueOf(propertyVal.get("DueAmount"));
	}
	
	@Override
	public List<String> getProperties() {
		return Arrays.asList("Id", "Event", "Number", "DueDate", "DueAmount");
	}
	
	@Override
	public List<String> toStrings() {
		return Arrays.asList(String.valueOf(this.getId()),
				String.valueOf(eventt.getId()),
				String.valueOf(installmentNumber),
				String.valueOf(dueDate.toString()),
				String.valueOf(dueAmount));
	}
}
