package com.zhtkj.jt808.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhtkj.jt808.entity.DeviceConfig;
import com.zhtkj.jt808.vo.req.VersionMsg.VersionInfo;

public interface DeviceConfigMapper {

	int insertDeviceConfig(@Param(value = "configInfo") VersionInfo configInfo);
	
	int updateDeviceConfig(@Param(value = "configInfo") VersionInfo configInfo);
	
	List<DeviceConfig> selectDeviceConfigByKey(@Param(value = "mac") String mac);
	
}
