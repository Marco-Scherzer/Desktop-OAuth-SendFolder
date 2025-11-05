package com.marcoscherzer.msimplegooglemailer;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public abstract class MFileWatcher {

    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final Map<Path, MObservedFile> fileStates = new ConcurrentHashMap<>();
    private final Set<Path> activeMonitors = ConcurrentHashMap.newKeySet();

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Startet die Überwachung eines Verzeichnisses und verarbeitet Dateiänderungen.
     */
    public void startWatching(Path watchDir) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        watchDir.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);

        pool.submit(() -> {
            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path changedFile = watchDir.resolve((Path) event.context());
                    if (Files.isRegularFile(changedFile) && activeMonitors.add(changedFile)) {
                        MObservedFile monitor = new MObservedFile(changedFile);
                        fileStates.put(changedFile, monitor);
                        pool.submit(monitor);
                    }
                }
                key.reset();
            }
        });

        System.out.println("MWatcher gestartet für: " + watchDir);
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Wird aufgerufen, wenn eine Datei fertig geschrieben und entsperrt ist.
     */
    protected abstract void onFileChangedAndUnlocked(Path file);

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Repräsentiert den Zustand einer Datei und erkennt Stabilität.
     */
    private class MObservedFile implements Runnable {
        private final Path file;
        private long lastSize = -1;
        private long lastModified = -1;

        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * Konstruktor für die Dateiüberwachung.
         */
        public MObservedFile(Path file) {
            this.file = file;
        }

        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * Überwacht eine einzelne Datei, bis sie stabil und nicht mehr gesperrt ist.
         */
        @Override
        public void run() {
            try {
                boolean done = false;
                while (!done) {
                    long size = Files.size(file);
                    long modified = Files.getLastModifiedTime(file).toMillis();
                    boolean locked = isLocked();
                    System.out.println("Datei: " + file.getFileName() + " | Größe: " + size + " | Geändert: " + modified + " | Gesperrt: " + locked);
                    done = !locked && size == lastSize && modified == lastModified;
                    lastSize = size;
                    lastModified = modified;
                    if (!done) Thread.sleep(3000);
                }
                System.out.println("Datei fertig geschrieben: " + file.getFileName());
                activeMonitors.remove(file);
                onFileChangedAndUnlocked(file);
            } catch (Exception e) {
                System.out.println("Fehler bei Dateiüberwachung: " + file + " → " + e.getMessage());
                activeMonitors.remove(file);
            }
        }

        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * Prüft, ob eine Datei aktuell gesperrt ist (z. B. durch Schreibzugriff).
         */
        private boolean isLocked() {
            try (FileChannel channel = FileChannel.open(file, StandardOpenOption.WRITE)) {
                FileLock lock = channel.tryLock();
                if (lock != null) {
                    lock.release();
                    return false;
                }
            } catch (IOException e) {
                return true;
            }
            return true;
        }
    }
}
