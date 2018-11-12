package com.zhtkj.jt808.mapper;

import org.apache.ibatis.annotations.Param;

import com.zhtkj.jt808.entity.VehicleRun;

public interface VehicleHisMapper {

	int insertVehicleHis(@Param(value = "month") String month, 
			@Param(value = "vehRun") VehicleRun vehRun);
}
