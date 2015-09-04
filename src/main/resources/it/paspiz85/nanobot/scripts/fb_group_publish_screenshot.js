var IOUtils = Java.type("org.apache.commons.io.IOUtils");
var HttpGet = Java.type("org.apache.http.client.methods.HttpGet");
var HttpPost = Java.type("org.apache.http.client.methods.HttpPost");
var ArrayList = Java.type("java.util.ArrayList");
var UrlEncodedFormEntity = Java.type("org.apache.http.client.entity.UrlEncodedFormEntity");
var BasicNameValuePair = Java.type("org.apache.http.message.BasicNameValuePair");
var MultipartEntityBuilder = Java.type("org.apache.http.entity.mime.MultipartEntityBuilder");
var File = Java.type("java.io.File");
var FileInputStream = Java.type("java.io.FileInputStream");
var FileOutputStream = Java.type("java.io.FileOutputStream");
var System = Java.type("java.lang.System");

(function () {
	
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
	
	var accessToken = "";
	var tmpFile = new File(System.getProperty("java.io.tmpdir",".")+"/fb_token.tmp");
	if (tmpFile.exists()) {
		var inStream = new FileInputStream(tmpFile);
		accessToken = IOUtils.toString(inStream);
		inStream.close();
	}
	accessToken = prompt("Paste here Access Token (for example from https://developers.facebook.com/tools/explorer/)", accessToken);
	if (accessToken == null) {
		return;
	}
	var outStream = new FileOutputStream(tmpFile);
	IOUtils.write(accessToken, outStream);
	outStream.close();
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
	//alert(""+response.getStatusLine().getStatusCode());
	if (response.getStatusLine().getStatusCode() != 200) {
		alert(IOUtils.toString(response.getEntity().getContent()));
		return;
	}
	resData = JSON.parse(IOUtils.toString(response.getEntity().getContent()));
	selection = selectData(resData, "Select an album");
	var albumId = null;
	if (selection == null) {
		var albumName = prompt("Insert Album Name","");
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
	
	while (true) {
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
		if (!confirm("Do you want update another screenshot?")) {
			return;
		}
	}
})();
