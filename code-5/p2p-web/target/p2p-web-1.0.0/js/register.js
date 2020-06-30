


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

//注册协议确认
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
//验证手机号
function checkPhone(){
	var phone =$.trim($("#phone").val());
	var flag = true;
	if(phone == ""){
		showError("phone" , "请输入手机号");
		return false;
	}else if(!/^1[1-9]\d{9}$/.test(phone)){
		showError("phone","请输入正确的手机号");
		return false;
	}else {
		//手机号码不能重复
		$.ajax({
			url:"loan/checkPhone",
			data:"phone="+phone,
			type:"get",
			success:function(data){
				if(data.errorMessage="OK"){
					showSuccess("phone",data.errorMessage);
					flag=true;
				}else{
					showError("phone","系统繁忙请稍后重试");
					flag = false;
				}

			}
		});
		return flag;
	}
}
function checkLoginPassword(){
	var loginPassword = $.trim($("#loginPassword").val());
	var  replayLoginPassword = $.trim($("#replayLoginPassword").val());
	if(loginPassword == ""){
		showError("loginPassword","请输入密码");
		return false;

	}else if(!/[0-9a-zA-Z]+$/.test(loginPassword)){
		showError("loginPassword","密码只能是数字和字母");
		return false;


	}else if(!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)){
		showError("loginPassword","密码应该同时包含数字和字母");
		return false;
	}else if(loginPassword.length<6 || loginPassword.length>20){
		showError("loginPassword","密码应该在6-20位之间");
		return false;
	}else {
		showSuccess("loginPassword");

	}
	if(replayLoginPassword !=loginPassword){
		showError("replayLoginPassword","两次密码应该一致");

	}
	return true;

}
	function checkLoginPasswordEq(){
	var loginPassword = $.trim($("#loginPassword").val());
	var replayLoginPassword = $.trim($("#replayLoginPassword").val());
	if(loginPassword ==""){
		showError("loginPassword","登录密码不能为空");
		return false;
	}else if(replayLoginPassword == ""){
		showError("replayLoginPassword","确认密码不能为空");
		return false;
	}else if(loginPassword !=replayLoginPassword){
		showError("replayLoginPassword","两次密码需要一致");
		return false;
	}else{
		showSuccess("replayLoginPassword");

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
function register() {

	//获取表单元素
	var phone = $.trim($("#phone").val());
	var loginPassword = $.trim($("#loginPassword").val());
	var replayLoginPassword = $.trim($("#loginPassword").val());


	if (checkPhone() && checkLoginPassword() && checkLoginPasswordEq() && checkCaptcha()) {

		$("#loginPassword").val($.md5(loginPassword));
		$("#replayLoginPassword").val($.md5(replayLoginPassword));


		$.ajax({
			url:"loan/register",
			type:"get",
			data:"phone="+phone+"&loginPassword="+$.md5(loginPassword),
			success:function (jsonObject) {
				if (jsonObject.errormessage == "ok") {
					//注册成功，跳转至实名认证页面
					window.location.href = "realName.jsp";
				} else {
					showError("captcha",jsonObject.errormessage);
				}

			},
			error:function () {
				showError("captcha","系统繁忙，请稍后重试...");
			}
		});


	}

}


