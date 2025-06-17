package com.order.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDelistingResponse {

    private Item item;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Item{

        private Long itemId;

        private String updateTime;

        private String endSaleTime;

        private String isOnSale;
    }

}
