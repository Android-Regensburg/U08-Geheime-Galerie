# U08 | Geheime-Bildergalerie

## Aufgabe

Fotos, die mit der normalen Kamera-App aufgenommen werden, landen in der Regel in der zentralen Bildergalerie des Android-Geräts und sind von dort für andere Apps zugänglich. In dieser Aufgabe implementieren Sie eine App, die eine geheime Bildergalerie bereitstellt. Über die App können Nutzer\*innen Fotos aufnehmen, die im Anschluss nur über diese App eingesehen werden können. Das _User Interface_ für die Anwendung haben wir Ihnen fast vollständig vorgegeben. Natürlich können Sie das _Design_ aber beliebig erweitert. Ihre eigentliche Aufgabe ist das Implementieren des **Aufnehmens von Bildern** mit der Kamera, das **Speichern dieser Bilder in einer entsprechenden Room-Datenbank**, sowie das **Anzeigen in der RecyclerView der Bildergalerie**. Einen Eindruck der fertigen App erhalten Sie über die Screenshots am Ende dieser Beschreibung.

## Allgemeine Hinweise

### Kameraaufnahmen mit dem Emulator

Damit Sie das Einbinden der Kamera auch auf ihrem Emulator testen und echte Kameraaufnahmen simulieren können, können Sie in den erweiterten Einstellungen des _AVD Managers_ Ihres Emulators beispielsweise die Frontkamera auf _Webcam_ (sofern vorhanden) oder die Backkamera auf _Virtual Scene_ setzen. Die Option _Virtual Scene_ ermöglicht das Navigieren in einer virtuellen Umgebung, in der dann Bilder aufgenommen werden können. Bei gedrückter `ALT`-Taste können Sie sich mit der Maus in der Szene umschauen und mit den Tasten `W`,`A`,`S` und `D` bewegen (auf dem Mac nutzen Sie die `Option`-Taste stastt `ALT`).
**Achtung:** Das Navigieren in der Virtual Scene funktioniert nicht, wenn der Emulator in einem _Tool Window_ von _Android Studio_ gestartet wird. Um den Emulator als eigenständige Anwendung zu starten, muss unter **File -> Setting -> Tools -> Emulator** der **Haken bei "Launch in a tool window" entfernt** werden.

### Übersicht über den bereits implementierter Code

Im Starterpaket finden Sie neben den vorgegebenen Layout-Dateien auch eine Reihe teilweise oder vollständig implementierte Java-Klassen. Dazu gehören die **drei Activties** der App:

- Die **`GalleryActivity`** wird beim Start der Anwendung angezeigt und beinhaltet eine _RecyclerView_, in der alle über die App aufgenommenen Bilder in einer Rasteransicht dargestellt werden. 
- Durch das Anklicken eines der angezeigten Bilder gelangen die Nutzer\*innen zur  **`DetailActivity`**, in der das Bild und sein Beschreibungstext vergrößert angezeigt wird.
- Über den _Floating Action Button_ der `GalleryActivity` erreichen die Nutzer\*innen die **`CreationActivity`**. In dieser soll ein Bild aufgenommen und anschließend mit einem Beschreibungstext versehen werden. Das eigentliche Aufnehmen der Bildern implementieren wir nicht selbst. Stattdessen nutzen wird die Möglichkeit, per Intent eine andere installierte Kamera-App aufzurufen und das von dieser erstellte Bild in unserer App zu verarbeiten.

Auch für die Verwaltung der Bilder innerhalb der Anwendung stehen bereits einige Klassen zur Verfügung:
- **`SecretImage`** stellt einen einzelnen Eintrag in unserer Galerie dar. Beim Erstellen eines neuen Objektes dieser Klasse wird das im Konstruktor übergebene Bitmap über eine bereits implementierte Methode im internen Speicher der App gespeichert. Statt dem Bild wird dann nur der Pfad zum Speicherort als Klassenvariable hinterlegt. Um das jeweilige Bild zu laden kann die `getBitmap()` Methode verwendet werden, die basierend auf dem Pfad das Bild wieder aus dem Speicher lädt. 
- Der **`SecretImageManager`** dient der Abtrennung von Daten und UI. Er verwaltet die Daten und gibt diese nach außen. Mit Hilfe des `SecretImageManagerListener`-Interface gibt der `SecretImageManager` bei Änderung der Daten dem _Listener_ Bescheid.

## Vorgehen

### Starten der Kamera und empfangen des aufgenommenen Bildes
Sorgen Sie dafür, dass beim Klick auf den _Floating Action Button_ die `CreationActivity` aufgerufen wird. Implementieren Sie dort die Aufnahme eines neuen Fotos mit einer anderen auf dem Gerät installierten Kamera-App. Der Aufruf externer Apps erfolgt dabei, indem Sie einen `ActivityResultLauncher` anlegen. Dies erreichen Sie über folgenden Aufruf:
```
ActivityResultLauncher launcher(new ActivityResultContracts.startActivityForResult(), new ActivityResultCallback<ActivityResult>() {
    @Override
    public void onActivityResult(ActivityResult result) {
        //Hier können Sie das result verarbeiten
    }
});
```
Um von einer anderen Activity ein Result zu erhalten starten Sie diese mit dem ActivityResultLauncher mit dem Code `launcher.launch(Intent intent)`.
Die Erstellung des Intent-Objekts variiert hier jedoch je nach Anwendungsfall. Für die Erzeugung des Intents zum Aufruf der Kamera können Sie sich an den Codebeispielen der Vorlesung oder des weiter unten verlinkten Guides orientieren. Im verlinkten Guide finden Sie außerdem eine Anleitung, wie Sie einen `FileProvider` im Manifest registrieren.
Ob die Aufnahme erfolgreich abgeschlossen ist, können Sie aus dem `result` des implementierten ActivityResultLaunchers mit `result.getResultCode()` auslesen. Versuchen Sie hier auch eine sinnvolle Lösung für den Fall zu implementieren, dass die Nutzer\*innen die Kamera-App schließen, ohne ein Bild aufgenommen zu haben. 
Wurde das Bild erfolgreich aufgenommen, so soll es in der `ImageView` der Activity angezeigt werden und von den Nutzer\*innen mit einer Beschreibung versehen werden. Beim Klick auf den Speicher-Button soll basierend aus den erhobenen Daten ein Objekt der Klasse `SecretImage` erstellt und an die `GalleryActivity` zurückgegeben werden, um es dann dort anzeigen zu lassen.

Eine gute Anlaufstelle zur Einbindung der Kamera ist [in diesem Guide](https://developer.android.com/training/camera/photobasics#TaskCaptureIntent) zu finden. 
Eine ausführlichere Erklärung zum ActivityResultLauncher gibt es [in diesem Guide](https://developer.android.com/training/basics/intents/result). Beachten Sie hier, dass der für uns relevante ActivityResultContract `ActivityResultContracts.StartActivityForResult()` ist.

### Bilder in der RecyclerView anzeigen

Stellen Sie sicher, dass die aufgerufenen `CreationActivity` das neue Bild als Ergebnis zurückgibt und verarbeiten Sie dieses in der `GalleryActivity`. Die neu erstellten Bilder übergeben Sie dort dem `SecretImageManager` übergeben. Durch die bereits vorhandene Implementierung wird dabei auch automatisch der Adapter des `RecyclerView` informiert. Damit die (neuen) Bilder aber auch im _User Inteface_ angezeigt werden, müssen Sie den verwendeten _View Holder_ (`SecretImageViewHolder`) vervollständigen. In dessen `bindViews()` Methode soll das Bild im vorbereiteten *ImageView* angezeigt werden. Außerdem besteht hier die Möglichkeit, einzelne Views mit einem Klick-Listener zu versehen (siehe nächster Punkt). 

### Detailansicht

Klicks auf einzelne Bilder der Galerie sollen dafür sorgen, dass die `DetailActivity` aufgerufen wird. Hier wird das ausgewählte Bild dann angezeigt. Damit das Bild dort dargestellt werden kann, muss dem jeweiligen Intent das angeklickt `SecretImage`-Objekt übergeben werden. 

### Room Datenbank implementieren, um Bilder dauerhaft zu speichern

Damit unsere _SecretImages_ dauerhaft erhalten bleiben, sollen Sie in einer Room-Datenbank gespeichert werden. Annotieren Sie also die entsprechend zu speichernde Entität und Erstellen sie das _DataAccessObject_, die Datenbank und eine passende _Helper_-Klasse. Binden Sie anschließend die Datenbank an den entsprechenden Stellen an die restliche App an. Denken Sie daran, Ihre Datenbankoperationen **nicht auf dem UI-Thread** laufen zu lassen, sondern diese alle in eigene Threads auslagern.

Sie können sich hierbei am Lösungsvorschlag der Datenbank-Implementierung der [5. Übungsaufgabe](https://android-regensburg.github.io/AssignmentViewer/index.html#Android-Regensburg/U05-Persistente-ToDo-Liste) orientieren. 


## Screenshots der Anwendung

![Screenshots der geheimen-Galerie-App](./docs/screenshots.png)
