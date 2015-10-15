var StateMainMenu = Java.type('it.paspiz85.nanobot.logic.StateMainMenu');

var active = true;
if (active) {
	if (context.getTrainCount() % 6 == 1) {
		StateMainMenu.instance().postMessage('RECLUTO IN CLAN DI SOLI MAGGIORENNI. NO BM. FATE RICHIESTA!');
	} else if (context.getTrainCount() % 6 == 4) {
		StateMainMenu.instance().postMessage('RECLUTO PER CLAN DI SOLI MAGGIORENNI. NO BM. FATE RICHIESTA!');
	}
}