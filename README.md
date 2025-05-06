# KabuApp

## Beschreibung

Diese Android-Anwendung dient zur Anzeige des Stundenplans. Benutzer können den Stundenplan für verschiedene Tage einsehen, durch Wischen navigieren und den Plan bei Bedarf aktualisieren.

**Diese App ist nur für Schüler:innen der BSZ Wiesau**

Die BSZ Wiesau ist nicht für diese App verantwortlich oder involviert, dies ist ein rein privates Projekt.

## Features

* Anzeige des Stundenplans für einen ausgewählten Tag.
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
* Externe API: www.digikabu.de/api/

# How to use

## Prerequisites
* Android phone (version 9.0+)
* No locking on unsinged Apps

## Steps (comming soon)
1. Download the newest release APK to your Phone
2. Click to install
2.1. if there are questions form your Phone, awnser them accordingly. If your Phone doesn't want to install the app, google how to install unsigned apps  \<your phone model>   
  
## Contributing

Contributions are welcome! If you find bugs or would like to suggest improvements, please create an Issue or submit a Pull Request.

## Getting Started

This guide helps you set up and run the project locally.

### Prerequisites

* Android Studio installed
* Checkstyle Plugin installed
* An Android device or emulator

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Random-user420/kabuApp.git
2. Open the project in Android Studio.
3. Let Gradle synchronize dependencies.
   
4. Connect an Android device to your computer or start an emulator.
5. Select the device in Android Studio.
6. Click the "Run" button (green arrow) or go to Run > Run 'app'.

The app should be installed and launched on your device/emulator. Note that the app requires a connection to the requires a login.

Structure
The project follows the standard Android app structure. Key packages/components include:

```org.lilith.kabuapp.schedule```: Contains the Activity and related logic for the schedule view.

```org.lilith.kabuapp.api```: Contains the logic for communication with the external API.

```org.lilith.kabuapp.login```: Contains the authentication logic.

```org.lilith.kabuapp.ui```: Contains UI-specific helper classes and adapters.

```res/layout```: XML layout files.

```res/values```: Resources like strings, colors, etc.

---
**Note:**
Check your codestyle with the Checkstyle Plugin and the checkstyle.xml included in the Project before making a Pull request. I use this to keep the code style consistant.

# Privicy Policy / Datenschutzerklärung
[here](https://github.com/Random-user420/kabuApp/blob/03a3f4968481bb9b7f2bf1d6e39d4fefae375c66/PRIVACY.md)

# Warranty
** We don't provide any sort of warranty on this programm **

**contact: lilithtechs@protonmail.com**

## Contributors

<p>Thanks to all the contributors who helped improve this project:</p>
<a href="https://github.com/Random-user420/kabuApp/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Random-user420/kabuApp" />
</a>

# Copyright
For Everything that is comitted under "Random-user420"

(C) 2025 Lilith

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
