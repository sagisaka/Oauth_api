$(document).ready(function() {
	$("#date_get").click(function(){
		search();
	});
});
function search(){
	var button = $(this);
	button.attr("disabled", true);
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
				for(var i in json){
					$("#output").append("<tr> <th scope=row>" + json[i].id + "</th> <td> <img id=img src=/image/"+json[i].imageUrl+"  width=100/> </td> <td> " + json[i].name + "</td> <td>"+ json[i].price + "円 </td> <td> " + json[i].author + "</td> <td> "+ json[i].productApi + "</td> </tr>");
				}
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
