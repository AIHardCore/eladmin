package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.domain.WxConfig;
import me.zhengjie.modules.system.service.OrderService;
import me.zhengjie.service.WxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/app/wx")
@Api(tags = "工具：微信管理")
public class WxUtilController {

    private final OrderService orderService;
    private final WxService wxService;

    @GetMapping("/url/code")
    @Log("查询微信授权Url")
    @ApiOperation("查询微信授权Url")
    @AnonymousGetMapping(value = "/code")
    public ResponseEntity<String> code(){
        WxConfig wxConfig = wxService.find();
        return new ResponseEntity<>(wxConfig.getCodeUrl().replace("{APPID}",wxConfig.getAppId()), HttpStatus.OK);
    }

    @RequestMapping("/notify")
    @Log("处理支付回调")
    @ApiOperation("处理支付回调")
    @AnonymousGetMapping(value = "/notify")
    public ResponseEntity.BodyBuilder notify(HttpServletRequest request){
        return orderService.notify(request);
    }
}
