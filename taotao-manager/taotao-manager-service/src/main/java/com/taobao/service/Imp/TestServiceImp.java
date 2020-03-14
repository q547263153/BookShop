package com.taobao.service.Imp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taotao.mapper.MyTest;
import com.taotao.service.TestService;
@Service
public class TestServiceImp implements TestService{

	@Autowired
	private MyTest mytest;
	
	@Override
	public String NowDate() {

		return mytest.NowDate();
	}
}
