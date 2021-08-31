trafficsim by KC (Java i JavaFX)

Szybki start:
1. Wyskoczy błąd o braku pliku do wczytania - bo jest zhardcodowany plik do wczytania w klasie SimulationApplication (domyślnie kratownica_wycieta).
2. Narysuj swoją symulację - przyciski Road oraz Urban Area i Industry Area - potem przeciągnij LPM, aby nanieść.
3. Przeciągnij PPM, aby skasować obszar.
4. Postaraj się, aby odległość segmentów od najbliższego segmentu drogi nie była większa niż 6 - w przeciwnym wypadku mogą występować błędy (wartością tą można sterować za pomocą zmiennej SEARCH_RADIUS w klasie MainWindow)
5. Wygeneruj graf przez Generate graph
6. Przypisz segmenty samym sobie przez Bind and calculate paths
7. Odpal symulację przez Play. Wcześniej możesz zaznaczyć Make citizen smart, aby omijali korki 
8. W trakcie symulacji możesz oglądać zatłoczenie poszczególnych krawędzi w trybie Traffic heat. 
9. Po symulacji możesz obejrzeć wykresy - liczby na dole wykresów dotyczą OSTATNIEJ serii danych.
10. Kliknij Stop i odpal symulację jeszcze raz. Jeśli nie kliknąłeś Bind and calculate paths, to do wykresów zostanie dopisana nowa seria danych, aby móc ją porównać z poprzednią.
11. W logu wyświetla się krótkie SUMMARY - mówi o ilości zmian tras i zawróceń.


FAQ:
0) Za "upartość" mieszkańców w dążeniu do celu odpowiada stubbornnessFactor i funkcja willingToChangeRoutes() w klasie MovingCitizen. StubbornnessFactor to po prostu ilość krawędzi, jaką musi przejechać mieszkaniec, by móc ponownie zmienić trasę. 
1) Dystans w zależności od zatłoczenia dyktuje funkcja getDistance() w klasie GraphNode.
2) Za rozmieszczanie mieszkańców na mapie w trakcie aktywnej symulacji odpowiada CitizenMovementsContainer.
3) Za kolorek w zależności od ruchu (w trybie Traffic Heat) odpowiada klasa RoadOverlay
4) SEARCH_RADIUS w klasie MainWindow odpowiada za odległość poszukiwania najbliższego segmentu drogi

Znane błędy: rysowanie przy lewej krawędzi (na x=0) wyrzuca błąd przy generowaniu grafu.
