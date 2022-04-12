package bilboards;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfejs, który powinna implementować aplikacja pełniąca rolę menadżera
 * tablic (nazwijmy ją Menadżer). Menadżer powinien wyświetlać wszystkie
 * dowiązane do niego tablice oraz ich bieżący stan. Tablice dowiązują się do i
 * odwiązują od menadżera wywołując jego metody bindBillboard oraz
 * unbindBillboard. Z menadżerem może połączyć się Klient przekazując mu
 * zamówienie wyświetlania danego ogłoszenia przez zadany czas. Robi to
 * wywołując metodę placeOrder. Menadżer, jeśli przyjmie zamówienie, zwraca
 * Klientowi numer zamówienia wykorzystując przy tym przekazaną w zamówieniu
 * namiastke. Klient rozpoznaje, czy przyjęto zamówienie, sprawdzając wynik
 * zwracany z metody placeOrder.
 * Zamówienia natychmiast po przyjęciu trafiają na dowiązane Tablice mogące w danej chwili przyjąć ogłoszenie do wyświetlania.
 * Jeśli w danej chwili nie ma żadnej wolnej Tablicy zamówienie nie powinno być przyjęte do realizacji.
 * Aby przekonać się o stanie tablic Menadżer wywołuje metody ich namiastek getCapacity.
 * Wystarczy, że istnieje jedna wolna tablica by przyjąć zamówienie.
 * Na ile tablic trafi dane zamówienie decyduje dostępność wolnych miejsc w chwili zamówienia.  
 * 
 * Uwaga: Menadżer powinien utworzyć lub połączyć się z rejestrem rmi o
 * wskazanym numerze portu. Zakładamy, że rejestr rmi działa na tym samym
 * komputerze, co Menadżer (może być częścią aplikacji Menadżera).
 * Menadżer rejestruje w rejestrze rmi posiadaną
 * namiastke IManager pod zadaną nazwą (nazwa ta nie może być na twardo
 * zakodowanym ciągiem znaków). Nazwa namiastki menadżera, host i port na którym
 * działa rejest rmi powinny być dostarczone Klientowi (jako parmetry) oraz Tablicom.
 * 
 * @author tkubik
 *
 */
public interface IManager extends Remote { // port, nazwa, GUI

	/**
	 * Metoda dowiązywania namiastki Tablicy do Menadżera (wywoływana przez Tablicę)
	 * 
	 * @param billboard - dowiązywana namiastka
	 * @return - zwraca numer przyznany namiastce w Menadżerze
	 * @throws RemoteException
	 */
	public int bindBillboard(IBillboard billboard) throws RemoteException;

	/**
	 * Metoda odwiązująca namiastkę Tablicy z Menadżera (wywoływana przez Tablicę)
	 * @param billboardId - numer odwiązywanej namiastki
	 * @return
	 * @throws RemoteException
	 */
	public boolean unbindBillboard(int billboardId) throws RemoteException;

	/**
	 * Metoda służąca do składania zamówienia wyświetlania ogłoszenia (wywoływana przez Klienta)
	 * @param order - szczegóły zamówienia (wraz z tekstem ogłoszenia, czasem jego wyświetlania i namiastką klienta)
	 * @return - zwraca true jeśli przyjęto zamówienie oraz false w przeciwnym wypadku
	 * @throws RemoteException
	 */
	public boolean placeOrder(Order order) throws RemoteException;

	/**
	 * Metoda służąca do wycofywania zamówienia (wywoływana przez Klienta)
	 * @param orderId - numer wycofywanego zamówienia 
	 * @return - zwraca true jeśli wycofano zamówienie oraz false w przeciwnym wypadku
	 * @throws RemoteException
	 */
	public boolean withdrawOrder(int orderId) throws RemoteException;
}