/**
 * FileName: MemberController
 * Author:   SAMSUNG-PC 孙中军
 * Date:     2018/10/4 13:32
 * Description: 后台：会员管理控制器
 */
package com.qst.goldenarches.controller;

import com.github.pagehelper.PageHelper;
import com.qst.goldenarches.pojo.Msg;
import com.qst.goldenarches.pojo.Product;
import com.qst.goldenarches.pojo.VIP;
import com.qst.goldenarches.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 会员后台：分页查找
     * 查询全部会员并分页显示
     * @param pn 页码
     * @return json数据 Msg
     */
    @ResponseBody
    @RequestMapping("/getAll")
    public Msg getAll(@RequestParam(value = "pageno",defaultValue = "1") Integer pn, String orderText){
        Map<String,String> map =new HashMap<String, String>();
        map.put("orderText",orderText);
        PageHelper.startPage(pn,10);
        List<VIP> vips =memberService.getAll(map);
        com.github.pagehelper.PageInfo<VIP> vipPageInfo =new com.github.pagehelper.PageInfo<VIP>(vips,5);
        return Msg.success().add("pageInfo",vipPageInfo);
    }
    /***
     * 会员后台：跳转方法
     * 跳转至会员详情主页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "member/index";
    }

}
