package com.order.main.dto.requst;

public class GetShopGoodsListRequest {

    private String token;

    // 商品列表类型。默认值为：sale，可选值为：sale（在售的商品），delisting（下架的商品），uncertif
    private String type;

    // 页码。默认为：1
    private Integer pageNum;

    // 每页最大条数。默认为：50，最大值为：100
    private Integer pageSize;

    // 商品编号。可以通过商品编号精确匹配查询商品。
    private Long itemId;

    // 货号。尾部模糊查询，例如：itemSn=测试货号，是查询货号以“测试货号”开头的所有商品
    private String itemSn;

    // ISBN号。可以更新ISBN号查询相同ISBN的商品。
    private String isbn;

    // 上传时间，用于查询此时间之后上传的商品。格式: 2020-04-29 12:00:00
    private String addTimeBegin;

    // 上传时间，用于查询此时间之前上传的商品。格式: 2020-04-29 12:00:00
    private String addTimeEnd;

    // 排序字段，可选值为:itemId（根据商品编号排序）,addTime（根据上书时间排序）
    private String sortOrder;

    // 排序方式，可选值为:ASC（正序）,DESC（倒序）
    private String sortType;

    // 偏移查询商品编号，结合 itemId 排序，可优化查询，遍历全部商品
    private Long offsetId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemSn() {
        return itemSn;
    }

    public void setItemSn(String itemSn) {
        this.itemSn = itemSn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAddTimeBegin() {
        return addTimeBegin;
    }

    public void setAddTimeBegin(String addTimeBegin) {
        this.addTimeBegin = addTimeBegin;
    }

    public String getAddTimeEnd() {
        return addTimeEnd;
    }

    public void setAddTimeEnd(String addTimeEnd) {
        this.addTimeEnd = addTimeEnd;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public Long getOffsetId() {
        return offsetId;
    }

    public void setOffsetId(Long offsetId) {
        this.offsetId = offsetId;
    }

}
