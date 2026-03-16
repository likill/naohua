package com.jiaxin.aiweb.manager;

import com.jiaxin.aiweb.config.CosClientConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class CosManager {
    @Resource
    private COSClient cosClient;

    @Resource
    private CosClientConfig cosClientConfig;

    /**
     * 上传对象
     * @param key 唯一键
     * @param file 文件
     * @return
     */
    public PutObjectResult putObject(String key,File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 上传文件到cos并返回访问的url地址
     *
     * @param key
     * @param file
     * @return
     */
    public String uploadFile(String key,File file) {
        PutObjectResult result = putObject(key, file);
        if(result!=null){
            String url = String.format("%s%s",cosClientConfig.getHost(),key);
            log.info("文件上传成功：{}-》{}",file.getName(),url) ;
            return url;
        }
        else {
            log.info("文件上传失败：{}，返回结果失败",file.getName()) ;
            return null;
        }
    }
}
