package com.gavyselflearn.flashsale.dao;

import com.gavyselflearn.flashsale.domain.FlashsaleUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FlashsaleDao {

    @Select("select * from flashsale_user where id = #{id}")
    public FlashsaleUser getById(@Param("id")long id);

}
