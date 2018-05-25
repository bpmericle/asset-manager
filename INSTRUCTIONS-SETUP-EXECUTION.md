# Instructions to Setup and Execute the Asset Manager Service

The following instructions will explain how to build and execute the Asset Manager Service on your local machine.

## Pre-requisites

### AWS

1. You need to have an AWS account. You can sign up for a free account [here](https://aws.amazon.com/free/).

2. You need to create an IAM user account and give the user a policy allowing access to S3. You can find more information about IAM [here](https://docs.aws.amazon.com/IAM/latest/UserGuide/getting-started.html) and you can find more information about creating a policy [here](https://aws.amazon.com/blogs/security/writing-iam-policies-how-to-grant-access-to-an-amazon-s3-bucket/).

3. You need to save off your AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY to a safe place when you create your IAM user account.

4. You need to create an S3 Bucket. You can find more information about this [here](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/create-bucket.html).

### Local Machine

1. You need to have the [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) installed on your local machine.

2. You need to have [Maven](https://maven.apache.org/index.html) installed on your local machine.

3. You need to have [Git](https://git-scm.com/) installed on your local machine.

## Build the service

The Asset Manager Service is a [Spring Boot](https://projects.spring.io/spring-boot/) web service that is built using Maven. To build the service from source, do the following:

1. Clone this repository to your local machine. Use the command-line git client or your favorite source code repository management tool.

**Ex.**
```
$ git clone https://github.com/bpmericle/asset-manager.git
```

2. Build the project. The resulting artifact will be located in the `target` directory from the root of the project.

**Ex.**
```
$ cd asset-manager
$ mvn clean package
```

## Start the service

This following assumes you will be running the service locally from your machine. You can do this by executing the following command where the jar file is located.

There are four arguments that are passed in when running the service.

| Arguments             | Description                                             |
|:----------------------|:--------------------------------------------------------|
| AWS_ACCESS_KEY_ID     | The Access key ID of the IAM user that was created.     |
| AWS_SECRET_ACCESS_KEY | The Secret access key of the IAM user that was created. |
| AWS_S3_BUCKET_NAME    | The name of the S3 bucket that was created.             |
| AWS_S3_REGION         | The region the the S3 bucket was created in.            |

**Ex.**
```
$ java -DAWS_ACCESS_KEY_ID={YOUR_AWS_ACCESS_KEY_ID} -DAWS_SECRET_ACCESS_KEY={YOUR_AWS_SECRET_ACCESS_KEY} -DAWS_S3_BUCKET_NAME={YOUR_AWS_S3_BUCKET_NAME} -DAWS_S3_REGION={AWS_S3_REGION} -jar target/asset-manager-1.0.0-SNAPSHOT.jar
```
