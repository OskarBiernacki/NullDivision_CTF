![NullDivision Logo](logo.png)

# NullDivision CTF

## Wprowadzenie
Witaj w CTF-ie **NullDivision**! Wcielasz się w rolę śledczego, który rozpracowuje grupę hakerską o nazwie **NullDivision**. Po długim śledztwie udało Ci się zlokalizować ich serwer, na którym znajduje się forum rekrutacyjne. Nowi członkowie grupy pobierają z niego aplikację mobilną, która służy do komunikacji między członkami grupy.

Twoim zadaniem jest:
1. **Zdekompilowanie aplikacji mobilnej**, aby zrozumieć jej działanie.
2. **Zalogowanie się na serwer FTP** jako użytkownik `anonymous`, aby pobrać aplikację.
3. **Eksploatacja błędów w komunikacji między aplikacją a serwerem**, aby uzyskać dostęp do treści zadań innych użytkowników.

## Wymagania
- **Docker** i **Docker Compose** zainstalowane na Twoim systemie.
- Narzędzia do dekompilacji aplikacji mobilnych (np. `apktool`, `jadx`, `dex2jar`, `jd-gui`).
- Narzędzia do analizy ruchu sieciowego (np. `Wireshark`, `Burp Suite`).

## Instalacja
1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/your-repo/NullDivision_CTF.git
   cd NullDivision_CTF
   ```

2. Uruchom środowisko za pomocą Docker Compose:
   ```bash
   docker-compose up --build -d
   ```

3. Sprawdź, czy kontener działa:
   ```bash
   docker-compose ps
   ```

4. Serwer FTP będzie dostępny na porcie `21`, a serwer aplikacji na porcie `2137`.

## Zadania
1. **Zaloguj się na serwer FTP**:
   - Użyj dowolnego klienta FTP, aby zalogować się jako `anonymous`.
   - Pobierz plik `nulldivision-client.apk`.

   Przykład logowania:
   ```bash
   ftp localhost 21
   ```

2. **Zdekompiluj aplikację mobilną**:
   - Zbadaj kod aplikacji, aby zrozumieć, jak komunikuje się z serwerem.
   - Użyj narzędzi takich jak:
     - `apktool`: Do dekompilacji pliku APK i analizy plików XML oraz zasobów.
     - `jadx`: Do analizy kodu źródłowego w języku Java.
     - `dex2jar`: Do konwersji plików `.dex` na `.jar`.
     - `jd-gui`: Do przeglądania plików `.jar` w czytelnej formie.

3. **Eksploatuj błędy w komunikacji**:
   - Zidentyfikuj słabości w implementacji API serwera.
   - Wykorzystaj je, aby uzyskać dostęp do treści zadań innych użytkowników.

## Tło fabularne
Jesteś śledczym, który od miesięcy śledzi grupę hakerską **NullDivision**. Po wielu próbach udało Ci się zlokalizować ich serwer, który pełni rolę forum rekrutacyjnego. Nowi członkowie grupy pobierają z niego aplikację mobilną, która umożliwia komunikację między członkami.

Twoim celem jest:
- Zrozumienie, jak działa aplikacja.
- Wykorzystanie błędów w jej implementacji, aby uzyskać dostęp do poufnych informacji grupy.

## Przydatne wskazówki
- **FTP**: Serwer FTP jest skonfigurowany w trybie `anonymous`, co oznacza, że możesz zalogować się bez hasła.
- **Dekompilacja**: Użyj narzędzi takich jak `apktool`, `jadx`, `dex2jar` i `jd-gui`, aby przeanalizować kod aplikacji.
- **Analiza ruchu sieciowego**: Monitoruj komunikację między aplikacją a serwerem, aby zidentyfikować potencjalne luki.

## Rozwiązywanie problemów
- Jeśli kontener nie działa, sprawdź logi:
  ```bash
  docker-compose logs
  ```
- Upewnij się, że porty `21` i `2137` są otwarte i nie są zajęte przez inne procesy.

## Autorzy
Ten CTF został stworzony przez **Oskar Biernacki** na poczet CTF_PJATK_2025.

Powodzenia, śledczy! 🎯