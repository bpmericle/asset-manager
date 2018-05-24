package com.bpmericle.assetmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * Metadata representing how to download an asset from the asset store.
 *
 * @author Brian Mericle
 */
public class AssetDownloadMetadata implements Serializable {

    @JsonProperty("Download_url")
    private String downloadUrl;

    /**
     * Default empty constructor.
     */
    public AssetDownloadMetadata() {
        this("");
    }

    /**
     * Creates an asset metadata object with download url.
     *
     * @param downloadUrl the url used to download the asset
     */
    public AssetDownloadMetadata(final String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     * Get the value of downloadUrl
     *
     * @return the value of downloadUrl
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AssetDownloadMetadata)) {
            return false;
        }

        AssetDownloadMetadata other = (AssetDownloadMetadata) obj;
        return Objects.equals(downloadUrl, other.downloadUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(downloadUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("Class: [%s] {downloadUrl: %s}", getClass().getSimpleName(), downloadUrl);
    }

}
