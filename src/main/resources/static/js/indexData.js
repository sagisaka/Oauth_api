$(document).ready(function() {
	$("#allData_get").click(function(){
		allData();
	});
	$("#search_get").click(function(){
		search();
	});
});

function allData(){
	$.getScript("js/escape.js", function(){
		token = escape_html($("#token").val());
		$.ajax({
			type: 'GET',
			url:  '/api/product',
			headers: {
				'Authorization':token,
			},
			success: function(json) {
				$('#output').empty();
				for(var i in json){
					$("#output").append("<tr> <th scope=row>" + json[i].id + "</th> <td> <img id=img src=/image/"+json[i].imageUrl+"  width=100/> </td> <td> " + json[i].name + "</td> <td>"+ json[i].price + "円 </td> <td> " + json[i].author + "</td> <td><a href="+ json[i].id +">詳細ページへ</a></td> </tr>");
				}
			},
			error: function() {         // HTTPエラー時
				alert("商品リストを取得できませんでした");
			}
		});
	});
}
function search(){
	var button = $(this);
	button.attr("disabled", true);
	$.getScript("js/escape.js", function(){
		token = escape_html($("#token").val());
		name = escape_html($("#name").val());
		var data = {
				name: name,
		};
		// 通信実行
		$.ajax({
			type:"post",
			url:"/api/product/sam",
			headers: {
				'Authorization':token,
			},
			data:JSON.stringify(data),
			contentType: 'application/json',
			success: function(json) {
				$('#output').empty();
				for(var i in json){
					$("#output").append("<tr> <th scope=row>" + json[i].id + "</th> <td> <img id=img src=/image/"+json[i].imageUrl+"  width=100/> </td> <td> " + json[i].name + "</td> <td>"+ json[i].price + "円 </td> <td> " + json[i].author + "</td> <td><a href="+ json[i].id +">詳細ページへ</a></td> </tr>");
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
