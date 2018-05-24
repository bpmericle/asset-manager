package com.bpmericle.assetmanager.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectTaggingRequest;
import com.amazonaws.services.s3.model.GetObjectTaggingResult;
import com.amazonaws.services.s3.model.SetObjectTaggingRequest;
import com.amazonaws.services.s3.model.SetObjectTaggingResult;
import com.amazonaws.services.s3.model.Tag;
import com.bpmericle.assetmanager.model.AssetDownloadMetadata;
import com.bpmericle.assetmanager.model.AssetStatus;
import com.bpmericle.assetmanager.model.AssetUploadMetadata;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests the {@link AssetManagerService} class.
 *
 * @author Brian Mericle
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AssetManagerServiceTest {

    private static final String ASSET_ID = UUID.randomUUID().toString().replaceAll("-", "");
    private static final int TIMEOUT = 100;
    private static final String DEFAULT_STATUS = "uploaded";
    private static final String TAG_STATUS_KEY = "Status";
    private static final String TAG_STATUS_VALUE_UPLOADED = "uploaded";
    private static final String TAG_STATUS_KEY_BAD = "bad_key";
    private static final String TAG_STATUS_VALUE_BAD = "bad_status";
    private static final String URL_VALUE = "https://s3.aws.com";
    private static final AmazonServiceException AMAZON_SERVICE_EXCEPTION = new AmazonServiceException("A problem has occurred!");
    private static final SdkClientException SDK_CLIENT_EXCEPTION = new SdkClientException("A problem has occurred!");
    private static final AssetStatus ASSET_MANAGER_STATUS = new AssetStatus(DEFAULT_STATUS);

    private static URL uploadURL;
    private static URL downloadURL;
    private static List<Tag> validTags;
    private static List<Tag> badKeyTags;
    private static List<Tag> badValueTags;
    private static List<Tag> badKeyAndValueTags;
    
    @Autowired
    private AssetManagerService service;

    @MockBean
    private AmazonS3 amazonS3;
    
    @MockBean
    private SetObjectTaggingResult setObjectTaggingResult;
    
    @MockBean
    private GetObjectTaggingResult getObjectTaggingResult;

    @BeforeClass
    public static void setupClass() throws Exception {
        uploadURL = new URL(URL_VALUE);
        downloadURL = new URL(URL_VALUE);
        
        validTags = new ArrayList<>();
        validTags.add(new Tag(TAG_STATUS_KEY, TAG_STATUS_VALUE_UPLOADED));
        
        badValueTags = new ArrayList<>();
        badValueTags.add(new Tag(TAG_STATUS_KEY, TAG_STATUS_VALUE_BAD));
        
        badKeyTags = new ArrayList<>();
        badKeyTags.add(new Tag(TAG_STATUS_KEY_BAD, TAG_STATUS_VALUE_UPLOADED));
        
        badKeyAndValueTags = new ArrayList<>();
        badKeyAndValueTags.add(new Tag(TAG_STATUS_KEY_BAD, TAG_STATUS_VALUE_BAD));
    }
    
    @Before
    public void setup() throws Exception {
        service.setS3Client(amazonS3);
    }

    @Test
    public void requestAssetUpload_validRequest() throws Exception {
        when(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class))).thenReturn(uploadURL);
        AssetUploadMetadata actual = service.requestAssetUpload();
        verify(amazonS3, times(1)).generatePresignedUrl(any(GeneratePresignedUrlRequest.class));
        
        assertEquals(actual.getUploadUrl(), uploadURL.toString());
    }

    @Test(expected=AssetManagerServiceException.class)
    public void requestAssetUpload_validRequestThrowsAmazonServiceException() throws Exception {
        requestAssetUpload_validRequestThrowsException(AMAZON_SERVICE_EXCEPTION);
    }

    @Test(expected=AssetManagerServiceException.class)
    public void requestAssetUpload_validRequestThrowsSdkClientException() throws Exception {
        requestAssetUpload_validRequestThrowsException(SDK_CLIENT_EXCEPTION);
    }
    
    private void requestAssetUpload_validRequestThrowsException(final Exception ex) throws Exception {
        when(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class))).thenThrow(ex);
        service.requestAssetUpload();
        verify(amazonS3, times(1)).generatePresignedUrl(any(GeneratePresignedUrlRequest.class));
    }

    @Test
    public void submitAssetUpdateStatus_validRequest() throws Exception {
        when(amazonS3.setObjectTagging(any(SetObjectTaggingRequest.class))).thenReturn(setObjectTaggingResult);
        service.submitAssetUpdateStatus(ASSET_ID, ASSET_MANAGER_STATUS);
        verify(amazonS3, times(1)).setObjectTagging(any(SetObjectTaggingRequest.class));
    }

    @Test(expected=AssetManagerServiceException.class)
    public void submitAssetUpdateStatus_validRequestThrowsAmazonServiceException() throws Exception {
        submitAssetUpdateStatus_validRequestThrowsException(AMAZON_SERVICE_EXCEPTION);
    }

    @Test(expected=AssetManagerServiceException.class)
    public void submitAssetUpdateStatus_validRequestThrowsSdkClientException() throws Exception {
        submitAssetUpdateStatus_validRequestThrowsException(SDK_CLIENT_EXCEPTION);
    }
    
    private void submitAssetUpdateStatus_validRequestThrowsException(final Exception ex) throws Exception {
        when(amazonS3.setObjectTagging(any(SetObjectTaggingRequest.class))).thenThrow(ex);
        service.submitAssetUpdateStatus(ASSET_ID, ASSET_MANAGER_STATUS);
        verify(amazonS3, times(1)).setObjectTagging(any(SetObjectTaggingRequest.class));
    }

    @Test
    public void requestAssetDownload_validRequest() throws Exception {
        when(amazonS3.getObjectTagging(any(GetObjectTaggingRequest.class))).thenReturn(getObjectTaggingResult);
        when(getObjectTaggingResult.getTagSet()).thenReturn(validTags);
        when(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class))).thenReturn(downloadURL);
        AssetDownloadMetadata actual = service.requestAssetDownload(ASSET_ID, TIMEOUT);
        verify(amazonS3, times(1)).getObjectTagging(any(GetObjectTaggingRequest.class));
        verify(getObjectTaggingResult, times(1)).getTagSet();
        verify(amazonS3, times(1)).generatePresignedUrl(any(GeneratePresignedUrlRequest.class));
        
        assertEquals(actual.getDownloadUrl(), downloadURL.toString());
    }

    @Test(expected=AssetManagerServiceException.class)
    public void requestAssetDownload_validRequestWithBadTagKey() throws Exception {
        requestAssetDownload_validRequestWithBadTags(badKeyTags);
    }

    @Test(expected=AssetManagerServiceException.class)
    public void requestAssetDownload_validRequestWithBadTagValue() throws Exception {
        requestAssetDownload_validRequestWithBadTags(badValueTags);
    }

    @Test(expected=AssetManagerServiceException.class)
    public void requestAssetDownload_validRequestWithBadTags() throws Exception {
        requestAssetDownload_validRequestWithBadTags(badKeyAndValueTags);
    }
    
    private void requestAssetDownload_validRequestWithBadTags(final List<Tag> tags) throws Exception {
        when(amazonS3.getObjectTagging(any(GetObjectTaggingRequest.class))).thenReturn(getObjectTaggingResult);
        when(getObjectTaggingResult.getTagSet()).thenReturn(tags);
        service.requestAssetDownload(ASSET_ID, TIMEOUT);
        verify(amazonS3, times(1)).getObjectTagging(any(GetObjectTaggingRequest.class));
        verify(getObjectTaggingResult, times(1)).getTagSet();
    }

    @Test(expected=AssetManagerServiceException.class)
    public void requestAssetDownload_validRequestThrowsAmazonServiceException() throws Exception {
        requestAssetDownload_validRequestThrowsException(AMAZON_SERVICE_EXCEPTION);
    }

    @Test(expected=AssetManagerServiceException.class)
    public void requestAssetDownload_validRequestThrowsSdkClientException() throws Exception {
        requestAssetDownload_validRequestThrowsException(SDK_CLIENT_EXCEPTION);
    }
    
    private void requestAssetDownload_validRequestThrowsException(final Exception ex) throws Exception {
        when(amazonS3.getObjectTagging(any(GetObjectTaggingRequest.class))).thenReturn(getObjectTaggingResult);
        when(getObjectTaggingResult.getTagSet()).thenReturn(validTags);
        when(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class))).thenThrow(ex);
        service.requestAssetDownload(ASSET_ID, TIMEOUT);
        verify(amazonS3, times(1)).getObjectTagging(any(GetObjectTaggingRequest.class));
        verify(getObjectTaggingResult, times(1)).getTagSet();
        verify(amazonS3, times(1)).generatePresignedUrl(any(GeneratePresignedUrlRequest.class));
    }
}
