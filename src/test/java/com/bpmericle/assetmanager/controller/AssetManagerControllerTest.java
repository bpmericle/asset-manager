package com.bpmericle.assetmanager.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bpmericle.assetmanager.model.AssetDownloadMetadata;
import com.bpmericle.assetmanager.model.AssetStatus;
import com.bpmericle.assetmanager.model.AssetUploadMetadata;
import com.bpmericle.assetmanager.service.AssetManagerService;
import com.bpmericle.assetmanager.service.AssetManagerServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests the {@link AssetManagerController} class.
 *
 * @author Brian Mericle
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AssetManagerController.class)
public class AssetManagerControllerTest {

    private static final String ASSET_ID = UUID.randomUUID().toString().replaceAll("-", "");
    private static final int DEFAULT_TIMEOUT = 60;
    private static final int TIMEOUT = 100;
    private static final String BAD_TIMEOUT = "bad_timeout_value";
    private static final String DEFAULT_STATUS = "uploaded";
    private static final String UPLOAD_ASSET_URL = "s3://xxx";
    private static final String DOWNLOAD_ASSET_URL = "s3://xxx";
    private static final String EXCEPTION_MESSAGE = "A problem has occured";
    private static final AssetManagerServiceException ASSET_MANAGER_SERVICE_EXCEPTION_WITH_MESSAGE = new AssetManagerServiceException(EXCEPTION_MESSAGE);
    private static final AssetManagerServiceException ASSET_MANAGER_SERVICE_EXCEPTION_WITH_MESSAGE_AND_CAUSE = new AssetManagerServiceException(EXCEPTION_MESSAGE, new Exception());
    private static final AssetStatus ASSET_MANAGER_STATUS = new AssetStatus(DEFAULT_STATUS);

    private static final String URI_BASE = "/asset";
    private static final String URI_REQUEST_UPLOAD_ASSET = URI_BASE;
    private static final String URI_SUBMIT_ASSET_STATUS = String.format("%s/%s", URI_BASE, ASSET_ID);
    private static final String URI_REQUEST_DOWNLOAD_ASSET = String.format("%s/%s", URI_BASE, ASSET_ID);
    private static final String PARAM_TIMEOUT = "timeout";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    private AssetManagerService serviceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void requestAssetUpload_validRequest() throws Exception {
        AssetUploadMetadata response = new AssetUploadMetadata(ASSET_ID, UPLOAD_ASSET_URL);
        String jsonResponse = OBJECT_MAPPER.writeValueAsString(response);

        when(serviceMock.requestAssetUpload()).thenReturn(response);
        mockMvc.perform(post(URI_REQUEST_UPLOAD_ASSET))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    public void requestAssetUpload_validRequestWithServiceThrowingException() throws Exception {
        when(serviceMock.requestAssetUpload()).thenThrow(ASSET_MANAGER_SERVICE_EXCEPTION_WITH_MESSAGE_AND_CAUSE);
        mockMvc.perform(get(URI_REQUEST_UPLOAD_ASSET))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void submitAssetStatus_validRequest() throws Exception {
        mockMvc.perform(put(URI_SUBMIT_ASSET_STATUS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(OBJECT_MAPPER.writeValueAsBytes(ASSET_MANAGER_STATUS)))
                .andExpect(status().isOk());
        verify(serviceMock, times(1)).submitAssetUpdateStatus(ASSET_ID, ASSET_MANAGER_STATUS);
    }

    @Test
    public void submitAssetStatus_invalidRequestNoContent() throws Exception {
        mockMvc.perform(put(URI_SUBMIT_ASSET_STATUS))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void submitAssetStatus_validRequestWithServiceThrowingException() throws Exception {
        doThrow(ASSET_MANAGER_SERVICE_EXCEPTION_WITH_MESSAGE_AND_CAUSE).when(serviceMock).submitAssetUpdateStatus(ASSET_ID, ASSET_MANAGER_STATUS);
        mockMvc.perform(put(URI_SUBMIT_ASSET_STATUS)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(OBJECT_MAPPER.writeValueAsBytes(ASSET_MANAGER_STATUS)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void requestAssetDownload_validRequestWithNoExplicitTimeoutSet() throws Exception {
        AssetDownloadMetadata response = new AssetDownloadMetadata(DOWNLOAD_ASSET_URL);
        String jsonResponse = OBJECT_MAPPER.writeValueAsString(response);

        when(serviceMock.requestAssetDownload(ASSET_ID, DEFAULT_TIMEOUT)).thenReturn(response);
        mockMvc.perform(get(URI_REQUEST_DOWNLOAD_ASSET))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    public void requestAssetDownload_validRequestWithExplicitTimeoutSet() throws Exception {
        AssetDownloadMetadata response = new AssetDownloadMetadata(DOWNLOAD_ASSET_URL);
        String jsonResponse = OBJECT_MAPPER.writeValueAsString(response);

        when(serviceMock.requestAssetDownload(ASSET_ID, TIMEOUT)).thenReturn(response);
        mockMvc.perform(get(URI_REQUEST_DOWNLOAD_ASSET)
                .param(PARAM_TIMEOUT, String.valueOf(TIMEOUT)))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    public void requestAssetDownload_invalidRequestWithBadExplicitTimeoutSet() throws Exception {
        mockMvc.perform(get(URI_REQUEST_DOWNLOAD_ASSET)
                .param(PARAM_TIMEOUT, BAD_TIMEOUT))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void requestAssetDownload_validRequestWithInvalidStatus() throws Exception {
        when(serviceMock.requestAssetDownload(ASSET_ID, TIMEOUT)).thenThrow(ASSET_MANAGER_SERVICE_EXCEPTION_WITH_MESSAGE);
        mockMvc.perform(get(URI_REQUEST_DOWNLOAD_ASSET)
                .param(PARAM_TIMEOUT, String.valueOf(TIMEOUT)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void requestAssetDownload_validRequestWithServiceThrowingException() throws Exception {
        when(serviceMock.requestAssetDownload(ASSET_ID, TIMEOUT)).thenThrow(ASSET_MANAGER_SERVICE_EXCEPTION_WITH_MESSAGE_AND_CAUSE);
        mockMvc.perform(get(URI_REQUEST_DOWNLOAD_ASSET)
                .param(PARAM_TIMEOUT, String.valueOf(TIMEOUT)))
                .andExpect(status().is5xxServerError());
    }
}
