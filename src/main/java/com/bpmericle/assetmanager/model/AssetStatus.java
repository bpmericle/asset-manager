package com.bpmericle.assetmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the status of an asset with respect to it asset store.
 *
 * @author Brian Mericle
 */
public class AssetStatus implements Serializable {

    @JsonProperty("Status")
    private String status;

    /**
     * Default empty constructor.
     */
    public AssetStatus() {
        this("");
    }

    /**
     * Constructs a status with a specific value.
     *
     * @param status the status of the asset.
     */
    public AssetStatus(final String status) {
        this.status = status;
    }

    /**
     * Get the value of status
     *
     * @return the value of status
     */
    public String getStatus() {
        return status;
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
        if (!(obj instanceof AssetStatus)) {
            return false;
        }

        AssetStatus other = (AssetStatus) obj;
        return Objects.equals(status, other.status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("Class: [%s] {status: %s}", getClass().getSimpleName(), status);
    }

}
