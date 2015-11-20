function showNotification(message) {
    var notification = document.getElementById('notification');
    notification.innerText = message;
    notification.className = 'fadein';
    setTimeout(function () {notification.className = null;}, 500);
}

function copyCode(code) {	
	chrome.permissions.request({
		permissions : ['clipboardWrite']
	}, function (granted) {
		if (granted) {
			var codeClipboard = document.getElementById('codeClipboard');
			codeClipboard.value = code;
			codeClipboard.focus();
			codeClipboard.select();
			document.execCommand('Copy');
			showNotification('copied');
		}
	});
}

function onWindowLoad() {
    chrome.tabs.executeScript(null, {
        file: "process.js"
    }, function(arr) {
		mcid = arr[0][0]
		info = arr[0][1]
		$("#code-0").text(info.ProductDetailInfo.ProductItemId);
		$("#code-0").click(function(){copyCode(info.ProductDetailInfo.ProductItemId)});
		$("#code-1").text(info.ProductDetailInfo.ProductId);
		$("#code-1").click(function(){copyCode(info.ProductDetailInfo.ProductId)});
		$("#code-2").text(mcid);
		$("#code-2").click(function(){copyCode(mcid)});
		$("#code-3").text(info.ProductDetailInfo.WarehouseId);
		$("#code-3").click(function(){copyCode(info.ProductDetailInfo.WarehouseId)});

		mcs = info.MerchantList
		var mccode = '';
		for (i = 0; i < mcs.length; i++) { 
			var mc = mcs[i];
			console.log(mc);
			if (mc.MerchantId == mcid) {
				mccode = mc.MerchantCode;
			}
		}
		
		$("#code-4").text(mccode);
		$("#code-4").click(function(){copyCode(mccode)});
		
		name = $('<textarea />').html(info.ProductDetailInfo.ProductName).text();
		$("#code-5").text(name);
		$("#code-5").click(function(){copyCode(name)});
		
		$("#code-6").text(info.ProductDetailInfo.SalePrice);
		$("#code-6").click(function(){copyCode(info.ProductDetailInfo.SalePrice)});
	});
}
function encode_utf8(s) {
  return unescape(encodeURIComponent(s));
}

function decode_utf8(s) {
  return decodeURIComponent(escape(s));
}
window.onload = onWindowLoad;