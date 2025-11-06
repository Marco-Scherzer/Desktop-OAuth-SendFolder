package com.marcoscherzer.msimplegooglemailer;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public abstract class MFileWatcher {

    private final ExecutorService pool;
    private final Map<Path, MObservedFile> fileStates;
    private final Set<Path> activeMonitors;
    private WatchService watchService;


    MFileWatcher(){
           pool = Executors.newCachedThreadPool();
           fileStates = new ConcurrentHashMap<>();
           activeMonitors = ConcurrentHashMap.newKeySet();
    }
    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Startet die Überwachung eines Verzeichnisses und verarbeitet Dateiänderungen.
     */
    public final boolean startWatching(Path watchDir) {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            watchDir.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);

            pool.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
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
                    } catch (InterruptedException exc) {
                        Thread.currentThread().interrupt();
                        System.out.println("MWatcher-Thread wurde unterbrochen.");
                    } catch (ClosedWatchServiceException exc) {
                        System.out.println("WatchService wurde geschlossen.");
                        break;
                    }
                }
            });

            System.out.println("MWatcher gestartet für: " + watchDir);
            return true;
        } catch (IOException exc) {
            System.out.println("Fehler beim Starten von MWatcher: " + exc.getMessage());
            return false;
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Beendet den Threadpool sanft und schließt den WatchService.
     */
    public void shutdown() throws Exception{
        System.out.println("MWatcher wird heruntergefahren...");
        try {
            if (watchService != null) watchService.close();
        } catch (IOException exc) {
            throw new RuntimeException("Fehler beim Schließen des WatchService: ", exc);
        }
        pool.shutdown();
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Beendet den Threadpool sofort und schließt den WatchService.
     */
    public List<Runnable> shutdownNow() throws Exception{
        System.out.println("MWatcher wird sofort heruntergefahren...");
        try {
            if (watchService != null) watchService.close();
        } catch (IOException exc) {
            throw new RuntimeException("Fehler beim Schließen des WatchService: ", exc);
        }
        List<Runnable> dropped = pool.shutdownNow();
        System.out.println("Abgebrochene Tasks: " + dropped.size());
        return dropped;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Wird aufgerufen, wenn eine Datei fertig geschrieben und entsperrt ist.
     */
    protected abstract void onFileChangedAndUnlocked(Path file);

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Beobachtet eine einzelne Datei bis zur Stabilität.
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
        public final void run() {
            try {
                boolean done = false;
                while (!done && !Thread.currentThread().isInterrupted()) {
                    long size = Files.size(file);
                    long modified = Files.getLastModifiedTime(file).toMillis();
                    boolean locked = isLocked();

                   /* System.out.println("Datei: " + file.getFileName() +
                            " | Größe: " + size +
                            " | Geändert: " + modified +
                            " | Gesperrt: " + locked);
                   */
                    done = !locked && size == lastSize && modified == lastModified;
                    lastSize = size;
                    lastModified = modified;

                    if (!done) Thread.sleep(1000);
                }

                if (done) {
                    System.out.println("Datei fertig geschrieben: " + file.getFileName());
                    activeMonitors.remove(file);
                    onFileChangedAndUnlocked(file);
                }

            } catch (Exception exc) {
                System.out.println("Fehler bei Dateiüberwachung: " + file + " → " + exc.getMessage());
                activeMonitors.remove(file);
            }
        }

        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * Prüft, ob eine Datei aktuell gesperrt ist (z.B. durch Schreibzugriff).
         */
        private boolean isLocked() {
            try (FileChannel channel = FileChannel.open(file, StandardOpenOption.WRITE)) {
                FileLock lock = channel.tryLock();
                if (lock != null) {
                    lock.release();
                    return false;
                }
            } catch (IOException exc) {
                return true;
            }
            return true;
        }
    }
}

