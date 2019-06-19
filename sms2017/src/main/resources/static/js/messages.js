// -- ошибка --
function msg_error(error_msg){
	$("body").append("<div id='msg_error_dlg'></div>");
	$("#msg_error_dlg").kendoDialog({
		width: "400px",
		title: "ошибка",
		closable: true,
		modal: true,
		content: error_msg,
		actions: [{text: "понятно"}]			
	});
	$(".k-window-titlebar").css("background","rgb(200,100,100)").css("color","white");
	$(".k-dialog-title").css("color","white");
}

// -- сообщение --
function msg_success(success_msg){
	$("body").append("<div id='msg_success_dlg'></div>");
	$("#msg_success_dlg").kendoDialog({
		width: "400px",
		title: "Сообщение",
		closable: true,
		modal: true,
		content: success_msg,
		actions: [{text: "понятно"}]			
	});
	$(".k-window-titlebar").css("color","white");
	$(".k-dialog-title").css("color","white");
}