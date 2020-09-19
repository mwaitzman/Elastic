#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <string.h>
#include <time.h>
#include <pthread.h>

#define VOICE_COUNT 256
#define THREAD_COUNT 4

struct {
	volatile bool working;
} thread[THREAD_COUNT];


struct Node_poly1 { // poly-node
	struct {
		float frequency;
	} node_sine1[VOICE_COUNT];

	int node_sine1_count;

	struct {
		float frequency;
	} node_square1[VOICE_COUNT];

	int node_square1_count;

	struct {
		float mix[VOICE_COUNT];
	} node_mix1;

	int node_mix1_count;
};

struct Node_group1 {
	struct {
		int voices;
	} node_poly1[VOICE_COUNT];

	int node_poly1_count;

	struct {
		float volume;
	} node_volume1[VOICE_COUNT];

	int node_volume1_count;
};


// Utility methods
void run_in_thread(void *func, int session) { // Will run a function with the argument "session". Will block if no threads available

}


// Group-node processors
void node_group1() { // Processing for group-node "group1"

}

void node_poly1() {

}


// Control methods
void init_threads() {
	pthread_t *thread = calloc(THREAD_COUNT, sizeof(pthread_t));
	for (int i = 0; i < THREAD_COUNT; i++) {
		pthread_create(thread[i], NULL, );
	}
}

void wait_for_data() {

}

void process() { // Will launch and run stuff in the threads set and then wait for all of them to be finished

}

void send_back() { // Send data back to Java-app

}

int main() {
	init_threads();
	for (int i = 0; i < 10; i++) {
		wait_for_data();
		process();
		send_back();
	}
	printf("%lu\n", sizeof(struct Node_group1));
	return 0;
}

