package com.gavyselflearn.flashsale.service;

import com.gavyselflearn.flashsale.dao.GoodsDao;
import com.gavyselflearn.flashsale.domain.FlashsaleGoods;
import com.gavyselflearn.flashsale.domain.Goods;
import com.gavyselflearn.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }


    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public int reduceStock(GoodsVo goods) {
        FlashsaleGoods fg = new FlashsaleGoods();
        fg.setGoodsId(goods.getId());
        int reduceRet = goodsDao.reduceStock(fg);
        if(reduceRet == 0) {
            throw new RuntimeException("该用户秒杀失败，实际库存已为0");
        }
        return reduceRet;
    }

    public String getStock(GoodsVo goods) {
        FlashsaleGoods fg = new FlashsaleGoods();
        fg.setGoodsId(goods.getId());
        return goodsDao.getStock(fg);
    }

}
