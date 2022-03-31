package processing;

public interface Processor {
	/**
	 * Metoda s³u¿¹ca do zlecania zadañ
	 * 
	 * @param task - tekst reprezentuj¹cy zadanie
	 * @param sl   - s³uchacz, który bêdzie informowany o statusie przetwarzania
	 * @return - wartoœæ logiczn¹ mówi¹c¹ o tym, czy zadanie przyjêto do
	 *         przetwarzania (nie mo¿na zleciæ kolejnych zadañ dopóki bie¿¹ce
	 *         zadanie siê nie zakoñczy³o i nie zosta³ pobrany wynik przetwarzania
	 */
	boolean submitTask(String task, StatusListener sl);

	/**
	 * Metoda s³u¿¹ca do pobierania informacji o algorytmie przetwarzania
	 * 
	 * @return - informacja o algorytmie przetwarzania (czytelna dla cz³owieka)
	 */
	String getInfo();

	/**
	 * Metoda s³u¿¹ca do pobierania wyniku przetwarzania
	 * 
	 * @return - tekst reprezentuj¹cy wynik przetwarzania
	 */
	String getResult();
}
