package com.wyq.taskSeven.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;


public class AliyumOSSUtil {

    private static Logger logger = Logger.getLogger(AliyumOSSUtil.class);

    private OSSClient ossClient = null;

    public void setOssClient(OSSClient ossClient) {
        this.ossClient = ossClient;
    }

    private String bucketName = null;

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public boolean upload(String key, String fileName, InputStream instream) {
        try {
            logger.info("Uploading an object to your bucket:" + bucketName + key + instream);

            this.createBucket();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(this.getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + key);

            ossClient.putObject(bucketName, key, instream, objectMetadata);
            return true;
        } catch (OSSException oe) {
            logger.info("Upload: Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            logger.info("Error Message: " + oe.getErrorCode());
            logger.info("Error Code:       " + oe.getErrorCode());
            logger.info("Request ID:      " + oe.getRequestId());
            logger.info("Host ID:           " + oe.getHostId());
            return false;
        } catch (ClientException ce) {
            ce.printStackTrace();
            logger.info("Upload: Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            logger.info("Error Message: " + ce.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
//        finally {
//            /*
//             * Do not forget to shut down the client finally to release all allocated resources.
//             */
////            ossClient.shutdown();
//            System.out.println(4);
//        }
    }

    public Object download(String key) {
        try {
            return ossClient.getObject(bucketName, key);
        } catch (OSSException oe) {
            logger.info("Dowmload: Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            logger.info("Error Message: " + oe.getErrorCode());
            logger.info("Error Code:       " + oe.getErrorCode());
            logger.info("Request ID:      " + oe.getRequestId());
            logger.info("Host ID:           " + oe.getHostId());
            return null;
        } catch (ClientException ce) {
            logger.info("Dowmload: Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            logger.info("Error Message: " + ce.getMessage());
            return null;
        }
//        finally {
//            /*
//             * Do not forget to shut down the client finally to release all allocated resources.
//             */
////            ossClient.shutdown();
//        }
    }

    public void createBucket() {
        /*
         * Determine whether the bucket exists
         */
        if (!ossClient.doesBucketExist(bucketName)) {
            /*
             * Create a new OSS bucket
             */
            logger.info("Creating bucket " + bucketName + "\n");
            ossClient.createBucket(bucketName);
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
            logger.info("Created bucket " + bucketName + "\n");
        }
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param filenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String filenameExtension) {
        if (filenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (filenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")
                || filenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (filenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (filenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (filenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (filenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }
}
