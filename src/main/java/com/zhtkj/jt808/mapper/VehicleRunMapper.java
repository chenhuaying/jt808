package com.zhtkj.jt808.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhtkj.jt808.entity.VehicleRun;
import com.zhtkj.jt808.vo.req.LocationMsg.LocationInfo;

public interface VehicleRunMapper extends BaseMapper<VehicleRun> {

	int insertVehicleRun(@Param(value = "locationInfo") LocationInfo locationInfo);
	
	int updateCarRuntime(@Param(value = "locationInfo") LocationInfo locationInfo);
	
	int setVehicleOnline(@Param(value = "licNumber") String licNumber);
	
	int setVehicleOffline(@Param(value = "idleTime") String idleTime);
	
	List<VehicleRun> findPassword(@Param(value = "licNumber") String licNumber);
}
