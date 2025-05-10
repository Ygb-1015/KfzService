package com.order.main.dto.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TDistrictVo implements Serializable {

    /**
     * 区划信息id
     */
    private Long id;

    /**
     * 父级挂接id
     */
    private Long pid;

    /**
     * 区划编码
     */
    private String code;

    /**
     * 区划名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态 0 正常 -2 删除 -1 停用
     */
    private Long status;

    /**
     * 级次id 0:省/自治区/直辖市 1:市级 2:县级
     */
    private Long level;

}