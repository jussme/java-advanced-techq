package processing;
public interface StatusListener {
	/**
	 * Metoda s³uchacza
	 * @param s - status przetwarzania zadania
	 */
	void statusChanged(Status s);
}
