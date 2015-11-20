regex_mcid = '<a id="selected_merchant_name" href="/.*?-mc([0-9]*?)"'
allHtml = document.body.outerHTML;
mcid = allHtml.match(regex_mcid)[1];	
s = allHtml.match('ParamDetail.DataDetail = eval\\((.*?)\\);')[1];
info = JSON.parse(s);
arr = [mcid, info]

arr