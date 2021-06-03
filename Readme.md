# U08 | Geheime-Bildergalerie

## Aufgabe

In dieser Aufgabe implementieren Sie eine Bilder-Galerie-App, in der neu aufgenommene Fotos abgelegt werden können, auf die Sie nur über die App zugreifen können. Das _User Interface_ der Anwendung haben wir bereits für Sie vorbereitet, kann aber beliebig von Ihnen erweitert werden. Ihre Aufgabe ist das Implementieren des Aufnehmens von Bildern mit der Kamera, das Speichern in einer entsprechenden Room-Datenbank, sowie das Anzeigen in der RecyclerView der Bildergalerie. Einen Eindruck der fertigen App erhalten Sie über die Screenshots am Ende dieser Beschreibung.

## Allgemeine Hinweise

### Kameraaufnahmen mit dem Emulator

Damit Sie das Einbinden der Kamera auch auf ihrem Emulator testen und echte Kameraaufnahmen simulieren können, können Sie in den erweiterten Einstellungen des AVD Managers ihres Emulators beispielsweise die Frontkamera auf "Webcam" (sofern vorhanden) oder die Back-Kamera auf "Virtual Scene" setzen. Die Option Virtual Scene ermöglicht das Navigieren in einer virtuellen Umgebung, in der dann Bilder aufgenommen werden können. Mit `ALT + Maus` können Sie sich umsehen und mit `ALT + WASD` können Sie sich in der Szene bewegen. (Bei MAC Nutzern dementsprechend `Option + Maus` bzw. `Option + WASD`)
Achtung: Das Navigieren in der Virtual Scene funktioniert nicht, wenn der Emulator in einem Tool Window von Android Studio gestartet wird. Um den Emulator als eigenständige Anwendung zu starten, muss unter **File -> Setting -> Tools -> Emulator** der **Haken bei "Launch in a tool window" entfernt** werden.

### Bereits implementierter Code

Im Starterpacket finden sie neben dem vorgegebenen Layout auch bereits implementierte Klassen. Die App besteht aus drei Activities. Die **GalleryActivity** beinhaltet eine RecyclerView, in der die aufgenommenen Bilder in einer Rasteransicht dargestellt werden. Wird auf eines der Bilder geklickt, so soll man zur **DetailActivity** gelangen, in der das Bild und sein Beschreibungstext vergrößert angezeigt wird. Um ein neues Bild zur Galerie hinzuzufügen, wird man über den FloatingActionButton zur **CreationActivity** weitergeleitet. In dieser soll mithilfe der Kamera ein Bild aufgenommen und anschließend mit einem Beschreibungstext versehen werden. Das Aufnehmen von Bildern soll dabei nicht selbst implementiert werden. Stattdessen wird die (jeweils auf dem System installierte) Kamera-App von Android ein Bild an die eigene App liefern.

Die Klasse **SecretImage** stellt einen einzelnen Eintrag in unserer Galerie dar. Beim Erstellen eines neuen Objektes dieser Klasse wird das im Konstruktor übergebene Bitmap über eine bereits implementierte Methode im internen Speicher der App gespeichert. Statt dem Bild wird dann nur der Pfad zum Speicherort als Klassenvariable hinterlegt. Um das jeweilige Bild zu laden kann die getBitmap() Methode verwendet werden, die basierend auf dem Pfad das Bild wieder aus dem Speicher lädt. 

Der **SecretImageManager** dient der Abtrennung von Daten und UI. Er verwaltet die Daten und gibt diese nach außen. Über ein Interface gibt der SecretImageManager bei Änderung der Daten dem Listener Bescheid.

## Vorgehen

### Starten der Kamera und empfangen des aufgenommenen Bildes
Implementieren Sie zunächst das Aufrufen der bereits auf dem System installierten Kamera-App in der CreationActivity. Der Aufruf externer Apps erfolgt dabei, indem Sie einen entsprechenden Intent gemeinsam mit einem selbstgewählten request code an die Methode startActivityForResult(Intent intent, in requestCode) übergeben. Die Erstellung des Intent-Objekts variiert hier jedoch je nach Anwendungsfall. Für die Erzeugung des Intents zum Aufruf der Kamera können Sie sich an den Codebeispielen der Vorlesung oder des weiter unten verlinkten Guides orientieren. Im verlinkten Guide finden sie außerdem eine Anleitung, wie Sie einen `FileProvider` im Manifest registrieren.
Das aufgenommene Bild können dann im übergebenen Intent der überschriebenen onActivityResult() Methode erhalten. Versuchen Sie zudem eine sinnvolle Lösung für den Fall, dass der Nutzer kein Bild aufnimmt und die Kamera einfach schließt, zu implementieren. 
Wurde das Bild erfolgreich aufgenommen, so soll es in der ImageView angezeigt werden und kann mit einer Beschreibung versehen werden. Beim Klick auf den Speicher-Button soll basierend auf den erhobenen Daten ein Objekt der Klasse SecretImage erstellt und an die GalleryActivity zurückgegeben werden, um es dann dort anzeigen zu lassen. 

Eine gute Anlaufstelle zur Einbindung der Kamera ist in diesem Guide zu finden: https://developer.android.com/training/camera/photobasics

### Bilder in der RecyclerView anzeigen

In der bindViews() Methode des SecretImageViewHolders können die angezeigten Elemente der RecyclerView mit den entsprechenden Daten versehen werden. Außerdem besteht hier die Möglichkeit, einzelne Views mit einem Klick-Listener zu versehen, über den man dann in die DetailActivity gelangt, um eine detailliertere Ansicht über das Bild zu bekommen. Damit in der DetailActivity das UI entsprechend geladen werden kann, muss via Intent das angeklickt SecretImage Objekt übergeben werden. 

### Room Datenbank implementieren um Bilder dauerhaft zu speichern
Damit unsere SecretImages dauerhaft erhalten bleiben, sollen sie in einer Room-Datenbank gespeichert werden. Annotieren Sie also die entsprechend zu speichernde Entität und Erstellen sie das DataAccessObject, die Datenbank und eine Datenbank-Helfer Klasse. Binden Sie anschließend die Datenbank an den entsprechenden Stellen an die restliche App an.

Sie können sich hierbei am Lösungsvorschlag der Datenbank-Implementierung der [5. Übungsaufgabe](https://android-regensburg.github.io/AssignmentViewer/index.html#Android-Regensburg/U05-Persistente-ToDo-Liste) orientieren. 


## Screenshots der Anwendung

![Screenshots der geheimen-Galerie-App](./docs/screenshots.png)
