/*
 * ---------------------------------------
 * Отправка SMS сообщений
 * ---------------------------------------
 */
var g_DateTime = null;

$(document).ready(function() 
{				
	$("#menu").kendoMenu().show();
	$("#calendar").kendoCalendar({culture: "ru-RU"});
	

	// ----------------------------
	// Создать сообщение
	// ----------------------------
	function CreateMessageWindow() 
	{	   
	   var new_message_wnd = $("#new_message_window");
	   
	   // -------------------------
	   // метод открытия окна
	   // -------------------------
	   function onOpen(e) 
       {              	   
    	   console.log(e);
    	    
    	   new_message_wnd.empty().append('<div style="text-align:center;width:100%;margin:10px"><input type="text" placeholder="номер абонента" class="input" id="abonent_number"></input></div>');    	   
    	   $("#abonent_number").kendoMaskedTextBox({mask: "00000000000"}).val(localStorage.getItem('abonent_number'));    	   
    	   //new_message_wnd.append('<div style="text-align:center;width:100%;margin:10px"><span style="font-size:21px">Запланировать:</span> <input id="datetimepicker" /></div>');
   		   
    	   /* 
    	    * --------------------------------------
    	    * событие при изменении календаря
			* --------------------------------------
    	    */
    	   function onDateTimePickerChange() {
    		   g_DateTime = this.value();    		   
    	   }
    	   
    	   // --------------------------
    	   // Дата и время (шедулер)
    	   // --------------------------
    	   $("#datetimepicker").kendoDateTimePicker({     		  
    		   culture: "ru-RU",    		   
    		   change: onDateTimePickerChange 
    		});
   		
    	   // -------------------
    	   // редактор
    	   // -------------------
    	   new_message_wnd.append('<textarea id="editor" placeholder=" Текст сообщения..."></textarea>');
    	   $("#new_message_window").append('<center><button id="btn_send_message" class="button" title="отправить сообщение абоненту">Отправить сообщение</button></center>');

    	   // ---------------------------------------------------
    	   //  Отправка сообщения
    	   // ---------------------------------------------------
    	   $("#btn_send_message").kendoButton().click(function() 
    	   {
    			if ( $("#abonent_number").val() == ""  || $("#editor").val() == "" ) 
    			alert("заполните все поля!");
    			else 
    			{    				
    				$.ajax
    				({
    					url: "/message/post",
    					type: "POST",
    					data: { number: $("#abonent_number").val(), text: $("#editor").val(), schedule: g_DateTime }
    				}).done( function(result){    					
    					localStorage.setItem('abonent_number', $("#abonent_number").val());
    					alert(result);    					
    				});    				
    			}
    	   	});    	   
       }

	   // ----------------------------------
       //  Создание окна
	   // ----------------------------------
       new_message_wnd.kendoWindow
       ({    	   
    	   title: "Новое сообщение",
    	   width: "700px",
           height: "460px",       
           visible: false,
           actions: [ "Minimize", "Close" ],
           open: onOpen              
       }).data("kendoWindow").center().open();       
	}
	
	// ---------------------------------
	// Информация о сообщении
	// 
	// ---------------------------------
	function CreateMessageInfoWindow() 
	{
		var message_info_wnd = $("#message_info_window");
		
		message_info_wnd.empty();
		message_info_wnd.append('<div style="text-align:center;width:100%;margin:10px"><input type="text" placeholder="id сообщения" class="input" id="message_id"></input><button class="button" id="message_info_button">инфо</button></div>');		
		message_info_wnd.append('<br><textarea placeholder=" Данные..." id="info_textarea" readonly></textarea>');
		
		$("#message_id").kendoMaskedTextBox({mask: "000000000"});
		
		// -- окошко -- 
		message_info_wnd.kendoWindow
		({    	   
			title: "Информация о сообщении",    
			width: "600px",
	        height: "440px",	           
	        visible: false,
	        actions: [ "Minimize", "Close" ],
	    }).data("kendoWindow").center().open();
		
		// -------------------------------------------------------
		// Получить информацию о сообщении
		// -------------------------------------------------------
		$("#message_info_button").kendoButton().click(function() 
		{
			if ($("#message_id").val() == "") alert("Требуется ID сообщения!")
			else {
				$.ajax
				({
					url: "/message/get",					
					data: { message_id: $("#message_id").val() },					
				}).done( function(result)
				{					
					if (result=="") alert("нет данных!");
					else
					{
						var obj = JSON.parse(result);
						$("#info_textarea").empty();
						$("#info_textarea").append("Дата отправки: "+obj.sent_time);
						$("#info_textarea").append("\nОтправитель: "+obj.sender);
						$("#info_textarea").append("\nНа номер: "+obj.recipient);
						$("#info_textarea").append("\nТекст: "+obj.message_text);
					}
				});  				
			}
		});						
	}	
	
	// ---------------------------------
	// Удалить сообщение
	// ---------------------------------
	function CreateMessageDeleteWindow() 
	{
		var message_delete_wnd = $("#message_delete_window");
		message_delete_wnd .empty().append('<div style="text-align:center;width:100%;margin:10px"><input type="text" placeholder="id сообщения" class="input" id="message_id_for_delete"></input><button class="button" id="message_delete_button">удалить</button></div>');
		
		$("#message_delete_button").kendoButton().click(function() {
		    if ( $("#message_id_for_delete").val()== "" ) alert("Не указан ID сообщения!")	
		});
		
		$("#message_id_for_delete").kendoMaskedTextBox({mask: "00000000000"});
		
		// -- окно удаления сообщения -- 
		message_delete_wnd.kendoWindow({
			title: "Удаление сообщения",
	        width: "550px",
	        height: "120px",	           
	        visible: false,
	        actions: [ "Minimize", "Close" ],
	       }).data("kendoWindow").center().open();		
	}
	
	
	/*
	 * --------------------------------------------------------------
	 *  Сообщения (2 пункт)
	 * -------------------------------------------------------------- 
	 */
	
	function CreateMessagesWindow()
	{
		 var new_messages_wnd = $("#new_messages_window");		
		 new_messages_wnd.empty();
		 //new_messages_wnd.append('<div style="width:100%;text-align:center;margin:5px;"><button id="button_new_sms" class="sms_messages_btn">добавить сообщение</button><button id="button_delete_sms" class="sms_messages_btn">удалить сообщение</button></div>');
		 
		 $(".sms_messages_btn").click(function() {
			 switch($(this).index())
			 {
			 	case 0: alert("ok0"); break;
			 	case 1: alert("ok1"); break;
			 }
		 });
		 		 
		 new_messages_wnd.append('<textarea placeholder=" Сообщение..." id="batchs_textarea">Тестовая рассылка</textarea>');
		 new_messages_wnd.append('<div id="sms_batch_list_grid"></div>');								
		
		 // -- окошко 
		 new_messages_wnd.kendoWindow
		 ({    	   
			 title: "Сообщения",    
			 width: "600px",
	         height: "620px",	           
	         visible: false,
	         actions: [ "Minimize", "Close" ],
	     }).data("kendoWindow").center().open();		 
		 
		 // -- Grid
		 $("#sms_batch_list_grid").kendoGrid
		 ({
             height: 280,
             sortable: true,
             pageable: 
             {
                 refresh: true,
                 pageSizes: true,
                 buttonCount: 5
             },
            columns: 
            [{
                 field: "phone",
                 title: "Номера",
                 width: 120
             }]             
         });
		 
		 var grid = $("#sms_batch_list_grid").data("kendoGrid");
		 
		 grid.dataSource.add( { phone: "77017344248" } );
		 grid.dataSource.add( { phone: "77015319087" } );
		 grid.dataSource.add( { phone: "77029599929" } );
		 		 		 
		 new_messages_wnd.append('<div style="width:100%;text-align:center;margin:5px;"><button id="button_send_sms" class="button sms_messages_btn">отправить</button></div>');
		 
		 /*
		  * ------------------------------------------
		  * Отправить пачку сообщений (CLICK)
		  * ------------------------------------------
		  */
		 $("#button_send_sms").click(function() {			 
			 var obj, arr, data, total;
			 
			 if ( $("#batchs_textarea").val() == "" ) {
				alert("Введите сообщение!");
				return;
			} 						
			 
			obj   = new Object();
			arr   = [];						
			data  = grid.dataSource.data();
			total = data.length;
			
			obj.text = $("#batchs_textarea").val();
			
			for (var i=0; i<total; i++ ) 
				arr.push(data[i].phone);			
			
			obj.numbers = arr;												
			 
			$.ajax
			({
				url: "/batches/post",					
				data: { data: JSON.stringify(obj) },
				method: "POST",
			}).done( function(result) {				
				alert(result);
			});
		});
		 
		 $("button").css("font-size","21px").css("margin","1px").css("padding","7px").css("border-radius","5px").kendoButton();
	}
	
	// получить информацию о батче
	function CreateBatchInfoWindow(){
		
		var batch_info_wnd = $("#batch_info_window");
		
		batch_info_wnd.empty();
		batch_info_wnd.append('<div style="text-align:center;width:100%;margin:10px"><input type="text" placeholder="id батча" class="input" id="batch_id"></input><button class="button" id="batch_info_button">инфо</button></div>');		
		batch_info_wnd.append('<br><textarea placeholder=" Данные..." id="batch_info_textarea" readonly></textarea>');
		
		$("#batch_id").kendoMaskedTextBox({mask: "00000000"});
		
		// создать окно для вывода информации о пачке сообщений
		batch_info_wnd.kendoWindow
		({    	   
			title: "Информация о батче",    
			width: "800px",
	        height: "600px",	           
	        visible: false,
	        actions: [ "Minimize", "Close" ],
	    }).data("kendoWindow").center().open();
		
		// запросить информацию
		$("#batch_info_button").kendoButton().click(function() {
			if ($("#batch_id").val() == "") alert("Требуется ID батча!")
			else {
				$.ajax
				({
					url: "/batches/get",					
					data: { batch_id: $("#batch_id").val() },					
				}).done( function(result) {					
					if ( result == "" ) alert( "нет данных!" );
					else {
						var obj = JSON.parse(result);
						$("#batch_info_textarea").empty().text("Текст: "+obj[0].message_text+"\n");
						var error_msg="";
						$.each(obj, function(index, value) {
							if (value.error_message == undefined) error_msg = "без ошибок";
							else error_msg = value.error_message;								
							$("#batch_info_textarea").append("----------------\n"+(index+1)+" cообщение\n----------------\nНомер: "+value.recipient+"\nСтатус: "+value.message_status+"\nДополнительно: "+error_msg+"\n");							
						});
					}
				});  				
			}
		});
	}
	
	/*
	 * --------------------------------
	 * Обработка меню	
	 * --------------------------------
	 */
	$("#menu li").click(function() {
		switch($(this).attr("id")) {			
			case "menu_item_message_create": 	CreateMessageWindow(); break;
			case "menu_item_message_info": 		CreateMessageInfoWindow(); break;
			case "menu_item_message_delete": 	CreateMessageDeleteWindow(); break;			
			case "menu_item_messages_create": 	CreateMessagesWindow(); break;		
			case "menu_item_batch_info": 		CreateBatchInfoWindow(); break;
		}
	});
});