package com.marcoscherzer.msimplegooglemailer;

import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public abstract class MFileWatcherWithDelayHandling {

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Startet den Watcher für ein bestimmtes Verzeichnis mit konfigurierbarer Verzögerung.
     * @param watchPath Pfad zum zu überwachenden Verzeichnis
     * @param changeHandlingDelayMs Zeit in Millisekunden, die nach einer Änderung gewartet wird
     * @throws IOException falls das WatchService nicht initialisiert werden kann
     */
    public void startWatching(Path watchPath, long changeHandlingDelayMs) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        watchPath.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);

        System.out.println("Watcher gestartet für: " + watchPath + " mit Delay: " + changeHandlingDelayMs + "ms");

        while (true) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                System.out.println("Watcher unterbrochen.");
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == OVERFLOW) continue;

                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path filename = ev.context();
                Path fullPath = watchPath.resolve(filename);

                System.out.println("Änderung erkannt: " + fullPath);
                new Thread(() -> {
                    try {
                        Thread.sleep(changeHandlingDelayMs);
                        onFileChanged(fullPath);
                    } catch (InterruptedException ignored) {
                    }
                }).start();
            }

            boolean valid = key.reset();
            if (!valid) {
                System.out.println("WatchKey ungültig – Beende Watcher.");
                break;
            }
        }
    }

    /**
     * * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Wird nach der Verzögerung aufgerufen, um die Datei zu verarbeiten.
     * @param file Die erkannte Datei
     */
    protected abstract void onFileChanged(Path file);
}
