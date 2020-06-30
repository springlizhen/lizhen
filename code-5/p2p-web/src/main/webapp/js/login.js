var referrer = "";//登录后返回页面
referrer = document.referrer;
if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
			referrer = window.opener.location.href;
		}  
	} catch (e) {
	}
}

//按键盘Enter键即可登录
$(document).keyup(function(event){
	if(event.keyCode == 13){
		login();
	}
});




$(function(){
	loadStart();

	$("#dateBtn1").on("click",function(){
		var phone = $.trim($("#phone").val());

		var _this=$(this);
		if ("" != phone) {
			$.ajax({
				url: "loan/messageCode",
				type: "get",
				data: "phone=" + phone,
				success: function (jsonObject) {
					if (jsonObject.errormessage == "ok") {
						alert("您手机收到的短信验证码是:" + jsonObject.messageCode);
						if (!$(this).hasClass("on")) {
							$.leftTime(60, function (d) {
								if (d.status) {
									_this.addClass("on");
									_this.html((d.s == "00" ? "60" : d.s) + "秒后重新获取");
								} else {
									_this.removeClass("on");
									_this.html("获取验证码");
								}
							});
						}
					} else {
						showError("message", "请稍后重试...");
					}

				}
			});

		} else {
			$("#showId").html("请输入手机号码");
		}
	});



});
	function loadStart(){
		$.ajax({
			url:"loan/loadStart",

			type:"get",
			success: function(data){
				$(".historyAveageRate").html(data.historyAveageRate);
				$("#allUserCount").html(data.allUserCount);
				$("#allBidMoney").html(data.allBidMoney);
		}
		})
	}
	function checkPhone(){
		var phone = $.trim($("#phone").val());
		if(phone==""){
			$("#showId").html("手机号不能为空");
			return false;

		}else if(!/^1[1-9]\d{9}$/.test(phone)){
			$("#showId").html("手机号输入不正确");
			return false;
		}else{
			$("#showId").html("");
		}
		return true;
	}
	function checkLoginPassword(){
		var loginPassword = $.trim($("#loginPassword").val());
		if(loginPassword==""){
			$("#showId").html("密码不能为空");
			return false;
		}else{
			$("#showId").html("");
		}
		return true;
	}
function checkCaptcha(){
	var captcha = $.trim($("#captcha").val());
	var flag = true;
	if(captcha == ""){
		$("#showId").html("图形验证码不能是空")
		return false;
	}else{
		$.ajax({
			url:"loan/checkCaptcha",
			data:"captcha="+captcha,
			type:"post",
			async:false,
			success:function(data){
				if(data.errormessage == "ok"){
					$("#showId").html("");
					flag = true;
				}else{
					$("#showId").html(data.errormessage);
					flag = false;
				}

			},
			error:function(){
				$("#showId").html("系统繁忙，请稍后再试");
				flag=false;
			}

		});

	}
	return flag;

}
function checkMessageCode(){
		var messageCode =$.trim($("#messageCode").val());
		if(messageCode=""){
			$("#showId").html("短信验证码不能为空");
			return false;
		}else{
			$("#showId").html("");
		}
		return true;

}

function login(){
	var phone = $.trim($("#phone").val());
	var loginPassword = $.trim($("#loginPassword").val());
	var messageCode = $.trim($("#messageCode").val());
	if(checkPhone()&&checkLoginPassword()&&checkMessageCode()){
		  $("#loginPassword").val($.md5(loginPassword));
		$.ajax({
			url:"loan/login",
			data:"phone="+phone+"&loginPassword="+$.md5(loginPassword)+"&messageCode="+messageCode,
			type:"post",
			success:function(data){
				if(data.errormessage=="ok"){
					window.location.href= referrer;
				}else{
					$("#showId").html(data.errormessage);
				}
			},
			error:function(){
				$("#showId").html("系统繁忙");
			}


		})
	}
}
