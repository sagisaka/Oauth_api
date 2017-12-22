$(document).ready(function() {
	$("#date_get").click(function(){
		search();
	});
});
function search(){
	var button = $(this);
	button.attr("disabled", true);
	$("#length").text(0);
	$('#output').empty();
	$.getScript("js/escape.js", function(){
		token = escape_html($("#token").val());
		year = escape_html($("#year").val());
		month = escape_html($("#month").val());
		day = escape_html($("#day").val());
		date = year + "-" + month + "-" +day;
		var data = {
				logDate:date,
		};
		// 通信実行
		$.ajax({
			type:"post",
			url:"api/log",
			headers: {
				'Authorization':token,
			},
			data:JSON.stringify(data),
			contentType: 'application/json',
			success: function(json) {
				var createCount = 0;
				var deleteCount = 0;
				for(var i in json){
					if(json[i].productApi==="create"){
						createCount++;
					}else if(json[i].productApi==="delete"){
						deleteCount++;
					}
					$("#output").append("<tr> <th scope=row> <img id=img src=/image/"+json[i].imageUrl+"  width=100/> </th> <td> " + json[i].name + "</td> <td>"+ json[i].price + "円 </td> <td> " + json[i].author + "</td> <td> "+ json[i].productApi + "</td> </tr>");
				}
				$("#createLength").text(createCount);
				$("#deleteLength").text(deleteCount);
			},
			error: function() {         // HTTPエラー時
				alert("タイトル名と同名の商品リストを取得できませんでした");
			},
			complete: function() {
				button.attr("disabled", false);
			}
		});
	});
}
