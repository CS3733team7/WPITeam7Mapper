#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <string.h>
#include "stb_image.h"


#define TRUE 1
#define FALSE 0
typedef struct ivec2_s {
	int x;
	int y;
} ivec2_t;

typedef struct quadleaf_s {
	ivec2_t center;
	int type;	//0 is mixed, 1 is good, -1 is bad
	int depth;
	struct quadleaf_s * parent;
	struct quadleaf_s * children[4];
	int bbox[4];
	ivec2_t bboxp[4];
	struct edge_s ** edges;
	int numedges;
} quadleaf_t;

typedef struct edge_s {
	quadleaf_t * n1;
	quadleaf_t * n2;
} edge_t;


quadleaf_t root = {0};

quadleaf_t ** nodes;
int numnodes = 0;

edge_t * edges;
int numedges = 0;


void addNode(quadleaf_t *n){
	numnodes++;
	nodes = realloc(nodes, numnodes * sizeof(quadleaf_t *));
	nodes[numnodes-1] = n;
}




int width = 0;
int height = 0;
int imagen = 0;
unsigned char * imagedata = 0;

int linebox(ivec2_t start, ivec2_t end, float * bbox){
	
	//todo
	return FALSE;
}


int checkline(ivec2_t start, ivec2_t end){
	if(start.y > end.y){
		int t = start.y;
		start.y = end.y;
		end.y = t;
	}
	//todo walk the tree
	float dx = start.x - end.x;
	if(dx != 0.0){
		float dy = start.y - end.y;
		float err = 0;
		float derr = abs(dy / dx);
		int x, y = start.y;
		for(x = start.x; x < end.x; x++){
			if(imagedata[(y * width + x) * imagen] < 129) return FALSE;
			err += derr;
			while(err >= 0.5f){
				if(imagedata[(y * width + x) * imagen] < 129) return FALSE;
				y++;
				err-=1.0;
			}
		}
	} else { //vertical
		int y;
		for(y = start.y; y < end.y; y++){
			if(imagedata[(y * width + start.x) * imagen] < 129) return FALSE;
		}
	}
	return TRUE;
}

void bboxpfrombbox(const int * bbox, ivec2_t * bboxp){
	bboxp[0].x = bbox[0];
	bboxp[0].y = bbox[1];
	bboxp[1].x = bbox[2];
	bboxp[1].y = bbox[1];
	bboxp[2].x = bbox[2];
	bboxp[2].y = bbox[3];
	bboxp[3].x = bbox[0];
	bboxp[3].y = bbox[3];
}

ivec2_t centerfrombbox(const int *bbox){
	ivec2_t out = {(bbox[0] + bbox[2])/2, (bbox[1] + bbox[3])/ 2};
	return out;
}
int mdepth = 0;
int genleafs(quadleaf_t *parent){
	if(parent->depth > mdepth) mdepth = parent->depth;
	bboxpfrombbox(parent->bbox, parent->bboxp);
	parent->center = centerfrombbox(parent->bbox);
//	printf("depth %i, %i %i\n", parent->depth, parent->bbox[0], parent->bbox[2]);

	//walk entire pixels of parent, check what type it is
	char hasgood = 0;
	char hasbad = 0;
	int x, y, maxx, maxy;
	maxx = parent->bbox[2];
	maxy = parent->bbox[3];
	for(y = parent->bbox[1]; y < maxy; y++){
	for(x = parent->bbox[0]; x < maxx; x++){
		if(imagedata[(y * width + x) * imagen] > 128) hasgood = TRUE;
		else hasbad = TRUE;
		if(hasgood && hasbad){ //both already triggered
			x = maxx;
			y = maxy;
		}
	}}
	parent->type = 0;
	if(hasbad) parent->type--;
	if(hasgood) parent->type++;
	if((!hasgood && !hasbad)) return 0;
	if(parent->type == 1){ //add to node list
		addNode(parent);
	}
	if(parent->type) return 0;
	int dx = parent->bbox[0] - maxx; dx = dx < 0 ? -dx : dx;
	int dy = parent->bbox[1] - maxy; dy = dy < 0 ? -dy : dy;
	if(dx < 10 || dy < 10) return 0;
	//we must split up here

	quadleaf_t * children = malloc(4 * sizeof(quadleaf_t));
	memset(children, 0, 4 * sizeof(quadleaf_t));
	children[0].bbox[0] = parent->bbox[0];
	children[0].bbox[1] = parent->bbox[1];
	children[0].bbox[2] = parent->center.x;
	children[0].bbox[3] = parent->center.y;
	children[1].bbox[0] = parent->center.x;
	children[1].bbox[1] = parent->bbox[1];
	children[1].bbox[2] = parent->bbox[2];
	children[1].bbox[3] = parent->center.y;

	children[2].bbox[0] = parent->bbox[0];
	children[2].bbox[1] = parent->center.y;
	children[2].bbox[2] = parent->center.x;
	children[2].bbox[3] = parent->bbox[3];

	children[3].bbox[0] = parent->center.x;
	children[3].bbox[1] = parent->center.y;
	children[3].bbox[2] = parent->bbox[2];
	children[3].bbox[3] = parent->bbox[3];
	int i;
	int total = 4;
	for(i = 0; i < 4; i++){
		quadleaf_t *c = &children[i];
		parent->children[i] = c;
		c->parent = parent;
		c->depth = parent->depth+1;
		total += genleafs(c);
	}
	return total;

}

int main (const int argc, const char ** argv){

	imagedata = stbi_load(argv[1], &width, &height, &imagen, 0);
	if(!imagedata) return TRUE;

	root.bbox[2] = width;
	root.bbox[3] = height;


	int total = genleafs(&root);

	//run through nodes, see if you can make an edge to every other node
	int i, z, x = 0;
	for(i = 0; i < numnodes; i++){
		quadleaf_t * fl = nodes[i];
		for(z = i+1; z < numnodes; z++){
			quadleaf_t *el = nodes[z];
			if(checkline(fl->center, el->center)) x++;
		}
//		printf("generated %i leafs, max depth %i, total edges %i\n", total, mdepth, x);
	}
	printf("generated %i leafs, max depth %i, total edges %i\n", total, mdepth, x);

	stbi_image_free(imagedata);
	return FALSE;
}
