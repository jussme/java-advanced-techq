package bilboards;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Duration;

/**
 * Interfejs, który powinna implementować aplikacja pełniąca rolę tablicy ogłoszeniowej (nazwijmy ją Tablica).
 * Aplikacja ta powinna wyświetlać cyklicznie teksty ogłoszeń dostarczane metodą addAdvertisement.
 * Za wyświetlanie tych ogłoszeń powinien odpowiadać osobny wątek.
 * Wątek powinien mieć dostęp do bufora na ogłoszenia, którego pojemność i liczbę wolnych miejsc
 * zwraca metoda getCapacity.
 * Za dodawanie ogłoszenia do bufora odpowiada metoda addAdvertisment. 
 * Z chwilą pierwszego wyświetlenia ogłoszenia na tablicy powinien zacząć zliczać się czas jego wyświetlania.
 * Usuwanie ogłoszenia może nastąpić z dwóch powodów: i) ogłoszenie może zostać usunięte na skutek
 * wywołania metody removeAdvertisement (przez Menadżera); ii) ogłoszenie może zostać usunięte, gdy skończy się przyznany 
 * mu czas wyświetlania na tablicy (przez wątek odpowiedzialny w Tablicy za cykliczne wyświetlanie testów).
 * Usuwanie ogłoszeń z bufora i ich dodawanie do bufora wiąże się z odpowiednim zarządzaniem
 * podległą strukturą danych
 * W "buforze" mogą się robić "dziury", bo ogłoszenia mogą mieć przyznany różny czas wyświetlania. 
 * Należy więc wybrać odpowiednią strukturę danych, która pozwoli zapomnieć o nieregularnym występowaniu tych "dziur").
 * Metoda start powinna dać sygnał aplikacji, że należy rozpocząć cykliczne wyświetlanie ogłoszeń.
 * Metoda stop zatrzymuje wyświetlanie ogłoszeń.
 * Metody start i stop można odpalać naprzemiennie, przy czym nie powinno to resetować zliczonych czasów wyświetlania 
 * poszczególnych ogłoszeń.
 * Uwaga: Tablica powininna być sparametryzowany numerem portu i hostem rejestru
 * rmi, w którym zarejestrowano namiastkę Menadżera, oraz nazwa, pod którą
 * zarejestrowano tę namiastkę.
 * Jest to potrzebne, by Tablica mogła dowiązać się do Menadżera. 
 * Tablica robi to wywołując metodę bindBillboard (przekazując jej swoją namiastkę typu IBillboard).
 * Otrzymuje przy tym identyfikator, który może użyć, by się mogła wypisać z Menadżera 
 * (co może stać się podczas zamykania tablicy).
 * 
 * @author tkubik
 *
 */

public interface IBillboard extends Remote {
	/**
	 * Metoda dodająca tekst ogłoszenia do tablicy ogłoszeniowej (wywoływana przez
	 * Menadżera po przyjęciu zamówienia od Klienta)
	 * 
	 * @param advertTextn   - tekst ogłoszenia, jakie ma pojawić się na tablicy
	 *                      ogłoszeniowej
	 * @param displayPeriod - czas wyświetlania ogłoszenia liczony od pierwszego
	 *                      jego ukazania się na tablicy ogłoszeniowej
	 * @param orderId       - numer ogłoszenia pod je zarejestrowano w menadżerze
	 *                      tablic ogłoszeniowych
	 * @return - zwraca true, jeśli udało się dodać ogłoszenie lub false w
	 *         przeciwnym wypadku (gdy tablica jest pełna)
	 * @throws RemoteException
	 */
	public boolean addAdvertisement(String advertText, Duration displayPeriod, int orderId) throws RemoteException;

	/**
	 * Metoda usuwająca ogłoszenie z tablicy (wywoływana przez Menadżera po
	 * wycofaniu zamówienia przez Klienta)
	 * 
	 * @param orderId - numer ogłoszenia pod jakim je zarejestrowano w menadżerze
	 *                tablic ogłoszeniowych
	 * @return - zwraca true, jeśli operacja się powiodła lub false w przeciwnym
	 *         wypadku (gdy nie ma ogłoszenia o podanym numerze)
	 * @throws RemoteException
	 */
	public boolean removeAdvertisement(int orderId) throws RemoteException;

	/**
	 * Metoda pobierająca informację o zajętości tablicy (wywoływana przez
	 * Menadżera)
	 * 
	 * @return - zwraca tablicę dwóch liczb całkowitych - pierwsza to pojemność
	 *         bufora tablicy, druga to liczba wolnych w nim miejsc
	 * @throws RemoteException
	 */
	public int[] getCapacity() throws RemoteException;

	/**
	 * Metoda pozwalająca ustawić czas prezentacji danego tekstu ogłoszenia na
	 * tablicy w jednym cyklu (wywoływana przez Menadżera). Proszę nie mylić tego z
	 * czasem, przez jaki ma być wyświetlany sam tekst ogłoszenia. Prezentacja
	 * danego hasła musi być powtórzona cyklicznie tyle razy, aby sumaryczny czas
	 * prezentacji był równy lub większy zamówionemu czasowi wyświetlania tekstu
	 * ogłoszenia.
	 * 
	 * @param displayInterval - definiuje czas, po którym następuje zmiana hasła
	 *                        wyświetlanego na tablicy. Odwrotność tego parametru
	 *                        można interpetować jako częstotliwość zmian pola
	 *                        reklamowego na Tablicy.
	 * @throws RemoteException
	 */
	public void setDisplayInterval(Duration displayInterval) throws RemoteException;

	/**
	 * Metoda startująca cykliczne wyświetlanie ogłoszeń (wywoływana przez
	 * Menadżera)
	 * 
	 * @return - zwraca true, jeśli zostanie poprawnie wykonana lub false w
	 *         przeciwnym wypadku
	 * @throws RemoteException
	 */
	public boolean start() throws RemoteException;

	/**
	 * Metoda stopująca cykliczne wyświetlanie ogłoszeń (wywoływana przez Menadżera)
	 * 
	 * @return - zwraca true, jeśli zostanie poprawnie wykonana lub false w
	 *         przeciwnym wypadku
	 * @throws RemoteException
	 */
	public boolean stop() throws RemoteException;
}