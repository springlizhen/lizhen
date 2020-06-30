
//同意实名认证协议
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
});
//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}
function checkRealName(){
	var realName = $.trim($("#realName").val());
	if(realName ==""){
		showError("realName","请输入姓名");
		return false;
	}else if(!/^[\u4e00-\u9fa5]{0,}$/.test(realName)){
		showError("realName","请输入正确的姓名");
		return false;
	}else{
		showSuccess("realName");
	}
	return true;
}
function checkIdCard(){
	var  idCard = $.trim($("#idCard").val());
	var replayIdCard =$.trim($("#replayIdCard").val());
	if(idCard ==""){
		showError("idCard","请输入你的身份证号");
		return false;
	}else if(!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard)){
		showError("idCard","请输入正确的身份证号");
		return false;
	}else {
		showSuccess("idCard");

	}
	if(idCard!=replayIdCard){
		showError("replayIdCard","两次身份证号不一样");
	}
	return true;
}
function checkIdCardEqu(){
	var  idCard = $.trim($("#idCard").val());
	var replayIdCard =$.trim($("#replayIdCard").val());
	if(idCard == ""){
		showError("idCard","请输入身份证号");
		return false;
	}else if(replayIdCard == ""){
		showError("replayIdCard","请输入确认身份证号");
		return false;
	}else if(idCard != replayIdCard){
		showError("replayIdCard","两次输入的身份证号不正确");
		return false;
	}else{
		showSuccess("replayIdCard");
	}
	return true;

}
function checkCaptcha(){
	var captcha = $.trim($("#captcha").val());
	var flag = true;
	if(captcha == ""){
		showError("captcha","请输入图形验证码");
		return false;
	}else{
		$.ajax({
			url:"loan/checkCaptcha",
			data:"captcha="+captcha,
			type:"post",
			async:false,
			success:function(data){
				if(data.errormessage == "ok"){
					showSuccess("captcha");
					flag = true;
				}else{
					showError("captcha",data.errormessage);
					flag = false;
				}

			},
			error:function(){
				showError("captcha","系统繁忙，请稍后再试");
				flag=false;
			}

		});

	}
	return flag;

}
function verifyRealName(){
	var  idCard = $.trim($("#idCard").val());
	var realName = $.trim($("#realName").val());
	if(checkRealName()&&checkIdCard()&&checkCaptcha()&&checkIdCardEqu()){
		$.ajax({
			url:"loan/verifyRealName",
			data:{
				"idCard":idCard,
				"realName":realName
			},
			type:"post",
			success:function (data){
				if(data.errormessage=="ok"){
					window.location.href="index";
				}else{
					showError("captcha","认证失败，请稍后1重试");
				}

			},
			error:function(){
				showError("captcha","认证失败，请稍后重试");
			}
		})
	}

}
