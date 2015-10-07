var StateMainMenu = Java.type('it.paspiz85.nanobot.logic.StateMainMenu');

var active = false;
if (active) {
	if (context.getTrainCount() % 2 == 1) {
		StateMainMenu.instance().postMessage('Recluto in clan di soli adulti');
	} else {
		StateMainMenu.instance().postMessage('Recluto in clan di soli adulti.');
	}
}