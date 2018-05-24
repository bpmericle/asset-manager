# Asset Manager Service

This service has the following features:

    1. Generate a pre-signed URL to enable a user to upload an asset to the AWS S3 object store.

    2. Update the status of the uploaded asset in the AWS S3 object store.

    3. Generate a pre-signed URL to enable a user to download an asset from the AWS S3 object store.

## Generate Pre-Signed URL to Upload Asset

A user can get a pre-signed URL to upload an asset by invoking the following (HTTP POST):

**cUrl**
```
$ curl -i -w "\n" -X POST "http://{hostname}:{port}/asset"
```

Ex. Get the URL to upload the asset (HTTP PUT).
```
$ curl -i -w "\n" -X POST "http://localhost:8080/asset"
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 24 May 2018 22:36:20 GMT

{"id":"0ba1b7dc00a142d28c5ff13b20ed97bf","upload_url":"https://xxxs3bucketnamexxx.s3.us-west-2.amazonaws.com/e33638d0df504175814ba97dfc60ca8c?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20180524T223620Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3599&X-Amz-Credential=AKIAIDLYXSZC3FGDOWBQ%2F20180524%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Signature=2a246886de3dd36604a4ec951a8546be5558b98fabb2f887065f3de008775f9d"}
```

Ex. Upload the asset using the "upload_url" (HTTP PUT).
```
$ curl -i -w "\n" -X PUT -d @test_file.txt "https://xxxs3bucketnamexxx.s3.us-west-2.amazonaws.com/e33638d0df504175814ba97dfc60ca8c?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20180524T224803Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3599&X-Amz-Credential=AKIAIDLYXSZC3FGDOWBQ%2F20180524%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Signature=54210b51c8a872ceafd932b5d2ef712919f7a796307b173789b511d1d068a9e9"
HTTP/1.1 200 OK
x-amz-id-2: 7ZfJIMSPZuRPtkUe3IGT6MGakj3W8relh7a9iaQLM2KCjArVz5bq4PyRIsmKhT1B/YiepPR3+fE=
x-amz-request-id: 1D87749191829BBE
Date: Thu, 24 May 2018 22:50:06 GMT
ETag: "d5f7207442f43ed7f7d63cf012fcc499"
Content-Length: 0
Server: AmazonS3
```

## Update Status of Asset

A user can update the status ('uploaded' is the only valid status at this time) of an asset by invoking the following (HTTP PUT):

**cUrl**
```
$ curl -i -w "\n" -H "Content-Type: application/json" -d '{"Status":"uploaded"}' -X PUT "http://{hostname}:{port}/asset/{id}"
```

Ex.
```$ curl -i -w "\n" -H "Content-Type: application/json" -d '{"Status":"uploaded"}' -X PUT http://localhost:8080/asset/e33638d0df504175814ba97dfc60ca8c
HTTP/1.1 200 
Content-Length: 0
Date: Thu, 24 May 2018 22:52:05 GMT
```

## Generate Pre-Signed URL to Download Asset

A user can get a pre-signed URL to download an asset by invoking the following (HTTP GET):

**cUrl**
```
$ curl -i -w "\n" -X GET "http://{hostname}:{port}/asset/{id}"
```

Ex. Get the URL to download the asset (HTTP GET).
```
$ curl -i -w "\n" -X GET "http://localhost:8080/asset/e33638d0df504175814ba97dfc60ca8c"
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 24 May 2018 22:54:49 GMT

{"Download_url":"https://xxxs3bucketnamexxx.s3.us-west-2.amazonaws.com/e33638d0df504175814ba97dfc60ca8c?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20180524T225449Z&X-Amz-SignedHeaders=host&X-Amz-Expires=59&X-Amz-Credential=AKIAIDLYXSZC3FGDOWBQ%2F20180524%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Signature=32549a16683620e3b83e9bc343b3d83cdc7c1333f540fa53d5095aaf7c660d35"}
```

Ex. Download the asset using the "Download_url" (HTTP GET).
```
$ curl -i -w "\n" -X GET "https://xxxs3bucketnamexxx.s3.us-west-2.amazonaws.com/e33638d0df504175814ba97dfc60ca8c?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20180524T225449Z&X-Amz-SignedHeaders=host&X-Amz-Expires=59&X-Amz-Credential=AKIAIDLYXSZC3FGDOWBQ%2F20180524%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Signature=32549a16683620e3b83e9bc343b3d83cdc7c1333f540fa53d5095aaf7c660d35"
HTTP/1.1 200 OK
x-amz-id-2: FO/HG32UXf41cBeMpRlXyiHpjT2VH7GZ2TaUvGKfABX/q0Lhj4uG2oWNe1yURkiV8hTZlF07dno=
x-amz-request-id: 9A5EC4219EA84FA4
Date: Thu, 24 May 2018 22:55:12 GMT
Last-Modified: Thu, 24 May 2018 22:50:06 GMT
ETag: "d5f7207442f43ed7f7d63cf012fcc499"
x-amz-tagging-count: 1
Accept-Ranges: bytes
Content-Type: application/x-www-form-urlencoded
Content-Length: 12
Server: AmazonS3

my text file
```