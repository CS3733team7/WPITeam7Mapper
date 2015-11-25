CC = gcc
LDFLAGS = -lm
CFLAGS = -Wall -Ofast -fstrict-aliasing -march=native
OBJECTS = stb_image_precompile.o stb_image_write_precompile.o automapper.o

%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $<

automapper: 	$(OBJECTS)
	$(CC) $(CFLAGS) $(OBJECTS) -o $@ $(LDFLAGS)

debug:	CFLAGS= -Wall -O0 -g -fstrict-aliasing -march=native
debug: 	$(OBJECTS)
	$(CC) $(CFLAGS) $(OBJECTS) -o automapper-$@ $(LDFLAGS)
clean:
	@echo cleaning up
	@rm -f $(OBJECTS)
purge:
	@echo purging up
	@rm -f $(OBJECTS)
	@rm -f automapper
	@rm -f automapper-debug
