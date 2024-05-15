package com.hhu.filmix.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;

@Component
public class SnowFlake {
    private final Snowflake snowflake;
    public SnowFlake(){
        snowflake = IdUtil.getSnowflake();
    }
    public Long genId(){
        return snowflake.nextId();
    }
    public String genStrId(){
        return snowflake.nextIdStr();
    }
}
