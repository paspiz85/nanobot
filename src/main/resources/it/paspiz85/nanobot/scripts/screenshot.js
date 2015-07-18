(function () {
	var file = platform.saveScreenshot("screen_" + new Date().getTime());
	alert("Saved in " + file.getAbsolutePath());
})();
