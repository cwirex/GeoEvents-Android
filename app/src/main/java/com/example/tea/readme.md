## Plan działania aplikacji
1. Logowanie/Rejestracja:
    - Podanie loginu (email) i hasła 
      + Uaktualnienie bazy po zalogowaniu
    - Pobieranie nicku użytkownika ~**LoginDataSource**
2. Friends:
    - Add: Pole do wprowadzania email (po wpisaniu sprawdza czy jest taki użytkownik i OD RAZU go dodaje do znajomych - obustronnie)
    - Delete: Usuwanie znajomych (zaczerpnięte z Sample)
    - onItemClick: Wyświetl dokładne informacje o wybranym znajomym
3. Events:
    - Add:
      - Pola tekstowe: title, description, locationName
      - Kalendarz z zegarkiem: timeFrame (startDate, endDate)
      - Mapka: Marker wybierany z mapy
      - Lista znajomych: (z checkboxami)
   - Delete, Edit: None (nie implementujemy)
   - Show: recyclerView z onItemClick for detailed info
4. Invitations:
   - Show: recyclerView z zaproszeniami, pogrupowane klasami (oczekujace, przyjete, odrzucone...)
   - onItemClick: info o Evencie (z mapką) i opcjami dla poszczególnych klas (akceptuj, odrzuc)
5. Map:
   - Display all events and friends' positions
   - onItemClick: info, action*

## DONE
- info: email to UID dict
- friends: add, get