;;ARCHIVO MESSAGES.MUD
;;Especifica los mensajes disponibles en el juego.
;;Estas líneas son comentarios. TODAS LAS LÍNEAS que NO
;;sean de mensaje o vacías DEBEN comenzar con doble punto y coma.


;;MESSAGE_GAME_ERROR
;;Parámetros: acción intentada y razón de la falla (ej.: "derecha" y "no hay casillas libres en la dirección indicada").
0x03 No se puede realizar la acción "%s" (%s).

;;MESSAGE_GAME_ERROR_INVENTORY
;;Parámetro: nombre del objeto especificado.
0x04 %s no existe en el inventario

;;MESSAGE_GAME_ERROR_COMMAND
0x0A el comando indicado no existe

;;MESSAGE_GAME_ERROR_MOVE
0x0B no hay casillas libres en la dirección indicada

;;MESSAGE_GAME_ERROR_RUN
;;Parámetro: nombre del jugador.
0x15 %s ya se encuentra corriendo

;;MESSAGE_GAME_ERROR_WALK
;;Parámetro: nombre del jugador.
0x16 %s ya se encuentra caminando

;;MESSAGE_GAME_ERROR_RUN_COOLDOWN
;;Parámetro: nombre del jugador.
0x18 %s debe descansar antes de volver a correr.

;;MESSAGE_GAME_ERROR_ARGUMENT
0x1A se necesita un segundo argumento

;;MESSAGE_GAME_ERROR_TYPE_EQUIP
;;Parámetro: nombre del objeto
0x24 %s no es un arma o una armadura, por lo que no puede equiparse

;;MESSAGE_GAME_ERROR_CELL_ITEM
;;Parámetro: nombre del objeto especificado.
0x25 %s no existe en esta casilla

;;MESSAGE_GAME_ERROR_TYPE_USE
;;Parámetro: nombre del objeto
0x2D %s es un arma o una armadura, por lo que no puede usarse

;;MESSAGE_GAME_ITEM_HEAL_FULL
;;Parámetro: nombre del jugador
0x2E ¡%s tiene todos sus puntos de salud!

;;MESSAGE_GAME_ITEM_DEFENSE_FULL
;;Parámetro: nombre del jugador
0x30 ¡%s ya está bajo los efectos de un objeto defensivo!

;;MESSAGE_GAME_ERROR_ATTACK
0x39 en esta casilla no hay un enemigo llamado %s

;;MESSAGE_GAME_GENERIC_ERROR
0x99 error
