
package com.bpmericle.assetmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * Metadata representing how to upload an asset to the asset store.
 *
 * @author brianmericle
 */
public class AssetUploadMetadata implements Serializable {

    @JsonProperty("id")
    private String id;
    
    @JsonProperty("upload_url")
    private String uploadUrl;

    /**
     * Default empty constructor.
     */
    public AssetUploadMetadata() {
        this("", "");
    }

    /**
     * Creates an asset metadata object with an id and an upload url.
     *
     * @param id the identifier for the asset
     * @param uploadUrl the url used to upload the asset
     */
    public AssetUploadMetadata(final String id, final String uploadUrl) {
        this.id = id;
        this.uploadUrl = uploadUrl;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Get the value of uploadUrl
     *
     * @return the value of uploadUrl
     */
    public String getUploadUrl() {
        return uploadUrl;
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
        if (!(obj instanceof AssetUploadMetadata)) {
            return false;
        }
        
        AssetUploadMetadata other = (AssetUploadMetadata) obj;
        return Objects.equals(id, other.id) &&
                Objects.equals(uploadUrl, other.uploadUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, uploadUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("Class: [%s] {id: %s, uploadUrl: %s}", getClass().getSimpleName(), id, uploadUrl);
    }
}
