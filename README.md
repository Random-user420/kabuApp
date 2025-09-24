# KabuApp

## Beschreibung

Diese Android-Anwendung dient zur Anzeige des Stunden- und Schulaufgabenplans. Benutzer:innen können den Stundenplan für verschiedene Tage einsehen, durch Wischen navigieren und den Plan bei Bedarf aktualisieren. Dies wird auch automatisch im Hintergrund getan.

**Diese App ist nur für Schüler:innen des BSZ Wiesau**

Das BSZ Wiesau ist nicht für diese App verantwortlich, dies ist ein rein privates Projekt.

## Features

* Anzeige des Stundenplans für einen ausgewählten Tag.
* Anzeige der Schulaufgaben, Ferien und besondere Ereignisse
* Horizontale Navigation (Wischen) zwischen den Tagen.
* Datumsauswahlleiste zum schnellen Wechsel des Datums.
* Pull-to-Refresh Funktion zur Aktualisierung des aktuellen Stundenplans.
* Authentifizierung/Login zur API.

## Technologien und Bibliotheken

* **Android SDK**
* **Java**
* **XML** für Layouts
* **AndroidX Libraries** (ConstraintLayout, RecyclerView, SwipeRefreshLayout, etc.)
* **View Binding** für den Zugriff auf UI-Elemente.
* **Java Time API (LocalDate)** für die Datumsverwaltung.
* **ExecutorService** für die Hintergrundausführung von Aufgaben (z.B. API-Aufrufe).
* **Apache-http\*** für die API Aufrufe.
* **Room** als DataBase abstraction.
* **[MetisJson](https://github.com/Random-user420/MetisJson)** als AGL2 Json lib.
* Externe API: www.digikabu.de/swagger/


# Zum Nutzen

## Voraussetzungen
* Android (version 9.0+)
* Aktivierung der Installation von nicht signed Apps, falls nötig. Bitte bei Problemen googeln.

## Schritte
1. Lade die neuste veröffentlichte APK von [Releases](https://github.com/Random-user420/kabuApp/releases/tag/0.5-beta) auf dein Handy
2. Klicke installieren\
  2.1. Falls das Handy die Installation nicht zulässt, gehe zum zweiten Punkt von Voraussetzungen zurück.    

# How to use

## Prerequisites
* Android phone (version 9.0+)
* No locking on unsinged Apps

## Steps
1. Download the newest release APK from [Releases](https://github.com/Random-user420/kabuApp/releases/tag/0.5-beta) to your Phone
2. Click to install\
  2.1. If there are questions form your phone, anwser them accordingly. If your phone doesn't want to install the app, google how to install unsigned apps  \<your phone model>   
  
## Contributing

Contributions are welcome! If you find bugs or would like to suggest improvements, please create an issue or submit a pull request.

## Getting Started

This guide helps you set up and run the project locally.

### Prerequisites

* Android Studio installed
* Checkstyle Plugin installed
* An Android device or emulator

### Installation

1. Have [MetisJson](https://github.com/Random-user420/MetisJson) in the local Maven repo installed.
2. Clone the repository:
   ```bash
   git clone https://github.com/Random-user420/kabuApp.git
3. Open the project in Android Studio.
4. Let Gradle synchronize dependencies.
   
5. Connect an Android device to your computer or start an emulator.
6. Select the device in Android Studio.
7. Click the "Run" button (green arrow) or go to Run > Run 'app'.

The app should be installed and launched on your device/emulator. Note that the app requires a connection to the requires a login.

Structure\
The project follows the standard Android app structure. Key packages/components include:

```org.lilith.kabuapp```: Contains Java code. The naming is self explaining.

```res/layout```: XML layout files.

```res/values```: Resources like strings, colors, etc.

---
**Note:**
Check your codestyle with the Checkstyle Plugin and the checkstyle.xml included in the project before making a pull request. I use this to keep the code style consistent.

# Privacy Policy / Datenschutzerklärung
[here](https://github.com/Random-user420/kabuApp/blob/03a3f4968481bb9b7f2bf1d6e39d4fefae375c66/PRIVACY.md)

# Warranty
** We don't provide any sort of warranty on this program **

**contact: lilithtechs@protonmail.com**

## Contributors

<a href="https://github.com/Random-user420/kabuApp/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Random-user420/kabuApp"/>
</a>

# Copyright
For Everything that is committed under "Random-user420"

(C) 2025 Lilith

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation version 3 of the License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
