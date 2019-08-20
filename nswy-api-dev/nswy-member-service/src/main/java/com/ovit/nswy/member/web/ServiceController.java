package com.ovit.nswy.member.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ovit.nswy.member.service.SystemDictService;
import com.ovit.nswy.member.web.base.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/service")
public class ServiceController {

    final static String SERVICE_DATA = "[{\"pinyin\":\"BaoXianFuWu\",\"value\":\"FW35-0003\",\"label\":\"保险服务\"},{\"pinyin\":\"BiaoZhunGuanLiFuWu\",\"value\":\"FW13-0002\",\"label\":\"标准管理服务\"},{\"pinyin\":\"BinZangFuWu\",\"value\":\"5a24aec47e0b4ac08470231f7ed700aa\",\"label\":\"殡葬服务\"},{\"pinyin\":\"BuLaoFuWu\",\"value\":\"FW07-0004\",\"label\":\"捕捞服务\"},{\"pinyin\":\"CaiShouFuWu\",\"value\":\"FW0507-0001\",\"label\":\"采收服务\"},{\"pinyin\":\"ChanPinShouJiFuWu\",\"value\":\"FW06-0006\",\"label\":\"产品收集服务\"},{\"pinyin\":\"ChengShiGongGongJiaoTongFuWu\",\"value\":\"FW10-0003\",\"label\":\"城市公共交通服务\"},{\"pinyin\":\"Chu（Qin）SheJianSheFuWu\",\"value\":\"FW3602-0002\",\"label\":\"畜（禽）舍建设服务\"},{\"pinyin\":\"Chu（Qin）SiYangFuWu\",\"value\":\"77609891911d4bd69d9e25aa6ecfbb93\",\"label\":\"畜（禽）饲养服务\"},{\"pinyin\":\"ChuMuGongChengSheJiFuWu\",\"value\":\"FW3109-0002\",\"label\":\"畜牧工程设计服务\"},{\"pinyin\":\"ChuQinChanPinJiaGongFuWu\",\"value\":\"19712a43a88b44c18725835647c59806\",\"label\":\"畜禽产品加工服务\"},{\"pinyin\":\"DaoYouFuWu\",\"value\":\"FW16-0002\",\"label\":\"导游服务\"},{\"pinyin\":\"DaoLuYunShuFuWu\",\"value\":\"FW10-0002\",\"label\":\"道路运输服务\"},{\"pinyin\":\"DongWuJianYanFuWu\",\"value\":\"FW12-0001\",\"label\":\"动物检验服务\"},{\"pinyin\":\"DongWuQuan、SheQingJieFuWu\",\"value\":\"FW06-0004\",\"label\":\"动物圈、舍清洁服务\"},{\"pinyin\":\"FaLvZiXunFuWu\",\"value\":\"33f88c18a2974df1a4f802d4f79d661b\",\"label\":\"法律咨询服务\"},{\"pinyin\":\"FangYangFuWu\",\"value\":\"FW06-0005\",\"label\":\"放养服务\"},{\"pinyin\":\"FeiShuiGuanLiFuWu\",\"value\":\"FW0505-0001\",\"label\":\"肥水管理服务\"},{\"pinyin\":\"FuYuGuanLiFuWu\",\"value\":\"FW0504-0001\",\"label\":\"抚育管理服务\"},{\"pinyin\":\"GuanDaoYunShuFuWu\",\"value\":\"FW10-0006\",\"label\":\"管道运输服务\"},{\"pinyin\":\"GuangGaoFuWu\",\"value\":\"FW11-0001\",\"label\":\"广告服务\"},{\"pinyin\":\"HangKongYunShuFuWu\",\"value\":\"FW10-0005\",\"label\":\"航空运输服务\"},{\"pinyin\":\"HuaFeiJianYanFuWu\",\"value\":\"FW12-0006\",\"label\":\"化肥检验服务\"},{\"pinyin\":\"HuiYiJiZhanLanFuWu\",\"value\":\"FW11-0002\",\"label\":\"会议及展览服务\"},{\"pinyin\":\"JiBoFuWu\",\"value\":\"d449e907d424450b82a07311ff34c8c4\",\"label\":\"机播服务\"},{\"pinyin\":\"JiChaFuWu\",\"value\":\"b2f95e3946714070a3d9ac94382f2fe9\",\"label\":\"机插服务\"},{\"pinyin\":\"JiFangFuWu\",\"value\":\"f92838c61ff449c7b252d561d4c7281f\",\"label\":\"机防服务\"},{\"pinyin\":\"JiGengFuWu\",\"value\":\"3f26fef7fb1c40e89979fc28a5273649\",\"label\":\"机耕服务\"},{\"pinyin\":\"JiXieYiQiSheBeiJianYanFuWu\",\"value\":\"FW12-0010\",\"label\":\"机械仪器设备检验服务\"},{\"pinyin\":\"JiLiangFuWu\",\"value\":\"FW13-0003\",\"label\":\"计量服务\"},{\"pinyin\":\"KeJiJiaoLiuHeTuiGuangFuWu\",\"value\":\"FW01-0002\",\"label\":\"科技交流和推广服务\"},{\"pinyin\":\"LvYouFuWu\",\"value\":\"FW16-0001\",\"label\":\"旅游服务\"},{\"pinyin\":\"MiaoZhongPeiYuFuWu\",\"value\":\"FW07-0001\",\"label\":\"苗种培育服务\"},{\"pinyin\":\"NongChuChanPinLingShou\",\"value\":\"FW11-0004\",\"label\":\"农畜产品零售\"},{\"pinyin\":\"NongChuChanPinPiFa\",\"value\":\"FW11-0003\",\"label\":\"农畜产品批发\"},{\"pinyin\":\"NongJiJianYanFuWu\",\"value\":\"FW12-0011\",\"label\":\"农机检验服务\"},{\"pinyin\":\"NongJiaLeYuan\",\"value\":\"FW21-0001\",\"label\":\"农家乐园\"},{\"pinyin\":\"NongLinChanPinBaoZhuangSheJiFuWu \",\"value\":\"a2d21a662544423caeb5105483814d53\",\"label\":\"农林产品包装设计服务 \"},{\"pinyin\":\"NongLinChanPinJiaGongFuWu\",\"value\":\"14d47084b51c47ce8edff257073f61bd\",\"label\":\"农林产品加工服务\"},{\"pinyin\":\"NongLinGongChengSheJiFuWu\",\"value\":\"FW3109-0001\",\"label\":\"农林工程设计服务\"},{\"pinyin\":\"NongLinSheShiJianSheFuWu\",\"value\":\"FW3602-0001\",\"label\":\"农林设施建设服务\"},{\"pinyin\":\"NongYaoJianYanFuWu\",\"value\":\"FW12-0005\",\"label\":\"农药检验服务\"},{\"pinyin\":\"NongYeJiShuZiXunFuWu\",\"value\":\"FW02-0001\",\"label\":\"农业技术咨询服务\"},{\"pinyin\":\"NongYeKeXueYanJiuFuWu\",\"value\":\"FW01-0001\",\"label\":\"农业科学研究服务\"},{\"pinyin\":\"RenZhengFuWu\",\"value\":\"FW13-0001\",\"label\":\"认证服务\"},{\"pinyin\":\"ShengChanGongZuoYongJuJianYanFuWu\",\"value\":\"FW12-0009\",\"label\":\"生产工作用具检验服务\"},{\"pinyin\":\"ShiPinJianYanFuWu\",\"value\":\"FW12-0003\",\"label\":\"食品检验服务\"},{\"pinyin\":\"ShouGeFuWu\",\"value\":\"29c3e5aba95e44eb938ef582008930a1\",\"label\":\"收割服务\"},{\"pinyin\":\"ShouLieBuZhuoFuWu\",\"value\":\"FW06-0007\",\"label\":\"狩猎捕捉服务\"},{\"pinyin\":\"ShouYaoJianYanFuWu\",\"value\":\"FW12-0007\",\"label\":\"兽药检验服务\"},{\"pinyin\":\"ShouYiJiYiBingFangZhiFuWu\",\"value\":\"FW06-0003\",\"label\":\"兽医及疫病防治服务\"},{\"pinyin\":\"ShuiChanPinJiaGongFuWu\",\"value\":\"47ee3b25f30f490ba7f2b55893db3e6e\",\"label\":\"水产品加工服务\"},{\"pinyin\":\"ShuiLiGongChengSheJiFuWu\",\"value\":\"FW3109-0004\",\"label\":\"水利工程设计服务\"},{\"pinyin\":\"ShuiLuYunShuFuWu\",\"value\":\"FW10-0004\",\"label\":\"水路运输服务\"},{\"pinyin\":\"ShuiShengDongWu（LiangQi）YangZhiFuWu\",\"value\":\"FW07-0002\",\"label\":\"水生动物（两栖）养殖服务\"},{\"pinyin\":\"SiLiaoJianYanFuWu\",\"value\":\"FW12-0008\",\"label\":\"饲料检验服务\"},{\"pinyin\":\"TieLuYunShuFuWu\",\"value\":\"FW10-0001\",\"label\":\"铁路运输服务\"},{\"pinyin\":\"TuDiZhiLi（GengZheng）FuWu\",\"value\":\"FW0502-0001\",\"label\":\"土地治理（耕整）服务\"},{\"pinyin\":\"XuanGengFuWu\",\"value\":\"a6dc2bfbff334183999ed815710b9ed6\",\"label\":\"旋耕服务\"},{\"pinyin\":\"YaoPinJianYanFuWu\",\"value\":\"FW12-0004\",\"label\":\"药品检验服务\"},{\"pinyin\":\"YiBingFangZhiFuWu\",\"value\":\"FW07-0003\",\"label\":\"疫病防治服务\"},{\"pinyin\":\"YinXingFuWu\",\"value\":\"FW35-0001\",\"label\":\"银行服务\"},{\"pinyin\":\"YouZhengJvalueiFuWu\",\"value\":\"FW10-0008\",\"label\":\"邮政寄递服务\"},{\"pinyin\":\"YouChu（Chu）FuYuFuWu\",\"value\":\"FW06-0002\",\"label\":\"幼畜（雏）抚育服务\"},{\"pinyin\":\"YuYeGongChengSheJiFuWu\",\"value\":\"FW3109-0003\",\"label\":\"渔业工程设计服务\"},{\"pinyin\":\"YuYeSheShiJianSheFuWu\",\"value\":\"FW3602-0003\",\"label\":\"渔业设施建设服务\"},{\"pinyin\":\"YuZhong（Miao）FuWu\",\"value\":\"FW0501-0001\",\"label\":\"育种（苗）服务\"},{\"pinyin\":\"ZaiPei（YiZai）FuWu\",\"value\":\"FW0503-0001\",\"label\":\"栽培（移栽）服务\"},{\"pinyin\":\"ZhengQuanFuWu\",\"value\":\"FW35-0002\",\"label\":\"证券服务\"},{\"pinyin\":\"ZhiBaoFuWu\",\"value\":\"FW0506-0001\",\"label\":\"植保服务\"},{\"pinyin\":\"ZhiWuJianYanFuWu\",\"value\":\"FW12-0002\",\"label\":\"植物检验服务\"},{\"pinyin\":\"ZhongChu（Qin）FanYuFuWu\",\"value\":\"FW06-0001\",\"label\":\"种畜（禽）繁育服务\"},{\"pinyin\":\"ZhuangXieBanYunFuWu\",\"value\":\"FW10-0007\",\"label\":\"装卸搬运服务\"}]";

    @Autowired
    private SystemDictService systemDictService;
    /**
     * 根据上级获取下级数据
     *
     * @return
     */
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public Result next(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String keyword = (String) params.get("keyword");
        String parentId = (String) params.get("parentId");
        String character = (String) params.get("character");
        JSONArray array = JSON.parseArray(SERVICE_DATA);
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (Object obj : array) {
            String value = ((JSONObject) obj).get("value").toString();
            String label = ((JSONObject) obj).get("label").toString();
            String pinyin = ((JSONObject) obj).get("pinyin").toString();
            if (!StringUtils.isEmpty(keyword)) {
                if (!label.contains(keyword)) {
                    continue;
                }
            }
            if (!StringUtils.isEmpty(parentId)) {
                if (!value.startsWith(parentId)) {
                    continue;
                }
            }
            if (!StringUtils.isEmpty(character)) {
                if (!pinyin.startsWith(character.toUpperCase())) {
                    continue;
                }
            }
            list.add((JSONObject) obj);
        }
        Result result = new Result();
        result.setData(list);
        return result;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result insert(@RequestBody HttpServletRequest request) {

        JSONArray array = JSON.parseArray(SERVICE_DATA);
        for (Object obj : array) {
            Map<String, Object> params = new HashMap<>();
            String value = ((JSONObject) obj).get("value").toString();
            String label = ((JSONObject) obj).get("label").toString();
            String pinyin = ((JSONObject) obj).get("pinyin").toString();
            String uuid = UUID.randomUUID().toString();
            params.put("dict_id",uuid);
            params.put("dict_type","d2389207-0661-11e8-ab8c-005056bc3817");
            params.put("dict_value",value);
            params.put("dict_name",label);
            params.put("pinyin",pinyin);
            systemDictService.insertServise(params);

        }
        Result result = new Result();

        return result;
    }




}