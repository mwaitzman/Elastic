#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <time.h>

int main() {
	long size = 104857600;
	float *data = (float *)malloc(sizeof(float) * size);

	// This one is a bit slower than the loops below
	for (long i = 0; i < size; i++) 
		data[i] += (data[i] + 1.2f) * 0.51f;
	//	data[i] += sin(data[i] + 1.2f) * 0.51f;

	// This one is 4-5% faster with GCC and clang-10!
	//for (long i = 0; i < size; i++)
	//	data[i] += 1.2f;
	//for (long i = 0; i < size; i++)
	//	data[i] = sin(data[i]);
	//for (long i = 0; i < size; i++)
	//	data[i] *= 0.51f;

	// Loop unrolling, maybe done wrong. Identical performance as the one above
	//for (long i = 0; i < size; i+=4) {
	//	data[i+0] += 1.2f;
	//	data[i+1] += 1.2f;
	//	data[i+2] += 1.2f;
	//	data[i+3] += 1.2f;
	//}
	//for (long i = 0; i < size; i+=4) {
	//	data[i+0] = sin(data[i+0]);
	//	data[i+1] = sin(data[i+1]);
	//	data[i+2] = sin(data[i+2]);
	//	data[i+3] = sin(data[i+3]);
	//}
	//for (long i = 0; i < size; i+=4) {
	//	data[i+0] *= 0.51f;
	//	data[i+1] *= 0.51f;
	//	data[i+2] *= 0.51f;
	//	data[i+3] *= 0.51f;
	//}

	return data[1234] > 4.0f;
}
