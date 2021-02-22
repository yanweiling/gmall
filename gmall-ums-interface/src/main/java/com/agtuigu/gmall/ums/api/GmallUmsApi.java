package com.agtuigu.gmall.ums.api;


import com.agtuigu.gmall.ums.entity.MemberEntity;
import com.atguigu.core.bean.Resp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface GmallUmsApi {
    /**
     * 根据用户名和密码查询用户
     */
    @GetMapping("ums/member/query")
    Resp<MemberEntity> queryUser(@RequestParam("username") String username, @RequestParam("password") String password);

}
