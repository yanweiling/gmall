package com.atguigu.gmall.auth.service;

import com.agtuigu.gmall.ums.entity.MemberEntity;
import com.atguigu.core.bean.Resp;
import com.atguigu.core.utils.JwtUtils;
import com.atguigu.gmall.auth.config.JwtProperties;
import com.atguigu.gmall.auth.feign.GmallUmsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    @Autowired
    GmallUmsClient gmallUmsClient;
    @Autowired
    JwtProperties jwtProperties;


    public String authentication(String username, String password) {
        try{
            //远程调用，校验用户名和密码
            Resp<MemberEntity> resp=gmallUmsClient.queryUser(username,password);
            MemberEntity memberEntity=resp.getData();
            //判断用户是否为null
            if(memberEntity==null){
                return null;
            }
            //生成jwt
            Map<String,Object> map=new HashMap<>();
            map.put("id",memberEntity.getId());
            map.put("username",memberEntity.getUsername());
            String token=JwtUtils.generateToken(map, jwtProperties.getPrivateKey(),jwtProperties.getExpire());
            return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
