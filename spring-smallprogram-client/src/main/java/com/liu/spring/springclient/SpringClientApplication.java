package com.liu.spring.springclient;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.github.tobato.fastdfs.FdfsClientConfig;
import com.liu.util.fastdfs.FastDFSClientWrapper;
import com.liu.util.rediscluster.RedisConfig;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.List;

//import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableSwagger2
@Controller
@EnableWebSecurity
@EnableEurekaClient
@ServletComponentScan
@SpringBootApplication
//@EnableFeignClients(basePackages = { "com.liu.spring.service"}) //启用Feign服务消费默认配置
@EnableRedisHttpSession //session共享
@EnableDistributedTransaction //分布式事务lcn，表明这是一个txmanager的client
@Import(FdfsClientConfig.class) //将Fdfs配置引入项目
@ComponentScan(basePackages = "com.liu")
@MapperScan(basePackages="com.liu.spring.service.*.mapper")
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class SpringClientApplication {

    @Autowired
    private FastDFSClientWrapper dfsClient;



    @Autowired
    private RedisConfig redisTemplate;




    public static void main(String[] args) throws FileNotFoundException {
        SpringApplication.run(SpringClientApplication.class, args);



//        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("MyProject-1234.json"))
//                .createScoped(Collections.singleton(SQLAdminScopes.SQLSERVICE_ADMIN));

        //www.googleapis.com/oauth2/v4/token


    }



    @ResponseBody
    @RequestMapping("getTest")
    public Object getTest(HttpServletRequest request) {
        String result = redisTemplate.getJedisCluster().set("wode","天天向上");

//        System.out.println(request.getServerPort());
        return result;
    }




    /**
     * 这里一定要加事务，不然添加是成功了，
     * 但是读取又跑到别的服务器去读，所以没在一个事务里，就读不到刚保存的数据，因为事务没有提交，别的服务器没有同步过去
     * @param request
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping("getHello")
    public Object getHello(HttpServletRequest request) {

        Object aaa = request.getSession().getAttribute("aaa");
        if(aaa == null){
            System.out.println(request.getServerPort() + "取了，空的");
            request.getSession().setAttribute("aaa","放了");
            System.out.println(request.getServerPort() + "放了");
        }else{
            System.out.println(request.getServerPort() + "取了，不是空的");
        }

//        System.out.println(request.getServerPort());
        return "upload";
    }


    @Transactional
    @LcnTransaction//表明使用lcn模式
    @RequestMapping("testLcn")
    @ResponseBody
    public String testLcn(HttpServletRequest request){
        System.out.println("插入完毕...事务提交！");

//        logger.info("插入完毕...事务提交！");
        return "succ";
    }



    @LcnTransaction
    @Transactional
    @RequestMapping("getHe2")
    @ResponseBody
    public String getHe2(HttpServletRequest request){
//        LoveSoundLogLogin loveSoundLogLogin = new LoveSoundLogLogin();

//        loveSoundLogLogin.setUserId(1);
//        loveSoundLogLogin.setOpenId("222");
//        loveSoundLogLogin.setCreateTime(DateUtils.getNowDateTimestamp());

//        logLoginService.saveLogLogin(loveSoundLogLogin);

//        Map a = logLoginService.upLogLogin();
//        System.out.println(a.toString());
//
//        logLoginService.updateLogLogin();

//        logLoginService.updateLogSearch1();
        try {
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println(2222222 + ":::::" + request.getServerPort());

//        if(1 == 1){
//            throw new RuntimeException("");
//        }

        return "success";
    }









    @ResponseBody
    @ApiOperation(value = "打印用户信息标题", notes = "打印user信息内容")
    @ApiImplicitParam(name = "user", value = "这应该是参数", required = true, dataType = "String")
    @RequestMapping(value = "getHello1", method = RequestMethod.GET)
    public String getHello1(HttpServletRequest request, String param) {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa::::::::::::::" + param);
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        // 登录名
        System.out.println("Username:" + securityContextImpl.getAuthentication().getName());
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) securityContextImpl.getAuthentication().getDetails();
        // 获得访问地址
        System.out.println("RemoteAddress:::   " + details.getRemoteAddress());
        // 获得sessionid
        System.out.println("SessionId:::   " + details.getSessionId());
        // 获得sessionid
        System.out.println("TokenValue:::   " + details.getTokenValue());
        // 获得当前用户所拥有的权限
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) securityContextImpl.getAuthentication().getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            System.out.println("Authority:::   " + grantedAuthority.getAuthority());
        }


        System.out.println(request.getServerPort());
        return request.getServerPort() + "getHello1";
    }


    @ResponseBody
    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping("getHello2")
    public String getHello2(HttpServletRequest request) {
        System.out.println(request.getServerPort());
        return request.getServerPort() + "getHello2";
    }

    @ResponseBody
    @RequestMapping("getHello3")
    public String getHello3(HttpServletRequest request) {
        System.out.println(request.getServerPort());
        return request.getServerPort() + "getHello3";
    }

    @ResponseBody
    @RequestMapping(value = "zzzzzz", method = RequestMethod.POST)
    public String upload(HttpServletRequest request, @RequestParam("photo") MultipartFile photo, @RequestHeader(value = "Content-Type") String type) {
        System.out.println("请求类型：" + type);
        String imgUrl = null;
        try {
//            File dir = new File("D:\\img");
//            if (!dir.exists()) {
//                dir.mkdirs();  //判断并创建文件夹
//            }
//            byte[] bytes = photo.getBytes();   //文件字节流
//            File fileToSave = new File(dir.getAbsolutePath() + File.separator + photo.getOriginalFilename());
//            FileCopyUtils.copy(bytes, fileToSave);   //保存文件

            imgUrl = dfsClient.uploadFile(photo);
            System.out.println(imgUrl);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        return request.getServerPort() + "upload";
        return imgUrl;
    }


    /**
     * http://lang:8005/delImage?imgPath=group1/M00/00/00/wKjohFu90LKACfTCAABoVtIJ1vw233.jpg
     * 清除缓存
     * http://192.168.232.130/purge/group1/M00/00/00/wKjohFu-wBmAbGoQAABoVtIJ1vw696.jpg
     * http://192.168.232.131/purge/group1/M00/00/00/wKjohFu-wBmAbGoQAABoVtIJ1vw696.jpg
     * @param request
     * @param imgPath
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "delImage", method = RequestMethod.GET)
    public String delImage(HttpServletRequest request,String imgPath) {
        dfsClient.deleteFile(imgPath);
        return "success";
    }


    /**
     * 使用controller来下载文件
     * @param request
     * @param response
     * @param imgPath
     */
    @RequestMapping(value = "downloadImage", method = RequestMethod.GET)
    public void downloadImage(HttpServletRequest request, HttpServletResponse response, String imgPath) {
        dfsClient.downloadFile1(response,imgPath);
    }




}
