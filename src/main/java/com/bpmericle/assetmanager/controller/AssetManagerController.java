package com.bpmericle.assetmanager.controller;

import com.bpmericle.assetmanager.model.AssetUploadMetadata;
import com.bpmericle.assetmanager.model.AssetStatus;
import com.bpmericle.assetmanager.model.AssetDownloadMetadata;
import com.bpmericle.assetmanager.service.AssetManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is the controller responsible for interacting with assets within an
 * asset store.
 *
 * @author Brian Mericle
 */
@RestController
@RequestMapping("/asset")
public class AssetManagerController {

    /**
     * The default timeout (in seconds) the download link will be good for.
     */
    private static final String DEFAULT_TIMEOUT = "60";

    /**
     * The service containing the business logic.
     */
    @Autowired
    private AssetManagerService service;

    /**
     * A request to upload to an asset store.
     *
     * @return metadata required to upload to the asset store.
     */
    @PostMapping
    @ResponseBody
    public AssetUploadMetadata requestAssetUpload() {
        return service.requestAssetUpload();
    }

    @PutMapping("/{id}")
    public void submitAssetUpdateStatus(@PathVariable final String id, 
            @RequestBody(required = true) final AssetStatus status) {
        service.submitAssetUpdateStatus(id, status);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public AssetDownloadMetadata requestAssetDownload(@PathVariable final String id,
            @RequestParam(name = "timeout", defaultValue = DEFAULT_TIMEOUT, required = false) final String timeout) {
        return service.requestAssetDownload(id, Integer.valueOf(timeout));
    }
}
