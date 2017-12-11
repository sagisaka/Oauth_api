$(document).ready(function() {
	$("#search_get").click(function(){
		search();
	});
});
function search(){
	var button = $(this);
	button.attr("disabled", true);
	$.getScript("js/escape.js", function(){
		day = escape_html($("#name").val());
		var data = {
				day: day,
		};
		// 通信実行
		$.ajax({
			type:"post",
			url:"/log",
			data:JSON.stringify(data),
			contentType: 'application/json',
			success: function(json) {
				$('#output').empty();
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
