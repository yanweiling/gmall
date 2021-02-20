package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.dao.SkuInfoDao;
import com.atguigu.gmall.pms.dao.SpuInfoDescDao;
import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.atguigu.gmall.pms.entity.SkuImagesEntity;
import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.atguigu.gmall.pms.entity.SpuInfoDescEntity;
import com.atguigu.gmall.pms.feign.SkuSaleFeign;
import com.atguigu.gmall.pms.service.AttrService;
import com.atguigu.gmall.pms.service.ProductAttrValueService;
import com.atguigu.gmall.pms.service.SkuImagesService;
import com.atguigu.gmall.pms.service.SkuInfoService;
import com.atguigu.gmall.pms.service.SkuSaleAttrValueService;
import com.atguigu.gmall.pms.service.SpuInfoDescService;
import com.atguigu.gmall.pms.vo.ProductAttrValueVO;
import com.atguigu.gmall.pms.vo.SkuInfoVO;
import com.atguigu.gmall.pms.vo.SpuInfoVO;
import com.atguigu.gmall.sms.vo.SkuSaleVO;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.SpuInfoDao;
import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import com.atguigu.gmall.pms.service.SpuInfoService;
import org.springframework.util.CollectionUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    AttrService attrService;

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SkuInfoDao skuInfoDao;

    @Autowired
    AttrDao attrDao;

    @Autowired
    SpuInfoDescDao spuInfoDescDao;

    @Autowired
    SkuSaleFeign skuSaleFeign;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo querySpuInfo(QueryCondition condition, Long catId) {
        // 封装分页条件
        IPage<SpuInfoEntity> page = new Query<SpuInfoEntity>().getPage(condition);

        // 封装查询条件
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        // 如果分类id不为0，要根据分类id查，否则查全部
        if (catId != 0) {
            wrapper.eq("catalog_id", catId);
        }
        // 如果用户输入了检索条件，根据检索条件查
        String key = condition.getKey();
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(t -> t.like("spu_name", key).or().like("id", key));
        }

        return new PageVo(this.page(page, wrapper));
    }


    @GlobalTransactional
    @Override
    public void saveSpuinfoVO(SpuInfoVO spuInfoVO) {

        /// 1.保存spu相关
        // 1.1. 保存spu基本信息 spu_info
        saveSpuInfo(spuInfoVO);

        // 1.2. 保存spu的描述信息 spu_info_desc
        saveSpuDesc(spuInfoVO);

        // 1.3. 保存spu的规格参数信息
        saveBaseAttrs(spuInfoVO);

        /// 2. 保存sku相关信息
        this.saveSkuInfoWithSaleInfo(spuInfoVO);

//        /// 1.保存spu相关
//        // 1.1. 保存spu基本信息 spu_info
//        spuInfoVO.setPublishStatus(1); // 默认是已上架
//        spuInfoVO.setCreateTime(new Date());
//        spuInfoVO.setUpdateTime(spuInfoVO.getCreateTime()); // 新增时，更新时间和创建时间一致
//        this.save(spuInfoVO);
//        Long spuId = spuInfoVO.getId(); // 获取新增后的spuId
//
//        // 1.2. 保存spu的描述信息 spu_info_desc
//        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
//        // 注意：spu_info_desc表的主键是spu_id,需要在实体类中配置该主键不是自增主键
//        spuInfoDescEntity.setSpuId(spuId);
//        // 把商品的图片描述，保存到spu详情中，图片地址以逗号进行分割
//        spuInfoDescEntity.setDecript(StringUtils.join(spuInfoVO.getSpuImages(), ","));
//        spuInfoDescService.save(spuInfoDescEntity);
//
//        // 1.3. 保存spu的规格参数信息
//        List<ProductAttrValueVO> baseAttrs = spuInfoVO.getBaseAttrs();
//        if (!CollectionUtils.isEmpty(baseAttrs)) {
//            List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(productAttrValueVO -> {
//                productAttrValueVO.setSpuId(spuId);
//                productAttrValueVO.setAttrSort(0);
//                productAttrValueVO.setQuickShow(0);
//                return productAttrValueVO;
//            }).collect(Collectors.toList());
//            this.productAttrValueService.saveBatch(productAttrValueEntities);
//        }
//
//        /// 2. 保存sku相关信息
//        List<SkuInfoVO> skuInfoVOs = spuInfoVO.getSkus();
//        if (CollectionUtils.isEmpty(skuInfoVOs)){
//            return;
//        }
//        skuInfoVOs.forEach(skuInfoVO -> {
//            // 2.1. 保存sku基本信息
//            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
//            BeanUtils.copyProperties(skuInfoVO, skuInfoEntity);
//            // 品牌和分类的id需要从spuInfo中获取
//            skuInfoEntity.setBrandId(spuInfoVO.getBrandId());
//            skuInfoEntity.setCatalogId(spuInfoVO.getCatalogId());
//            // 获取随机的uuid作为sku的编码
//            skuInfoEntity.setSkuCode(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
//            // 获取图片列表
//            List<String> images = skuInfoVO.getImages();
//            // 如果图片列表不为null，则设置默认图片
//            if (!CollectionUtils.isEmpty(images)){
//                // 设置第一张图片作为默认图片
//                skuInfoEntity.setSkuDefaultImg(skuInfoEntity.getSkuDefaultImg()==null ? images.get(0) : skuInfoEntity.getSkuDefaultImg());
//            }
//            skuInfoEntity.setSpuId(spuId);
//            skuInfoService.save(skuInfoEntity);
//            // 获取skuId
//            Long skuId = skuInfoEntity.getSkuId();
//
//            // 2.2. 保存sku图片信息
//            if (!CollectionUtils.isEmpty(images)){
//                String defaultImage = images.get(0);
//                List<SkuImagesEntity> skuImageses = images.stream().map(image -> {
//                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
//                    skuImagesEntity.setDefaultImg(StringUtils.equals(defaultImage, image) ? 1 : 0);
//                    skuImagesEntity.setSkuId(skuId);
//                    skuImagesEntity.setImgSort(0);
//                    skuImagesEntity.setImgUrl(image);
//                    return skuImagesEntity;
//                }).collect(Collectors.toList());
//                skuImagesService.saveBatch(skuImageses);
//            }
//
//            // 2.3. 保存sku的规格参数（销售属性）
//            List<SkuSaleAttrValueEntity> saleAttrs = skuInfoVO.getSaleAttrs();
//            saleAttrs.forEach(saleAttr -> {
//                // 设置属性名，需要根据id查询AttrEntity
//                saleAttr.setAttrName(attrService.getById(saleAttr.getAttrId()).getAttrName());
//                saleAttr.setAttrSort(0);
//                saleAttr.setSkuId(skuId);
//            });
//            skuSaleAttrValueService.saveBatch(saleAttrs);
//
//            // 3. 保存营销相关信息，需要远程调用gmall-sms
////            SkuSaleDTO skuSaleDTO = new SkuSaleDTO();
////            BeanUtils.copyProperties(skuInfoVO, skuSaleDTO);
////            skuSaleDTO.setSkuId(skuId);
////            this.skuSaleFeign.saveSkuSaleInfo(skuSaleDTO);
//        });
//
//        // 发送mq消息
////        sendMessage(spuInfoVO.getId(),"insert");
//
//            // 3. 保存营销相关信息，需要远程调用gmall-sms
//            // 3.1. 积分优惠
//
//            // 3.2. 满减优惠
//
//            // 3.3. 数量折扣
////        });
    }

    /**
     * 保存sku相关信息及营销信息
     * @param spuInfoVO
     */
    private void saveSkuInfoWithSaleInfo(SpuInfoVO spuInfoVO) {
        List<SkuInfoVO> skuInfoVOs = spuInfoVO.getSkus();
        if (CollectionUtils.isEmpty(skuInfoVOs)){
            return;
        }
        skuInfoVOs.forEach(skuInfoVO -> {
            // 2.1. 保存sku基本信息
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(skuInfoVO, skuInfoEntity);
            // 品牌和分类的id需要从spuInfo中获取
            skuInfoEntity.setBrandId(spuInfoVO.getBrandId());
            skuInfoEntity.setCatalogId(spuInfoVO.getCatalogId());
            // 获取随机的uuid作为sku的编码
            skuInfoEntity.setSkuCode(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
            // 获取图片列表
            List<String> images = skuInfoVO.getImages();
            // 如果图片列表不为null，则设置默认图片
            if (!CollectionUtils.isEmpty(images)){
                // 设置第一张图片作为默认图片
                skuInfoEntity.setSkuDefaultImg(skuInfoEntity.getSkuDefaultImg()==null ? images.get(0) : skuInfoEntity.getSkuDefaultImg());
            }
            skuInfoEntity.setSpuId(spuInfoVO.getId());
            this.skuInfoDao.insert(skuInfoEntity);
            // 获取skuId
            Long skuId = skuInfoEntity.getSkuId();

            // 2.2. 保存sku图片信息
            if (!CollectionUtils.isEmpty(images)){
                String defaultImage = images.get(0);
                List<SkuImagesEntity> skuImageses = images.stream().map(image -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setDefaultImg(StringUtils.equals(defaultImage, image) ? 1 : 0);
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgSort(0);
                    skuImagesEntity.setImgUrl(image);
                    return skuImagesEntity;
                }).collect(Collectors.toList());
                this.skuImagesService.saveBatch(skuImageses);
            }

            // 2.3. 保存sku的规格参数（销售属性）
            List<SkuSaleAttrValueEntity> saleAttrs = skuInfoVO.getSaleAttrs();
            saleAttrs.forEach(saleAttr -> {
                // 设置属性名，需要根据id查询AttrEntity
                saleAttr.setAttrName(this.attrDao.selectById(saleAttr.getAttrId()).getAttrName());
                saleAttr.setAttrSort(0);
                saleAttr.setSkuId(skuId);
            });
            this.skuSaleAttrValueService.saveBatch(saleAttrs);

            // 3. 保存营销相关信息，需要远程调用gmall-sms
            SkuSaleVO skuSaleDTO = new SkuSaleVO();
            BeanUtils.copyProperties(skuInfoVO, skuSaleDTO);
            skuSaleDTO.setSkuId(skuId);
            this.skuSaleFeign.saveSkuSaleInfo(skuSaleDTO);
        });
    }

    /**
     * 保存spu基本属性信息
     * @param spuInfoVO
     */
    private void saveBaseAttrs(SpuInfoVO spuInfoVO) {
        List<ProductAttrValueVO> baseAttrs = spuInfoVO.getBaseAttrs();
        if (!CollectionUtils.isEmpty(baseAttrs)) {
            List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(productAttrValueVO -> {
                productAttrValueVO.setSpuId(spuInfoVO.getId());
                productAttrValueVO.setAttrSort(0);
                productAttrValueVO.setQuickShow(0);
                return productAttrValueVO;
            }).collect(Collectors.toList());
            this.productAttrValueService.saveBatch(productAttrValueEntities);
        }
    }

    /**
     * 保存spu描述信息（图片）
     * @param spuInfoVO
     */
    private void saveSpuDesc(SpuInfoVO spuInfoVO) {
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        // 注意：spu_info_desc表的主键是spu_id,需要在实体类中配置该主键不是自增主键
        spuInfoDescEntity.setSpuId(spuInfoVO.getId());
        // 把商品的图片描述，保存到spu详情中，图片地址以逗号进行分割
        spuInfoDescEntity.setDecript(StringUtils.join(spuInfoVO.getSpuImages(), ","));
        this.spuInfoDescDao.insert(spuInfoDescEntity);
    }

    /**
     * 保存spu基本信息
     * @param spuInfoVO
     */
    private void saveSpuInfo(SpuInfoVO spuInfoVO) {
        spuInfoVO.setPublishStatus(1); // 默认是已上架
        spuInfoVO.setCreateTime(new Date());
        spuInfoVO.setUpdateTime(spuInfoVO.getCreateTime()); // 新增时，更新时间和创建时间一致
        this.save(spuInfoVO);
    }
}