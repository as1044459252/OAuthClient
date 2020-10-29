package com.circles.bookstore.controller.oauthController;

import com.alibaba.fastjson.JSONObject;
import com.circles.bookstore.bean.Customer;
import com.circles.bookstore.bean.User;
import com.circles.bookstore.bean.oauthClient.AuthToken;
import com.circles.bookstore.bean.oauthClient.Constant;
import com.circles.bookstore.service.LoginService;
import com.circles.bookstore.service.ShiroService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.SessionScope;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/*
这个controller属于RP
类中的许多常量都可以放在常量类中，有需要再改动

其中的.replace()都是用来方便本地手机开发的，项目真正发布之后就可以删掉
 */
@Controller
public class oauthLoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    ShiroService shiroService;

    String ip = "";
    String qqNumber="";
    String token="";
    //点击使用QQ登录之后，RP使用302让用户转向QQ登录页面
    @RequestMapping("/toOauthQQLogin")
    @ResponseBody
    public String toQQLogin(HttpServletResponse response){
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
            ip = ia.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String state = RandomStringUtils.randomAlphanumeric(16);

        try {
            OAuthClientRequest request = OAuthClientRequest
                    .authorizationLocation(Constant.authorizationLocation.replace("localhost",ip))
                    .setResponseType(OAuth.OAUTH_CODE)
                    .setClientId(Constant.clientId)
                    .setRedirectURI(Constant.codeRedirectURI.replace("localhost",ip))
                    .setState(state)
                    .buildQueryMessage();
            //把state放进cookie里
            Cookie cookie = new Cookie("state",state);
            response.addCookie(cookie);
            return request.getLocationUri();
        }
        catch (OAuthSystemException e){
            e.printStackTrace();
        }
        return null;
    }

    //RP收到code和state后，检查state并用code去交换token
    @RequestMapping("/authorize/qq_callback")
    public String getToken(HttpServletRequest request){
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthAuthzResponse response = null;
        try {
            response = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
            String code = response.getCode();
            OAuthClientRequest oAuthClientRequest = OAuthClientRequest.tokenLocation(Constant.accessTokenUrl.replace("localhost",ip))
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(Constant.clientId)
                    .setClientSecret(Constant.clientSecret)
                    .setCode(code)
                    .setRedirectURI(Constant.tokenRedirectURI.replace("localhost",ip))
                    .buildQueryMessage();
            //用request去请求token并返回
            OAuthAccessTokenResponse oAuthAccessTokenResponse = oAuthClient.accessToken(oAuthClientRequest,OAuth.HttpMethod.POST);
            String accessToken = oAuthAccessTokenResponse.getAccessToken();
            //本来应该检验token是否过期
            //不应该return redirect，因为accesstoken不能暴露给用户，因此通过accessToken获取userInfo的操作直接在这个请求里面完成
            OAuthClient client = new OAuthClient(new URLConnectionClient());
            try {
                OAuthClientRequest clientRequest = new OAuthBearerClientRequest(Constant.userInfoUrl.replace("localhost",ip))
                        .setAccessToken(accessToken).buildQueryMessage();
                OAuthResourceResponse resourceResponse = client.resource(clientRequest,OAuth.HttpMethod.GET,OAuthResourceResponse.class);

                //以下逻辑与OAuth没有关系了
                String currentQqNumber = resourceResponse.getBody();
                qqNumber = currentQqNumber;
                token = shiroService.createToken();
                shiroService.saveToken(qqNumber,token);
                //shiro相关操作，即本应该放在/login请求里面的shiro相关的操作
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken upToken = new AuthToken(token);
                subject.login(upToken);
                return  "qqLoginSuc";
            } catch (OAuthSystemException | OAuthProblemException e) {
                e.printStackTrace();
            }
            return "error";
        }
        catch (OAuthProblemException | OAuthSystemException e){
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping("/register")
    @ResponseBody
    public String register(@RequestBody JSONObject jsonObject){
       // String username = jsonObject.getString("username");
        System.out.println(jsonObject);
        return "registing...";
    }

    @RequestMapping("/checkLogin")
    @ResponseBody
    public Object checkLogin(){
        Map<String,String> result = new HashMap<>();
        if(qqNumber!=""){
            result.put("qqNumber",qqNumber);
            result.put("token",token);
            return result;
        }
        else {
            return "error";
        }
    }

    @RequestMapping("/checkRole")
    @ResponseBody
    public Object checkRole(String token){
        Map<String,String> result = new HashMap<>();
        String currentQQ = shiroService.getQQByToken(token);
        return result;
    }


}

