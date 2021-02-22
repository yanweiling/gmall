package com.atguigu.gmall.gateway.config;

import com.atguigu.core.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@Data
@Slf4j
@ConfigurationProperties("gmall.jwt")
public class JwtProperties {
    //公钥地址
    private String pubKeyPath;
    //cookie名称
    private String cookieName;
    //公钥
    private PublicKey publicKey;

    @PostConstruct
    public void init(){
        try{
            this.publicKey= RsaUtils.getPublicKey(pubKeyPath);
        }catch (Exception e){
            log.error("初始化公钥失败",e);
        }
    }
}
