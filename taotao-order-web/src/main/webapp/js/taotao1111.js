var TT = TAOTAO = {
	checkLogin : function(){
		alert("新js");
		var _ticket = $.cookie("TT_TOKEN");//这里出问题
		alert("新js2");
		alert(_ticket);
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://localhost:8088/user/token/" + _ticket,
			dataType : "jsonp",			// 数据类型 jsonp 如果注释了,就不能实现跨域请求
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var username = data.data.username;
					//自己加的
					//var html = username + "，欢迎来到淘淘！<a href=\"http://www.taotao.com/user/logout.html\" class=\"link-logout\">[退出]</a>";
					var html = username + "，欢迎来到图书网购商城！<a href=\"http://localhost:8088/user/logout/"+_ticket+".html\" class=\"link-logout\">[退出]</a>";
					//$("#loginbar").html(html); 
					var uid = data.data.id;
					alert(uid);
					/*var href = $('#myOrder').attr('href');
					alert(href);
					var temp=href+uid;
					var href2=$("#myOrder").attr('href',temp);
					alert(href2);*/
					
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