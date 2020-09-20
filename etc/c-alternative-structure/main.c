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
	pthread_t thread;
	pthread_cond_t cond_started; // When the thread has finalized initialization
	pthread_cond_t cond;
	pthread_mutex_t lock;
	volatile void *func; // Method to run
	volatile int session; // Session id for the node
} threads[THREAD_COUNT];


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
void *thread_runner(void *args) { // Runs threads
	int thread_index = *((int *)args);
	printf("Thread %i launched\n", thread_index);
	pthread_cond_broadcast(threads[thread_index].cond_start);
	while (true) {
		pthread_cond_wait(&threads[thread_index].cond, &threads[thread_index].lock);
		if (threads[thread_index].func == 0) {
			fprintf(stderr, "No job set\n");
			exit(EXIT_FAILURE); // TODO is this okay inside a thread...? What will happen?
		}
		printf("Thread %i has gotten work\n", thread_index);
	}

	return NULL;
}

void init_threads() {
	// Clear memory
	printf("Hohho: %lu\n", sizeof(threads));
	memset(threads, 0, sizeof(threads));

	// Set up conditions, that we use to make the threads sleep
	pthread_cond_t cond = PTHREAD_COND_INITIALIZER;
	for (int i = 0; i < THREAD_COUNT; i++) {
		memcpy(&threads[THREAD_COUNT].cond_started, &cond, sizeof(cond));
		memcpy(&threads[THREAD_COUNT].cond, &cond, sizeof(cond));
	}

	pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;
	for (int i = 0; i < THREAD_COUNT; i++) {
		memcpy(&threads[THREAD_COUNT].lock, &lock, sizeof(pthread_mutex_t));
	}

	for (int i = 0; i < THREAD_COUNT; i++) {
		int *thread_index = malloc(sizeof(int));
		*thread_index = i;
		pthread_create(&threads[i].thread, NULL, thread_runner, thread_index);
	}

	// Wait for all threads to have inited
	for (int i = 0; i < THREAD_COUNT; i++) {
		printf("Waiting for thread %i to start\n", i);
		pthread_cond_wait(&threads[i].cond_started, &threads[i].lock);
		printf("Thread %i started\n", i);
	}

	for (int i = 0; i < THREAD_COUNT; i++) {
		pthread_cond_broadcast(&threads[i].cond);
	}

	for (int i = 0; i < THREAD_COUNT; i++) {
		pthread_join(threads[i].thread, NULL);
	}
}

void init_stdinout() { // Initializes communication using stdin and stdout

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

