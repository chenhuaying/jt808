package com.zhtkj.jt808.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhtkj.jt808.entity.DeviceConfig;
import com.zhtkj.jt808.vo.req.VersionMsg.VersionInfo;

public interface DeviceConfigMapper extends BaseMapper<DeviceConfig> {

	int insertDeviceConfig(@Param(value = "configInfo") VersionInfo configInfo);
	
	int updateDeviceConfig(@Param(value = "configInfo") VersionInfo configInfo);
	
}
