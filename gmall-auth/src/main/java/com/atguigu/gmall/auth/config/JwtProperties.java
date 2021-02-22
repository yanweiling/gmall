package com.atguigu.gmall.auth.config;

import com.atguigu.core.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@Slf4j
@ConfigurationProperties(prefix = "gmall.jwt")
public class JwtProperties {

    private String secret; //秘钥
    private String pubKeyPath;
    private String priKeyPath;
    private int expire;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private String cookieName;

    @PostConstruct
    public void init(){
       try{
           File pubKey=new File(pubKeyPath);
           File priKey=new File(priKeyPath);
           if(!pubKey.exists() || !priKey.exists()){
               //生成公钥和私钥
               RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
           }
           this.publicKey=RsaUtils.getPublicKey(pubKeyPath);
           this.privateKey=RsaUtils.getPrivateKey(priKeyPath);
       }catch (Exception e){
           log.error("初始化公钥私钥失败",e);
           throw new RuntimeException();
       }
    }
}
