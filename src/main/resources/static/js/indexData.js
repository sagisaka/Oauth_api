$(document).ready(function() {
	allData();
	$("#search_get").click(function(){
		search();
	});
});

function allData(){
	$.ajax({
		type: 'GET',
		url:  '/api/product',
		success: function(json) {
			for(var i in json){
				$("#output").append("<tr> <th scope=row>" + json[i].id + "</th> <td> <img id=img src=/image/"+json[i].imageUrl+"  width=100/> </td> <td> " + json[i].name + "</td> <td>"+ json[i].price + "円 </td> <td><a href="+ json[i].id +">詳細ページへ</a></td> </tr>");
			}
		},
		error: function() {         // HTTPエラー時
			alert("Server Error. Pleasy try again later.");
		}
	});
}
function search(){
	var button = $(this);
	button.attr("disabled", true);
	$.getScript("js/escape.js", function(){
		name = escape_html($("#name").val());
		var data = {
				name: name,
		};
		// 通信実行
		$.ajax({
			type:"post",
			url:"/api/product/sam",
			data:JSON.stringify(data),
			contentType: 'application/json',
			success: function(json) {
				$('#output').empty();
				for(var i in json){
					$("#output").append("<tr> <th scope=row>" + json[i].id + "</th> <td> <img id=img src=/image/"+json[i].imageUrl+"  width=100/> </td> <td> " + json[i].name + "</td> <td>"+ json[i].price + "円 </td> <td><a href="+ json[i].id +">詳細ページへ</a></td> </tr>");
				}
			},
			error: function() {         // HTTPエラー時
				alert("Server Error. Pleasy try again later.");
			},
			complete: function() {
				button.attr("disabled", false);
			}
		});
	});
}
