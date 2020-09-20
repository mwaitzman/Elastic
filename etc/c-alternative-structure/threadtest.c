#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <time.h>
#include <pthread.h>
int lol = 0;
void* noe(void *whatever) {
	//printf("Hei, du er teit %i\n", lol++);

	return NULL;
}

int main() {
	pthread_t *thread = malloc(sizeof(pthread_t) * 5);;
	for (int c = 0; c < 10000; c++) {
		for (int i = 0; i < 5; i++) {
			if (pthread_create(&thread[i], NULL, noe, NULL)) {
				fprintf(stderr, "Nei, dette vil jeg ikke\n");
			}

		}

		for (int i = 0; i < 5; i++) {
			pthread_join(thread[i], NULL);
		}
	}


	return 0;
}
