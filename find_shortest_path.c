#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

#define IN 9999999

FILE * fin;
FILE * fout;

typedef struct graf {
	int n;
	int **mat;
} graf;

graf *init(int n)
{
	graf *g = (graf *) malloc (sizeof(graf));
	g->n = n;
	g->mat = (int **) malloc (n * sizeof(int*));
	int i;
	for(i = 0;i < n;i++)
		g->mat[i] = (int *) calloc (n, sizeof(int));
	return g;
}
void add_edges(graf * g, int i, int j, int cost){
	g->mat[i][j] = cost;
// 	g->mat[j][i] = cost;
}


void reverse_array(int *pointer, int n)
{
  int *s, c, d;
  s = (int*)malloc(sizeof(int)*n);
  if( s == NULL )
      exit(EXIT_FAILURE);
  for ( c = n - 1, d = 0 ; c >= 0 ; c--, d++ )
      *(s+d) = *(pointer+c);
  for ( c = 0 ; c < n ; c++ )
      *(pointer+c) = *(s+c);
  free(s);
}

void dijsktra2(int **cost,int source,int target, int N){
    int i,m,min,start,d,j; 
    int * path_vector = (int*)malloc(sizeof(int)*N);
    int * dist = (int *)malloc(sizeof(int) * N);
    int * prev = (int *)malloc(sizeof(int) * N);
    int * selected = (int *)malloc(sizeof(int) * N);
    
    for(i=0;i < N;i++){
		for(j=0;j < N;j++){
			if(cost[i][j] == 0)
				cost[i][j] = IN;
			else
				cost[i][j] = cost[i][j];
		}
    }
				
    for(i=0; i<N; i++){
        selected[i] = 0;
    }
    for(i=0;i< N;i++){
        dist[i] = IN;
        prev[i] = -1;
    }
    start = source;
    selected[start] = 1;
    dist[start] = 0;
    while(selected[target] == 0)
    {
        min = IN;
        m = 0;
        for(i=0;i< N;i++)
        {
            d = dist[start] + cost[start][i];
            if(d < dist[i] && selected[i]==0)
            {
                dist[i] = d;
                prev[i] = start;
            }
            if(min>dist[i] && selected[i]==0)
            {
                min = dist[i];
                m = i;
            }
        }
        start = m;
        selected[start] = 1;
    }
    start = target;
    int k=0;
    while(start != -1)
    {
        path_vector[k++] = start;
        start = prev[start];
    }
    
    reverse_array(path_vector,k);
    int k2;
    for(k2 = 1; k2 < k; k2++){
        if(k2 == k-1){
            printf("%d",path_vector[k2]);
            fprintf(fout,"%d",path_vector[k2]);
        }else{
            printf("%d ",path_vector[k2]);
            fprintf(fout,"%d ",path_vector[k2]);
        }
    }
}
void printareMatrice(int ** mat, int dim){
    int i,j;
    for(i=0; i<dim; i++){
        for(j=0; j<dim; j++){
            if(j==dim-1){
                printf("%d ", mat[i][j]);
            }else{
                printf("%d\t", mat[i][j]);
            }
        }
        printf("\n");
    }
}
int ** Floyed(int **cost, int N){
	int **A = (int**)malloc(sizeof(int*) * N);
	int i,j,k;	
	for(i=0;i<N;i++){
		A[i] = (int*)calloc(N , sizeof(int));
	}
	for(i=0;i<N;i++){
		for(j=0;j<N;j++){
			if(cost[i][j] == 0)
			cost[i][j] = 999999;
		}
	}
	for(i=0;i<N;i++){
		for(j=0;j<N;j++){
			if(i != j)
			A[i][j] = cost[i][j];	
		}
	}
	for(i=0;i<N;i++){
		for(j=0;j<N;j++){
			if(i == j)
			A[i][j] = 0;
		}
	}
	for(k=0;k<N;k++){
		for(i=0;i<N;i++){
			for(j=0;j<N;j++){
				if(A[i][k]+A[k][j] < A[i][j]){
					A[i][j] = A[i][k] + A[k][j];
				}
			}
		}		
	}
	return A;
}

int calDis(int **matrice,int node1,int node2){
    int variabila = 0;
    variabila = matrice[node1][node2];
    return variabila;
    
}
int main(int argc, char * argv[]){
    
    fin = fopen(argv[1],"r");
    fout = fopen(argv[2],"w");
        
    char *line = (char*) calloc(50, sizeof(char));
    
    graf * g = NULL;
    int * clients;
    int i , k , nm , l , cl = 0;
    int val_dist = 0;
    int numar_task , numar_nodes , sursa , numar_muchii , numar_comenzi;
    fgets(line,50,fin);
    numar_task = atoi(line);
    
    int nrlinie = 0;
    while(fgets(line,50,fin)){
        nrlinie++;
        int len = strlen(line);
        char buf[50];
            if(nrlinie == 1){
                for(i=0;i<len;i++){
                    buf[i] = line[i];
                }
                numar_nodes = atoi(buf);
                g = init(numar_nodes);
            }
            if(nrlinie == 2){
                int tmp;
                for(tmp=0;tmp<12;tmp++)
                if(line[tmp] == '1'){
                    sursa = tmp/2;
                }
                
            }
            if(nrlinie == 3){
                for(l=0;l<len;l++){
                    buf[l] = line[l];
                }
                numar_muchii = atoi(buf);
            }
            if(nrlinie > 3){
                int z;
                if(len >= 6){
                    int n1;
                    if(line[0] != ' ' && line[1] == ' '){
                        buf[0] = line[0];
                        buf[1] = '\0';
                        n1 = atoi(buf);
                    }
                    if(line[0] != ' ' && line[1] != ' ' && line[2] == ' '){
                        buf[0] = line[0];
                        buf[1] = line[1];
                        buf[2] = '\0';
                        n1 = atoi(buf);
                    }
                    int n2;
                    if(line[1] == ' ' && line[2] != ' ' && line[3] == ' '){
                        buf[0] = line[2];
                        buf[1] = '\0';
                        n2 = atoi(buf);
                    }
                    if(line[1] == ' ' && line[2] != ' ' && line[3] != ' ' && line[4] == ' '){
                        buf[0] = line[2];
                        buf[1] = line[3];
                        buf[2] = '\0';
                        n2 = atoi(buf);
                    }
                    if(line[2] == ' ' && line[3] != ' ' && line[4] == ' '){
                        buf[0] = line[3];
                        buf[1] = '\0';
                        n2 = atoi(buf);
                    }
                    if(line[2] == ' ' && line[3] != ' ' && line[4] != ' ' && line[5] == ' '){
                        buf[0] = line[3];
                        buf[1] = line[4];
                        buf[2] = '\0';
                        n2 = atoi(buf);
                    }
                    int cst;
                    if(line[3] == ' '){
                        for(k=4;k<len;k++){
                            buf[k-4] = line[k];
                        }
                        cst = atoi(buf);
                    }
                    if(line[4] == ' '){
                        for(k=5;k<len;k++){
                            buf[k-5] = line[k];
                        }
                        cst = atoi(buf);
                    }
                    if(line[5] == ' '){
                        for(k=6;k<len;k++){
                            buf[k-6] = line[k];
                        }
                        cst = atoi(buf);
                    }
                    add_edges(g,n1,n2,cst);
                }
            }
            if(nrlinie == numar_muchii+4){
                for(i=0;i<len;i++){
                    buf[i] = line[i];
                }
                numar_comenzi = atoi(buf);
                clients = (int*)malloc(sizeof(int) * numar_comenzi);
            }
            if(nrlinie >= numar_muchii+5){
                for(i=0;i<len;i++){
                    buf[i+0] = line[i+2];
                    buf[i+1] = '\0';
                }
                int client = atoi(buf);
                clients[cl++] = client;
            }
    }
    fclose(fin);
    int **M = (int**)malloc(sizeof(int*) * g->n);
	int col;	
	for(col=0;col<g->n;col++){
		M[col] = (int*)calloc(g->n, sizeof(int));
	}
    
    printareMatrice(g->mat,g->n);
    for (i = 0; i < numar_comenzi; i++) {
        printf(" {%d} ",clients[i]);
    }
    
  	int var = 0;
	printf("\nDIJKSTA PATH:\n");
	printf("%d ",sursa);
	fprintf(fout,"%d ",sursa);
	for (i = 0; i < numar_comenzi; i++) {
	    
    	dijsktra(g->mat,sursa,clients[i],g->n);
    	M = Floyed(g->mat,g->n);
    	var += calDis(M,sursa,clients[i]);
    	printf(" ");
    	fprintf(fout," ");
    	dijsktra(g->mat,clients[i],sursa,g->n);
    	M = Floyed(g->mat,g->n);
    	var += calDis(M,clients[i],sursa);
    	printf(" ");
    	if(i != numar_comenzi-1){
            fprintf(fout," ");
    	}
    }
    printf("\nvar = %d\n",var);    
    fprintf(fout,"\n%d\n",var);
    
    fclose(fout);
    return 0;
}