var TT = TAOTAO = {
	checkLogin : function(){
		var _ticket = $.cookie("TT_TOKEN");
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://localhost:8088/user/token/" + _ticket,
			dataType : "jsonp",			// 数据类型 jsonp 如果注释了,就不能实现跨域请求
			type : "GET",
			success : function(data){
				if(data.status == 200){
				//	alert("测试");
					var username = data.data.username;
					//var html = username + "，欢迎来到淘淘！<a href=\"http://www.taotao.com/user/logout.html\" class=\"link-logout\">[退出]</a>";
					var html = username + "，欢迎来到图书网购商城！<a href=\"http://localhost:8088/user/logout/"+_ticket+".html\" class=\"link-logout\">[退出]</a>";
					
				/*	var uid=data.data.id;
					alert(uid);
					alert($("#loginbar"));
					document.getElementById("uuid").setAttribute("value", uid);*/
					
					var uid = data.data.id;
					alert(uid);
					var href=document.getElementById("myOrder").getAttribute("href");
					href=href+uid;
					alert(href);
					document.getElementById("myOrder").setAttribute("href", href);
					$("#loginbar").html(html); //加载网页到横条中   情况分为 |登录后||没有登录|
				}
			}//,
		/*	error:function(data)[
		 * 	alert("出错啦");
		 * 
		 * },
		 * dataType:"text"			//数据类型
		*/
		});
	}
}
	//页面加载自动校验
$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});