package com.atguigu.gmall.pms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.corba.se.impl.presentation.rmi.IDLTypeException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * spu信息介绍
 * 
 * @author yanweiling
 * @email yanweilingjob@126.com
 * @date 2021-01-28 18:19:50
 */
@ApiModel
@Data
@TableName("pms_spu_info_desc")
public class SpuInfoDescEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品id
	 */
	@TableId(type = IdType.INPUT)
	@ApiModelProperty(name = "spuId",value = "商品id")
	private Long spuId;
	/**
	 * 商品介绍
	 */
	@ApiModelProperty(name = "decript",value = "商品介绍")
	private String decript;

}
