var IOUtils = Java.type("org.apache.commons.io.IOUtils");
(function () {
	
	var HttpGet = Java.type("org.apache.http.client.methods.HttpGet");
	var HttpPost = Java.type("org.apache.http.client.methods.HttpPost");
	var ArrayList = Java.type("java.util.ArrayList");
	var UrlEncodedFormEntity = Java.type("org.apache.http.client.entity.UrlEncodedFormEntity");
	var BasicNameValuePair = Java.type("org.apache.http.message.BasicNameValuePair");
	var MultipartEntityBuilder = Java.type("org.apache.http.entity.mime.MultipartEntityBuilder");
	var File = Java.type("java.io.File");

	
	function selectData(resJson, msg) {
		var options = [];
		var optionsResolver = {};
		for (var i in resJson.data) {
			var e = resJson.data[i];
			options.push(e.name);
			optionsResolver[e.name] = e;
		}
		var selection = select(msg, options);
		if (selection == null) {
			return null;
		}
		return optionsResolver[selection];
	}	
	
	var request, response, resData, selection;
	
	/*
	request = new HttpGet("https://graph.facebook.com/v2.4/oauth/access_token?client_id=" + appId + "&client_secret=" + appSecret + "&grant_type=client_credentials");
	response = httpClient.execute(request);
	//alert(""+response.getStatusLine().getStatusCode());
	if (response.getStatusLine().getStatusCode() != 200) {
		alert(IOUtils.toString(response.getEntity().getContent()));
		return;
	}
	//alert(IOUtils.toString(response.getEntity().getContent()));
	resData = JSON.parse(IOUtils.toString(response.getEntity().getContent()));
	var accessToken = resData.access_token;
	accessToken = accessToken.replace("|","%7C");
	alert(accessToken);
	*/
	var accessToken = prompt("Paste here Access Token");
	if (accessToken == null) {
		return;
	}
	request = new HttpGet("https://graph.facebook.com/v2.4/me/groups?access_token=" + accessToken);
	response = httpClient.execute(request);
	//alert(""+response.getStatusLine().getStatusCode());
	if (response.getStatusLine().getStatusCode() != 200) {
		alert(IOUtils.toString(response.getEntity().getContent()));
		return;
	}
	resData = JSON.parse(IOUtils.toString(response.getEntity().getContent()));
	selection = selectData(resData, "Select a group");
	if (selection == null) {
		return;
	}
	var groupId = selection.id;
	//var groupId = "1556084751307517";
	request = new HttpGet("https://graph.facebook.com/v2.4/" + groupId + "/albums?access_token=" + accessToken);
	response = httpClient.execute(request);
	alert(""+response.getStatusLine().getStatusCode());
	if (response.getStatusLine().getStatusCode() != 200) {
		alert(IOUtils.toString(response.getEntity().getContent()));
		return;
	}
	resData = JSON.parse(IOUtils.toString(response.getEntity().getContent()));
	selection = selectData(resData, "Select an album");
	var albumId = null;
	if (selection == null) {
		var albumName = prompt("Insert Album Name");
		if (albumName == null) {
			return;
		} else {
			request = new HttpPost("https://graph.facebook.com/v2.4/" + groupId + "/albums");
			var params = new ArrayList();
			params.add(new BasicNameValuePair("access_token", accessToken));
			params.add(new BasicNameValuePair("name", albumName));
	        request.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(request);
			//alert(""+response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != 200) {
				alert(IOUtils.toString(response.getEntity().getContent()));
				return;
			}
			resData = JSON.parse(IOUtils.toString(response.getEntity().getContent()));
			albumId = resData.id;
		}
	} else {
		albumId = selection.id;
	}
	
	request = new HttpPost("https://graph.facebook.com/v2.4/" + albumId + "/photos");
	var entityBuilder = MultipartEntityBuilder.create();
	entityBuilder.addTextBody("access_token", accessToken);
	entityBuilder.addBinaryBody("source", platform.saveScreenshot("screen_" + new Date().getTime()));
	//entityBuilder.addBinaryBody("source", new File("/Users/v-ppizzuti/workspace/nanobot/target/test-classes/features/img/train_1435772811358.png"));
	request.setEntity(entityBuilder.build());
	response = httpClient.execute(request);
	//alert(""+response.getStatusLine().getStatusCode());
	if (response.getStatusLine().getStatusCode() != 200) {
		alert(IOUtils.toString(response.getEntity().getContent()));
		return;
	}
	alert(IOUtils.toString(response.getEntity().getContent()));
})();
