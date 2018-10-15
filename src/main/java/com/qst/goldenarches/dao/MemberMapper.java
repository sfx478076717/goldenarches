/**
 * FileName: MemberMapper
 * Author:   SAMSUNG-PC 孙中军
 * Date:     2018/10/4 13:37
 * Description: 后台：会员数据库访问层
 */
package com.qst.goldenarches.dao;

import com.qst.goldenarches.pojo.VIP;

import java.util.List;
import java.util.Map;

public interface MemberMapper {

    List<VIP> selectAll(Map<String, String> map);
}
