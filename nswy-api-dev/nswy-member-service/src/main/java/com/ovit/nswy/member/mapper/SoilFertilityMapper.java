package com.ovit.nswy.member.mapper;

import java.util.Map;

public interface SoilFertilityMapper {
    Map<String,Object> query(Map<String, Object> params);

    int save(Map<String, Object> params);
}
