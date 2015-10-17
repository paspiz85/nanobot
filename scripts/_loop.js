int n = 5;
if (context.getTrainCount() % (2*n) == 1) {
	postMessage('RECLUTO IN CLAN DI SOLI MAGGIORENNI. NO BM. FATE RICHIESTA!', false);
} else if (context.getTrainCount() % (2*n) == n+1) {
	postMessage('RECLUTO PER CLAN DI SOLI MAGGIORENNI. NO BM. FATE RICHIESTA!', false);
}