# 💣 localhost-rzucanetnt

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.20.4-brightgreen.svg)
![Engine](https://img.shields.io/badge/Server-Paper%20%7C%20Spigot%20%7C%20Purpur-blue.svg)
![Build](https://img.shields.io/badge/Build-Maven-red.svg)
![Author](https://img.shields.io/badge/Author-Localhost-purple.svg)

Kompletny, legendarny i niezwykle dynamiczny plugin dodający **Rzucane TNT (rTNT)** na serwery Minecraft (Gildie, Anarchia, Hardcore, Survival, BoxPvP).

Plugin pozwala graczom na miotanie zapalonych ładunków wybuchowych z unikalną fizyką lotu, efektowną smugą cząsteczek, szybkim zapalnikiem oraz **szansą na niszczenie obsydianu i bloków zalanych wodą**.

---

## ✨ Funkcje i Możliwości

- **Rzucanie Ładunków Wybuchowych (PPM)**:
  - Kliknięcie Prawym Przyciskiem Myszy wyrzuca zapalony ładunek TNT w stronę patrzenia gracza z konfigurowalnym mnożnikiem prędkości oraz kątem uniesienia.
- **Szybki Zapalnik (Fast Fuse)**:
  - Domyślnie ładunek detonuje po **1.75 sekundy (35 ticków)** od momentu wyrzucenia, co umożliwia precyzyjne ataki w locie i na ścianach wrogich baz.
- **Niszczenie Obsydianu (Obsidian Breaker)**:
  - Konfigurowalna szansa (domyślnie **25%**) na skruszenie bloków obsydianu w promieniu wybuchu – kluczowa funkcja na serwerach z oblężeniami i wojnami gildii.
- **Niszczenie w Wodzie (Anti-Water Shield)**:
  - Opcja niszczenia bloków nawet jeśli ładunek wpadnie do wody (omija waniliową blokadę niszczenia bloków pod wodą).
- **Efektowna Smuga Cząsteczek i Dźwięki**:
  - Płomienisty ślad cząsteczek (`FLAME`) towarzyszący ładunkowi w powietrzu oraz dedykowane dźwięki wyrzutu rakiety i syczenia lontu.
- **Dynamiczny ActionBar**:
  - Wyświetla na bieżąco czas pozostały do kolejnego rzutu, gdy gracz trzyma Rzucane TNT w dłoni (`Rzucane TNT: Odnawianie (Xs)` lub `Gotowe do rzutu!`).
- **Wbudowana Receptura Craftingu**:
  - Legalny crafting w stole rzemieślniczym (domyślnie: 4x Proch, 4x TNT oraz 1x Kula Ognia / Fire Charge w środku).
- **Identyfikacja PersistentDataContainer (PDC)**:
  - Ładunki są znakowane cyfrowym kluczem NBT/PDC – gracze nie mogą sfałszować przedmiotu przez zmianę nazwy zwykłego TNT w kowadle.
- **Wsparcie CustomModelData**:
  - Kompatybilność z własnymi Resource Packami (Oraxen / ItemsAdder).

---

## 🔨 Receptura w Craftingu

Domyślny układ w stole rzemieślniczym (3x3):

```text
[ Proch ]   [  TNT  ]   [ Proch ]
[  TNT  ]   [ FireCharge ] [  TNT  ]
[ Proch ]   [  TNT  ]   [ Proch ]
```

*Recepturę, składniki oraz ilość otrzymywanego TNT można zmienić w `config.yml`.*

---

## 💻 Komendy i Uprawnienia

### Komendy:
Główna komenda: `/rzucanetnt` (aliasy: `/rtnt`, `/throwtnt`)

- `/rzucanetnt` – Wyświetla informacje o działaniu ładunku i recepturze craftingu.
- `/rzucanetnt daj <gracz> [ilość]` – Nadaje graczowi wybraną liczbę Rzucanego TNT (*np. `/rzucanetnt daj Steve 16`*).
- `/rzucanetnt reload` – Przeładowuje konfigurację oraz rejestruje zaktualizowany crafting.

### Uprawnienia (Permissions):

| Uprawnienie | Domyślnie | Opis |
| :--- | :---: | :--- |
| `rzucanetnt.use` | Wszyscy (`true`) | Dostęp do rzucania TNT oraz sprawdzania receptury |
| `rzucanetnt.admin` | Administratorzy (`op`) | Dostęp do komendy `/rzucanetnt daj` oraz `/rzucanetnt reload` |

---

## 📂 Struktura Projektu

```text
localhost-rzucanetnt/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── pl/localhost/rzucanetnt/
        │       ├── Main.java                        # Główna klasa pluginu (Main)
        │       ├── command/
        │       │   └── RzucaneTNTCommand.java       # Obsługa komendy /rzucanetnt i TabCompleter
        │       ├── listener/
        │       │   └── TNTListener.java             # Rzucanie (PPM), niszczenie obsydianu i wody
        │       └── manager/
        │           ├── ConfigManager.java           # Konfiguracja parametrów wybuchu, dźwięków i wiadomości
        │           ├── CooldownManager.java         # Odliczanie czasu i pasek ActionBar
        │           └── TNTManager.java              # Fizyka rzutu, cząsteczki i receptura craftingu
        └── resources/
            ├── config.yml                           # Konfiguracja prędkości, zapalnika, szans i komunikatów
            └── plugin.yml                           # Metadane pluginu
```

---

## 🛠️ Jak Zbudować (Kompilacja)

### Wymagania:
- **Java 17** lub nowsza
- **Maven 3.8+**

### Kompilacja:
```bash
cd localhost-rzucanetnt
mvn clean package
```

Zbudowany plik `.jar` pojawi się w folderze `target/`:
```text
target/localhost-rzucanetnt-1.0-SNAPSHOT.jar
```

---

## ⚙️ Wymagania i Zależności

- **Zależności:** Brak dodatkowych pluginów! Działa w 100% natywnie (*standalone*) na **Paper**, **Purpur** oraz **Spigot 1.20+**.
- **Kompatybilność:** Java 17+, Paper 1.20 - 1.20.4+.

---

## 👤 Autor

- **Autor:** Localhost
- **Discord:** `localhost_127001`
- **Projekt:** Seria pluginów serwerowych `localhost-*`
