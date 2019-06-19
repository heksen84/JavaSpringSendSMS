<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset=UTF-8">
	<title>SMS manager</title>		
	<!-- styles -->
	<link href = "css/lib/kendo2017/kendo.common.min.css" 			rel = "stylesheet"/>
	<link href = "css/lib/kendo2017/kendo.metro.min.css" 			rel = "stylesheet"/>
	<link href = "css/lib/kendo2017/kendo.default.mobile.min.css"  	rel = "stylesheet"/>
	<link href = "css/welcome.css"  								rel = "stylesheet"/>		
	<!-- scripts -->
	<script src = "js/lib/kendo2017/jquery.min.js"></script>	
	<script src = "js/lib/kendo2017/kendo.all.min.js"></script>		
	<script src = "js/lib/kendo2017/messages/kendo.messages.ru-RU.min.js"></script>
	<script src = "js/lib/kendo2017/cultures/kendo.culture.ru-RU.min.js"></script>
	<script src = "js/lib/kendo2017/kendo.fx.min.js"></script>		
	<script src = "js/messages.js"></script>
	<script src = "js/welcome.js"></script>
</head>
<body>
	<ul id="menu">
		<li>
			Сообщение
			<ul>
				<li id="menu_item_message_create">Создать сообщение</li>
				<li id="menu_item_message_info">Информация о сообщении</li>
				<li id="menu_item_message_delete">Удалить сообщение</li>				
			</ul>
		</li>
		<li>
			Сообщения
			<ul>
				<li id="menu_item_messages_create">Создать пачку сообщений</li>				
				<li id="menu_item_batch_info">Информация о батче</li>				
			</ul>
		</li>
		<li id="menu_item_senders">
			Отправители			
		</li>
		<li id="menu_item_users">
			Пользователи			
		</li>
	</ul>
	<div id="calendar"></div>
	<!-- ОКНА -->
	<!-- СООБЩЕНИЕ -->
	<div id="new_message_window"></div>
	<div id="message_info_window"></div>
	<div id="message_delete_window"></div>
	<!-- СООБЩЕНИЯ -->
	<div id="new_messages_window"></div>
	<!-- БАТЧИ -->
	<div id="batch_info_window"></div>
</body>
</html>