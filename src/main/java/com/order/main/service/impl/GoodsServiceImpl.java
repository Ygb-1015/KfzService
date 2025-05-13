package com.order.main.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.exceptions.ForestNetworkException;
import com.order.main.dto.requst.*;
import com.order.main.dto.response.*;
import com.order.main.service.GoodsService;
import com.order.main.service.client.ErpClient;
import com.order.main.service.client.PhpClient;
import com.order.main.util.*;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopBaseHttpResponse;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddGoodsAddRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddGoodsAddResponse;
import com.pdd.pop.sdk.http.api.pop.response.PddGoodsCatRuleGetResponse;
import com.pdd.pop.sdk.http.api.pop.response.PddGoodsSpecIdGetResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    // 日志记录器
    private static final Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);

    /**
     * 锁对象
     */
    private final Map<String, Object> lockMap = new HashMap<>();
    private final Map<String, Boolean> runMap = new HashMap<>();

    @Autowired
    private ErpClient erpClient;

    @Autowired
    private PhpClient phpClient;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    public Boolean synchronizationGoods(Long shopId) {
        // 根据店铺Id查询店铺信息
        ShopVo shopInfo = erpClient.getShopInfo(ClientConstantUtils.ERP_URL, shopId);
        Assert.isTrue(ObjectUtil.isNotEmpty(shopInfo), "查询不到店铺信息");
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 定义格式：YYYY-MM-DD HH:mm:ss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化当前时间
        String currentDateTime = now.format(formatter);
        // 从Redis中获取上次同步时间
        String lastGoodsSynTime = (String) redisUtils.getCacheObject("lastGoodsSynTime_" + shopInfo.getId());
        ThreadPoolUtils.execute(() -> {
            List<GetShopGoodsListResponse.ShopGoods> shopGoodsList = queryShopGoods(shopInfo.getToken(), shopInfo.getRefreshToken(), shopId, lastGoodsSynTime);
            List<ZhishuShopGoodsRequest> zhishuShopGoodsRequestList = shopGoodsList.stream().map(shopGoods -> {
                ZhishuShopGoodsRequest zhishuShopGoodsRequest = new ZhishuShopGoodsRequest();
                zhishuShopGoodsRequest.setUserId(shopInfo.getCreateBy().toString());
                zhishuShopGoodsRequest.setProductId(shopGoods.getItemId().toString());
                zhishuShopGoodsRequest.setGoodsName(shopGoods.getItemName());
                zhishuShopGoodsRequest.setIsbn(shopGoods.getIsbn());
                zhishuShopGoodsRequest.setArtNo(shopGoods.getItemSn());
                zhishuShopGoodsRequest.setStock(0L);
                zhishuShopGoodsRequest.setPrice(shopGoods.getPrice());
                zhishuShopGoodsRequest.setConditionCode(shopGoods.getQuality());
                zhishuShopGoodsRequest.setCreateBy(shopInfo.getCreateBy());
                zhishuShopGoodsRequest.setItemNumber(shopGoods.getItemId().toString());
                zhishuShopGoodsRequest.setItemNumber(shopGoods.getItemId().toString());
                zhishuShopGoodsRequest.setFixPrice(shopGoods.getOriPrice());
                zhishuShopGoodsRequest.setInventory(shopGoods.getNumber());
                zhishuShopGoodsRequest.setBookPic("https://www0.kfzimg.com/" + shopGoods.getImgUrl());
                // zhishuShopGoodsBo.setIsArtNoConversion(1);
                return zhishuShopGoodsRequest;
            }).collect(Collectors.toList());
            GoodsComparisonRequest request = new GoodsComparisonRequest();
            request.setShopId(shopId);
            request.setUserId(shopInfo.getCreateBy());
            request.setZhishuShopGoodsRequestList(zhishuShopGoodsRequestList);
            erpClient.goodsComparison(ClientConstantUtils.ERP_URL, request);
            redisUtils.setCacheObject("lastGoodsSynTime_" + shopInfo.getId(), currentDateTime);
        });

        return true;
    }

    public List<GetShopGoodsListResponse.ShopGoods> queryShopGoods(String token, String refreshToken, Long shopId, String lastGoodsSynTime) {
        boolean isHaveNext = true;
        Integer pageNum = 1;
        List<GetShopGoodsListResponse.ShopGoods> shopGoodsList = new ArrayList<>();

        boolean isRefreshToken = false;

        while (isHaveNext) {
            // 构建查询店铺商品请求参数
            GetShopGoodsListRequest getShopGoodsListRequest = new GetShopGoodsListRequest();
            getShopGoodsListRequest.setToken(token);
            getShopGoodsListRequest.setPageNum(pageNum);
            getShopGoodsListRequest.setPageSize(100);
            // 判断是不是第一次，不是拼接上次同步时间作为筛选条件
            if (ObjectUtil.isNotNull(lastGoodsSynTime)) {
                getShopGoodsListRequest.setAddTimeBegin(lastGoodsSynTime);
            }

            KfzBaseResponse<GetShopGoodsListResponse> shopGoodsResponse = phpClient.getShopGoodsList(ClientConstantUtils.PHP_URL, getShopGoodsListRequest);
            log.info("查询孔夫子店铺商品响应-{}", JSONObject.toJSONString(shopGoodsResponse));
            if (!isRefreshToken && ObjectUtil.isNotEmpty(shopGoodsResponse.getErrorResponse())) {
                List<Long> tokenErrorCode = new ArrayList<>();
                tokenErrorCode.add(1000L);
                tokenErrorCode.add(1001L);
                tokenErrorCode.add(2000L);
                tokenErrorCode.add(2001L);
                if (tokenErrorCode.contains(shopGoodsResponse.getErrorResponse().getCode())) {
                    token = tokenUtils.refreshToken(refreshToken, shopId);
                    isRefreshToken = true;
                } else {
                    throw new RuntimeException("查询孔夫子店铺商品异常-" + JSONObject.toJSONString(shopGoodsResponse));
                }
            } else {
                if (ObjectUtil.isNotEmpty(shopGoodsResponse.getSuccessResponse().getList())) {
                    shopGoodsList.addAll(shopGoodsResponse.getSuccessResponse().getList());
                    if (shopGoodsResponse.getSuccessResponse().getPages() <= shopGoodsResponse.getSuccessResponse().getPageNum())
                        isHaveNext = false;
                    pageNum++;
                } else {
                    isHaveNext = false;
                }
            }
        }
        return shopGoodsList;
    }

    /**
     * 获取店铺详情
     */
    @Override
    public String getShopInfo(String token){
        KfzBaseResponse<GetShopInfoResponse> response = phpClient.getShopInfo(ClientConstantUtils.PHP_URL, token);
        if(response.getErrorResponse() != null){
            return "";
        }
        return JsonUtil.transferToJson(response.getSuccessResponse());
    }

    @Override
    public Boolean updateArtNo(UpdateArtNoRequest request) {
        // 根据店铺Id查询店铺信息
        ShopVo shopInfo = erpClient.getShopInfo(ClientConstantUtils.ERP_URL, request.getShopId());
        Assert.isTrue(ObjectUtil.isNotEmpty(shopInfo), "查询不到店铺信息");
        String token = shopInfo.getToken();
        String refreshToken = shopInfo.getRefreshToken();
        for (int i = 0; i < 2; i++) {
            ItemItemsnUpdateRequest updateRequest = new ItemItemsnUpdateRequest();
            updateRequest.setToken(token);
            updateRequest.setItemId(request.getProductId());
            updateRequest.setItemSn(request.getArtNo());
            KfzBaseResponse<ItemItemsnUpdateResponse> response = new KfzBaseResponse<>();
            try {
                response = phpClient.itemItemsnUpdate(ClientConstantUtils.PHP_URL, updateRequest);
                if (ObjectUtil.isNotEmpty(response.getErrorResponse())) {
                    List<Long> tokenErrorCode = new ArrayList<>();
                    tokenErrorCode.add(1000L);
                    tokenErrorCode.add(1001L);
                    tokenErrorCode.add(2000L);
                    tokenErrorCode.add(2001L);
                    if (tokenErrorCode.contains(response.getErrorResponse().getCode())) {
                        token = tokenUtils.refreshToken(refreshToken, request.getShopId());
                    } else {
                        throw new RuntimeException("更新孔夫子商品货号异常-" + JSONObject.toJSONString(response));
                    }
                } else {
                    Assert.isTrue(ObjectUtil.isNotEmpty(response.getSuccessResponse()), "更新孔夫子商品货号异常-" + JSONObject.toJSONString(response));
                    i++;
                }
            } catch (ForestNetworkException e) {
                throw new ForestNetworkException(e.getMessage(), e.getStatusCode(), e.getResponse());
            }
        }
        return true;
    }

    @Override
    public String getTemplateSimpleList(String token) {
        return phpClient.getTemplateSimpleList(ClientConstantUtils.PHP_URL, token);
    }

    @Override
    public String getCategory(String token) {
        return phpClient.getCategory(ClientConstantUtils.PHP_URL, token);
    }

    @Override
    public String itemAdd(GoodsItemAddRequest request){
        return phpClient.itemAdd(ClientConstantUtils.PHP_URL, request);
    }

    @Override
    public String upload(String file,String token){
        return phpClient.upload(ClientConstantUtils.PHP_URL, file,token);
    }

    @Override
    public String itemNumberUpdate(String token,String itemId,String number){
        return phpClient.itemNumberUpdate(ClientConstantUtils.PHP_URL, token,itemId,number);
    }

    @Override
    public String getBookInfoF(String isbn){
        return phpClient.getBookInfoF(ClientConstantUtils.PHP_URL, isbn);
    }

    @Override
    public void goodsAddOne(Map map){
        GoodsItemAddRequest request = new GoodsItemAddRequest();

        request.setToken(map.get("token").toString());
        request.setTpl(map.get("tpl").toString());
        request.setCatId("43000000000000000");
        request.setMyCatId(map.get("myCatId") == null ? "" : map.get("myCatId").toString());
        request.setItemName(map.get("itemName") == null ? "" : map.get("itemName").toString());
        request.setImportantDesc(map.get("importantDesc") == null ? "" : map.get("importantDesc").toString());
        request.setPrice(new BigDecimal(map.get("price").toString()).divide(new BigDecimal(100)).toString());
        request.setNumber(map.get("number") == null ? "" : map.get("number").toString());
        request.setQuality(map.get("quality") == null ? "" : map.get("quality").toString());
        request.setQualityDesc(map.get("qualityDesc") == null ? "" : map.get("qualityDesc").toString());
        request.setItemSn(map.get("itemSn") == null ? "" : map.get("itemSn").toString());
        request.setImgUrl(map.get("imgUrl") == null ? "" : map.get("imgUrl").toString());
        request.setOriPrice(map.get("oriPrice") == null ? "" : map.get("oriPrice").toString());

        //获取实拍图网路路径
        String[] imagesArr = map.get("images") == null ? new String[0] : map.get("images").toString().split(";");

        String images = "";
        for(int i=0;i<imagesArr.length;i++){

            String iamge = imagesArr[i];

            Map dataMap;
            try{
                dataMap = JsonUtil.transferToObj(upload(iamge,request.getToken()), Map.class);
            }catch (Exception e){
                System.out.println("上传图片异常---------------");
                e.printStackTrace();
                continue;
            }

            Map errorResponse = (Map) dataMap.get("errorResponse");
            if(errorResponse != null){
                System.out.println("上传图片报错---------errorResponse------："+errorResponse);
                continue;
            }

            Map successResponse = (Map) dataMap.get("successResponse");
            Map kongkzImage = (Map) successResponse.get("image");

            if(i==0){
                images = kongkzImage.get("url").toString();
            }else{
                images = images + ";" + kongkzImage.get("url").toString();
            }
            //当图片数量不足八张时
            if(i == imagesArr.length-1 && i < 8){
                for(int j=i;j<7;j++){
                    images = images + ";" + kongkzImage.get("url").toString();
                }
            }
        }

        request.setImages(images);

        request.setItemDesc(map.get("itemDesc") == null ? "" : map.get("itemDesc").toString());
        request.setBearShipping(map.get("bearShipping").toString());
        request.setMouldId(Long.parseLong(map.get("mouldld").toString()));
        request.setWeight(map.get("weight") == null ? BigDecimal.ZERO : new BigDecimal(map.get("weight").toString()));
        request.setWeightPiece(map.get("weightPiece") == null ? BigDecimal.ZERO : new BigDecimal(map.get("weightPiece").toString()));

        request.setIsbn(map.get("isbn") == null ? "" : map.get("isbn").toString());
        request.setAuthor(map.get("author") == null ? "" : map.get("author").toString());
        request.setPress(map.get("press") == null ? "" : map.get("press").toString());
        request.setPubDate(map.get("pubDate") == null ? "" : map.get("pubDate").toString());
        request.setBinding(map.get("binding") == null ? "" : map.get("binding").toString());
        request.setBinding(map.get("otherName") == null ? "" : map.get("otherName").toString());


        Map dataMap = JsonUtil.transferToObj(itemAdd(request), Map.class);
        System.out.println("-----------------------调用上传商品接口："+JsonUtil.transferToJson(dataMap));
        Map errorResponse  = (Map) dataMap.get("errorResponse");
        if(errorResponse != null){
            System.out.println("---------------------上传报错");
            System.out.println(JsonUtil.transferToJson(errorResponse)+"------------");
        }else{
            System.out.println("---------------------上传成功");
            Map successResponse = (Map) dataMap.get("successResponse");
            Map item = (Map) successResponse.get("item");

            Map callBackMap = new HashMap();
            callBackMap.put("shopId", map.get("shopId").toString());
            callBackMap.put("goodId", map.get("goodId").toString());
            callBackMap.put("itemId", item.get("itemId"));
            callBackMap.put("userId", map.get("userId").toString());
            //调用接口
            System.out.println("-----------------------调用新增发布商品接口");
            InterfaceUtils.getInterfacePost("/api/kongfz/goodAddCallBack", callBackMap);
        }
    }

    @Override
    public void goodsAddMain(Map map, Map dataMap) {

        // 页面填写数据Map
        Map htmlData = (Map) map.get("htmlData");
        // 任务信息对象
        Map taskBo = (Map) map.get("taskBo");
        // 店铺数据List
        List<Map> shopVoMapList = (List<Map>) map.get("shopVoList");
        // 数据过滤Map
        Map filterMap = (Map) dataMap.get("filterMap");

        // 上架状态
        String listStatus = htmlData.get("listStatus").toString();
        // 图书类目
        String bookCategory = htmlData.get("bookCategory").toString();
        //自选类目
        String bookCategoryAppoint = htmlData.get("bookCategoryAppoint") == null ? "" : htmlData.get("bookCategoryAppoint").toString();

        // 图书数据List
        List<Map> bookBaseInfoVoList = (List<Map>) map.get("bookBaseInfoVoList");
        // 线程锁对象初始化
        lockMap.put(String.valueOf(Thread.currentThread().getId()), new Object());
        runMap.put(String.valueOf(Thread.currentThread().getId()), false);

        // 创建表头
        Map<String, String> headMap = new HashMap<>();
        headMap.put("bookNum", "书号");
        headMap.put("price", "价格");
        headMap.put("stock", "库存");
        headMap.put("logMsg", "日志");
        // 创建执行日志
        EasyExcelUtil.writeJsonToFile(taskBo.get("id") + "-msg.txt", "开始执行文件" + System.lineSeparator() +
                "开始进行数据加载" + System.lineSeparator() +
                "数据加载完毕" + System.lineSeparator() +
                "正在执行" + System.lineSeparator() +
                "已执行数据：0条" + System.lineSeparator() +
                "待执行数据：" + bookBaseInfoVoList.size() + "条");
        // 获取系统设置白名单/黑名单
        String systemWhiteStr = filterMap.get("System-whiteStr") != null ? filterMap.get("System-whiteStr").toString() : "";
        String systemBlackStr = filterMap.get("System-blackStr") != null ? filterMap.get("System-blackStr").toString() : "";
        for (Map shopVoMap : shopVoMapList) {
            if(!shopVoMap.get("shopType").equals("2")){
                continue;
            }
            // 创建文件生成路径
            String filePath = UrlUtil.getUrl() + taskBo.get("id") + shopVoMap.get("id") + ".xlsx";
            // 生成excel文件
            EasyExcelUtil.writeExcel(filePath, new ArrayList<>(), null);
            // 获取用户自定义白名单/黑名单
            String whiteStr = filterMap.get(shopVoMap.get("id") + "-whiteStr") != null ? filterMap.get(shopVoMap.get("id") + "-whiteStr").toString() : "";
            String blackStr = filterMap.get(shopVoMap.get("id") + "-blackStr") != null ? filterMap.get(shopVoMap.get("id") + "-blackStr").toString() : "";

            //增加数据
            goodsAdd(shopVoMap,bookBaseInfoVoList,listStatus,bookCategoryAppoint,filePath,String.valueOf(taskBo.get("id")),map,systemWhiteStr,systemBlackStr,whiteStr,blackStr,bookCategory);
        }

        // 写入文件
        EasyExcelUtil.writeJsonToFile(taskBo.get("id") + "-msg.txt", "开始执行文件" + System.lineSeparator() +
                "开始进行数据加载" + System.lineSeparator() +
                "数据加载完毕" + System.lineSeparator() +
                "正在执行" + System.lineSeparator() +
                "已执行数据：" + bookBaseInfoVoList.size() + "条" + System.lineSeparator() +
                "待执行数据：0 条 " + System.lineSeparator() +
                "文件执行完成");
        // 调用完成任务接口
        InterfaceUtils.getInterface("/zhishu/task/finishTash/" + taskBo.get("id"));
    }

    /**
     *
     * @param shopVo              店铺信息
     * @param bookBaseInfoVoList      商品信息
     * @param listStatus              上架状态
     * @param bookCategoryAppoint     类目
     * @param filePath                店铺总数居excel路径
     * @param taskId                  任务id
     * @param map                     总数据map
     * @param systemWhiteStr            系统过滤白名单
     * @param systemBlackStr            系统过滤黑名单
     * @param whiteStr                  用户自定义白名单
     * @param blackStr                  用户自定义黑名单
     * @param bookCategory              图书类目： 0 自动分类  1 手动选择固定类目
     */
    public void goodsAdd(Map shopVo,
                         List<Map> bookBaseInfoVoList,
                         String listStatus,
                         String bookCategoryAppoint,
                         String filePath,
                         String taskId,
                         Map map,
                         String systemWhiteStr,
                         String systemBlackStr,
                         String whiteStr,
                         String blackStr,
                         String bookCategory) {
        //店铺详细设置数据
        Map shopDetailVo = (Map) map.get(shopVo.get("id")+"shopDetailVo");
        //销售模板数据
        Map priceTemplateVo = (Map) map.get(shopVo.get("id")+"priceTemplateVo");
        //获取自定义过滤设置
        String[] mySetUpArr = shopDetailVo.get("mySetUp") != null ? shopDetailVo.get("mySetUp").toString().split(",") : new String[0];
        //获取系统过滤设置
        String[] systemSetUpArr = shopDetailVo.get("systemSetUp") != null ? shopDetailVo.get("mySetUp").toString().split(",")  : new String[0];
        //获取系统默认ISBN黑名单
        String isbnNoStr = EasyExcelUtil.readFileContentString(UrlUtil.getUrl()+"isbnNo.txt");

        /**
         * 封面水印  specSyImageUrl
         */
        String specSyImageUrl = "";
        //获取店铺图片
        List<Map> shopImgList = map.get(shopVo.get("id")+"shopImg") == null ? new ArrayList<>() : (List<Map>) map.get(shopVo.get("id")+"shopImg");
        for (Map shopImgMap : shopImgList){
            specSyImageUrl = shopImgMap.get("absolutePath").toString();
        }

        //记录日志信息
        Map<String,String> logsMap = new HashMap<>();

        //记录循环次数
        int markI = 1;
        for(Map bookBaseInfoVo : bookBaseInfoVoList){
            //暂停/启动线程
            taskWait(String.valueOf(Thread.currentThread().getId()));
            //创建数据List
            List<String> dataList = new ArrayList<String>();
            dataList.add(String.valueOf(bookBaseInfoVo.get("isbn")));
            dataList.add(bookBaseInfoVo.get("bookName") == null ? "" : bookBaseInfoVo.get("bookName").toString());
            dataList.add(new BigDecimal(bookBaseInfoVo.get("price").toString()).divide(new BigDecimal(100)).toString());
            dataList.add(String.valueOf(bookBaseInfoVo.get("stock")));
            if(bookBaseInfoVo.get("msg") != null && bookBaseInfoVo.get("msg").equals("无数据")){
                wirteExcel(taskId,shopVo,bookBaseInfoVoList,dataList,logsMap,filePath,"未获取到商品信息");
                continue;
            }
            if(bookBaseInfoVo.get("bookName") == null){
                wirteExcel(taskId,shopVo,bookBaseInfoVoList,dataList,logsMap,filePath,"未获取到书名信息");
                continue;
            }

            /**
             * 系统默认黑名单
             */
            Boolean systemBlack = false;
            //校验ISBN
            String isbn = bookBaseInfoVo.get("isbn") == null ? "" : bookBaseInfoVo.get("isbn").toString();
            if(StringUtils.isNotEmpty(isbn)){
                systemBlack = isbnNoStr.contains(bookBaseInfoVo.get("isbn").toString());
            }
            //校验书名
            systemBlack = systemBlack ? systemBlack : checkString(bookBaseInfoVo.get("bookName").toString(),  BookFilterUtil.BOOKNAMEFILTERSTR);
            if(systemBlack){
                wirteExcel(taskId,shopVo,bookBaseInfoVoList,dataList,logsMap,filePath,"该商品存在黑名单中");
                continue;
            }
            //校验白名单和黑名单
            Boolean whiteBool = false;
            //校验系统白名单  0 isbn 1 书名 2 作者 3 出版社 4 出版时间 5 类目  敏感词 7 限价库
            for (String systemId : systemSetUpArr){
                whiteBool = switchBool(systemId,bookBaseInfoVo,systemWhiteStr);
                if(whiteBool){
                    //白名单校验成功，跳出循环
                    break;
                }
            }
            //校验自定义白名单  0 isbn 1 书名 2 作者 3 出版社 4 出版时间 5 类目  敏感词 7 限价库
            for (String systemId : mySetUpArr){
                whiteBool = switchBool(systemId,bookBaseInfoVo,whiteStr);
                if(whiteBool){
                    //白名单校验成功，跳出循环
                    break;
                }
            }
            if(!whiteBool){
                Boolean blackBool = false;
                //校验系统黑名单  0 isbn 1 书名 2 作者 3 出版社 4 出版时间 5 类目  敏感词 7 限价库
                for (String systemId : systemSetUpArr){
                    blackBool = switchBool(systemId,bookBaseInfoVo,systemBlackStr);
                    if(blackBool){
                        //黑名单校验成功，跳出循环
                        break;
                    }
                }
                //校验用户黑名单  0 isbn 1 书名 2 作者 3 出版社 4 出版时间 5 类目  敏感词 7 限价库
                for (String systemId : mySetUpArr){
                    blackBool = switchBool(systemId,bookBaseInfoVo,blackStr);
                    if(blackBool){
                        //黑名单校验成功，跳出循环
                        break;
                    }
                }
                if(blackBool){
                    //黑名单校验成功，跳出循环
                    wirteExcel(taskId,shopVo,bookBaseInfoVoList,dataList,logsMap,filePath,"该商品存在黑名单中");
                    continue;
                }
            }
            //校验是否已发布，是否存在
            if(bookBaseInfoVo.get("goodsId") != null){
                String goodsFilterStr = EasyExcelUtil.readFileContentString(UrlUtil.getUrl()+"/"+taskId+shopVo.get("id")+"-filter.txt");
                if(goodsFilterStr != null){
                    Boolean exists = goodsFilterStr.contains(bookBaseInfoVo.get("goodsId").toString());
                    if(exists){
                        //写入日志
                        wirteExcel(taskId,shopVo,bookBaseInfoVoList,dataList,logsMap,filePath,"商品已发布,无法继续发布");
                        continue;
                    }
                }
            }

            map.put("shopId",shopVo.get("id"));
            map.put("goodId", bookBaseInfoVo.get("goodsId"));
            map.put("token",shopVo.get("token"));
            map.put("tpl",shopDetailVo.get("bookTemplate"));
            map.put("catId","");
            map.put("myCatId","");
            /**
             * 商品标题
             */
            map.put("itemName",getGoodTitle(shopDetailVo,bookBaseInfoVo));
            map.put("importantDesc",shopDetailVo.get("recommend"));
            /**
             * 价格
             */
            map.put("price",getPrice(shopDetailVo,bookBaseInfoVo,priceTemplateVo));
            /**
             * 库存
             */
            map.put("number",bookBaseInfoVo.get("stock"));
            /**
             * 品相
             */
            if(bookBaseInfoVo.get("conditionCode") != null){
                map.put("quality",bookBaseInfoVo.get("conditionCode"));
            }else{
                map.put("quality",shopDetailVo.get("conditionDef"));
            }
            map.put("qualityDesc",shopDetailVo.get("conditionDes"));
            /**
             * 货号
             */
            map.put("itemSn",bookBaseInfoVo.get("artNo"));
            /**
             * 图片
             */
            //获取实拍图
            String[] useImages = bookBaseInfoVo.get("useImages") != null ? bookBaseInfoVo.get("useImages").toString().split(";") : new String[0];
            //无水印图片记录
            String noSyImage = "";
            String images = "";

            if(useImages.length == 0){
                //没有实拍图则获取中心书库的图片
                String image = "http://111.229.25.150:8001/" + bookBaseInfoVo.get("bookPic");

                //如果图片访问不到
                Boolean bool = ImageUtils.isImageExists(image);
                if(!bool){
                    //白底图
                    image = getWhiteImage(bookBaseInfoVo.get("bookName").toString());
                    noSyImage = image;
                }else{
                    noSyImage = image;
                    //图片增加水印，若报错则返回白底图
                    if(specSyImageUrl != ""){
                        image = getImageSy(specSyImageUrl,image,bookBaseInfoVo.get("bookName").toString());
                    }
                }
                System.out.println("中央书库上传首图-------------"+image+"；书名:"+bookBaseInfoVo.get("bookName"));
                map.put("imgUrl", image);
                //实拍图
                images = image;
            }else{
                //第一张图作为首图,第一张肯定有水印
                String imageFirst = UploadUtil.getFiles("",bookBaseInfoVo.get("bookName").toString()) + useImages[0];
                noSyImage = imageFirst;
                if(specSyImageUrl != ""){
                    imageFirst = getImageSy(specSyImageUrl,imageFirst,bookBaseInfoVo.get("bookName").toString());
                }
                System.out.println("用户上传首图-------------"+imageFirst+"；书名:"+bookBaseInfoVo.get("bookName").toString());
                map.put("imgUrl", imageFirst);
                // WatermarkPosition 水印位置 0 全部  1第一张
                if(shopDetailVo.get("watermarkPosition").equals("0")){
                    //为0是全部增加水印
                    for(String useImage : useImages){

                        //图片路径
                        String iamge = UploadUtil.getFiles("",bookBaseInfoVo.get("bookName").toString()) + useImage;
                        //水印不为空则增加水印
                        if(specSyImageUrl != ""){
                            iamge = getImageSy(specSyImageUrl,iamge,bookBaseInfoVo.get("bookName").toString());
                        }
                        System.out.println("用户上传后续-------------"+iamge+"；书名:"+bookBaseInfoVo.get("bookName").toString());
                        //记录图片
                        if(images == ""){
                            images = iamge;
                        }else{
                            images = images + ";" + iamge;
                        }
                    }
                }else{
                    //实拍图 为1时，正常不加水印图片
                    for(String useImage : useImages){
                        String image = UploadUtil.getFiles("",bookBaseInfoVo.get("bookName").toString()) + useImage;
                        System.out.println("实拍图 为1时，正常不加水印图片：用户上传后续-------------"+image+"；书名:"+bookBaseInfoVo.get("bookName").toString());
                        if(images == ""){
                            images = image;
                        }else{
                            images = images + ";" + image;
                        }
                    }
                }
            }
            //只有1张图片，并且只在第一张图片上加水印
            if(images.split(";").length == 1 && shopDetailVo.get("watermarkPosition").equals("1")){
                images = images +";"+ noSyImage;
            }
            int index = images.indexOf(";") + 1;
            if(index != -1){
                images = images.substring(index);
            }

            map.put("images",images);

            map.put("itemDesc",bookBaseInfoVo.get("content"));
            if(shopDetailVo.get("isParcel").equals("0")){
                //不包邮
                map.put("bearShipping","buyer");
            }else{
                //包邮
                map.put("bearShipping","seller");
            }

            map.put("mouldld", shopDetailVo.get("templateId"));
            map.put("weight",shopDetailVo.get("bookWeight"));
            map.put("weightPiece",shopDetailVo.get("standardNumber"));

            map.put("isbn",bookBaseInfoVo.get("isbn"));
            map.put("author",bookBaseInfoVo.get("author"));
            map.put("press",bookBaseInfoVo.get("publisher"));
            map.put("pubDate",bookBaseInfoVo.get("publicationTime"));
            map.put("binding",bookBaseInfoVo.get("bindingLayout"));
            map.put("oriPrice",bookBaseInfoVo.get("fixPrice"));

            goodsAddOne(map);
        }
    }


    public void taskWait(String threadId){
        if(runMap.get(threadId)){
            try {
                synchronized (lockMap.get(threadId)){
                    lockMap.get(threadId).wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void zanTing(String threadId){
        runMap.put(threadId,true);
    }

    @Override
    public void huanXing(String threadId){
        synchronized (lockMap.get(threadId)){
            runMap.put(threadId,false);
            lockMap.get(threadId).notify();
        }
    }

    /**
     * 获取商品标题
     * @return
     */
    public String getGoodTitle(Map shopDetailVo,Map bookBaseInfoVo){
        //获取标题组成的内容
        String titleStr = shopDetailVo.get("titleConsistOf").toString();
        String[] titleOfArr = titleStr.split(",");
        titleStr = "";
        for(int i=0;i<titleOfArr.length;i++){
            String[] titleMark = titleOfArr[i].split(":");
            if(titleMark[1].equals("true")){
                titleStr += "," + titleMark[0];
            }
        }

        String prefix = shopDetailVo.get("titlePrefix") != null ? shopDetailVo.get("titlePrefix").toString() : "";
        String suffix = shopDetailVo.get("titleSuffix") != null ? shopDetailVo.get("titleSuffix").toString() : "";
        String jianGeFu = "";
        String biaoTi = prefix;
        if(shopDetailVo.get("spaceCharacter").equals("1")){
            jianGeFu = " ";
        }

        //获取excel上传书名
        String bookNameExcel = bookBaseInfoVo.get("bookNameExcel") == null ? null : bookBaseInfoVo.get("bookNameExcel").toString();
        if(bookNameExcel != null){
            biaoTi = biaoTi + jianGeFu + bookNameExcel;
        }else{
            String[] titleArr = titleStr.split(",");

            for(String title : titleArr){
                switch (title){
                    case "0" :biaoTi = biaoTi + jianGeFu + (bookBaseInfoVo.get("isbn") == null ? "" : bookBaseInfoVo.get("isbn"));break;
                    case "1" :
                        biaoTi = biaoTi + jianGeFu + (bookBaseInfoVo.get("bookName") == null ? "" : bookBaseInfoVo.get("bookName"));
                        break;
                    case "2" :biaoTi = biaoTi + jianGeFu + (bookBaseInfoVo.get("author") == null ? "" : bookBaseInfoVo.get("author"));break;
                    case "3" :biaoTi = biaoTi + jianGeFu + (bookBaseInfoVo.get("publisher") == null ? "" : bookBaseInfoVo.get("publisher"));break;
                    case "4" :biaoTi = biaoTi + jianGeFu + (bookBaseInfoVo.get("publicationTime") == null ? "" : bookBaseInfoVo.get("publicationTime"));break;
                    case "5" :biaoTi = biaoTi + jianGeFu + (bookBaseInfoVo.get("bindingLayout") == null ? "" : bookBaseInfoVo.get("bindingLayout"));break;
                    case "6" :biaoTi = biaoTi + jianGeFu + (bookBaseInfoVo.get("format") == null ? "" : bookBaseInfoVo.get("format"));break;
                }
            }
        }


        //后缀
        biaoTi = biaoTi + jianGeFu + suffix;
        // 裁剪字符串，保留200个字符
        biaoTi = truncateString(biaoTi, 199);
        return biaoTi;
    }

    /**
     * 获取价格
     */
    public String getPrice(Map shopDetailVo,Map bookBaseInfoVo,Map priceTemplateVo){
        //校验价格区间，低于最小价格则改为最低价
        BigDecimal priceL = null;
        BigDecimal priceOld = new BigDecimal(bookBaseInfoVo.get("price").toString()).divide(new BigDecimal(100));
        BigDecimal highPrice = new BigDecimal(String.valueOf(shopDetailVo.get("highPrice"))).divide(new BigDecimal(100));
        BigDecimal lowPrice = new BigDecimal(String.valueOf(shopDetailVo.get("lowerPrice"))).divide(new BigDecimal(100));

        if(priceOld.compareTo(lowPrice) < 0){
            //小于最低价
            priceL = lowPrice;
        }else if(priceOld.compareTo(highPrice) > 0){
            //大于最高价
            priceL = highPrice;
        }else{
            priceL = priceOld;
        }
        //根据价格模板换算后的价格
        BigDecimal price = new BigDecimal(String.valueOf(priceTemplateVo.get("proportion")))
                .divide(new BigDecimal(100))
                .multiply(priceL)
                .add(priceL)
                .add(new BigDecimal(priceTemplateVo.get("addAmount").toString()))
                .multiply(new BigDecimal(100))
                .setScale(0, RoundingMode.DOWN);

        return price.toString();
    }



    /**
     * 裁剪字符串，保留指定长度的字符（中文占2个字符，其他占1个字符）
     *
     * @param input 输入字符串
     * @param maxLength 最大字符长度
     * @return 裁剪后的字符串
     */
    public static String truncateString(String input, int maxLength) {
        if (input == null || maxLength <= 0) {
            return "";
        }
        int length = 0;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            // 判断是否为中文字符
            if (isChinese(c)) {
                length += 2;
            } else {
                length += 1;
            }
            // 如果超过最大长度，则停止
            if (length > maxLength) {
                break;
            }
            result.append(c);
        }

        return result.toString();
    }

    /**
     * 获取白底图
     */
    public String getWhiteImage(String goodsName){
        String uuid = UUID.randomUUID().toString();
        return ImageUtils.generateImage(goodsName, 800, 800,uuid+".jpg");
    }

    /**
     * 处理水印图片
     * specSyImageUrl 水印图片
     */
    public String getImageSy(String specSyImageUrl,String iamge,String goodsName){

        try {
            return ImageUtils.mergeImages(iamge,specSyImageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return getWhiteImage(goodsName);
        }
    }

    /**
     * 判断字符是否为中文字符
     *
     * @param c 字符
     * @return 是否为中文字符
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;

        }
        return false;
    }


    /**
     * 写入文件
     */
    public void wirteExcel(String taskId,
                           Map shopVo,
                           List<Map> bookBaseInfoVoList,
                           List<String> dataList,
                           Map<String,String> logsMap,
                           String filePath,
                           String msg){
        //记录任务店铺进度
        readTaskShopTxt(taskId,String.valueOf(shopVo.get("id")),String.valueOf(shopVo.get("shopName")),String.valueOf(bookBaseInfoVoList.size()));
        //总文件信息录入
        dataList.add(msg);
        //根据类型分支数据记录
        if(logsMap.get(msg) == null){
            String uuId = String.valueOf(UUID.randomUUID());
            //创建上传成功文件
            EasyExcelUtil.writeExcel(UrlUtil.getUrl()+uuId+".xlsx",new ArrayList<>(),null);
            logsMap.put(msg,uuId);
            EasyExcelUtil.continuousWriting(UrlUtil.getUrl()+uuId+".xlsx",dataList);
        }else{
            EasyExcelUtil.continuousWriting(UrlUtil.getUrl()+logsMap.get(msg)+".xlsx",dataList);
        }
        //记录任务店铺日志详细进度
        readTaskShopLogsTxt(taskId,String.valueOf(shopVo.get("id")),String.valueOf(shopVo.get("shopName")),msg,logsMap.get(msg),String.valueOf(bookBaseInfoVoList.size()));
        EasyExcelUtil.continuousWriting(filePath,dataList);
    }

    /**
     * 存储任务中店铺的进度
     *  根据任务id获取日志文件内容，转为Map，根据店铺id获取店铺执行数量+1，与total进行计算百分比
     * @param taskId    任务id
     * @param shopId    店铺id
     * @param total     店铺任务总数
     */

    public void readTaskShopTxt(String taskId,String shopId,String shopName,String total){
        String filePath = UrlUtil.getUrl()+taskId+".txt";
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(isFileExists(filePath)){
            mapList = EasyExcelUtil.readFileContent(filePath);
        }
        if(mapList.size() == 0){
            Map<String,Object> map = new HashMap<>();
            map.put("shopId",shopId);
            map.put("shopName",shopName);
            map.put("num",1);
            //计算百分比
            map.put("progress", CountUtils.calculatePercentage(Integer.parseInt(total),1));

            map.put("createTime", DateUtilsUtils.getNowDate());
            map.put("updateTime", DateUtilsUtils.getNowDate());
            map.put("taskId",taskId);
            mapList.add(map);
        }else{
            //true为txt文件中不存在店铺信息，false代表存在
            Boolean bool = true;
            //txt文本不为空，则遍历List对象，查看是否存在店铺id，存在，则数量+1,不存在则添加
            for (Map<String, Object> map : mapList) {
                if(map.get("shopId").equals(shopId)){
                    bool = false;
                    int num = Integer.parseInt(String.valueOf(map.get("num"))) + 1;
                    map.put("num",num);
                    map.put("progress",CountUtils.calculatePercentage(Integer.parseInt(total),num));
                }
            }
            if(bool){
                Map<String,Object> map = new HashMap<>();
                map.put("shopId",shopId);
                map.put("shopName",shopName);
                map.put("num",1);
                //计算百分比
                map.put("progress",CountUtils.calculatePercentage(Integer.parseInt(total),1));
                map.put("createTime", DateUtilsUtils.getNowDate());
                map.put("updateTime", DateUtilsUtils.getNowDate());
                map.put("taskId",taskId);
                mapList.add(map);
            }
        }
        //写入txt文件中
        EasyExcelUtil.writeJsonToFile(taskId+".txt",mapList);
    }

    /**
     * 存储任务中店铺里的日志类型进度
     * @param taskId
     * @param shopId
     * @param shopName
     * @param logName
     * @param mark
     * @param total
     */
    public void readTaskShopLogsTxt(String taskId,String shopId,String shopName,String logName,String mark,String total){
        String filePath = UrlUtil.getUrl()+taskId+shopId+".txt";
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(isFileExists(filePath)){
            mapList = EasyExcelUtil.readFileContent(filePath);
        }
        if(mapList.size() == 0){
            Map<String,Object> map = new HashMap<>();
            map.put("shopId",shopId);
            map.put("shopName",shopName);
            map.put("num",1);
            //计算百分比
            map.put("progress",CountUtils.calculatePercentage(Integer.parseInt(total),1));
            map.put("taskId",taskId);
            map.put("logName",logName);
            map.put("mark",mark);
            mapList.add(map);
        }else{
            //true为txt文件中不存在店铺信息，false代表存在
            Boolean bool = true;
            //txt文本不为空，则遍历List对象，查看是否存在店铺id，存在，则数量+1,不存在则添加
            for (Map<String, Object> map : mapList) {
                if(map.get("logName").equals(logName)){
                    bool = false;
                    int num = Integer.parseInt(String.valueOf(map.get("num"))) + 1;
                    map.put("num",num);
                    map.put("progress",CountUtils.calculatePercentage(Integer.parseInt(total),num));
                }
            }
            if(bool){
                Map<String,Object> map = new HashMap<>();
                map.put("shopId",shopId);
                map.put("shopName",shopName);
                map.put("num",1);
                //计算百分比
                map.put("progress",CountUtils.calculatePercentage(Integer.parseInt(total),1));
                map.put("taskId",taskId);
                map.put("logName",logName);
                map.put("mark",mark);
                mapList.add(map);
            }
        }
        //写入txt文件中
        EasyExcelUtil.writeJsonToFile(taskId+shopId+".txt",mapList);
    }

    public static boolean isFileExists(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.exists(path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean switchBool(String systemId,Map bookBaseInfoVo,String str){
        Boolean bool = false;
        //0 isbn 1 书名 2 作者 3 出版社 4 出版时间 5 类目  敏感词 7 限价库
        switch (systemId){
            case "0" : bool = str.contains(bookBaseInfoVo.get("isbn").toString());break;
            case "1" : bool = str.contains(bookBaseInfoVo.get("bookName").toString());break;
            case "2" : bool = str.contains(bookBaseInfoVo.get("author").toString());break;
            case "3" : bool = str.contains(bookBaseInfoVo.get("publisher").toString());break;
            case "4" : bool = str.contains(bookBaseInfoVo.get("publicationTime").toString());break;
//          case "5" : bool = str.contains(bookBaseInfoVo.get("").toString());
            case "6" : bool = checkString(bookBaseInfoVo.get("bookName").toString(),  str);break;
        }
        return bool;
    }

    public static boolean checkString(String input, String[] patterns) {
        for (String patternStr : patterns) {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkString(String input, String patternsStr) {
        String[] patterns = patternsStr.split(",");
        for (String patternStr : patterns) {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }
}
