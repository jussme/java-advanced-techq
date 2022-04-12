package bilboards;

import java.io.Serializable;
import java.time.Duration; // available since JDK 1.8

/**
 * Klasa reprezentujaca zamowienie wyswietlania ogloszenia o zadanej tresci
 * przez zadany czas ze wskazaniem na namiastke klienta, przez ktora mozna
 * przeslac informacje o numerze zamowienia w przypadku jego przyjecia
 * 
 * @author tkubik
 *
 */
public class Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String advertText;
	public Duration displayPeriod;
	public IClient client;
}