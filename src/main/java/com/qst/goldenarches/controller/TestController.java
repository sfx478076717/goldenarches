/** * FileName: TestController * Author:   SAMSUNG-PC 孙中军 * Date:     2018/9/26 11:13 * Description: */package com.qst.goldenarches.controller;import com.qst.goldenarches.dao.*;import com.qst.goldenarches.pojo.*;import com.qst.goldenarches.service.PermissionService;import com.qst.goldenarches.utils.DigitalUtil;import com.qst.goldenarches.utils.TimeUtil;import com.sun.org.apache.bcel.internal.generic.NEW;import javafx.scene.chart.PieChart;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.beans.factory.annotation.Value;import org.springframework.expression.spel.ast.Projection;import org.springframework.stereotype.Controller;import org.springframework.ui.ModelMap;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestMethod;import org.springframework.web.bind.annotation.ResponseBody;import org.springframework.web.multipart.MultipartFile;import org.springframework.web.multipart.MultipartHttpServletRequest;import javax.servlet.http.HttpServletRequest;import javax.servlet.http.HttpServletResponse;import javax.servlet.http.HttpSession;import java.io.File;import java.io.FileOutputStream;import java.io.IOException;import java.io.InputStream;import java.security.Permissions;import java.text.SimpleDateFormat;import java.util.*;@Controllerpublic class TestController {    /**     * 跳转到后台主页面     * @return     */    @RequestMapping("/main")    public String main(){        return "main";    }    /**     * 跳转到后台主页面     * @return     */    @RequestMapping("/login")    public String login(){        return "login";    }    @Autowired    private MemberMapper memberMapper;    @Autowired    private CategoryMapper categoryMapper;    @Autowired    private DetailMapper detailMapper;    @Autowired    private ProductMapper productMapper;    @Autowired    private OrderMapper orderMapper;    @Autowired    private PermissionService permissionService;    @RequestMapping("test4")    @ResponseBody    public Object test4(){        Admin admin=new Admin();        admin.setId(1);        return permissionService.getPermissionsByAdmin(admin);    }    @RequestMapping("test3")    @ResponseBody    public Msg test3(){        //获得当前时间和月份        Calendar calendar = Calendar.getInstance();        int month = calendar.get(Calendar.MONTH) + 1;        int year =calendar.get(Calendar.YEAR);        Integer maxCount =0;//保存所有月份中最高销售数量        List<Map> series =new ArrayList<Map>();        List<String> itemList =new ArrayList<String>();        //获取所有的类别        List<Category> categories =categoryMapper.selectAll(null);        //遍历每个类别        for (Category c:categories) {            //用于保存该类别下的每个月份的销售情况            List<Integer> dataList =new ArrayList<Integer>();            for(int i=1;i<=month;i++){                String firstTime = TimeUtil.getFirstTimeOfMonth(year,i);                String lastTime =TimeUtil.getLastTimeOfMonth(year,i);                Map<String,Object> queryCriteria = new HashMap<String, Object>();                queryCriteria.put("cid",c.getId());                queryCriteria.put("fTime",firstTime);                queryCriteria.put("lTime",lastTime);                Integer count =detailMapper.selectCountOfProTypeSale(queryCriteria);                if (count!=null){                    if (count>maxCount){                        maxCount=count;                    }                    dataList.add(count);                }else {                    dataList.add(0);                }            }            //将该类的数据封装成对象            Map<String,Object> data =new HashMap<String, Object>();            data.put("name",c.getName());            data.put("type","bar");//柱状图            data.put("data",dataList);            series.add(data);            itemList.add(c.getName());        }        //获取每个月的销售金额        double saleMax =0;//保存最高销售金额        List<Double> dataList =new ArrayList<Double>();        //查询每个月的销销售金额        for(int i=1;i<=month;i++){            String firstTime = TimeUtil.getFirstTimeOfMonth(year,i);            String lastTime =TimeUtil.getLastTimeOfMonth(year,i);            Map<String,Object> queryCriteria = new HashMap<String, Object>();            queryCriteria.put("fTime",firstTime);            queryCriteria.put("lTime",lastTime);            double totalSaleOfMonth = orderMapper.selectAmountOfMonth(queryCriteria);            if (totalSaleOfMonth>saleMax){                saleMax=totalSaleOfMonth;            }            dataList.add(totalSaleOfMonth);        }        Map<String,Object> data =new HashMap<String, Object>();        data.put("name","销售金额");        data.put("type","line");//折线图        data.put("yAxisIndex",1);        data.put("data",dataList);        series.add(data);        itemList.add("销售金额");        //分别对Max最低十为单位取整，用于构建Y轴最大值        Map<String,Integer> YAxisMax=new HashMap<String, Integer>();        YAxisMax.put("maxCount", DigitalUtil.convertLeastTenAsUnit(maxCount));        YAxisMax.put("saleMax",DigitalUtil.convertLeastTenAsUnit(saleMax));        return Msg.success().add("seriesData",series).add("items",itemList).add("YAxis",YAxisMax);    }    @RequestMapping("test1")    @ResponseBody    public Msg test1(){        //1获得所有的商品类别        List<Category> categories =categoryMapper.selectAll(null);        List<Map> datas =new ArrayList<Map>();        for (Category c:categories){            //根据cid 查询该类商品的销售数量 从订单详情表里面查询            Integer count =detailMapper.selectSaleNumByCid(c.getId());            System.out.println("====>"+c +" : "+count);            if (count!=null){                Map<String,Object> data = new HashMap<String, Object>();                data.put("name",c.getName());                data.put("value",count);                datas.add(data);            }        }        return Msg.success().add("chartDatas",datas);    }    /***     * 测试使用     * @return     */    @RequestMapping("test2")    @ResponseBody    public Msg test2(){        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        Date date=new Date();        Calendar calendar = Calendar.getInstance();        calendar.setTime(date);        calendar.add(Calendar.HOUR_OF_DAY, -12);        date = calendar.getTime();        System.out.println(sdf.format(date));        System.out.println(Calendar.HOUR+"==="+Calendar.HOUR_OF_DAY);        return Msg.success().add("vips",sdf.format(new Date()));    }    /**     * 图片上传 已经实现     * @return     * @throws IOException     */    @RequestMapping(value = "uploadIMG", method = RequestMethod.POST)    @ResponseBody    public String uploadSong(HttpServletRequest request,Category category,                      HttpServletResponse response, ModelMap model, HttpSession session) throws IOException {        System.out.println("into uploadIMG-------------------------------->");        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;        String rootPath =request.getSession().getServletContext().getRealPath(File.separator);        String relatPath = File.separator+"img"+ File.separator;        System.out.println("path----->"+rootPath+relatPath);        MultipartFile mFile = multipartRequest.getFile("codeUrl");        request.getParameter("id");        request.getParameter("name");        System.out.println("request.getParameter===>"+ request.getParameter("id")+ request.getParameter("name"));        System.out.println("category======>"+category.toString());        //String path="D:\\TMP\\";//替换成你所要保存的文件的位置        String path =rootPath+relatPath;        String filename = mFile.getOriginalFilename();        System.out.println("filename:"+filename);        InputStream inputStream = mFile.getInputStream();        byte[] b = new byte[1048576];        int length = inputStream.read(b);        String url =path + filename;        System.out.println(url);        FileOutputStream outputStream = new FileOutputStream(url);        outputStream.write(b, 0, length);        inputStream.close();        outputStream.close();        return filename;    }}