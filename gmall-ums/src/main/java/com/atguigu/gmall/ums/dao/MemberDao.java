package com.atguigu.gmall.ums.dao;

import com.agtuigu.gmall.ums.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author yanweiling
 * @email yanweilingjob@126.com
 * @date 2021-01-29 15:07:57
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
