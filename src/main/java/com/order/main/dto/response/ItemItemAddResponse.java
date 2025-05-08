package com.order.main.dto.response;

public class ItemItemAddResponse {

    // 详细信息
    private Item item;

    public static class Item {

        // 商品ID
        private Long itemId;


        // 更新时间
        private String addTime;

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
