package com.marcoscherzer.oauthfilesendfolderforgooglemail;

import java.nio.file.*;
import java.util.concurrent.*;
import static java.nio.file.StandardWatchEventKinds.*;
/**
 *@author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MFileNameWatcher {

    /**
     *@author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private final WatchService watcher;
    private final Path directory;
    private final String fileName;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running = true;

    /**
     *@author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MFileNameWatcher(Path filePath) throws Exception {
        this.directory = filePath.getParent();
        this.fileName = filePath.getFileName().toString();
        this.watcher = FileSystems.getDefault().newWatchService();
        directory.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        System.out.println("Überwache Dateinamen…");
        startWatching();
    }

    /**
     *@author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void startWatching() {
        executor.submit(() -> {
            try {
                while (running) {
                    WatchKey key = watcher.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path changed = (Path) event.context();
                        if (changed.toString().equals(fileName)) {
                            System.out.println("Namensänderung erkannt: " + event.kind() + " → " + changed);
                        }
                    }
                    key.reset();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     *@author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void shutdown() {
        running = false;
        try {
            watcher.close();
            executor.shutdownNow();
            System.out.println("Watcher sauber beendet.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
