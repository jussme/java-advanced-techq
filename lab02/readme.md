## -Xmxn
Opcja ustawiająca maksymalny rozmiar sterty na *n* bajtów.
Całkowita zajętość pamięci przez maszynę wirtualną może być większa od *n*.

Dla *n* równego 50 plik *.png* o rozmiarze 13 MB jest każdorazowo ładowany z dysku.
Dla *n* równego 60 jest każdorazowo ładowany z pamięci(jeżeli nie są ładowane inne obrazy).
## -XX:+/-ShrinkHeapInSteps
Opcja pozwalająca wymusić(-) całkowitą redukcję rozmiaru sterty do docelowego
w jednym cyklu działania GC, domyślnie proces ten trwa kilka cykli.
Może wiązać się ze spadkiem wydajności
### Typy GC
## -XX:+UseSerialGC
Zatrzymuje wszystkie wątki aplikacji gdy działa. Wykorzystuje pojedynczy wątek.
## -XX:+UseParallelGC
Zatrzymuje wszystkie wątki aplikacji gdy działa. Wykorzystuje wiele wątków.
Więcej możliwości modyfikacji działania.
## -XX:+UseG1GC
Dzieli stertę na ciągłe, równe regiony. Priorytetowo doczyszcza regiony o małej zajętości.