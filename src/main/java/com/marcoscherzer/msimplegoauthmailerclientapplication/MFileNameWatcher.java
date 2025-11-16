package com.marcoscherzer.msimplegoauthmailerclientapplication;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.*;
import static java.nio.file.StandardWatchEventKinds.*;

/** @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved */
public abstract class MFileNameWatcher {

    private WatchService watcher;
    private final Path directory;
    private final String fileName;
    private final ExecutorService pool = Executors.newSingleThreadExecutor();
    private final Path filePath;
    private volatile boolean running;

    /** @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved */
    public MFileNameWatcher(Path filePath) throws Exception {
        this.filePath = filePath;
        this.directory = filePath.getParent();
        this.fileName = filePath.getFileName().toString();
    }
    /** @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved */
    public final boolean startWatching() {
        try{
        if(!running){
            this.watcher = FileSystems.getDefault().newWatchService();
            directory.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            pool.submit(() -> {
                running=true;
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            WatchKey key = watcher.take();
                            for (WatchEvent<?> event : key.pollEvents()) {
                                Path changed = (Path) event.context();
                                if (changed.toString().equals(fileName)) {
                                    onFileNameChanged(event.kind(), changed);
                                }
                            }
                            key.reset();
                        } catch (InterruptedException exc) {
                            Thread.currentThread().interrupt();
                            System.err.println("MWatcher thread was interrupted.");
                        } catch (ClosedWatchServiceException exc) {
                            System.err.println("WatchService has been closed.");
                            break;
                        }
                    }
            });
        }
            System.out.println("MWatcher started for: \"" + fileName+"\"");
            return true;
        } catch (IOException exc) {
            System.err.println("Error starting MWatcher: " + exc.getMessage());
            return false;
        }
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void shutdown() throws Exception {
        System.out.println("Shutting down FileNameWatcher for \""+filePath+"\" ...");
        try {
            if (watcher != null) watcher.close();
        } catch (IOException exc) {
            System.err.println("Error closing WatchService:" + exc.getMessage());
            throw new Exception("Error closing WatchService: ", exc);
        }
        pool.shutdown();
    }

    /**
     *  @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     *     Wird bei Dateinamenänderung aufgerufen
     * */
    protected abstract void onFileNameChanged(WatchEvent.Kind<?> kind, Path fileName);
}

