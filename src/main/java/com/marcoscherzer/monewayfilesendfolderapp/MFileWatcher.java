package com.marcoscherzer.monewayfilesendfolderapp;

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

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MFileWatcher() {
        pool = Executors.newCachedThreadPool();
        fileStates = new ConcurrentHashMap<>();
        activeMonitors = ConcurrentHashMap.newKeySet();
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
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
                        System.err.println("MWatcher thread was interrupted.");
                    } catch (ClosedWatchServiceException exc) {
                        System.err.println("WatchService has been closed.");
                        break;
                    }
                }
            });

            System.out.println("MWatcher started for: \"" + watchDir+"\"");
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
        System.out.println("Shutting down MWatcher...");
        try {
            if (watchService != null) watchService.close();
        } catch (IOException exc) {
            System.err.println("Error closing WatchService:" + exc.getMessage());
            throw new Exception("Error closing WatchService: ", exc);
        }
        pool.shutdown();
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final List<Runnable> shutdownNow() throws Exception {
        System.out.println("Immediately shutting down MWatcher...");
        try {
            if (watchService != null) watchService.close();
        } catch (IOException exc) {
            System.err.println("Error closing WatchService:" + exc.getMessage());
            throw new RuntimeException("Error closing WatchService: ", exc);
        }
        List<Runnable> dropped = pool.shutdownNow();
        System.out.println("Cancelled tasks: " + dropped.size());
        return dropped;
    }

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract void onFileChangedAndUnlocked(Path file);

    /**
     * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private final class MObservedFile implements Runnable {
        private final Path file;
        private long lastSize = -1;
        private long lastModified = -1;

        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private MObservedFile(Path file) {
            this.file = file;
        }

        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        @Override
        public final void run() {
            try {
                boolean done = false;
                while (!done && !Thread.currentThread().isInterrupted()) {
                    long size = Files.size(file);
                    long modified = Files.getLastModifiedTime(file).toMillis();
                    boolean locked = isLocked();

                    done = !locked && size == lastSize && modified == lastModified;
                    lastSize = size;
                    lastModified = modified;

                    if (!done) Thread.sleep(1000);
                }

                if (done) {
                    System.out.println("File fully written: " + file.getFileName());
                    activeMonitors.remove(file);
                    onFileChangedAndUnlocked(file);
                }

            } catch (Exception exc) {
                System.out.println("Error monitoring file: " + file + " → " + exc.getMessage());
                activeMonitors.remove(file);
            }
        }

        /**
         * @author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
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


