package ex.api;

/**
 * Interfejs serwisu pozwalaj¹cego przeprowadziæ analizê skupieñ.
 * Zak³adamy, ¿e serwis dzia³a asynchronicznie.
 * Na pocz¹tek nale¿y do serwisu za³adowaæ dane.
 * Potem mo¿na z serwisu pobraæ wyniki analizy.
 * W przypadku niepowodzenia wykonania jakiejœ metody wyrzucony zostanie wyj¹tek.
 * 
 * @author tkubik
 *
 */
public interface ClusterAnalysisService {
	public void setOptions(String[] options) throws ClusteringException; // ustawia opcje
	// metoda zwracaj¹ca nazwê algorytmu
	public String getName();                                   
	// metoda pozwalaj¹ca przekazaæ dane do analizy
	// wyrzuca wyj¹tek, jeœli aktualnie trwa przetwarzanie danych
	public void submit(DataSet ds) throws ClusteringException; 
	// metoda pozwalaj¹ca pobraæ wynik analizy
    // zwraca null - jeœli trwa jeszcze przetwarzanie lub nie przekazano danych do analizy
	// wyrzuca wyj¹tek - jeœli podczas przetwarzania dosz³o do jakichœ b³êdów
	// clear = true - jeœli wyniki po pobraniu maj¹ znikn¹æ z serwisu
    public DataSet retrieve(boolean clear) throws ClusteringException;   
}

