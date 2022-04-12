package bilboards;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfejs, który powinien zaimplementować klient (nazwijmy tę alikację
 * Klient) komunikujący się z menadżerem tablic. Klient powinien mieć interfejs
 * pozwalający: i) definiować zamówienia wyświetlania ogłoszenia (tekst
 * ogłoszenia, czas wyświetlania) ii) składać zamówienia wyświetlania ogłoszenia
 * Menadżerowi, iii) wycofywać złożone zamówienia.
 * 
 * Przy okazji składania zamówienia wyświetlania ogłoszenia Klient przekazuje
 * Menadżerowi namiastkę IClient. Menagdżer użyje tej namiastki, by zwrotnie
 * przekazać klientowi numer zamówienia (jeśli oczywiście zamówienie zostanie
 * przyjęte). Ma to działać podobnie jak ValueSetInterface z przykładu RMITest.
 * 
 * Numery zamówień i treści ogłoszeń przyjętych przez Menadżera powinny być
 * widoczne na interfejsie Klienta. Klient powinien sam zadbać o usuwanie
 * wpisów, których okres wyświetlania zakończył się (brak synchronizacji w tym
 * względzie z menadżerem)
 * 
 * Uwaga: Klient powinien być sparametryzowany numerem portu i hostem rejestru
 * rmi, w którym zarejestrowano namiastkę Menadżera, oraz nazwa, pod którą
 * zarejestrowano tę namiastkę.
 * 
 * @author tkubik
 *
 */
public interface IClient extends Remote { // host, port, nazwa
	/**
	 * Metoda służąca do przekazania numeru przyjętego zamówienia (wywoływana przez
	 * Menadżera na namiastce klienta przekazanej w zamówieniu)
	 * 
	 * @param orderId
	 * @throws RemoteException
	 */
	public void setOrderId(int orderId) throws RemoteException;
}