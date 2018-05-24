package com.bpmericle.assetmanager.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectTaggingRequest;
import com.amazonaws.services.s3.model.GetObjectTaggingResult;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.SetObjectTaggingRequest;
import com.amazonaws.services.s3.model.Tag;
import com.bpmericle.assetmanager.model.AssetDownloadMetadata;
import com.bpmericle.assetmanager.model.AssetStatus;
import com.bpmericle.assetmanager.model.AssetUploadMetadata;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service interface for the asset manager.
 *
 * @author Brian Mericle
 */
@Service
public class AssetManagerService {

    /**
     * The name of the S3 bucket to use.
     */
    @Value("#{systemProperties['AWS_S3_BUCKET_NAME']}")
    private String bucketName;

    /**
     * The client to use to communicatie with the AWS S3 service.
     */
    private AmazonS3 s3Client;

    private static final String TAG_STATUS_KEY = "Status";
    private static final String TAG_STATUS_VALUE_UPLOADED = "uploaded";
    private static final String EXCEPTION_MESSAGE_INVALID_STATUS = "Status of asset is not \'uploaded\'.";
    private static final String EXCEPTION_MESSAGE_AWS_SERVICE = "The call was transmitted successfully, but Amazon S3 couldn't process it, so it returned an error response.";
    private static final String EXCEPTION_MESSAGE_AWS_SDK_CLIENT = "Amazon S3 couldn't be contacted for a response, or the client couldn't parse the response from Amazon S3.";

    /**
     * Returns metadata about how to upload an asset to the asset store.
     *
     * @return metadata about how to upload an asset to the asset store.
     * @throws AssetManagerServiceException if an issue occurs building the
     * asset metadata
     */
    public AssetUploadMetadata requestAssetUpload() throws AssetManagerServiceException {
        AssetUploadMetadata response;

        try {
            // Set the pre-signed URL to expire after one hour.
            Calendar expiration = Calendar.getInstance();
            expiration.add(Calendar.HOUR, 1);

            // Generate the pre-signed URL.
            String assetId = UUID.randomUUID().toString().replaceAll("-", "");
            GeneratePresignedUrlRequest generatePresignedUrlRequest
                    = new GeneratePresignedUrlRequest(bucketName, assetId)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration.getTime());
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

            response = new AssetUploadMetadata(assetId, url.toString());
        } catch (AmazonServiceException ex) {
            throw new AssetManagerServiceException(EXCEPTION_MESSAGE_AWS_SERVICE, ex);
        } catch (SdkClientException ex) {
            throw new AssetManagerServiceException(EXCEPTION_MESSAGE_AWS_SDK_CLIENT, ex);
        }

        return response;
    }

    /**
     * Sets the status of the asset in the asset store.
     *
     * @param id the identifier of the asset
     * @param status the status of the asset to be set.
     */
    public void submitAssetUpdateStatus(final String id, final AssetStatus status) {
        try {
            List<Tag> tags = new ArrayList<>();
            tags.add(new Tag(TAG_STATUS_KEY, status.getStatus()));
            s3Client.setObjectTagging(new SetObjectTaggingRequest(bucketName, id, new ObjectTagging(tags)));
        } catch (AmazonServiceException ex) {
            throw new AssetManagerServiceException(EXCEPTION_MESSAGE_AWS_SERVICE, ex);
        } catch (SdkClientException ex) {
            throw new AssetManagerServiceException(EXCEPTION_MESSAGE_AWS_SDK_CLIENT, ex);
        }
    }

    /**
     * Returns metadata about how to download an asset from the asset store.
     *
     * @param id the identifier of the asset
     * @param timeout the amount of time in seconds the asset download will be
     * available for
     * @return metadata about how to download an asset from the asset store.
     */
    public AssetDownloadMetadata requestAssetDownload(final String id, final int timeout) {
        AssetDownloadMetadata response;

        try {
            // Validate the asset as a 'Status' tag value of 'uploaded', if not, fail
            GetObjectTaggingResult result = s3Client.getObjectTagging(new GetObjectTaggingRequest(bucketName, id));
            List<Tag> tags = result.getTagSet();
            boolean statusValid = false;
            for (Tag tag : tags) {
                if (TAG_STATUS_KEY.equals(tag.getKey())
                        && TAG_STATUS_VALUE_UPLOADED.equals(tag.getValue())) {
                    statusValid = true;
                    break;
                }
            }

            if (!statusValid) {
                throw new AssetManagerServiceException(EXCEPTION_MESSAGE_INVALID_STATUS);
            }

            // Set the pre-signed URL to expire after one hour.
            Calendar expiration = Calendar.getInstance();
            expiration.add(Calendar.SECOND, timeout);

            // Generate the pre-signed URL.
            GeneratePresignedUrlRequest generatePresignedUrlRequest
                    = new GeneratePresignedUrlRequest(bucketName, id)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration.getTime());
            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

            response = new AssetDownloadMetadata(url.toString());
        } catch (AmazonServiceException ex) {
            throw new AssetManagerServiceException(EXCEPTION_MESSAGE_AWS_SERVICE, ex);
        } catch (SdkClientException ex) {
            throw new AssetManagerServiceException(EXCEPTION_MESSAGE_AWS_SDK_CLIENT, ex);
        }

        return response;
    }

    /**
     * Sets the client to use to communicate with the AWS S3 service.
     *
     * @param client the AWS S3 client
     */
    @Autowired
    public void setS3Client(final AmazonS3 client) {
        this.s3Client = client;
    }
}
