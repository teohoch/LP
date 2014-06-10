;;ARCHIVO MESSAGES.MUD
;;Especifica los mensajes disponibles en el juego.
;;Estas líneas son comentarios. TODAS LAS LÍNEAS que NO
;;sean de mensaje o vacías DEBEN comenzar con doble punto y coma.

;;MESSAGE_GAME_INIT
0x01 Bienvenido a "Jugando con barro", el MUD del siglo XXI!

;;MESSAGE_GAME_EXIT
;;Parámetro: razón de término de la partida.
0x02 Partida terminada: %s. Gracias por jugar!

;;MESSAGE_GAME_ERROR
;;Parámetros: acción intentada y razón de la falla (ej.: "derecha" y "no hay casillas libres en la dirección indicada").
0x03 No se puede realizar la acción "%s" (%s).

;;MESSAGE_GAME_ERROR_INVENTORY
;;Parámetro: nombre del objeto especificado.
0x04 %s no existe en el inventario

;;MESSAGE_GAME_INVENTORY_FULL
;;Parámetro: nombre del objeto a guardar.
0x05 No se puede guardar %s; el inventario esta lleno.

;;MESSAGE_GAME_EXIT_USER
0x06 has cerrado la aplicación

;;MESSAGE_GAME_EXIT_DEFEAT
0x07 tu personaje fue derrotado

;;MESSAGE_GAME_EXIT_VICTORY_ENEMIES
0x08 has eliminado a todos los enemigos del tablero

;;MESSAGE_GAME_EXIT_VICTORY_CELLS
0x09 has explorado todo el tablero

;;MESSAGE_GAME_ERROR_COMMAND
0x0A el comando indicado no existe

;;MESSAGE_GAME_ERROR_MOVE
0x0B no hay casillas libres en la dirección indicada

;;MESSAGE_GAME_MOVE
;;Parámetros: nombre del jugador, cantidad de casillas desplazadas y dirección del movimiento.
0x0C %s se ha movido %s %s.

;;Direcciones de movimiento.
;;MESSAGE_GAME_MOVE_UP
0x0D al frente
;;MESSAGE_GAME_MOVE_DOWN
0x0E hacia atrás
;;MESSAGE_GAME_MOVE_LEFT
0x0F a la izquierda
;;MESSAGE_GAME_MOVE_RIGHT
0x10 a la derecha

;;Cantidad de movimiento.
;;MESSAGE_GAME_MOVE_ONE
0x11 una casilla
;;MESSAGE_GAME_MOVE_TWO
0x12 dos casillas

;;MESSAGE_GAME_MOVE_TYPE_RUN
;;Parámetro: nombre del jugador.
0x13 %s ahora corre.

;;MESSAGE_GAME_MOVE_TYPE_WALK
;;Parámetro: nombre del jugador.
0x14 %s ahora camina.

;;MESSAGE_GAME_ERROR_RUN
;;Parámetro: nombre del jugador.
0x15 %s ya se encuentra corriendo

;;MESSAGE_GAME_ERROR_WALK
;;Parámetro: nombre del jugador.
0x16 %s ya se encuentra caminando

;;MESSAGE_GAME_MOVE_RUN_END
;;Parámetro: nombre del jugador.
0x17 %s ha dejado de correr y debe descansar.

;;MESSAGE_GAME_ERROR_RUN_COOLDOWN
;;Parámetro: nombre del jugador.
0x18 %s debe descansar antes de volver a correr.

;;MESSAGE_GAME_MOVE_COOLDOWN_END
;;Parámetro: nombre del jugador.
0x19 %s está descansado y puede volver a correr.

;;MESSAGE_GAME_ERROR_ARGUMENT
0x1A se necesita un segundo argumento

;;MESSAGE_GAME_INVENTORY_EMPTY
;;Parámetro: nombre del jugador.
0x1B El inventario de %s está vacío.

;;MESSAGE_GAME_INVENTORY_CONTENT
;;Parámetro: nombre del jugador, string con los datos de TODOS los objetos en el inventario.
0x1C %s tiene %s en su inventario.

;;MESSAGE_GAME_INVENTORY_CONTENT_ITEM
;;Parámetro: nombre del objeto, tipo del objeto, valor de su efecto.
0x1D %s (%s, %d) 

;;Tipos de objeto.
;;MESSAGE_GAME_INVENTORY_CONTENT_TYPE_WEAPON
0x1E Arma
;;MESSAGE_GAME_INVENTORY_CONTENT_TYPE_ARMOR
0x1F Armadura
;;MESSAGE_GAME_INVENTORY_CONTENT_TYPE_HEALING
0x20 Curativo
;;MESSAGE_GAME_INVENTORY_CONTENT_TYPE_DEFENSE
0x21 Defensivo
;;MESSAGE_GAME_INVENTORY_CONTENT_TYPE_OFFENSE
0x22 Ofensivo

;;MESSAGE_GAME_CONCATENATOR;;Parámetros: dos String a concatenar (para unificar formato).
0x23 %s, %s

;;MESSAGE_GAME_ERROR_TYPE_EQUIP
;;Parámetro: nombre del objeto
0x24 %s no es un arma o una armadura, por lo que no puede equiparse

;;MESSAGE_GAME_ERROR_CELL_ITEM
;;Parámetro: nombre del objeto especificado.
0x25 %s no existe en esta casilla

;;MESSAGE_GAME_CELL_ITEM_EMPTY
0x26 En esta casilla no hay objetos por tomar.

;;MESSAGE_GAME_CELL_ITEM_CONTENT
;;Parámetro: nombre del jugador, string con los datos de TODOS los objetos en la casilla.
0x27 Los objetos que %s puede ver: %s.

;;MESSAGE_GAME_CELL_ENEMY_EMPTY
0x28 En esta casilla no hay enemigos presentes.

;;MESSAGE_GAME_CELL_ENEMY_CONTENT
;;Parámetro: nombre del jugador, string con los nombres de TODOS los enemigos en la casilla.
0x29 Los enemigos que %s advierte: %s.

;;MESSAGE_GAME_CELL_MOVEMENT_OPTIONS
;;Parámetro: nombre del jugador, TODAS las direcciones hacia las que se encuentra al menos una casilla libre.
0x2A Desde esta casilla, %s puede moverse %s.

;;MESSAGE_GAME_CELL_MOVEMENT_EMPTY
;;Parámetro: nombre del jugador
0x2B Desde esta casilla, %s no puede moverse en dirección alguna.

;;MESSAGE_GAME_OR_CONCATENATOR
;;Parámetros: dos String a concatenar (para unificar formato) - se usa en caso de concatenación de elementos excluyentes, antes del último elemento (ej.: "izquierda, derecha o arriba").
0x2C %s o %s

;;MESSAGE_GAME_ERROR_TYPE_USE
;;Parámetro: nombre del objeto
0x2D %s es un arma o una armadura, por lo que no puede usarse

;;MESSAGE_GAME_ITEM_HEAL_FULL
;;Parámetro: nombre del jugador
0x2E ¡%s tiene todos sus puntos de salud!

;;MESSAGE_GAME_ITEM_HEAL
;;Parámetros: nombre del jugador, nombre de objeto curativo, delta HP y HP final.
0x2F %s usó %s, recuperó %d puntos de impacto y quedó con un total de %d HP.

;;MESSAGE_GAME_ITEM_DEFENSE_FULL
;;Parámetro: nombre del jugador
0x30 ¡%s ya está bajo los efectos de un objeto defensivo!

;;MESSAGE_GAME_ITEM_DEFENSE
;;Parámetros: nombre del jugador, nombre del objeto, valor de la bonificación de defensa
0x31 %s usó %s (+%d a su defensa por 3 turnos).

;;MESSAGE_GAME_ATTACK_PLAYER 
;;Parámetros: nombre del jugador, nombre del arma/objeto usado, nombre del enemigo atacado.
0x32 %s usó %s contra %s.

;;MESSAGE_GAME_ATTACK_DAMAGE
;;Parámetros: nombre del atacado (jugador o enemigo), cantidad de puntos de daño recibidos.
0x33 %s recibió %d puntos de daño.

;;MESSAGE_GAME_ATTACK_FAINT
;;Parámetros: nombre del enemigo/jugador cuya salud llegó a 0 HP.
0x34 ¡%s fue derrotado!

;;MESSAGE_GAME_ATTACK_ENEMY
;;Parámetros: nombre del enemigo atacante, nombre del jugador, cantidad de daño causado.
0x35 %s atacó a %s y le causó %d puntos de daño.

;;MESSAGE_GAME_XP_EARN
;;Parámetros: nombre del jugador, cantidad de puntos de experiencia ganados.
0x36 %s ganó %d puntos de experiencia.

;;MESSAGE_GAME_LEVEL_UP
;;Parámetros: nombre del jugador, nuevo máximo de puntos de impacto, nueva capacidad máxima del inventario.
0x37 ¡%s subió al nivel %d! (%d puntos de impacto máximos, %d espacios totales en inventario).

;;MESSAGE_GAME_ATTACK_NONE
;;Parámetros: nombre del jugador, nombre del objeto ofensivo utilizado.
0x38 %s usa %s, pero no surte efecto, ¡no hay enemigos a los que dañar!

;;MESSAGE_GAME_ERROR_ATTACK
0x39 en esta casilla no hay un enemigo llamado %s

;;MESSAGE_GAME_ITEM_DROP
;;Parámetro: nombre del jugador, nombre del objeto.
0x3A %s se deshizo de %s.

;;MESSAGE_GAME_ITEM_PICK
;;Parámetro: nombre del jugador, nombre del objeto.
0x3B %s tomó %s y lo colocó en su inventario.

;;MESSAGE_GAME_EQUIP_WEAPON
;;Parámetro: nombre del jugador, nombre del objeto.
0x3C %s ahora está usando %s como su arma.

;;MESSAGE_GAME_EQUIP_ARMOR
;;Parámetro: nombre del jugador, nombre del objeto.
0x3D %s ahora está usando %s como su armadura.

;;MESSAGE_GAME_GENERIC_ERROR
0x99 error
