$(document).ready(function() {
	$("#delete").click(function(){
		deleteData();
	});
	$("#upload").click(function(){
		updata();
	});
});
function updata() {
	var button = $(this);
	button.attr("disabled", true);
	//処理に不足があるか
	if($("#name").val() == 0){
		alert('商品名を入力してください');
		return;
	}else if($("#introduction").val().length==0){
		alert('紹介を入力してください');
		return;
	}else if($("#price").val().length==0){
		alert('価格を入力してください');
		return;
	}else if(isNaN($("#price").val())){
		alert('数値を入力してください');
		return;
	}else if($("#file").val().length==0){
		alert('fileを選択してください');
		return;
	}
	// 各フィールドから値を取得してJSONデータを作成
	$.getScript("js/escape.js", function(){
		name = escape_html($("#name").val());
		introduction = escape_html($("#introduction").val());
		price = escape_html($("#price").val());
		var formData = new FormData($('#form').get()[0]);
		formData.append("name",name);
		formData.append("introduction",introduction);
		formData.append("price",price);
		// 通信実行
		$.ajax({
			type:"POST",
			url:"/api/product/"+$("#s").text(),
			data:formData,
			processData:false,
			contentType:false,
			cache: false,
			dataType: 'json',
			success: function(json_data) {
				document.location = "/";
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
function deleteData() {
	var button = $(this);
	button.attr("disabled", true);
	if(window.confirm('この商品のデータを消しますか？')){
		$.ajax({
			type:"DELETE",
			url:"/api/product/"+$("#s").text(),
			success: function() {
				document.location = "/";
			},
			error: function() {         // HTTPエラー時
				alert("Server Error. Pleasy try again later.");
			},
			complete: function() {
				button.attr("disabled", false);
			}
		});
	}
}
