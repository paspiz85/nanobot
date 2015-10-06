var Point = Java.type('it.paspiz85.nanobot.util.Point');

var active = false;
if (active) {   
    platform.leftClick(new Point(18,348), true);
    platform.sleepRandom(1000);
    platform.leftClick(new Point(76,23), true);
    platform.sleepRandom(200);
    platform.leftClick(new Point(18,63), true);
    platform.sleepRandom(200);
    var msg;
    if (context.getTrainCount() % 2 == 1) {
        msg = 'Recluto in clan di soli adulti';
    } else {
        msg = 'Recluto in clan di soli adulti.';
    }
    platform.write(msg);
    logger.info('Posted message "' + msg + '"');
    platform.leftClick(new Point(282,62), true);
    platform.sleepRandom(200);
    platform.leftClick(new Point(332,353), true);
    platform.sleepRandom(1000);
}