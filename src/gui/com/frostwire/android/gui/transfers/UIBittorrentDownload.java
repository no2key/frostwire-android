package com.frostwire.android.gui.transfers;

import com.frostwire.bittorrent.BTDownload;
import com.frostwire.bittorrent.BTDownloadListener;
import com.frostwire.logging.Logger;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author gubatron
 * @author aldenml
 */
public final class UIBittorrentDownload implements BittorrentDownload {

    private static final Logger LOG = Logger.getLogger(UIBittorrentDownload.class);

    private final TransferManager manager;
    private final BTDownload dl;

    private String displayName;
    private long size;

    public UIBittorrentDownload(TransferManager manager, BTDownload dl) {
        this.manager = manager;
        this.dl = dl;
        this.dl.setListener(new StatusListener());

        this.displayName = dl.getDisplayName();
        this.size = calculateSize(dl);

        if (!dl.wasPaused()) {
            dl.resume();
        }
    }

    @Override
    public String getHash() {
        return dl.getInfoHash();
    }

    @Override
    public String getPeers() {
        int connectedPeers = dl.getConnectedPeers();
        int peers = dl.getTotalPeers();

        String tmp = connectedPeers > peers ? "%1" : "%1 " + "/" + " %2";

        tmp = tmp.replaceAll("%1", String.valueOf(connectedPeers));
        tmp = tmp.replaceAll("%2", String.valueOf(peers));

        return tmp;
    }

    @Override
    public String getSeeds() {
        int connectedSeeds = dl.getConnectedSeeds();
        int seeds = dl.getTotalSeeds();

        String tmp = connectedSeeds > seeds ? "%1" : "%1 " + "/" + " %2";

        tmp = tmp.replaceAll("%1", String.valueOf(connectedSeeds));
        String param2 = "?";
        if (seeds != -1) {
            param2 = String.valueOf(seeds);
        }
        tmp = tmp.replaceAll("%2", param2);

        return tmp;
    }

    @Override
    public boolean isResumable() {
        return dl.isPaused();
    }

    @Override
    public boolean isPausable() {
        return !dl.isPaused();
    }

    @Override
    public boolean isSeeding() {
        return dl.isSeeding();
    }

    @Override
    public void enqueue() {

    }

    @Override
    public void pause() {
        dl.pause();
    }

    @Override
    public void resume() {
        dl.resume();
    }

    @Override
    public List<? extends BittorrentDownloadItem> getBittorrentItems() {
        // TODO:BITTORRENT
        return null;
    }

    @Override
    public File getSavePath() {
        return dl.getSavePath();
    }

    @Override
    public boolean isDownloading() {
        return dl.isDownloading();
    }

    @Override
    public void cancel(boolean deleteData) {
        // TODO:BITTORRENT
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getStatus() {
        return dl.getState().toString();
    }

    @Override
    public int getProgress() {
        return dl.getProgress();
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public Date getDateCreated() {
        return dl.getCreated();
    }

    @Override
    public long getBytesReceived() {
        return dl.getBytesReceived();
    }

    @Override
    public long getBytesSent() {
        return dl.getBytesSent();
    }

    @Override
    public long getDownloadSpeed() {
        return dl.getDownloadSpeed();
    }

    @Override
    public long getUploadSpeed() {
        return dl.getUploadSpeed();
    }

    @Override
    public long getETA() {
        return dl.getETA();
    }

    @Override
    public boolean isComplete() {
        return dl.isComplete();
    }

    @Override
    public List<? extends TransferItem> getItems() {
        // TODO:BITTORRENT
        return null;
    }

    @Override
    public void cancel() {
        // TODO:BITTORRENT
    }

    @Override
    public String getDetailsUrl() {
        return null;
    }

    private class StatusListener implements BTDownloadListener {

        @Override
        public void update(BTDownload dl) {
            displayName = dl.getDisplayName();
            size = calculateSize(dl);
        }

        @Override
        public void finished(BTDownload dl) {
            // TODO:BITTORRENT
            /*
            if (!SharingSettings.SEED_FINISHED_TORRENTS.getValue() || (dl.isPartial() && !SharingSettings.SEED_HANDPICKED_TORRENT_FILES.getValue())) {
                dl.pause();
            }

            File saveLocation = dl.getSavePath();

            if (iTunesSettings.ITUNES_SUPPORT_ENABLED.getValue() && !iTunesMediator.instance().isScanned(saveLocation)) {
                if ((OSUtils.isMacOSX() || OSUtils.isWindows())) {
                    iTunesMediator.instance().scanForSongs(saveLocation);
                }
            }

            if (!LibraryMediator.instance().isScanned(dl.hashCode())) {
                LibraryMediator.instance().scan(dl.hashCode(), saveLocation);
            }

            //if you have to hide seeds, do so.
            GUIMediator.safeInvokeLater(new Runnable() {
                public void run() {
                    BTDownloadMediator.instance().updateTableFilters();
                }
            });
            */
        }

        @Override
        public void removed(BTDownload dl, Set<File> incompleteFiles) {
            // TODO:BITTORRENT
            //finalCleanup(incompleteFiles);
        }
    }

    private long calculateSize(BTDownload dl) {
        long size = dl.getSize();

        boolean partial = dl.isPartial();
        if (partial) {
            List<com.frostwire.transfers.TransferItem> items = dl.getItems();

            long totalSize = 0;
            for (com.frostwire.transfers.TransferItem item : items) {
                if (!item.isSkipped()) {
                    totalSize += item.getSize();
                }
            }

            if (totalSize > 0) {
                size = totalSize;
            }
        }

        return size;
    }
}
