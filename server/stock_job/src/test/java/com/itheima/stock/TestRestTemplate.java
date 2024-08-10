package com.itheima.stock;

import com.itheima.stock.pojo.Account;
import com.itheima.stock.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestRestTemplate {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    /**
     * 测试get请求携带url参数，访问外部接口
     */
    @Test
    public void test01(){
        String url="http://localhost:6766/account/getByUserNameAndAddress?userName=itheima&address=shanghai";
        /*
          参数1：url请求地址
          参数2：请求返回的数据类型
         */
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        //获取响应头
        HttpHeaders headers = result.getHeaders();
        System.out.println(headers);
        //响应状态码
        int statusCode = result.getStatusCodeValue();
        System.out.println(statusCode);
        //响应数据
        String respData = result.getBody();
        System.out.println(respData);
    }
    @Test
    public void test02(){
        String url="http://localhost:6766/account/getByUserNameAndAddress?userName=itheima&address=shanghai";
        Account account = restTemplate.getForObject(url, Account.class);
        System.out.println(account);

    }
    @Test
    public void test03(){
        String url="http://localhost:6766/account/getHeader";
        //设置请求头参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("userName","zhangsan");
        //请求头填充到请求对象下
        HttpEntity<Map> entry = new HttpEntity<>(headers);
        //发送请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entry, String.class);
        String result = responseEntity.getBody();
        System.out.println(result);
    }
    @Test
    public void test04(){
        String url="http://localhost:6766/account/addAccount";
        //设置请求头，指定请求数据方式
//        HttpHeaders headers = new HttpHeaders();
//        //告知被调用方，请求方式是form表单提交，这样对方解析数据时，就会按照form表单的方式解析处理
//        headers.add("Content-type","application/x-www-form-urlencoded");
//        //组装模拟form表单提交数据，内部元素相当于form表单的input框
//        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
//        map.add("id","10");
//        map.add("userName","itheima");
//        map.add("address","shanghai");
//        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
//        /*
//            参数1：请求url地址
//            参数2：请求方式 POST
//            参数3：请求体对象，携带了请求头和请求体相关的参数
//            参数4：响应数据类型
//         */
//        ResponseEntity<Account> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Account.class);
//        Account body = exchange.getBody();
//        System.out.println(body);
        HttpHeaders headers = new HttpHeaders();
        //告知被调用方，发送的数据格式的json格式，对方要以json的方式解析处理
        headers.add("Content-type","application/json; charset=utf-8");
        String jsonReq="{\"address\":\"上海\",\"id\":\"1\",\"userName\":\"zhangsan\"}";
        //构建请求对象
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonReq, headers);
        ResponseEntity<Account> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Account.class);
        Account body = responseEntity.getBody();
        System.out.println(body);
    }
    @Test
    public void test06(){
        String url="http://localhost:6766/account/getCookie";
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        //获取cookie
        List<String> cookies = result.getHeaders().get("Set-Cookie");
        //获取响应数据
        String resStr = result.getBody();
        System.out.println(resStr);
        System.out.println(cookies);
    }
    @Test
    public void test07(){
        stockTimerTaskService.getInnerMarketInfo();
    }

}

