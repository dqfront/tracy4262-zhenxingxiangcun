package com.ovit.manager.modules.nswy.service;

import com.alibaba.fastjson.JSONObject;
import com.github.xphsc.http.HttpUtil;
import com.github.xphsc.util.StringUtil;
import com.ovit.manager.common.config.Global;
import com.ovit.manager.common.persistence.Page;
import com.ovit.manager.common.service.CrudService;
import com.ovit.manager.modules.nswy.dao.ExpertApplyDao;
import com.ovit.manager.modules.nswy.entity.ExpertApply;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**物种 service
 * Created by ${zhanlei} on 2017-8-16.
 */
@Service
@Transactional(readOnly = true)
public class NswySpeciesVarieteyService extends CrudService<ExpertApplyDao, ExpertApply> {

    @Autowired
    private ExpertApplyDao ExpertApplyDao;

    public Page<Map<String,Object>> listSpeciesVarietey(Map<String, Object> params) {
        String pageNo;
        String pageSize;
        String fname;
        if (params.get("pageNo") != null && params.get("pageNo") != ""){
            pageNo = String.valueOf(params.get("pageNo"));
        }else {
            pageNo = "1";
        }
        if (params.get("pageSize") != null && params.get("pageSize") != ""){
            pageSize = String.valueOf(params.get("pageSize"));
        }else {
            pageSize = "30";
        }

        String url = Global.getConfig("api.url")+"/wiki/api/wiki/listSpeciesVarietey";
        StringBuffer jsonBody = new StringBuffer();
        jsonBody.append("{pageNum:")
                .append(pageNo)
                .append(",pageSize:")
                .append(pageSize)
                .append(",auditstatus:")
                .append(MapUtils.getString(params,"auditstatus"));
        if (params.get("fname") != null && params.get("fname") != ""){
            fname = MapUtils.getString(params,"fname");
            jsonBody.append(",keywords:")
                    .append('"').append(fname).append('"');
        }
        jsonBody.append("}");
    	Page<Map<String,Object>> page = new Page<>();
        List<Map<String,Object>> list;
        try {
            String resul = HttpUtil.doPost(url,jsonBody.toString());
            JSONObject object = JSONObject.parseObject(resul);
            list = (List<Map<String,Object>>) object.get("data");
            Long total = Long.parseLong(String.valueOf(object.get("total")));
            for (Map<String,Object> map : list) {
                String fcreatorid = MapUtils.getString(map,"fcreatorid");
                String displayName = ExpertApplyDao.queryDisplayNameByAccount(fcreatorid);
                map.put("displayName",displayName);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String fcreatetime  = formatter.format(new Date(Long.parseLong(MapUtils.getString(map,"updatetime"))));
                map.put("updatetime",fcreatetime);
            }
            page.setList(list);
            page.setPageNo(Integer.parseInt(pageNo));
            page.setPageSize(Integer.parseInt(pageSize));
            page.setCount(total);
            //设置总页数
            Integer totals = Integer.valueOf(String.valueOf(total));
            Integer size  = Integer.valueOf(pageSize);
            page.setLast( totals % size == 0 ? totals / size : totals / size + 1);
            if (Integer.parseInt(pageNo) != 0){
                page.setPrev(Integer.parseInt(pageNo)-1);
            }
            page.setNext(Integer.parseInt(pageNo)+1);
        }catch (Exception e){

        }
        return page;
    }

    public Map<String,Object> getSpeciesVarieteyById(String indexid){
        Map<String,Object> map = new HashMap<>();
        String url = Global.getConfig("api.url")+"/wiki/api/wiki/getSpeciesVarietey/"+indexid;
        try {
            String result = HttpUtil.doGet(url);
            JSONObject object = JSONObject.parseObject(result);
            map = (Map<String, Object>) object.get("data");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            if(StringUtil.isNotBlank(MapUtils.getString(map,"fauthannouncedate"))){
                Date fadate  = formatter.parse(MapUtils.getString(map,"fauthannouncedate"));
                String fauthannouncedate  = formatter.format(fadate);
                map.put("fauthannouncedate",fauthannouncedate);
            }

            if(StringUtil.isNotBlank(MapUtils.getString(map,"fapplydate"))){
                Date fapplydates  = formatter.parse(MapUtils.getString(map,"fapplydate"));
                String fapplydate  = formatter.format(fapplydates);
                map.put("fapplydate",fapplydate);
            }

            if(StringUtil.isNotBlank(MapUtils.getString(map,"fapplyannouncedate"))){
                Date fapplyannouncedates  = formatter.parse(MapUtils.getString(map,"fapplyannouncedate"));
                String fapplyannouncedate  = formatter.format(fapplyannouncedates);
                map.put("fapplyannouncedate",fapplyannouncedate);
            }

            if(StringUtil.isNotBlank(MapUtils.getString(map,"fauthdate"))){
                Date fauthdates  = formatter.parse(MapUtils.getString(map,"fauthdate"));
                String fauthdate  = formatter.format(fauthdates);
                map.put("fauthdate",fauthdate);
            }
            if(StringUtil.isNotBlank(MapUtils.getString(map,"fvarietyapprdate"))){
                SimpleDateFormat matter = new SimpleDateFormat("yyyy");
                Date fvdate  = matter.parse(String.valueOf(map.get("fvarietyapprdate")));
                String fvarietyapprdate  = matter.format(fvdate);
                map.put("fvarietyapprdate",fvarietyapprdate);
            }

        }catch (Exception e){

        }
        return  map;
    }

    @Transactional()
    public void updateSpeciesVarietey(Map<String,Object> params){
        List<Map<String,Object>> list;
        String url = new String();
        if (params.get("auditstatus") != null){
            try {

                String auditstatus  = String.valueOf(params.get("auditstatus"));
                String auditstatu = new String();
                url = Global.getConfig("api.url")+"/wiki/api/wiki/updateSpeciesVarieteyAudit";
                if(auditstatus.equals("0")){
                    auditstatu = "5";
                }else if(auditstatus.equals("2")){
                    auditstatu = "1";
                }else if(auditstatus.equals("3")){
                    auditstatu = "5";
                }else{
                    auditstatu = auditstatus;
                }
                StringBuffer json = new StringBuffer();
                json.append("{indexid:")
                    .append("\"")
                    .append(String.valueOf(params.get("indexid")))
                    .append("\"")
                    .append(",")
                    .append("auditstatus:")
                    .append(auditstatu)
                    .append("}");
                HttpUtil.doPost(url,json.toString());
                if(auditstatus.equals("2")||auditstatus.equals("0")){
                    url = Global.getConfig("api.url")+"/wiki/api/wiki/findByVarieteyName";
                    String resul = HttpUtil.doPost(url,json.toString());
                    JSONObject object = JSONObject.parseObject(resul);
                    list = (List<Map<String,Object>>) object.get("data");
                    Map<String,Object> map = new HashMap<>();
                    map.put("image", MapUtils.getString(list.get(0),"ficon"));
                    map.put("account",MapUtils.getString(list.get(0),"fcreatorid"));
                    map.put("libraryId",MapUtils.getString(list.get(0),"fid"));
                    map.put("name",MapUtils.getString(list.get(0),"fname"));
                    map.put("classId",MapUtils.getString(list.get(0),"speciesid"));
                    map.put("indexid",MapUtils.getString(list.get(0),"indexid"));
                    Map<String,Object> wiki = ExpertApplyDao.findVarietyList(map);
                    if(MapUtils.isEmpty(wiki)){
                        ExpertApplyDao.saveVarietyLibraryInfo(map);
                    }

                }


                if(auditstatus.equals("0")){//更新待审核

                    url = Global.getConfig("api.url")+"/wiki/api/wiki/updateVarieteyPassAudit";
                    StringBuffer jsons = new StringBuffer();
                    jsons.append("{indexid:")
                            .append("\"")
                            .append(String.valueOf(params.get("indexid")))
                            .append("\"")
                            .append("}");
                    HttpUtil.doPost(url,jsons.toString());

                    url = Global.getConfig("api.url")+"/wiki/api/wiki/findByVarieteyName";
                    String resul = HttpUtil.doPost(url,jsons.toString());
                    JSONObject object = JSONObject.parseObject(resul);
                    list = (List<Map<String,Object>>) object.get("data");
                    Map<String,Object> map = new HashMap<>();
                    map.put("fid",MapUtils.getString(list.get(0),"fid"));
                    map.put("image", MapUtils.getString(list.get(0),"ficon"));
                    map.put("classId",MapUtils.getString(list.get(0),"speciesid"));
                    ExpertApplyDao.updateVarietyLibraryInfo(map);
                }else if(auditstatus.equals("3")){//删除待审核


                    url = Global.getConfig("api.url")+"/wiki/api/wiki/listSpeciesVarietey";
                    StringBuffer jsonBody = new StringBuffer();
                    jsonBody.append("{speciesid:")
                            .append("\"")
                            .append(MapUtils.getString(params,"speciesid"))
                            .append("\"")
                            .append(",auditstatus:")
                            .append("1")
                            .append(",fid:")
                            .append("\"")
                            .append(MapUtils.getString(params,"fid"))
                            .append("\"")
                            .append("}");

                    String resul = HttpUtil.doPost(url,jsonBody.toString());
                    JSONObject object = JSONObject.parseObject(resul);
                    list = (List<Map<String,Object>>) object.get("data");
                    String indexids  = String.valueOf(list.get(0).get("indexid"));
                    url = Global.getConfig("api.url")+"/wiki/api/wiki/updateSpeciesVarieteyAudit";
                    StringBuffer jsons = new StringBuffer();
                    jsons.append("{indexid:")
                            .append("\"")
                            .append(indexids)
                            .append("\"")
                            .append(",fisdelete:")
                            .append("1").append("}");
                    HttpUtil.doPost(url,jsons.toString());

                }

            }catch (Exception e){

            }
        }
    }


}
