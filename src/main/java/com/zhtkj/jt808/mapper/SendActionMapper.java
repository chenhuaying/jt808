package com.zhtkj.jt808.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhtkj.jt808.entity.SendAction;

public interface SendActionMapper extends BaseMapper<SendAction> {
	
	List<SendAction> findSendAction();
	
}
