$(document).ready(function() {
	$("#create").click(function(){
		create();
	});
});
function create(){
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
	$.getScript("js/escape.js", function(){
		name = escape_html($("#name").val());
		introduction = escape_html($("#introduction").val());
		price = escape_html($("#price").val());
		var formData = new FormData($('#form').get()[0]);
		formData.append("name",name);
		formData.append("introduction",introduction);
		formData.append("price",price);
		$.ajax({
			url:'/api/product/',
			method:'post',
			data:formData,
			processData:false,
			contentType:false,
			cache: false,
			dataType: 'json',
			success: function(json) {
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
