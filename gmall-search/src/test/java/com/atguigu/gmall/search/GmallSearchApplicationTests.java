package com.atguigu.gmall.search;

import com.alibaba.fastjson.JSON;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.entity.BrandEntity;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import com.atguigu.gmall.search.entity.Goods;
import com.atguigu.gmall.search.entity.SearchAttr;
import com.atguigu.gmall.search.feign.GmallPmsFeign;
import com.atguigu.gmall.search.feign.GmallWmsFeign;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class GmallSearchApplicationTests {

    @Autowired
    private GmallPmsFeign gmallPmsFeign;

    @Autowired
    private GmallWmsFeign gmallWmsFeign;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Test
    public void importData(){
        int pageSize=100;
        long page=1l;
        do{
            QueryCondition condition=new QueryCondition();
            condition.setPage(page);
            condition.setLimit(10l);
            Resp<List<SpuInfoEntity>> resp=this.gmallPmsFeign.querySpuInfoByStatus(condition,1);
            List<SpuInfoEntity> spuInfoEntities=resp.getData();
            if(!CollectionUtils.isEmpty(spuInfoEntities)){
                spuInfoEntities.forEach(spuInfoEntity -> {
                    buildGoods(spuInfoEntity);
                });
            }
            //当前页的记录数
            pageSize=spuInfoEntities.size();
            page++;
        }while (pageSize==100);
    }

    private void buildGoods(SpuInfoEntity spuInfoEntity) {
        Resp<List<SkuInfoEntity>> skuInfoResp=this.gmallPmsFeign.querySkuBySpuId(spuInfoEntity.getId());
        List<SkuInfoEntity> skuInfoEntities=skuInfoResp.getData();
        List<Goods> goodsList=new ArrayList<>();
        if(!CollectionUtils.isEmpty(skuInfoEntities)){
            skuInfoEntities.forEach(skuInfoEntity -> {
                Goods goods=new Goods();
                goods.setBrandId(spuInfoEntity.getBrandId());
                //根据品牌id获取品牌对象
                Resp<BrandEntity> brandResp=this.gmallPmsFeign.info(spuInfoEntity.getBrandId());
                BrandEntity brand=brandResp.getData();
                goods.setBrandName(brand==null?spuInfoEntity.getSpuName():brand.getName());
                goods.setAttrs(null);
                goods.setCategoryId(spuInfoEntity.getCatalogId());
                //根据分类id获取分类对象
                Resp<CategoryEntity> categoryEntityResp=this.gmallPmsFeign.catInfo(spuInfoEntity.getCatalogId());
                CategoryEntity categoryEntity=categoryEntityResp.getData();
                goods.setCategoryName(categoryEntity.getName());
                goods.setCreateTime(spuInfoEntity.getCreateTime());
                goods.setPic(skuInfoEntity.getSkuDefaultImg());
                goods.setPrice(skuInfoEntity.getPrice().doubleValue());
                goods.setSale(0l);
                goods.setSkuId(skuInfoEntity.getSkuId());
                goods.setTitle(skuInfoEntity.getSkuTitle());

                //查询库存
                Resp<List<WareSkuEntity>> wareSkuResp= this.gmallWmsFeign.queryWareSkuBySkuId(skuInfoEntity.getSkuId());
                List<WareSkuEntity> wareSkuEntities=wareSkuResp.getData();
                long stock=wareSkuEntities.stream().mapToLong(WareSkuEntity::getStock).sum();
                goods.setStock(stock);

                //设置属性
                Resp<List<ProductAttrValueEntity>> attrResp=this.gmallPmsFeign.querySearchAttrValue(spuInfoEntity.getId());
                List<ProductAttrValueEntity> attrValueEntities=attrResp.getData();

                List<SearchAttr> attrs=attrValueEntities.stream().map(productAttrValueEntity -> {
                    SearchAttr searchAttr=new SearchAttr();
                    searchAttr.setAttrId(productAttrValueEntity.getAttrId());
                    searchAttr.setAttrName(productAttrValueEntity.getAttrName());
                    searchAttr.setAttrValue(productAttrValueEntity.getAttrValue());
                    return searchAttr;
                }).collect(Collectors.toList());
                goods.setAttrs(attrs);
                IndexRequest request = new IndexRequest("goods", "info", String.valueOf(goods.getSkuId()))
                        .source(JSON.toJSONString(goods), XContentType.JSON)
                        .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
                try {
                    IndexResponse response=restHighLevelClient.index(request);
                    System.out.println("插入成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                goodsList.add(goods);

            });



        }
    }

}
