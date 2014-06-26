:- use_module(library(pairs)).

%Utilidades
% Generar Lista de Equipos
genEquipos(Equipos):-
 findall(R,(partido(R,_,_,_,_)),Equipos1),
 findall(R,(partido(R,_,_,_,_)),Equipos2),
 concatenar(Equipos1,Equipos2,EquiposT),
 sort(EquiposT,Equipos).

%Concatenar Listas
concatenar([],L,L).
concatenar([X|L1],L2,[X|L3]):-concatenar(L1,L2,L3).

%Sumar Elementos de Lista

sumaElementos([], 0).

sumaElementos([X|Xs], S):-
 sumaElementos(Xs, S2),
 S is S2 + X.

%Empaquetar
empaquetar([],[]).
empaquetar([X|Xs],[Z|Zs]) :- transferir(X,Xs,Ys,Z), empaquetar(Ys,Zs).


%Transferir

transferir(X,[],[],[X]).
transferir(X,[Y|Ys],[Y|Ys],[X]) :- X \= Y.
transferir(X,[X|Xs],Ys,[X|Zs]) :- transferir(X,Xs,Ys,Zs).

%Transformar, convierte las sublistas a pares en forma n_repeticiones-llave

transformar([],[]).
transformar([[X|Xs]|Ys],[(N-X)|Zs]) :-
 length([X|Xs],N),
 transformar(Ys,Zs).

%Codificar
/*Convierte de una lista desordenada a una lista con pares ordenados 
n_repeticiones-llave, de menor numero de repeticines a mayor.*/

codificar(L1,L2) :- 
 msort(L1,LS),
 empaquetar(LS,L), 
 transformar(L,L2).

%Ultimo
/*Encuentra el ultimo elemento de la lista.*/

ultimo(X,[X]).
ultimo(X,[_|L]) :- ultimo(X,L).

%Separador
/*Separa por el simbolo -*/

sep_dash(L,As):-
 pairs_values(L,As).

%Resta

resta([A,B],R):-
 R is (A-B).

%Restas en lista

restarLista([],[]).

restarLista([A|Rest],[Sum|C]):-
 resta(A,Sum),
 restarLista(Rest,C).

%Comparador
comparador(>, (_,C1),(_,C2)) :-
        C1>C2.

comparador(<, (_,C1),(_,C2)) :-
        C1=<C2.

%Parte 1

%Goles

goles(X,Y,G):-
 findall(Z, partido(X,Y,Z,_,_),L1),
 findall(Z, partido(Y,X,_,Z,_),L2),
 concatenar(L1,L2,GT),
 sumaElementos(GT, G).

%Victorias

victorias(X,Y,V):-
 findall(1,(partido(X,Y,G1,G2,_),G1>G2),V1),
 findall(1,(partido(Y,X,G1,G2,_),G1<G2),V2),
 concatenar(V1,V2,VT),
 sumaElementos(VT,V).

%Perdedor

perdedor(X,R,C):-
 findall(1,(partido(X,_,G1,G2,R),G1<G2),P1),
 findall(1,(partido(_,X,G1,G2,R),G1>G2),P2),
 concatenar(P1,P2,P),
 sumaElementos(P,C).

%Ganadores de Ronda, con repeticion

ganadoresRonda(R,G):-
 findall(X,(partido(X,_,G1,G2,R),G1>G2),GR1),
 findall(X,(partido(_,X,G1,G2,R),G1<G2),GR2),
 concatenar(GR1,GR2,G).

%Ganadores de Mundial

ganadores(L,M):-
 ganadoresRonda(final,LT),
 sort(LT,L),
 codificar(LT,LC),
 keysort(LC,MS),
 ultimo(MT,MS),
 sep_dash([MT],MT2),
 ultimo(M,MT2). 
 
%Parte 2
%Lista de Rondas

rondas([[grupos,1],[octavos,2],[cuartos,3],[semifinales,4],[tercero,4],[final,5]]).

%Lista de Equipos

equipos([algeria, angola, argentina, australia, austria, belgium, bolivia, brazil, bulgaria, cameroon, canada, chile, china, colombia, costa_rica, cote_divoire, croatia, cuba, czech_republic, czechoslovakia, denmark, dutch_east_indies, east_germany, ecuador, egypt, el_salvador, england, france, germany, ghana, greece, haiti, honduras, hungary, iran, iraq, ireland, israel, italy, jamaica, japan, kuwait, mexico, morocco, netherlands, new_zealand, nigeria, north_korea, northern_ireland, norway, paraguay, peru, poland, portugal, romania, russia, saudi_arabia, scotland, senegal, serbia, slovakia, slovenia, south_africa, south_korea, soviet_union, spain, sweden, switzerland, togo, trinidad_and_tobago, tunisia, turkey, ukraine, united_arab_emirates, united_states, uruguay, wales, west_germany, yugoslavia, zaire]).

%Diferencia de Goles Equipo-Ronda
/*Entrega la diferencia de goles de un equipo en cierta ronda*/

diferenciaRonda(Equipo,[Ronda|Multiplicador],Diferencia):-
 findall([G1,G2],(partido(Equipo,_,G1,G2,Ronda)),DR1),
 findall([G2,G1],(partido(_,Equipo,G1,G2,Ronda)),DR2),
 concatenar(DR1,DR2,DR),
 restarLista(DR,DT),
 sumaElementos(DT,DT2),
 Diferencia is Multiplicador*DT2.

%Diferencia de Goles Equipo todas las rondas

diferenciaTodasRondas(_,[],[]).

diferenciaTodasRondas(Equipo,[[RondaActual|Multiplicador]|RondasRestantes],[DiferenciaActual|DiferenciasPasadas]):-
 diferenciaRonda(Equipo,[RondaActual|Multiplicador],DiferenciaActual),
 diferenciaTodasRondas(Equipo,RondasRestantes,DiferenciasPasadas).

%Suma de Diferencias de todas las Rondas para un equipo

diferenciaTodasRondasSumadas(Equipo,Diferencia):-
 rondas(Rondas),
 diferenciaTodasRondas(Equipo,Rondas,DT),
 sumaElementos(DT,Diferencia).

%Diferencias de todos los equipos

diferenciaTodosEquipos([],[]).

diferenciaTodosEquipos([EquipoActual|EquiposRestantes],[(EquipoActual,Diferencia)|DiferenciasPasadas]):-
 diferenciaTodasRondasSumadas(EquipoActual,Diferencia),
 diferenciaTodosEquipos(EquiposRestantes,DiferenciasPasadas).

%Campeon

campeon(X):-
 equipos(Equipos),
 diferenciaTodosEquipos(Equipos,D),
 predsort(comparador, D , DS),
 ultimo((X,_),DS).
 
 
 

 

%Seccion de hechos entregados
partido(mexico,france,1,4,grupos).
partido(france,argentina,0,1,grupos).
partido(mexico,chile,0,3,grupos).
partido(france,chile,0,1,grupos).
partido(mexico,argentina,3,6,grupos).
partido(chile,argentina,1,3,grupos).
partido(brazil,yugoslavia,1,2,grupos).
partido(bolivia,yugoslavia,0,4,grupos).
partido(bolivia,brazil,0,4,grupos).
partido(peru,romania,1,3,grupos).
partido(peru,uruguay,0,1,grupos).
partido(romania,uruguay,0,4,grupos).
partido(belgium,united_states,0,3,grupos).
partido(paraguay,united_states,0,3,grupos).
partido(belgium,paraguay,0,1,grupos).
partido(united_states,argentina,1,6,semifinales).
partido(yugoslavia,uruguay,1,6,semifinales).
partido(argentina,uruguay,2,4,final).
partido(argentina,sweden,2,3,grupos).
partido(france,austria,3,4,grupos).
partido(belgium,germany,2,5,grupos).
partido(brazil,spain,1,3,grupos).
partido(egypt,hungary,2,4,grupos).
partido(netherlands,switzerland,2,3,grupos).
partido(united_states,italy,1,7,grupos).
partido(romania,czechoslovakia,1,2,grupos).
partido(switzerland,czechoslovakia,2,3,cuartos).
partido(sweden,germany,1,2,cuartos).
partido(spain,italy,1,1,cuartos).
partido(hungary,austria,1,2,cuartos).
partido(spain,italy,0,1,cuartos).
partido(austria,italy,0,1,semifinales).
partido(germany,czechoslovakia,1,3,semifinales).
partido(austria,germany,2,3,tercero).
partido(czechoslovakia,italy,2,3,final).
partido(germany,switzerland,2,2,grupos).
partido(dutch_east_indies,hungary,0,6,grupos).
partido(belgium,france,1,3,grupos).
partido(romania,cuba,5,5,grupos).
partido(norway,italy,2,3,grupos).
partido(poland,brazil,9,10,grupos).
partido(netherlands,czechoslovakia,0,3,grupos).
partido(romania,cuba,1,2,grupos).
partido(switzerland,germany,4,2,grupos).
partido(czechoslovakia,brazil,2,2,cuartos).
partido(hungary,switzerland,2,0,cuartos).
partido(cuba,sweden,0,8,cuartos).
partido(italy,france,3,1,cuartos).
partido(czechoslovakia,brazil,1,2,cuartos).
partido(sweden,hungary,1,5,semifinales).
partido(brazil,italy,1,2,semifinales).
partido(brazil,sweden,4,2,tercero).
partido(italy,hungary,4,2,final).
partido(mexico,brazil,0,4,grupos).
partido(switzerland,yugoslavia,0,3,grupos).
partido(switzerland,brazil,2,2,grupos).
partido(mexico,yugoslavia,1,4,grupos).
partido(yugoslavia,brazil,0,2,grupos).
partido(mexico,switzerland,1,2,grupos).
partido(chile,england,0,2,grupos).
partido(united_states,spain,1,3,grupos).
partido(chile,spain,0,2,grupos).
partido(england,united_states,0,1,grupos).
partido(england,spain,0,1,grupos).
partido(united_states,chile,2,5,grupos).
partido(italy,sweden,2,3,grupos).
partido(paraguay,sweden,2,2,grupos).
partido(paraguay,italy,0,2,grupos).
partido(bolivia,uruguay,0,8,grupos).
partido(spain,uruguay,2,2,octavos).
partido(sweden,brazil,1,7,octavos).
partido(spain,brazil,1,6,octavos).
partido(sweden,uruguay,2,3,octavos).
partido(spain,sweden,1,3,octavos).
partido(brazil,uruguay,1,2,octavos).
partido(mexico,brazil,0,5,grupos).
partido(france,yugoslavia,0,1,grupos).
partido(yugoslavia,brazil,2,2,grupos).
partido(mexico,france,2,3,grupos).
partido(turkey,west_germany,1,4,grupos).
partido(south_korea,hungary,0,9,grupos).
partido(west_germany,hungary,3,8,grupos).
partido(south_korea,turkey,0,7,grupos).
partido(turkey,west_germany,2,7,grupos).
partido(czechoslovakia,uruguay,0,2,grupos).
partido(scotland,austria,0,1,grupos).
partido(czechoslovakia,austria,0,5,grupos).
partido(scotland,uruguay,0,7,grupos).
partido(belgium,england,7,7,grupos).
partido(italy,switzerland,1,2,grupos).
partido(switzerland,england,0,2,grupos).
partido(belgium,italy,1,4,grupos).
partido(italy,switzerland,1,4,grupos).
partido(switzerland,austria,5,7,cuartos).
partido(england,uruguay,2,4,cuartos).
partido(west_germany,yugoslavia,2,0,cuartos).
partido(hungary,brazil,4,2,cuartos).
partido(austria,west_germany,1,6,semifinales).
partido(uruguay,hungary,4,6,semifinales).
partido(austria,uruguay,3,1,tercero).
partido(west_germany,hungary,3,2,final).
partido(argentina,west_germany,1,3,grupos).
partido(czechoslovakia,northern_ireland,0,1,grupos).
partido(northern_ireland,argentina,1,3,grupos).
partido(czechoslovakia,west_germany,2,2,grupos).
partido(northern_ireland,west_germany,2,2,grupos).
partido(argentina,czechoslovakia,1,6,grupos).
partido(czechoslovakia,northern_ireland,2,3,grupos).
partido(paraguay,france,3,7,grupos).
partido(scotland,yugoslavia,1,1,grupos).
partido(france,yugoslavia,2,3,grupos).
partido(scotland,paraguay,2,3,grupos).
partido(scotland,france,1,2,grupos).
partido(yugoslavia,paraguay,3,3,grupos).
partido(mexico,sweden,0,3,grupos).
partido(wales,hungary,1,1,grupos).
partido(wales,mexico,1,1,grupos).
partido(hungary,sweden,1,2,grupos).
partido(wales,sweden,0,0,grupos).
partido(mexico,hungary,0,4,grupos).
partido(hungary,wales,1,2,grupos).
partido(austria,brazil,0,3,grupos).
partido(england,soviet_union,2,2,grupos).
partido(england,brazil,0,0,grupos).
partido(austria,soviet_union,0,2,grupos).
partido(austria,england,2,2,grupos).
partido(soviet_union,brazil,0,2,grupos).
partido(england,soviet_union,0,1,grupos).
partido(northern_ireland,france,0,4,cuartos).
partido(soviet_union,sweden,0,2,cuartos).
partido(wales,brazil,0,1,cuartos).
partido(yugoslavia,west_germany,0,1,cuartos).
partido(brazil,france,5,2,semifinales).
partido(sweden,west_germany,3,1,semifinales).
partido(france,west_germany,6,3,tercero).
partido(brazil,sweden,5,2,final).
partido(colombia,uruguay,1,2,grupos).
partido(yugoslavia,soviet_union,0,2,grupos).
partido(uruguay,yugoslavia,1,3,grupos).
partido(colombia,soviet_union,4,4,grupos).
partido(uruguay,soviet_union,1,2,grupos).
partido(colombia,yugoslavia,0,5,grupos).
partido(switzerland,chile,1,3,grupos).
partido(italy,west_germany,0,0,grupos).
partido(italy,chile,0,2,grupos).
partido(switzerland,west_germany,1,2,grupos).
partido(chile,west_germany,0,2,grupos).
partido(switzerland,italy,0,3,grupos).
partido(mexico,brazil,0,2,grupos).
partido(spain,czechoslovakia,0,1,grupos).
partido(czechoslovakia,brazil,0,0,grupos).
partido(mexico,spain,0,1,grupos).
partido(spain,brazil,1,2,grupos).
partido(czechoslovakia,mexico,1,3,grupos).
partido(bulgaria,argentina,0,1,grupos).
partido(england,hungary,1,2,grupos).
partido(argentina,england,1,3,grupos).
partido(bulgaria,hungary,1,6,grupos).
partido(argentina,hungary,0,0,grupos).
partido(bulgaria,england,0,0,grupos).
partido(soviet_union,chile,1,2,cuartos).
partido(west_germany,yugoslavia,0,1,cuartos).
partido(england,brazil,1,3,cuartos).
partido(hungary,czechoslovakia,0,1,cuartos).
partido(chile,brazil,2,4,semifinales).
partido(yugoslavia,czechoslovakia,1,3,semifinales).
partido(yugoslavia,chile,0,1,tercero).
partido(czechoslovakia,brazil,1,3,final).
partido(uruguay,england,0,0,grupos).
partido(mexico,france,1,1,grupos).
partido(france,uruguay,1,2,grupos).
partido(mexico,england,0,2,grupos).
partido(uruguay,mexico,0,0,grupos).
partido(france,england,0,2,grupos).
partido(switzerland,west_germany,0,5,grupos).
partido(spain,argentina,1,2,grupos).
partido(switzerland,spain,1,2,grupos).
partido(west_germany,argentina,0,0,grupos).
partido(switzerland,argentina,0,2,grupos).
partido(spain,west_germany,1,2,grupos).
partido(bulgaria,brazil,0,2,grupos).
partido(hungary,portugal,1,3,grupos).
partido(brazil,hungary,1,3,grupos).
partido(bulgaria,portugal,0,3,grupos).
partido(brazil,portugal,1,3,grupos).
partido(bulgaria,hungary,1,3,grupos).
partido(north_korea,soviet_union,0,3,grupos).
partido(chile,italy,0,2,grupos).
partido(north_korea,chile,1,1,grupos).
partido(italy,soviet_union,0,1,grupos).
partido(italy,north_korea,0,1,grupos).
partido(chile,soviet_union,1,2,grupos).
partido(argentina,england,0,1,cuartos).
partido(uruguay,west_germany,0,4,cuartos).
partido(hungary,soviet_union,1,2,cuartos).
partido(north_korea,portugal,3,5,cuartos).
partido(soviet_union,west_germany,1,2,semifinales).
partido(portugal,england,1,2,semifinales).
partido(soviet_union,portugal,1,2,tercero).
partido(west_germany,england,4,6,final).
partido(soviet_union,mexico,0,0,grupos).
partido(el_salvador,belgium,0,3,grupos).
partido(belgium,soviet_union,1,4,grupos).
partido(el_salvador,mexico,0,4,grupos).
partido(el_salvador,soviet_union,0,2,grupos).
partido(belgium,mexico,0,1,grupos).
partido(israel,uruguay,0,2,grupos).
partido(sweden,italy,0,1,grupos).
partido(italy,uruguay,0,0,grupos).
partido(israel,sweden,1,1,grupos).
partido(sweden,uruguay,1,0,grupos).
partido(italy,israel,0,0,grupos).
partido(england,romania,1,0,grupos).
partido(brazil,czechoslovakia,4,1,grupos).
partido(czechoslovakia,romania,1,2,grupos).
partido(brazil,england,1,0,grupos).
partido(brazil,romania,3,2,grupos).
partido(czechoslovakia,england,0,1,grupos).
partido(bulgaria,peru,2,3,grupos).
partido(west_germany,morocco,2,1,grupos).
partido(morocco,peru,0,3,grupos).
partido(west_germany,bulgaria,5,2,grupos).
partido(west_germany,peru,3,1,grupos).
partido(morocco,bulgaria,1,1,grupos).
partido(uruguay,soviet_union,1,0,cuartos).
partido(mexico,italy,1,4,cuartos).
partido(peru,brazil,2,4,cuartos).
partido(england,west_germany,4,5,cuartos).
partido(brazil,uruguay,3,1,semifinales).
partido(west_germany,italy,4,5,semifinales).
partido(west_germany,uruguay,1,0,tercero).
partido(italy,brazil,1,4,final).
partido(chile,west_germany,0,1,grupos).
partido(australia,east_germany,0,2,grupos).
partido(east_germany,chile,1,1,grupos).
partido(west_germany,australia,3,0,grupos).
partido(chile,australia,0,0,grupos).
partido(west_germany,east_germany,0,1,grupos).
partido(yugoslavia,brazil,0,0,grupos).
partido(scotland,zaire,2,0,grupos).
partido(zaire,yugoslavia,0,9,grupos).
partido(brazil,scotland,0,0,grupos).
partido(yugoslavia,scotland,1,1,grupos).
partido(brazil,zaire,3,0,grupos).
partido(netherlands,uruguay,2,0,grupos).
partido(bulgaria,sweden,0,0,grupos).
partido(sweden,netherlands,0,0,grupos).
partido(bulgaria,uruguay,1,1,grupos).
partido(bulgaria,netherlands,1,4,grupos).
partido(uruguay,sweden,0,3,grupos).
partido(haiti,italy,1,3,grupos).
partido(argentina,poland,2,3,grupos).
partido(poland,haiti,7,0,grupos).
partido(italy,argentina,1,1,grupos).
partido(haiti,argentina,1,4,grupos).
partido(italy,poland,1,2,grupos).
partido(east_germany,brazil,0,1,grupos).
partido(argentina,netherlands,0,4,grupos).
partido(netherlands,east_germany,2,0,grupos).
partido(brazil,argentina,2,1,grupos).
partido(east_germany,argentina,1,1,grupos).
partido(brazil,netherlands,0,2,grupos).
partido(west_germany,yugoslavia,2,0,grupos).
partido(poland,sweden,1,0,grupos).
partido(sweden,west_germany,2,4,grupos).
partido(yugoslavia,poland,1,2,grupos).
partido(west_germany,poland,1,0,grupos).
partido(yugoslavia,sweden,1,2,grupos).
partido(poland,brazil,1,0,tercero).
partido(west_germany,netherlands,2,1,final).
partido(hungary,argentina,1,2,grupos).
partido(france,italy,1,2,grupos).
partido(france,argentina,1,2,grupos).
partido(hungary,italy,1,3,grupos).
partido(italy,argentina,1,0,grupos).
partido(hungary,france,1,3,grupos).
partido(poland,west_germany,0,0,grupos).
partido(mexico,tunisia,1,3,grupos).
partido(tunisia,poland,0,1,grupos).
partido(mexico,west_germany,0,6,grupos).
partido(mexico,poland,1,3,grupos).
partido(tunisia,west_germany,0,0,grupos).
partido(spain,austria,1,2,grupos).
partido(sweden,brazil,1,1,grupos).
partido(sweden,austria,0,1,grupos).
partido(spain,brazil,0,0,grupos).
partido(sweden,spain,0,1,grupos).
partido(austria,brazil,0,1,grupos).
partido(scotland,peru,1,3,grupos).
partido(iran,netherlands,0,3,grupos).
partido(iran,scotland,1,1,grupos).
partido(peru,netherlands,0,0,grupos).
partido(iran,peru,1,4,grupos).
partido(netherlands,scotland,2,3,grupos).
partido(west_germany,italy,0,0,grupos).
partido(netherlands,austria,5,1,grupos).
partido(austria,italy,0,1,grupos).
partido(west_germany,netherlands,2,2,grupos).
partido(netherlands,italy,2,1,grupos).
partido(west_germany,austria,2,3,grupos).
partido(poland,argentina,0,2,grupos).
partido(brazil,peru,3,0,grupos).
partido(brazil,argentina,0,0,grupos).
partido(poland,peru,1,0,grupos).
partido(peru,argentina,0,6,grupos).
partido(brazil,poland,3,1,grupos).
partido(italy,brazil,1,2,tercero).
partido(argentina,netherlands,4,2,final).
partido(poland,italy,0,0,grupos).
partido(cameroon,peru,0,0,grupos).
partido(peru,italy,1,1,grupos).
partido(cameroon,poland,0,0,grupos).
partido(peru,poland,1,5,grupos).
partido(cameroon,italy,1,1,grupos).
partido(algeria,west_germany,2,1,grupos).
partido(austria,chile,1,0,grupos).
partido(chile,west_germany,1,4,grupos).
partido(austria,algeria,2,0,grupos).
partido(chile,algeria,2,3,grupos).
partido(austria,west_germany,0,1,grupos).
partido(belgium,argentina,1,0,grupos).
partido(el_salvador,hungary,1,10,grupos).
partido(hungary,argentina,1,4,grupos).
partido(el_salvador,belgium,0,1,grupos).
partido(hungary,belgium,1,1,grupos).
partido(el_salvador,argentina,0,2,grupos).
partido(france,england,1,3,grupos).
partido(kuwait,czechoslovakia,1,1,grupos).
partido(czechoslovakia,england,0,2,grupos).
partido(kuwait,france,1,4,grupos).
partido(czechoslovakia,france,1,1,grupos).
partido(kuwait,england,0,1,grupos).
partido(honduras,spain,1,1,grupos).
partido(northern_ireland,yugoslavia,0,0,grupos).
partido(yugoslavia,spain,1,2,grupos).
partido(northern_ireland,honduras,1,1,grupos).
partido(yugoslavia,honduras,1,0,grupos).
partido(northern_ireland,spain,1,0,grupos).
partido(soviet_union,brazil,1,2,grupos).
partido(new_zealand,scotland,2,5,grupos).
partido(scotland,brazil,1,4,grupos).
partido(new_zealand,soviet_union,0,3,grupos).
partido(scotland,soviet_union,2,2,grupos).
partido(new_zealand,brazil,0,4,grupos).
partido(belgium,poland,0,3,grupos).
partido(soviet_union,belgium,1,0,grupos).
partido(poland,soviet_union,0,0,grupos).
partido(england,west_germany,0,0,grupos).
partido(spain,west_germany,1,2,grupos).
partido(england,spain,0,0,grupos).
partido(argentina,italy,1,2,grupos).
partido(brazil,argentina,3,1,grupos).
partido(brazil,italy,2,3,grupos).
partido(france,austria,1,0,grupos).
partido(northern_ireland,austria,2,2,grupos).
partido(france,northern_ireland,4,1,grupos).
partido(italy,poland,2,0,semifinales).
partido(france,west_germany,8,9,semifinales).
partido(france,poland,2,3,tercero).
partido(west_germany,italy,1,3,final).
partido(italy,bulgaria,1,1,grupos).
partido(south_korea,argentina,1,3,grupos).
partido(argentina,italy,1,1,grupos).
partido(bulgaria,south_korea,1,1,grupos).
partido(italy,south_korea,3,2,grupos).
partido(bulgaria,argentina,0,2,grupos).
partido(mexico,belgium,2,1,grupos).
partido(iraq,paraguay,0,1,grupos).
partido(paraguay,mexico,1,1,grupos).
partido(belgium,iraq,2,1,grupos).
partido(mexico,iraq,1,0,grupos).
partido(belgium,paraguay,2,2,grupos).
partido(france,canada,1,0,grupos).
partido(hungary,soviet_union,0,6,grupos).
partido(soviet_union,france,1,1,grupos).
partido(canada,hungary,0,2,grupos).
partido(france,hungary,3,0,grupos).
partido(canada,soviet_union,0,2,grupos).
partido(brazil,spain,1,0,grupos).
partido(northern_ireland,algeria,1,1,grupos).
partido(algeria,brazil,0,1,grupos).
partido(spain,northern_ireland,2,1,grupos).
partido(brazil,northern_ireland,3,0,grupos).
partido(spain,algeria,3,0,grupos).
partido(west_germany,uruguay,1,1,grupos).
partido(denmark,scotland,1,0,grupos).
partido(scotland,west_germany,1,2,grupos).
partido(uruguay,denmark,1,6,grupos).
partido(west_germany,denmark,0,2,grupos).
partido(uruguay,scotland,0,0,grupos).
partido(poland,morocco,0,0,grupos).
partido(england,portugal,0,1,grupos).
partido(morocco,england,0,0,grupos).
partido(portugal,poland,0,1,grupos).
partido(morocco,portugal,3,1,grupos).
partido(poland,england,0,3,grupos).
partido(bulgaria,mexico,0,2,octavos).
partido(belgium,soviet_union,6,5,octavos).
partido(uruguay,argentina,0,1,octavos).
partido(poland,brazil,0,4,octavos).
partido(france,italy,2,0,octavos).
partido(west_germany,morocco,1,0,octavos).
partido(paraguay,england,0,3,octavos).
partido(spain,denmark,5,1,octavos).
partido(france,brazil,6,5,cuartos).
partido(mexico,west_germany,1,4,cuartos).
partido(belgium,spain,7,6,cuartos).
partido(england,argentina,1,2,cuartos).
partido(belgium,argentina,0,2,semifinales).
partido(west_germany,france,2,0,semifinales).
partido(belgium,france,4,6,tercero).
partido(west_germany,argentina,2,3,final).
partido(austria,italy,0,1,grupos).
partido(czechoslovakia,united_states,5,1,grupos).
partido(united_states,italy,0,1,grupos).
partido(czechoslovakia,austria,1,0,grupos).
partido(czechoslovakia,italy,0,2,grupos).
partido(united_states,austria,1,2,grupos).
partido(cameroon,argentina,1,0,grupos).
partido(romania,soviet_union,2,0,grupos).
partido(soviet_union,argentina,0,2,grupos).
partido(romania,cameroon,1,2,grupos).
partido(romania,argentina,1,1,grupos).
partido(soviet_union,cameroon,4,0,grupos).
partido(sweden,brazil,1,2,grupos).
partido(scotland,costa_rica,0,1,grupos).
partido(costa_rica,brazil,0,1,grupos).
partido(scotland,sweden,2,1,grupos).
partido(scotland,brazil,0,1,grupos).
partido(costa_rica,sweden,2,1,grupos).
partido(colombia,united_arab_emirates,2,0,grupos).
partido(yugoslavia,west_germany,1,4,grupos).
partido(colombia,yugoslavia,0,1,grupos).
partido(united_arab_emirates,west_germany,1,5,grupos).
partido(colombia,west_germany,1,1,grupos).
partido(united_arab_emirates,yugoslavia,1,4,grupos).
partido(south_korea,belgium,0,2,grupos).
partido(spain,uruguay,0,0,grupos).
partido(uruguay,belgium,1,3,grupos).
partido(spain,south_korea,3,1,grupos).
partido(spain,belgium,2,1,grupos).
partido(uruguay,south_korea,1,0,grupos).
partido(ireland,england,1,1,grupos).
partido(egypt,netherlands,1,1,grupos).
partido(netherlands,england,0,0,grupos).
partido(egypt,ireland,0,0,grupos).
partido(egypt,england,0,1,grupos).
partido(netherlands,ireland,1,1,grupos).
partido(colombia,cameroon,1,2,octavos).
partido(costa_rica,czechoslovakia,1,4,octavos).
partido(argentina,brazil,1,0,octavos).
partido(netherlands,west_germany,1,2,octavos).
partido(romania,ireland,4,5,octavos).
partido(uruguay,italy,0,2,octavos).
partido(yugoslavia,spain,3,2,octavos).
partido(belgium,england,0,1,octavos).
partido(argentina,yugoslavia,3,2,cuartos).
partido(ireland,italy,0,1,cuartos).
partido(czechoslovakia,west_germany,0,1,cuartos).
partido(cameroon,england,4,5,cuartos).
partido(argentina,italy,6,5,semifinales).
partido(england,west_germany,4,5,semifinales).
partido(england,italy,1,2,tercero).
partido(argentina,west_germany,0,1,final).
partido(switzerland,united_states,1,1,grupos).
partido(romania,colombia,3,1,grupos).
partido(switzerland,romania,4,1,grupos).
partido(colombia,united_states,1,2,grupos).
partido(romania,united_states,1,0,grupos).
partido(colombia,switzerland,2,0,grupos).
partido(sweden,cameroon,2,2,grupos).
partido(russia,brazil,0,2,grupos).
partido(cameroon,brazil,0,3,grupos).
partido(russia,sweden,1,3,grupos).
partido(cameroon,russia,1,6,grupos).
partido(sweden,brazil,1,1,grupos).
partido(bolivia,germany,0,1,grupos).
partido(south_korea,spain,2,2,grupos).
partido(spain,germany,1,1,grupos).
partido(bolivia,south_korea,0,0,grupos).
partido(spain,bolivia,3,1,grupos).
partido(south_korea,germany,2,3,grupos).
partido(greece,argentina,0,4,grupos).
partido(bulgaria,nigeria,0,3,grupos).
partido(nigeria,argentina,1,2,grupos).
partido(bulgaria,greece,4,0,grupos).
partido(nigeria,greece,2,0,grupos).
partido(bulgaria,argentina,2,0,grupos).
partido(ireland,italy,1,0,grupos).
partido(mexico,norway,0,1,grupos).
partido(norway,italy,0,1,grupos).
partido(ireland,mexico,1,2,grupos).
partido(norway,ireland,0,0,grupos).
partido(mexico,italy,1,1,grupos).
partido(morocco,belgium,0,1,grupos).
partido(saudi_arabia,netherlands,1,2,grupos).
partido(netherlands,belgium,0,1,grupos).
partido(morocco,saudi_arabia,1,2,grupos).
partido(netherlands,morocco,2,1,grupos).
partido(saudi_arabia,belgium,1,0,grupos).
partido(belgium,germany,2,3,octavos).
partido(switzerland,spain,0,3,octavos).
partido(sweden,saudi_arabia,3,1,octavos).
partido(argentina,romania,2,3,octavos).
partido(ireland,netherlands,0,2,octavos).
partido(united_states,brazil,0,1,octavos).
partido(italy,nigeria,3,2,octavos).
partido(bulgaria,mexico,5,3,octavos).
partido(spain,italy,1,2,cuartos).
partido(brazil,netherlands,3,2,cuartos).
partido(germany,bulgaria,1,2,cuartos).
partido(sweden,romania,8,7,cuartos).
partido(italy,bulgaria,2,1,semifinales).
partido(brazil,sweden,1,0,semifinales).
partido(bulgaria,sweden,0,4,tercero).
partido(italy,brazil,2,3,final).
partido(scotland,brazil,1,2,grupos).
partido(norway,morocco,2,2,grupos).
partido(norway,scotland,1,1,grupos).
partido(morocco,brazil,0,3,grupos).
partido(morocco,scotland,3,0,grupos).
partido(norway,brazil,2,1,grupos).
partido(austria,cameroon,1,1,grupos).
partido(chile,italy,2,2,grupos).
partido(austria,chile,1,1,grupos).
partido(cameroon,italy,0,3,grupos).
partido(austria,italy,1,2,grupos).
partido(cameroon,chile,1,1,grupos).
partido(denmark,saudi_arabia,1,0,grupos).
partido(south_africa,france,0,3,grupos).
partido(saudi_arabia,france,0,4,grupos).
partido(denmark,south_africa,1,1,grupos).
partido(denmark,france,1,2,grupos).
partido(saudi_arabia,south_africa,2,2,grupos).
partido(bulgaria,paraguay,0,0,grupos).
partido(nigeria,spain,3,2,grupos).
partido(bulgaria,nigeria,0,1,grupos).
partido(paraguay,spain,0,0,grupos).
partido(bulgaria,spain,1,6,grupos).
partido(paraguay,nigeria,3,1,grupos).
partido(belgium,netherlands,0,0,grupos).
partido(mexico,south_korea,3,1,grupos).
partido(south_korea,netherlands,0,5,grupos).
partido(mexico,belgium,2,2,grupos).
partido(south_korea,belgium,1,1,grupos).
partido(mexico,netherlands,2,2,grupos).
partido(iran,yugoslavia,0,1,grupos).
partido(united_states,germany,0,2,grupos).
partido(yugoslavia,germany,2,2,grupos).
partido(iran,united_states,2,1,grupos).
partido(iran,germany,0,2,grupos).
partido(yugoslavia,united_states,1,0,grupos).
partido(colombia,romania,0,1,grupos).
partido(tunisia,england,0,2,grupos).
partido(tunisia,colombia,0,1,grupos).
partido(england,romania,1,2,grupos).
partido(tunisia,romania,1,1,grupos).
partido(england,colombia,2,0,grupos).
partido(croatia,jamaica,3,1,grupos).
partido(japan,argentina,0,1,grupos).
partido(croatia,japan,1,0,grupos).
partido(jamaica,argentina,0,5,grupos).
partido(jamaica,japan,2,1,grupos).
partido(croatia,argentina,0,1,grupos).
partido(chile,brazil,1,4,octavos).
partido(norway,italy,0,1,octavos).
partido(denmark,nigeria,4,1,octavos).
partido(paraguay,france,0,1,octavos).
partido(mexico,germany,1,2,octavos).
partido(yugoslavia,netherlands,1,2,octavos).
partido(england,argentina,7,8,octavos).
partido(croatia,romania,1,0,octavos).
partido(france,italy,4,3,cuartos).
partido(denmark,brazil,2,3,cuartos).
partido(croatia,germany,3,0,cuartos).
partido(argentina,netherlands,1,2,cuartos).
partido(netherlands,brazil,4,6,semifinales).
partido(croatia,france,1,2,semifinales).
partido(croatia,netherlands,2,1,tercero).
partido(france,brazil,3,0,final).
partido(senegal,france,1,0,grupos).
partido(denmark,uruguay,2,1,grupos).
partido(uruguay,france,0,0,grupos).
partido(senegal,denmark,1,1,grupos).
partido(france,denmark,0,2,grupos).
partido(uruguay,senegal,3,3,grupos).
partido(south_africa,paraguay,2,2,grupos).
partido(slovenia,spain,1,3,grupos).
partido(paraguay,spain,1,3,grupos).
partido(slovenia,south_africa,0,1,grupos).
partido(spain,south_africa,3,2,grupos).
partido(paraguay,slovenia,3,1,grupos).
partido(turkey,brazil,1,2,grupos).
partido(costa_rica,china,2,0,grupos).
partido(china,brazil,0,4,grupos).
partido(turkey,costa_rica,1,1,grupos).
partido(brazil,costa_rica,5,2,grupos).
partido(china,turkey,0,3,grupos).
partido(poland,south_korea,0,2,grupos).
partido(portugal,united_states,2,3,grupos).
partido(united_states,south_korea,1,1,grupos).
partido(poland,portugal,0,4,grupos).
partido(south_korea,portugal,1,0,grupos).
partido(united_states,poland,1,3,grupos).
partido(cameroon,ireland,1,1,grupos).
partido(saudi_arabia,germany,0,8,grupos).
partido(ireland,germany,1,1,grupos).
partido(saudi_arabia,cameroon,0,1,grupos).
partido(germany,cameroon,2,0,grupos).
partido(ireland,saudi_arabia,3,0,grupos).
partido(sweden,england,1,1,grupos).
partido(nigeria,argentina,0,1,grupos).
partido(nigeria,sweden,1,2,grupos).
partido(england,argentina,1,0,grupos).
partido(argentina,sweden,1,1,grupos).
partido(england,nigeria,0,0,grupos).
partido(mexico,croatia,1,0,grupos).
partido(ecuador,italy,0,2,grupos).
partido(croatia,italy,2,1,grupos).
partido(ecuador,mexico,1,2,grupos).
partido(italy,mexico,1,1,grupos).
partido(croatia,ecuador,0,1,grupos).
partido(belgium,japan,2,2,grupos).
partido(tunisia,russia,0,2,grupos).
partido(russia,japan,0,1,grupos).
partido(belgium,tunisia,1,1,grupos).
partido(japan,tunisia,2,0,grupos).
partido(russia,belgium,2,3,grupos).
partido(paraguay,germany,0,1,octavos).
partido(england,denmark,3,0,octavos).
partido(senegal,sweden,3,2,octavos).
partido(ireland,spain,4,5,octavos).
partido(united_states,mexico,2,0,octavos).
partido(belgium,brazil,0,2,octavos).
partido(turkey,japan,1,0,octavos).
partido(italy,south_korea,2,3,octavos).
partido(brazil,england,2,1,cuartos).
partido(united_states,germany,0,1,cuartos).
partido(south_korea,spain,5,3,cuartos).
partido(turkey,senegal,1,0,cuartos).
partido(south_korea,germany,0,1,semifinales).
partido(turkey,brazil,0,1,semifinales).
partido(turkey,south_korea,3,2,tercero).
partido(brazil,germany,2,0,final).
partido(costa_rica,germany,2,4,grupos).
partido(ecuador,poland,2,0,grupos).
partido(poland,germany,0,1,grupos).
partido(costa_rica,ecuador,0,3,grupos).
partido(germany,ecuador,3,0,grupos).
partido(poland,costa_rica,2,1,grupos).
partido(paraguay,england,0,1,grupos).
partido(sweden,trinidad_and_tobago,0,0,grupos).
partido(trinidad_and_tobago,england,0,2,grupos).
partido(paraguay,sweden,0,1,grupos).
partido(england,sweden,2,2,grupos).
partido(trinidad_and_tobago,paraguay,0,2,grupos).
partido(cote_divoire,argentina,1,2,grupos).
partido(netherlands,serbia,1,0,grupos).
partido(serbia,argentina,0,6,grupos).
partido(cote_divoire,netherlands,1,2,grupos).
partido(argentina,netherlands,0,0,grupos).
partido(serbia,cote_divoire,2,3,grupos).
partido(iran,mexico,1,3,grupos).
partido(portugal,angola,1,0,grupos).
partido(angola,mexico,0,0,grupos).
partido(iran,portugal,0,2,grupos).
partido(mexico,portugal,1,2,grupos).
partido(angola,iran,1,1,grupos).
partido(ghana,italy,0,2,grupos).
partido(czech_republic,united_states,3,0,grupos).
partido(united_states,italy,1,1,grupos).
partido(ghana,czech_republic,2,0,grupos).
partido(italy,czech_republic,2,0,grupos).
partido(united_states,ghana,1,2,grupos).
partido(croatia,brazil,0,1,grupos).
partido(japan,australia,1,3,grupos).
partido(australia,brazil,0,2,grupos).
partido(croatia,japan,0,0,grupos).
partido(brazil,japan,4,1,grupos).
partido(australia,croatia,2,2,grupos).
partido(switzerland,france,0,0,grupos).
partido(togo,south_korea,1,2,grupos).
partido(south_korea,france,1,1,grupos).
partido(switzerland,togo,2,0,grupos).
partido(france,togo,2,0,grupos).
partido(south_korea,switzerland,0,2,grupos).
partido(ukraine,spain,0,4,grupos).
partido(saudi_arabia,tunisia,2,2,grupos).
partido(tunisia,spain,1,3,grupos).
partido(ukraine,saudi_arabia,4,0,grupos).
partido(spain,saudi_arabia,1,0,grupos).
partido(tunisia,ukraine,0,1,grupos).
partido(sweden,germany,0,2,octavos).
partido(mexico,argentina,2,3,octavos).
partido(ecuador,england,0,1,octavos).
partido(netherlands,portugal,0,1,octavos).
partido(australia,italy,0,1,octavos).
partido(ukraine,switzerland,3,0,octavos).
partido(ghana,brazil,0,3,octavos).
partido(france,spain,3,1,octavos).
partido(argentina,germany,4,6,cuartos).
partido(ukraine,italy,0,3,cuartos).
partido(portugal,england,3,1,cuartos).
partido(france,brazil,1,0,cuartos).
partido(italy,germany,2,0,semifinales).
partido(france,portugal,1,0,semifinales).
partido(portugal,germany,1,3,tercero).
partido(france,italy,5,7,final).
partido(mexico,south_africa,1,1,grupos).
partido(france,uruguay,0,0,grupos).
partido(uruguay,south_africa,3,0,grupos).
partido(mexico,france,2,0,grupos).
partido(uruguay,mexico,1,0,grupos).
partido(south_africa,france,2,1,grupos).
partido(greece,south_korea,0,2,grupos).
partido(nigeria,argentina,0,1,grupos).
partido(south_korea,argentina,1,4,grupos).
partido(nigeria,greece,1,2,grupos).
partido(south_korea,nigeria,2,2,grupos).
partido(argentina,greece,2,0,grupos).
partido(united_states,england,1,1,grupos).
partido(slovenia,algeria,1,0,grupos).
partido(united_states,slovenia,2,2,grupos).
partido(algeria,england,0,0,grupos).
partido(algeria,united_states,0,1,grupos).
partido(england,slovenia,1,0,grupos).
partido(ghana,serbia,1,0,grupos).
partido(australia,germany,0,4,grupos).
partido(serbia,germany,1,0,grupos).
partido(australia,ghana,1,1,grupos).
partido(serbia,australia,1,2,grupos).
partido(germany,ghana,1,0,grupos).
partido(denmark,netherlands,0,2,grupos).
partido(cameroon,japan,0,1,grupos).
partido(japan,netherlands,0,1,grupos).
partido(denmark,cameroon,2,1,grupos).
partido(japan,denmark,3,1,grupos).
partido(netherlands,cameroon,2,1,grupos).
partido(paraguay,italy,1,1,grupos).
partido(slovakia,new_zealand,1,1,grupos).
partido(paraguay,slovakia,2,0,grupos).
partido(new_zealand,italy,1,1,grupos).
partido(italy,slovakia,2,3,grupos).
partido(new_zealand,paraguay,0,0,grupos).
partido(portugal,cote_divoire,0,0,grupos).
partido(north_korea,brazil,1,2,grupos).
partido(cote_divoire,brazil,1,3,grupos).
partido(north_korea,portugal,0,7,grupos).
partido(brazil,portugal,0,0,grupos).
partido(cote_divoire,north_korea,3,0,grupos).
partido(chile,honduras,1,0,grupos).
partido(switzerland,spain,1,0,grupos).
partido(switzerland,chile,0,1,grupos).
partido(honduras,spain,0,2,grupos).
partido(spain,chile,2,1,grupos).
partido(honduras,switzerland,0,0,grupos).
partido(south_korea,uruguay,1,2,octavos).
partido(ghana,united_states,3,2,octavos).
partido(england,germany,1,4,octavos).
partido(mexico,argentina,1,3,octavos).
partido(slovakia,netherlands,1,2,octavos).
partido(chile,brazil,0,3,octavos).
partido(japan,paraguay,3,5,octavos).
partido(portugal,spain,0,1,octavos).
partido(brazil,netherlands,1,2,cuartos).
partido(ghana,uruguay,4,6,cuartos).
partido(germany,argentina,4,0,cuartos).
partido(spain,paraguay,1,0,cuartos).
partido(netherlands,uruguay,3,2,semifinales).
partido(spain,germany,1,0,semifinales).
partido(germany,uruguay,3,2,tercero).
partido(spain,netherlands,1,0,final).
