package com.zhtkj.jt808.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhtkj.jt808.entity.SendParam;

public interface SendParamMapper extends BaseMapper<SendParam> {

	List<SendParam> findSendParam();
	
}
