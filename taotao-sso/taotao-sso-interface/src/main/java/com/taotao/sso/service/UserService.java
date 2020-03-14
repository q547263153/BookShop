package com.taotao.sso.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {

 TaotaoResult checkData(String data ,Integer type);//校验用户注册信息(是否数据库中已经存在)
 
 TaotaoResult register(TbUser tbUser);//用户注册
	
 TaotaoResult login(String username ,String password);//用户登录
 
 TaotaoResult getUserByToken(String token); //用于校验用户是否在登陆状态
 
 TaotaoResult logout(String token);//用于安全退出

EasyUIDataGridResult getUserOrder(Integer page, Integer rows);

TaotaoResult delOrderyId(long ids);

EasyUIDataGridResult getUserOrder(Integer page, Integer rows, String userid);


}
