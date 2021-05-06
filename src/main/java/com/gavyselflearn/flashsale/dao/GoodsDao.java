package com.gavyselflearn.flashsale.dao;

import com.gavyselflearn.flashsale.domain.FlashsaleGoods;
import com.gavyselflearn.flashsale.domain.Goods;
import com.gavyselflearn.flashsale.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("select g.*, mg.stock_count, mg.start_date, mg.end_date, mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id=g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*, mg.stock_count, mg.start_date, mg.end_date, mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id=g.id where g.id = #{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    @Update("update miaosha_goods set stock_count=stock_count-1 where goods_id=#{goodsId} and stock_count > 0") //stock_count > 0，因为同一时间只有一个线程可以对这条记录进行操作，这里我们通过数据库的写锁解决超卖问题
    public int reduceStock(FlashsaleGoods fg);

    @Select("select stock_count from miaosha_goods where goods_id=#{goodsId}")
    String getStock(FlashsaleGoods fg);

}
