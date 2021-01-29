package com.atguigu.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.UndoLogEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 
 *
 * @author yanweiling
 * @email yanweilingjob@126.com
 * @date 2021-01-28 18:19:50
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageVo queryPage(QueryCondition params);
}

