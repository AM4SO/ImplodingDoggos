package gameServer;

import java.io.Serializable;
import java.util.ArrayList;

public class HandState implements Serializable{
	private static final long serialVersionUID = 1L;
	public ArrayList<CardState> cards;
}