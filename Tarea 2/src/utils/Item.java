package utils;

import java.awt.Point;
/**
 * Clase contenedora para Objetos, con nombre tipo, valor de efecto y localizacion dentro del mapa.
 * @author teohoch
 *
 */
public class Item
{
	/**
	 * @param name Nombre del Item
	 * @param type Tipo de Item
	 * @param value Valor del Efecto del Item
	 * @param location Punto dentro del Mapa donde el item se encuentra, si es (-1,-1) esta en el inventario del usuario
	 */
	public Item(String name, char type, int value, Point location) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.location = location;
	}
	private String name;
	private char type;
	private int value;
	private Point location;
	
	public void setLocation(Point location) {
		this.location = location;
	}	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}
	/**
	 * @return the type
	 */
	public char getType() {
		return type;
	}

}
