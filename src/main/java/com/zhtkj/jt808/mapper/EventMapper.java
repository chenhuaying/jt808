package com.zhtkj.jt808.mapper;

import org.apache.ibatis.annotations.Param;

import com.zhtkj.jt808.vo.req.EventMsg.EventInfo;
import com.zhtkj.jt808.vo.req.LocationMsg.LocationInfo;

public interface EventMapper {

	int insertCarEvent(@Param(value = "month") String month,
			@Param(value = "eventInfo") EventInfo eventInfo,
			@Param(value = "locationInfo") LocationInfo locationInfo);
	
}
