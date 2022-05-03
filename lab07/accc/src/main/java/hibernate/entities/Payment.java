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

@Entity(name="payment")
@Table(name="Payments", schema = "Accounting")
public class Payment extends BaseEntity{
	
	@ManyToOne(optional=false)
	@JoinColumn(name="person_id", nullable=false)
	private Person payingPerson;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="event_id", nullable=false)
	private Event coveredEvent;
	
	@Column(name="paymentDate", nullable=false, unique=false)
	private LocalDateTime paymentDate;
	
	@Column(name="paymentAmount", nullable=false, unique=false)
	private Long paymentAmount;
	
	@Column(name="installmentNumber", nullable=false, unique=false)
	private int installmentNumber;
	
	public Payment(Person payingPerson, Event event, LocalDateTime paymentDate, Long paymentAmount, int installmentNumber) {
		this.payingPerson = payingPerson;
		this.coveredEvent = event;
		this.paymentDate = paymentDate;
		this.paymentAmount = paymentAmount;
		this.installmentNumber = installmentNumber;
	}
	
	public Payment() {}
	
	public Payment(Map<String, String> propertyValue) {
		coveredEvent = DatabaseWrapper.getEntity(Event.class, Long.valueOf(propertyValue.get("EventId")));
		payingPerson = DatabaseWrapper.getEntity(Person.class, Long.valueOf(propertyValue.get("PayerId")));
		installmentNumber = Integer.valueOf(propertyValue.get("InstallmentNumber"));
		paymentDate = LocalDateTime.parse(propertyValue.get("PaymentDate"));
		paymentAmount = Long.valueOf(propertyValue.get("PaymentAmount"));
	}
	
	@Override
	public List<String> getProperties() {
		return Arrays.asList("Id", "PayerId", "EventId", "InstallmentNumber", "PaymentDate", "PaymentAmount");
	}
	
	public List<String> toStrings() {
		return Arrays.asList(String.valueOf(this.getId()),
				String.valueOf(payingPerson.getId()),
				String.valueOf(coveredEvent.getId()),
				String.valueOf(installmentNumber),
				paymentDate.toString(),
				String.valueOf(paymentAmount));
	}
}
